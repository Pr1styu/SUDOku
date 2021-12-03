package hu.bme.compsec.sudoku.caffservice.service;

import hu.bme.compsec.sudoku.caffservice.data.CAFFRepository;
import hu.bme.compsec.sudoku.caffservice.data.domain.Comment;
import hu.bme.compsec.sudoku.caffservice.data.CommentRepository;
import hu.bme.compsec.sudoku.caffservice.presentation.dto.CommentDTO;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

            var comment = new Comment();
            comment.setText(commentDTO.getText());
//            comment.setUserId(auth.getDetails());
            comment.setUsername(getAuthenticatedUserName());
            caffFile.addComment(comment);

            caffRepository.save(caffFile);

            return true;
        } else {
            return false;
        }
    }

    private String getAuthenticatedUserName() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }


}
