import org.example.WiseSaying;
import org.example.WiseSayingRepository;
import org.example.WiseSayingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WiseSayingServiceTest {
    private WiseSayingService service;
    private WiseSayingRepository repo;

    @BeforeEach
    public void setUp() {
        repo = mock(WiseSayingRepository.class);  // 경로는 mock 처리
        service = new WiseSayingService(repo);
    }

    @Test   // 명언 추가 테스트
    public void testAddSaying() {
        String author = "author1";
        String content = "content1";

        when(repo.getNextId()).thenReturn(1);

        int id = service.addSaying(author, content);

        verify(repo, times(1)).addSaying(1, author, content);
        assertEquals(1, id, "id 동일 여부 확인");
    }

    @Test   // 임시 명언 불러오기 테스트
    public void testGetTempSayings() {
        Map<Integer, WiseSaying> tempMap = new HashMap<>();

        tempMap.put(1, new WiseSaying(1, "author1", "content1"));
        tempMap.put(2, new WiseSaying(2, "author2", "content2"));

        when(repo.getTemporarySayings()).thenReturn((HashMap<Integer, WiseSaying>) tempMap);

        assertEquals("2 / author2 / content2 (저장되지 않음)",
                String.format("%d / %s / %s (저장되지 않음)", tempMap.get(2).getId(), tempMap.get(2).getAuthor(), tempMap.get(2).getContent()), "2번 데이터");

        assertEquals("1 / author1 / content1 (저장되지 않음)",
                String.format("%d / %s / %s (저장되지 않음)", tempMap.get(1).getId(), tempMap.get(1).getAuthor(), tempMap.get(1).getContent()), "1번 데이터");

    }

    @Test   // 저장된 명언 불러오기 테스트
    public void testGetSavedSayings() {
        when(repo.getSavedSayings()).thenReturn(List.of("3 / author3 / content3"));

        List<String> allSayings = service.getSavedSayings();

        assertEquals(1, allSayings.size(), "목록 크기 동일 여부 확인");
        assertEquals("3 / author3 / content3", allSayings.get(0), "id3");
    }

    @Test   // 명언 수정 테스트
    public void testUpdateSaying() {
        Map<Integer, WiseSaying> tempSayings = new HashMap<>();
        tempSayings.put(1, new WiseSaying(1, "author1", "content1"));

        when(repo.getTemporarySayings()).thenReturn((HashMap<Integer, WiseSaying>) tempSayings);
        when(repo.updateTemporarySaying(1, "author1", "content1")).thenReturn(true);
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
        Map<Integer, WiseSaying> tempSayings = new HashMap<>();
        tempSayings.put(1, new WiseSaying(1, "author1", "content1"));
        tempSayings.put(2, new WiseSaying(2, "author2", "content2"));

        when(repo.getTemporarySayings()).thenReturn((HashMap<Integer, WiseSaying>) tempSayings);
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
        Map<Integer, WiseSaying> tempList = new HashMap<>();
        tempList.put(1, new WiseSaying(1, "author1", "content1"));
        tempList.put(2, new WiseSaying(2, "author2", "content2"));

        when(repo.getTemporarySayings()).thenReturn((HashMap<Integer, WiseSaying>) tempList);

        service.saveSaying();

        verify(repo).saveTemporarySaying();
    }

}
