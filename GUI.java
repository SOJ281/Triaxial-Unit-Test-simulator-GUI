import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import java.util.*;
import java.io.*;
import java.lang.*;
import javax.swing.table.*;
import java.time.LocalDate; // import the LocalDate class
import javax.swing.UIManager.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;

import java.time.LocalTime;	// import the LocalTime class
import java.util.concurrent.TimeUnit;
/*
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
*/

public class GUI extends JFrame implements ActionListener, KeyListener/*, WindowListener, MouseListener, FocusListener*/ {
	
    public class InLabel extends JLabel {
        InLabel(String title, String explanation) {
            super(title);
            setToolTipText(explanation); //displays text when the mouse hovers over it
        }
    }
    
    public class InButton extends JButton {
        InButton(String title, String explanation) {
            super(title);
            setToolTipText(explanation); //displays text when the mouse hovers over it
        }
    }
    
    public class StartImage extends Canvas{  
        public void paint(Graphics g) {  
            Toolkit t = Toolkit.getDefaultToolkit();  
            Image i = t.getImage(getClass().getClassLoader().getResource("images/uniLogo.png"));
            g.drawImage(i, 0, 0,this);  
              
        }
        
        public void show() {  
            JFrame f=new JFrame();
            f.setUndecorated(true);
            //f.getContentPane().add(new JLabel(new ImageIcon(getClass().getResource("images/Smile.JPG"))));
            f.add(this);  
            f.setSize(1600, 516);
            f.setLocationRelativeTo(null);
            f.setVisible(true);
            try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            f.setVisible(false);
        }
      
    } 
    
    StartImage img = new StartImage();
    
    public class SampleBox extends JComboBox {
    	Database data = new Database();
    	public SampleBox() {
    		super();
    		//File f = new File(System.getProperty("user.dir"));
    		File f = new File("samples.txt");
    		if(!(f.exists() && !f.isDirectory())) {
    			//File file = new File("samples.txt");
    			data.addSample(new TriaxialTest("Clay", 1.1, 0.23, 0.03, 2.8, 0.3));
    			data.addSample(new TriaxialTest("Netherrack", 0.95, 0.2, 0.04, 2.5, 0.15));
        		data.addSample(new TriaxialTest("Orundum", 0.45, 0.5, 0.09, 7.5, 0.45));
        		data.addSample(new TriaxialTest("Kryptonite", 0.15, 0.8, 0.4, 10.5, 0.75));
        		data.writeSamplesListToFile();
    		}
    		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(data.getSampleNames());
    		setModel(model);
    	}
    	public void refresh() {
    		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(data.getSampleNames());
    		setModel(model);
    	}
    }
    
