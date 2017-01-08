package mah.ui.window;

import mah.ui.layout.LayoutType;

/**
 * Created by zgq on 2017-01-08 11:43
 */
public final class WindowProperties {

    private final LayoutType layoutType;

    public WindowProperties(Builder builder) {
        this.layoutType = builder.type;
    }

    public static class Builder {

        private LayoutType type;

        public Builder(LayoutType type) {
            this.type = type;
        }

        public WindowProperties build() {
            return new WindowProperties(this);
        }
    }

    public LayoutType getLayoutType() {
        return layoutType;
    }
}
