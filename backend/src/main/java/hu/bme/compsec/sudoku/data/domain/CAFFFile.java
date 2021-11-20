package hu.bme.compsec.sudoku.data.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    private Timestamp creationTime;

    private Timestamp modificationTime;

    @OneToMany(mappedBy = "caffFile", cascade = CascadeType.ALL)
    private final List<Comment> comments = new ArrayList<>();

    public Comment addComment(String commentText) {
        var comment = new Comment();
        comment.setCaffFile(this);
        comment.setText(commentText);

        var auth = SecurityContextHolder.getContext().getAuthentication();
        // TODO: Fetch userid with actual username
        log.info(auth.getName());
        comment.setUserId(1l);

        this.comments.add(comment);

        return comment;
    }

    @PrePersist
    public void onCreate() {
        this.creationTime = this.modificationTime = new Timestamp(Instant.now().toEpochMilli());

        // TODO: REMOVE THESE
        {
            this.metaData = List.of("test", "hodl", "kaposzta", "hello", "hohoho", "hullaho");
            addComment("test comment, this is awesome!");
        }

        log.info("Saving CAFF file: {}", this);
    }

    @PreUpdate
    public void onModify() {
        this.modificationTime = new Timestamp(Instant.now().toEpochMilli());
        // TODO: How to make sure the preview generation was successful?

        log.info("Updating CAFF file: {}", this);
    }

}
