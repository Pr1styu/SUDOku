package hu.bme.compsec.sudoku.caffservice.service;

import config.TestSecurityConfig;
import hu.bme.compsec.sudoku.caffservice.common.exception.CAFFProcessorRuntimeException;
import hu.bme.compsec.sudoku.caffservice.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.caffservice.common.exception.CaffFileNotFoundException;
import hu.bme.compsec.sudoku.common.security.UserRole;
import hu.bme.compsec.sudoku.caffservice.data.CAFFRepository;
import hu.bme.compsec.sudoku.caffservice.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.caffservice.helper.CaffFileHelper;
import hu.bme.compsec.sudoku.caffservice.service.processor.CaffProcessor;
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

import static config.TestSecurityConfig.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestSecurityConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CAFFServiceTest {

    private CAFFService caffService;

    public CAFFRepository caffRepository;

    private Long[] randomIds;
    private final long adminUserId = 1L;

    @BeforeAll
    public void setup() throws CaffFileFormatException, IOException, CAFFProcessorRuntimeException {
        CaffFileHelper helper = new CaffFileHelper();

        MultipartFile multipart = helper.loadMultipartFile("1.caff");
        CaffProcessor processor = new CaffProcessor();
        processor.process(multipart, "1.caff");
        List<String> metaData = Arrays.asList("sunset", "landscape", "mountains");
        assertThat(new HashSet<>(processor.getMetaData())).isEqualTo(new HashSet<>(metaData));

        caffRepository = Mockito.mock(CAFFRepository.class);
        caffService = new CAFFService(caffRepository);

        String[] fileNames = new String[]{"1.caff", "2.caff", "3.caff"};
        randomIds = new Long[]{getRandomId(), getRandomId(), getRandomId()};

        int idx = 0;
        for (int i = 0; i < fileNames.length; i++) {
            CAFFFile entity = helper.loadCaffFile(fileNames[idx]);
            entity.setOwnerId(adminUserId);

            CAFFFile entityWithId = helper.loadCaffFile(fileNames[idx]);
            entityWithId.setId(randomIds[idx]);
            entityWithId.setOwnerId(adminUserId);

            Mockito.when(caffRepository.save(entity)).thenReturn(entityWithId);
            Mockito.when(caffRepository.findById(randomIds[idx])).thenReturn(Optional.of(entityWithId));

            idx++;
        }

        ArrayList<CAFFFile> caffFiles = new ArrayList<>();
        for (int i = 0; i < fileNames.length - 1; i++) {
            caffFiles.add(helper.loadCaffFile(fileNames[i]));
        }

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
    void testFindById() {
        mockAuthenticatedUserId(getRandomId());
        mockAuthWithUserRoleAndId(UserRole.USER);

        Optional<CAFFFile> caff = caffService.getCaffFileById(randomIds[0]);
        assertThat(caff).isPresent();
        assertThat(caff.get().getFileName()).isEqualTo("1.caff");
    }

    @Test
    void testMetaData() {
        mockAuthenticatedUserId(getRandomId());
        mockAuthWithUserRoleAndId(UserRole.USER);

        Optional<CAFFFile> caff = caffService.getCaffFileById(randomIds[0]);
        assertThat(caff).isPresent();

        List<String> metaData = Arrays.asList("sunset", "landscape", "mountains");
        assertThat(new HashSet<>(caff.get().getMetaData())).isEqualTo(new HashSet<>(metaData));
    }

    @Test
    void testCRUD() throws IOException, CaffFileNotFoundException {
        mockAuthenticatedUserId(adminUserId);
        mockAuthWithUserRoleAndId(UserRole.ADMIN);

        List<CAFFFile> caffFiles = caffService.getAllCaffFile();
        assertThat(caffFiles.size()).isEqualTo(2);

        CaffFileHelper helper = new CaffFileHelper();
        MultipartFile file = helper.loadMultipartFile("3.caff");
        caffService.saveCaffFile(file, "3.caff");

        caffFiles = caffService.getAllCaffFile();
        assertThat(caffFiles.size()).isEqualTo(3);


        caffService.deleteCaffFile(randomIds[2]);
        caffService.deleteCaffFile(randomIds[0]);
        caffFiles = caffService.getAllCaffFile();
        assertThat(caffFiles.size()).isEqualTo(1);
    }

    @Test
    void testSearchByMetaData() throws IOException {
        mockAuthenticatedUserId(adminUserId);
        mockAuthWithUserRoleAndId(UserRole.ADMIN);

        for (int i = 1; i <= 3; i++) {
            CaffFileHelper helper = new CaffFileHelper();
            String fileName = i + ".caff";
            MultipartFile file = helper.loadMultipartFile(fileName);
            caffService.saveCaffFile(file, fileName);
        }

        List<CAFFFile> found = caffService.searchCaffFilesByMetaData("sunset");
        assertThat(found.size()).isEqualTo(3);
    }


    @Test
    void shouldDeleteOwnFileAsAdmin() throws Exception {
        // Mock JWT
        var adminsFileId = getRandomId();
        var adminUserId = getRandomId();

        // Order is important as we should set the USER_ID claim value before we generate the JWT
        mockAuthenticatedUserId(adminUserId); // The JWT will contain the USER_ID claim with this mock value
        mockAuthWithUserRoleAndId(UserRole.ADMIN); // The JWT will contain ADMIN's authorities (=roles + permissions)

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
        assertThat(caffService.deleteCaffFile(adminsFileId)).isTrue();
    }

    @Test
    void shouldDeleteOthersFileAsAdmin() throws Exception {
        var otherFileId = getRandomId();
        var otherUserId = getRandomId();
        var adminUserId = getRandomId();

        mockAuthenticatedUserId(adminUserId);
        mockAuthWithUserRoleAndId(UserRole.ADMIN);

        var mockCaffFileEntity = Optional.of(
                CAFFFile.builder()
                        .id(otherFileId)
                        .ownerId(otherUserId)
                        .build()
        );

        Mockito.when(caffRepository.findById(otherFileId)).thenReturn(mockCaffFileEntity);

        assertThat(caffService.deleteCaffFile(otherFileId)).isTrue();
    }

    @Test
    void shouldDeleteOwnFile() throws Exception {
        var fileId = getRandomId();
        var userId = getRandomId();


        mockAuthenticatedUserId(userId);
        mockAuthWithUserRoleAndId(UserRole.USER);

        var mockCaffFileEntity = Optional.of(
                CAFFFile.builder()
                        .id(fileId)
                        .ownerId(userId)
                        .build()
        );

        Mockito.when(caffRepository.findById(fileId)).thenReturn(mockCaffFileEntity);

        assertThat(caffService.deleteCaffFile(fileId)).isTrue();
    }

    @Test
    void shouldFailOnDeleteOthersFile() {
        var userId = getRandomId();
        var otherFileId = getRandomId();
        var otherUserId = getRandomId();

        mockAuthenticatedUserId(userId);
        mockAuthWithUserRoleAndId(UserRole.USER);

        var mockCaffFileEntity = Optional.of(
                CAFFFile.builder()
                        .id(otherFileId)
                        .ownerId(otherUserId)
                        .build()
        );

        Mockito.when(caffRepository.findById(otherFileId)).thenReturn(mockCaffFileEntity);

        Assertions.assertThrows(AccessDeniedException.class, () -> caffService.deleteCaffFile(otherFileId));
    }

    // TODO: Create test for cover CaffFileNotFoundException thrown

    // TODO: Create test for cover InterruptedException handle

}
