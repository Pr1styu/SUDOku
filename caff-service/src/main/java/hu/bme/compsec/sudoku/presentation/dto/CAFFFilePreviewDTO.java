package hu.bme.compsec.sudoku.presentation.dto;

import lombok.Data;

@Data
public class CAFFFilePreviewDTO {

    private Long id;

    private String fileName;

    // First CIFF file in Base64 string
    private String preview;

    private Integer size;

}
