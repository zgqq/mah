package mah.common.util;

import java.io.*;

/**
 * Created by zgq on 2017-01-08 19:56
 */
public final class IOUtils {

    private IOUtils() {

    }
    public static String getExtensive(String name) {
        int fisrtInd = name.indexOf('.');
        if ((fisrtInd==-1)) {
            fisrtInd = name.length()-1;
        }
        return name.substring(fisrtInd, name.length());
    }

    public static String getFilename(String name){
        int lastInd = name.lastIndexOf('.');
        if (lastInd==-1){
            lastInd = name.length();
        }
        return name.substring(0,lastInd);
    }

    public static final String toString(String filename) throws IOException {
        String string = new String(toBytes(filename),"utf-8");
        return string;
    }

    public static final String toString(InputStream inputStream) throws IOException {
        String string = new String(toBytes(inputStream),"UTF-8");
        return string;
    }
    public static final byte[] toBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        int len;
        while ((len = inputStream.read(bytes,0,4096))!=-1){
            baos.write(bytes,0,len);
        }

        byte[] bytes1 = baos.toByteArray();
        baos.close();
        return bytes1;
    }

    public static String getStringByFilename(String filename) throws IOException {
        File file = new File(filename);
        FileInputStream fis;
        fis = new FileInputStream(file);
        return toString(fis);
    }


    public static void writeToFile(String filename,InputStream inputStream) throws IOException {
        File file = new File(filename);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] bytes = toBytes(inputStream);
        fos.write(bytes,0,bytes.length);
        fos.close();
    }

    public static void writeToFile(String filename,String content) throws IOException {
        File file = new File(filename);
        FileOutputStream fos = new FileOutputStream(file);
        byte[] bytes = content.getBytes();
        fos.write(bytes,0,bytes.length);
        fos.close();
    }

    public static FileInputStream getInputStream(String filename) throws FileNotFoundException {
        File file = new File(filename);
        FileInputStream fis;
        fis = new FileInputStream(file);
        return fis;
    }

    public static byte[] toBytes(String filename) throws IOException {
        FileInputStream inputStream = getInputStream(filename);
        return toBytes(inputStream);
    }

    public static void createFileIfNotExists(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            file.createNewFile();
        }
    }
}

