package com.zaaach.toprightmenu;

/**
 * Author：Bro0cL on 2016/12/26.
 */

public class MenuItem {
    private int icon;
    private String text;
    private boolean hasPoint = false;
    private int rightIcon;

    public MenuItem() {}

    public MenuItem(String text) {
        this.text = text;
    }

    public MenuItem(int iconId, String text) {
        this.icon = iconId;
        this.text = text;
    }

    public MenuItem(int icon, String text, int rightIcon) {
        this.icon = icon;
        this.text = text;
        this.rightIcon = rightIcon;
    }

    public MenuItem(int icon, String text, boolean hasPoint) {
        this.icon = icon;
        this.text = text;
        this.hasPoint = hasPoint;
    }

    public int getIcon() {
        return icon;

}
    public void setIcon(int iconId) {
        this.icon = iconId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isHasPoint() {
        return hasPoint;
    }

    public void setHasPoint(boolean hasPoint) {
        this.hasPoint = hasPoint;
    }

    public int getRightIcon() {
        return rightIcon;
    }

    public void setRightIcon(int rightIcon) {
        this.rightIcon = rightIcon;
    }
}
