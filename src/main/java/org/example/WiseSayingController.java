package org.example;

import java.util.List;
import java.util.Scanner;

public class WiseSayingController {
    private final WiseSayingService service;
    private final Scanner scanner = new Scanner(System.in);

    public WiseSayingController(WiseSayingService service) {
        this.service = service;
    }

    //명언 입력
    public void writeSaying(){
        System.out.print("명언 : ");
        String saying = scanner.nextLine();
        System.out.print("작가 : ");
        String name = scanner.nextLine();

        int nextId = service.addSaying(saying, name);
        System.out.println(nextId + "번 명언이 등록되었습니다.");
    }

    //명언 출력
    public void printSaying(){
        List<String> sayings = service.getAllSayings();

        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");

        for(String saying : sayings){
            System.out.println(saying);
        }
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
    public void saveSaying(String path){
        service.saveSaying();
        System.out.println("작성한 모든 명언이 저장되었습니다.");
    }
}
