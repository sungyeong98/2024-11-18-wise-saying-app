// Util 클래스의 테스트 코드

import org.example.Command;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static junit.framework.TestCase.assertEquals;

public class CommandTest {
    private ByteArrayOutputStream output;
    private Command command;

    @Before
    public void setUp(){
        output = TestUtil.setOutToByteArray();
    }

    @After
    public void tearDown(){
        TestUtil.clearSetOutToByteArray(output);
    }

    @Test
    public void test() {
        command = new Command();

        String result = command.getKeywordType("목록?keywordType=content&keyword=과거");
        assertEquals("content", result);

        result = command.getKeyword("목록?keywordType=content&keyword=과거");
        assertEquals("과거", result);

        int result1 = command.getPage("목록?page=10");
        assertEquals(10, result1);

        result1 = command.getPage("목록?page=");
        assertEquals(1, result1);

        result1 = command.getPage("목록?page");
        assertEquals(1, result1);
    }

    @Test
    public void test2(){
        command = new Command();

        int result = command.getId("수정?");
        assertEquals(0, result);

        result = command.getId("수정? ");
        assertEquals(0, result);

        result = command.getId("수정?=");
        assertEquals(0, result);

        result = command.getId("수정?=10");
        assertEquals(10, result);
    }
}
