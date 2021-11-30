package hu.bme.compsec.sudoku.helper;

import hu.bme.compsec.sudoku.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.service.processor.CaffProcessor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CaffFileHelper {
    public CAFFFile loadCaffFile(String fileName) throws IOException, CaffFileFormatException {
        MultipartFile result = loadMultipartFile(fileName);

        var processor = new CaffProcessor();

        CAFFFile caff = new CAFFFile();
        caff.setFileName(fileName);
        processor.process(result, fileName);
        caff.setPreview(processor.getPreview());
        caff.setMetaData(processor.getMetaData());

        return caff;
    }

    public MultipartFile loadMultipartFile(String fileName) throws IOException {
        Path path = Paths.get("src/test/resources/caff_files/" + fileName);
        String contentType = "application/octet-stream";
        byte[] content = Files.readAllBytes(path);
        return new MockMultipartFile(fileName, fileName, contentType, content);
    }
}
