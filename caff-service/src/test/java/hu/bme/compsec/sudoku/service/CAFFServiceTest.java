package hu.bme.compsec.sudoku.service;

import hu.bme.compsec.sudoku.common.config.security.UserRole;
import hu.bme.compsec.sudoku.common.exception.CAFFProcessorRuntimeException;
import hu.bme.compsec.sudoku.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.common.exception.CaffFileNotFoundException;
import hu.bme.compsec.sudoku.config.TestSecurityConfig;
import hu.bme.compsec.sudoku.data.CAFFRepository;
import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.helper.CaffFileHelper;
import hu.bme.compsec.sudoku.service.processor.CaffProcessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestSecurityConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CAFFServiceTest {

    private CAFFService caffService;

    public CAFFRepository caffRepository;

    @BeforeAll
    public void setup() throws CaffFileFormatException, IOException, CAFFProcessorRuntimeException, InterruptedException {
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
            } catch (IOException | CaffFileFormatException | CAFFProcessorRuntimeException e) {
                e.printStackTrace();
            }
        });

        ArrayList<CAFFFile> caffFiles = new ArrayList<>();
        Arrays.asList(fileNames).forEach(file -> {
            try {
                caffFiles.add(helper.loadCaffFile(file));
            } catch (IOException | CaffFileFormatException | CAFFProcessorRuntimeException e) {
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
        assertThat(caff).isPresent();
        assertThat(caff.get().getFileName()).isEqualTo("1.caff");
    }

    @Test
    public void testMetaData() {
        Optional<CAFFFile> caff = caffService.getCaffFileById(1L);
        assertThat(caff).isPresent();

        List<String> metaData = Arrays.asList("sunset", "landscape", "mountains");
        assertThat(new HashSet<>(caff.get().getMetaData())).isEqualTo(new HashSet<>(metaData));
    }

    @Test
    void testCRUD() throws IOException, CaffFileNotFoundException {

        List<CAFFFile> caffFiles = caffService.getAllCaffFile();
        assertThat(caffFiles.size()).isEqualTo(2);

        CaffFileHelper helper = new CaffFileHelper();
        MultipartFile file = helper.loadMultipartFile("3.caff");
        caffService.saveCaffFile(file, "3.caff");

        caffFiles = caffService.getAllCaffFile();
        assertThat(caffFiles.size()).isEqualTo(3);


        caffService.deleteCaffFile(3L);
        caffService.deleteCaffFile(1L);
        caffFiles = caffService.getAllCaffFile();
        assertThat(caffFiles.size()).isEqualTo(1);
    }

    @Test
    void testSearchByMetaData() throws IOException {
        var adminUserId = 1L;

        TestSecurityConfig.mockAuthenticatedUserId(adminUserId);
        TestSecurityConfig.mockAuthWithUserRoleAndId(UserRole.ADMIN);

        for (int i = 1; i <= 3; i++) {
            CaffFileHelper helper = new CaffFileHelper();
            String fileName = i + ".caff";
            MultipartFile file = helper.loadMultipartFile(fileName);
            caffService.saveCaffFile(file, fileName);
        }

        List<CAFFFile> found = caffService.searchCaffFilesByMetaData("sunset");
        assertThat(found.size()).isEqualTo(3);
    }

    private long getRandomId() {
        return new Random().nextInt(100);
    }

    @Test
    public void shouldDeleteOwnFileAsAdmin() throws Exception {
        // Mock JWT
        var adminsFileId = getRandomId();
        var adminUserId = getRandomId();

        // Order is important as we should set the USER_ID claim value before we generate the JWT
        TestSecurityConfig.mockAuthenticatedUserId(adminUserId); // The JWT will contain the USER_ID claim with this mock value
        TestSecurityConfig.mockAuthWithUserRoleAndId(UserRole.ADMIN); // The JWT will contain ADMIN's authorities (=roles + permissions)

        // Mock Caff File entity with proper id and ownerId values
        var mockCaffFileEntity = Optional.of(
                CAFFFile.builder()
                        .id(adminsFileId)
                        .ownerId(adminUserId)
                        .build()
        );

        // Mock the repository to add back the proper entity
        Mockito.when(caffRepository.findById(adminsFileId)).thenReturn(mockCaffFileEntity);

        // Call the service level method
        caffService.deleteCaffFile(adminsFileId);
    }

    @Test
    public void shouldDeleteOthersFileAsAdmin() throws Exception {
        var otherFileId = getRandomId();
        var otherUserId = getRandomId();
        var adminUserId = getRandomId();

        TestSecurityConfig.mockAuthenticatedUserId(adminUserId);
        TestSecurityConfig.mockAuthWithUserRoleAndId(UserRole.ADMIN);

        var mockCaffFileEntity = Optional.of(
                CAFFFile.builder()
                        .id(otherFileId)
                        .ownerId(otherUserId)
                        .build()
        );

        Mockito.when(caffRepository.findById(otherFileId)).thenReturn(mockCaffFileEntity);

        caffService.deleteCaffFile(otherFileId);
    }

    @Test
    public void shouldDeleteOwnFile() throws Exception {
        var fileId = getRandomId();
        var userId = getRandomId();


        TestSecurityConfig.mockAuthenticatedUserId(userId);
        TestSecurityConfig.mockAuthWithUserRoleAndId(UserRole.USER);

        var mockCaffFileEntity = Optional.of(
                CAFFFile.builder()
                        .id(fileId)
                        .ownerId(userId)
                        .build()
        );

        Mockito.when(caffRepository.findById(fileId)).thenReturn(mockCaffFileEntity);

        caffService.deleteCaffFile(fileId);
    }

    @Test
    public void shouldFailOnDeleteOthersFile() {
        var userId = getRandomId();
        var otherFileId = getRandomId();
        var otherUserId = getRandomId();

        TestSecurityConfig.mockAuthenticatedUserId(userId);
        TestSecurityConfig.mockAuthWithUserRoleAndId(UserRole.USER);

        var mockCaffFileEntity = Optional.of(
                CAFFFile.builder()
                        .id(otherFileId)
                        .ownerId(otherUserId)
                        .build()
        );

        Mockito.when(caffRepository.findById(otherFileId)).thenReturn(mockCaffFileEntity);

        Assertions.assertThrows(AccessDeniedException.class, () -> {
            caffService.deleteCaffFile(otherFileId);
        });
    }

    // TODO: Create test for cover CaffFileNotFoundException thrown


}
