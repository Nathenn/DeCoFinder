package decofinder.gui;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.UIManager;
import javax.swing.JComboBox;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;

import decofinder.algorithm.*;
import decofinder.display.DisplayGraph;
import decofinder.util.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import java.awt.Color;


public class MainFrame {

	//Frame
	private JFrame frmDecoFinder;
	
	//Panels
	private JPanel inputPanel;
	private JPanel barabasiPanel;
	private JPanel dorogPanel;
	private JPanel wattsPanel;
	private JPanel euclidianPanel;
	private JPanel algPanel;
	private JPanel outputPanel;
	private JPanel kCorePanel;
	private JPanel cliqueEnumPanel;
	
	
	//Labels
	private JLabel lblInputGraf;
	private JLabel lblGeneralas;
	private JLabel lblAlgoritmus;
	private JLabel lblParamterek;
	private JLabel lblMinMret;
	private JLabel lblMaxMret;
	private JLabel lblK;
	private JLabel lblBarabasiMaxLink;
	private JLabel lblBarabasiNumOfNodes;
	private JLabel lblDorogNumOfNodes;
	private JLabel lblWattsNumOfNodes;
	private JLabel lblWattsBaseDegree;
	private JLabel lblWattsRewireProbablity;
	private JLabel lblEucliNumOfNodes;
	
	//Buttons
	private JButton btnMehet;
	private JButton btnMegtekint;
	private JButton btnMegnyitas;
	private JButton btnBrowse;
	private JButton btnGeneralas;
	
	//TextFields
	private JTextField txtBrowsedFile;
	private JTextField txtMinInput;
	private JTextField txtMaxInput;
	private JTextField txtKcoreInput;
	private JTextField txtBarabasiMaxLink;
	private JTextField txtBarabasiNumOfNodes;
	private JTextField txtDorogNumOfNodes;
	private JTextField txtWattsNumOfNodes;
	private JTextField txtWattsBaseDegree;
	private JTextField txtWattsRewireProbablity;
	private JTextField txtEucliNumOfNodes;
	
	private JTextArea textConsole;
	private JScrollPane consoleScrollPane;
	
	private JComboBox<String> algComboBox;
	private JComboBox<String> genComboBox;
	
	private String filePath;
	private String algorithm;
	private String genAlgorithm;
	private static String cname[] = 	{"----Válasszon algoritmust----","CliqueEnum","CliqueEnum(Deg)","CliqueEnum(Tom)","CliqueEnum(Deg-Tom)","K-Core"};
	private static String genName[] = 	{"----Válasszon algoritmust----","Barabási-Albert generátor","Dorogovtsev-Mendes generátor"	,"Watts-Strogatz generátor", "Random Euklideszi generátor"};
	
	Graph graph;
	Graph resultGraph = new TinkerGraph();
	
	private PrintStream printStream;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					MainFrame window = new MainFrame();
					window.frmDecoFinder.setVisible(true);
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

		frmDecoFinder = new JFrame();
		frmDecoFinder.setTitle("DeCoFinder");
		frmDecoFinder.setMinimumSize(new Dimension(640, 560));
		frmDecoFinder.setResizable(false);
		frmDecoFinder.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDecoFinder.getContentPane().setLayout(null);
		frmDecoFinder.setLocationRelativeTo(null);
		
		//Creating Panels
		inputPanel = new JPanel();
		inputPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Bemenet", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		inputPanel.setBounds(10, 11, 602, 169);
		inputPanel.setLayout(null);
		
