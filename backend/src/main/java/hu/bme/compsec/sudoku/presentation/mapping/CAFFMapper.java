package hu.bme.compsec.sudoku.presentation.mapping;

import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.presentation.dto.CAFFPreviewDTO;
import org.mapstruct.Mapper;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface CAFFMapper {

    CAFFPreviewDTO toPreviewDTO(CAFFFile entity);

    default String getBase64EncodedString(byte[] preview) {
        return Base64.getEncoder().encodeToString(preview);
    }

}
