package ID3.v2;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by Tom Whitford on 17/12/2015.
 */
public class v2_3Test {

    File fileWithTag;
    File fileWithExtendedTag;
    File fileWithNoTag;

    @Before
    public void initialise()
    {
        fileWithTag = null;
        fileWithExtendedTag = null;
        fileWithNoTag = null;
    }

    @Test
    public void testConstructorExtendedTag() throws IOException {
        v2_3 tag = new v2_3(fileWithExtendedTag);
        assertTrue(tag.hasExtendedHeader());

        tag = new v2_3(fileWithNoTag);
        assertFalse(tag.hasExtendedHeader());

        tag = new v2_3(fileWithTag);
        assertTrue(tag.hasExtendedHeader());
    }

    @Test
    public void testHasTag() throws IOException {
        assertTrue(v2_3.hasTag(fileWithTag));
        assertFalse(v2_3.hasTag(fileWithNoTag));
    }

    @Test (expected = IOException.class)
    public void testHasTag_ThrowsIOException() throws IOException {
        v2_3.hasTag(new File("ARANDOMFILE"));
    }

    @Test
    public void testSetFrame_Exception_FrameSize0()
    {
        fail();
    }
}