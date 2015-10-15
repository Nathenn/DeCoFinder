package decofinder.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

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
	
	List<List<Vertex>> cliques;
	
	private boolean degOrdering = false;
	private boolean algTomita = false;
	
	private String algorithm;
		
	@Override
	public Graph createDenseComponent(Graph graph, String output) {
		
		//init
		cliques = new ArrayList<List<Vertex>>();
		nonCand = new ArrayList<Vertex>(); 	//init empty
		cand = new ArrayList<Vertex>();
		c = new ArrayList<Vertex>(); 		//init empty
		
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
			//cand.sort(GraphOperation.compare);
			cliqueEnumeration(c, degOrder(graph, (ArrayList<Vertex>) cand), nonCand);
			algorithm = "CliqueEnum(Deg)";
		}else if(algTomita == true && degOrdering == true){		//CliqueEnum(Deg-Tom)
			System.out.println("Algorithm: CliqueEnum(Deg-Tom)");
			System.out.println("OrderedCand: " + cand );
			cand.sort(GraphOperation.compare);
			cliqueEnumerationTomita(c, cand, nonCand);
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
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Write to console + giving denseIDs to vertices
		System.out.println("Cliques: " + cliques.size() + "\n");
		for (List<Vertex> cl : cliques) {
			for (Vertex vertex : cl) {
				if(graph.getVertex(vertex.getId()).getProperty("densegroup")==null)
					graph.getVertex(vertex.getId()).setProperty("densegroup", denseId);
			}
			System.out.println(cl);
			denseId++;
		}
			

		//output: generate the .graphml file
		try {
			if(new File("output.graphml").exists()){
				new File("output.graphml").delete();
				System.out.println("Output.graphml frissitve!");
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

	
	//This algorithm can execute the CliqueEnum and CliqueEnum(Deg) algorithms
	private void cliqueEnumeration( List<Vertex> c, List<Vertex> cand, List<Vertex> nonCand ){
		
		if( cand.isEmpty() && nonCand.isEmpty() ){
			
			if(minDense!=0 || maxDense !=0){
				
				if(minDense != 0 && maxDense != 0){
					if(c.size() >= minDense && c.size() <= maxDense){
						cliques.add(new ArrayList<Vertex>(c));
					}
				}else if(minDense != 0 && maxDense == 0){
					if(c.size() >= minDense){
						cliques.add(new ArrayList<Vertex>(c));
					}
				}else if(minDense == 0 && maxDense != 0){
					if(c.size() <= maxDense){
						cliques.add(new ArrayList<Vertex>(c));
					}
				}
			}else{
				cliques.add(new ArrayList<Vertex>(c));
			}
				
		}

		for (Iterator<Vertex> iterator = cand.iterator(); iterator.hasNext();) {
			Vertex vertex = iterator.next();
			iterator.remove(); //miert is?
			
			if(!c.contains(vertex)){
				c.add(vertex);
			}
			
			List<Vertex> neighbours = new ArrayList<Vertex>();
			for (Vertex v : vertex.getVertices(Direction.BOTH)) {
				neighbours.add(v);
			}
			
			
			cliqueEnumeration( 	c,  
								GraphOperation.intersect(cand, neighbours),
								GraphOperation.intersect(nonCand, neighbours));
			
			c.remove(vertex);
			nonCand.add(vertex);
			
		}

	}

	//This algorithm can execute the CliqueEnum(Tom) and CliqueEnum(Deg-Tom) algorithms
	private void cliqueEnumerationTomita(List<Vertex> c, List<Vertex> cand, List<Vertex> nonCand){
		
		if( cand.isEmpty() && nonCand.isEmpty() ){
			cliques.add(new ArrayList<Vertex>(c));
		}

		List<Vertex> cwnop = new ArrayList<Vertex>(cand); //Cand Without Neighbours Of Pivot
		
		//Choose pivot vertex in Cand U NonCand as the vertex with the highest number of neighbors in C OR maxdegree
		Vertex pivot = choosePivotByDegree(GraphOperation.unionList(cand, nonCand));
		
		//Cand \ N(pivot)
		cand = GraphOperation.subtract(cand, pivot.getVertices(Direction.BOTH));
		
		// foreach (Cand / N(pivot))
		for (Iterator<Vertex> iterator = cand.iterator(); iterator.hasNext();) {
			Vertex vertex = iterator.next();
			iterator.remove(); //miert is?
			
			if(!c.contains(vertex)){
				c.add(vertex);
			}
			
			List<Vertex> neighbours = new ArrayList<Vertex>();
			for (Vertex v : vertex.getVertices(Direction.BOTH)) {
				neighbours.add(v);
			}

			cliqueEnumeration( 	c,  
								GraphOperation.intersect(cwnop, neighbours),
								GraphOperation.intersect(nonCand, neighbours));

			
			c.remove(vertex);
			cwnop.remove(vertex); //itt volt a hiba: http://algos.org/maximal-cliquesbron-kerbosch-without-pivot-java/
			nonCand.add(vertex);
			
		}
	}
	
	//Choose pivot vertex in Cand U NonCand as the vertex with the highest number of neighbors in C
	/*Cand és nonCand uniójából azt a csúcsot választjuk ki, amelyiknek a legtöbb szomszédja található meg C-ben*/
	private Vertex ChoosePivot(List<Vertex> union){
		int max = 0;
		Vertex result = null;
		boolean foundResult = false;
		
		for(Vertex v: union){
			
			int temp = 0;	//számlálja, hány szomszédja van C-ben
			for(Vertex t : v.getVertices(Direction.BOTH)){
				
				if(GraphOperation.contains(cand, t)){
					temp++;
				}
			}	
			
			if (temp > max){
				if(!foundResult) foundResult = true;
				max = temp;
				result = v;
			}
			
		}
		if(!foundResult){
			System.out.println("pivot not found!");
			return GraphOperation.unionList(cand, nonCand).get(0); //ha nem talált semmit akkor az elsõ elemmel tér vissza
			
		}else{
			return result;
		}
	}
	
	/*Degeneracy ordering of the graph*/
	private ArrayList<Vertex> degOrder(Graph g, ArrayList<Vertex> cand){
		
		Graph G = g;
		ArrayList<Vertex> newcand = new ArrayList<Vertex>();
		
		while(GraphOperation.getGraphSize(G)!=0){
			ArrayList<Vertex> tempcand = new ArrayList<Vertex>();
			tempcand.addAll( (Collection<? extends Vertex>) G.getVertices());	
			tempcand.sort(GraphOperation.compare);
			
			newcand.add(tempcand.get(0));		
		}
		return newcand;
	}
	
	/*Cand és nonCand uniójából a legnagyobb fokszámú csúcsot választjuk ki*/
	private Vertex choosePivotByDegree(List<Vertex> union){
		union.sort(GraphOperation.compare);
		return union.get(union.size() - 1);
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
