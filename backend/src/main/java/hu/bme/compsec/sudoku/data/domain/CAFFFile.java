package hu.bme.compsec.sudoku.data.domain;

import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Slf4j
@Entity
public class CAFFFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: Add validation
    private String fileName;

    @Lob
    @ToString.Exclude
    private byte[] rawBytes;

    @Lob
    @ToString.Exclude
    private byte[] preview; // 'First' processed CIFF file in JPEG format

    @ElementCollection
    private List<String> metaData;


    // TODO: Figure out the comment representation
    //private final List<Comment> comments;

    private Timestamp previewGenerationTime;

    private Timestamp creationTime;

    private Timestamp modificationTime;


    @PrePersist
    public void onCreate() {
        this.creationTime = this.modificationTime = new Timestamp(Instant.now().toEpochMilli());

        // TODO: REMOVE THIS
        this.preview = UUID.randomUUID().toString().getBytes(StandardCharsets.UTF_8);
        this.metaData = List.of("test", "hodl", "kaposzta", "hello", "hohoho", "hullaho");



        log.info("Saving CAFF file: {}", this);
    }

    @PreUpdate
    public void onModify() {
        this.modificationTime = new Timestamp(Instant.now().toEpochMilli());
        // TODO: How to make sure the preview generation was successful?

        log.info("Updating CAFF file: {}", this);
    }

}
