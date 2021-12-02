package hu.bme.compsec.sudoku.presentation.controller;

import hu.bme.compsec.sudoku.presentation.dto.CAFFFileDetailDTO;
import hu.bme.compsec.sudoku.presentation.dto.CAFFFilePreviewDTO;
import hu.bme.compsec.sudoku.presentation.dto.CommentDTO;
import hu.bme.compsec.sudoku.presentation.mapping.CAFFMapper;
import hu.bme.compsec.sudoku.service.CAFFService;
import hu.bme.compsec.sudoku.service.CommentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200/", maxAge = 3600)
@RequestMapping("/caff")
public class CAFFController {

    private CAFFService caffService;
    private CAFFMapper caffMapper;

    private CommentService commentService;


    @GetMapping("")
    public ResponseEntity<List<CAFFFilePreviewDTO>> getAllCaffFilePreview() {
        var caffFiles = caffService.getAllCaffFile().parallelStream()
                .map(caffMapper::toPreviewDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(caffFiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CAFFFileDetailDTO> getCAFFFile(@PathVariable Long id) {
        return caffService.getCaffFileById(id)
                .map(caffMapper::toDetailDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<CAFFFileDetailDTO> uploadCaffFile(@RequestPart ("caffFile") MultipartFile uploadedCaffFile,
                                                            @RequestPart ("fileName") String clientFileName,
                                                            UriComponentsBuilder b) {
        var createdCaffFileEntity = caffService.saveCaffFile(uploadedCaffFile, FilenameUtils.getBaseName(StringUtils.cleanPath(clientFileName)));
        if (createdCaffFileEntity != null) {
            UriComponents uriComponents = b.path("/{id}").buildAndExpand(createdCaffFileEntity.getId());
            return ResponseEntity.created(uriComponents.toUri()).body(caffMapper.toDetailDTO(createdCaffFileEntity));
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable Long id) {
        return caffService.getCaffFileById(id)
                .map(caffFile -> {
                            Resource resource = new ByteArrayResource(caffFile.getRawBytes());

                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.add("File-Name", caffFile.getFileName());
                            httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
                            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM) // TODO: Check this with frontend
                                    .headers(httpHeaders).body(resource);

                        }
                )
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCaffFile(@PathVariable Long id) {

        // TODO: Check permission for requested file

        if (caffService.deleteCaffFile(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search/{metaData}")
    public ResponseEntity<List<CAFFFilePreviewDTO>> getCaffFilesByMetaData(@PathVariable String metaData) {
        var caffFiles = caffService.searchCaffFilesByMetaData(metaData).parallelStream()
                .map(caffMapper::toPreviewDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(caffFiles);
    }

    /* COMMENTS */

    @GetMapping("/{id}/comment")
    public ResponseEntity<List<CommentDTO>> getCommentForCaffFile(@PathVariable Long id) {
        var comments = commentService.getAllCommentForCaffFile(id).parallelStream()
                .map(caffMapper::toCommentDTO)
                .collect(Collectors.toList());

        // TODO: Improve responses based on more possible scenario
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity addCommentForCaffFile(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {

        log.info("Adding comment '{}' for caff file with id {}.", commentDTO.getText(), id);

        var commentAdded = commentService.addCommentToCaffFile(id, commentDTO);
        if (commentAdded) {
            return ResponseEntity.accepted().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}