		algPanel = new JPanel();
		algPanel.setBorder(new TitledBorder(null, "Algoritmus kiválasztása és paraméterezése", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		algPanel.setBounds(10, 191, 602, 119);
		algPanel.setLayout(null);

		outputPanel = new JPanel();
		outputPanel.setBorder(new TitledBorder(null, "Kimeneti konzol", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		outputPanel.setBounds(10, 321, 602, 199);
		outputPanel.setLayout(null);
		
		cliqueEnumPanel = new JPanel();
		cliqueEnumPanel.setVisible(false);
		cliqueEnumPanel.setBounds(248, 51, 187, 57);
		cliqueEnumPanel.setLayout(null);
		
		kCorePanel = new JPanel();
		kCorePanel.setVisible(false);
		kCorePanel.setBounds(85, 51, 153, 57);
		kCorePanel.setLayout(null);
		
		barabasiPanel = new JPanel();
		barabasiPanel.setBounds(85, 89, 401, 56);
		barabasiPanel.setVisible(false);
		barabasiPanel.setLayout(null);
		
		dorogPanel = new JPanel();
		dorogPanel.setBounds(85, 89, 401, 56);
		dorogPanel.setVisible(false);
		dorogPanel.setLayout(null);
		
		wattsPanel = new JPanel();
		wattsPanel.setBounds(85, 89, 401, 56);
		wattsPanel.setVisible(false);
		wattsPanel.setLayout(null);
		
		euclidianPanel = new JPanel();
		euclidianPanel.setBounds(85, 89, 401, 56);
		euclidianPanel.setVisible(false);
		euclidianPanel.setLayout(null);
		
		
		//Creating Labels
		lblMinMret = new JLabel("Min. méret:");
		lblMinMret.setBounds(10, 11, 55, 14);
		lblMinMret.setToolTipText("A sûrû komponens minimális mérete; legalább ennyi csúcsot kell tartalmaznia az eredménynek.");
		
		lblMaxMret = new JLabel("Max. méret:");
		lblMaxMret.setBounds(10, 36, 59, 14);
		lblMaxMret.setToolTipText("A sûrû komponens maximális mérete; legfeljebb ennyi csúcsot tartalmazhat az eredmény.");

		lblGeneralas = new JLabel("Generálás:");
		lblGeneralas.setBounds(10, 64, 53, 14);
		
		lblK = new JLabel("K:");
		lblK.setBounds(10, 11, 10, 14);

		lblAlgoritmus = new JLabel("Algoritmus:");
		lblAlgoritmus.setBounds(10, 21, 54, 14);
		
		lblParamterek = new JLabel("Paraméterek:");
		lblParamterek.setBounds(10, 60, 65, 14);
		
		lblInputGraf = new JLabel("Input gráf:");
		lblInputGraf.setBounds(10, 24, 53, 14);
		
		lblBarabasiNumOfNodes = new JLabel("Csúcsok száma:");
		lblBarabasiNumOfNodes.setBounds(10, 8, 82, 14);
		
		lblBarabasiMaxLink = new JLabel("MaxLinkPerLépés:");
		lblBarabasiMaxLink.setBounds(10, 35, 86, 14);
		
		lblDorogNumOfNodes = new JLabel("Csúcsok száma:");
		lblDorogNumOfNodes.setBounds(10, 8, 82, 14);
		
		lblWattsNumOfNodes = new JLabel("Csúcsok száma:");
		lblWattsNumOfNodes.setBounds(10, 8, 82, 14);
		
		lblWattsBaseDegree = new JLabel("Alap fokszám:");
		lblWattsBaseDegree.setBounds(10, 35, 86, 14);
		
		lblWattsRewireProbablity = new JLabel("Élismétlés valószínûsége:");
		lblWattsRewireProbablity.setBounds(200, 8, 120, 14);
		
		lblEucliNumOfNodes = new JLabel("Csúcsok száma:");
		lblEucliNumOfNodes.setBounds(10, 8, 82, 14);
		
		//Creating TextFields
		txtMinInput = new JTextField();	//Input textField - MIN méret
		txtMinInput.setBounds(91, 8, 86, 20);
		txtMinInput.setColumns(10);

		txtMaxInput = new JTextField();	//Input textField - MAX méret
		txtMaxInput.setBounds(91, 33, 86, 20);
		txtMaxInput.setColumns(10);

		txtKcoreInput = new JTextField();	//kCorePanel.input
		txtKcoreInput.setBounds(30, 8, 86, 20);
		txtKcoreInput.setColumns(10);
		
		txtBrowsedFile = new JTextField();	//textfield: path of the choosed file
		txtBrowsedFile.setBounds(85, 21, 401, 20);
		txtBrowsedFile.setEditable(false);
		txtBrowsedFile.setColumns(10);
		
		txtBarabasiMaxLink = new JTextField();
		txtBarabasiMaxLink.setBounds(106, 32, 53, 20);
		txtBarabasiMaxLink.setColumns(10);
		
		txtBarabasiNumOfNodes = new JTextField();
		txtBarabasiNumOfNodes.setBounds(106, 5, 53, 20);
		txtBarabasiNumOfNodes.setColumns(10);
		
		txtDorogNumOfNodes = new JTextField();
		txtDorogNumOfNodes.setBounds(106, 5, 53, 20);
		txtDorogNumOfNodes.setColumns(10);
		
		txtWattsNumOfNodes = new JTextField();
		txtWattsNumOfNodes.setBounds(106, 5, 53, 20);
		txtWattsNumOfNodes.setColumns(10);
		
		txtWattsBaseDegree = new JTextField();
		txtWattsBaseDegree.setBounds(106, 32, 53, 20);
		txtWattsBaseDegree.setColumns(10);
		
		txtWattsRewireProbablity = new JTextField();
		txtWattsRewireProbablity.setBounds(330, 5, 53, 20);
		txtWattsRewireProbablity.setColumns(10);
		
		txtEucliNumOfNodes = new JTextField();
		txtEucliNumOfNodes.setBounds(106, 5, 53, 20);
		txtEucliNumOfNodes.setColumns(10);
		
		//Creating console TextArea with ScrollPane
		textConsole = new JTextArea();		//Console: information area about program-running
		textConsole.setFont(new Font("Arial", Font.PLAIN, 12));
		consoleScrollPane = new JScrollPane(textConsole);
		consoleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		consoleScrollPane.setBounds(10, 30, 585, 160);
		printStream = new PrintStream(new CustomOutputStream(textConsole));
		System.setOut(printStream);
		System.setErr(printStream);
		

		//Creating ComboBoxes
		algComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(cname));	//Algorithm-chosing ComboBox
		algComboBox.setBounds(85, 20, 401, 20);
		algComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				algorithm = (String) algComboBox.getSelectedItem();
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
		
		genComboBox = new JComboBox<String>(new DefaultComboBoxModel<String>(genName));
		genComboBox.setBounds(85, 58, 401, 20);
		genComboBox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {

				genAlgorithm = (String) genComboBox.getSelectedItem();
				JPanel[] Pans = {barabasiPanel, dorogPanel, wattsPanel, euclidianPanel};
				
				if(genAlgorithm == "Barabási-Albert generátor" ){
					for(JPanel pan : Pans){
						if(pan.equals(barabasiPanel)){
							pan.setVisible(true);
						}else{
							pan.setVisible(false);
						}
					}
				}else if(genAlgorithm == "Dorogovtsev-Mendes generátor"){
					for(JPanel pan : Pans){
						if(pan.equals(dorogPanel)){
							pan.setVisible(true);
						}else{
							pan.setVisible(false);

						}
					}
				}else if(genAlgorithm == "Watts-Strogatz generátor"){
					for(JPanel pan : Pans){
						if(pan.equals(wattsPanel)){
							pan.setVisible(true);
						}else{
							pan.setVisible(false);

						}
					}
				}else if(genAlgorithm == "Random Euklideszi generátor"){
					for(JPanel pan : Pans){
						if(pan.equals(euclidianPanel)){
							pan.setVisible(true);
						}else{
							pan.setVisible(false);

						}
					}
				}
				
			}
		});
		
		//Creating Buttons
		btnMehet = new JButton("Mehet");
		btnMehet.setBounds(503, 17, 88, 23);
		btnMehet.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int min=0, max=0, k=0;
				//read inputs
				if(cliqueEnumPanel.isVisible()){
					
					if(!txtMinInput.getText().isEmpty()){
						try{
							min = Integer.parseInt(txtMinInput.getText());
						}
						catch(NumberFormatException er){
							JOptionPane.showMessageDialog(frmDecoFinder, "Pozitív egész számot adjon meg minimum értéknek!");
							return;
						}
						
					}
					if(!txtMaxInput.getText().isEmpty()){
						try{
							max = Integer.parseInt(txtMaxInput.getText());
						}
						catch(NumberFormatException er){
							JOptionPane.showMessageDialog(frmDecoFinder, "Pozitív egész számot adjon meg maximum értéknek!");
							return;
						}
					}
					
				}else if(kCorePanel.isVisible()){
					
					if(txtKcoreInput.getText() != null){
						try{
							k = Integer.parseInt(txtKcoreInput.getText());
						}
						catch(NumberFormatException er){
							JOptionPane.showMessageDialog(frmDecoFinder, "Pozitív egész számot adjon meg K értékének!");
							return;
						}
					}
				}
				
				//filePath is already correct here, except if it is empty aka filePath != null
				if(!(algComboBox.getSelectedItem() == "----Válasszon algoritmust----") && filePath != null){
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
						resultGraph.shutdown();
						resultGraph = kc.createDenseComponent(graph, "output.graphml");
					}
				}else{
					JOptionPane.showMessageDialog(frmDecoFinder, "Nem választott algoritmust vagy nem adott meg input gráfot!");
					return;
				}

			}
		});
		
		btnMegtekint = new JButton("Megtekint");	//Display the visual layout of the found cliques
		btnMegtekint.setBounds(503, 51, 88, 23);
		btnMegtekint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DisplayGraph.display(); 
