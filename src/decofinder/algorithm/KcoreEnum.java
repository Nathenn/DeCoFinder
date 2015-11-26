package decofinder.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.GraphHelper;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

import decofinder.util.GraphOperation;

public class KcoreEnum implements DenseComponentAlgorithm {
	
	private int k;
	

	public Graph createDenseComponent(Graph g, String output){

		//print
		System.out.println("Creating the graph's " + k + "-Core..." + "\n");

		Graph graph = new TinkerGraph();
		graph = calcKCore(g,k);
		
		
		//print section
		System.out.println("Basic graph: " + g);
		System.out.println("Result graph: " + graph);
		//System.out.println("Result: " + graph.getVertices());
		
		//Writing output.graphml
		try {
			if(new File("output.graphml").exists()){
				new File("output.graphml").delete();
				System.out.println("\nOutput.graphml frissitve!");
			}
			OutputStream os = new FileOutputStream(output);
			GraphMLWriter.outputGraph(graph, os);
			System.out.println("Done!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return graph;
		
	}

	//Create K-Core with the minimum degree of k
	private Graph calcKCore(Graph g, int k){
		
		//create a new copy from Graph g in the parameter line to not affect it by the algorithm
		Graph graph = new TinkerGraph();
		GraphHelper.copyGraph(g, graph);
		
		CopyOnWriteArrayList<Vertex> graphVertices = new CopyOnWriteArrayList<Vertex>();
		graphVertices.addAll( (Collection<? extends Vertex>) graph.getVertices());
		graphVertices.sort(GraphOperation.compare);

		//int firstDegree = GraphOperation.getNborSize(graphVertices.get(0));
		while (GraphOperation.getNborSize(graphVertices.get(0)) < k && graphVertices.size() !=0){
			graph.removeVertex(graphVertices.get(0));
			GraphOperation.updateListFromGraph(graphVertices, graph);
			graphVertices.sort(GraphOperation.compare);
			if(GraphOperation.getGraphSize(graph)==0){
				break;
			}
		}
		
//		for(Vertex v : graphVertices){
//			
//			if(GraphOperation.getNborSize(v)<k){
//				graphVertices.remove(v);
//				graph.removeVertex(v);
//				GraphOperation.updateListFromGraph(graphVertices, graph);
//				graphVertices.sort(GraphOperation.compare);
//				
//			}else{
//				
//				for(Vertex v2 : GraphOperation.getNbors(v)){
//					
//					if(GraphOperation.getNborSize(v2) < k){
//						graphVertices.remove(v2);
//						graph.removeVertex(v2);
//						GraphOperation.updateListFromGraph(graphVertices, graph);
//						graphVertices.sort(GraphOperation.compare);
//					}
//				}
//			}
//			
//		}

		
		return graph;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}
	
}
