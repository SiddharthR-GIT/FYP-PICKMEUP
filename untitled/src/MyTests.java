import org.junit.Test;
        import static org.junit.Assert.*;
public class MyTests {
    @Test
    public void multiplicationofZeroIntegersShouldReturnZeo(){
        MyClass tester = new MyClass();

        assertEquals(0,tester.multiply(10,0));
        assertEquals(0,tester.multiply(0,10));
        assertEquals(100,tester.multiply(10,10));

    }
}
