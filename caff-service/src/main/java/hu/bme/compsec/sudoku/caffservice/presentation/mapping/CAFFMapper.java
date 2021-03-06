package hu.bme.compsec.sudoku.caffservice.presentation.mapping;

import hu.bme.compsec.sudoku.caffservice.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.caffservice.data.domain.Comment;
import hu.bme.compsec.sudoku.caffservice.presentation.dto.CAFFFileDetailDTO;
import hu.bme.compsec.sudoku.caffservice.presentation.dto.CAFFFilePreviewDTO;
import hu.bme.compsec.sudoku.caffservice.presentation.dto.CommentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;

import java.util.Base64;

@Mapper(componentModel = "spring", nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
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