    class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            System.out.println(getWidth()+","+getHeight());
        }
    }
    
    WriteDataToExcel st = new WriteDataToExcel();
    
    JTabbedPane tabs = new JTabbedPane(); //creates tabs
    TriaxialTest test;
    
    LineChartPanel devi = new LineChartPanel();//"Deviatoric vs Axial", "Axial Strain, %", "Deviatoric Stress, KPa");
    LineChartPanel stressP = new LineChartPanel();//"Pore Water Pressure or Volumetric Strain", "Axial Strain, %", "Volumetic Strain, %");
    LineChartPanel watVol = new LineChartPanel();//"Pore Water Pressure or Volumetric Strain", "Axial Strain, %", "Volumetic Strain, %");
    LineChartPanel mesVsvr = new LineChartPanel();//"Mean effective Stress vs Void Ratio", "Mean Effective Stress, p, KPa", "Void ratio");
    ExplanPanel explanPanel = new ExplanPanel();
    
    //Main Panel
    JPanel upper = new JPanel();
    JPanel sample = new JPanel();
    JPanel procedure = new JPanel();
    //Sample
    int manual = 0;
    JButton showPanel;
    String[] s1 = { "Netherrack", "end sand", "bedrock", "clear"};
    Map<String, double[]> map = new HashMap<String, double[]>();
    //JComboBox subSelect = new JComboBox(s1);
    SampleBox subSelect = new SampleBox();
    
    //Procedure
    String[] s2 = {"drained", "undrained"}; //drained = 1, undrained = 2
    JComboBox analysis = new JComboBox(s2);
    //ValidatedField iteration = new ValidatedField(7500.0, 100000.0);
    //JTextField iteration = new JTextField();
    ValidatedField consolidation = new ValidatedField();
    ValidatedField confining = new ValidatedField();
    ValidatedField strsteps = new ValidatedField();
    JLabel description = new JLabel("---------------------Enter Data---------------------", SwingConstants.CENTER);
    InButton run;
    InButton editData;
    InButton saveData;
    EditTable editTable = new EditTable();
    
    
    InputData inputPanel = new InputData();



    public void startGUI() {
    	//setLocationRelativeTo(null);
    	//addComponentListener(new ResizeListener());
		//changes standard features of java components
        
    	try {
            UIManager.setLookAndFeel(
            UIManager.getCrossPlatformLookAndFeelClassName());
         } catch (Exception e) {}
		UIManager.put("OptionPane.messageFont", new Font("Segoe UI", Font.BOLD, 12)); //new standard message font
		UIManager.put("Button.font", new Font("Arial", Font.BOLD, 16));
		UIManager.put("Label.font", new Font("Arial", Font.BOLD, 16));
		UIManager.put("TabbedPane.font", new Font("Arial", Font.BOLD, 16));
		UIManager.put("TabbedPane.foreground", Color.black);
		UIManager.put("ComboBox.font", new Font("Arial", Font.BOLD, 16));
		UIManager.put("Label.foreground", Color.black);
		
		UIManager.put("TextArea.background", new Color(0,0,0,0));
		UIManager.put("TextArea.font", new Font("Arial", Font.BOLD, 18));
		UIManager.put("TextArea.foreground", Color.black);
		
		UIManager.put("TextField.font", new Font("Arial", Font.PLAIN, 14));
		UIManager.put("Table.font", new Font("Arial", Font.PLAIN, 13));
		UIManager.put("TableHeader.font", new Font("Arial", Font.BOLD, 14));
		
		//UIManager.put("OptionPane.border", noBorder);
		//UIManager.put("Button.background", Color.lightGray.brighter());
		UIManager.put("Button.foreground", Color.black);
		SwingUtilities.updateComponentTreeUI(this);
		
		/*
		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		for (int i = 0; i < fonts.length; i++) {
			System.out.println(fonts[i]);
		}
		
		for(int i = 0; i < 10; i+=2) { //for loop increments to reach the next tabs
			try{
				tabs.setBackgroundAt(i, badmintonBlue); //sets background color to blue
				tabs.setForegroundAt(i, Color.WHITE);	//sets text color to white
				
				tabs.setBackgroundAt(i+1, Color.BLACK); //sets background color to black
				tabs.setForegroundAt(i+1, Color.WHITE);	//sets text color to white
			}
			catch (Exception error) {		//Stops crashing when the pattern is completed
			}
		}*/
		
		tabs = new JTabbedPane(); //creates tabs
		
		upper = new JPanel();
	    sample = new JPanel();
	    
	    consolidation = new ValidatedField();
	    confining = new ValidatedField();
	    strsteps = new ValidatedField();
	    
	    procedure = new JPanel();
		editData = new InButton("Edit Soil Libraries", "Edit saved samples");
		showPanel = new InButton("Manual", "Manually enter values");
		subSelect = new SampleBox();
		analysis = new JComboBox(s2);
		description = new JLabel("---------------------Enter Data---------------------", SwingConstants.CENTER);
		run = new InButton("run", "Run triaxial test");
	    saveData = new InButton("Save All", "Save all triaxial data");
	    editTable = new EditTable();
	    inputPanel = new InputData();
	    
	    devi = new LineChartPanel();
	    stressP = new LineChartPanel();
	    watVol = new LineChartPanel();
	    mesVsvr = new LineChartPanel();
	    explanPanel = new ExplanPanel();
	    
	    editTable.viewTable.getViewport().addContainerListener(new ContainerListener() {
	    	public void componentRemoved(ContainerEvent e) {
	    		  subSelect.refresh();
	    	}
			@Override
			public void componentAdded(ContainerEvent arg0) {
				// TODO Auto-generated method stub
				
			}
	    });


		//new ImageIcon("badmintonLogo.jpg")).getImage().getScaledInstance(377,79,0)
        this.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		UIManager.put("ScrollBar.thumb" , Color.WHITE); // changes ScrollBar thumb look and feel to white
		UIManager.put("ScrollBar.background" , new Color(0,0,0,0)); //changes the ScrollBar background to transaparent
		
		//img.show();
		
		//Upper Panel code
        upper.add(editData);
        editData.addActionListener(this); //adds action listener
        //Sample
        upper.setLayout(new FlowLayout());
        con.fill = GridBagConstraints.BOTH;
        con.gridx = 0;
        con.gridy = 0;
        con.weightx = 0.5;
        con.weighty = 0.1;
        showPanel.addActionListener(this); //adds action listener
        sample.add(showPanel);
        upper.setBorder(
            BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(
                    EtchedBorder.RAISED, Color.black
                    , Color.DARK_GRAY), "Controls"));
        map.put("Netherrack", new double[]{0.95, 0.2, 0.04, 2.5, 0.15});
        map.put("end sand", new double[]{0.45, 0.5, 0.09, 7.5, 0.45});
        map.put("bedrock", new double[]{0.15, 0.8, 0.4, 10.5, 0.75});
        subSelect.addActionListener(this); //adds action listener
        sample.setBorder(
            BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(
                    EtchedBorder.RAISED, Color.black
                    , Color.DARK_GRAY), "Sample"));
        sample.add(subSelect);
        upper.add(sample);
        
        //Procedure
        analysis.addActionListener(this); //adds action listener
        procedure.add(analysis);
        
        procedure.add(new InLabel("consolidation", "consolidation"));
        consolidation.setColumns(5);
        consolidation.addKeyListener(this);
        procedure.add(consolidation);

	    procedure.add(new InLabel("confining", "confining"));
	    confining.setColumns(5);
	    confining.addKeyListener(this);
        procedure.add(confining);
        
        procedure.add(new InLabel("strsteps", "strsteps"));
        strsteps.setColumns(5);
        strsteps.addKeyListener(this);
        procedure.add(strsteps);
        procedure.setBorder(
            BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(
                    EtchedBorder.RAISED, Color.black
                    , Color.DARK_GRAY), "Test Procedure"));
        upper.add(procedure);
        
        description.addComponentListener(new ResizeListener());
        description.setSize(330, 16);
        description.setMinimumSize(new Dimension(330, 16));
        description.setPreferredSize(new Dimension(330, 16));
        //description.setText("---------------------Enter Data---------------------");
        description.setBackground(Color.white);
        description.setOpaque(true);
        upper.add(description);
        
        //upper.setBackground(Color.darkGray);
        upper.add(run);
        run.addActionListener(this); //adds action listener
        upper.add(saveData);
        saveData.addActionListener(this); //adds action listener
        this.add(upper, con);
        
        
        
        //Lower panel
        tabs.addTab("stress strain behaviour", devi);
        tabs.addTab("Stress Path", stressP);
        tabs.addTab("Pore water pressure / Volume change", watVol);
        tabs.addTab("Void ratio vs. p", mesVsvr);
        tabs.addTab("Explanation", explanPanel);
        con.gridx = 0;
        con.gridy = 1;
        con.weightx = 0.5;
        con.weighty = 0.9;
        //con.gridwidth = 2;
        con.fill = GridBagConstraints.BOTH;
        this.add(tabs, con);

        this.setTitle("Triaxial Unit Test"); //sets the title for the pane
        this.setSize(1550, 1200); //sets the JPanel size
        setMinimumSize(new Dimension(850, 520));
        setPreferredSize(new Dimension(400, 300));
        this.setVisible(true);
        this.setResizable(true);
        
        inputPanel.setVisible(false);
        inputPanel.setResizable(false);
        
        inputPanel.submit.addActionListener(this); //adds action listener
    }
    
    public void setDescription() {
        //description.setText(test.getTest());
    	try {
    		String temp = strsteps.getText();
	        description.setText(test.getSample() + "," + consolidation.getText() + "," + confining.getText() + "," + analysis.getSelectedItem() + ",7500" + "," + temp);
	        description.setToolTipText(test.getSampleExplan() 
	        		+ "<br>Consolidation pressure: " + consolidation.getText() +" KPa"
	        		+ "<br>Confining pressure: " + confining.getText() +" KPa"
	        		+ "<br>Sample is " + analysis.getSelectedItem() + 
	        		"<br>iterations are always 7500" + 
	        		"<br>strsteps: " + temp +"</html>");
    	} catch (Exception e) { }
            
    }
    
    public void loadAutoSample() throws Exception {
        Database temp = new Database();
        test = temp.searchByName(subSelect.getSelectedItem()+"");
        manual = 0;
        setDescription();
    }
    
    //runs triaxial test code and stores results
    public void runTest() {
    	long start = System.currentTimeMillis();
    	if (manual == 0)
			try {
				loadAutoSample();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				JOptionPane.showMessageDialog(null, "Unkown error, probably bad numbers entered", "Invalid", 0);
				e1.printStackTrace();
			}
        try {
            if (analysis.getSelectedItem().equals("drained"))
                test.runTest(consolidation.getFig(), confining.getFig(), 1, 7500, strsteps.getFig());
            else
                test.runTest(consolidation.getFig(), confining.getFig(), 2, 7500, strsteps.getFig());
        } catch (Exception e1) {
			JOptionPane.showMessageDialog(null, "Congratulations, you broke the model this program runs off,\n"
					+ "You probably entered some numbers incorrectly, check that first, if not contact the module leader.", "Invalid", 0);
			e1.printStackTrace();
		}
        long finish = System.currentTimeMillis();
        System.out.println("Test Time elapsed was"+(finish - start));
        try {
            setupTest();
        } catch(Exception e) {}
    }
    
    //Creates graphs and table data
    public void setupTest() throws Exception {
    	long start = System.currentTimeMillis();
        setDescription();
        double[][] es = test.getes();
        double[][] q = test.getq();
        double[][] esq = new double[2][test.getiter()];
        for (int i = 0; i < test.getiter(); i++) {
            esq[0][i] = es[0][i];
            esq[1][i] = q[i][0];
        }
        devi.initUI(esq, "Deviatoric vs Axial", "Axial Strain, %", "Deviatoric Stress, KPa");
        
        
        
        double[][] p = test.getp();
        double[][] qp = new double[2][test.getiter()];
        for (int i = 0; i < test.getiter(); i++) {
            qp[0][i] = p[i][0];
            qp[1][i] = q[i][0];
        }
        stressP.initUI(qp, "p vs q", "p, KPa", "q, KPa");
        
        
        
        double[][] epsV = test.getepsV();
        double[][] u = test.getu();
        double[][] wv = new double[2][test.getiter()];
        if (test.getanalysis() == 1) {
            for (int i = 0; i < test.getiter(); i++) {
                wv[0][i] = es[0][i];
                wv[1][i] = epsV[i][0]*100;
            }
            watVol.initUI(wv, "Pore Water Pressure or Volumetric Strain", "Axial Strain, %", "Volumetic Strain, %");
        } else {
            for (int i = 0; i < test.getiter(); i++) {
                wv[1][i] = p[i][0];
                wv[0][i] = es[0][i];
            }
            watVol.initUI(wv, "Pore Water Pressure or Volumetric Strain", "Axial strain, %", "Excess Pore Water Pressure");
        }
        
        
        
        double[][] voidy = test.getvoidy();
        double[][] pvoidy = new double[2][test.getiter()];
        for (int i = 0; i < test.getiter(); i++) {
            pvoidy[0][i] = p[i][0];
            pvoidy[1][i] = voidy[i][0];
        }
        mesVsvr.initUI(pvoidy, "Mean effective Stress vs Void Ratio", "Mean Effective Stress, p, KPa", "Void ratio");
        long finish = System.currentTimeMillis();
        System.out.println("Graph Time elapsed was"+(finish - start));
    }
    
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == inputPanel.submit) {
        	manual = 1;
            test = inputPanel.getSample();
            inputPanel.setVisible(false);
            setDescription();
        }
        if (ae.getSource() == showPanel) {
            inputPanel.setVisible(true);
        }
        
        
        if (ae.getSource() == run) {
        	runTest();
        }
        
        if (ae.getSource() == subSelect) {
            try {
				loadAutoSample();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        if (ae.getSource() == analysis) {
            setDescription();
        }
        
        if (ae.getSource() == saveData) {
            try {
                st.createAllTables(new double[][][]{devi.fullData, stressP.fullData, watVol.fullData, mesVsvr.fullData}, new String[]{devi.thisTitle, stressP.thisTitle, watVol.thisTitle, mesVsvr.thisTitle}, "TEST");
            } catch(Exception e) { 
                e.printStackTrace();
            }
        }
        
        if (ae.getSource() == editData) {
            try {
                editTable.setVisible(true);
                editTable.createWindow();
            } catch(Exception e) { 
                e.printStackTrace();
            }
        }
        
    }

    
    public void keyReleased(KeyEvent kevt) { //if a key is released
	}
	public void keyPressed(KeyEvent kevt) { //if a key is pressed
        if (kevt.getKeyCode() == KeyEvent.VK_ENTER) { //if key pressed
			if (strsteps.isFocusOwner()) { //if focus owner
				runTest();
			}
        }
	}
	public void keyTyped(KeyEvent kevt) { //if a specific key is pressed
	}
    
	public static void main(String[] args) {
        GUI msg = new GUI();
        msg.startGUI();
    }
}  