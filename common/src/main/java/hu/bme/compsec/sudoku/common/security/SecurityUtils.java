package hu.bme.compsec.sudoku.common.security;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

    public static final String USERID_CLAIM = "user_id";
    public static final String AUTHORITIES_CLAIM = "authorities";


    public static Long getUserIdFromJwt() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            var jwt = (Jwt) auth.getPrincipal();

            return jwt.getClaim(USERID_CLAIM);
        } catch (Exception e) {
            log.error("Cannot parse JWT from authentication principle!");
            throw new AccessDeniedException("Cannot parse JWT.");
        }
    }

    /**
     * Check the actual user permission for user/resource operation.
     * Note that the admin role also pass all request.
     * @param userId The id of the given user or the resource owner under the requested operation.
     */
    public static void checkPermission(Long userId) {
        var jwtUserId = getUserIdFromJwt();
        // TODO: Fix these once we use UUIDs
        if (Objects.equals(userId, jwtUserId)) {
            return;
        } else {
            if (isAuthenticatedUserAdmin()) {
                return;
            }
        }

        throw new AccessDeniedException(
                String.format("User with id %d does NOT have permission for resource/profile related to userId %d.",jwtUserId, userId));
    }

    private static boolean isAuthenticatedUserAdmin() {
        var authorities = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        return authorities.stream()
                .anyMatch(authority -> authority.equals("ROLE_" + UserRole.ADMIN.name()));
    }

}