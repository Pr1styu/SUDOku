package hu.bme.compsec.sudoku;

import hu.bme.compsec.sudoku.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.data.CAFFRepository;
import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.helper.CaffFileHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CAFFRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CAFFRepository caffRepository;

    @Test
    public void whenFindById_thenReturnCAFF() throws IOException, CaffFileFormatException {
        CaffFileHelper helper = new CaffFileHelper();
        String fileName = "1.caff";
        CAFFFile caff = helper.loadCaffFile(fileName);

        entityManager.persist(caff);
        entityManager.flush();

        Optional<CAFFFile> found = caffRepository.findById(1L);
        assertThat(found.isPresent()).isTrue();

        assertThat(found.get().getFileName()).isEqualTo(fileName);
    }
}
