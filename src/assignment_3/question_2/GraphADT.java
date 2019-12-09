package assignment_3.question_2;

import com.sun.tools.internal.ws.wsdl.framework.DuplicateEntityException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class GraphADT<E> {
    private Map<E,Vertex<E>> vertices;

    public boolean add(E val){
        if(!contains(val)){
            this.vertices.put(val,new Vertex<>(val));
            return true;
        }
        return false;
    }

    public boolean contains(E val){
        if(vertices.containsKey(val))
            return true;
        return false;
    }

    public boolean link(E val_1, E val_2){
        if(vertices.containsKey(val_1) && vertices.containsKey(val_2)){
            Vertex<E> v1 = vertices.get(val_1);
            Vertex<E> v2 = vertices.get(val_2);
            v1.link(v2);
            v2.link(v1);
            return true;
        }
        return false;
    }

    public Vertex<E> getVertex(E val){
        if(!vertices.containsKey(val))
            return null;
        return vertices.get(val);
    }

}

class Vertex<E>{
    private E val;
    private boolean visited;
    private LinkedList<Vertex<E>> adjList;

    public Vertex (E val){
        this.val = val;
        this.adjList = new LinkedList<>();
    }

    public boolean link(Vertex<E> v){
        if(adjList.contains(v)){
            return false;
        }
        adjList.add(v);
        return true;
    }

    public void setVisited(Boolean visited){
        this.visited = visited;
    }
}