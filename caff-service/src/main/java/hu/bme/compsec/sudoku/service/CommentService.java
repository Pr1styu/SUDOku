package hu.bme.compsec.sudoku.service;

import hu.bme.compsec.sudoku.data.CAFFRepository;
import hu.bme.compsec.sudoku.data.domain.Comment;
import hu.bme.compsec.sudoku.data.CommentRepository;
import hu.bme.compsec.sudoku.presentation.dto.CommentDTO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
