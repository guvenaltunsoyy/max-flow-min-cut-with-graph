public class WeightedEdge {
    private int Weight;
    private Vertex Start;
    private Vertex End;

    public int getWeigh() {
        return Weight;
    }

    public void setWeigh(int weigh) {
        Weight = weigh;
    }

    public Vertex getStart() {
        return Start;
    }
    public String getStartVertexId(){
        return Start.getId();
    }

    public String getEndVertexId(){
        return End.getId();
    }
    public void setStart(Vertex start) {
        Start = start;
    }

    public Vertex getEnd() {
        return End;
    }

    public void setEnd(Vertex end) {
        End = end;
    }

    public WeightedEdge(int weigh, Vertex start, Vertex end) {
        Weight = weigh;
        Start = start;
        End = end;
    }
}
