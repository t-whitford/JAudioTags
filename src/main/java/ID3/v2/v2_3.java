package ID3.v2;

import java.io.*;
import java.util.HashMap;


/**
 * Created by Tom Whitford on 17/12/2015.
 */
public class v2_3 {

    boolean hasExtendedHeader;

    byte[] header;
    int framesSize;

    byte[] extendedHeader;
    int extendedHeaderSize;

    HashMap<String, byte[]> tagMap;

    public v2_3(File file) throws IOException {

        int id3Len;

        header = new byte[10];
        byte[] exHeader;

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        in.read(header);

        framesSize = getFramesSize();

        hasExtendedHeader(header);
        extractExtendedHeader();


        byte[] frames = new byte[framesSize];
        buildMap(frames);

    }

    private int getFramesSize() {
        int size = 0;
        boolean[] bits = new boolean[];

        //TODO check that 0%7 still works

        boolean[] bits = new boolean[28];
        for(int i  = 0, pos = 0; i < 28; i++, pos++)
        {
            if(pos != 0 && pos % 7 ==0)
                pos++;

            byte b = header[6 + pos / 8];

            boolean bit = (b & (int)(Math.pow(2, (pos % 8)))) == 0;

            if(bit)
                size = size + (int)(Math.pow(2, pos % 8));
        }

        return size;
    }

    private void extractExtendedHeader() {
        if(hasExtendedHeader){
            byte[] exSizeBytes = new byte[4];
            in.read(exSizeBytes);
            int size = 0;

            for(int i =0; i < 4; i++)
            {
                size = size + (exSizeBytes[3-i] + (256 * i));
            }

            byte[] temp = new byte[size];
            in.read(temp);

            exHeader = new byte[4 + size];
            int pos = 0;
            for(byte b: exSizeBytes){
                exHeader[pos] = b;
                pos++;
            }
            for(byte b: temp)
            {
                exHeader[pos] = b;
                pos++;
            }
        }
    }

    private void buildMap(byte[] frames)
    {
        int pos = 0;
        while(pos < frames.length - 1)
        {
            String frameID = new String(frames, pos, 4);
            int frameSize = getSize(frames, pos + 4, 4);
            byte[] content = new byte[frameSize];
            System.arraycopy(frames, pos+8, content, 0, content.length);

            tagMap.put(frameID, content);


            pos += 8 + frameSize;
        }
    }

    private int getSize(byte[] frames, int offset, int length) {
        int size = 0;
        for (int i = 0; i < length; i++)
        {
            size +=
        }
        throw new RuntimeException("Method not written");
    }

    private void hasExtendedHeader(byte[] header) {
        if((header[6] & 32 )== 32)
        {
            hasExtendedHeader = true;
        }
    }


    public static boolean hasTag(File file) throws IOException {

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

        byte[] bytesIn = new byte[5];
        in.read(bytesIn);

        in.close();

        if( (new String(bytesIn, 0, 3)).compareTo("ID3") == 0 && bytesIn[3] == 3)
            return true;
        else
            return false;
    }

    public boolean hasExtendedHeader() {
        return hasExtendedHeader;
    }
}
