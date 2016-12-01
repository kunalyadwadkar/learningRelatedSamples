package module5;

import processing.core.PApplet;

import java.util.function.Function;

public class ToggleButton {

    public static final int BUTTON_WIDTH_DEFAULT = 20;

    private int x;
    private int y;
    private PApplet pg;
    private boolean selected = false;
    private int buttonWidth = BUTTON_WIDTH_DEFAULT;
    private StyleSetter styleSetter;
    private Function<Boolean, String> titleFetcher;
    private boolean setToHide = false;

    public void setButtonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
    }

    public int getButtonWidth() {
        return buttonWidth;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSetToHide() {
        return setToHide;
    }

    public void setIsSetToHide(boolean setToHide) {
        this.setToHide = setToHide;
    }

    public ToggleButton(int locX, int locY, PApplet pg) {
        this.x = locX;
        this.y = locY;
        this.pg = pg;
    }

    public static ToggleButton withLocation(PApplet pg, int x, int y) {
        return new ToggleButton(x, y, pg);
    }

    public ToggleButton addStyleSetter(StyleSetter styleSetter) {
        this.styleSetter = styleSetter;
        return this;
    }

    public ToggleButton addTitleFetcher(Function<Boolean, String> titleFetcher) {
        this.titleFetcher = titleFetcher;
        return this;
    }

    public void draw() {
        if (styleSetter != null) {
            styleSetter.before(pg);
        }

        pg.rect(x, y, buttonWidth, buttonWidth);

        if(titleFetcher != null) {
            String title = titleFetcher.apply(setToHide);
            pg.fill(0, 0, 0);
            pg.text(title, x + buttonWidth + 10, y);
        }

        if (styleSetter != null) {
            styleSetter.after(pg);
        }
    }

    public boolean isInside(int x, int y) {
        return x >= this.x && x <= this.x + buttonWidth && y >= this.y && y <= this.y + buttonWidth;

    }

    public interface StyleSetter {

        void before(PApplet p);

        void after(PApplet p);
    }


}
