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
package mah.plugin.support.orgnote.source;


import mah.plugin.support.orgnote.entity.NodeEntity;
import mah.plugin.support.orgnote.util.IOUtil;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zgq on 10/1/16.
 */
public class OrgMode {

    public static List<NodeEntity> getNoteList(String filename, String title) {
        try {
            ArrayList<NodeEntity> noteEntities = new ArrayList<>();
            String content = IOUtil.getStringByFilename(filename);
            StringBuilder stars = getPrefixStars(title);
            String group = getItemsContent(content, stars);
            Pattern itemPatt = Pattern.compile(stars + "*(.*?)(" + stars + "*)?");
            Matcher items = itemPatt.matcher(group);
            while (items.find()) {
                NodeEntity entity = parseNoteEntiy(items.group(1));
                noteEntities.add(entity);
            }
            return noteEntities;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static StringBuilder getPrefixStars(String title) {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < title.length(); i++) {
            char c = title.charAt(i);
            if (c != '*' || c !=' ') {
                break;
            }
            stars.append('*');
        }
        return stars;
    }

    private static String getItemsContent(String content, StringBuilder stars) {
        Pattern subitemsPatt = Pattern.compile(stars + ".*?\n(.+?)(" + stars + ")?");
        Matcher matcher = subitemsPatt.matcher(content);
        matcher.find();
        return matcher.group(1);
    }

    private static NodeEntity parseNoteEntiy(String content) {
        Scanner scanner = new Scanner(content);
        String titleLine = scanner.nextLine();
        Pattern titlePatt = Pattern.compile("(:?TODO)?(?:\\[.*?\\])?(.*?)(:(.*):):");
        NodeEntity nodeEntity = new NodeEntity();
        Matcher titleMat = titlePatt.matcher(titleLine);
        if (titleMat.find()) {
            String title = titleMat.group(1);
            List<String> tags = parseNoteTags(titleMat.group(2));
            nodeEntity.setTitle(title);
            nodeEntity.setTags(tags);
        }
        for (; scanner.nextLine().contains("CLOCK"); ) {
        }
        String nextLine = null;
        StringBuilder noteContent = new StringBuilder();
        try {
            while (true) {
                nextLine = scanner.nextLine();
                noteContent.append(nextLine);
            }
        }catch (NoSuchElementException e){
            nodeEntity.setContent(noteContent.toString());
        }
        return nodeEntity;
    }

    private static List<String> parseNoteTags(String group) {
        return Arrays.asList(group.split(":"));
    }

}
