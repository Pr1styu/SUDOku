package hu.bme.compsec.sudoku.presentation.controller;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import hu.bme.compsec.sudoku.config.TestSecurityConfig;
import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.data.domain.Comment;
import hu.bme.compsec.sudoku.helper.CaffFileHelper;
import hu.bme.compsec.sudoku.presentation.dto.CAFFFileDetailDTO;
import hu.bme.compsec.sudoku.presentation.dto.CommentDTO;
import hu.bme.compsec.sudoku.presentation.mapping.CAFFMapper;
import hu.bme.compsec.sudoku.service.CAFFService;
import hu.bme.compsec.sudoku.service.CommentService;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(classes = TestSecurityConfig.class)
@WithMockUser(username = "admin", password = "admin", authorities = {"caff:read", "caff:write", "caff:delete"})
public class CAFFControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CAFFService caffServiceMock;

    private final CAFFMapper caffMapper = Mappers.getMapper(CAFFMapper.class);

    @MockBean
    private CommentService commentServiceMock;

    private final CaffFileHelper helper = new CaffFileHelper();
    private final Moshi moshi = new Moshi.Builder().build();
    private final JsonAdapter<CAFFFileDetailDTO> caffJsonAdapter = moshi.adapter(CAFFFileDetailDTO.class);
    private final JsonAdapter<CommentDTO> commentJsonAdapter = moshi.adapter(CommentDTO.class);

    @Test
    public void shouldListAllFiles() throws Exception {
        given(caffServiceMock.getAllCaffFile())
                .willReturn(helper.loadAllCaffFiles());

        mockMvc.perform(get("/caff")
                        .with(user("admin").password("admin")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(
                        JSONArray.toJSONString(helper.loadAllCaffFiles()
                                .parallelStream()
                                .map(caffMapper::toPreviewDTO)
                                .collect(Collectors.toList()))
                        )
                );
    }

    @Test
    public void shouldReturnCaffById() throws Exception {
        final long mockId = 1L;
        CAFFFile f = helper.loadCaffFile("1.caff");
        var mockCaffFile = CAFFFile.builder().fileName(f.getFileName())
                .id(mockId)
                .metaData(f.getMetaData())
                .preview(f.getPreview())
                .rawBytes(f.getRawBytes())
                .build();

        given(caffServiceMock.getCaffFileById(mockId))
                .willReturn(Optional.of(mockCaffFile));

        CAFFFileDetailDTO detail = caffMapper.toDetailDTO(mockCaffFile);
        String jsonString = caffJsonAdapter.toJson(detail);
        System.out.println(detail);

        mockMvc.perform(get("/caff/" + mockId)
                        .with(user("admin").password("admin")))
                .andExpect(content().json(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(caffServiceMock, times(1)).getCaffFileById(mockId);
        verifyNoMoreInteractions(caffServiceMock);
    }

    @Test
    public void should404WhenMissingFile() throws Exception {
        this.mockMvc.perform(get("/caff/1")
                        .with(user("admin").password("admin")))
                .andExpect(status().isNotFound());

        this.mockMvc.perform(get("/caff/download/1")
                        .with(user("admin").password("admin")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnBadRequestForUpload() throws Exception {
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
    }

    @Test
    public void shouldUploadSuccessfully() throws Exception {
        final long mockId = 1L;
        CAFFFile f = helper.loadCaffFile("1.caff");
        MockMultipartFile multipartFile = helper.loadMultipartFileWithNullFields("1.caff");
        var mockCaffFile = CAFFFile.builder().fileName(f.getFileName())
                .id(mockId)
                .metaData(f.getMetaData())
                .preview(f.getPreview())
                .rawBytes(f.getRawBytes())
                .build();

        given(caffServiceMock.saveCaffFile(isNotNull(), isNotNull()))
                .willReturn(mockCaffFile);

        this.mockMvc.perform(multipart("/caff/upload")
                                .file("caffFile", multipartFile.getBytes())
                                .file(new MockMultipartFile("fileName", "1.caff".getBytes())))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldDownloadCaff() throws Exception {
        final long mockId = 1L;
        CAFFFile f = helper.loadCaffFile("1.caff");
        var mockCaffFile = CAFFFile.builder().fileName(f.getFileName())
                .id(mockId)
                .metaData(f.getMetaData())
                .preview(f.getPreview())
                .rawBytes(f.getRawBytes())
                .build();

        given(caffServiceMock.getCaffFileById(mockId))
                .willReturn(Optional.of(mockCaffFile));

        mockMvc.perform(get("/caff/download/" + mockId)
                        .with(user("admin").password("admin"))
                        .accept("application/octet-stream")
                        .contentType("application/octet-stream"))
                .andExpect(content().bytes(f.getRawBytes()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));

        verify(caffServiceMock, times(1)).getCaffFileById(mockId);
        verifyNoMoreInteractions(caffServiceMock);
    }

    @Test
    public void shouldDeleteFile() throws Exception {
        //TODO: double check this, I can't believe this is enough
        mockMvc.perform(delete("/caff/1")
                        .with(user("admin").password("admin")))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnCaffByMetadata() throws Exception {
        given(caffServiceMock.searchCaffFilesByMetaData("sunset"))
                .willReturn(helper.loadAllCaffFiles());

        mockMvc.perform(get("/caff/search/sunset")
                .with(user("admin").password("admin")))
                .andExpect(content().json(JSONArray.toJSONString(
                        helper.loadAllCaffFiles()
                                .parallelStream()
                                .map(caffMapper::toPreviewDTO)
                                .collect(Collectors.toList()))))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnCommentsForCaffFile() throws Exception {
        ArrayList<Comment> comments = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            comments.add(Comment.builder()
                    .caffFile(helper.loadCaffFile("1.caff"))
                    .text("Test comment" + i)
                    .userId(1L)
                    .username("admin")
                    .build()
            );
        }

        given(caffServiceMock.getAllCaffFile())
                .willReturn(helper.loadAllCaffFiles());
        given(commentServiceMock.getAllCommentForCaffFile(1L))
                .willReturn(comments);

        mockMvc.perform(get("/caff/1/comment")
                .with(user("admin").password("admin")))
                .andExpect(content().json(JSONArray.toJSONString(
                        comments.parallelStream()
                                .map(caffMapper::toCommentDTO)
                                .collect(Collectors.toList())
                )))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAddCommentSuccessfully() throws Exception {
        CommentDTO commentDTO = new CommentDTO("Test comment1", "admin");
        given(commentServiceMock.addCommentToCaffFile(1L, commentDTO))
                .willReturn(true);

        mockMvc.perform(post("/caff/1/comment")
                        .with(user("admin").password("admin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJsonAdapter.toJson(commentDTO)))
                .andExpect(status().isAccepted());
    }

    @Test
    public void shouldReturnNotFoundWhenAddingComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO("Test comment1", "admin");
        mockMvc.perform(post("/caff/2/comment")
                        .with(user("admin").password("admin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJsonAdapter.toJson(commentDTO)))
                .andExpect(status().isNotFound());
    }
}