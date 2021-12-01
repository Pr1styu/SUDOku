package hu.bme.compsec.sudoku.service;

import hu.bme.compsec.sudoku.config.TestSecurityConfig;
import hu.bme.compsec.sudoku.data.CommentRepository;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

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
}
