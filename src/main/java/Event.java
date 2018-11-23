public class Event implements Comparable {

    private final Type type;
    private double y;
    private int index;
    private Site site;
    private Vector2 point;
    private Arc arc;

    public Event(Site site) {
        type = Type.SITE;
        y = site.getPoint().getY();
        index = -1;
        this.site = site;
    }

    public Event(double y, Vector2 point, Arc arc) {
        type = Type.CIRCLE;
        this.y = y;
        index = -1;
        this.point = point;
        this.arc = arc;
    }

    public int compareTo(Object o) {
        return (int)(this.y - ((Event)o).getY());
    }

    @Override
    public String toString() {
        if (type == Type.SITE) {
            return "S(" + site.getIndex() + ", " + y + ")";
        }
        else {
            return "C(" + arc + ", " + y + ", " + point + ")";
        }
    }

    public Type getType() {
        return type;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Vector2 getPoint() {
        return point;
    }

    public void setPoint(Vector2 point) {
        this.point = point;
    }

    public Arc getArc() {
        return arc;
    }

    public void setArc(Arc arc) {
        this.arc = arc;
    }
}
