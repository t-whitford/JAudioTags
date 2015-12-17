package ID3.v1;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;


/**
 * Created by Tom Whitford on 16/12/2015.
 */
public class v1_1 extends ID3v1_0 {

    File file;

    public v1_1(File file) throws IOException {
        super(file);


        // LinkedHashMap<String, byte[]>
        LinkedHashMap<String, byte[]> m = super.map;

        byte[] newComment = new byte[29];

        for (int i = 0; i < 28; i++) {
            newComment[i] = m.get("Comment")[i];
        }

        newComment[28] = 0;

        m.replace("Comment", newComment);


        byte[] genre = m.remove("Genre");
        byte[] track = {getTrackFromFile(file)};
        m.put("Track", track);
        m.put("Genre", genre);

        super.map = m;
    }

    private static byte getTrackFromFile(File file) throws IOException {

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        in.skip(file.length() - 2);
        byte result = (byte) in.read();
        in.close();
        return result;
    }

    public static boolean hasTag(File file) {
        return false;
    }

    public void setTrack(byte track) throws IOException {
        byte[] arr = {track};
        super.setTag("Track", arr);
    }

    public byte getTrack() {
        return super.getMap().get("Track")[0];
    }

    @Override
    public void setComment(String newComment) throws IOException {
        if(newComment.length() > 28)
            throw new IllegalArgumentException("Comment is too long (greater than 28");

        super.setComment(newComment);
    }

    
}
