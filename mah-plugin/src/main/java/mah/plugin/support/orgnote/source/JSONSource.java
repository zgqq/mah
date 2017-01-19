package mah.plugin.support.orgnote.source;

import com.alibaba.fastjson.JSON;
import mah.common.util.IOUtils;
import mah.plugin.support.orgnote.entity.NodeEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Created by zgq on 10/1/16.
 */
public class JSONSource implements Source{
    @Override
    public List<NodeEntity> getAllNotes() {
        try {
            String home = System.getProperty("user.home");
            File file = new File(home+"/app/java-note/reviews.json");
            if(!file.exists()){
                return null;
            }
            FileInputStream jsonStream = new FileInputStream(file);
            List<NodeEntity> noteEntities = JSON.parseArray(IOUtils.toString(jsonStream), NodeEntity.class);
            return noteEntities;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
