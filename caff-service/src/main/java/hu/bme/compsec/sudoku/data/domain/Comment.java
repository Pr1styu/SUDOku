package hu.bme.compsec.sudoku.data.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

    @NotBlank
    private String username;

    @ManyToOne
    @ToString.Exclude
    private CAFFFile caffFile;

    private String text;

    private Timestamp creationTime;

    @PrePersist
    public void onCreate() {
        this.creationTime = new Timestamp(Instant.now().toEpochMilli());
    }

}
