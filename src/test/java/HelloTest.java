import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Tom Whitford on 09/12/2015.
 */
public class HelloTest {

    @Test
    public void testHello(){

        int result = Hello.returnZero();
        assertEquals(result, 0);

    }

}