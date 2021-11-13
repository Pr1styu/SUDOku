package hu.bme.compsec.sudoku.presentation.controller;

import hu.bme.compsec.sudoku.presentation.dto.CAFFPreviewDTO;
import hu.bme.compsec.sudoku.presentation.mapping.CAFFMapper;
import hu.bme.compsec.sudoku.service.CAFFService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.io.IOException;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/caff")
public class CAFFController {

    private CAFFService caffService;

    private CAFFMapper caffMapper;

    @GetMapping("/{id}")
    public ResponseEntity<CAFFPreviewDTO> getCAFFFile(@PathVariable Long id) {
        return caffService.getCAFFFileById(id)
                .map(caffMapper::toPreviewDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<CAFFPreviewDTO> uploadCaffFile(@RequestPart ("caffFile") MultipartFile uploadedCaffFile,
                                                         @RequestPart ("fileName") String fileName,
                                                         UriComponentsBuilder b) {

        // TODO: Should we check the file format/integrity before persisting?

        var createdCaffFileEntity = caffService.saveCaffFile(uploadedCaffFile, StringUtils.cleanPath(fileName));

        UriComponents uriComponents = b.path("/download/{id}").buildAndExpand(createdCaffFileEntity.getId());
        return ResponseEntity.created(uriComponents.toUri()).body(caffMapper.toPreviewDTO(createdCaffFileEntity));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable Long id) throws IOException {
        return caffService.getCAFFFileById(id)
                .map(caffFile -> {
                            Resource resource = new ByteArrayResource(caffFile.getRawBytes());

                            HttpHeaders httpHeaders = new HttpHeaders();
                            httpHeaders.add("File-Name", caffFile.getFileName());
                            httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
                            return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM)
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

}



