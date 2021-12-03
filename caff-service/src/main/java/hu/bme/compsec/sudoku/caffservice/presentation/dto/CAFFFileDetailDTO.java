package hu.bme.compsec.sudoku.caffservice.presentation.dto;

import lombok.Data;

import java.util.List;

@Data
public class CAFFFileDetailDTO extends CAFFFilePreviewDTO {

    private List<String> metaData;

    private List<CommentDTO> comments;

}
