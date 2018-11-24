public class Beachline {

    public Arc createArc(Site site) {
//        return new Arc{mNil, mNil, mNil, site, null, null, null, mNil, mNil, Color.RED};
        return new Arc();
    }

    public boolean isEmpty() {
        return isNil(root);
    }

    public boolean isNil(Arc arc) {
        return arc == mNil;
    }

    public void setRoot(Arc arc) {
        root = arc;
        root.setColor(Color.BLACK);
    }

    public Arc getLeftmostArc() {
        Arc arc = root;
        while (!isNil(arc.getPrev()))
            arc = arc.getPrev();
        return arc;
    }

    public Arc locateArcAbove(Vector2 point, double l) {
        Arc node = root;
        boolean found = false;
        while (!found)
        {
            double breakpointLeft = -Double.MAX_VALUE;
            double breakpointRight = Double.MAX_VALUE;
            if (!isNil(node.getPrev()))
                breakpointLeft =  computeBreakpoint(node.getPrev().getSite().getPoint(), node.getSite().getPoint(), l);
            if (!isNil(node.getNext()))
                breakpointRight = computeBreakpoint(node.getSite().getPoint(), node.getNext().getSite().getPoint(), l);
            if (point.getX() < breakpointLeft)
                node = node.getLeft();
            else if (point.getX() > breakpointRight)
                node = node.getRight();
            else
                found = true;
        }
        return node;
    }

    public void insertBefore(Arc x, Arc y) {
        // Find the right place
        if (isNil(x.getLeft()))
        {
            x.setLeft(y);
            y.setParent(x);
        }
        else
        {
            x.getPrev().setRight(y);
            y.setParent(x.getPrev());
        }
        // Set the pointers
        y.setPrev(x.getPrev());
        if (!isNil(y.getPrev()))
            y.getPrev().setNext(y);
        y.setNext(x);
        x.setPrev(y);
        // Balance the tree
        insertFixup(y);
    }

    public void insertAfter(Arc x, Arc y) {
        // Find the right place
        if (isNil(x.getRight()))
        {
            x.setRight(y);
            y.setParent(x);
        }
        else
        {
            x.getNext().setLeft(y);
            y.setParent(x.getNext());
        }
        // Set the pointers
        y.setNext(x.getNext());
        if (!isNil(y.getNext()))
            y.getNext().setPrev(y);
        y.setPrev(x);
        x.setNext(y);
        // Balance the tree
        insertFixup(y);
    }

    public void replace(Arc x, Arc y) {
        transplant(x, y);
        y.setLeft(x.getLeft());
        y.setRight(x.getRight());
        if (!isNil(y.getLeft()))
            y.getLeft().setParent(y);
        if (!isNil(y.getRight()))
            y.getRight().setParent(y);
        y.setPrev(x.getPrev());
        y.setNext(x.getNext());
        if (!isNil(y.getPrev()))
            y.getPrev().setNext(y);
        if (!isNil(y.getNext()))
            y.getNext().setPrev(y);
        y.setColor(x.getColor());
    }

    public void remove(Arc z) {
        Arc y = z;
        Color yOriginalColor = y.getColor();
        Arc x;
        if (isNil(z.getLeft()))
        {
            x = z.getRight();
            transplant(z, z.getRight());
        }
        else if (isNil(z.getRight()))
        {
            x = z.getLeft();
            transplant(z, z.getLeft());
        }
        else
        {
            y = minimum(z.getRight());
            yOriginalColor = y.getColor();
            x = y.getRight();
            if (y.getParent() == z)
                x.setParent(y); // Because x could be Nil
            else
            {
                transplant(y, y.getRight());
                y.setRight(z.getRight());
                y.getRight().setParent(y);
            }
            transplant(z, y);
            y.setLeft(z.getLeft());
            y.getLeft().setParent(y);
            y.setColor(z.getColor());
        }
        if (yOriginalColor == Color.BLACK)
        removeFixup(x);
        // Update next and prev
        if (!isNil(z.getPrev()))
            z.getPrev().setNext(z.getNext());
        if (!isNil(z.getNext()))
            z.getNext().setPrev(z.getPrev());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Arc arc = getLeftmostArc();
        while (!isNil(arc))
        {
            sb.append(arc.getSite().getIndex()).append(' ');
            arc = arc.getNext();
        }
        return sb.toString();
    }

    public Beachline() {
        mNil = new Arc();
        root = mNil;
        mNil.setColor(Color.BLACK);
    }

    private Arc mNil;
    private Arc root;

    // Utility methods
    private Arc minimum(Arc arc) {
        while (!isNil(arc.getLeft()))
            arc = arc.getLeft();
        return arc;
    }

    private void transplant(Arc u, Arc v) {
        if (isNil(u.getParent()))
            root = v;
        else if (u == u.getParent().getLeft())
            u.getParent().setLeft(v);
        else
            u.getParent().setRight(v);
        v.setParent(u.getParent());
    }

    // Fixup functions
    private void insertFixup(Arc arc) {
        while (arc.getParent().getColor() == Color.RED)
        {
            if (arc.getParent() == arc.getParent().getParent().getLeft())
            {
                Arc y = arc.getParent().getParent().getRight();
                // Case 1
                if (y.getColor() == Color.RED)
                {
                    arc.getParent().setColor(Color.BLACK);
                    y.setColor(Color.BLACK);
                    arc.getParent().getParent().setColor(Color.RED);
                    arc = arc.getParent().getParent();
                }
                else
                {
                    // Case 2
                    if (arc == arc.getParent().getRight())
                    {
                        arc = arc.getParent();
                        leftRotate(arc);
                    }
                    // Case 3
                    arc.getParent().setColor(Color.BLACK);
                    arc.getParent().getParent().setColor(Color.RED);
                    rightRotate(arc.getParent().getParent());
                }
            }
            else
            {
                Arc y = arc.getParent().getParent().getLeft();
                // Case 1
                if (y.getColor() == Color.RED)
                {
                    arc.getParent().setColor(Color.BLACK);
                    y.setColor(Color.BLACK);
                    arc.getParent().getParent().setColor(Color.RED);
                    arc = arc.getParent().getParent();
                }
                else
                {
                    // Case 2
                    if (arc == arc.getParent().getLeft())
                    {
                        arc = arc.getParent();
                        rightRotate(arc);
                    }
                    // Case 3
                    arc.getParent().setColor(Color.BLACK);
                    arc.getParent().getParent().setColor(Color.RED);
                    leftRotate(arc.getParent().getParent());
                }
            }
        }
        root.setColor(Color.BLACK);
    }

    private void removeFixup(Arc arc) {
        while (arc != root && arc.getColor() == Color.BLACK)
        {
            Arc w;
            if (arc == arc.getParent().getLeft())
            {
                w = arc.getParent().getRight();
                // Case 1
                if (w.getColor() == Color.RED)
                {
                    w.setColor(Color.BLACK);
                    arc.getParent().setColor(Color.RED);
                    leftRotate(arc.getParent());
                    w = arc.getParent().getRight();
                }
                // Case 2
                if (w.getLeft().getColor() == Color.BLACK && w.getRight().getColor() == Color.BLACK)
                {
                    w.setColor(Color.RED);
                    arc = arc.getParent();
                }
                else
                {
                    // Case 3
                    if (w.getRight().getColor() == Color.BLACK)
                    {
                        w.getLeft().setColor(Color.BLACK);
                        w.setColor(Color.RED);
                        rightRotate(w);
                        w = arc.getParent().getRight();
                    }
                    // Case 4
                    w.setColor(arc.getParent().getColor());
                    arc.getParent().setColor(Color.BLACK);
                    w.getRight().setColor(Color.BLACK);
                    leftRotate(arc.getParent());
                    arc = root;
                }
            }
            else
            {
                w = arc.getParent().getLeft();
                // Case 1
                if (w.getColor() == Color.RED)
                {
                    w.setColor(Color.BLACK);
                    arc.getParent().setColor(Color.RED);
                    rightRotate(arc.getParent());
                    w = arc.getParent().getLeft();
                }
                // Case 2
                if (w.getLeft().getColor() == Color.BLACK && w.getRight().getColor() == Color.BLACK)
                {
                    w.setColor(Color.RED);
                    arc = arc.getParent();
                }
                else
                {
                    // Case 3
                    if (w.getLeft().getColor() == Color.BLACK)
                    {
                        w.getRight().setColor(Color.BLACK);
                        w.setColor(Color.RED);
                        leftRotate(w);
                        w = arc.getParent().getLeft();
                    }
                    // Case 4
                    w.setColor(arc.getParent().getColor());
                    arc.getParent().setColor(Color.BLACK);
                    w.getLeft().setColor(Color.BLACK);
                    rightRotate(arc.getParent());
                    arc = root;
                }
            }
        }
        arc.setColor(Color.BLACK);
    }

    // Rotations
    private void leftRotate(Arc arc) {
        Arc tmp = arc.getRight();
        arc.setRight(tmp.getLeft());
        if (!isNil(tmp.getLeft()))
            tmp.getLeft().setParent(arc);
        tmp.setParent(arc.getParent());
        if (isNil(arc.getParent()))
            root = tmp;
        else if (arc.getParent().getLeft() == arc)
            arc.getParent().setLeft(tmp);
        else
            arc.getParent().setRight(tmp);
        tmp.setLeft(arc);
        arc.setParent(tmp);
    }

    private void rightRotate(Arc arc) {
        Arc tmp = arc.getLeft();
        arc.setLeft(tmp.getRight());
        if (!isNil(tmp.getRight()))
            tmp.getRight().setParent(arc);
        tmp.setParent(arc.getParent());
        if (isNil(arc.getParent()))
            root = tmp;
        else if (arc.getParent().getLeft() == arc)
            arc.getParent().setLeft(tmp);
        else
            arc.getParent().setRight(tmp);
        tmp.setRight(arc);
        arc.setParent(tmp);
    }

    private double computeBreakpoint(Vector2 point1, Vector2 point2, double l) {
        double x1 = point1.getX(), y1 = point1.getY(), x2 = point2.getX(), y2 = point2.getY();
        double d1 = 1.0 / (2.0 * (y1 - l));
        double d2 = 1.0 / (2.0 * (y2 - l));
        double a = d1 - d2;
        double b = 2.0 * (x2 * d2 - x1 * d1);
        double c = (y1 * y1 + x1 * x1 - l * l) * d1 - (y2 * y2 + x2 * x2 - l * l) * d2;
        double delta = b * b - 4.0 * a * c;
        return (-b + Math.sqrt(delta)) / (2.0 * a);
    }

    private void free(Arc arc) {

    }

    public String getStringArc(Arc arc, String tabs) {
        StringBuilder sb = new StringBuilder();
        sb.append(tabs).append(arc.getSite().getIndex()).append(' ').append(arc.leftHalfEdge).append(' ').append(arc.rightHalfEdge).append('\n');
        if (!isNil(arc.getLeft())) {
            sb.append(getStringArc(arc.getLeft(), tabs+'\t'));
        }
        if (!isNil(arc.getRight())) {
            sb.append(getStringArc(arc.getRight(), tabs+'\t'));
        }
        return sb.toString();
    }

    /*std::ostream& printArc(std::ostream& os, const Arc* arc, std::string tabs = "") const;
    std::ostream& operator<<(std::ostream& os, const Beachline& beachline);*/
}
