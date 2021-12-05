package hu.bme.compsec.sudoku.caffservice.service;

import hu.bme.compsec.sudoku.caffservice.data.CAFFRepository;
import hu.bme.compsec.sudoku.caffservice.data.CommentRepository;
import hu.bme.compsec.sudoku.caffservice.data.domain.Comment;
import hu.bme.compsec.sudoku.caffservice.presentation.dto.CommentDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static hu.bme.compsec.sudoku.common.security.SecurityUtils.getUserIdFromJwt;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CommentService {

    private CAFFRepository caffRepository;

    private CommentRepository commentRepository;

    public List<Comment> getAllCommentForCaffFile(Long caffFileId) {
        log.info("Fetching all comment for caff file {} by user with id {}.", caffFileId, getUserIdFromJwt());
        return commentRepository.findAllByCaffFileId(caffFileId);
    }

    public boolean addCommentToCaffFile(Long id, CommentDTO commentDTO) {
        log.info("Trying to add comment for CAFF file with id {} by user with id {}.", id, getUserIdFromJwt());
        if (caffRepository.existsById(id)) {
            var caffFile = caffRepository.getById(id);

            var comment = new Comment();
            comment.setText(commentDTO.getText());
            comment.setUserId(getUserIdFromJwt());
            comment.setUsername(getAuthenticatedUserName());
            caffFile.addComment(comment);

            caffRepository.save(caffFile);
            log.info("Added comment '{}' for caff file with id {} by user with id {}.", commentDTO.getText(), id, getUserIdFromJwt());
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
