package org.ss.json.util;

import java.io.*;
import java.nio.charset.Charset;

public class IOs {

    public static String toString(InputStream inputStream, Charset charset) {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(8192);
        byte[] bytes = new byte[8192];
        int length;
        while (true) {
            try {
                if ((length = bufferedInputStream.read(bytes)) < 1) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byteArrayOutputStream.write(bytes, 0, length);
        }
        try {
            return byteArrayOutputStream.toString(charset.name());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
