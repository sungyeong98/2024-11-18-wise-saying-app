package org.example;


import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private final WiseSayingService service;
    private final Scanner scanner = new Scanner(System.in);

    public WiseSayingController(Config config) {
        WiseSayingRepository repo = new WiseSayingRepository(config);
        this.service = new WiseSayingService(repo);
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
