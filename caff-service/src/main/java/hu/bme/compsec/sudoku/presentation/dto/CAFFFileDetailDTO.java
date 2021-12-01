package hu.bme.compsec.sudoku.presentation.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
//@Builder
public class CAFFFileDetailDTO extends CAFFFilePreviewDTO {

    private List<String> metaData;

    private List<CommentDTO> comments;

}
