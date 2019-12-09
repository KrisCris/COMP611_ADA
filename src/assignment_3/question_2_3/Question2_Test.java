package assignment_3.question_2_3;

public class Question2_Test {

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

        GraphUtil<String> graphUtil = new GraphUtil<>();
        GraphADT<Info<String>> infoGraph = graphUtil.calculateDepths(testGraph, testGraph.getVertex("a"));

        for (Vertex<Info<String>> v : infoGraph.getVertices().values()) {
            Info<String> info = v.getVal();
            String val = info.getVertex().getVal();
            int d = info.getD();
            int m = info.getM();

            System.out.println(val + "\td = " + d + "\tm = " + m);

        }

    }
}
