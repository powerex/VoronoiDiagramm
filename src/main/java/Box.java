import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class Box {

    public static final double EPSILON = 1e-12;

    double left;
    double bottom;
    double right;
    double top;

    boolean contains(Vector2 point) {
        return point.getX() >= left - EPSILON && point.getX() <= right + EPSILON &&
                point.getY() >= bottom - EPSILON && point.getY() <= top + EPSILON;

    }

    Intersection getFirstIntersection(Vector2 origin, Vector2 direction) {
        Intersection intersection = new Intersection();
        double t = Double.MAX_VALUE;
        if (direction.getX() > 0.0)
        {
            t = (right - origin.getX()) / direction.getX();
            intersection.setSide(Side.RIGHT);
            intersection.setPoint(Vector2.add(origin, Vector2.mult(t, direction)));
        }
        else if (direction.getX() < 0.0)
        {
            t = (left - origin.getX()) / direction.getX();
            intersection.setSide(Side.LEFT);
            intersection.setPoint(Vector2.add(origin, Vector2.mult(t, direction)));
        }
        if (direction.getY() > 0.0)
        {
            double newT = (top - origin.getY()) / direction.getY();
            if (newT < t)
            {
                intersection.setSide(Side.TOP);
                intersection.setPoint(Vector2.add(origin, Vector2.mult(newT, direction)));
            }
        }
        else if (direction.getY() < 0.0)
        {
            double newT = (bottom - origin.getY()) / direction.getY();
            if (newT < t)
            {
                intersection.setSide(Side.BOTTOM);
                intersection.setPoint(Vector2.add(origin, Vector2.mult(newT, direction)));
            }
        }
        return intersection;
    }


    int getIntersections(Vector2 origin, Vector2 destination, Intersection[] intersections) {
// WARNING: If the intersection is a corner, both intersections are equals
        Vector2 direction = Vector2.minus(destination, origin);
        double[] t = new double[2];
        int i = 0; // index of the current intersection
        // Left
        t[i] = (left - origin.getX()) / direction.getX();
        if (t[i] > EPSILON && t[i] < 1.0 - EPSILON)
        {
            intersections[i].setSide(Side.LEFT);
            intersections[i].setPoint(Vector2.add(origin,Vector2.mult(t[i], direction)));
            if (intersections[i].point.getY() >= bottom  - EPSILON && intersections[i].point.getY() <= top + EPSILON)
                ++i;
        }
        // Right
        t[i] = (right - origin.getX()) / direction.getX();
        if (t[i] > EPSILON && t[i] < 1.0 - EPSILON)
        {
            intersections[i].setSide(Side.RIGHT);
            intersections[i].setPoint(Vector2.add(origin,Vector2.mult(t[i], direction)));
            if (intersections[i].point.getY() >= bottom - EPSILON && intersections[i].point.getY() <= top + EPSILON)
                ++i;
        }
        // Bottom
        t[i] = (bottom - origin.getY()) / direction.getY();
        if (i < 2 && t[i] > EPSILON && t[i] < 1.0 - EPSILON)
        {
            intersections[i].setSide(Side.BOTTOM);
            intersections[i].setPoint(Vector2.add(origin, Vector2.mult(t[i], direction)));
            if (intersections[i].point.getX() >= left  - EPSILON && intersections[i].point.getX() <= right + EPSILON)
                ++i;
        }
        // Top
        t[i] = (top - origin.getY()) / direction.getY();
        if (i < 2 && t[i] > EPSILON && t[i] < 1.0 - EPSILON)
        {
            intersections[i].setSide(Side.TOP);
            intersections[i].setPoint(Vector2.add(origin, Vector2.mult(t[i], direction)));
            if (intersections[i].point.getX() >= left - EPSILON && intersections[i].point.getX() <= right + EPSILON)
                ++i;
        }
        // Sort the intersections from the nearest to the farthest
        if (i == 2 && t[0] > t[1])
            Collections.swap(Arrays.asList(intersections), 0, 1);
        return i;
    }

}
