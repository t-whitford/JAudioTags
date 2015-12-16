package ID3;

import com.sun.istack.internal.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Tom Whitford on 09/12/2015.
 *
 */
public class ID3v1_0 {

    final int TITLEPOS = 3;
    final int ARTISTPOS = 33;
    final int ALBUMPOS = 63;
    final int YEARPOS = 93;
    final int COMMENTPOS = 97;
    final int GENREPOS = 127;

    LinkedHashMap<String, byte[]> map;

    File file;

    private void buildMapFromBytes(byte[] input) {
        map = new LinkedHashMap<String, byte[]>(6);

        map.put("Title", getSubArray(input, TITLEPOS, TITLEPOS + 29));
        map.put("Artist", getSubArray(input, ARTISTPOS, ARTISTPOS + 29));
        map.put("Album", getSubArray(input, ALBUMPOS, ALBUMPOS + 29));
        map.put("Year", getSubArray(input, YEARPOS, YEARPOS + 3));
        map.put("Comment", getSubArray(input, COMMENTPOS, COMMENTPOS + 29));
        map.put("Genre", getSubArray(input, GENREPOS, GENREPOS));

    }

    public ID3v1_0(@NotNull File file) throws IOException {

        this.file = file;

        if(!ID3v1_0.hasTag(file))
        {
            map = new LinkedHashMap<String, byte[]>();

            map.put("Title", new byte[30]);
            map.put("Artist", new byte[30]);
            map.put("Album", new byte[30]);
            map.put("Year", new byte[4]);
            map.put("Comment", new byte[30]);
            map.put("Genre", new byte[1]);

            writeTagToFile();
        }
        else
        {
            BufferedInputStream is = new BufferedInputStream(new FileInputStream(file));
            byte[] tag = new byte[128];
            is.skip(file.length() - 128);
            is.read(tag);
            buildMapFromBytes(tag);
            is.close();
        }
    }

    private void writeTagToFile() throws IOException {
        File temp = new File(file.getParentFile(), "temp");
        Files.copy(file.toPath(), temp.toPath());

        long posToPlaceTag;

        if(hasTag(temp))
            posToPlaceTag = temp.length() - 128;
        else
            posToPlaceTag = temp.length();

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(temp));
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));

        for(int i = 0; i < posToPlaceTag; i++)
        {
            os.write(in.read());
        }

        os.write(getByteArray());

        in.close();
        os.flush();
        os.close();

        temp.delete();
    }

    public HashMap<String, byte[]> getMap() {
        return map;
    }

    public String getTitle() {
        return new String(map.get("Title")).trim();
    }

    private void setTag(String tagName, byte[] newContent) throws IOException {
        byte[] arr = map.get(tagName);
        for (int i = 0; i < arr.length; i++) {
            if (i < newContent.length) {
                arr[i] = newContent[i];
            } else
                arr[i] = 0;
        }
        writeTagToFile();
    }

    public String getArtist() {
        return new String(map.get("Artist")).trim();
    }

    public String getAlbum() {
        return new String(map.get("Album")).trim();
    }

    public String getYear() {
        return new String(map.get("Year"));
    }

    public String getComment() {
        return new String(map.get("Comment")).trim();
    }

    public int getGenre() {
        return map.get("Genre")[0];
    }

    private static byte[] getSubArray(byte[] array, int start, int end) {
        int size = end - start + 1;

        if (size < 0)
            throw new IllegalArgumentException("End is before start");

        byte[] result = new byte[size];


        for (int i = 0; i < size; i++) {
            result[i] = array[i + start];
        }
        return result;
    }

    public byte[] getByteArray() {
        byte[] result = new byte[128];

        String tag = "TAG";

        int pos = 0;
        for (byte b : tag.getBytes()) {
            result[pos] = b;
            pos++;
        }


        for (byte[] barray : map.values()) {
            for (byte b : barray) {
                result[pos] = b;
                pos++;
            }
        }
        return result;

    }

    public void setTitle(String title) throws IOException {
        setTag("Title", title.getBytes());
    }

    public void setArtist(String newArt) throws IOException {
        setTag("Artist", newArt.getBytes());
    }

    public void setAlbum(String newArt) throws IOException {
        setTag("Album", newArt.getBytes());
    }

    public void setComment(String comment) throws IOException {
        setTag("Comment", comment.getBytes());
    }

    public void setYear(String newYear) throws IOException {
        setTag("Year", newYear.getBytes());
    }

    public void setGenre(byte newGenre) throws IOException {
        byte[] tempArr = {newGenre};
        setTag("Genre", (tempArr));
    }

    public static boolean hasTag(File file) throws IOException {

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

        long tagLocation = (file.length() - 128);
        bis.skip(tagLocation);

        byte[] in = new byte[3];
        bis.read(in);

        bis.close();

        return (new String(in).compareTo("TAG") == 0);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File newFile)
    {
        this.file = newFile;
    }
}
