package hu.bme.compsec.sudoku.data;

import hu.bme.compsec.sudoku.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.data.CAFFRepository;
import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.helper.CaffFileHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
public class CAFFRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CAFFRepository caffRepository;

    CaffFileHelper helper = new CaffFileHelper();

    @Before
    public void initRepository() throws CaffFileFormatException, IOException {
        entityManager.clear();
        String[] fileNames = new String[] {"1.caff", "2.caff", "3.caff"};
        for (String file : fileNames) {
            CAFFFile caff = helper.loadCaffFile(file);
            entityManager.persist(caff);
            entityManager.flush();
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
    public void testInsert() throws CaffFileFormatException, IOException {
        caffRepository.save(helper.loadCaffFile("3.caff"));
        assertThat(caffRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    public void testFindByMetadata() {
        List<CAFFFile> result = caffRepository.findAllByMetaDataIgnoreCase("sunset");
        assertThat(result.size()).isEqualTo(3);
    }
}
