package hu.bme.compsec.sudoku.caffservice.service.processor;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import hu.bme.compsec.sudoku.caffservice.common.exception.CAFFProcessorRuntimeException;
import hu.bme.compsec.sudoku.caffservice.common.exception.CaffFileFormatException;
import hu.bme.compsec.sudoku.caffservice.data.helper.CiffList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Slf4j
public final class CaffProcessor {

    private static final String WORK_DIR_PATH = "./workdir/";
    private static final String NATIVE_CAFF_PARSER_PATH_WIN = "./native/bin/CAFFParser.exe";
    private static final String NATIVE_CAFF_PARSER_PATH_UNIX = "./native/bin/CAFFParser";
    private static final String GENERATED_PREVIEW_EXTENSION = ".jpeg";
    private static final String GENERATED_METADATA_EXTENSION = ".txt";
    private static final String PARSER_LOG_EXTENSION = ".parse";

    private static final String CAFF_FILE_EXTENSION = ".caff";
    private static final int PARSING_TIMEOUT_MIN = 2;

    private final String savedBaseName = UUID.randomUUID().toString();
    private Path workDir;
    private Path savedCaffFilePath;
    private Path generatedPreviewPath;
    private Path generatedMetaDataPath;

    private final Moshi moshi = new Moshi.Builder().build();
    private final JsonAdapter<CiffList> jsonAdapter = moshi.adapter(CiffList.class);

    @Getter
    private byte[] preview;
    @Getter
    private List<String> metaData;


    public void process(MultipartFile uploadedCaffFile, String clientFileName) throws CaffFileFormatException, CAFFProcessorRuntimeException {
        saveCaffFileToFileSystem(uploadedCaffFile, clientFileName);
        parseCaffFile();

        loadPreview();
        extractMetaData();

        cleanWorkDir();
    }

    @PostConstruct
    private void createWorkDir() throws CAFFProcessorRuntimeException {
        try {
            Files.createDirectories(Paths.get(WORK_DIR_PATH));
        } catch (Exception e) {
            throw new CAFFProcessorRuntimeException("Could not create working directory for caff files!");
        }
    }

    /**
     * Delete actual caff file and generated preview, metadata from working dir.
     */
    private void cleanWorkDir() {
        try {
            Arrays.stream(Objects.requireNonNull(workDir.toFile().listFiles()))
                    .filter(f -> f.getName().contains(savedBaseName))
                    // Letting parse log for internal audit.
                    .filter(f -> !f.getName().endsWith(PARSER_LOG_EXTENSION))
                    .forEach(File::delete);
        } catch (Exception e) {
            log.error("Cannot clean working directory due to: {}.", e.getMessage());
        }
    }

    private void saveCaffFileToFileSystem(MultipartFile uploadedCaffFile, String clientFileName) throws CAFFProcessorRuntimeException {
        log.trace("Trying to save caff file '{}' to the filesystem with name {}.", clientFileName, savedBaseName);
        try {
            workDir = Paths.get(WORK_DIR_PATH);
            if (!Files.exists(workDir)) {
                createWorkDir();
            }

            final String savedCaffFileName = savedBaseName + CAFF_FILE_EXTENSION;
            this.savedCaffFilePath = workDir.resolve(savedCaffFileName);
            this.generatedPreviewPath = workDir.resolve(savedBaseName + GENERATED_PREVIEW_EXTENSION);
            this.generatedMetaDataPath = workDir.resolve(savedBaseName + GENERATED_METADATA_EXTENSION);

            Files.copy(uploadedCaffFile.getInputStream(), savedCaffFilePath);
            log.trace("Uploaded caff file '{}' successfully saved with name {}.", clientFileName, savedCaffFileName);
        } catch (Exception e) {
            log.warn("Uploaded caff file '{}' could not be saved to filesystem.", clientFileName);
            throw new CAFFProcessorRuntimeException("Uploaded caff file could not be saved to filesystem due to: " + e.getMessage());
        }
    }

    /* Command parameters:
     * CAFFParser -i <caff fájl helye> <-of> <jpeg kimeneti helye és neve> <-ot> <metaadatoknak a kimeneti helye és neve>
     * CAFFParser -of "hello.jpeg" -i "C:\Users\ABC\CLionProjects\SUDOku\NativeComponent\CAFFTest\3.caff" -ot "txtout.txt"
     * */
    private void parseCaffFile() throws CaffFileFormatException {
        String parserCommand = createParserCommand();
        ProcessBuilder processBuilder = new ProcessBuilder(parserCommand.split(" "));
        File parseLogFile = workDir.resolve(savedBaseName + PARSER_LOG_EXTENSION).toFile();
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(parseLogFile));
        try {
            log.trace("Starting parser with command: \n\t{}", parserCommand);
            final Process parserProcess = processBuilder.start();
            parserProcess.waitFor(PARSING_TIMEOUT_MIN, TimeUnit.MINUTES);
            final int parseResultCode = parserProcess.exitValue();
            if (parseResultCode == ParseResult.PARSED.getCode()) {
                log.info("Caff file {} parsed successfully.", savedBaseName);
            } else {
                log.error("Caff parser finished with error code: {}", parseResultCode);
                throw new CaffFileFormatException("Could not parse caff file: {}, code: ", savedBaseName, parseResultCode);
            }
        } catch (IOException e) {
            log.error("Could not call native parser for caff file {} due to {}.", savedBaseName, e.getMessage());
            throw new CaffFileFormatException("Could not parse caff file: {}", savedBaseName);
        } catch (InterruptedException e) {
            log.error("Could process caff file {} on time.", savedBaseName);
            cleanWorkDir();
            cleanPreviewAndMetaData();
            Thread.currentThread().interrupt();
            throw new CaffFileFormatException("Too complex caff file {} to parse!", savedBaseName);
        }
    }

    private void cleanPreviewAndMetaData() {
        try {
            Files.delete(generatedPreviewPath);
            Files.delete(generatedMetaDataPath);
            log.info("Cleaned up after interrupt exception.");
        }catch(IOException e){
            log.warn("IOException occurred during clean up it might be just that the files were not created in the first place. Message: {}", e.getMessage());
        }
    }

    private String createParserCommand() {
        StringJoiner parserCommand = new StringJoiner(" ");
        if (SystemUtils.IS_OS_WINDOWS) {
            parserCommand.add(NATIVE_CAFF_PARSER_PATH_WIN);
        } else {
            parserCommand.add(NATIVE_CAFF_PARSER_PATH_UNIX);
        }
        parserCommand
                .add("-i").add(savedCaffFilePath.toString())
                .add("-of").add(generatedPreviewPath.toString())
                .add("-ot").add(generatedMetaDataPath.toString());
        return parserCommand.toString();
    }

    private void loadPreview() {
        try {
            this.preview = Files.readAllBytes(generatedPreviewPath);
            log.trace("Caff file {} preview loaded successfully.", savedBaseName);
        } catch (Exception e) {
            log.error("Cannot load preview image for caff file {} due to: {}", savedBaseName, e.getMessage());
        }
    }

    private void extractMetaData() {
        try {
            String json = Files.readString(generatedMetaDataPath);
            CiffList ciffList = jsonAdapter.fromJson(json);
            HashSet<String> tags = new HashSet<>();
            if (ciffList != null) {
                ciffList.getCiffs().forEach(ciff -> tags.addAll(ciff.getTags()));
            }
            metaData = new ArrayList<>(tags);
            log.trace("Caff file {} metaData extracted successfully.", savedBaseName);
        } catch (IOException e) {
            log.error("Cannot extract meta data caff file {} due to: {}", savedBaseName, e.getMessage());
        }
    }
}
