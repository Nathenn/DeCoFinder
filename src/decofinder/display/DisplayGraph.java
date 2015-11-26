package decofinder.display;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;


public class DisplayGraph {

	
	
	public static void display(){
		//Generate a .pdf file (without signing of cliques)
		//source: http://stackoverflow.com/questions/5330889/gephi-api-example
		//source2: https://github.com/gephi/gephi-toolkit-demos/blob/master/src/org/gephi/toolkit/demos/HeadlessSimple.java

		if(!new File("output.graphml").exists()){
			JOptionPane.showMessageDialog(new JFrame(), "Nem letezik a vizualizaciohoz szukseges output.graphml!");
			return;
		}


		File graphmlFile = new File("output.graphml");

		//Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		//Get models and controllers for this new workspace - will be useful later
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
		ExportController ec = Lookup.getDefault().lookup(ExportController.class);


		//Import file
		org.gephi.io.importer.api.Container container;
		try {
			container = importController.importFile(graphmlFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		//Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);

		//Layout options
		ForceAtlasLayout layout = new ForceAtlasLayout(null);
		layout.setGraphModel(graphModel);
		layout.resetPropertiesValues();

		layout.initAlgo();
		for(int i= 0; i < 100 && layout.canAlgo(); i++){
			layout.goAlgo();
		}
		layout.endAlgo();


		//Export graph to PDF
		try {
			if(new File("graph.pdf").exists()){
				new File("graph.pdf").delete();
				System.out.println("A regi graph.pdf fajl torlolve!");
			}
			ec.exportFile(new File("graph.pdf"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//open the generated .pdf file
		//File graph = new File("graph.pdf");
		if(new File("graph.pdf").exists()){
			try {
				Desktop.getDesktop().open(new File("graph.pdf"));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(new JFrame(), "Nem sikerult megnyitni a pdf filet!");
				//e.printStackTrace();
				return;
			}
		}else{
			JOptionPane.showMessageDialog(new JFrame(), "Nem található graph.pdf!");
			return;
		}
	}
}


