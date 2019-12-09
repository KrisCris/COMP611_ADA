package assignment_3.question_2_3;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class GraphADT<E> {

    private int time;
    private Map<E, Vertex<E>> vertices;

    public GraphADT() {
        this.time = 0;
        this.vertices = new LinkedHashMap<>();
    }

    public boolean add(E val) {
        if (!contains(val)) {
            this.vertices.put(val, new Vertex<>(val));
            return true;
        }
        return false;
    }

    public boolean add(Vertex<E> v) {
        if (!contains(v)) {
            this.vertices.put(v.getVal(), v);
            return true;
        }
        return false;
    }

    public boolean contains(E val) {
        if (vertices.containsKey(val))
            return true;
        return false;
    }

    public boolean contains(Vertex<E> v) {
        if (vertices.containsValue(v))
            return true;
        return false;
    }

    public boolean linkUndirected(E val_1, E val_2) throws Exception {
        if (contains(val_1) && contains(val_2)) {
            Vertex<E> v1 = getVertex(val_1);
            Vertex<E> v2 = getVertex(val_2);
            v1.link(v2);
            v2.link(v1);
            return true;
        }
        return false;
    }

    public boolean linkDirected(E val_1, E val_2) throws Exception {
        if (contains(val_1) && contains(val_2)) {
            Vertex<E> v1 = getVertex(val_1);
            Vertex<E> v2 = getVertex(val_2);
            v1.link(v2);
            return true;
        }
        return false;
    }

    public int size(){
        return this.vertices.size();
    }

    public Vertex<E> getVertex(E val) throws Exception {
        if (!contains(val))
            throw new Exception("[DFS] Vertex " + val + " do not exist!");
        return vertices.get(val);
    }

    public Map<E, Vertex<E>> getVertices() {
        return vertices;
    }

    public void showLinkage(){
        for (Vertex<E> v : vertices.values()){
            for(Vertex<E> u : v.getAdjList()){
                System.out.println(v.getVal()+"---"+u.getVal());
            }
        }
    }
    /**
     * 0 unvisited
     * 1 visiting
     * 2 visited
     *
     * @param val
     */
    public void dfs(E val) throws Exception {
        this.time = 0;

        for (Vertex vertex : vertices.values()) {
            vertex.unvisited();
            vertex.setAscVertex(null);
        }

        dfsVisit(getVertex(val));

        /**
         * In case some vertices are not linked together.
         */
        for (Vertex vertex : vertices.values()) {
            if (vertex.getVisitedState() == 0) {
                dfsVisit(vertex);
            }
        }
    }

    private void dfsVisit(Vertex<E> v) {
        v.setD(time);
        v.setM(time++);
        v.visiting();
        for (Vertex u : v.getAdjList()) {

            if (u.getVisitedState() == 0) {
                u.setAscVertex(v);
                dfsVisit(u);
            }

            if (u.getVisitedState() > 0 && u != v.getAscVertex()) {
                if (u.getM() < v.getM()) {
                    v.setM(u.getM());
                }
            }
        }
        v.visited();

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Vertex vertex : vertices.values()) {
            sb.append(vertex.toString() + "\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        GraphADT<String> testGraph = new GraphADT<>();
        testGraph.add("a");
        testGraph.add("b");
        testGraph.add("c");
        testGraph.add("d");
        testGraph.add("e");
        testGraph.add("f");
        testGraph.add("g");
        testGraph.add("h");
        testGraph.add("i");
        testGraph.add("j");
        testGraph.add("k");
        testGraph.add("l");
        testGraph.add("m");

        testGraph.linkUndirected("a", "b");
        testGraph.linkUndirected("b", "c");
        testGraph.linkUndirected("c", "d");
        testGraph.linkUndirected("b", "d");
        testGraph.linkUndirected("c", "e");
        testGraph.linkUndirected("e", "f");
        testGraph.linkUndirected("f", "g");
        testGraph.linkUndirected("g", "e");
        testGraph.linkUndirected("e", "h");
        testGraph.linkUndirected("h", "i");
        testGraph.linkUndirected("e", "j");
        testGraph.linkUndirected("j", "k");
        testGraph.linkUndirected("k", "l");
        testGraph.linkUndirected("l", "m");
        testGraph.linkUndirected("j", "l");
        testGraph.linkUndirected("j", "m");

        System.out.println("[GraphADT.java] For test: ");

        testGraph.dfs("a");

        System.out.println(testGraph);

        testGraph.showLinkage();

    }
}


class Vertex<E> {
    private E val;
    private int visitedState;
    private LinkedList<Vertex<E>> adjList;
    private Vertex<E> ascVertex;

    /**
     * These two are the same usage as in Class Info, which resolved the question2.
     * And these two attributes will be used if you call the dfs() in GraphADT.
     * However these attributes are just for test, not for solving question2.
     */
    private int d;
    private int m;

    public Vertex(E val) {
        this.val = val;
        this.adjList = new LinkedList<>();
    }

    public boolean link(Vertex<E> v) {
        if (adjList.contains(v)) {
            return false;
        }
        adjList.add(v);
        return true;
    }

    public int getD() {
        return d;
    }

    public void setD(int d) {
        this.d = d;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public LinkedList<Vertex<E>> getAdjList() {
        return this.adjList;
    }

    public E getVal() {
        return val;
    }

    public int getVisitedState() {
        return visitedState;
    }

    public void unvisited() {
        this.visitedState = 0;
    }

    public void visiting() {
        this.visitedState = 1;
    }

    public void visited() {
        this.visitedState = 2;
    }

    public Vertex<E> getAscVertex() {
        return ascVertex;
    }

    public void setAscVertex(Vertex<E> ascVertex) {
        this.ascVertex = ascVertex;
    }

    @Override
    public String toString() {
        return "val = "+val+
                ", d = " + d +
                ", m = " + m;
    }
}