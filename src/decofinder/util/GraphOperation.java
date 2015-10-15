package decofinder.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;

public class GraphOperation {
	
	/*Ket csucslista metszetet adja vissza*/
	public static List<Vertex> intersect(Iterable<Vertex> c1, Iterable<Vertex> c2){
		
		List<Vertex> result = new ArrayList<Vertex>();
		
		for (Vertex v1 : c1) 
			for(Vertex v2 : c2)
				if( v1 == v2 )
					result.add(v2);

		return result;
	}
	
	/*Egy csucsot uniozunk hozza egy csucslistahoz (Iterable miatt kell igy)*/
	public static List<Vertex> unionVertex(Iterable<Vertex> c1, Vertex v1){
		
		if(!contains(c1,v1)){
		
			List<Vertex> result = new ArrayList<Vertex>();
			result = (List<Vertex>) c1;
			result.add(v1);
			return result;
		}else{
			List<Vertex> result = new ArrayList<Vertex>();
			result = (List<Vertex>) c1;
			return result;
		}
	}
	
	/*Ket csucslista uniojat adja vissza*/
	public static List<Vertex> unionList(Iterable<Vertex> c1, Iterable<Vertex> c2){
		List<Vertex> result = new ArrayList<Vertex>();
		result = (List<Vertex>) c1;
		
		for(Vertex v : c2){
			if (!result.contains(v)){
				result.add(v);
			}
		}
		
		return result;
	}
	
	/*c1 csucslistabol vonja ki a c2-t*/
	public static List<Vertex> subtract(Iterable<Vertex> c1, Iterable<Vertex> c2){
		//c1 - c2
		
		List<Vertex> c1list = new ArrayList<Vertex>();
		List<Vertex> c2list = new ArrayList<Vertex>();
		
		for (Vertex vertex : c2)
			c2list.add(vertex);
		
		for (Vertex vertex : c1)
			c1list.add(vertex);

		
		for(Vertex v2 : c2list){
			if(c1list.contains(v2)){
				c1list.remove(v2);
			}
		}

		return c1list;
	}
	
	/*S-ben levo csucsok kozos szomszedjait adja vissza*/
	public static List<Vertex> getCommonAdjs(Graph g, Iterable<Vertex> s){
		
		List<Vertex> result = new ArrayList<Vertex>();
		
		for (Vertex vertex : s) {
			
			if(result.isEmpty()){
				
				for (Vertex v1 : vertex.getVertices(Direction.BOTH)) {
					result.add(v1);
				}
			}		
			else{
				for (Vertex vert : vertex.getVertices(Direction.BOTH)) {
					if(result.equals(vert)){
						result.remove(vert);
						//ha igy nem mukodne, osszeszedjuk egy setbe es utana vonjuk ki az egeszet resultbol
					}
				}
			}
			
			
		}
		
		return result;
	}

	/*Igaz, ha az i csucslista tartalmazza a v csucsot*/
	public static boolean contains(Iterable<Vertex> i, Vertex v){

		for (Vertex vertex : i) {
			if(vertex.getId() == v.getId()){
				return true;
			}
		}
		return false;
	}

	/*Ket csucs kozul a kisebb fokszamuval ter vissza*/
	public static Comparator<Vertex> compare = new Comparator<Vertex>() {
		
		@Override
		public int compare(Vertex v1, Vertex v2) {
			
			  if (v1.getVertices(Direction.BOTH) instanceof Collection)
			    return ((Collection<?>)v1.getVertices(Direction.BOTH)).size();
			  // else iterate
			  int v1_size = 0;
			  for (Iterator<Vertex> it = v1.getVertices(Direction.BOTH).iterator(); it.hasNext(); ){ 
				  v1_size++; 
				  it.next();
			  }
			  
			  if (v2.getVertices(Direction.BOTH) instanceof Collection)
				    return ((Collection<?>)v2.getVertices(Direction.BOTH)).size();
			  // else iterate
			  int v2_size = 0;
			  for (Iterator<Vertex> it = v2.getVertices(Direction.BOTH).iterator(); it.hasNext(); ) {
				  v2_size++;
				  it.next();
			  }
				  
			  return v2_size > v1_size ? v1_size : v2_size;
			//return Integer.compare(v2_size, v1_size); 
		}
		
	};
	
	/*Egy csucs szomszedainak szamat adja vissza (Iterable miatt kell)*/
	public static int getNborSize(Vertex vertex){
		int size = 0;
		for(Iterator<Vertex> it = vertex.getVertices(Direction.BOTH).iterator(); it.hasNext();){
			size++;
			it.next();
		}
		return size;
	}
	
	/*Egy csucs szomszedaival ter vissza List-ben (Iterable miatt kell)*/
	public static List<Vertex> getNbors(Vertex vertex){
		List<Vertex> Nbors = new ArrayList<Vertex>();
		
		if(GraphOperation.getNborSize(vertex) != 0){
			for(Vertex v : vertex.getVertices(Direction.BOTH)){
				Nbors.add(v);
			}
		}
		
		return Nbors;
	}

	/*Torli a ket csucs kozotti elt (kcoreEnum)*/
	public static void removeEdge(Vertex v, Vertex v2) {
		
		Edge toRemove = null;
		Edge toRemove2 = null;
		
		for(Iterator<Edge> ite = v.getEdges(Direction.BOTH).iterator(); ite.hasNext();){
			Edge e = ite.next();
			for(Iterator<Edge> ite2 = v2.getEdges(Direction.BOTH).iterator(); ite2.hasNext();){
				Edge e2 = ite2.next();
				if(e.equals(e2)){
					toRemove = e;
					toRemove2 = e2;
					break;
				}
			}
			if(toRemove != null)
				break;
		}
		
		if(toRemove!=null){
			for(Iterator<Edge> ite = v.getEdges(Direction.BOTH).iterator(); ite.hasNext();){
				Edge e = ite.next();
				if(e.equals(toRemove)){
					ite.remove();
				}
			}
			for(Iterator<Edge> ite = v2.getEdges(Direction.BOTH).iterator(); ite.hasNext();){
				Edge e = ite.next();
				if(e.equals(toRemove)){
					ite.remove();
				}
			}
		}
		
	}
	
	/*Graph's size*/
	@SuppressWarnings("unused")
	public static int getGraphSize(Graph g){
		int i=0;
		for(Vertex v : g.getVertices()){
			i++;
		}
		return i;
	}
	
	
	
	public static void removeVertices(List<Vertex> collection, List<Vertex> removable){
		
		
		
		for(Iterator<Vertex> it = removable.iterator(); it.hasNext();){
			Vertex v = it.next();
			
			if(GraphOperation.contains(collection, v)){
				collection.remove(v);
			}
			
			
		}
	}

	
}




	