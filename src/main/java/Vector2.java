public class Vector2 {

    private double x;
    private double y;

    public Vector2() {
        x = 0;
        y = 0;
    }

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 getNegative() {
        return new Vector2(-x, -y);
    }

    public Vector2 appendVector(Vector2 argument) {
        x += argument.getX();
        y += argument.getY();
        return this;
    }

    public Vector2 stealVector(Vector2 argument) {
        x -= argument.getX();
        y -= argument.getY();
        return this;
    }

    public Vector2 multipleScalar(double lambda) {
        x *= lambda;
        y *= lambda;
        return this;
    }

    public Vector2 getOrthogonal() {
        return new Vector2(-y, x);
    }

    public double dot(Vector2 other) {
        return x * other.getX() + y * other.getY();
    }

    public double getNorm() {
        return Math.sqrt(x * x + y * y);
    }

    public double getDistance(Vector2 other) {
        return Vector2.minus(this, other).getNorm();
    }

    public double getDet(Vector2 other) {
        return x * other.getY() - y * other.getX();
    }

    public static Vector2 add(Vector2 lhs, Vector2 rhs) {
        return new Vector2(lhs.getX() + rhs.getX(), lhs.getY() + rhs.getY());
    }

    public static Vector2 minus(Vector2 lhs, Vector2 rhs) {
        return new Vector2(lhs.getX() - rhs.getX(), lhs.getY() - rhs.getY());
    }

    public static Vector2 mult(double t, Vector2 vec) {
        return new Vector2(t * vec.getX(), t * vec.getY());
    }

    public static Vector2 mult(Vector2 vec, double t) {
        return Vector2.mult(t, vec);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
