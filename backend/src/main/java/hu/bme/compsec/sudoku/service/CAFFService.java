package hu.bme.compsec.sudoku.service;

import hu.bme.compsec.sudoku.data.CAFFRepository;
import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CAFFService {

    private CAFFRepository caffRepository;

    @PreAuthorize("hasAuthority('caff:read')")
    public Optional<CAFFFile> getCAFFFileById(Long id) {
        log.info("Fetching CAFF file with id: {}", id);
        return caffRepository.findById(id);
    }

    @PreAuthorize("hasAuthority('caff:write')")
    public CAFFFile saveCaffFile(MultipartFile uploadedCaffFile, String fileName) {

        // TODO: Process raw CAFF file with native component


        var caffFileEntity = new CAFFFile();
        caffFileEntity.setFileName(fileName);

        try {
            caffFileEntity.setRawBytes(uploadedCaffFile.getBytes());
        } catch (IOException e) {
            log.error("Error while getting raw bytes of uploaded CAFF file: {}", e.getMessage());
        }

        //TODO: Load generated preview (jpeg file) and metadata (txt)
        caffFileEntity.setPreview(null);
        caffFileEntity.setMetaData(null);

        return caffRepository.save(caffFileEntity);
    }


    /**
     * This method responsible for the Caff file deletion.
     * @return true if the entity has been successfully deleted, false if it does not exist in the database.
     */
    @PreAuthorize("hasAuthority('caff:delete')")
    public boolean deleteCaffFile(Long id) {
        if (caffRepository.existsById(id)) {
            caffRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

}