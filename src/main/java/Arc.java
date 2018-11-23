import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class Arc {

    // Hierarchy
    private Arc parent;
    private Arc left;
    private Arc right;
    // Diagram
    private Site site;
    HalfEdge leftHalfEdge;
    HalfEdge rightHalfEdge;
    Event event;
    // Optimizations
    private Arc prev;
    private Arc next;
    // Only for balancing
    private Color color;


    public void setColor(Color color) {
        this.color = color;
    }

    public Arc getPrev() {
        return prev;
    }

    public Site getSite() {
        return site;
    }

    public Arc getNext() {
        return next;
    }

    public Arc getLeft() {
        return left;
    }

    public Arc getRight() {
        return right;
    }

    public Color getColor() {
        return color;
    }

    public Arc getParent() {
        return parent;
    }

    public void setLeft(Arc left) {
        this.left = left;
    }

    public void setParent(Arc parent) {
        this.parent = parent;
    }

    public void setRight(Arc right) {
        this.right = right;
    }

    public void setPrev(Arc prev) {
        this.prev = prev;
    }

    public void setNext(Arc next) {
        this.next = next;
    }

    @Override
    public String toString() {
        throw new NotImplementedException();
//        return "Arc to String not Implemented yet";
    }
}
