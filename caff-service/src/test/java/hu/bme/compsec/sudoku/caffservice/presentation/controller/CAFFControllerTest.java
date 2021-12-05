package hu.bme.compsec.sudoku.caffservice.presentation.controller;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import config.TestSecurityConfig;
import hu.bme.compsec.sudoku.caffservice.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.caffservice.data.domain.Comment;
import hu.bme.compsec.sudoku.caffservice.helper.CaffFileHelper;
import hu.bme.compsec.sudoku.caffservice.presentation.dto.CAFFFileDetailDTO;
import hu.bme.compsec.sudoku.caffservice.presentation.dto.CommentDTO;
import hu.bme.compsec.sudoku.caffservice.presentation.mapping.CAFFMapper;
import hu.bme.compsec.sudoku.caffservice.service.CAFFService;
import hu.bme.compsec.sudoku.caffservice.service.CommentService;
import hu.bme.compsec.sudoku.common.security.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static config.TestSecurityConfig.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest(classes = TestSecurityConfig.class)
@WithMockUser(username = "admin", password = "admin", authorities = {"caff:read", "caff:write", "caff:delete"})
class CAFFControllerTest {

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

    @BeforeEach
    public void mockJWT() {
        var userId = getRandomId();
        mockAuthenticatedUserId(userId);
        mockAuthWithUserRoleAndId(UserRole.ADMIN);
    }

    @Test
    void shouldListAllFiles() throws Exception {
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
    void shouldReturnCaffById() throws Exception {
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
    void should404WhenMissingFile() throws Exception {
        this.mockMvc.perform(get("/caff/1")
                        .with(user("admin").password("admin")))
                .andExpect(status().isNotFound());

        this.mockMvc.perform(get("/caff/download/1")
                        .with(user("admin").password("admin")))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestForUpload() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(TestSecurityConfig.jwt().getTokenValue());

        MockMultipartFile multipartFile = helper.loadMultipartFile("1.caff");
        this.mockMvc.perform(multipart("/caff/upload")
                                .file("caffFile", multipartFile.getBytes())
                                .file(new MockMultipartFile("fileName", "testFileName".getBytes()))
                                .headers(httpHeaders)
                                .contentType("multipart/form-data")
                                .accept("multipart/form-data")
                )
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist(HttpHeaders.LOCATION));
    }

    @Test
    void shouldUploadSuccessfully() throws Exception {
        final long mockId = new Random().nextInt(100);
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

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(TestSecurityConfig.jwt().getTokenValue());

        this.mockMvc.perform(multipart("/caff/upload")
                                .file("caffFile", multipartFile.getBytes())
                                .file(new MockMultipartFile("fileName", "1.caff".getBytes()))
                        .headers(httpHeaders))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/caff/" + mockId));
    }

    @Test
    void shouldDownloadCaff() throws Exception {
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

        mockMvc.perform(get("/caff/"+ mockId+"/download")
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
    void shouldDeleteFileAsAdmin() throws Exception {

        var userId = getRandomId();
        mockAuthenticatedUserId(userId);
        mockAuthWithUserRoleAndId(UserRole.ADMIN);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(TestSecurityConfig.jwt().getTokenValue());
        //TODO: consider implementing this to check responses
        mockMvc.perform(delete("/caff/1")
                        .headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteOwnFile() throws Exception {

        var userId = getRandomId();
        mockAuthenticatedUserId(userId);
        mockAuthWithUserRoleAndId(UserRole.USER);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(TestSecurityConfig.jwt().getTokenValue());
        //TODO: consider implementing this to check responses
        mockMvc.perform(delete("/caff/1")
                        .headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailOnDeleteOthersFile() throws Exception {

        var userId = getRandomId();
        mockAuthenticatedUserId(userId);
        mockAuthWithUserRoleAndId(UserRole.USER);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(TestSecurityConfig.jwt().getTokenValue());

        //TODO: consider implementing this to check responses
        mockMvc.perform(delete("/caff/1")
                        .headers(httpHeaders))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnCaffByMetadata() throws Exception {
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
    void shouldReturnCommentsForCaffFile() throws Exception {
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
    void shouldAddCommentSuccessfully() throws Exception {
        CommentDTO commentDTO = new CommentDTO("Test comment1", "admin");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(TestSecurityConfig.jwt().getTokenValue());


        given(commentServiceMock.addCommentToCaffFile(1L, commentDTO))
                .willReturn(true);

        mockMvc.perform(post("/caff/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJsonAdapter.toJson(commentDTO))
                        .headers(httpHeaders))
                .andExpect(status().isAccepted());
    }

    @Test
    void shouldReturnNotFoundWhenAddingComment() throws Exception {
        CommentDTO commentDTO = new CommentDTO("Test comment1", "admin");

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(TestSecurityConfig.jwt().getTokenValue());

        mockMvc.perform(post("/caff/2/comment")
                        .with(user("admin").password("admin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJsonAdapter.toJson(commentDTO))
                        .headers(httpHeaders))
                .andExpect(status().isNotFound());
    }
}