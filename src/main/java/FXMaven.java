import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;
import scala.util.parsing.combinator.testing.Str;

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
    public Vertex[] Nodes ;
    public List<WeightedEdge> Edges = new ArrayList<WeightedEdge>() ;
    Graph UIGraph = new SingleGraph("Tutorial 1");
    Viewer viewer = new Viewer(UIGraph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
    View view = viewer.addDefaultView(false);   // false indicates "no JFrame".

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        final Class<?> myClass = this.getClass();
        final URL fxml = myClass.getResource("tes.fxml");
        Parent root = FXMLLoader.load(fxml);
        Scene scene = new Scene(root, 600, 600);
        stage.setTitle("FXML Welcome");
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
        /*UIGraph.addNode("A");
        UIGraph.addNode("B");
        UIGraph.addNode("C");
        UIGraph.addEdge("AB", "A", "B");
        UIGraph.addEdge("AC","A","C");
        UIGraph.addEdge("BC","B","C");
        for (var node : UIGraph) {
            node.addAttribute(, node.getId());
        }
        UIGraph.display();*/

    }
    public void DrawNodes(){
        for(Vertex vertex:Nodes){
            UIGraph.addNode(vertex.getId());
        }
        for (var node : UIGraph) {
            node.addAttribute("ui.label", node.getId());
        }
        UIGraph.display();
    }
    public void DrawEdges(){
        for (int i = 0; i < Edges.size(); i++) {
            UIGraph.addEdge(String.valueOf(i), Edges.get(i).getStartVertexId(), Edges.get(i).getEndVertexId());
        }
        int i = 0;
        for(Edge edge: UIGraph.getEachEdge()) {
            edge.addAttribute("ui.label",String.valueOf(Edges.get(i).getWeigh()));
            i++;
        }
    }
    public void AssignEdge(int startVertexId, int endVertexId, int weight){
        WeightedEdge edge = new WeightedEdge(weight, Nodes[startVertexId], Nodes[endVertexId]);
        Edges.add(edge);
        Nodes[startVertexId].getNeighbors().add(Nodes[endVertexId]);
        Nodes[startVertexId].getEdges().add(edge);
        Nodes[endVertexId].getNeighbors().add(Nodes[startVertexId]);
        Nodes[endVertexId].getEdges().add(edge);
        UIGraph.addEdge(String.valueOf(Edges.indexOf(edge)), edge.getStartVertexId(), edge.getEndVertexId());

    }

    public void clearGraph(ActionEvent actionEvent) {
        UIGraph.clear();
    }

    public void connectNode(ActionEvent actionEvent) {
        System.out.println(String.format("1.dugum :%s 2.dugum :%s agirlik : %s",tfFirstNodeId.getText(), tfSecondNodeId.getText(), tfWeight.getText()));
        AssignEdge(Integer.parseInt(tfFirstNodeId.getText()),Integer.parseInt(tfSecondNodeId.getText()),Integer.parseInt(tfWeight.getText()));
    }
}
