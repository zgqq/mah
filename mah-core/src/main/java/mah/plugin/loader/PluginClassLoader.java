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
package mah.plugin.loader;

import mah.plugin.PluginException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by zgq on 2017-01-08 20:18
 */
public class PluginClassLoader extends ClassLoader{

    private final Map<String,Class<?>> CLASS_CACHES = new HashMap();

    public PluginClassLoader() {
        super(PluginClassLoader.class.getClassLoader());
    }

    private static final ThreadLocal<JarFile> localJarFile = new ThreadLocal<>();

    public Class<?> loadClass(String className,JarFile jar) throws ClassNotFoundException {
        localJarFile.set(jar);
        return super.loadClass(className);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clazz = CLASS_CACHES.get(name);
        if (clazz == null) {
            JarFile jar = localJarFile.get();
            JarEntry entry = jar.getJarEntry(name + ".class");
            InputStream is = null;
            try {
                is = jar.getInputStream(entry);
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                int nextValue = is.read();
                while (-1 != nextValue) {
                    byteStream.write(nextValue);
                    nextValue = is.read();
                }
                byte[] classByte = byteStream.toByteArray();
                Class<?> result = defineClass(name, classByte, 0, classByte.length, null);
                CLASS_CACHES.put(name, result);
                return result;
            } catch (IOException e) {
                throw new PluginException(e);
            }
        }
        return null;
    }

}
