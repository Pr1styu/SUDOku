package hu.bme.compsec.sudoku.caffservice.service;

import config.TestSecurityConfig;
import hu.bme.compsec.sudoku.caffservice.common.exception.CAFFProcessorRuntimeException;
import hu.bme.compsec.sudoku.caffservice.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.caffservice.data.CAFFRepository;
import hu.bme.compsec.sudoku.caffservice.data.CommentRepository;
import hu.bme.compsec.sudoku.caffservice.data.domain.Comment;
import hu.bme.compsec.sudoku.caffservice.helper.CaffFileHelper;
import hu.bme.compsec.sudoku.caffservice.presentation.dto.CommentDTO;
import hu.bme.compsec.sudoku.common.security.UserRole;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static config.TestSecurityConfig.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestSecurityConfig.class)
@ContextConfiguration(classes = {CommentService.class, CommentRepository.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;

    @MockBean
    private CAFFRepository caffRepository;

    final CaffFileHelper helper = new CaffFileHelper();

    @BeforeEach
    public void setup() throws CaffFileFormatException, IOException, CAFFProcessorRuntimeException {
        commentRepository = Mockito.mock(CommentRepository.class);
        caffRepository = Mockito.mock(CAFFRepository.class);
        commentService = new CommentService(caffRepository, commentRepository);

        for (String file : helper.getAllFileNames()) {
            int id = Integer.parseInt(Character.toString(file.charAt(0)));
            Mockito.when(caffRepository.findById((long) id)).thenReturn(Optional.ofNullable(helper.loadAllCaffFiles().get(id - 1)));
            Mockito.when(caffRepository.getById((long) id)).thenReturn(helper.loadAllCaffFiles().get(id - 1));
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

    @BeforeEach
    public void mockJWT() {
        var userId = getRandomId();
        mockAuthenticatedUserId(userId);
        mockAuthWithUserRoleAndId(UserRole.USER);
    }

    @Test
    void testGetAllCommentsForCaffFile() {
        List<Comment> comments = commentService.getAllCommentForCaffFile(1L);
        assertThat(comments.size()).isEqualTo(3);

        List<Comment> emptyList = commentService.getAllCommentForCaffFile(2L);
        assertThat(emptyList.size()).isEqualTo(0);
    }

    @Test
    void testAddCommentToCaffFile() {
        boolean result = commentService.addCommentToCaffFile(1L, new CommentDTO("Test comment4", "admin"));
        assertThat(result).isTrue();
    }

    @Test
    void testTryAddCommentToNonExistingCaffFile() {
        boolean result = commentService.addCommentToCaffFile(4L, new CommentDTO("Test comment4", "admin"));
        assertThat(result).isFalse();
    }
}
