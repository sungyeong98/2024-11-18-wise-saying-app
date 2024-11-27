package org.example;

public class Main {
    public static void main(String[] args) {
        WiseSayingController controller = new WiseSayingController(new WiseSayingService(new WiseSayingRepository()));

        controller.run();
    }
}