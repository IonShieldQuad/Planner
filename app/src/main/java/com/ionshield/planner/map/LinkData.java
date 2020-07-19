package com.ionshield.planner.map;

public class LinkData {
    public int id;
    public int fromId;
    public int toId;
    public int typeId;
    public double modifier;
    public double fromX;
    public double fromY;
    public double toX;
    public double toY;
    public int color;
    public int width;
    public double speed;

    public LinkData(int id, int fromId, int toId, int typeId, double modifier, double fromX, double fromY, double toX, double toY, int color, int width, double speed) {
        this.id = id;
        this.fromId = fromId;
        this.toId = toId;
        this.typeId = typeId;
        this.modifier = modifier;
        this.fromX = fromX;
        this.fromY = fromY;
        this.toX = toX;
        this.toY = toY;
        this.color = color;
        this.width = width;
        this.speed = speed;
    }
}
