//public class WiseSayingServiceTest {
//    private static String path = "db/wiseSayingTest";
//    private WiseSayingService service;
//    private WiseSayingRepository repo;
//
//    @BeforeEach
//    public void setUp() {
//        /*
//        File testFolder = new File(path);
//        if (!testFolder.exists()) {
//            testFolder.mkdirs();  // 테스트 폴더가 없으면 생성
//        }
//
//        // 기존 파일 삭제 (매 테스트 전에 초기화)
//        for (File file : testFolder.listFiles()) {
//            file.delete();
//        }
//
//         */
//        repo = mock(WiseSayingRepository.class);  // 경로는 mock 처리
//        service = new WiseSayingService();
//    }
//
//    @Test   // 명언 추가 테스트
//    public void testAddSaying() {
//        String author = "author1";
//        String content = "content1";
//
//        when(repo.getNextId()).thenReturn(1);
//
//        int id = service.addSaying(author, content);
//
//        verify(repo, times(1)).addSaying(1, author, content);
//        assertEquals(1, id, "id 동일 여부 확인");
//    }
//
//    @Test   // 임시 명언 불러오기 테스트
//    public void testGetTempSayings() {
//        repo.addSaying(1, "author1", "content1");
//        repo.addSaying(2, "author2", "content2");
//
//        Map<Integer, WiseSaying> temp = repo.getTemporarySayings();
//
//        assertEquals("2 / author2 / content2 (저장되지 않음)",
//                String.format("%d / %s / %s (저장되지 않음)", temp.get(2).getId(), temp.get(2).getAuthor(), temp.get(2).getContent()));
//
//        assertEquals("1 / author1 / content1 (저장되지 않음)",
//                String.format("%d / %s / %s (저장되지 않음)", temp.get(1).getId(), temp.get(1).getAuthor(), temp.get(1).getContent()));
//
//    }
//
//    @Test   // 저장된 명언 불러오기 테스트
//    public void testGetSavedSayings() {
//        repo.addSaying(3, "author3", "content3");
//
//        List<String> allSayings = service.getSavedSayings();
//
//        assertEquals(1, allSayings.size(), "목록 크기 동일 여부 확인");
//        assertEquals("3 / author3 / content3", allSayings.get(0), "id3");
//    }
//
//    @Test   // 명언 수정 테스트
//    public void testUpdateSaying() {
//        when(repo.getTemporarySayings()).thenReturn(
//                (HashMap<Integer, WiseSaying>) Map.of(1, new WiseSaying(1, "author1", "content1"))
//        );
//        when(repo.updateTemporarySaying(1,"author1", "content1")).thenReturn(true);
//        when(repo.updateSavedSaying(2, "author2", "content2")).thenReturn(true);
//
//        boolean tempResult = service.updateSaying(1, "author1", "content1");
//        assertTrue(tempResult, "임시 파일 수정 여부 확인");
//
//        boolean savedResult = service.updateSaying(2, "author2", "content2");
//        assertTrue(savedResult, "저장 파일 수정 여부 확인");
//
//        verify(repo).updateTemporarySaying(1, "author1", "content1");
//        verify(repo).updateSavedSaying(2, "author2", "content2");
//    }
//
//    @Test   // 명언 삭제 테스트
//    public void testDeleteSaying() {
//        when(repo.getTemporarySayings()).thenReturn(
//                (HashMap<Integer, WiseSaying>) Map.of(1, new WiseSaying(1, "author1", "content1"),
//                        2, new WiseSaying(2, "author2", "content2"))
//        );
//        when(repo.deleteTemporarySaying(2)).thenReturn(true);
//        when(repo.deleteSavedSaying(3)).thenReturn(true);
//
//        boolean tempResult = service.deleteSaying(2);
//        assertTrue(tempResult, "임시 파일 삭제 여부 확인");
//
//        boolean savedResult = service.deleteSaying(3);
//        assertTrue(savedResult, "저장 파일 삭제 여부 확인");
//
//        verify(repo).deleteTemporarySaying(2);
//        verify(repo).deleteSavedSaying(3);
//    }
//
//    @Test   // 명언 저장 테스트
//    public void testSaveSaying() throws IOException {
//        Map<Integer, WiseSaying> tempList = Map.of(1, new WiseSaying(1, "author1", "content1"),
//                                                2, new WiseSaying(2, "author2", "content2"));
//
//        when(repo.getTemporarySayings()).thenReturn((HashMap<Integer, WiseSaying>) tempList);
//
//        service.saveSaying();
//
//        verify(repo).saveTemporarySaying();
//
////        File savedFile1 = new File("db/wiseSaying/1.json");
////        File savedFile2 = new File("db/wiseSaying/2.json");
////
////        assertTrue(savedFile1.exists(), "파일1 존재 유무 확인");
////        assertTrue(savedFile2.exists(), "파일2 존재 유무 확인");
////
////        String savedContent1 = new String(Files.readAllBytes(savedFile1.toPath()));
////        String savedContent2 = new String(Files.readAllBytes(savedFile2.toPath()));
////
////        assertTrue(savedContent1.contains("Author1"), "author1 확인");
////        assertTrue(savedContent1.contains("Saying1"), "content1 확인");
////
////        assertTrue(savedContent2.contains("Author2"), "author2 확인");
////        assertTrue(savedContent2.contains("Saying2"), "content2 확인");
//    }
//
//}
