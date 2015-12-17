package ID3.v1;

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

    /**
     * The constructor will either build an empty tag into the file, or extract an existing tag.
     * @param file An mp3 file. This operation will change the file.
     * @throws IOException
     */
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

    /**
     * Write the tag as is to file
     * @throws IOException
     */
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

    /**
     *
     * @return Returns the map associated with the tag. Each part of the Tag is a seperate entry with a string as the
     * key and byte array as the content.
     *
     */
    public HashMap<String, byte[]> getMap() {
        return map;
    }

    /**
     *
     * @return The title from the tag.
     */
    public String getTitle() {
        return new String(map.get("Title")).trim();
    }

    /**
     * Sets a tag using the tag name. Immidiately writes the new tag to file.
     * @param tagName The string of the key of the tag.
     * @param newContent The byte array of the new content
     * @throws IOException
     */
    protected void setTag(String tagName, byte[] newContent) throws IOException {
        byte[] arr = map.get(tagName);
        for (int i = 0; i < arr.length; i++) {
            if (i < newContent.length) {
                arr[i] = newContent[i];
            } else
                arr[i] = 0;
        }
        writeTagToFile();
    }

    /**
     *
     * @return The artist from the tag.
     */
    public String getArtist() {
        return new String(map.get("Artist")).trim();
    }

    /**
     *
     * @return The album from the tag
     */
    public String getAlbum() {
        return new String(map.get("Album")).trim();
    }

    /**
     *
     * @return The year from the tag.
     */
    public String getYear() {
        return new String(map.get("Year"));
    }

    /**
     *
     * @return The comment from the Tag
     */
    public String getComment() {
        return new String(map.get("Comment")).trim();
    }

    public int getGenre() {
        return map.get("Genre")[0];
    }

    /**
     * Helper class to build arrays
     * @param array The parent array to take section from
     * @param start The start of the subarray
     * @param end The end of the subarray
     * @return The sub array between start and end
     */
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

    /**
     * Builds and returns the full tag in the form of a byte array.
     * @return byte[] of the array, as would be saved in a file. It is 128 characters long, beginning with "TAG". See
     * <a href="http://id3.org/ID3v1">ID3v1</a> for more information about the byte array.
     */
    public byte[] getByteArray() {

        //Add TAG to start, then add all members of the map.
        //Using Map rather than hardcoding allows children classes to change the Map and this method will still work.

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

    /**
     * Changes the Title saved in the tag. This saves immidiately to the file associated with the Tag
     * @param title The title to save.
     * @throws IOException
     */
    public void setTitle(String title) throws IOException {
        setTag("Title", title.getBytes());
    }

    /**
     * Changes the artist saved in the tag. This saves immidiately to the file associated with the Tag
     * @param newArtist The new artist to save.
     * @throws IOException
     */
    public void setArtist(String newArtist) throws IOException {
        setTag("Artist", newArtist.getBytes());
    }

    /**
     * Changes the Album saved in the tag. This saves immidiately to the file associated with the Tag
     * @param newAlbum The album to save.
     * @throws IOException
     */
    public void setAlbum(String newAlbum) throws IOException {
        setTag("Album", newAlbum.getBytes());
    }

    /**
     * Changes the comment saved in the tag. This saves immidiately to the file associated with the Tag
     * @param comment
     * @throws IOException
     */
    public void setComment(String comment) throws IOException {
        setTag("Comment", comment.getBytes());
    }

    /**
     * Changes the Year saved in the tag. This saves immidiately to the file associated with the Tag
     * @param newYear The year to save
     * @throws IOException
     */
    public void setYear(String newYear) throws IOException {
        setTag("Year", newYear.getBytes());
    }

    /**
     * Change the genre of the Tag. This saves immidiately to the file associated with the Tag.
     * @param newGenre A new Genre to save
     * @throws IOException
     */
    public void setGenre(byte newGenre) throws IOException {
        //TODO Check that genre is valid?
        byte[] tempArr = {newGenre};
        setTag("Genre", (tempArr));
    }

    /**
     * A static class to check whether a file has an ID3v1_0 tag
     * @param file The file to inspect
     * @return True if a tag exists (even if empty). False otherwise
     * @throws IOException If the file doesn't exist or an error occurs trying to read the file
     */
    public static boolean hasTag(File file) throws IOException {

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));

        long tagLocation = (file.length() - 128);
        bis.skip(tagLocation);

        byte[] in = new byte[3];
        bis.read(in);

        bis.close();

        return (new String(in).compareTo("TAG") == 0);
    }

    /**
     * Get the file associated with the Tag.
     * @return The file associated with the Tag.
     */
    public File getFile() {
        return file;
    }

    /**
     * Change the file associated with the Tag
     * @param newFile The new file. This is where any changes are saved to.
     */
    public void setFile(File newFile)
    {
        this.file = newFile;
    }

    /**
     *
     * @param file
     */
    public static void stripTagFromFile(File file)
    {
       //TODO - This!
    }
}
