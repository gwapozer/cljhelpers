package javas;

import java.io.*;

public class fileUtil {

    public static byte[] readFileToByte(File file) throws IOException
    {
        int size = (int)file.length();

        byte[] data = new byte[size];

        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        dis.readFully(data);

        dis.close();

        return data;
    }

    public static byte[] InputStreamToByte(InputStream is) throws IOException
    {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        int size = is.available();

        byte[] data = new byte[size];

        while ((nRead = is.read(data, 0, data.length)) != -1)
        {
            buffer.write(data, 0, nRead);
        }

        buffer.close();

        return data;
    }
}
