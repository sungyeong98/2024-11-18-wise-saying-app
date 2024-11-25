import org.example.Config;

public class TestConfig extends Config {
    public TestConfig() {
        this.dbPath = "db/wiseSayingTest"; // 상위 클래스의 필드 덮어쓰기
    }
}
