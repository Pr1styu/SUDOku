package hu.bme.compsec.sudoku.presentation.dto;

import lombok.Data;

import java.util.List;

@Data
public class CAFFFileDetailDTO extends CAFFFilePreviewDTO {

    private List<CommentDTO> comments;

}
