package hu.bme.compsec.sudoku.service;

import hu.bme.compsec.sudoku.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.common.exception.CaffFileNotFoundException;
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
import java.util.List;
import java.util.Optional;

import static hu.bme.compsec.sudoku.common.config.security.SecurityUtils.checkPermissionForCaffFile;
import static hu.bme.compsec.sudoku.common.config.security.SecurityUtils.getUserIdFromJwt;

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
        var caffFileEntity = new CAFFFile();
        caffFileEntity.setFileName(clientFileName);

        var processor = new CaffProcessor();
        try {
            processor.process(uploadedCaffFile, clientFileName);
            caffFileEntity.setPreview(processor.getPreview());
            caffFileEntity.setMetaData(processor.getMetaData());
        } catch (CaffFileFormatException e) {
            log.error("Error while trying to process CAFF file '{}': {}", clientFileName, e.getMessage());
            return null;
        }

        try {
            caffFileEntity.setRawBytes(uploadedCaffFile.getBytes());
        } catch (IOException e) {
            log.error("Error while getting raw bytes of uploaded CAFF file: {}", e.getMessage());
            return null;
        }

        caffFileEntity.setOwnerId(getUserIdFromJwt());

        return caffRepository.save(caffFileEntity);
    }

    /**
     * This method responsible for the Caff file deletion.
     */
    @PreAuthorize("hasAuthority('caff:delete')")
    public void deleteCaffFile(Long id) throws CaffFileNotFoundException {
        var caffFileEntity = caffRepository.findById(id)
                .map(caffFile -> {
                    checkPermissionForCaffFile(caffFile.getOwnerId());
                    return caffFile;
                })
                .orElseThrow(() -> new CaffFileNotFoundException("There is no caff file with id %s.", id));

        caffRepository.delete(caffFileEntity);
    }

    @PreAuthorize("hasAuthority('caff:read')")
    public List<CAFFFile> searchCaffFilesByMetaData(String metaData) {
        return caffRepository.findAllByMetaDataIgnoreCase(metaData);
    }

}