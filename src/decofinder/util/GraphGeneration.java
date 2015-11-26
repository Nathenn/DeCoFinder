package decofinder.util;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.graphstream.algorithm.generator.BarabasiAlbertGenerator;
import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomEuclideanGenerator;
import org.graphstream.algorithm.generator.WattsStrogatzGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.stream.file.FileSinkGraphML;
import org.graphstream.stream.file.FileSourceGraphML;
import org.graphstream.ui.view.View;
import org.graphstream.ui.view.Viewer;

public class GraphGeneration {

	
	
	public static void generate(GeneratorParameter genParam){
		
		String alg = genParam.getGeneratorName();
		Graph graph;
		
		if(alg == "----Válasszon algoritmust----"){
			
			JOptionPane.showMessageDialog(new JFrame(), "Nem választott generáló algoritmust!");
			return;
		}
		
		if(alg == "Barabási-Albert generátor"){
			
			int maxLinksPerStep = genParam.getMaxLinksPerStep();
			int numberOfNodes = genParam.getNumberOfNodes();
			
			graph = new SingleGraph("Barabasi-Albert");
			Generator gen = new BarabasiAlbertGenerator(maxLinksPerStep);
			gen.addSink(graph);
			gen.begin();
			for(int i=0; i<numberOfNodes; i++) {
			    gen.nextEvents();
			}
			gen.end();
			
			writeFile(graph, "Barabasi-Albert");
			displayGraph(graph);
			
		}
		
		else if(alg == "Dorogovtsev-Mendes generátor"){
			
			int numberOfNodes = genParam.getNumberOfNodes();
			graph = new SingleGraph("Dorogovtsev mendes");
			
			Generator gen = new DorogovtsevMendesGenerator();
			gen.addSink(graph);
			gen.begin();
			for(int i=0; i<numberOfNodes; i++) {
			    gen.nextEvents();
			}
			gen.end();
			
			writeFile(graph, "Dorogovtsev-Mendes");
			displayGraph(graph);

		}
		
		else if(alg == "Watts-Strogatz generátor"){
			
			int numberOfNodes = genParam.getNumberOfNodes();
			int baseDegree = genParam.getBaseDegree();
			double rewireProbablity = genParam.getRewireProbablity();
			
			graph = new SingleGraph("Watts-Strogatz");
			Generator gen = new WattsStrogatzGenerator(numberOfNodes, baseDegree, rewireProbablity);
			 
			gen.addSink(graph);
			gen.begin();
			while(gen.nextEvents()) {}
			gen.end();
			 
			writeFile(graph, "Watts-Strogatz");
			displayGraph(graph);
	
		}
		
		else if(alg == "Random Euklideszi generátor"){
			
			int numberOfNodes = genParam.getNumberOfNodes();
			
			graph = new SingleGraph("Random euclidean");
			Generator gen = new RandomEuclideanGenerator();
			gen.addSink(graph);
			gen.begin();
			for(int i=0; i<numberOfNodes; i++) {
			        gen.nextEvents();
			}
			gen.end();
			
			writeFile(graph, "Random-euclidean");
			displayGraph(graph);
		}
		
	}
	
	private static void displayGraph(Graph graph) {
		
		Viewer viewer = new Viewer(graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
		viewer.enableAutoLayout();
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.CLOSE_VIEWER);
		
		View view = viewer.addDefaultView(true);
		view.setMouseManager(new MyMouseManager());

		viewer.addView(view);

	}

	public static void writeFile(Graph graph, String graphName){
		FileSinkGraphML fs = new FileSinkGraphML();
		try {
			fs.writeAll(graph, graphName + ".graphml");
		} catch (IOException e) {
			JOptionPane.showMessageDialog(new JFrame(), "Hiba a gráf kiírása közben!");
			e.printStackTrace();
			return;
		}
	}
	
	
	//proba: meg nem mukodik
	//cel: barmelyik grafot(generalt, betallozott, mas konyvtar altal generalt) meg lehessen jeleniteni
	//hiba: csak a sajat(GraphStream) altal generalt .graphml fajlokat kezeli/olvassa be
	public static void readFile() throws IOException{
		
		String fileName = "C:/Users/Nathen/workspace/DeCoFinder/output.graphml";
		Graph g = new DefaultGraph("g");
		
	    
	    FileSourceGraphML reader = new FileSourceGraphML();
	    reader.addAttributeSink(g.getAttribute("label"));
	    
	    reader.addSink(g);
	    reader.readAll(fileName);

	    displayGraph(g);
	}
}
