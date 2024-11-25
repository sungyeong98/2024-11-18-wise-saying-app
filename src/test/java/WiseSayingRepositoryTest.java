import org.example.WiseSaying;
import org.example.WiseSayingRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WiseSayingRepositoryTest {
    private WiseSayingRepository repo;
    private final String path = "db/wiseSayingTest";

    @BeforeEach
    public void setUp() {
        this.repo = new WiseSayingRepository(path);

        File folder = new File(path);

        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @AfterEach
    public void tearDown() {
        File folder = new File(path);
        if(folder.exists()) {
            for(File file : folder.listFiles()) {
                file.delete();
            }
            folder.delete();
        }
    }

    // 등록 부분
    @Test   // 명언 추가 테스트
    void testAddSaying() {
        String author = "test_author";
        String content = "test_content";
        int id = repo.getNextId();

        repo.addSaying(id, author, content);

        WiseSaying wiseSaying = repo.getTemporarySayings().get(id);
        assertNotNull(wiseSaying, "저장 확인");
        assertEquals(id, wiseSaying.getId(), "id 확인");
        assertEquals(author, wiseSaying.getAuthor(), "작가 확인");
        assertEquals(content, wiseSaying.getContent(), "내용 확인");
    }

    // 목록 부분
    @Test   // 명언 불러오기 테스트
    void testGetTemporarySayings() {
        repo.addSaying(1, "a1", "c1");
        repo.addSaying(2, "a2", "c2");

        HashMap<Integer, WiseSaying> temp = repo.getTemporarySayings();

        assertEquals(2, temp.size(), "데이터 갯수");
        assertNotNull(temp.get(1), "1번 데이터");
        assertNotNull(temp.get(2), "2번 데이터");
    }

    @Test   // 저장 명언 불러오기 테스트
    void testGetStoredSayings() throws IOException {
        File file1 = new File(path + "/1.json");
        File file2 = new File(path + "/2.json");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file1))) {
            writer.write("{\"id\": 1, \"author\": \"Author1\", \"content\": \"Saying1\"}");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file2))) {
            writer.write("{\"id\": 2, \"author\": \"Author2\", \"content\": \"Saying2\"}");
        }

        List<String> sayings = repo.getStoredSayings();
        assertEquals(2, sayings.size(), "파일 크기");
        assertTrue(sayings.contains("1 / Author1 / Saying1"), "1번 데이터");
        assertTrue(sayings.contains("2 / Author2 / Saying2"), "2번 데이터");
    }

    // 수정 부분
    @Test   // 명언 수정 테스트
    void testUpdateTemporarySayings() {
        repo.addSaying(1, "Author1", "Saying1");

        boolean updated = repo.updateTemporarySaying(1, "UpdatedAuthor", "UpdatedSaying");
        assertTrue(updated, "저장여부");

        WiseSaying wiseSaying = repo.getTemporarySayings().get(1);
        assertEquals("UpdatedAuthor", wiseSaying.getAuthor(), "작가 확인");
        assertEquals("UpdatedSaying", wiseSaying.getContent(), "내용 확인");
    }

    @Test   // 저장 명언 수정 테스트
    void testUpdateSavedSayings() throws IOException {
        File file = new File(path + "/1.json");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("{\"id\": 1, \"author\": \"Author1\", \"content\": \"Saying1\"}");
        }

        boolean updated = repo.updateSavedSaying(1, "UpdatedAuthor", "UpdatedSaying");
        assertTrue(updated, "파일 갱신 여부");

        String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
        assertTrue(content.contains("\"author\": \"UpdatedAuthor\""), "작가 동일 여부");
        assertTrue(content.contains("\"content\": \"UpdatedSaying\""), "내용 동일 여부");
    }

    // 삭제 부분
    @Test   // 명언 삭제 테스트
    void testDeleteTemporarySayings() {
        repo.addSaying(1, "Author1", "Saying1");
        boolean deleted = repo.deleteTemporarySaying(1);
        assertTrue(deleted, "삭제 메서드 동작 여부");
        assertTrue(repo.getTemporarySayings().isEmpty(), "삭제 여부");
    }

    @Test   // 저장 명언 삭제 테스트
    void testDeleteSavedSayings() throws IOException {
        File file = new File(path + "/1.json");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("{\"id\": 1, \"author\": \"Author1\", \"content\": \"Saying1\"}");
        }

        boolean deleted = repo.deleteSavedSaying(1);
        assertTrue(deleted, "메서드 동작 여부");
        assertFalse(file.exists(), "파일 삭제 여부");
    }

    // 저장 부분
    @Test
    void testSaveTemporarySaying() {
        repo.addSaying(1, "Author1", "Saying1");
        repo.addSaying(2, "Author2", "Saying2");

        repo.saveTemporarySaying();
        assertTrue(repo.getTemporarySayings().isEmpty(), "파일 정상 저장 여부");

        File file1 = new File(path + "/1.json");
        File file2 = new File(path + "/2.json");

        assertTrue(file1.exists(), "1번 파일 저장 여부");
        assertTrue(file2.exists(), "2번 파일 저장 여부");
    }

    // 기능 부분
    @Test   // 빈 폴더에서 id 반환 테스트
    void testGetNextId_FileNotExist() {
        int nextId = repo.getNextId();
        assertEquals(1, nextId, "파일이 없으면 1이 출력되어야 함");
    }

    @Test   // 폴더에서 id 반환 테스트
    void testGetNextId_FileExist() throws IOException {
        File idFile = new File(path + "/lastId.txt");
        try(var writer = new java.io.FileWriter(idFile)) {
            writer.write("10");
        }

        int nextId = repo.getNextId();
        assertEquals(10, nextId, "id로 10을 가져와야 함");
    }

    @Test   // id 갱신 테스트
    void testUpdateIdFile() throws IOException {
        repo.updateIdFile(20);

        File idFile = new File(path + "/lastId.txt");
        assertTrue(idFile.exists(), "파일이 존재해야함");

        String content = new String(java.nio.file.Files.readAllBytes(idFile.toPath()));
        assertEquals("20", content.trim(), "20으로 갱신되어야 함");
    }
}
