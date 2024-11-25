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

    // 아래 주석 처리 된 부분을 해결 중인데, 잘 해결되지 않습니다. 혹시 어떤 식으로 해결해야 되는지
    // 궁금합니다.
    // static을 제거 한다는 것인가요?
    // 강사님께서 서비스랑 레포는 현재 코드파일에서 필요가 없기 때문에 제거하라고 피드백 주셔서
    // 제거를 하고 코드를 수정하였는데, 테스트 작업에서 계속해서 문제가 생겨서
    // 방법이 잘못됐다고 판단했습니다.
    // 제거 방법 다시 찾아봐야 될 듯함
    // 이런식으로 해봄은 어떠할까요?
    // 혹시 config를 private로 설정해도 문제가 되지는 않을까요? 경로를 숨기고 싶은데
    // private로 설정해도 될지 궁금합니다.
    // config을 컨트롤러와 서비스 레포지터리로 내려줄때에 인스턴스의 프로퍼티로 저장하지 않기때문에
    // 외부에서 수정 할 수는 없습니다 할수는 있는데 getter만 잘 설정해주면 되겠습니다.

    // 그 부분은 공부해서 해결해보겠습니다. 감사합니다

    Config config = new Config();
    WiseSayingController controller = new WiseSayingController(config);

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