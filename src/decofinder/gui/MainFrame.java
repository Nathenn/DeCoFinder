package decofinder.gui;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextPane;

import java.awt.BorderLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.ScrollPaneConstants;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.SystemColor;

import javax.swing.UIManager;
import javax.swing.JComboBox;













//import com.teradata.jdbc.jdbc_4.io.TDNetworkIOIF.Lookup;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;

import decofinder.algorithm.*;
import decofinder.display.DisplayGraph;
import decofinder.util.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.DropMode;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas.ForceAtlasLayout;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import java.awt.GridLayout;


public class MainFrame {

	private JFrame frame;
	private JTextField txtInputGraf;
	private JTextField txtBrowsedFile;
	private JTextField txtAlgoritmus;
	private JComboBox<String> comboBox;
	
	private String filePath;
	private String algorithm;
	private JButton btnMehet;
	
	private static String cname[] = {"----Válasszon algoritmust----","CliqueEnum","CliqueEnum(Deg)","CliqueEnum(Tom)","CliqueEnum(Deg-Tom)","K-Core"};
	
	Graph graph;
	Graph resultGraph = new TinkerGraph();
	private JTextField txtParamterek;
	private JTextField txtMinMret;
	private JTextField txtMaxMret;
	private JTextField minIn;
	private JTextField maxIn;
	private JTextField txtK;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 500, 340);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		
		//information area about program-running
		JTextArea textConsole = new JTextArea();
		JScrollPane scroll = new JScrollPane(textConsole);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(10, 155, 464, 135);
		PrintStream printStream = new PrintStream(new CustomOutputStream(textConsole));
		frame.getContentPane().add(scroll);
		System.setOut(printStream);
		System.setErr(printStream);
		

		//textField: "Input gráf"
		txtInputGraf = new JTextField();
		txtInputGraf.setEditable(false);
		txtInputGraf.setBackground(UIManager.getColor("TextField.disabledBackground"));
		txtInputGraf.setText("Input gr\u00E1f:");
		txtInputGraf.setBounds(10, 11, 63, 20);
		txtInputGraf.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		frame.getContentPane().add(txtInputGraf);
		txtInputGraf.setColumns(10);
		
		//Button: FileBrowser
		JButton BtnBrowse = new JButton("Tall\u00F3z\u00E1s");
		BtnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser= new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Graph file", "graphml");
				chooser.setFileFilter(filter);
				
				int choice = chooser.showOpenDialog(chooser);
				if (choice != JFileChooser.APPROVE_OPTION) return;

				if(!filter.accept(chooser.getSelectedFile())){
					JOptionPane.showMessageDialog(frame, "Nem megfelelõ fájlkiterjesztés!");
					return;
				}else{
					File chosenFile = chooser.getSelectedFile();
					txtBrowsedFile.setText(chosenFile.getAbsolutePath());
					filePath = chosenFile.getAbsolutePath(); //
					
					InputStream is;
					try {
						graph = new TinkerGraph();
						is = new FileInputStream(filePath);
						GraphMLReader.inputGraph(graph, is);
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		BtnBrowse.setBounds(373, 10, 101, 23);
		frame.getContentPane().add(BtnBrowse);
		
		
		//textfield: path of the choosed file
		txtBrowsedFile = new JTextField();
		txtBrowsedFile.setEditable(false);
		txtBrowsedFile.setBounds(93, 11, 270, 20);
		frame.getContentPane().add(txtBrowsedFile);
		txtBrowsedFile.setColumns(10);
		
		//textField: "Algorithm"
		txtAlgoritmus = new JTextField();
		txtAlgoritmus.setEditable(false);
		txtAlgoritmus.setBackground(UIManager.getColor("TextField.disabledBackground"));
		txtAlgoritmus.setText("Algoritmus:");
		txtAlgoritmus.setBounds(10, 42, 63, 20);
		txtAlgoritmus.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		frame.getContentPane().add(txtAlgoritmus);
		txtAlgoritmus.setColumns(10);
		txtAlgoritmus.setBackground(UIManager.getColor("TextField.disabledBackground"));
		
		//cliqueEnumPanel
		JPanel cliqueEnumPanel = new JPanel();
		cliqueEnumPanel.setVisible(false);
		cliqueEnumPanel.setBounds(94, 73, 127, 59);
		frame.getContentPane().add(cliqueEnumPanel);
		cliqueEnumPanel.setLayout(null);
		
		//kCorePanel
		JPanel kCorePanel = new JPanel();
		kCorePanel.setBounds(94, 73, 127, 59);
		frame.getContentPane().add(kCorePanel);
		kCorePanel.setVisible(false);
		kCorePanel.setLayout(null);
		
		//Algorithm-chosing ComboBox
		DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>(cname);
		comboBox = new JComboBox<String>(comboModel);
		
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				algorithm = (String) comboBox.getSelectedItem();
				
				JPanel[] Pans = {cliqueEnumPanel, kCorePanel};

				
				if(algorithm == "CliqueEnum" || algorithm == "CliqueEnum(Deg)" || algorithm == "CliqueEnum(Tom)" || algorithm == "CliqueEnum(Deg-Tom)"){
					for(JPanel pan : Pans){
						if(pan.equals(cliqueEnumPanel)){
							pan.setVisible(true);
						}else{
							pan.setVisible(false);
						}
					}
				}else if(algorithm == "K-Core"){
					for(JPanel pan : Pans){
						if(pan.equals(kCorePanel)){
							pan.setVisible(true);
						}else{
							pan.setVisible(false);
						}
					}
				}
				
			}
		});
		
		
		
		comboBox.setBounds(93, 42, 270, 20);
		frame.getContentPane().add(comboBox);
		
		
		btnMehet = new JButton("Mehet");
		btnMehet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//create inputVerifier
				InputVerifier verifier = new InputVerifier() {
				      public boolean verify(JComponent comp) {
				        boolean returnValue;
				        JTextField textField = (JTextField) comp;
				        try {
				          Integer.parseInt(textField.getText());
				          returnValue = true;
				        } catch (NumberFormatException e) {
				          returnValue = false;
				        }
				        return returnValue;
				      }
				    };
				
				int min=0, max=0, k=0;
				//read inputs
				if(cliqueEnumPanel.isVisible()){
					
					if(!minIn.getText().isEmpty()){
						try{
							min = Integer.parseInt(minIn.getText());
						}
						catch(NumberFormatException er){
							JOptionPane.showMessageDialog(frame, "Pozitív egész számot adjon meg minimum értéknek!");
							return;
						}
						
					}
					
					if(!maxIn.getText().isEmpty()){
						try{
							max = Integer.parseInt(maxIn.getText());
						}
						catch(NumberFormatException er){
							JOptionPane.showMessageDialog(frame, "Pozitív egész számot adjon meg maximum értéknek!");
							return;
						}
						
					}
				}else if(kCorePanel.isVisible()){
					
					if(textField_1.getText() != null){
						try{
							k = Integer.parseInt(textField_1.getText());
							
						}
						catch(NumberFormatException er){
							JOptionPane.showMessageDialog(frame, "Pozitív egész számot adjon meg K értékének!");
							return;
						}
						
					}
				}
				
				
				
				//filePath is already correct here, except if it is empty aka filePath != null
				if(!(comboBox.getSelectedItem() == "----Válasszon algoritmust----") && filePath != null){
					if(algorithm == "CliqueEnum"){
						CliqueEnum cl = new CliqueEnum();
						cl.setMinDense(min); cl.setMaxDense(max);
						resultGraph = cl.createDenseComponent(graph,"output.graphml");
					}else if(algorithm == "CliqueEnum(Deg)"){
						CliqueEnum cl = new CliqueEnum();
						cl.setMinDense(min); cl.setMaxDense(max);
						cl.setDegOrdering();
						resultGraph = cl.createDenseComponent(graph,"output.graphml");
					}else if(algorithm == "CliqueEnum(Tom)"){
						CliqueEnum cl = new CliqueEnum();
						cl.setMinDense(min); cl.setMaxDense(max);
						cl.setTomita();
						resultGraph = cl.createDenseComponent(graph,"output.graphml");
					}else if(algorithm == "CliqueEnum(Deg-Tom)"){
						CliqueEnum cl = new CliqueEnum();
						cl.setMinDense(min); cl.setMaxDense(max);
						cl.setDegOrdering();
						cl.setTomita();
						resultGraph = cl.createDenseComponent(graph,"output.graphml");
					}else if(algorithm == "K-Core"){
						KcoreEnum kc = new KcoreEnum();
						kc.setK(k);
						
						
						//debugprint
						//System.out.println("1:  " + graph.getVertices().toString());
						resultGraph = kc.createDenseComponent(graph, "output.graphml");
						//System.out.println("2:  " + graph.getVertices().toString());
						//return;
					}
				}else{
					JOptionPane.showMessageDialog(frame, "Nem választott algoritmust vagy nem adott meg input gráfot!");
					return;
				}

			}
		});
		btnMehet.setBounds(373, 41, 101, 23);
		frame.getContentPane().add(btnMehet);
		
		
		//Display the visual layout of the found cliques
		JButton btnMegtekint = new JButton("Megtekint");
		btnMegtekint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DisplayGraph.display(); 
			}
		});
		btnMegtekint.setBounds(373, 75, 101, 23);
		frame.getContentPane().add(btnMegtekint);
		
		
		//Open the .graphml file with Gephi
		JButton btnMegnyits = new JButton("Megnyit\u00E1s");
		btnMegnyits.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(new File("output.graphml").exists()){
					System.out.println("Output.graphml megnyitasa!");
					try {
						new ProcessBuilder("C:\\Program Files (x86)\\Gephi-0.8.2\\bin\\gephi64.exe", "output.graphml").start();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(frame, "Nem található Gephi a \n C:\\Program Files (x86)\\Gephi-0.8.2\\bin\\gephi64.exe \n helyen!");
						return;
					}
				}else{
					JOptionPane.showMessageDialog(frame, "Nem található output.graphml!");
					return;
				}
			}
		});
		btnMegnyits.setBounds(373, 109, 101, 23);
		frame.getContentPane().add(btnMegnyits);

		
		//textField: "Min. méret"
		txtMinMret = new JTextField();
		txtMinMret.setBounds(0, 0, 63, 20);
		cliqueEnumPanel.add(txtMinMret);
		txtMinMret.setToolTipText("A s\u0171r\u0171 komponens minim\u00E1lis m\u00E9rete, legal\u00E1bb ennyi cs\u00FAcsot kell tartalmaznia.");
		txtMinMret.setEditable(false);
		txtMinMret.setBackground(UIManager.getColor("TextField.disabledBackground"));
		txtMinMret.setText("Min. m\u00E9ret:");
		txtMinMret.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		txtMinMret.setColumns(10);
		
		//Input textField - MIN méret
		minIn = new JTextField();
		minIn.setBounds(73, 0, 40, 20);
		cliqueEnumPanel.add(minIn);
		minIn.setColumns(10);
		
		//textField: "Max. méret"
		txtMaxMret = new JTextField();
		txtMaxMret.setBounds(0, 31, 63, 20);
		cliqueEnumPanel.add(txtMaxMret);
		txtMaxMret.setEditable(false);
		txtMaxMret.setBackground(UIManager.getColor("TextField.disabledBackground"));
		txtMaxMret.setText("Max. m\u00E9ret:");
		txtMaxMret.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		txtMaxMret.setColumns(10);
		
		//Input textField - MAX méret
		maxIn = new JTextField();
		maxIn.setBounds(73, 31, 40, 20);
		cliqueEnumPanel.add(maxIn);
		maxIn.setColumns(10);
		
		//textfield: "Paraméterek"
		txtParamterek = new JTextField();
		txtParamterek.setBounds(10, 73, 77, 20);
		frame.getContentPane().add(txtParamterek);
		txtParamterek.setEditable(false);
		txtParamterek.setText("Param\u00E9terek:");
		txtParamterek.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		txtParamterek.setColumns(10);
		
		//kCorePanel.K
		txtK = new JTextField();
		txtK.setEditable(false);
		txtK.setText("K:");
		txtK.setBounds(40, 0, 25, 20);
		txtK.setBorder(javax.swing.BorderFactory.createEmptyBorder());
		kCorePanel.add(txtK);
		txtK.setColumns(10);
		
		//kCorePanel.input
		textField_1 = new JTextField();
		textField_1.setBounds(73, 0, 40, 20);
		kCorePanel.add(textField_1);
		textField_1.setColumns(10);
		
	

	}
}
