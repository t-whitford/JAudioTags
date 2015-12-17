package ID3.v1;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Tom Whitford on 16/12/2015.
 */
public class v1_1Test{

    public String tag = "TAG";
    public final String title =    "TitleTest                     ";
    public final String artist =   "ArtistTest                    ";
    public final String album =    "AlbumTest                     ";
    public final String year =     "4567";
    public final String comment =  "CommentTest                   ";
    public final int track = 1;
    public final int genre = 1;


    v1_1 v1;

    byte[] emptyTagBytes;
    byte[] fullTagBytes;

    File tempNoTag;
    File tempWithTag;

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Before
    public void setUp() throws IOException {

        fullTagBytes = new byte[128];
        emptyTagBytes = new byte[128];

        int pos = 0;

        for (byte b : "TAG".getBytes()) {
            emptyTagBytes[pos] = b;
            fullTagBytes[pos] = b;
            pos++;
        }


        for (byte b : (title + artist + album + year + comment).getBytes()) {
            if (b == 32)
                b = 0;

            fullTagBytes[pos] = b;
            pos++;
        }
        fullTagBytes[126] = (byte) track;
        fullTagBytes[127] = (byte) genre;


        buildRandomTestFile(false);
        buildRandomTestFile(true);

        v1 = new v1_1(tempWithTag);
    }

    private void buildRandomTestFile(boolean withTag) throws IOException {

        BufferedOutputStream os;

        if (!withTag) {
            tempNoTag = testFolder.newFile("tempNoTag.mp3");
            os = new BufferedOutputStream(new FileOutputStream(tempNoTag));
        } else {
            tempWithTag = testFolder.newFile("tempWithTag.mp3");
            os = new BufferedOutputStream(new FileOutputStream(tempWithTag));
        }

        Random rand = new Random();

        int random = rand.nextInt(10000);
        for (int i = 0; i < random; i++) {
            os.write(rand.nextInt(256));
        }

        if (withTag) {
            os.write(fullTagBytes);
        }

        os.flush();
        os.close();
    }

    @Test
    public void testSetTrack() throws IOException {
        v1.setTrack((byte)2);
        assertEquals(2, v1.getTrack());
    }


    @Test
    public void testSetComment() throws IOException {
        v1.setComment("NewComment!");
        assertEquals("NewComment!", v1.getComment());
    }

    @Test (expected = IllegalArgumentException.class)
    public void testSetComment_ThrowsExceptionTooBig() throws IOException {
        String tooBig = new String(new char[29]);
        v1.setComment(tooBig);
    }
}