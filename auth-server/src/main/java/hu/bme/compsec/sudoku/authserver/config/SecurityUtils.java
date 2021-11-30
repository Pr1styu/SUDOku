package hu.bme.compsec.sudoku.authserver.config;

import hu.bme.compsec.sudoku.authserver.common.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.stream.Collectors;

@Slf4j
public class SecurityUtils {

    public static boolean checkUserId(String userId) {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            var jwt = (Jwt) auth.getPrincipal();
            var jwtUserId = jwt.getClaim("user_id");
            // TODO: Clean these once we use UUIDs
            log.info(String.valueOf(jwt));
            log.info("Check: {}", String.valueOf(jwtUserId).equals(userId));
            return String.valueOf(jwtUserId).equals(userId);
        } catch (Exception e) {
            return false;
        }
    }

//    public static void checkOwnerEntitlement(Optiona) {
//        log.info("Checking owner for resource {}.", );
//        var entitled = .map(value -> checkUserId(String.valueOf(value.getId())))
//                .orElse(true); // TODO: Consider this breach
//
//        if (!entitled) {
//            if (!isAuthenticatedUserContentCreator()) {
//                throw new AccessDeniedException(String.format("User not have the right permission for resource %s.", ));
//            }
//        }
//    }

    public static Long getUserIdFromJwt() {
        try {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            var jwt = (Jwt) auth.getPrincipal();

            return jwt.getClaim("user_id");
        } catch (Exception e) {
            throw new AccessDeniedException("Cannot parse JWT.");
        }
    }

    public static boolean isAuthenticatedUserAdmin() {
        var authorities = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities().stream()
                .map(Object::toString)
                .collect(Collectors.toList());

        return authorities.stream()
                .anyMatch(authority -> authority.equals("ROLE_" + UserRole.ADMIN.name()));
    }

}
