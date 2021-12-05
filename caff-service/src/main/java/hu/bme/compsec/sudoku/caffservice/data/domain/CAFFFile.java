package hu.bme.compsec.sudoku.caffservice.data.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CAFFFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Lob
    @ToString.Exclude
    private byte[] rawBytes;

    @Lob
    @ToString.Exclude
    private byte[] preview; // 'First' processed CIFF file in JPEG format

    @ElementCollection
    private List<String> metaData;

    private Timestamp creationTime;

    private Timestamp modificationTime;

    @OneToMany(mappedBy = "caffFile", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        comment.setCaffFile(this);
        this.comments.add(comment);
    }

    @PrePersist
    public void onCreate() {
        this.creationTime = this.modificationTime = new Timestamp(Instant.now().toEpochMilli());
        log.debug("Saving CAFF file: {}", this);
    }

    @PreUpdate
    public void onModify() {
        this.modificationTime = new Timestamp(Instant.now().toEpochMilli());
        log.debug("Updating CAFF file: {}", this);
    }

    private Long ownerId;

}
