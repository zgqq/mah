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
