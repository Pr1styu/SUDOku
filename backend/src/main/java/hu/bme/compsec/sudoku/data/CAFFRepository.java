package hu.bme.compsec.sudoku.data;

import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CAFFRepository extends JpaRepository<CAFFFile, Long> {

}