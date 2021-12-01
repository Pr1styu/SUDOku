package hu.bme.compsec.sudoku.presentation.controller;

import java.util.List;
import java.util.Optional;

import hu.bme.compsec.sudoku.data.domain.CAFFFile;
import hu.bme.compsec.sudoku.service.CAFFService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/*@AutoConfigureMockMvc
@SpringBootTest
public class FileUploadTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private CAFFService caffServiceMock;

	@Test
	public void shouldListAllFiles() throws Exception {
		final long mockId = 1l;
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

		mockMvc.perform(get("/caff/"+mockId)
						.with(user("admin").password("admin")))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(caffFilePreviewDto_JSON.toString()));

		verify(caffServiceMock, times(1)).getCaffFileById(mockId);
		verifyNoMoreInteractions(caffServiceMock);
	}

	/*@Test
	public void shouldSaveUploadedFile() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
				"text/plain", "Spring Framework".getBytes());
		this.mockMvc.perform(multipart("/caff/uploadCaff")
						.file(multipartFile)
						.with(user("admin").password("admin")))
				.andExpect(status().isFound())
				.andExpect(header().string("Location", "/"));

		//then(this.storageService).should().store(multipartFile);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void should404WhenMissingFile() throws Exception {
		given(this.caffService.loadAsResource("test.txt"))
				.willThrow(StorageFileNotFoundException.class);

		this.mvc.perform(get("/files/test.txt")).andExpect(status().isNotFound());
	}*/

//}