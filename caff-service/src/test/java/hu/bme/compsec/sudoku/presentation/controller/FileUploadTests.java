package hu.bme.compsec.sudoku.presentation.controller;

import com.nimbusds.jose.shaded.json.JSONArray;
import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.helper.CaffFileHelper;
import hu.bme.compsec.sudoku.presentation.mapping.CAFFMapper;
import hu.bme.compsec.sudoku.service.CAFFService;
import hu.bme.compsec.sudoku.service.CommentService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CAFFController.class)
@WithMockUser(username = "admin", password = "admin", authorities = {"caff:read", "caff:write", "caff:delete"})
public class FileUploadTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CAFFService caffServiceMock;

    @MockBean
    private CAFFMapper caffMapperMock;

    @MockBean
    private CommentService commentServiceMock;

    CaffFileHelper helper = new CaffFileHelper();

    @Test
    public void shouldListAllFiles() throws Exception {
        String[] fileNames = new String[]{"1.caff", "2.caff", "3.caff"};

        given(caffServiceMock.getAllCaffFile())
                .willReturn(helper.loadAllCaffFiles(fileNames));

        mockMvc.perform(get("/caff")
                        .with(user("admin").password("admin")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        JSONArray.toJSONString(helper.loadAllCaffFiles(fileNames)
                                .parallelStream()
                                .map(caffMapperMock::toPreviewDTO)
                                .collect(Collectors.toList()))
                        )
                );
    }

    @Test
    public void shouldReturnCaffById() throws Exception {
        final long mockId = 1L;
        var mockCaffFile = CAFFFile.builder().fileName("test.caff")
                .id(mockId)
                .metaData(List.of("test", "meta", "passed"))
                .preview("Base64EncodedStringPreview".getBytes())
                .rawBytes("rawBytesOfCassFile".getBytes())
                .build();

        given(caffServiceMock.getCaffFileById(mockId))
                .willReturn(Optional.of(mockCaffFile));

        JSONObject caffFilePreviewDto_JSON =
                new JSONObject().put("id", mockId).put("fileName", "test.caff");

        mockMvc.perform(get("/caff/" + mockId)
                        .with(user("admin").password("admin")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(caffFilePreviewDto_JSON.toString()));

        verify(caffServiceMock, times(1)).getCaffFileById(mockId);
        verifyNoMoreInteractions(caffServiceMock);
    }

    @Test
    public void shouldIgnoreUploadedFile_OR_BETTER_NAME() throws Exception {
        MockMultipartFile multipartFile = helper.loadMultipartFile("1.caff");
        this.mockMvc.perform(multipart("/caff/upload")
                                .file("caffFile", multipartFile.getBytes())
                                .file(new MockMultipartFile("fileName", "testFileName".getBytes())) // TODO: Not sure why this we needed instead of requestAttr
//								.requestAttr("caffFile", multipartFile)
//								.requestAttr("fileName", "1.caff")
                                .contentType("multipart/form-data")
                                .accept("multipart/form-data")
                )
                .andExpect(status().isBadRequest());
//				.andExpect(header().string("Location", "/"));

        //.then(this.storageService).should().store(multipartFile);
    }

    // TODO: Implement this with caffServiceMock return a valid entity and check for 201 etc
    //public void shouldSaveUploadedFile() throws Exception {


	/*@SuppressWarnings("unchecked")
	@Test
	public void should404WhenMissingFile() throws Exception {
		given(this.caffServiceMock.loadAsResource("test.txt"))
				.willThrow(StorageFileNotFoundException.class);

		this.mockMvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
	}*/

}