package com.ionshield.planner.pathfinding;

import java.util.ArrayList;
import java.util.List;

public class PathResult {
    public List<Integer> pathNodes = new ArrayList<>();
    public List<Integer> pathLinks = new ArrayList<>();
    public List<Integer> searchNodes = new ArrayList<>();
    public List<Integer> searchLinks = new ArrayList<>();
    public boolean success = false;

    public PathResult() {
    }

    public PathResult(List<Integer> pathNodes, List<Integer> pathLinks, List<Integer> searchNodes, List<Integer> searchLinks, boolean success) {
        this.pathNodes = pathNodes;
        this.pathLinks = pathLinks;
        this.searchNodes = searchNodes;
        this.searchLinks = searchLinks;
        this.success = success;
    }
}
