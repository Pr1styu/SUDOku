package hu.bme.compsec.sudoku.common.config.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum UserRole {

    USER(Set.of(UserPermission.CAFF_READ)),
    ADMIN(Set.of(UserPermission.CAFF_READ, UserPermission.CAFF_WRITE, UserPermission.CAFF_DELETE));

    private final Set<UserPermission> permissions;


    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> authorities = permissions.stream()
                .map(userPermission -> new SimpleGrantedAuthority(userPermission.getPermissionText()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return authorities;
    }

}
