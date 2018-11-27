import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VoronoiDiagram {

    private List<Site> mSites;
    private List<Face> mFaces;
    private List<Vertex> mVertices;
    private List<HalfEdge> mHalfEdges;

    //*
    private Vertex createVertex(Vector2 point) {
        mVertices.emplace_back();
        mVertices.get(mVertices.size() - 1).setPoint(point);
        mVertices.back().it = std::prev(mVertices.end());
        return mVertices.get(mVertices.size() - 1);
    }

    private Vertex createCorner(Box box, Side side) {

    }

    private HalfEdge createHalfEdge(Face face) {

    }

    private void link(Box box, HalfEdge start, Side startSide, HalfEdge end, Side endSide) {

    }

    private void removeVertex(Vertex vertex) {

    }

    private void removeHalfEdge(HalfEdge halfEdge) {

    }

    public VoronoiDiagram(List<Vector2> points) {
        Collections.reverse(mSites);
        Collections.reverse(mFaces);
        for (int i=0; i<points.size(); i++) {
            mSites.add(new Site(i, points.get(i), null));
            mFaces.add(new Face(mSites.get(mSites.size()-1), null));
            mSites.get(mSites.size()-1).setFace(mFaces.get(mFaces.size()-1));
        }
    }

    public Site getSite(int index) {
        return mSites.get(index);
    }

    public int getNbSites() {
        return mSites.size();
    }

    public Face getFace(int index)
    {
        return mFaces.get(index);
    }

    public List<Vertex> getVertices() {
        return mVertices;
    }

    public List<HalfEdge> getHalfEdges() {
        return mHalfEdges;
    }

    public void intersect(Box box) throws CloneNotSupportedException {
        Set<HalfEdge> processedHalfEdges = new HashSet<HalfEdge>();
        Set<Vertex> verticesToRemove = new HashSet<Vertex>();
        for (Site site: mSites)
        {
            HalfEdge halfEdge = site.face.getOuterComponent();
            boolean inside = box.contains(halfEdge.getOrigin().getPoint());
            boolean outerComponentDirty = !inside;
            HalfEdge incomingHalfEdge = null; // First half edge coming in the box
            HalfEdge outgoingHalfEdge = null; // Last half edge going out the box
            Side incomingSide = null, outgoingSide = null;
            do
            {
                Intersection[] intersections = new Intersection[2];
                int nbIntersections = box.getIntersections(halfEdge.getOrigin().getPoint(), halfEdge.getDestination().getPoint(), intersections);
                HalfEdge nextHalfEdge = halfEdge.getNext();
                // The edge is completely outside the box
                if (nbIntersections == 0 && !inside)
                {
                    verticesToRemove.add((Vertex) halfEdge.getOrigin().clone()); // add or clone
                    //                    removeHalfEdge(halfEdge);
                }
                else if (nbIntersections == 1)
                {
                    // The edge is going outside the box
                    if (inside)
                    {
                        if (processedHalfEdges.contains(halfEdge.getTwin()))
                            halfEdge.setDestination(halfEdge.getTwin().getOrigin());
                        else
                            halfEdge.setDestination(createVertex(intersections[0].getPoint()));
                        outgoingHalfEdge = halfEdge;
                        outgoingSide = intersections[0].side;
                    }
                    // The edge is coming inside the box
                    else
                    {
                        verticesToRemove.add((Vertex) halfEdge.getOrigin().clone());
                        if (processedHalfEdges.contains(halfEdge.getTwin()))
                            halfEdge.setOrigin(halfEdge.getTwin().getDestination());
                        else
                            halfEdge.setOrigin(createVertex(intersections[0].point));
                        if (outgoingHalfEdge != null)
                            link(box, outgoingHalfEdge, outgoingSide, halfEdge, intersections[0].side);
                        if (incomingHalfEdge == null)
                        {
                            incomingHalfEdge = halfEdge;
                            incomingSide = intersections[0].side;
                        }
                    }
                    processedHalfEdges.add((HalfEdge) halfEdge.clone()); // add or clone
                }
                // The edge crosses twice the frontiers of the box
                else if (nbIntersections == 2)
                {
                    verticesToRemove.add((Vertex) halfEdge.getOrigin().clone());
                    if (processedHalfEdges.contains(halfEdge.getTwin()))
                    {
                        halfEdge.setOrigin(halfEdge.getTwin().getDestination());
                        halfEdge.setDestination(halfEdge.getTwin().getOrigin());
                    }
                    else
                    {
                        halfEdge.setOrigin(createVertex(intersections[0].point));
                        halfEdge.setDestination(createVertex(intersections[1].point));
                    }
                    if (outgoingHalfEdge != null)
                        link(box, outgoingHalfEdge, outgoingSide, halfEdge, intersections[0].side);
                    if (incomingHalfEdge == null)
                    {
                        incomingHalfEdge = halfEdge;
                        incomingSide = intersections[0].side;
                    }
                    outgoingHalfEdge = halfEdge;
                    outgoingSide = intersections[1].side;
                    processedHalfEdges.add((HalfEdge) halfEdge.clone());
                }
                halfEdge = nextHalfEdge;
                // Update inside
                inside = box.contains(halfEdge.getOrigin().getPoint());
            } while (halfEdge != site.getFace().getOuterComponent());
            // Link the last and the first half edges inside the box
            if (outerComponentDirty && incomingHalfEdge != null)
                link(box, outgoingHalfEdge, outgoingSide, incomingHalfEdge, incomingSide);
            // Set outer component
            if (outerComponentDirty)
                site.getFace().setOuterComponent(incomingHalfEdge);
        }
        // Remove vertices
        for (Vertex vertex: verticesToRemove)
        removeVertex(vertex);
    }

    public Vertex createVertex(Vector2 point) {
        mVertices.emplace_back();
        mVertices.back().point = point;
        mVertices.back().it = std::prev(mVertices.end());
        return &mVertices.back();
    }

    VoronoiDiagram::Vertex* VoronoiDiagram::createCorner(Box box, Box::Side side)
    {
        switch (side)
        {
            case Box::Side::LEFT:
            return createVertex(Vector2(box.left, box.top));
            case Box::Side::BOTTOM:
            return createVertex(Vector2(box.left, box.bottom));
            case Box::Side::RIGHT:
            return createVertex(Vector2(box.right, box.bottom));
            case Box::Side::TOP:
            return createVertex(Vector2(box.right, box.top));
            default:
                return nullptr;
        }
    }

    VoronoiDiagram::HalfEdge* VoronoiDiagram::createHalfEdge(Face* face)
    {
        mHalfEdges.emplace_back();
        mHalfEdges.back().incidentFace = face;
        mHalfEdges.back().it = std::prev(mHalfEdges.end());
        if(face->outerComponent == nullptr)
            face->outerComponent = &mHalfEdges.back();
        return &mHalfEdges.back();
    }

    void VoronoiDiagram::link(Box box, HalfEdge* start, Box::Side startSide, HalfEdge* end, Box::Side endSide)
    {
        HalfEdge* halfEdge = start;
        int side = static_cast<int>(startSide);
        while (side != static_cast<int>(endSide))
        {
            side = (side + 1) % 4;
            halfEdge->next = createHalfEdge(start->incidentFace);
            halfEdge->next->prev = halfEdge;
            halfEdge->next->origin = halfEdge->destination;
            halfEdge->next->destination = createCorner(box, static_cast<Box::Side>(side));
            halfEdge = halfEdge->next;
        }
        halfEdge->next = createHalfEdge(start->incidentFace);
        halfEdge->next->prev = halfEdge;
        end->prev = halfEdge->next;
        halfEdge->next->next = end;
        halfEdge->next->origin = halfEdge->destination;
        halfEdge->next->destination = end->origin;
    }

    void VoronoiDiagram::removeVertex(Vertex* vertex)
    {
        mVertices.erase(vertex->it);
    }

    void VoronoiDiagram::removeHalfEdge(HalfEdge* halfEdge)
    {
        mHalfEdges.erase(halfEdge->it);
    }
    //*/
}
