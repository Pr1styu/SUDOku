package hu.bme.compsec.sudoku.caffservice.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDTO {

    private String text;

    private String username;

}
