package hu.bme.compsec.sudoku.presentation.mapping;

import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.data.domain.Comment;
import hu.bme.compsec.sudoku.presentation.dto.CAFFFileDetailDTO;
import hu.bme.compsec.sudoku.presentation.dto.CAFFFilePreviewDTO;
import hu.bme.compsec.sudoku.presentation.dto.CommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface CAFFMapper {

    @Mapping(target = "size", source = "rawBytes")
    CAFFFilePreviewDTO toPreviewDTO(CAFFFile entity);

    @Mapping(target = "size", source = "rawBytes")
    CAFFFileDetailDTO toDetailDTO(CAFFFile entity);

    default String getBase64EncodedString(byte[] preview) {
        return Base64.getEncoder().encodeToString(preview);
    }

    default Integer setCaffFileSize(byte[] rawBytes) {
        if(rawBytes != null) {
            return rawBytes.length;
        }
        else{
            return null;
        }
    }

    CommentDTO toCommentDTO(Comment comment);

}
