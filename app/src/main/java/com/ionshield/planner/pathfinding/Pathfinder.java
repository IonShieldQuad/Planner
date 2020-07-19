package com.ionshield.planner.pathfinding;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.ionshield.planner.database.DBC;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Pathfinder {
    private SQLiteDatabase mDatabase;

    public Pathfinder(SQLiteDatabase database) {
        this.mDatabase = database;
    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.mDatabase = database;
    }

    public PathResult findPath(int startNode, Map<Integer, Double> targetNodes) {
        PathResult pathResult = new PathResult();

        Map<Integer, NodeData> targetsData = queryNodesCoordinates(targetNodes.keySet());
        for (Map.Entry<Integer, NodeData> entry : targetsData.entrySet()) {
            entry.getValue().heuristicWeight = targetNodes.get(entry.getKey());
        }
        Collection<NodeData> targets = targetsData.values();

        Map<Integer, NodeData> open = new HashMap<>();
        Map<Integer, NodeData> all = new HashMap<>();

        open.put(startNode, queryNode(startNode));
        all.put(startNode, open.get(startNode));
        all.get(startNode).pathCost = 0;
        all.get(startNode).totalCost = heuristic(all.get(startNode), targetsData.values());
        while (!open.isEmpty()) {
            NodeData curr = Collections.min(open.values(), new Comparator<NodeData>() {
                @Override
                public int compare(NodeData a, NodeData b) {
                    return Double.compare(a.totalCost, b.totalCost);
                }
            });
            if (targetNodes.containsKey(curr.id)) {
                pathResult.success = true;

                while (curr != null && curr.from != null) {
                    pathResult.pathNodes.add(curr.id);
                    pathResult.pathLinks.add(curr.from.id);

                    curr = all.get(curr.from.nodeFrom);
                }
                if (curr != null) {
                    pathResult.pathNodes.add(curr.id);
                }

                Collections.reverse(pathResult.pathNodes);
                Collections.reverse(pathResult.pathLinks);

                pathResult.searchNodes = new ArrayList<>(all.keySet());
                return pathResult;
            }
            open.remove(curr.id);

            for (LinkData link : curr.links.values()) {
                NodeData other;
                pathResult.searchLinks.add(link.id);
                if (all.containsKey(link.nodeTo) && all.get(link.nodeTo) != null) {
                    other = all.get(link.nodeTo);
                }
                else {
                    other = queryNode(link.nodeTo);
                    all.put(other.id, other);
                }
                if (other == null) continue;

                double score = curr.pathCost + link.cost;
                if (score < other.pathCost) {
                    other.from = link;
                    other.pathCost = score;
                    other.totalCost = score + heuristic(other, targets);
                    if (!open.containsKey(other.id)) {
                        open.put(other.id, other);
                    }
                }
            }

        }

        pathResult.success = false;
        pathResult.searchNodes = new ArrayList<>(all.keySet());

        return pathResult;
    }

    private void calcTotalCost(Map<Integer, NodeData> nodes, Collection<NodeData> targets) {
        for (Map.Entry<Integer, NodeData> entry : nodes.entrySet()) {
            entry.getValue().totalCost = entry.getValue().pathCost + heuristic(entry.getValue(), targets);
        }
    }

    private void calcTotalCost(Collection<NodeData> nodes, Collection<NodeData> targets) {
        for (NodeData node : nodes) {
            node.totalCost = node.pathCost + heuristic(node, targets);
        }
    }

    private Map<Integer, NodeData> queryNodes(List<Integer> ids) {
        //Make list of ids
        Map<Integer, NodeData> data = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            int id = ids.get(i);
            sb.append(id);
            if (i < ids.size() - 1) {
                sb.append(", ");
            }
        }

        //Query nodes
        Cursor nodes = mDatabase.rawQuery("SELECT * FROM " + DBC.Nodes.TABLE_NAME
                        + " WHERE " + BaseColumns._ID + " IN (" + ids + ");",
                new String[]{});

        for (nodes.moveToFirst(); !nodes.isAfterLast(); nodes.moveToNext()) {
            int id = nodes.getInt(nodes.getColumnIndexOrThrow(BaseColumns._ID));
            double x = nodes.getDouble(nodes.getColumnIndexOrThrow(DBC.Nodes.COORDINATE_X));
            double y = nodes.getDouble(nodes.getColumnIndexOrThrow(DBC.Nodes.COORDINATE_Y));
            data.put(id, new NodeData(id, x, y));
        }

        nodes.close();

        //Query links
        Cursor links = mDatabase.rawQuery("SELECT " + DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes.SPEED + ", " + DBC.Links.TABLE_NAME + ".* FROM "
                        + DBC.Links.TABLE_NAME + " JOIN " + DBC.LinkTypes.TABLE_NAME + " ON "
                        + DBC.Links.TABLE_NAME + "." + DBC.Links.LINK_TYPE_ID + "="
                        + DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes._ID
                        + " WHERE " + DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes.ENABLED + "<>0"
                        + " AND " + DBC.Links.TABLE_NAME + "." + DBC.Links.FROM_NODE_ID + " IN (" + ids + ");",
                new String[]{});

        List<Integer> toNodes = new ArrayList<>();
        List<LinkData> linkList = new ArrayList<>();
        for (links.moveToFirst(); !links.isAfterLast(); links.moveToNext()) {
            int linkId = links.getInt(links.getColumnIndexOrThrow(BaseColumns._ID));
            int nodeFrom = links.getInt(links.getColumnIndexOrThrow(DBC.Links.FROM_NODE_ID));
            int nodeTo = links.getInt(links.getColumnIndexOrThrow(DBC.Links.TO_NODE_ID));
            double speed = links.getDouble(links.getColumnIndexOrThrow(DBC.LinkTypes.SPEED));
            double modifier = links.getDouble(links.getColumnIndexOrThrow(DBC.Links.MODIFIER));

            toNodes.add(nodeTo);

            LinkData ld = new LinkData(linkId, nodeFrom, nodeTo, speed, modifier, 0);
            linkList.add(ld);
            Objects.requireNonNull(data.get(nodeFrom)).links.put(linkId, ld);
        }
        Map<Integer, NodeData> toNodesData = queryNodesCoordinates(toNodes);

        //Calculate cost for each link
        for (LinkData link : linkList) {
            NodeData fromNode = data.get(link.nodeFrom);
            NodeData toNode = toNodesData.get(link.nodeTo);
            if (fromNode == null || toNode == null) {
                link.cost = Double.MAX_VALUE;
            } else {
                link.cost = Math.min(Double.MAX_VALUE, Math.max(0, link.modifier * distance(fromNode, toNode) / link.speed));
            }
        }

        links.close();
        return data;
    }

    private NodeData queryNode(int id) {
        Cursor node = mDatabase.rawQuery("SELECT * FROM " + DBC.Nodes.TABLE_NAME
                + " WHERE " + BaseColumns._ID + "=?;",
                new String[]{String.valueOf(id)});
        node.moveToFirst();
        if (node.isAfterLast()) return null;

        double x = node.getDouble(node.getColumnIndexOrThrow(DBC.Nodes.COORDINATE_X));
        double y = node.getDouble(node.getColumnIndexOrThrow(DBC.Nodes.COORDINATE_Y));
        node.close();

        NodeData nodeData = new NodeData(id, x, y);

        Cursor links = mDatabase.rawQuery("SELECT " + DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes.SPEED + ", " + DBC.Links.TABLE_NAME + ".* FROM "
                + DBC.Links.TABLE_NAME + " JOIN " + DBC.LinkTypes.TABLE_NAME + " ON "
                + DBC.Links.TABLE_NAME + "." + DBC.Links.LINK_TYPE_ID + "="
                + DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes._ID
                + " WHERE " + DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes.ENABLED + "<>0"
                + " AND " + DBC.Links.TABLE_NAME + "." + DBC.Links.FROM_NODE_ID + "=?;",
                new String[]{String.valueOf(id)});

        List<Integer> toNodes = new ArrayList<>();
        for (links.moveToFirst(); !links.isAfterLast(); links.moveToNext()) {
            int linkId = links.getInt(links.getColumnIndexOrThrow(BaseColumns._ID));
            int nodeFrom = links.getInt(links.getColumnIndexOrThrow(DBC.Links.FROM_NODE_ID));
            int nodeTo = links.getInt(links.getColumnIndexOrThrow(DBC.Links.TO_NODE_ID));
            double speed = links.getDouble(links.getColumnIndexOrThrow(DBC.LinkTypes.SPEED));
            double modifier = links.getDouble(links.getColumnIndexOrThrow(DBC.Links.MODIFIER));

            toNodes.add(nodeTo);

            nodeData.links.put(linkId, new LinkData(linkId, nodeFrom, nodeTo, speed, modifier, 0));
        }
        Map<Integer, NodeData> toNodesData = queryNodesCoordinates(toNodes);
        for (Map.Entry<Integer, LinkData> entry : nodeData.links.entrySet()) {
            LinkData link = entry.getValue();
            if (toNodesData.get(link.nodeTo) == null) {
                link.cost = Double.MAX_VALUE;
            }
            else {
                link.cost = Math.min(Double.MAX_VALUE, Math.max(0, link.modifier * distance(nodeData, Objects.requireNonNull(toNodesData.get(link.nodeTo))) / link.speed));
            }
        }
        links.close();
        return nodeData;
    }

    private Map<Integer, NodeData> queryNodesCoordinates(Collection<Integer> ids) {
        Map<Integer, NodeData> data = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        for (int id : ids) {
            if (first) {
                first = false;
            }
            else {
                sb.append(", ");
            }
            sb.append(id);
        }
        Cursor nodes = mDatabase.rawQuery("SELECT * FROM " + DBC.Nodes.TABLE_NAME
                + " WHERE " + BaseColumns._ID + " IN (" + sb.toString() + ");"
                , new String[]{});

        for (nodes.moveToFirst(); !nodes.isAfterLast(); nodes.moveToNext()) {
            int id = nodes.getInt(nodes.getColumnIndexOrThrow(BaseColumns._ID));
            double x = nodes.getDouble(nodes.getColumnIndexOrThrow(DBC.Nodes.COORDINATE_X));
            double y = nodes.getDouble(nodes.getColumnIndexOrThrow(DBC.Nodes.COORDINATE_Y));
            data.put(id, new NodeData(id, x, y));
        }
        nodes.close();

        return data;
    }

    private double heuristic(NodeData startNode, Collection<NodeData> targetNodes) {
        double result = 0;

        for (NodeData node : targetNodes) {
            result += distance(startNode, node) * node.heuristicWeight;
        }

        return result;
    }

    private double distance(NodeData a, NodeData b) {
        return Math.sqrt((a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y));
    }

    private static class LinkData {
        int id;
        int nodeFrom;
        int nodeTo;
        double speed;
        double modifier;
        double cost;

        public LinkData() {
        }

        public LinkData(int id, int nodeFrom, int nodeTo, double speed, double modifier, double cost) {
            this.id = id;
            this.nodeFrom = nodeFrom;
            this.nodeTo = nodeTo;
            this.speed = speed;
            this.modifier = modifier;
            this.cost = cost;
        }
    }

    private static class NodeData {
        int id;
        double x;
        double y;
        double pathCost = Double.MAX_VALUE;
        double totalCost = Double.MAX_VALUE;
        double heuristicWeight = 0;
        LinkData from = null;
        Map<Integer, LinkData> links = new HashMap<>();

        public NodeData() {
        }

        public NodeData(int id, double x, double y) {
            this.id = id;
            this.x = x;
            this.y = y;
        }

        public NodeData(int id, double x, double y, double heuristicWeight) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.heuristicWeight = heuristicWeight;
        }

        public NodeData(int id, double x, double y, double heuristicWeight, Map<Integer, LinkData> links) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.heuristicWeight = heuristicWeight;
            this.links = links;
        }
    }
}
