package hu.bme.compsec.sudoku.presentation.dto;

import lombok.Data;

@Data
public class CAFFPreviewDTO {

    private Long id;

    private String fileName;

    // First CIFF file in Base64 string
    private String preview;

}
