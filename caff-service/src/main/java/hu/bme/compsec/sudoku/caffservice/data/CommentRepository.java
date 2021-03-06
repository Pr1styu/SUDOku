package hu.bme.compsec.sudoku.caffservice.data;

import hu.bme.compsec.sudoku.caffservice.data.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByCaffFileId(Long id);
}
