package hu.bme.compsec.sudoku.data.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
