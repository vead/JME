package no.jschief.lupercal.voronoi2;


public class HalfEdge
{
    HalfEdge ELleft, ELright;
    Edge ELedge;
    boolean deleted;
    int ELpm;
    Site vertex;
    float ystar;
    HalfEdge PQnext;

    public HalfEdge()
    {
        PQnext = null;
    }
}
