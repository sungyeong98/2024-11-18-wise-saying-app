import org.example.WiseSayingRepository;
import org.example.WiseSayingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class WiseSayingServiceTest {

    private WiseSayingService service;
    private WiseSayingRepository repo;

    @BeforeEach
    public void setUp() {
        repo = mock(WiseSayingRepository.class);
        service = new WiseSayingService(repo);
    }

    @Test   // 명언 추가 테스트
    public void testAddSaying() {
        String author = "author1";
        String content = "content1";

        when(repo.getNextId()).thenReturn(1);
        int id = service.addSaying(author, content);

        assertEquals(1,id, "id 동일 여부 확인");
        verify(repo, times(1)).addSaying(1, author, content);
        verify(repo, times(1)).updateIdFile(2);
    }

    @Test   // 명언 불러오기 테스트
    public void testGetAllSayings() {
        when(repo.getTemporarySayings()).thenReturn(
                Map.of(1, new String[]{"author1", "content1"}, 2, new String[]{"author2", "content2"})
        );

        when(repo.getStoredSayings()).thenReturn(
                Arrays.asList("3 / author3 / content3")
        );

        List<String> allSayings = service.getAllSayings();

        assertEquals(3, allSayings.size(), "목록 크기 동일 여부 확인");
        assertEquals("3 / author3 / content3", allSayings.get(0), "id3");
        assertEquals("2 / author2 / content2 (저장되지 않음)", allSayings.get(1), "id2");
        assertEquals("1 / author1 / content1 (저장되지 않음)", allSayings.get(2), "id1");
    }

    @Test   // 명언 수정 테스트
    public void testUpdateSaying() {
        when(repo.getTemporarySayings()).thenReturn(
                Map.of(1, new String[]{"author1", "content1"})
        );
        when(repo.updateTemporarySaying(1,"author1", "content1")).thenReturn(true);
        when(repo.updateSavedSaying(2, "author2", "content2")).thenReturn(true);

        boolean tempResult = service.updateSaying(1, "author1", "content1");
        assertTrue(tempResult, "임시 파일 수정 여부 확인");

        boolean savedResult = service.updateSaying(2, "author2", "content2");
        assertTrue(savedResult, "저장 파일 수정 여부 확인");

        verify(repo).updateTemporarySaying(1, "author1", "content1");
        verify(repo).updateSavedSaying(2, "author2", "content2");
    }

    @Test   // 명언 삭제 테스트
    public void testDeleteSaying() {
        when(repo.getTemporarySayings()).thenReturn(
                Map.of(1, new String[]{"author1", "content1"},
                        2, new String[]{"author2", "content2"})
        );
        when(repo.deleteTemporarySaying(2)).thenReturn(true);
        when(repo.deleteSavedSaying(3)).thenReturn(true);

        boolean tempResult = service.deleteSaying(2);
        assertTrue(tempResult, "임시 파일 삭제 여부 확인");

        boolean savedResult = service.deleteSaying(3);
        assertTrue(savedResult, "저장 파일 삭제 여부 확인");

        verify(repo).deleteTemporarySaying(2);
        verify(repo).deleteSavedSaying(3);
    }

    @Test   // 명언 저장 테스트
    public void testSaveSaying() throws IOException {
        Map<Integer, String[]> tempList = Map.of(1, new String[]{"author1", "content1"},
                                                2, new String[]{"author2", "content2"});

        when(repo.getTemporarySayings()).thenReturn(tempList);

        service.saveSaying();

        verify(repo).saveTemporarySaying();

//        File savedFile1 = new File("db/wiseSaying/1.json");
//        File savedFile2 = new File("db/wiseSaying/2.json");
//
//        assertTrue(savedFile1.exists(), "파일1 존재 유무 확인");
//        assertTrue(savedFile2.exists(), "파일2 존재 유무 확인");
//
//        String savedContent1 = new String(Files.readAllBytes(savedFile1.toPath()));
//        String savedContent2 = new String(Files.readAllBytes(savedFile2.toPath()));
//
//        assertTrue(savedContent1.contains("Author1"), "author1 확인");
//        assertTrue(savedContent1.contains("Saying1"), "content1 확인");
//
//        assertTrue(savedContent2.contains("Author2"), "author2 확인");
//        assertTrue(savedContent2.contains("Saying2"), "content2 확인");
    }

    @Test   // id호출 테스트
    public void testGetParamAsInt(){
        int result = service.getParamAsInt("삭제?id=10");
        assertEquals(10,result);

        result = service.getParamAsInt("삭제?id=");
        assertEquals(0,result);
    }
}
