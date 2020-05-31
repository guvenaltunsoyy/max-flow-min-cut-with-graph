import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class FXMaven extends Application {
    @FXML
    Button btnSayHello;
    @FXML
    TextField tfNodeCount;
    @FXML
    TextField tfFirstNodeId;
    @FXML
    TextField tfSecondNodeId;
    @FXML
    TextField tfWeight;
    @FXML
    GridPane myGrid;
    @FXML
    TextField tfTarget;
    @FXML
    TextField tfSource;
    @FXML
    Label lbResult;
    @FXML
    Label lbMaxFlow;
    public Vertex[] Nodes ;
    public List<WeightedEdge> Edges = new ArrayList<WeightedEdge>() ;
    public int[][] GraphMatrix;
    Graph UIGraph = new MultiGraph("Tutorial 1");
    String Style= "shape: line; size: 3px; fill-color: grey; fill-mode: plain; arrow-shape: arrow; arrow-size: 20px, 5px; text-style: bold-italic; text-size:30; text-offset: 0px, 4px;";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        final Class<?> myClass = this.getClass();
        final URL fxml = myClass.getResource("tes.fxml");
        Parent root = FXMLLoader.load(fxml);
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("Hosgeldiniz");
        stage.setScene(scene);
        stage.show();
    }

    public void sayHelloWorld(ActionEvent actionEvent) {
        System.out.println(tfNodeCount.getText());
        CreateGraph(Integer.parseInt(tfNodeCount.getText()));
    }

    public void CreateGraph(int nodeCount) {
        Nodes = new Vertex[nodeCount];
        for (int i = 0; i < nodeCount; i++) {
            Nodes[i] = new Vertex((String.valueOf(i)));
        }
        DrawNodes();
        GraphMatrix = new int[nodeCount][nodeCount];
        UIGraph.display();

    }
    public void DrawGraph(){
        UIGraph.clear();
        DrawNodes();
        DrawEdges();
    }
    public void DrawNodes(){
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        for(Vertex vertex:Nodes){
            var node = UIGraph.addNode(vertex.getId());
            node.addAttribute("ui.style","fill-color: rgb(0,100,255); size:30; text-style: bold-italic;  text-alignment: above; text-size:20; text-offset: 0px, 4px;");
            node.addAttribute("ui.label", node.getId());
            System.out.println(String.format("Created node id :%s", node.getId()));
        }
    }
    public void DrawEdges(){
        for (int i = 0; i < Edges.size(); i++) {
           try {
               var edge = UIGraph.addEdge(String.valueOf(i), Edges.get(i).getStartVertexId(), Edges.get(i).getEndVertexId(), true);
               edge.addAttribute("ui.label",String.valueOf(Edges.get(i).getWeigh()));
               edge.addAttribute("ui.style",Style);
               System.out.println(String.format("Created edge id :%s", edge.getId()));
           }catch (Exception e){
               System.err.println(e.getMessage());
           }
        }
    }
    public void AssignEdge(int startVertexId, int endVertexId, int weight){
        WeightedEdge edge = new WeightedEdge(weight, Nodes[startVertexId], Nodes[endVertexId]);
        Edges.add(edge);
        Nodes[startVertexId].getNeighbors().add(Nodes[endVertexId]);
        Nodes[startVertexId].getEdges().add(edge);
        try {
            UIGraph.addEdge(String.valueOf(Edges.indexOf(edge)), edge.getStartVertexId(), edge.getEndVertexId(), true);
        }
        catch (Exception e){
            System.err.println(e.getMessage());
        }
    }
    public void clearGraph(ActionEvent actionEvent) {
        UIGraph.clear();
    }
    public void connectNode(ActionEvent actionEvent) {
        System.out.println(String.format("1.dugum :%s 2.dugum :%s agirlik : %s",tfFirstNodeId.getText(), tfSecondNodeId.getText(), tfWeight.getText()));
        AssignEdge(Integer.parseInt(tfFirstNodeId.getText()),Integer.parseInt(tfSecondNodeId.getText()),Integer.parseInt(tfWeight.getText()));
    }
    public int[][] CreateEdgeMatrix(){
        int matrix[][] = new int[Nodes.length][Nodes.length];
        for (int i = 0; i < Nodes.length; i++) {
            for (WeightedEdge e: Nodes[i].getEdges()){
                matrix[i][e.getEndVertexIntId()]= e.getWeigh();
            }
        }
        for (int i = 0; i < Nodes.length; i++) {
            for (int j = 0; j < Nodes.length; j++) {
                System.out.print(matrix[i][j]+ " ");
            }
            System.out.println();
        }
        return matrix;
    }
    private static void dfs(int[][] rGraph, int s, boolean[] visited) {
        visited[s] = true;
        for (int i = 0; i < rGraph.length; i++) {
            if (rGraph[s][i] > 0 && !visited[i]) {
                dfs(rGraph, i, visited);
            }
        }
    }
    boolean bfs(int rGraph[][], int s, int t, int parent[])
    {
        // Create a visited array and mark all vertices as not
        // visited
        int V= Nodes.length;
        boolean visited[] = new boolean[V];
        for(int i=0; i<V; ++i)
            visited[i]=false;

        // Create a queue, enqueue source vertex and mark
        // source vertex as visited
        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        visited[s] = true;
        parent[s]=-1;

        // Standard BFS Loop
        while (queue.size()!=0)
        {
            int u = queue.poll();

            for (int v=0; v<V; v++)
            {
                if (visited[v]==false && rGraph[u][v] > 0)
                {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }
        // If we reached sink in BFS starting from source, then
        // return true, else false
        return (visited[t] == true);
    }
    int fordFulkerson(int graph[][], int s, int t)
    {
        int u, v;
        int V= Nodes.length;
        // Create a residual graph and fill the residual graph
        // with given capacities in the original graph as
        // residual capacities in residual graph

        // Residual graph where rGraph[i][j] indicates
        // residual capacity of edge from i to j (if there
        // is an edge. If rGraph[i][j] is 0, then there is
        // not)
        int rGraph[][] = new int[V][V];

        for (u = 0; u < V; u++)
            for (v = 0; v < V; v++)
                rGraph[u][v] = graph[u][v];

        // This array is filled by BFS and to store path
        int parent[] = new int[V];

        int max_flow = 0;  // There is no flow initially

        // Augment the flow while tere is path from source
        // to sink
        while (bfs(rGraph, s, t, parent))
        {
            // Find minimum residual capacity of the edhes
            // along the path filled by BFS. Or we can say
            // find the maximum flow through the path found.
            int path_flow = Integer.MAX_VALUE;
            for (v=t; v!=s; v=parent[v])
            {
                u = parent[v];
                path_flow = Math.min(path_flow, rGraph[u][v]);
            }

            // update residual capacities of the edges and
            // reverse edges along the path
            for (v=t; v != s; v=parent[v])
            {
                u = parent[v];
                rGraph[u][v] -= path_flow;
                rGraph[v][u] += path_flow;
            }

            // Add path flow to overall flow
            max_flow += path_flow;
        }
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                if (GraphMatrix[i][j] != 0) {
                    GraphMatrix[i][j] = rGraph[j][i];
                }
            }
        }
        System.out.println("MATRIX");
        printMatrix(GraphMatrix);
        DrawMaxFlowGraph(GraphMatrix);
        // Return the overall flow
        return max_flow;
    }
    public void printMatrix(int[][] matrix){
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    public void DrawMaxFlowGraph(int[][] graph){
        int sourceNode = Integer.parseInt(tfSource.getText()), targetNode = Integer.parseInt(tfTarget.getText());
        Random r=new Random(); //random sınıfı
        Graph MaxFlowGraph = new MultiGraph("Tutorial 2");
        for (int i = 0; i < Nodes.length; i++) {
            var n =MaxFlowGraph.addNode(String.valueOf(i));
            n.addAttribute("ui.label", String.valueOf(i));
            if (i == sourceNode){
                n.addAttribute("ui.style","fill-color: rgb(255,0,0); size:30; text-style: bold-italic;  text-alignment: above; text-size:20; text-offset: 0px, 4px;");
            }else if (i == targetNode){
                n.addAttribute("ui.style","fill-color: rgb(0,150,100); size:30; text-style: bold-italic;  text-alignment: above; text-size:20; text-offset: 0px, 4px;");
            }else{
                n.addAttribute("ui.style","fill-color: rgb(0,100,255); size:30; text-style: bold-italic;  text-alignment: above; text-size:20; text-offset: 0px, 4px;");
            }
        }
        for (int j = 0; j < Nodes.length; j++) {
            for (int i = 0; i < Nodes.length; i++) {
                if (graph[j][i] != 0){
                    var e = MaxFlowGraph.addEdge(String.valueOf(r.nextInt()), String.valueOf(j), String.valueOf(i), true);
                    e.addAttribute("ui.label", String.valueOf(graph[j][i]));
                    e.addAttribute("ui.style",Style);
                }
            }
        }
        MaxFlowGraph.display();
    }
    private void minCut(int[][] graph, int s, int t) {
        int u,v;

        // Create a residual graph and fill the residual
        // graph with given capacities in the original
        // graph as residual capacities in residual graph
        // rGraph[i][j] indicates residual capacity of edge i-j
        int[][] rGraph = new int[graph.length][graph.length];
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                rGraph[i][j] = graph[i][j];
            }
        }

        // This array is filled by BFS and to store path
        int[] parent = new int[graph.length];

        // Augment the flow while tere is path from source to sink
        while (bfs(rGraph, s, t, parent)) {

            // Find minimum residual capacity of the edhes
            // along the path filled by BFS. Or we can say
            // find the maximum flow through the path found.
            int pathFlow = Integer.MAX_VALUE;
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                pathFlow = Math.min(pathFlow, rGraph[u][v]);
            }

            // update residual capacities of the edges and
            // reverse edges along the path
            for (v = t; v != s; v = parent[v]) {
                u = parent[v];
                rGraph[u][v] = rGraph[u][v] - pathFlow;
                rGraph[v][u] = rGraph[v][u] + pathFlow;
            }
        }

        // Flow is maximum now, find vertices reachable from s
        boolean[] isVisited = new boolean[graph.length];
        dfs(rGraph, s, isVisited);

        // Print all edges that are from a reachable vertex to
        // non-reachable vertex in the original graph
        for (int i = 0; i < graph.length; i++) {
            for (int j = 0; j < graph.length; j++) {
                if (graph[i][j] > 0 && isVisited[i] && !isVisited[j]) {
                    System.out.println(i + " - " + j);
                    lbResult.setText(lbResult.getText() + i + " - " + j + "\n");
                }
            }
        }
    }
    public void findMaxFlow(ActionEvent actionEvent) {
        /*int nGraph[][] =new int[][] { {0, 16, 13, 0, 0, 0},
                                        {0, 0, 10, 12, 0, 0},
                                        {0, 4, 0, 0, 14, 0},
                                        {0, 0, 9, 0, 0, 20},
                                        {0, 0, 0, 7, 0, 4},
                                        {0, 0, 0, 0, 0, 0}
        };*/ // Test graph
        int sourceNode = Integer.parseInt(tfSource.getText()), targetNode = Integer.parseInt(tfTarget.getText());
        GraphMatrix = CreateEdgeMatrix();
        int result = fordFulkerson(CreateEdgeMatrix(),sourceNode, targetNode);
        System.out.println("MAX FLOW => " + result);
        lbMaxFlow.setText(lbMaxFlow.getText()+ " : " + result);
        minCut(GraphMatrix, sourceNode, targetNode);

    }
}
