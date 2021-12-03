package hu.bme.compsec.sudoku.data;

import hu.bme.compsec.sudoku.caffservice.common.exception.CAFFProcessorRuntimeException;
import hu.bme.compsec.sudoku.caffservice.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.caffservice.data.CAFFRepository;
import hu.bme.compsec.sudoku.caffservice.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.helper.CaffFileHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CAFFRepositoryTest {

    @Autowired
    private CAFFRepository caffRepository;

    final CaffFileHelper helper = new CaffFileHelper();

    @BeforeAll
    public void initRepository() throws CaffFileFormatException, IOException, CAFFProcessorRuntimeException {
        caffRepository.deleteAll();
        String[] fileNames = new String[] {"1.caff", "2.caff", "3.caff"};
        for (String file : fileNames) {
            CAFFFile caff = helper.loadCaffFile(file);
            caffRepository.save(caff);
            caffRepository.flush();
        }
    }

    @Test
    public void testFindByName() {
        CAFFFile found = caffRepository.findByFileName("1.caff");
        assertThat(found.getFileName()).isEqualTo("1.caff");
    }

    @Test
    public void testFindAll() {
        List<CAFFFile> caffFiles = caffRepository.findAll();
        assertThat(caffFiles.size()).isEqualTo(3);
    }

    @Test
    public void testRemove() {
        CAFFFile test = caffRepository.findByFileName("1.caff");
        caffRepository.delete(test);
        assertThat(caffRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    public void testInsert() throws CaffFileFormatException, IOException, CAFFProcessorRuntimeException {
        caffRepository.save(helper.loadCaffFile("3.caff"));
        assertThat(caffRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    public void testFindByMetadata() {
        List<CAFFFile> result = caffRepository.findAllByMetaDataIgnoreCase("sunset");
        assertThat(result.size()).isEqualTo(3);
    }
}
