package no.jschief.lupercal.voronoi2;

class Edge {
    public float a = 0, b = 0, c = 0;
    Site[] ep;  // JH: End points?
    Site[] reg; // JH: Sites this edge bisects?
    int edgenbr;

    public Edge() {
        ep = new Site[2];
        reg = new Site[2];
    }
}
