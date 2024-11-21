package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.run();

    }
}

// getNextId 가져올 때, txt파일로 간편하게 최신화하게끔 코드 수정 필요
// 등록 과정에서 txt파일 갱신

class App {
    static Scanner scanner = new Scanner(System.in);

    static String path = "db/wiseSaying";

    static WiseSayingRepository repo = new WiseSayingRepository(path);
    static WiseSayingService service = new WiseSayingService(repo);
    static WiseSayingController controller = new WiseSayingController(service);

    public static void run() {
        System.out.println("== 명언 앱 ==");   //시작 시 보이는 글짜 출력

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
                int deleteId = service.getParamAsInt(query);

                controller.deleteSaying(deleteId);
            }

            //명언 수정
            else if (query.contains("수정?id=")) {
                int editNum = service.getParamAsInt(query);

                controller.modifySaying(editNum);
            }

            //명언 저장
            else if (query.equals("빌드")) {
                controller.saveSaying(path);
            }

            //종료
            else if (query.equals("종료")) {
                break;
            }
        }
    }
}