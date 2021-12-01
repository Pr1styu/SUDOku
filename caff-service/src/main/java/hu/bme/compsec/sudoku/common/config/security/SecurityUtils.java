package hu.bme.compsec.sudoku.common.config.security;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Objects;
import java.util.stream.Collectors;

// TODO: Extract this to common module once we have one
@Slf4j
public class SecurityUtils {

    public static final String USERID_CLAIM = "user_id";
    public static final String AUTHORITIES_CLAIM = "authorities";


    public static Long getUserIdFromJwt() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            var jwt = (Jwt) auth.getPrincipal();

            return jwt.getClaim(USERID_CLAIM);
        } catch (Exception e) {
            log.error("Cannot parse JWT from authentication principle: {}", auth.getPrincipal());
            throw new AccessDeniedException("Cannot parse JWT.");
        }
    }

    /**
     * Check the actual user permission and if not match, the admin role also pass the request.
     * @param userId
     */
    public static void checkPermissionForCaffFile(Long userId) {
        var jwtUserId = getUserIdFromJwt();
        // TODO: Fix these once we use UUIDs
        if (!Objects.equals(userId, jwtUserId)) {
            if (!isAuthenticatedUserAdmin())
            throw new AccessDeniedException(
                    String.format("User with id {} does NOT have permission for edit CAFF file with id {}.", jwtUserId, userId)
            );
        }
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