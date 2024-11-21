package org.example;

import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.run();

    }
}

class App {
    static Scanner scanner = new Scanner(System.in);

    static HashMap<Integer, String[]> sayingList = new HashMap<Integer, String[]>();   //입력받은 정보 임시 저장공간
    static String path = "db/wiseSaying";

    //Controller, Service, Repository       ->      수정 필요!!!
    static WiseSayingRepository repo = new WiseSayingRepository(path);
    static WiseSayingService service = new WiseSayingService(repo);
    static WiseSayingController controller = new WiseSayingController(service);

    //주 구동 함수       ->      구조 변경 필요!!!
    public static void run() {
        System.out.println("== 명언 앱 ==");   //시작 시 보이는 글짜 출력

        //File folder = new File(path);

        while (true) {
            System.out.print("명령) ");
            String query = scanner.nextLine();

            //명언 등록
            if (query.equals("등록")) {
                controller.writeSaying();
            }

            //명언 목록
            else if (query.equals("목록")) {
                controller.printSaying();
            }

            //명언 삭제
            else if (query.contains("삭제?id=")) {
                int deleteId = Integer.parseInt(query.substring(query.indexOf("=")+1));

                controller.deleteSaying(deleteId);
            }

            //명언 수정
            else if (query.contains("수정?id=")) {
                int editNum = Integer.parseInt(query.substring(query.indexOf("=")+1));

                controller.modifySaying(editNum);
            }

            //명언 저장
            else if (query.equals("빌드")) {
                controller.saveSaying(path);

                sayingList.clear();
            }

            //종료
            else if (query.equals("종료")) {
                break;
            }
        }
    }
}