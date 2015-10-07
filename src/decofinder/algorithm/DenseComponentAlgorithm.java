package decofinder.algorithm;

import com.tinkerpop.blueprints.Graph;



public interface DenseComponentAlgorithm {
	
	public Graph createDenseComponent(Graph graph, String output);
	
}
