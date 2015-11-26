package decofinder.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;
import decofinder.util.GraphOperation;

public class CliqueEnum implements DenseComponentAlgorithm{

	private List<Vertex> c;
	private List<Vertex> nonCand;
	private List<Vertex> cand;
	
	private int denseId=0;
	private int minDense, maxDense; 
	private int maxCliqueSize;
	
	List<List<Vertex>> cliques;
	
	private boolean degOrdering = false;
	private boolean algTomita = false;
	
	private String algorithm;
		
	@Override
	public Graph createDenseComponent(Graph graph, String output) {
		
		
		//init
		cliques = new ArrayList<List<Vertex>>();
		nonCand = new ArrayList<Vertex>(); 	//init empty
		cand = new CopyOnWriteArrayList<Vertex>();
		c = new ArrayList<Vertex>(); 		//init empty
		
		maxCliqueSize = 0;
		
		cand.addAll( (Collection<? extends Vertex>) graph.getVertices());		

		//Calling the requested algorithm
		if(!algTomita == true && degOrdering != true){			//CliqueEnum
			System.out.println("Algorithm: CliqueEnum");
			cliqueEnumeration(c, cand, nonCand);
			algorithm = "CliqueEnum";
		}else if(algTomita == true && degOrdering != true){		//CliqueEnum(Tom)
			System.out.println("Algorithm: CliqueEnum(Tom)");
			cliqueEnumerationTomita(c, cand, nonCand);
			algorithm = "CliqueEnum(Tom)";
		}else if(!algTomita == true && degOrdering == true){	//CliqueEnum(Deg)
			System.out.println("Algorithm: CliqueEnum(Deg)");
			cliqueEnumeration(c, degOrder(graph), nonCand);
			algorithm = "CliqueEnum(Deg)";
		}else if(algTomita == true && degOrdering == true){		//CliqueEnum(Deg-Tom)
			System.out.println("Algorithm: CliqueEnum(Deg-Tom)");
			cliqueEnumerationTomita(c, degOrder(graph), nonCand);
			algorithm = "CliqueEnum(Deg-Tom)";
		}
		

		//Writing to log.txt
		try {
			FileWriter fw = new FileWriter("log.txt",true);
			
			fw.write("Algorithm: " + algorithm + System.lineSeparator());
			fw.write("Cliques: " + cliques.size() + System.lineSeparator());
			for (List<Vertex> cl : cliques) {
			    fw.write(cl.toString() + System.lineSeparator());
			}
			fw.write(System.lineSeparator());
			
			fw.close();
		} catch (IOException e1) {
			System.out.println("A log.txt fajl irasa kozben hiba tortent!");
			e1.printStackTrace();
		}
		
		
		//Write the result to console + giving denseIDs to vertices
		for (List<Vertex> cl : cliques) {
			for (Vertex vertex : cl) {
				if(graph.getVertex(vertex.getId()).getProperty("densegroup")==null)
					graph.getVertex(vertex.getId()).setProperty("densegroup", denseId);
			}
			System.out.println(cl);
			denseId++;
		}
		System.out.println("Cliques: " + cliques.size() + "\n");
		System.out.println("Maximal Clique Size: " + maxCliqueSize);
			

		//output: generate the .graphml file
		try {
			if(new File("output.graphml").exists()){
				new File("output.graphml").delete();
				System.out.println("Output.graphml frissitve!");
			}
			
			GraphMLWriter writer = new GraphMLWriter(graph);
			writer.setNormalize(true);
			writer.outputGraph(output);
			
//			OutputStream os = new FileOutputStream(output);
//			GraphMLWriter.outputGraph(graph, os);
			System.out.println("Done!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return graph;
	}

	
	/* This algorithm can execute the CliqueEnum and CliqueEnum(Deg) algorithms */
	private void cliqueEnumeration( List<Vertex> c, List<Vertex> cand, List<Vertex> nonCand ){
		
		if( cand.isEmpty() && nonCand.isEmpty() ){
			updateMaxClique(c);
			if(minDense!=0 || maxDense !=0){	//<----------if there exist a given MIN or MAX value
				if(minDense != 0 && maxDense != 0){			//BOTH MIN and MAX value is given
					if(c.size() >= minDense && c.size() <= maxDense)
						cliques.add(new ArrayList<Vertex>(c));
				}else if(minDense != 0 && maxDense == 0){	//ONLY MIN value is given
					if(c.size() >= minDense)
						cliques.add(new ArrayList<Vertex>(c));		
				}else if(minDense == 0 && maxDense != 0){	//ONLY MAX value is given
					if(c.size() <= maxDense)
						cliques.add(new ArrayList<Vertex>(c));
				}
			}else{
				cliques.add(new ArrayList<Vertex>(c));
			}				
		}else{

			for (Vertex vertex : cand) {

				List<Vertex> neighbours = new ArrayList<Vertex>(GraphOperation.getNbors(vertex));

				cliqueEnumeration( 	GraphOperation.unionVertex(c, vertex),    
									GraphOperation.intersect(cand, neighbours),
									GraphOperation.intersect(nonCand, neighbours));
				
				c.remove(vertex);
				cand.remove(vertex);
				nonCand.add(vertex);
				
			}
		}
		
	}

	/* This algorithm can execute the CliqueEnum(Tom) and CliqueEnum(Deg-Tom) algorithms */
	private void cliqueEnumerationTomita(List<Vertex> c, List<Vertex> cand, List<Vertex> nonCand){
		
		if( cand.isEmpty() && nonCand.isEmpty() ){
			updateMaxClique(c);
			if(minDense!=0 || maxDense !=0){	//<----------if there exist a given MIN or MAX value
				if(minDense != 0 && maxDense != 0){			//BOTH MIN and MAX value is given
					if(c.size() >= minDense && c.size() <= maxDense)
						cliques.add(new ArrayList<Vertex>(c));
				}else if(minDense != 0 && maxDense == 0){	//ONLY MIN value is given
					if(c.size() >= minDense)
						cliques.add(new ArrayList<Vertex>(c));		
				}else if(minDense == 0 && maxDense != 0){	//ONLY MAX value is given
					if(c.size() <= maxDense)
						cliques.add(new ArrayList<Vertex>(c));
				}
			}else{
				cliques.add(new ArrayList<Vertex>(c));
			}	
		}else if(!cand.isEmpty()){ // if cand is empty and we dont check this, the pivot will be chosen from nonCand

			//Choose pivot vertex in Cand U NonCand as the vertex with the highest number of neighbors in C OR maxdegree
			Vertex pivot = choosePivot(cand, nonCand);
			List<Vertex> pivotNbors = new ArrayList<Vertex>(GraphOperation.getNbors(pivot));

			//Cand \ N(pivot)
			List<Vertex> newcand = new CopyOnWriteArrayList<Vertex>(GraphOperation.subtract(cand, pivotNbors));
			
			// foreach (Cand \ N(pivot))
			for (Vertex vertex : newcand) {
				
				if(!GraphOperation.contains(nonCand, vertex)){
					List<Vertex> neighbours = new ArrayList<Vertex>(GraphOperation.getNbors(vertex));

					cliqueEnumerationTomita( 	GraphOperation.unionVertex(c, vertex),  
												GraphOperation.intersect(cand, neighbours),
												GraphOperation.intersect(nonCand, neighbours));
	
					c.remove(vertex);
					cand.remove(vertex);
					nonCand.add(vertex);
				}
			}
		}
		
	}
	
	/* Choose pivot vertex in Cand U NonCand as the vertex with the highest number of neighbors in Cand */
	private Vertex choosePivot(List<Vertex> cand, List<Vertex> nonCand){
		
		List<Vertex> union = new ArrayList<Vertex>(GraphOperation.unionList(cand, nonCand));
		
		int max = -1; 	//this value will be 0 after the first examination, so we dont need a boolean value to sign that we found a result 
						//like in the privous version; with max=-1 there is with a 7 line less code than before
		Vertex result = null;

		for(Vertex v: union){
			
			int temp = 0;	//counts how many neighbours of v are in Cand
			
			
			for(Vertex t : v.getVertices(Direction.BOTH)){
				if(GraphOperation.contains(cand, t))
					temp++;	
			}	
			
			if (temp > max){
				max = temp;
				result = v;
			}
			
		}
		
		return result;
		
	}
	
	/* Degeneracy ordering of the graph:
	 * source: http://www.datalab.uci.edu/muri/january2012/slides/GoodrichMURI2012.pdf
	 * In the first iteration, we delete one of the vertex with the less degree. We save the neighbours of that vertex and
	 * in the next iteration we examine one vertex with the less degree from the neighbours list AND one vertex with the 
	 * less degree from the remained graph(graph). We choose the one from neigbour list if its degree less or equals with
	 * the other one's degree, else we choose the other one. So first always check the deleted vertices neigbours, they 
	 * have higher priority. */
	private List<Vertex> degOrder(Graph graph){

		List<Vertex> graphVertices = new ArrayList<Vertex>((Collection<? extends Vertex>) graph.getVertices());
		List<Vertex> newCand = new CopyOnWriteArrayList<Vertex>();
		List<Vertex> prevNeighbours = new ArrayList<Vertex>();

		while(!graphVertices.isEmpty()){

			graphVertices.sort(GraphOperation.compare);
			Vertex graphVertFirst = graphVertices.get(0);

			// First check the neighbours of the prviously examined vertex
			if(prevNeighbours.size() != 0){
				
				prevNeighbours.sort(GraphOperation.compare);
				Vertex prevFirst = prevNeighbours.get(0);

				List<Vertex> NborList = GraphOperation.intersect( GraphOperation.getNbors(prevFirst) , graphVertices );			// Nbors of prevFirst
				List<Vertex> OtherList = GraphOperation.intersect( GraphOperation.getNbors(graphVertFirst) , graphVertices );	// Nbors of graphVertFirst
				
				if( NborList.size() <= OtherList.size() ){
					newCand.add( prevFirst );
					graphVertices.remove(prevFirst);
					prevNeighbours.clear();
					prevNeighbours = GraphOperation.intersect(graphVertices, GraphOperation.getNbors(prevFirst)) ;  

				}else{ 
					newCand.add( graphVertFirst );	
					graphVertices.remove(graphVertFirst);
					prevNeighbours.clear();
					prevNeighbours = GraphOperation.intersect(graphVertices, GraphOperation.getNbors(graphVertFirst)) ;
					
				}
			}else{ 
				newCand.add( graphVertFirst );	
				graphVertices.remove(graphVertFirst);
				prevNeighbours.clear();
				prevNeighbours = GraphOperation.intersect(graphVertices, GraphOperation.getNbors(graphVertFirst)) ;
				
			}
		}

		System.out.println("Order: ");
		System.out.println(newCand);
		return newCand;

	}
	
	private void updateMaxClique(List<Vertex> clique){
		if(clique.size() > maxCliqueSize)
			maxCliqueSize = clique.size();
	}
	
	public void setDegOrdering(){
		this.degOrdering = true;
	}

	public void setTomita() {
		this.algTomita = true;	
	}

	public int getMinDense() {
		return minDense;
	}

	public void setMinDense(int minDense) {
		this.minDense = minDense;
	}

	public int getMaxDense() {
		return maxDense;
	}

	public void setMaxDense(int maxDense) {
		this.maxDense = maxDense;
	}

}
