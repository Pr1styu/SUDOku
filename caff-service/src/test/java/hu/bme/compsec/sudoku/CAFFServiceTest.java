package hu.bme.compsec.sudoku;

import hu.bme.compsec.sudoku.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.data.CAFFRepository;
import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.helper.CaffFileHelper;
import hu.bme.compsec.sudoku.service.CAFFService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SecurityTestExecutionListeners
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {CAFFService.class, CAFFRepository.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class CAFFServiceTest {

    @Autowired
    private CAFFService caffService;

    @MockBean
    public CAFFRepository caffRepository;

    @TestConfiguration
    class CAFFServiceTestConfiguration {
        @Bean
        public CAFFService caffService() {
            return new CAFFService(caffRepository);
        }
    }

    @Before
    public void setup() {
        caffRepository = Mockito.mock(CAFFRepository.class);

        CaffFileHelper helper = new CaffFileHelper();
        String[] fileNames = new String[] {"1.caff", "2.caff"};

        Arrays.asList(fileNames).forEach(file -> {
            int id = Integer.parseInt(Character.toString(file.charAt(0)));

            try {
                Mockito.when(caffRepository.findById((long) id)).thenReturn(Optional.ofNullable(helper.loadCaffFile(fileNames[id - 1])));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CaffFileFormatException e) {
                e.printStackTrace();
            }
        });

        ArrayList<CAFFFile> caffFiles = new ArrayList<>();
        Arrays.asList(fileNames).forEach(file -> {
            try {
                caffFiles.add(helper.loadCaffFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CaffFileFormatException e) {
                e.printStackTrace();
            }
        });

        Mockito.when(caffRepository.findAll()).thenReturn(caffFiles);
    }

    @Test
    public void testFindAll() {
        caffService = new CAFFService(caffRepository);

        List<CAFFFile> caffFiles = caffService.getAllCaffFile();

        assertThat(caffFiles.size()).isEqualTo(2);
    }

    @Test
    public void testFindById() {
        caffService = new CAFFService(caffRepository);

        Optional<CAFFFile> caff = caffService.getCaffFileById(1L);
        assertThat(caff.isPresent()).isTrue();
        assertThat(caff.get().getFileName()).isEqualTo("1.caff");
    }

    @Test
    public void testMetaData() {
        caffService = new CAFFService(caffRepository);

        Optional<CAFFFile> caff = caffService.getCaffFileById(1L);
        assertThat(caff.isPresent()).isTrue();

        List<String> metaData = Arrays.asList("sunset", "landscape", "mountains");
        assertThat(new HashSet<>(caff.get().getMetaData())).isEqualTo(new HashSet<>(metaData));
    }
}
