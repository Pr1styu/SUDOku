package hu.bme.compsec.sudoku.service.processor;

import hu.bme.compsec.sudoku.common.exception.CaffFileFormatExpression;
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
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Slf4j
public final class CaffProcessor {

    private static final String workDirPath = "./workdir/";
    private static final String NATIVE_CAFF_PARSER_PATH_WIN = "./../NativeComponent/bin/CAFFParser.exe";
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

    @Getter
    private byte[] preview;
//    private String[] or JSONObject metaData;

    public void process(MultipartFile uploadedCaffFile, String clientFileName) throws CaffFileFormatExpression {
        /*
         * DONE 1. Save uploaded caff file (uploadedCaffFile) to the filesystem.
         * DONE 2. Call the native component with proper params
         * DONE 3. Load preview image (generated jpg) bytes
         * 3.5 and get meta data from generated txt. (After we can clean the workdir.)
         * DONE 4. Save CAFFFile entity. ( in service level calling this method)
         * */

        saveCaffFileToFileSystem(uploadedCaffFile, clientFileName);
        parseCaffFile();

        loadPreview();
        extractMetaData();

        cleanWorkDir();
    }

    @PostConstruct
    private void createWorkDir() {
        try {
            Files.createDirectories(Paths.get(workDirPath));
        } catch (Exception e) {
            throw new RuntimeException("Could not create working directory for caff files!");
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

    private void saveCaffFileToFileSystem(MultipartFile uploadedCaffFile, String clientFileName) {
        log.info("Trying to save caff file {} to the filesystem with name {}.", clientFileName, savedBaseName);
        try {
            workDir = Paths.get(workDirPath);
            if (!Files.exists(workDir)) {
                createWorkDir();
            }

            final String savedCaffFileName = savedBaseName + CAFF_FILE_EXTENSION;
            this.savedCaffFilePath = workDir.resolve(savedCaffFileName);
            this.generatedPreviewPath = workDir.resolve(savedBaseName + GENERATED_PREVIEW_EXTENSION);
            this.generatedMetaDataPath = workDir.resolve(savedBaseName + GENERATED_METADATA_EXTENSION);

            Files.copy(uploadedCaffFile.getInputStream(), savedCaffFilePath);
            log.info("Uploaded caff file {} successfully saved with name {}.", clientFileName, savedCaffFileName);
        } catch (Exception e) {
            log.info("Uploaded caff file {} could not be saved to filesystem.", clientFileName);
            throw new RuntimeException("Uploaded caff file could not be saved to filesystem due to: " + e.getMessage());
        }
    }

    /* Command parameters:
     * CAFFParser -i <caff fájl helye> <-of> <jpeg kimeneti helye és neve> <-ot> <metaadatoknak a kimeneti helye és neve>
     * CAFFParser -of "hello.jpeg" -i "C:\Users\ABC\CLionProjects\SUDOku\NativeComponent\CAFFTest\3.caff" -ot "txtout.txt"
     * */
    private void parseCaffFile() throws CaffFileFormatExpression {
        String parserCommand = createParserCommand();
        ProcessBuilder processBuilder = new ProcessBuilder(parserCommand.split(" "));
        File parseLogFile = workDir.resolve(savedBaseName + PARSER_LOG_EXTENSION).toFile(); // TODO: Move logs another place
        processBuilder.redirectErrorStream(true);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.appendTo(parseLogFile));
        try {
            log.info("Starting parser with command: \n\t{}", parserCommand);
            final Process parserProcess = processBuilder.start();
            parserProcess.waitFor(PARSING_TIMEOUT_MIN, TimeUnit.MINUTES);
            final int parseResultCode = parserProcess.exitValue();
            if (parseResultCode == ParseResult.PARSED.getCode()) {
                log.info("Caff file {} parsed successfully.", savedBaseName);
            } else {
                log.error("Caff parser finished with error code: {}", parseResultCode);
                throw new CaffFileFormatExpression("Could not parse caff file: {}, code: ", savedBaseName, parseResultCode);
            }
        } catch (IOException e) {
            log.error("Could not call native parser for caff file {} due to {}.", savedBaseName, e.getMessage());
            throw new CaffFileFormatExpression("Could not parse caff file: {}", savedBaseName);
        } catch (InterruptedException e) {
            log.error("Could process caff file {} on time.", savedBaseName);
            throw new CaffFileFormatExpression("Too complex caff file to parse!");
        }
    }

    private String createParserCommand() {
        StringJoiner parserCommand = new StringJoiner(" ");
        if (SystemUtils.IS_OS_WINDOWS) {
            parserCommand.add(NATIVE_CAFF_PARSER_PATH_WIN);
        } else {
            // TODO: Create unix binary and use this here
//            parserCmd.add(NATIVE_CAFF_PARSER_PATH_UNIX);
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
            log.info("Caff preview {} loaded successfully.", savedBaseName);
        } catch (Exception e) {
            log.error("Cannot load preview image for caff file {} due to: {}", savedBaseName, e.getMessage());
            // TODO: Set default image as preview?
//            this.preview =
        }
    }

    private void extractMetaData() {
        // TODO: Implement this
    }

}