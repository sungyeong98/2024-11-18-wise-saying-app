package org.example;

public class Main {
    public static void main(String[] args) {
//        App app = new App();
//        app.run();

        WiseSayingController controller = new WiseSayingController(new WiseSayingService(new WiseSayingRepository()));

        controller.run();
    }
}