package mah.command.event;

/**
 * Created by zgq on 2017-01-10 10:47
 */
public class FilterEvent {

    private final String triggerKey;

    public FilterEvent(String triggerKey) {
        this.triggerKey = triggerKey;
    }

    public String getTriggerKey() {
        return triggerKey;
    }
}
