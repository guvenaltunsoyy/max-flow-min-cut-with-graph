import java.util.ArrayList;
import java.util.List;

public class Vertex {
    private String Id;
    private List<Vertex> Neighbors;
    private List<WeightedEdge> Edges;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
    public List<Vertex> getNeighbors() {
        return Neighbors;
    }

    public void setNeighbors(List<Vertex> neighbors) {
        Neighbors = neighbors;
    }

    public List<WeightedEdge> getEdges() {
        return Edges;
    }

    public void setEdges(List<WeightedEdge> edges) {
        Edges = edges;
    }

    public Vertex(String id){
        this.Id = id;
        Neighbors = new ArrayList<Vertex>();
        Edges = new ArrayList<WeightedEdge>();
    }
}
