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
        else if (query.equals("목록")) {
            printSaying();
        }

        //명언 삭제
        else if (query.contains("삭제?id=")) {
            int deleteId = getParamAsInt(query);

            deleteSaying(deleteId);
        }

        //명언 수정
        else if (query.contains("수정?id=")) {
            int editNum = getParamAsInt(query);

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

    //명언 입력
    public void writeSaying(){
        System.out.print("명언 : ");
        String saying = scanner.nextLine();
        System.out.print("작가 : ");
        String name = scanner.nextLine();

        int id = service.addSaying(saying, name);
        System.out.println(id + "번 명언이 등록되었습니다.");
    }

    //명언 출력
    public void printSaying(){
        System.out.println("번호 / 작가 / 명언\n----------------------");
        
        List<WiseSaying> tempSayings = service.getTempSayings();
        Collections.reverse(tempSayings);
        tempSayings.forEach(wiseSaying -> System.out.printf("%d / %s / %s (저장되지 않음)%n",
                wiseSaying.getId(), wiseSaying.getAuthor(), wiseSaying.getContent()));

        List<String> SavedSayings = service.getSavedSayings();
        SavedSayings.forEach(System.out::println);
    }

    //명언 삭제
    public void deleteSaying(int id){
        boolean deleted = service.deleteSaying(id);

        if(deleted){
            System.out.println(id + "번 명언이 삭제되었습니다.");
        }
        else{
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    //명언 수정
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

    //명언 저장
    public void saveSaying(){
        service.saveSaying();
        System.out.println("작성한 모든 명언이 저장되었습니다.");
    }

    // 입력값에서 id추출
    public int getParamAsInt(String param) {
        try{
            return Integer.parseInt(param.replaceAll("[^0-9]", ""));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
