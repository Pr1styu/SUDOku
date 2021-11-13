package hu.bme.compsec.sudoku.data.domain;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@Entity
public class Comment {

    @Id
    @GeneratedValue
    private Long id;

    private Long userId;

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
