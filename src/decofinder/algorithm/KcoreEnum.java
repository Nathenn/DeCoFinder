package decofinder.algorithm;

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
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;

import decofinder.util.GraphOperation;

public class KcoreEnum implements DenseComponentAlgorithm {

	private List<Vertex> G;
	//private HashMap<Integer, List<Vertex>> Cores;
	
	private int k;

	public Graph createDenseComponent(Graph g, String output){
		
		Graph graph;
		
		if(k!=0){
			System.out.println("Creating the graph's " + k + "-Core...");
			graph = calcKCore(g,k);
		}else{
			System.out.println("Creating the graph's Core...");
			graph = calcCore(g);
		}
		

		//print reszleg
		G = new ArrayList<Vertex>();
		G.addAll( (Collection<? extends Vertex>) graph.getVertices());
		G.sort(GraphOperation.compare);
		
		for (Iterator<Vertex> it = G.iterator(); it.hasNext();) {
			Vertex v = it.next();
			System.out.print(v);
		}
	
		//Writing output.graphml
		try {
			OutputStream os = new FileOutputStream(output);
			GraphMLWriter.outputGraph(graph, os);
			System.out.println("\n" + "Done!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return graph;
		
	}

	//Create K-Core with the minimum degree of k
	private Graph calcKCore(Graph g, int k){
		Graph graph = g;

		while(true){
			
			G = new ArrayList<Vertex>();
			G.addAll( (Collection<? extends Vertex>) graph.getVertices());
			G.sort(GraphOperation.compare);
			
			boolean modified = false;
			
			for (Iterator<Vertex> it = G.iterator(); it.hasNext();) {
				
				boolean deleted = false;
				Vertex v = it.next();
				
				if(GraphOperation.getNborSize(v) < k){
					graph.removeVertex(v);
					//it.remove();
					
					System.out.print("UQ:" + v);
					modified = true;
					break; //ezaltal a tobbi csucsnak is valtozik a fokszama --> ujraszamolas
				}else{
				
					
					for(Iterator<Vertex> it2 =  v.getVertices(Direction.BOTH).iterator(); it2.hasNext();){
						
						
						Vertex v2 = it2.next();
						if(GraphOperation.getNborSize(v2) < k){
							graph.removeVertex(v);
							//it2.remove();
							
							System.out.println("Torolt: " + v);
							modified = true;
							deleted = true;
							break; //elvileg kell, mert ha ket szomszedja szomszedos egymassal akkor befolyasolhatja az egeszet
						}
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
			
			G = new ArrayList<Vertex>();
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
