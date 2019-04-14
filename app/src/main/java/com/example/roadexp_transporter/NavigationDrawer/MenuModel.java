package com.example.roadexp_transporter.NavigationDrawer;

public class MenuModel {

    private int id;
    private String menuName;
    private boolean hasChildren, isGroup;
    private int icon_id;

    public MenuModel(int id, String menuName, boolean hasChildren, boolean isGroup, int icon_id) {
        this.id = id;
        this.menuName = menuName;
        this.hasChildren = hasChildren;
        this.isGroup = isGroup;
        this.icon_id = icon_id;
    }

    public int getId() {
        return id;
    }

    public String getMenuName() {
        return menuName;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public int getIcon_id() {
        return icon_id;
    }
}
