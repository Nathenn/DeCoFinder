package decofinder.util;

public class GeneratorParameter {

	private String generatorName;
	
	

	int numberOfNodes;
	
	//Barabasi-Albert generator
	int maxLinksPerStep;
	
	//Watts-Strogatz generator
	int baseDegree;
	double rewireProbablity;
	
	//constructor: Barabasi
	public GeneratorParameter(String genName, int numofnodes, int maxlink){
		generatorName = genName;
		numberOfNodes = numofnodes;
		maxLinksPerStep = maxlink;
	}
	
	//constructor: Dorogovtsev-Mendes & Random Euclidian
	public GeneratorParameter(String genName, int numofnodes){
		generatorName = genName;
		numberOfNodes = numofnodes;
	}
	
	//constructor: Watts-Strogatz
	public GeneratorParameter(String genName, int numofnodes, int basedegree, double rewireprob){
		generatorName = genName;
		numberOfNodes = numofnodes;
		baseDegree = basedegree;
		rewireProbablity = rewireprob;
	}
	
	
	
	//Getter-setters
	public String getGeneratorName() {
		return generatorName;
	}

	public void setGeneratorName(String generatorName) {
		this.generatorName = generatorName;
	}

	public int getNumberOfNodes() {
		return numberOfNodes;
	}

	public void setNumberOfNodes(int numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
	}

	public int getMaxLinksPerStep() {
		return maxLinksPerStep;
	}

	public void setMaxLinksPerStep(int maxLinksPerStep) {
		this.maxLinksPerStep = maxLinksPerStep;
	}

	public int getBaseDegree() {
		return baseDegree;
	}

	public void setBaseDegree(int baseDegree) {
		this.baseDegree = baseDegree;
	}

	public double getRewireProbablity() {
		return rewireProbablity;
	}

	public void setRewireProbablity(double rewireProbablity) {
		this.rewireProbablity = rewireProbablity;
	}
	
}
