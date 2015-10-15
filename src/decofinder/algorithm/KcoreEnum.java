package decofinder.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

import decofinder.util.GraphOperation;

public class KcoreEnum implements DenseComponentAlgorithm {

	//private HashMap<Integer, List<Vertex>> Cores;
	
	private int k;
	private Graph graph;
	private Graph resultGraph;

	public Graph createDenseComponent(Graph g, String output){
		
//		graph = new TinkerGraph();
		graph = g;
		//create a new copy from Graph g in the parameter line to not affect it by the algorithm
//		graph = new TinkerGraph();
//		for (Vertex vertex : g.getVertices()) {
//		    graph.addVertex(vertex.getId());
//		    for(Edge e : vertex.getEdges(Direction.BOTH)) {
//		    	  vertex.addEdge(e.getLabel(), e.getVertex(Direction.IN));
//		    }
//		}

		
//		for(Iterator<Vertex> it = graph.getVertices().iterator(); it.hasNext();){
//			Vertex v = it.next();
//			System.out.println("Vertex: " + v.toString() + "Nbors: " + GraphOperation.getNbors(v).toString());
//		}
		

		System.out.println("Creating the graph's " + k + "-Core...");
		
		resultGraph = calcKCore(graph,k);

		//print reszleg
		List<Vertex> sortedResult = new ArrayList<Vertex>();
		sortedResult.addAll( (Collection<? extends Vertex>) resultGraph.getVertices());
		sortedResult.sort(GraphOperation.compare);
		
		for (Iterator<Vertex> it = sortedResult.iterator(); it.hasNext();) {
			Vertex v = it.next();
			System.out.print(v + " ");
		}
		System.out.println("");
		
		//Writing output.graphml
		try {
			if(new File("output.graphml").exists()){
				new File("output.graphml").delete();
				System.out.println("Output.graphml frissitve!");
			}
			OutputStream os = new FileOutputStream(output);
			GraphMLWriter.outputGraph(resultGraph, os);
			System.out.println("Done!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return resultGraph;
		
	}

	//Create K-Core with the minimum degree of k
	private Graph calcKCore(Graph graph, int k){
		

		while(true){

			List<Vertex> G = new ArrayList<Vertex>();
			G.addAll( (Collection<? extends Vertex>) graph.getVertices());
			G.sort(GraphOperation.compare);

			//delete the vertices in G whose degree less than k (sorting)
//			List<Vertex> delList = new ArrayList<Vertex>();
//			for(Iterator<Vertex> it = G.iterator(); it.hasNext();){
//				Vertex v = it.next();
//				if(GraphOperation.getNborSize(v) < k){
//					graph.removeVertex(v);
//					delList.add(v);
//				}
//			}
//			
//			GraphOperation.removeVertices(G, delList);
			
			boolean modified = false;
			
			for (Iterator<Vertex> it = G.iterator(); it.hasNext();) {
				
				boolean deleted = false;
				Vertex v = it.next();
					
					for(Iterator<Vertex> it2 =  v.getVertices(Direction.BOTH).iterator(); it2.hasNext();){
						
						Vertex v2 = it2.next();
						if(GraphOperation.getNborSize(v2) < k){
							graph.removeVertex(v);
							it2.remove();
							
							System.out.println("Torolt: " + v);
							modified = true;
							deleted = true;
							break; //elvileg kell, mert ha ket szomszedja szomszedos egymassal akkor befolyasolhatja az egeszet
						}
					}
					
				if(deleted){
					break;
				}
			}
			
			if(!modified){ //ha nem tortent modositas, akkor kiugrunk a while-bol
				break;
			}
			
		}
		return graph;
	}


	//Create the graph's Core   <-------------MEG NEM JO
	private Graph calcCore(Graph g){
		Graph graph = g;

		while(true){
			
			List<Vertex> G = new ArrayList<Vertex>();
			G.addAll( (Collection<? extends Vertex>) graph.getVertices());
			G.sort(GraphOperation.compare);
			
			boolean modified = false;
			
			for (Iterator<Vertex> it = G.iterator(); it.hasNext();) {
				
				boolean deleted = false;
				Vertex v = it.next();
				

				for(Iterator<Vertex> it2 =  v.getVertices(Direction.BOTH).iterator(); it2.hasNext();){
					
					
					Vertex v2 = it2.next();
					if(GraphOperation.getNborSize(v2) > GraphOperation.getNborSize(v)){
						graph.removeVertex(v);
						//it2.remove();
						
						System.out.println("Torolt: " + v);
						modified = true;
						deleted = true;
						break; //elvileg kell, mert ha ket szomszedja szomszedos egymassal akkor befolyasolhatja az egeszet
					}
				}
				
				if(deleted){
					break;
				}
			}
			
			if(!modified){ //ha nem tortent modositas, akkor kiugrunk a while-bol
				break;
			}
			
		}
		return graph;
	}
	
	
	
	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	
	
	
}
