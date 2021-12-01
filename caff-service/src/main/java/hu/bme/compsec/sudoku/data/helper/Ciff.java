package hu.bme.compsec.sudoku.data.helper;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Ciff {
    private int id;
    private int duration;
    private int size;
    private int width;
    private int height;
    private String caption;
    private List<String> tags;
}
