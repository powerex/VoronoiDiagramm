import java.util.List;

public class Vertex implements Cloneable {

    private Vector2 point;
    private List<Vertex> list;

    public Vector2 getPoint() {
        return point;
    }

    public void setPoint(Vector2 point) {
        this.point = point;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
