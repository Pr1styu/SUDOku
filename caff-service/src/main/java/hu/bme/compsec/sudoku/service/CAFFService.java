package hu.bme.compsec.sudoku.service;

import hu.bme.compsec.sudoku.common.exception.CaffFileFormatExpression;
import hu.bme.compsec.sudoku.data.CAFFRepository;
import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.service.processor.CaffProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CAFFService {

    private CAFFRepository caffRepository;


    @PreAuthorize("hasAuthority('caff:read')")
    public List<CAFFFile> getAllCaffFile() {
        log.info("Fetching all caff files.");
        return caffRepository.findAll();
    }

    @PreAuthorize("hasAuthority('caff:read')")
    public Optional<CAFFFile> getCaffFileById(Long id) {
        log.info("Fetching CAFF file with id: {}", id);
        return caffRepository.findById(id);
    }

    @PreAuthorize("hasAuthority('caff:write')")
    public CAFFFile saveCaffFile(MultipartFile uploadedCaffFile, String clientFileName) {

        // TODO: Process raw CAFF file with native component

        var caffFileEntity = new CAFFFile();
        caffFileEntity.setFileName(clientFileName);

        var processor = new CaffProcessor();
        try {
            processor.process(uploadedCaffFile, clientFileName);
            //TODO: Load generated preview (jpeg file) and metadata (txt)
            caffFileEntity.setPreview(processor.getPreview());
            caffFileEntity.setMetaData(processor.getMetaData());
        } catch (CaffFileFormatExpression e) {
            return null;
        }

        try {
            caffFileEntity.setRawBytes(uploadedCaffFile.getBytes());
        } catch (IOException e) {
            log.error("Error while getting raw bytes of uploaded CAFF file: {}", e.getMessage());
        }

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

    @PreAuthorize("hasAuthority('caff:read')")
    public List<CAFFFile> searchCaffFilesByMetaData(String metaData) {
        return caffRepository.findAllByMetaDataIgnoreCase(metaData);
    }


}