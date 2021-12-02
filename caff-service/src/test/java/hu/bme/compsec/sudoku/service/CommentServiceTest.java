package hu.bme.compsec.sudoku.service;

import hu.bme.compsec.sudoku.common.exception.CAFFProcessorRuntimeException;
import hu.bme.compsec.sudoku.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.config.TestSecurityConfig;
import hu.bme.compsec.sudoku.data.CAFFRepository;
import hu.bme.compsec.sudoku.data.CommentRepository;
import hu.bme.compsec.sudoku.data.domain.Comment;
import hu.bme.compsec.sudoku.helper.CaffFileHelper;
import hu.bme.compsec.sudoku.presentation.dto.CommentDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SecurityTestExecutionListeners
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSecurityConfig.class)
@ContextConfiguration(classes = {CommentService.class, CommentRepository.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    public CommentRepository commentRepository;

    @MockBean
    public CAFFRepository caffRepository;

    final CaffFileHelper helper = new CaffFileHelper();

    @BeforeAll
    public void setup() throws CaffFileFormatException, IOException, CAFFProcessorRuntimeException {
        commentRepository = Mockito.mock(CommentRepository.class);
        caffRepository = Mockito.mock(CAFFRepository.class);
        commentService = new CommentService(caffRepository, commentRepository);

        for (String file : helper.getAllFileNames()) {
            int id = Integer.parseInt(Character.toString(file.charAt(0)));
            Mockito.when(caffRepository.findById((long) id)).thenReturn(Optional.ofNullable(helper.loadAllCaffFiles().get(id - 1)));
            Mockito.when(caffRepository.existsById((long) id)).thenReturn(true);
        }
        Mockito.when(caffRepository.findAll()).thenReturn(helper.loadAllCaffFiles());

        ArrayList<Comment> comments = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            comments.add(Comment.builder()
                    .caffFile(helper.loadCaffFile("1.caff"))
                    .text("Test comment" + i)
                    .userId(1L)
                    .username("admin")
                    .build()
            );
        }
        Mockito.when(commentRepository.findAllByCaffFileId(1L)).thenReturn(comments);
    }

    @Test
    public void testGetALlCommentsForCaffFile() {
        List<Comment> comments = commentService.getAllCommentForCaffFile(1L);
        assertThat(comments.size()).isEqualTo(3);
    }

    /*@Test
    void testAddCommentToCaffFile() {
        boolean result = commentService.addCommentToCaffFile(1L, new CommentDTO("Test comment4", "admin"));
        //TODO: getAuthenticatedUserName() throws NullPointerException
        assertThat(result).isTrue();
    }*/

    @Test
    public void testTryAddCommentToNonExistingCaffFile() {
        boolean result = commentService.addCommentToCaffFile(4L, new CommentDTO("Test comment4", "admin"));
        assertThat(result).isFalse();
    }
}
