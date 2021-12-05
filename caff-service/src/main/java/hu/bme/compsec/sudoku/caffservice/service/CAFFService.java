package hu.bme.compsec.sudoku.caffservice.service;

import hu.bme.compsec.sudoku.caffservice.common.exception.CAFFProcessorRuntimeException;
import hu.bme.compsec.sudoku.caffservice.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.caffservice.common.exception.CaffFileNotFoundException;
import hu.bme.compsec.sudoku.caffservice.data.CAFFRepository;
import hu.bme.compsec.sudoku.caffservice.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.caffservice.service.processor.CaffProcessor;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static hu.bme.compsec.sudoku.common.security.SecurityUtils.checkPermission;
import static hu.bme.compsec.sudoku.common.security.SecurityUtils.getUserIdFromJwt;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CAFFService {

    private CAFFRepository caffRepository;


    @PreAuthorize("hasAuthority('caff:read')")
    public List<CAFFFile> getAllCaffFile() {
        log.info("Fetching all caff files by user with id {}.", getUserIdFromJwt());
        return caffRepository.findAll();
    }

    @PreAuthorize("hasAuthority('caff:read')")
    public Optional<CAFFFile> getCaffFileById(Long id) {
        log.info("Fetching CAFF file with id {} by user with id {}.", id, getUserIdFromJwt());
        return caffRepository.findById(id);
    }

    @PreAuthorize("hasAuthority('caff:write')")
    public CAFFFile saveCaffFile(MultipartFile uploadedCaffFile, String clientFileName) {
        log.info("Trying to save CAFF file with fileName '{}' by user with id {}.", clientFileName, getUserIdFromJwt());

        var caffFileEntity = new CAFFFile();
        caffFileEntity.setFileName(clientFileName);

        var processor = new CaffProcessor();
        try {
            processor.process(uploadedCaffFile, clientFileName);
            caffFileEntity.setPreview(processor.getPreview());
            caffFileEntity.setMetaData(processor.getMetaData());
        } catch (CaffFileFormatException | CAFFProcessorRuntimeException e) {
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

        var savedCaffEntity = caffRepository.save(caffFileEntity);
        log.info("Saved CAFF file with filename '{}' and id {} by user with id {}.", savedCaffEntity.getFileName(), savedCaffEntity.getId(), getUserIdFromJwt());
        return savedCaffEntity;
    }

    /**
     * This method responsible for the Caff file deletion.
     * @return
     */
    @PreAuthorize("hasAuthority('caff:delete')")
    public boolean deleteCaffFile(Long id) throws CaffFileNotFoundException {
        log.info("Trying to delete CAFF file with id {} by user with id {}.", id, getUserIdFromJwt());
        var caffFileEntity = caffRepository.findById(id)
                .map(caffFile -> {
                    checkPermission(caffFile.getOwnerId());
                    return caffFile;
                })
                .orElseThrow(() -> new CaffFileNotFoundException("There is no caff file with id %s.", id));
        try {
            caffRepository.delete(caffFileEntity);
            log.info("CAFF file with id {} deleted by user with id {}.", caffFileEntity.getId(), getUserIdFromJwt());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @PreAuthorize("hasAuthority('caff:read')")
    public List<CAFFFile> searchCaffFilesByMetaData(String metaData) {
        log.info("Searching for CAFF files with metaData {} by user with id {}.", metaData, getUserIdFromJwt());
        return caffRepository.findAllByMetaDataIgnoreCase(metaData);
    }

}