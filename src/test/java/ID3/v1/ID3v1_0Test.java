package ID3.v1;

import ID3.v1.GenreEnum;
import ID3.v1.ID3v1_0;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by Tom Whitford on 09/12/2015.
 */
public class ID3v1_0Test {

    //TODO - Add tests for illegal arguments + other exceptions

    public String tag = "TAG";
    public final String title = "TitleTest                     ";
    public final String artist = "ArtistTest                    ";
    public final String album = "AlbumTest                     ";
    public final String year = "4567";
    public final String comment = "CommentTest                   ";
    public final int genre = 1;


    ID3v1_0 v1;

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

        fullTagBytes[127] = (byte) genre;


        buildRandomTestFile(false);
        buildRandomTestFile(true);

        v1 = new ID3v1_0(tempWithTag);
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
    public void testGetFile() {
        assertEquals(tempWithTag, v1.getFile());
    }

    @Test
    public void testWriteToFile() throws IOException {
        v1.setTitle("NewTitle");
        v1.setArtist("New Artist!");

        ID3v1_0 newTag = new ID3v1_0(v1.getFile());

        assertEquals(v1.getTitle(), newTag.getTitle());
        assertArrayEquals(v1.getByteArray(), newTag.getByteArray());


    }

    @Test
    public void testGetByteArray() {
//        byte[] expected = new byte[128];
//        int pos = 0;
//        for (byte b : (tag + title + artist + album + year + comment).getBytes()) {
//            if (b == 32)
//                b = 0;
//            expected[pos] = b;
//            pos++;
//        }
//        expected[127] = (byte) genre;
        assertArrayEquals(fullTagBytes, v1.getByteArray());
    }

    @Test
    public void testSetTitle() throws IOException {
        v1.setTitle("NewTitle");
        assertEquals("NewTitle", v1.getTitle());
    }

    @Test
    public void testSetArtist() throws IOException {
        String newArt = "New Artist";
        v1.setArtist(newArt);

        assertEquals(newArt, v1.getArtist());
    }

    @Test
    public void testSetAlbum() throws IOException {
        String newAlbum = "New Album!";
        v1.setAlbum(newAlbum);
        assertEquals(newAlbum, v1.getAlbum());
    }

    @Test
    public void testSetComment() throws IOException {
        String newCom = "New Artist";
        v1.setComment(newCom);
        assertEquals(newCom, v1.getComment());
    }

    @Test
    public void testSetYear() throws IOException {
        String newYear = "9191";
        v1.setYear(newYear);
        assertEquals(newYear, v1.getYear());
    }

    @Test
    public void testSetGenre() throws IOException {
        byte newGenre = 6;
        v1.setGenre(newGenre);
        assertEquals(newGenre, v1.getGenre());
        assertEquals(GenreEnum.get(newGenre), GenreEnum.get(v1.getGenre()));
    }

    @Test
    public void testHasTag() throws IOException {
        assertTrue(ID3v1_0.hasTag(tempWithTag));
        assertFalse(ID3v1_0.hasTag(tempNoTag));
    }

    @Test (expected = IOException.class)
    public void testHasTag_ThrowsException_FileDoesntExist() throws IOException {
        ID3v1_0.hasTag(new File("noFile!"));
    }

    @Test
    public void testConstructor_FileWithNoTag() throws IOException {
        ID3v1_0 v1 = new ID3v1_0(tempNoTag);
        assertArrayEquals(emptyTagBytes, v1.getByteArray());
    }

    @Test
    public void testConstructor_FileWithTag() throws IOException {
        ID3v1_0 v1 = new ID3v1_0(tempWithTag);
        assertArrayEquals(fullTagBytes, v1.getByteArray());
    }

    @Test(expected = IOException.class)
    public void testConstructor_FileDoesntExit() throws IOException {
        ID3v1_0 temp = new ID3v1_0(new File("AFAIL"));
    }



}