package hu.bme.compsec.sudoku.helper;

import hu.bme.compsec.sudoku.common.exception.CAFFProcessorRuntimeException;
import hu.bme.compsec.sudoku.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.service.processor.CaffProcessor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CaffFileHelper {
    public CAFFFile loadCaffFile(String resourceFileName) throws IOException, CaffFileFormatException, CAFFProcessorRuntimeException {
        MultipartFile result = loadMultipartFile(resourceFileName);

        var processor = new CaffProcessor();

        CAFFFile caff = new CAFFFile();
        caff.setFileName(resourceFileName);
        processor.process(result, resourceFileName);
        caff.setPreview(processor.getPreview());
        caff.setMetaData(processor.getMetaData());
        caff.setRawBytes(result.getBytes());

        return caff;
    }

    public MockMultipartFile loadMultipartFile(String resourceFileName) throws IOException {
        Path path = Paths.get("src/test/resources/caff_files/" + resourceFileName);
        //String contentType = "application/octet-stream";
        String contentType = "multipart/form-data";
        byte[] content = Files.readAllBytes(path);
        return new MockMultipartFile("caffFile", resourceFileName, contentType, content);
    }

    public MockMultipartFile loadMultipartFileWithNullFields(String resourceFileName) throws IOException {
        Path path = Paths.get("src/test/resources/caff_files/" + resourceFileName);
        //String contentType = "application/octet-stream";
        String contentType = "multipart/form-data";
        byte[] content = Files.readAllBytes(path);
        return new MockMultipartFile("caffFile", "", null, content);
    }

    public List<CAFFFile> loadAllCaffFiles() throws CaffFileFormatException, IOException, CAFFProcessorRuntimeException {
        ArrayList<CAFFFile> result = new ArrayList<>();
        for (String fileName : getAllFileNames()) {
            result.add(loadCaffFile(fileName));
        }
        return result;
    }

    public String[] getAllFileNames() {
        return new String[]{"1.caff", "2.caff", "3.caff"};
    }
}
