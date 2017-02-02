/**
 * MIT License
 *
 * Copyright (c) 2017 zgqq
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package mah.plugin.support.orgnote.util;

import java.io.*;

/**
 * Created by zgq on 10/1/16.
 */
public class IOUtil {

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
        String string = new String(toBytes(inputStream),"utf-8");
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
