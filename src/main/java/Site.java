public class Site {

    int index;
    Vector2 point;
    Face face;

    public Site(int index, Vector2 point, Face face) {
        this.index = index;
        this.point = point;
        this.face = face;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Vector2 getPoint() {
        return point;
    }

    public void setPoint(Vector2 point) {
        this.point = point;
    }

    public Face getFace() {
        return face;
    }

    public void setFace(Face face) {
        this.face = face;
    }
}
