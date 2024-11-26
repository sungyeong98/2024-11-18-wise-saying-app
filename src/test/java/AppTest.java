//import org.example.App;
//import org.example.WiseSayingController;
//import org.example.WiseSayingRepository;
//import org.example.WiseSayingService;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.util.Scanner;
//
//public class AppTest {
//    private App app;
//    private WiseSayingController controller;
//    private WiseSayingService service;
//    private WiseSayingRepository repository;
//    private ByteArrayOutputStream output;
//
//    @Before
//    public void setUp() {
//        repository = new WiseSayingRepository("db/wiseSayingTest");
//        service = new WiseSayingService(repository);
//        controller = new WiseSayingController();
//        app = new App();
//        output = TestUtil.setOutToByteArray();
//    }
//
//    @After
//    public void tearDown() {
//        TestUtil.clearSetOutToByteArray(output);
//        File folder = new File("db/wiseSayingTest");
//        if(folder.exists()){
//            folder.delete();
//        }
//    }
//
//    @Test
//    public void test() {
//        Scanner scanner = TestUtil.genScanner("""
//
//                """);
//    }
//}