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
package mah.plugin.support.keyrecorder;

import com.alibaba.fastjson.JSON;
import mah.command.CommandException;
import mah.command.event.EventHandler;
import mah.command.event.InitializeEvent;
import mah.command.event.TriggerEvent;
import mah.common.json.JsonArr;
import mah.common.json.JsonException;
import mah.common.json.JsonObj;
import mah.common.json.JsonUtils;
import mah.common.util.CollectionUtils;
import mah.common.util.IoUtils;
import mah.keybind.KeybindManager;
import mah.keybind.listener.GlobalKeyEvent;
import mah.keybind.listener.GlobalKeyListener;
import mah.plugin.command.PluginCommandSupport;
import mah.ui.icon.Icon;
import mah.ui.layout.ClassicItemListLayout;
import mah.ui.pane.item.FullItem;
import mah.ui.pane.item.FullItemImpl;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by zgq on 2/6/17.
 */
class TopKeystrokeCommand extends PluginCommandSupport implements GlobalKeyListener {
    private final Map<String, KeyRecorder> keyRecorderMap = new HashMap<>();
    private final List<KeyRecorder> keyRecorders = new ArrayList();
    private ClassicItemListLayout layout;
    private final String pluginFile;
    private final String name;
    private final RecorderCondition condition;
    private String localFile;

    TopKeystrokeCommand(String pluginFile, String name, RecorderCondition condition) {
        this.pluginFile = pluginFile;
        this.name = name;
        this.condition = condition;
        init();
    }

    private void init() {
        addInitializeEventHandler(new EventHandler<InitializeEvent>() {
            @Override
            public void handle(InitializeEvent event) throws Exception {
                layout = getLayoutFactory().createClassicItemListLayout();
                localFile = getFileStoredInPluginDataDir(pluginFile);
                readKeystrokes();
                KeybindManager.getInstance().addGlobalKeyListener(TopKeystrokeCommand.this);
            }
        });
        final Comparator<KeyRecorder> comparator = new Comparator<KeyRecorder>() {
            @Override
            public int compare(KeyRecorder o1, KeyRecorder o2) {
                return (o1.getPressedCount() < o2.getPressedCount()) ? 1 : -1;
            }
        };
        addTriggerEventHandler(new EventHandler<TriggerEvent>() {
            @Override
            public void handle(TriggerEvent event) throws Exception {
                ArrayList<KeyRecorder> keyRecorders = new ArrayList<>(TopKeystrokeCommand.this.keyRecorders);
                keyRecorders.sort(comparator);
                int size = CollectionUtils.getSize(keyRecorders, 9);
                List<FullItem> items = new ArrayList<>();
                for (int i = 0; i < size; i++) {
                    KeyRecorder keyRecorder = keyRecorders.get(i);
                    FullItemImpl item = FullItemImpl.builder(keyRecorder.getKey()).description("已经被击中"
                            + keyRecorder.getPressedCount() + "次")
                            .icon(Icon.valueOf("icon.png")).build();
                    items.add(item);
                }
                layout.updateItems(items);
            }
        });
    }

    public void readKeystrokes() {
        JsonArr map;
        try {
            map = JsonUtils.parseArrFromLocalFile(localFile);
        } catch (JsonException e) {
            if (e.getCause() instanceof FileNotFoundException) {
                try {
                    IoUtils.createFileIfNotExists(localFile);
                } catch (IOException ioe) {
                    throw new CommandException(ioe);
                }
                return;
            }
            throw new CommandException(e);
        }
        if (map == null) {
            return;
        }
        map.forEach(entry -> {
            JsonObj jsonObj = (JsonObj) entry;
            KeyRecorder keyRecorder = new KeyRecorder();
            keyRecorder.setKey(jsonObj.getString("key"));
            keyRecorder.setPressedCount(jsonObj.getInt("pressedCount"));
            keyRecorderMap.put(jsonObj.getString("key"), keyRecorder);
            keyRecorders.add(keyRecorder);
        });
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void keyPressed(GlobalKeyEvent keyEvent) {
        if (!condition.shouldRecord(keyEvent)) {
           return;
        }
        final KeyStroke keyStroke = keyEvent.getKeyStroke();
        KeyRecorder keyRecorder = keyRecorderMap.get(keyStroke.toString());
        if (keyRecorder == null) {
            keyRecorder = new KeyRecorder(keyStroke.toString(), 1);
            keyRecorderMap.put(keyStroke.toString(), keyRecorder);
            keyRecorders.add(keyRecorder);
        } else {
            keyRecorder.setPressedCount(keyRecorder.getPressedCount() + 1);
        }
        try {
            IoUtils.writeToFile(localFile, JSON.toJSONString(keyRecorders));
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }
}
