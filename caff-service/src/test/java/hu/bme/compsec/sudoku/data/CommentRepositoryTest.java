package hu.bme.compsec.sudoku.data;

import hu.bme.compsec.sudoku.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.data.domain.Comment;
import hu.bme.compsec.sudoku.helper.CaffFileHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
public class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CAFFRepository caffRepository;

    final CaffFileHelper helper = new CaffFileHelper();
    Long caffId = 0L;
    Long commentId = 0L;

    @Before
    public void initRepository() throws CaffFileFormatException, IOException {
        CAFFFile saved = caffRepository.saveAndFlush(helper.loadCaffFile("1.caff"));
        CAFFFile other = caffRepository.saveAndFlush(helper.loadCaffFile("2.caff"));
        caffId = saved.getId();

        commentId = commentRepository.saveAndFlush(
                Comment.builder()
                        .caffFile(saved)
                        .text("Test comment1")
                        .userId(1L)
                        .username("admin")
                        .build()
        ).getId();

        commentRepository.saveAndFlush(
                Comment.builder()
                        .caffFile(other)
                        .text("Test comment2")
                        .userId(1L)
                        .username("admin")
                        .build()
        );

        commentRepository.saveAndFlush(
                Comment.builder()
                        .caffFile(saved)
                        .text("Test comment3")
                        .userId(1L)
                        .username("admin")
                        .build()
        );
    }

    @Test
    void testFindByText() {
        Comment found = null;
        if(commentRepository.findById(commentId).isPresent()) {
            found = commentRepository.findById(commentId).get();
        }
        assert found != null;
        assertThat(found.getText()).isEqualTo("Test comment1");
    }

    @Test
    void testFindAll() {
        List<Comment> caffFiles = commentRepository.findAll();
        assertThat(caffFiles.size()).isEqualTo(3);
    }

    @Test
    void testRemove() {
        Comment test = null;
        if(commentRepository.findById(commentId).isPresent()){
             test = commentRepository.findById(commentId).get();
        }
        assert test != null;
        commentRepository.delete(test);
        assertThat(commentRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    void testInsert() {
        if(caffRepository.findById(caffId).isPresent()) {
            commentRepository.saveAndFlush(Comment.builder()
                    .caffFile(caffRepository.findById(caffId).get())
                    .text("Test comment4")
                    .userId(1L)
                    .username("admin")
                    .build());
        }
        assertThat(commentRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    void testFindAllByCaffFileId() {
        List<Comment> comments = commentRepository.findAllByCaffFileId(caffId);
        assertThat(comments.size()).isEqualTo(2);
    }
}
