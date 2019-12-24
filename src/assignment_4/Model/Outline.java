package assignment_4.Model;

public class Outline {
    Vertex vertex;
    int length;

    private Outline() {
    }

    public Outline(Vertex v, int length) {
        this.vertex = v;
        this.length = length;
    }

    public Outline(int x, int y, int length) {
        this.vertex = new Vertex(x, y);
        this.length = length;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public int getLength() {
        return this.length;
    }
}

class Vertex {
    int x;
    int y;

    private Vertex() {
    }

    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
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
}
