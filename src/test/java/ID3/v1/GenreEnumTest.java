package ID3.v1;

import ID3.v1.GenreEnum;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Tom Whitford on 11/12/2015.
 */
public class GenreEnumTest {

    @Test
    public void testLookup()
    {
        String blues = "BLUES";
        assertEquals(blues, GenreEnum.get(0).name());


        String dhall = "DANCEHALL";
        assertEquals(dhall, GenreEnum.get(125).name());


    }

}