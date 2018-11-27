public class HalfEdge implements Cloneable {

    private Vertex origin = null;
    private Vertex destination = null;
    HalfEdge twin = null;
    Face incidentFace;
    HalfEdge prev = null;
    HalfEdge next = null;

    public Vertex getOrigin() {
        return origin;
    }

    public Vertex getDestination() {
        return destination;
    }

    public HalfEdge getNext() {
        return next;
    }

    public HalfEdge getTwin() {
        return twin;
    }

    public void setDestination(Vertex destination) {
        this.destination = destination;
    }

    public void setOrigin(Vertex origin) {
        this.origin = origin;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
