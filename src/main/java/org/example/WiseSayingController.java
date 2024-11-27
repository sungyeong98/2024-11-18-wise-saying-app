package org.example;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private WiseSayingService service;
    private final Scanner scanner;

    // 실사용
    public WiseSayingController(WiseSayingService service) {
        WiseSayingRepository repo = new WiseSayingRepository();
        this.service = new WiseSayingService(repo);
        this.scanner = new Scanner(System.in);
    }

    // 테스트용
    public WiseSayingController(WiseSayingService service, Scanner scanner) {
        this.scanner = scanner;
        this.service = service;
    }

    // 루프
    public void run(){
        boolean flag = true;

        System.out.println("== 명언 앱 ==");

        while(flag) {
            flag = Command();
        }
    }

    // 동작
    public boolean Command() {
        System.out.print("명령) ");
        String query = scanner.nextLine();

        //명언 등록
        if (query.equals("등록")) {
            writeSaying();
        }

        //명언 목록
        else if (query.startsWith("목록")) {
            String keywordType = Command.getKeywordType(query);
            String keyword = Command.getKeyword(query);
            int page = Command.getPage(query);

            if(keywordType!=null && keyword!=null) {
                printSaying(keywordType, keyword, page);
                printPageInfo(keywordType, keyword, page);
            }
            else{
                printSaying(page);
                printPageInfo(page);
            }
        }

        //명언 삭제
        else if (query.startsWith("삭제")) {
            int deleteId = Command.getId(query);

            deleteSaying(deleteId);
        }

        //명언 수정
        else if (query.startsWith("수정?id=")) {
            int editNum = Command.getId(query);

            modifySaying(editNum);
        }

        //명언 저장
        else if (query.equals("빌드")) {
            saveSaying();
        }

        //종료
        else if (query.equals("종료")) {
            return false;
        }

        return true;
    }

    // 명언 입력
    public void writeSaying(){
        System.out.print("명언 : ");
        String content = scanner.nextLine();
        System.out.print("작가 : ");
        String name = scanner.nextLine();

        int id = service.addSaying(name, content);
        System.out.println(id + "번 명언이 등록되었습니다.");
    }

    // 명언 출력(사용X)
    public void printSaying(){
        System.out.println("번호 / 작가 / 명언\n----------------------");

        List<WiseSaying> tempSayings = service.getTempSayings();
        Collections.reverse(tempSayings);
        tempSayings.forEach(wiseSaying -> System.out.printf("%d / %s / %s (저장되지 않음)%n",
                wiseSaying.getId(), wiseSaying.getAuthor(), wiseSaying.getContent()));

        List<String> SavedSayings = service.getSavedSayings();
        SavedSayings.forEach(System.out::println);
    }

    // 명언 출력(사용X)
    public void printSaying(String keywordType, String keyword){
        System.out.println("----------------------");
        System.out.printf("검색타입 : %s\n".formatted(keywordType));
        System.out.printf("검색어  : %s\n".formatted(keyword));
        System.out.println("----------------------");

        System.out.println("번호 / 작가 / 명언\n----------------------");

        List<WiseSaying> tempSayings = service.getTempSayings(keywordType, keyword);
        Collections.reverse(tempSayings);
        tempSayings.forEach(wiseSaying -> System.out.printf("%d / %s / %s (저장되지 않음)%n",
                wiseSaying.getId(), wiseSaying.getAuthor(), wiseSaying.getContent()));

        List<String> SavedSayings = service.getSavedSayings(keywordType, keyword);
        SavedSayings.forEach(System.out::println);
    }

    // 명언 출력(페이지)   ->  최종
    public void printSaying(int page){
        System.out.println("번호 / 작가 / 명언\n----------------------");

        List<String> sayings = service.getSayings(page);
        sayings.forEach(System.out::println);

        System.out.println("----------------------");
    }

    // 명언 출력(키워드)   ->  최종
    public void printSaying(String keywordType, String keyword, int page){
        System.out.println("----------------------");
        System.out.printf("검색타입 : %s\n".formatted(keywordType));
        System.out.printf("검색어  : %s\n".formatted(keyword));
        System.out.println("----------------------");
        System.out.println("번호 / 작가 / 명언\n----------------------");

        List<String> sayings = service.getSayings(keywordType,keyword,page);
        sayings.forEach(System.out::println);

        System.out.println("----------------------");
    }

    // 명언 삭제
    public void deleteSaying(int id){
        boolean deleted = service.deleteSaying(id);

        if(deleted){
            System.out.println(id + "번 명언이 삭제되었습니다.");
        }
        else{
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    // 명언 수정
    public void modifySaying(int id) {
        System.out.print("새로운 명언: ");
        String newSaying = scanner.nextLine();
        System.out.print("새로운 작가: ");
        String newAuthor = scanner.nextLine();

        boolean modified = service.updateSaying(id, newSaying, newAuthor);
        if (modified) {
            System.out.println(id + "번 명언이 수정되었습니다.");
        }
        else{
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    // 명언 저장
    public void saveSaying(){
        service.saveSaying();
        System.out.println("작성한 모든 명언이 저장되었습니다.");
    }

    // 페이지 출력
    public void printPageInfo(int page){
        int dataSize = service.getSayingsSize();

        int totalPages = (dataSize + 5 - 1) / 5;

        StringBuilder pageInfo = new StringBuilder("페이지 : ");
        for(int i = 1; i <= totalPages; i++){
            if(i == page){
                pageInfo.append("[").append(i).append("]");
            }
            else{
                pageInfo.append(i);
            }
            if(i < totalPages){
                pageInfo.append(" / ");
            }
        }

        System.out.println(pageInfo);
    }

    // 페이지 출력(키워드)
    public void printPageInfo(String keywordType, String keyword, int page){
        int dataSize = service.getSayingsSize(keywordType, keyword);

        int totalPages = (dataSize + 5 - 1) / 5;

        StringBuilder pageInfo = new StringBuilder("페이지 : ");
        for(int i = 1; i <= totalPages; i++){
            if(i == page){
                pageInfo.append("[").append(i).append("]");
            }
            else{
                pageInfo.append(i);
            }
            if(i < totalPages){
                pageInfo.append(" / ");
            }
        }

        System.out.println(pageInfo);
    }
}
