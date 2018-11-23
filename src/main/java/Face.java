public class Face {

    Site site;
    HalfEdge outerComponent;

    public Face(Site site, HalfEdge outerComponent) {
        this.site = site;
        this.outerComponent = outerComponent;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public HalfEdge getOuterComponent() {
        return outerComponent;
    }

    public void setOuterComponent(HalfEdge outerComponent) {
        this.outerComponent = outerComponent;
    }
}
