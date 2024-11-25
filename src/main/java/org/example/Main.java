package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        App app = new App();
        app.run();

    }
}


//app에서 static 전부 제거
class App {
    static Scanner scanner = new Scanner(System.in);

    private static String path = "db/wiseSaying";

    WiseSayingRepository repo = new WiseSayingRepository(path);  //이거 제거
    WiseSayingService service = new WiseSayingService(repo); //이거 제거
    WiseSayingController controller = new WiseSayingController(service);

    public void run() {
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
                int deleteId = controller.getParamAsInt(query);

                controller.deleteSaying(deleteId);
            }

            //명언 수정
            else if (query.contains("수정?id=")) {
                int editNum = controller.getParamAsInt(query);

                controller.modifySaying(editNum);
            }

            //명언 저장
            else if (query.equals("빌드")) {
                controller.saveSaying();
            }

            //종료
            else if (query.equals("종료")) {
                break;
            }
        }
    }
}