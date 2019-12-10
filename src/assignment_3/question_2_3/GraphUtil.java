package assignment_3.question_2_3;

import java.util.*;

public class GraphUtil<E> {
    int time;
    Map<Vertex<E>, Info<E>> infoMap;
    Boolean unconnected;

    public GraphUtil() {
        this.time = 0;
        this.infoMap = new LinkedHashMap<>();
        this.unconnected = false;
    }

    /**
     * Question.2
     * This methods completely follow the requirements of Question.2,
     * though it is quite weird to put Info into a graph while the Info itself holds a reference to the Vertex of Original Graph.
     */
    public GraphADT<Info<E>> calculateDepths(GraphADT<E> graph, Vertex<E> startVertex) throws Exception {
        time = 0;
        infoMap.clear();

        if (!graph.contains(startVertex))
            throw new NullPointerException("Vertex " + startVertex + " does not exist!");

        GraphADT<Info<E>> infoGraph = new GraphADT<>();

        /**
         * To prepare for DFS and initializing Info Graph.
         */
        for (Vertex<E> vertex : graph.getVertices().values()) {
            vertex.unvisited();
            vertex.setAscVertex(null);
            Info<E> info = new Info<>(vertex);
            infoMap.put(vertex, info);
            infoGraph.add(info);
        }

        /**
         * DFS start from the startVertex.
         */
        dfs(startVertex, infoGraph);

        /**
         * In case some vertices are not linked together.
         */
        for (Vertex<E> vertex : graph.getVertices().values()) {
            if (vertex.getVisitedState() == 0) {
                this.unconnected = true;
                dfs(vertex, infoGraph);
            }
        }

        return infoGraph;

    }

    public AnalyzedResult<E> analyzeGraph(GraphADT<E> graph) throws Exception {
        Vertex<E> startVertex = null;
        AnalyzedResult<E> analyzedResult = new AnalyzedResult<>();

        if (graph.size() == 0) throw new Exception("[analyzeGraph] Graph is empty!");

        /**
         * To get a startVertex.
         */
        for (Vertex<E> v : graph.getVertices().values()) {
            startVertex = v;
            break;
        }

        GraphADT<Info<E>> infoGraph = this.calculateDepths(graph, startVertex);
        if (this.unconnected) throw new Exception("[analyzeGraph] Graph is unconnected");
        if (!this.isUndirected(graph)) throw new Exception("[analyzeGraph] Graph is directed");


        for (Vertex<Info<E>> infoV : infoGraph.getVertices().values()) {
            /**
             * to check if a Root of the tree is a articulationPoint.
             */
            if (infoV.getAscVertex() == null && infoV.getAdjList().size() > 1) {
                /**
                 * infoVertices hold reference to Info instances, Info instances contain the ORIGINAL vertices.
                 */
                analyzedResult.addArticulationPoint(infoV.getVal().getVertex());
            } else if (infoV.getAscVertex() != null) {
                /**
                 * the ordinary articulationPoint situation.
                 */
                for (Vertex<Info<E>> infoU : infoV.getAdjList()) {
                    if (infoU.getVal().getM() >= infoV.getVal().getD()) {
                        analyzedResult.addArticulationPoint(infoV.getVal().getVertex());
                    }
                }
            }

            /**
             * TO find bridge.
             */
            for (Vertex<Info<E>> infoU : infoV.getAdjList()) {
                if (infoU.getVal().getM() > infoV.getVal().getD()) {
                    analyzedResult.addBridge(infoV.getVal().getVertex(), infoU.getVal().getVertex());
                }
            }
        }

        return analyzedResult;
    }

    /**
     * To check if the graph is undirected.
     */
    public boolean isUndirected(GraphADT<E> graph) {
        for (Vertex<E> v : graph.getVertices().values()) {
            for (Vertex<E> u : v.getAdjList()) {
                if (!u.getAdjList().contains(v))
                    return false;
            }
        }
        return true;
    }

    /**
     * This method does a Deep first search to the Original Graph and saving M and D information into the Info Graph.
     */
    public void dfs(Vertex<E> v, GraphADT<Info<E>> infoGraph) throws Exception {
        Info<E> v_info = infoGraph.getVertex(infoMap.get(v)).getVal();
        v_info.setD(time);
        v_info.setM(time++);
        v.visiting();

        for (Vertex u : v.getAdjList()) {
            Info<E> u_info = infoGraph.getVertex(infoMap.get(u)).getVal();

            if (u.getVisitedState() == 0) {
                u.setAscVertex(v);
                /**
                 * Put the link() here is to aim to really build a DFS tree,
                 * so that every vertex will only be link to vertices haven't been visited.
                 */
                infoGraph.linkDirected(v_info, u_info);
                infoGraph.getVertex(u_info).setAscVertex(infoGraph.getVertex(v_info));
                dfs(u, infoGraph);
            }

            if (u.getVisitedState() > 0 && u != v.getAscVertex()) {
                if (u_info.getM() < v_info.getM()) {
                    v_info.setM(u_info.getM());
                }
            }
        }
        v.visited();

    }

}

class Info<E> {
    Vertex<E> vertex;
    int d;
    int m;

    public Info(Vertex<E> v) {
        this.vertex = v;
    }

    public Vertex<E> getVertex() {
        return vertex;
    }

    public void setVertex(Vertex<E> vertex) {
        this.vertex = vertex;
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

    @Override
    public String toString() {
        return vertex + "";
    }
}

/**
 * This class is for analyzeGraph(),
 * who returns an AnalyzedResult that contains both articulation points and bridges of the graph.
 * @param <E>
 */
class AnalyzedResult<E> {
    Set<Vertex<E>> articulationPoints;
    Map<Vertex<E>, LinkedList<Vertex<E>>> bridges;

    public AnalyzedResult() {
        this.articulationPoints = new LinkedHashSet<>();
        this.bridges = new LinkedHashMap<>();
    }

    public Set<Vertex<E>> getArticulationPoints() {
        return articulationPoints;
    }

    public Map<Vertex<E>, LinkedList<Vertex<E>>> getBridges() {
        return bridges;
    }

    public void addArticulationPoint(Vertex<E> v) {
        this.articulationPoints.add(v);
    }

    public void addBridge(Vertex<E> side1, Vertex<E> side2) {
        if (!bridges.containsKey(side1) && !bridges.containsKey(side2)) {
            bridges.put(side1, new LinkedList<>());
        }
        if (bridges.containsKey(side1)) {
            if (bridges.get(side1).contains(side2)) {

            } else {
                bridges.get(side1).add(side2);
            }
        } else if (bridges.containsKey(side2)) {
            if (bridges.get(side2).contains(side1)) {

            } else {
                bridges.get(side2).add(side1);
            }
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Articulation Points:\n");
        for (Vertex<E> v : articulationPoints) {
            sb.append(v.getVal() + "\t");
        }
        sb.append("\nBridges:\n");
        for (Vertex<E> key : bridges.keySet()) {
            for (Vertex<E> value : bridges.get(key)) {
                sb.append(key.getVal() + "  ---  " + value.getVal() + "\n");
            }
        }
        return sb.toString();

    }


}