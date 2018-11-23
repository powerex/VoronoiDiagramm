public class Beachline {

    public Arc createArc(Site site) {
        return new Arc{mNil, mNil, mNil, site, null, null, null, mNil, mNil, Color.RED};
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
        while (z->parent->color == Arc::Color::RED)
        {
            if (z->parent == z->parent->parent->left)
            {
                Arc* y = z->parent->parent->right;
                // Case 1
                if (y->color == Arc::Color::RED)
                {
                    z->parent->color = Arc::Color::BLACK;
                    y->color = Arc::Color::BLACK;
                    z->parent->parent->color = Arc::Color::RED;
                    z = z->parent->parent;
                }
                else
                {
                    // Case 2
                    if (z == z->parent->right)
                    {
                        z = z->parent;
                        leftRotate(z);
                    }
                    // Case 3
                    z->parent->color = Arc::Color::BLACK;
                    z->parent->parent->color = Arc::Color::RED;
                    rightRotate(z->parent->parent);
                }
            }
            else
            {
                Arc* y = z->parent->parent->left;
                // Case 1
                if (y->color == Arc::Color::RED)
                {
                    z->parent->color = Arc::Color::BLACK;
                    y->color = Arc::Color::BLACK;
                    z->parent->parent->color = Arc::Color::RED;
                    z = z->parent->parent;
                }
                else
                {
                    // Case 2
                    if (z == z->parent->left)
                    {
                        z = z->parent;
                        rightRotate(z);
                    }
                    // Case 3
                    z->parent->color = Arc::Color::BLACK;
                    z->parent->parent->color = Arc::Color::RED;
                    leftRotate(z->parent->parent);
                }
            }
        }
        mRoot->color = Arc::Color::BLACK;
    }

    private void removeFixup(Arc arc) {
        while (x != mRoot && x->color == Arc::Color::BLACK)
        {
            Arc* w;
            if (x == x->parent->left)
            {
                w = x->parent->right;
                // Case 1
                if (w->color == Arc::Color::RED)
                {
                    w->color = Arc::Color::BLACK;
                    x->parent->color = Arc::Color::RED;
                    leftRotate(x->parent);
                    w = x->parent->right;
                }
                // Case 2
                if (w->left->color == Arc::Color::BLACK && w->right->color == Arc::Color::BLACK)
                {
                    w->color = Arc::Color::RED;
                    x = x->parent;
                }
                else
                {
                    // Case 3
                    if (w->right->color == Arc::Color::BLACK)
                    {
                        w->left->color = Arc::Color::BLACK;
                        w->color = Arc::Color::RED;
                        rightRotate(w);
                        w = x->parent->right;
                    }
                    // Case 4
                    w->color = x->parent->color;
                    x->parent->color = Arc::Color::BLACK;
                    w->right->color = Arc::Color::BLACK;
                    leftRotate(x->parent);
                    x = mRoot;
                }
            }
            else
            {
                w = x->parent->left;
                // Case 1
                if (w->color == Arc::Color::RED)
                {
                    w->color = Arc::Color::BLACK;
                    x->parent->color = Arc::Color::RED;
                    rightRotate(x->parent);
                    w = x->parent->left;
                }
                // Case 2
                if (w->left->color == Arc::Color::BLACK && w->right->color == Arc::Color::BLACK)
                {
                    w->color = Arc::Color::RED;
                    x = x->parent;
                }
                else
                {
                    // Case 3
                    if (w->left->color == Arc::Color::BLACK)
                    {
                        w->right->color = Arc::Color::BLACK;
                        w->color = Arc::Color::RED;
                        leftRotate(w);
                        w = x->parent->left;
                    }
                    // Case 4
                    w->color = x->parent->color;
                    x->parent->color = Arc::Color::BLACK;
                    w->left->color = Arc::Color::BLACK;
                    rightRotate(x->parent);
                    x = mRoot;
                }
            }
        }
        x->color = Arc::Color::BLACK;
    }

    // Rotations
    private void leftRotate(Arc arc) {
        Arc* y = x->right;
        x->right = y->left;
        if (!isNil(y->left))
            y->left->parent = x;
        y->parent = x->parent;
        if (isNil(x->parent))
            mRoot = y;
        else if (x->parent->left == x)
            x->parent->left = y;
        else
            x->parent->right = y;
        y->left = x;
        x->parent = y;
    }

    private void rightRotate(Arc arc) {
        Arc* x = y->left;
        y->left = x->right;
        if (!isNil(x->right))
            x->right->parent = y;
        x->parent = y->parent;
        if (isNil(y->parent))
            mRoot = x;
        else if (y->parent->left == y)
            y->parent->left = x;
        else
            y->parent->right = x;
        x->right = y;
        y->parent = x;
    }

    private double computeBreakpoint(Vector2 point1, Vector2 point2, double l) {
        double x1 = point1.x, y1 = point1.y, x2 = point2.x, y2 = point2.y;
        double d1 = 1.0 / (2.0 * (y1 - l));
        double d2 = 1.0 / (2.0 * (y2 - l));
        double a = d1 - d2;
        double b = 2.0 * (x2 * d2 - x1 * d1);
        double c = (y1 * y1 + x1 * x1 - l * l) * d1 - (y2 * y2 + x2 * x2 - l * l) * d2;
        double delta = b * b - 4.0 * a * c;
        return (-b + std::sqrt(delta)) / (2.0 * a);
    }

    private void free(Arc arc) {

    }

    /*std::ostream& printArc(std::ostream& os, const Arc* arc, std::string tabs = "") const;
    std::ostream& operator<<(std::ostream& os, const Beachline& beachline);*/
}
