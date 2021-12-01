package hu.bme.compsec.sudoku.service;

import hu.bme.compsec.sudoku.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.common.exception.CaffFileNotFoundException;
import hu.bme.compsec.sudoku.config.TestSecurityConfig;
import hu.bme.compsec.sudoku.data.CAFFRepository;
import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.helper.CaffFileHelper;
import hu.bme.compsec.sudoku.service.CAFFService;
import hu.bme.compsec.sudoku.service.processor.CaffProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.annotation.SecurityTestExecutionListeners;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SecurityTestExecutionListeners
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TestSecurityConfig.class)
@ContextConfiguration(classes = {CAFFService.class, CAFFRepository.class})
@TestPropertySource(locations = "classpath:application-test.properties")
public class CAFFServiceTest {

    @Autowired
    private CAFFService caffService;

    @MockBean
    public CAFFRepository caffRepository;

    @Before
    @Test
    public void setup() throws CaffFileFormatException, IOException {
        CaffFileHelper helper = new CaffFileHelper();

        MultipartFile multipart = helper.loadMultipartFile("1.caff");
        CaffProcessor processor = new CaffProcessor();
        processor.process(multipart, "1.caff");
        List<String> metaData = Arrays.asList("sunset", "landscape", "mountains");
        assertThat(new HashSet<>(processor.getMetaData())).isEqualTo(new HashSet<>(metaData));

        caffRepository = Mockito.mock(CAFFRepository.class);
        caffService = new CAFFService(caffRepository);

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

        CAFFFile newFile = helper.loadCaffFile("3.caff");
        ArrayList<CAFFFile> biggerList = new ArrayList<>(caffFiles);
        biggerList.add(newFile);

        ArrayList<CAFFFile> smallerList = new ArrayList<>();
        smallerList.add(helper.loadCaffFile("2.caff"));

        Mockito.when(caffRepository.findAll()).thenReturn(caffFiles).thenReturn(biggerList).thenReturn(smallerList);

        Mockito.when(caffRepository.findAllByMetaDataIgnoreCase("landscape")).thenReturn(biggerList);
        Mockito.when(caffRepository.findAllByMetaDataIgnoreCase("sunset")).thenReturn(biggerList);
        Mockito.when(caffRepository.findAllByMetaDataIgnoreCase("mountains")).thenReturn(biggerList);
    }

    @Test
    public void testFindById() {
        Optional<CAFFFile> caff = caffService.getCaffFileById(1L);
        assertThat(caff.isPresent()).isTrue();
        assertThat(caff.get().getFileName()).isEqualTo("1.caff");
    }

    @Test
    public void testMetaData() {
        Optional<CAFFFile> caff = caffService.getCaffFileById(1L);
        assertThat(caff.isPresent()).isTrue();

        List<String> metaData = Arrays.asList("sunset", "landscape", "mountains");
        assertThat(new HashSet<>(caff.get().getMetaData())).isEqualTo(new HashSet<>(metaData));
    }

 /*   @Test
    public void testCRUD() throws IOException, CaffFileNotFoundException {
        List<CAFFFile> caffFiles = caffService.getAllCaffFile();
        assertThat(caffFiles.size()).isEqualTo(2);

        CaffFileHelper helper = new CaffFileHelper();
        MultipartFile file = helper.loadMultipartFile("3.caff");
        caffService.saveCaffFile(file, "3. caff");

        caffFiles = caffService.getAllCaffFile();
        assertThat(caffFiles.size()).isEqualTo(3);

        caffService.deleteCaffFile(3L);
        caffService.deleteCaffFile(1L);
        caffFiles = caffService.getAllCaffFile();
        assertThat(caffFiles.size()).isEqualTo(1);
    } */

 /*   @Test
    public void testSearchByMetaData() throws IOException {
        for (int i = 1; i <= 3; i++) {
            CaffFileHelper helper = new CaffFileHelper();
            String fileName = i + ".caff";
            MultipartFile file = helper.loadMultipartFile(fileName);
            caffService.saveCaffFile(file, fileName);
        }

        List<CAFFFile> found = caffService.searchCaffFilesByMetaData("sunset");
        assertThat(found.size()).isEqualTo(3);
    }*/
}
