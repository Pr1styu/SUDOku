package hu.bme.compsec.sudoku.service;

import hu.bme.compsec.sudoku.data.CAFFRepository;
import hu.bme.compsec.sudoku.data.domain.Comment;
import hu.bme.compsec.sudoku.data.CommentRepository;
import hu.bme.compsec.sudoku.presentation.dto.CommentDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CommentService {

    private CAFFRepository caffRepository;

    private CommentRepository commentRepository;

    public List<Comment> getAllCommentForCaffFile(Long caffFileId) {
        return commentRepository.findAllByCaffFileId(caffFileId);
    }

    public boolean addCommentToCaffFile(Long id, CommentDTO commentDTO) {
        if (caffRepository.existsById(id)) {
            var caffFile = caffRepository.getById(id);

            var commentEntity = caffFile.addComment(commentDTO.getText());

            caffRepository.save(caffFile);

            return true;
        } else {
            return false;
        }
    }


}
