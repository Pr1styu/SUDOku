package hu.bme.compsec.sudoku.authserver.presentation.mapping;

import hu.bme.compsec.sudoku.authserver.config.SecurityUser;
import hu.bme.compsec.sudoku.authserver.data.User;
import hu.bme.compsec.sudoku.authserver.presentation.dto.UserDTO;
import hu.bme.compsec.sudoku.authserver.presentation.dto.UserProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserMapper {

    UserDTO toDTO(User entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "password", qualifiedBy = EncodedMapping.class)
    User toEntity(UserDTO dto);

    SecurityUser toSecurityUser(User userEntity);

    UserProfileDTO toProfileDTO(User entity);

}
