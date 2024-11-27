import org.example.WiseSayingController;
import org.example.WiseSayingRepository;
import org.example.WiseSayingService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;

public class WiseSayingControllerTest {
    private WiseSayingController controller;
    private WiseSayingService service;
    private WiseSayingRepository repo;
    private ByteArrayOutputStream output;

    @Before
    public void setUp(){
        repo = new WiseSayingRepository("db/wiseSayingTest");
        service = new WiseSayingService(repo);
        output = TestUtil.setOutToByteArray();
    }

    @After
    public void tearDown(){
        TestUtil.clearSetOutToByteArray(output);
        File folder = new File("db/wiseSayingTest");
        File[] files = folder.listFiles();
        if(files != null){
            for(File file : files){
                file.delete();
            }
        }
        folder.delete();
    }

    @DisplayName("명언 등록 테스트")
    @Test
    public void testWrite(){
        Scanner scanner = TestUtil.genScanner("""
                등록
                내용1
                작가1
                """);
        controller = new WiseSayingController(service, scanner);
        controller.Command();

        String result = output.toString();
        assertThat(result).contains("명언 : ").contains("작가 : ").contains("1번 명언이 등록되었습니다.");
        assertThat(repo.getTemporarySayings().toString()).isEqualTo("{1=1 / 내용1 / 작가1}");
    }

    @DisplayName("명언 출력 테스트")
    @Test
    public void testPrint(){
        Scanner scanner = TestUtil.genScanner("목록");
        repo.addSaying(1,"작가1", "내용1");

        controller = new WiseSayingController(service, scanner);
        controller.Command();

        String result = output.toString();
        assertThat(result).contains("1 / 작가1 / 내용1 (저장되지 않음)");
    }

    @DisplayName("명언 출력 테스트(키워드 입력)")
    @Test
    public void testPrint2(){
        Scanner scanner = TestUtil.genScanner("목록?keywordType=content&keyword=과거");
        repo.addSaying(1,"작가1", "현재1");
        repo.addSaying(2,"작가2", "과거1");
        repo.addSaying(3,"작가3", "현재2");
        repo.addSaying(4,"작가4", "과거2");
        repo.addSaying(5,"작가5", "현재3");
        repo.addSaying(6,"작가6", "과거3");
        repo.addSaying(7,"작가7", "현재4");
        repo.addSaying(8,"작가8", "과거4");
        repo.addSaying(9,"작가9", "현재5");

        controller = new WiseSayingController(service, scanner);
        controller.Command();

        String result = output.toString();

        assertThat(result)
                .contains("----------------------")
                .contains("검색타입 : content")
                .contains("검색어  : 과거")
                .contains("----------------------")
                .contains("번호 / 작가 / 명언\n----------------------")
                .contains("8 / 작가8 / 과거4 (저장되지 않음)")
                .contains("6 / 작가6 / 과거3 (저장되지 않음)")
                .contains("4 / 작가4 / 과거2 (저장되지 않음)")
                .contains("2 / 작가2 / 과거1 (저장되지 않음)");
    }

    @DisplayName("명언 삭제 테스트(정상 삭제)")
    @Test
    public void testDelete(){
        Scanner scanner = TestUtil.genScanner("삭제?id=1");
        repo.addSaying(1,"작가1", "내용1");

        controller = new WiseSayingController(service, scanner);
        controller.Command();

        String result = output.toString();
        assertThat(result).contains("1번 명언이 삭제되었습니다.");

        assertThat(repo.getTemporarySayings()).hasSize(0);
    }

    @DisplayName("명언 삭제 테스트(삭제 실패시)")
    @Test
    public void testDelete2(){
        Scanner scanner = TestUtil.genScanner("삭제?id=1");

        controller = new WiseSayingController(service, scanner);
        controller.Command();

        String result = output.toString();
        assertThat(result).contains("1번 명언은 존재하지 않습니다.");
    }

    @DisplayName("명언 수정 테스트(정상 수정)")
    @Test
    public void testModify(){
        Scanner scanner = TestUtil.genScanner("""
                수정?id=1
                내용수정1
                작가수정1
                """);
        repo.addSaying(1,"작가1", "내용1");

        controller = new WiseSayingController(service, scanner);
        controller.Command();

        String result = output.toString();
        assertThat(result).contains("새로운 명언: ").contains("새로운 작가: ");
        assertThat(repo.getTemporarySayings().toString()).isEqualTo("{1=1 / 내용수정1 / 작가수정1}");;
    }

    @DisplayName("명언 수정 테스트(수정 실패시)")
    @Test
    public void testModify2(){
        Scanner scanner = TestUtil.genScanner("""
                수정?id=1
                내용수정1
                작가수정1
                """);

        controller = new WiseSayingController(service, scanner);
        controller.Command();

        String result = output.toString();
        assertThat(result).contains("1번 명언은 존재하지 않습니다.");
    }

    @DisplayName("저장 테스트")
    @Test
    public void testSave(){
        Scanner scanner = TestUtil.genScanner("빌드");
        repo.addSaying(1,"작가1", "내용1");

        controller = new WiseSayingController(service, scanner);
        controller.Command();

        String result = output.toString();
        assertThat(result).contains("작성한 모든 명언이 저장되었습니다.");

        List<String> temp = repo.getSavedSayings();
        assertThat(temp.get(0).toString()).contains("1 / 작가1 / 내용1");
    }
}