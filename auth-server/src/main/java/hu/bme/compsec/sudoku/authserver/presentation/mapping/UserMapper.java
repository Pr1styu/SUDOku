package hu.bme.compsec.sudoku.authserver.presentation.mapping;

import hu.bme.compsec.sudoku.authserver.config.SecurityUser;
import hu.bme.compsec.sudoku.authserver.data.User;
import hu.bme.compsec.sudoku.authserver.presentation.dto.UserDTO;
import hu.bme.compsec.sudoku.authserver.presentation.dto.UserProfileDTO;
import hu.bme.compsec.sudoku.authserver.presentation.dto.UserType;
import hu.bme.compsec.sudoku.common.security.UserRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User toEntity(UserDTO dto);

    SecurityUser toSecurityUser(User userEntity);

    @Mapping(source = "authorities", target = "userType", qualifiedByName = "getUserType")
    UserProfileDTO toProfileDTO(User entity);

    @Named("getUserType")
    default UserType getUserType(Collection<GrantedAuthority> authorities) {
        var authorityNames = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        if (authorityNames.contains("ROLE_" + UserRole.ADMIN.name())) {
            return UserType.ADMIN;
        }

        return UserType.USER;
    }

}
