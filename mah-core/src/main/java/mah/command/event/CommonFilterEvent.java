package mah.command.event;

/**
 * Created by zgq on 2017-01-10 10:48
 */
public class CommonFilterEvent extends FilterEvent {

    private final String content;

    public CommonFilterEvent(String triggerKey, String content) {
        super(triggerKey);
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}