//				try {
//					GraphGeneration.readFile();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		});
		
		btnMegnyitas = new JButton("Megnyitás");	//Open the .graphml file with Gephi
		btnMegnyitas.setBounds(503, 85, 88, 23);
		btnMegnyitas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(new File("output.graphml").exists()){
					System.out.println("Output.graphml megnyitasa!");
					try {
						new ProcessBuilder("C:\\Program Files (x86)\\Gephi-0.8.2\\bin\\gephi64.exe", "output.graphml").start();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(frmDecoFinder, "Nem található Gephi a \n C:\\Program Files (x86)\\Gephi-0.8.2\\bin\\gephi64.exe \n helyen!");
						return;
					}
				}else{
					JOptionPane.showMessageDialog(frmDecoFinder, "Nem található output.graphml!");
					return;
				}
			}
		});
		
		btnBrowse = new JButton("Tallózás");		//Button: FileBrowser
		btnBrowse.setBounds(511, 20, 81, 23);
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				JFileChooser chooser= new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Graph file", "graphml");
				chooser.setFileFilter(filter);
				
				int choice = chooser.showOpenDialog(chooser);
				if (choice != JFileChooser.APPROVE_OPTION) return;

				if(!filter.accept(chooser.getSelectedFile())){
					JOptionPane.showMessageDialog(frmDecoFinder, "Nem megfelelõ fájlkiterjesztés!");
					return;
				}else{
					File chosenFile = chooser.getSelectedFile();
					txtBrowsedFile.setText(chosenFile.getAbsolutePath());
					filePath = chosenFile.getAbsolutePath();
					
					InputStream is;
					try {
						graph = new TinkerGraph();
						is = new FileInputStream(filePath);
						GraphMLReader.inputGraph(graph, is);
						
					} catch (IOException e) {
						JOptionPane.showMessageDialog(frmDecoFinder, "Hiba a gráf beolvasása közben!");
						e.printStackTrace();
						return;
					}
				}
			}
		});

		btnGeneralas = new JButton("Generálás");	//Button: "Generalas"
		btnGeneralas.setBounds(511, 54, 81, 24);
		btnGeneralas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(genComboBox.getSelectedItem() == "----Válasszon algoritmust----"){
					JOptionPane.showMessageDialog(frmDecoFinder, "Nem választott algoritmust!");
					return;
				}else if(genAlgorithm == "Barabási-Albert generátor"){
					filePath = "Barabasi-Albert.graphml";
					GeneratorParameter genParam = new GeneratorParameter(	genAlgorithm, 
																			Integer.parseInt(txtBarabasiNumOfNodes.getText()), 
																			Integer.parseInt(txtBarabasiMaxLink.getText()));
					GraphGeneration.generate(genParam);
					
					
				}else if(genAlgorithm == "Dorogovtsev-Mendes generátor"){
					filePath = "Dorogovtsev-Mendes.graphml";
					GeneratorParameter genParam = new GeneratorParameter(	genAlgorithm, 
																			Integer.parseInt(txtDorogNumOfNodes.getText()));
					GraphGeneration.generate(genParam);
					
					
				}else if(genAlgorithm == "Watts-Strogatz generátor"){	
					if(txtWattsRewireProbablity.getText().isEmpty() || txtWattsBaseDegree.getText().isEmpty() || txtWattsNumOfNodes.getText().isEmpty()){
						JOptionPane.showMessageDialog(frmDecoFinder, "Egy vagy több paramétert nem adott meg!");
						return;
					}
					if(Double.parseDouble(txtWattsRewireProbablity.getText()) > 1 || Double.parseDouble(txtWattsRewireProbablity.getText()) < 0){
						JOptionPane.showMessageDialog(frmDecoFinder, "Az élek újraszámításba-vételének valószínûsége 0 és 1 között kell legyen!");
						return;
					}
					if(Integer.parseInt(txtWattsBaseDegree.getText()) % 2 != 0 ){
						JOptionPane.showMessageDialog(frmDecoFinder, "Az alap fokszámnak párosnak kell lennie!");
						return;
					}
					
					filePath = "Watts-Strogatz.graphml";
					GeneratorParameter genParam = new GeneratorParameter(	genAlgorithm, 
																			Integer.parseInt(txtWattsNumOfNodes.getText()), 
																			Integer.parseInt(txtWattsBaseDegree.getText()),
																			Double.parseDouble(txtWattsRewireProbablity.getText()));
					GraphGeneration.generate(genParam);
				
					
				}else if(genAlgorithm == "Random Euklideszi generátor"){
					filePath = "Random-euclidean.graphml";
					GeneratorParameter genParam = new GeneratorParameter(	genAlgorithm, 
																			Integer.parseInt(txtEucliNumOfNodes.getText()));
					GraphGeneration.generate(genParam);
				}
				
				txtBrowsedFile.setText("Generált gráf: " + filePath);
				
				InputStream is;
				try {
					graph = new TinkerGraph();
					is = new FileInputStream(filePath);
					GraphMLReader.inputGraph(graph, is);
					
				} catch (IOException e) {
					JOptionPane.showMessageDialog(frmDecoFinder, "Hiba a gráf beolvasása közben!");
					e.printStackTrace();
					return;
				}
				
			}
		});
		

		//Adding components to panels
		barabasiPanel.add(lblBarabasiMaxLink);
		barabasiPanel.add(txtBarabasiMaxLink);
		barabasiPanel.add(lblBarabasiNumOfNodes);
		barabasiPanel.add(txtBarabasiNumOfNodes);

		wattsPanel.add(lblWattsBaseDegree);
		wattsPanel.add(lblWattsNumOfNodes);
		wattsPanel.add(lblWattsRewireProbablity);
		wattsPanel.add(txtWattsBaseDegree);
		wattsPanel.add(txtWattsNumOfNodes);
		wattsPanel.add(txtWattsRewireProbablity);

		dorogPanel.add(lblDorogNumOfNodes);
		dorogPanel.add(txtDorogNumOfNodes);
		
		euclidianPanel.add(lblEucliNumOfNodes);
		euclidianPanel.add(txtEucliNumOfNodes);
		
		inputPanel.add(dorogPanel);
		inputPanel.add(barabasiPanel);
		inputPanel.add(wattsPanel);
		inputPanel.add(euclidianPanel);
		inputPanel.add(btnBrowse);
		inputPanel.add(lblGeneralas);
		inputPanel.add(lblInputGraf);
		inputPanel.add(txtBrowsedFile);
		inputPanel.add(btnGeneralas);
		inputPanel.add(genComboBox);
		
		cliqueEnumPanel.add(lblMinMret);
		cliqueEnumPanel.add(lblMaxMret);
		cliqueEnumPanel.add(txtMinInput);
		cliqueEnumPanel.add(txtMaxInput);
		
		kCorePanel.add(lblK);
		kCorePanel.add(txtKcoreInput);
		
		algPanel.add(cliqueEnumPanel);
		algPanel.add(kCorePanel);
		algPanel.add(lblAlgoritmus);
		algPanel.add(lblParamterek);
		algPanel.add(algComboBox);
		algPanel.add(btnMehet);
		algPanel.add(btnMegtekint);
		algPanel.add(btnMegnyitas);
		
		outputPanel.add(consoleScrollPane);
		
		//Adding panels to the MainFrame
		frmDecoFinder.getContentPane().add(inputPanel);
		frmDecoFinder.getContentPane().add(algPanel);
		frmDecoFinder.getContentPane().add(outputPanel);

	}
}
