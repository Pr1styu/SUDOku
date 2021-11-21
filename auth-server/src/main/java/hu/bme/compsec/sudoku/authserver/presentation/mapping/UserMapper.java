package hu.bme.compsec.sudoku.authserver.presentation.mapping;

import hu.bme.compsec.sudoku.authserver.data.User;
import hu.bme.compsec.sudoku.authserver.presentation.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    User toEntity(UserDTO dto);

    default org.springframework.security.core.userdetails.User toSecurityUser(User userEntity) {
        return new org.springframework.security.core.userdetails.User(userEntity.getUsername(), userEntity.getPassword(), userEntity.getAuthorities());
    }

}
