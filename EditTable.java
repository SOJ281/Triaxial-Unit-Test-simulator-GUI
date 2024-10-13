import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;
import java.io.*;
//import java.lang.*;
import javax.swing.table.*;
/*import java.time.LocalDate; // import the LocalDate class
import javax.swing.UIManager.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;*/

//import java.time.LocalTime;	// import the LocalTime class

//edit team panel components displayed
public class EditTable extends JFrame implements ActionListener{
    Database data = new Database();
	JLabel sampleNameHeading = new JLabel();
    ArrayList<JLabel> sampleName = new ArrayList<JLabel>();
    //ArrayList<JLabel> edit = new ArrayList<JLabel>();
    JPanel main = new JPanel();
    JPanel controls = new JPanel();
    JButton batch = new JButton("Batch add");
    JButton export = new JButton("export");
    JButton confirm = new JButton("Add");
    JButton deleteAll = new JButton("Delete all");
    ArrayList<JButton> edit = new ArrayList<JButton>();
    ArrayList<JButton> delete = new ArrayList<JButton>();
    
    //Custom scrolltable for storing add and data view
    public class ScrollableJTable extends JScrollPane {
		JTable table = new JTable(200, 8);
		Object[] columnNames = {"Name", "M", "Lamda (\u03bb)", "Kappa (\u03ba)", "N", "poissons ratio  (\u03bd)", "Delete", "Edit"};
		Object[][] rowData;
		Object[] addColumnNames = {"Name", "M", "Lamda (\u03bb)", "Kappa (\u03ba)", "N", "poissons ratio  (\u03bd)", "Add"};
		Object[][] addRowData;
		int add = 0;
		//ArrayList<JButton> delete = new ArrayList<JButton>();
		
		//Setup for the add function
		public ScrollableJTable(int q) {
	    	super();
	    	add = 1;
	    	setBorder(javax.swing.BorderFactory.createEmptyBorder());
	    	getViewport().add(table);
	    	initializeAdd();
	    }
		//Setup for full table version
	    public ScrollableJTable() {
	    	super();
	    	getViewport().add(table);
	    	initializeUI();
	    }
	    
	    //Add 
	    public void add() {
	    	try {
	    		if (data.addSample(new TriaxialTest(table.getModel().getValueAt(0, 0)+"",
	    			Double.parseDouble(table.getModel().getValueAt(0, 1)+""),
	    			Double.parseDouble(table.getModel().getValueAt(0, 2)+""),
	    			Double.parseDouble(table.getModel().getValueAt(0, 3)+""),
	    			Double.parseDouble(table.getModel().getValueAt(0, 4)+""),
	    			Double.parseDouble(table.getModel().getValueAt(0, 5)+""))) == false)
	    		JOptionPane.showMessageDialog(null, "Name is already taken, try again.", "Invalid", 0);
	    	} catch (Exception e) {
	    		JOptionPane.showMessageDialog(null, "Invalid data entered.", "Invalid", 0);
	    	}
	    }
	    
	    public void makeEdit(final int eRow) {
	    	try {
		    	data.editByName(table.getModel().getValueAt(eRow, 0)+"", 
		    			Double.parseDouble(table.getModel().getValueAt(eRow, 1)+""),
		    			Double.parseDouble(table.getModel().getValueAt(eRow, 2)+""),
		    			Double.parseDouble(table.getModel().getValueAt(eRow, 3)+""),
		    			Double.parseDouble(table.getModel().getValueAt(eRow, 4)+""),
		    			Double.parseDouble(table.getModel().getValueAt(eRow, 5)+""));
		    	initializeUI();
		    	doListeners();
	    	} catch (Exception e) {
	    		JOptionPane.showMessageDialog(null, "Invalid data entered.", "Invalid", 0);
	    	}
	    }
	    
	    public void readyEdit(final int editRow) {
	    	getViewport().remove(table);
	    	for (int i = 0; i < data.getNextSamplePosition(); i++)
	    		rowData[i][7] = "Edit";
	    	rowData[editRow][7] = "Confirm";
	    	table = new JTable(rowData, columnNames) {
	            @Override
	            public boolean isCellEditable(int row, int column) {
	            	if (editRow == row && column != 0)
	            		return true;
	            	else
	            		return false;
	            }
	        };
	        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
	            @Override
	            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	                c.setBackground(row == editRow ? Color.WHITE : Color.LIGHT_GRAY);
	                return c;
	            }
	        });
	        table.setRowHeight(table.getRowHeight() + 5);
	        table.getTableHeader().setReorderingAllowed(false);
	        table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
	        table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox()));
	        
	        table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
	        table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox()));
	        
	        getViewport().add(table);
	    }
	    
	    private void initializeUI() {
	    	data.readSamplesFromFile();
	        rowData = new Object[data.getNextSamplePosition()][8];
	        delete = new ArrayList<JButton>();
	        edit = new ArrayList<JButton>();
	        for (int i = 0; i < data.getNextSamplePosition(); i++) {
	        	TriaxialTest temp = data.getSampleAt(i);
	        	rowData[i][0] = temp.name;
	        	rowData[i][1] = new Double(temp.Miu);
	        	rowData[i][2] = new Double(temp.liu);
	        	rowData[i][3] = new Double(temp.kiu);
	        	rowData[i][4] = new Double(temp.Niu);
	        	rowData[i][5] = new Double(temp.nuiu);
	        	rowData[i][6] = "Delete";
	        	delete.add(new JButton());
	        	
	        	rowData[i][7] = "Edit";
	        	edit.add(new JButton());
	        	//delete.get(i).addActionListener(this);
	        }

	        getViewport().remove(table);
	    	table = new JTable(rowData, columnNames) {
	            @Override
	            public boolean isCellEditable(int row, int column) {
	            	if (column < 6)
	            		return false;
	            	return true;
	            }
	        };
	        table.setRowHeight(table.getRowHeight() + 5);
	        table.getTableHeader().setReorderingAllowed(false);
	        table.getColumn("Delete").setCellRenderer(new ButtonRenderer());
	        table.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox()));
	        table.getColumn("Edit").setCellRenderer(new ButtonRenderer());
	        table.getColumn("Edit").setCellEditor(new ButtonEditor(new JCheckBox()));
	        
	        getViewport().add(table);
	    }
	    
	    private void initializeAdd() {
	    	addRowData = new Object[1][7];
	    	addRowData[0][0] = "";
	        addRowData[0][1] = "";
	        addRowData[0][2] = "";
	        addRowData[0][3] = "";
	        addRowData[0][4] = "";
	        addRowData[0][5] = "";
	        addRowData[0][6] = "Add";
	        	

	        getViewport().remove(table);
	    	table = new JTable(addRowData, addColumnNames);
	    	table.setRowHeight(table.getRowHeight() + 5);
	    	table.getTableHeader().setReorderingAllowed(false);
	        table.getColumn("Add").setCellRenderer(new ButtonRenderer());
	        table.getColumn("Add").setCellEditor(new ButtonEditor(new JCheckBox()));
	        
	        //System.out.println("l"+addRowData.length+"r"+addRowData[0].length);
	        //System.out.println("l"+addColumnNames.length);
	        getViewport().add(table);
	    }
	    
	    class ButtonRenderer extends JButton implements TableCellRenderer {
	    	public ButtonRenderer() {
	    		setOpaque(true);
	    	}
	    	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	    		setText(value.toString());
	    		return this;
	    	}
	    }
	    
	    class ButtonEditor extends DefaultCellEditor {
	    	private String label;
	      
	    	public ButtonEditor(JCheckBox checkBox) {
	    		super(checkBox);
	    	}
	    	
	    	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		        label = value.toString();
		        if (add == 1)
		        	return confirm;
		        delete.get(row).setText(label);
		        if (column == 6)
		        	return delete.get(row);
		        edit.get(row).setText(label);
		        return edit.get(row);
	    	}
	    	public Object getCellEditorValue() {
	    	  	return new String(label);
	    	}
	    }
	}
    //Object[] addColumnNames = {"Name", "M", "Lamda (\u03bb)", "Kappa (\u03ba)", "N", "poissons ratio  (\u03bd)", "Add"};
	
    ScrollableJTable viewTable = new ScrollableJTable();
    ScrollableJTable addTable = new ScrollableJTable(1);
    
    //Initialises and arranges components for the window
	public void createWindow() {		
        data.readSamplesFromFile();
		//actual panel defined
		setTitle("EditTable");

        setSize(1150,400); //sets size
        setForeground(Color.WHITE);
        setBackground(Color.WHITE);
        setResizable(true);
        GridBagConstraints con = new GridBagConstraints();
        main.setLayout(new GridBagLayout());
        add(main);
        
        con.fill = GridBagConstraints.BOTH;
        Insets f = new Insets(20, 30, 0, 30);//int top, int left, int bottom, int right
        con.insets = f;
        con.gridx = 0;
        con.gridy = 0;
        con.weightx = 0.5;
        con.weighty = 0.9;
        con.gridwidth = 4;
        add(main);
        main.add(viewTable, con);
        
        
        f = new Insets(0, 30, 0, 30);//int top, int left, int bottom, int right
        con.insets = f;
        con.weighty = 0.1;
        con.gridy = 1;
        main.add(controls, con);
		controls.add(batch);
		batch.addActionListener(this);
		controls.add(deleteAll);
		deleteAll.addActionListener(this);
		controls.add(export);
		export.addActionListener(this);
		
		doListeners();
		confirm.addActionListener(this);
		con.gridx = 0;
		con.gridy = 2;
		con.weighty = 0.2;
		con.gridwidth = 4;
		main.add(addTable, con);
    }
	
	//Redfines table listeners
	public void doListeners() {
		for (int i = 0; i < data.getNextSamplePosition(); i++) {
        	delete.get(i).addActionListener(this);
        	edit.get(i).addActionListener(this);
        }
	}
	
	
	public void actionPerformed(ActionEvent ae) {
		for(int i = 0; i < data.getNextSamplePosition(); i++) { //loops through players
			if (ae.getSource() == edit.get(i)) {
				if (edit.get(i).getText().equals("Edit")) {
		        	viewTable.readyEdit(i);
				} else if (edit.get(i).getText().equals("Confirm")) {
					viewTable.makeEdit(i);
				}
	        }
	        if (ae.getSource() == delete.get(i)) {
	        	data.deleteByName(viewTable.table.getModel().getValueAt(i, 0)+"");
	        	viewTable.initializeUI();
	        	doListeners();
	        }
	        
		}
		if (ae.getSource() == confirm) {
			addTable.add();
			viewTable.initializeUI();
			doListeners();
        }
		if (ae.getSource() == batch) {
			JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
	        int result = fileChooser.showOpenDialog(null);
	        if (result == JFileChooser.APPROVE_OPTION) {
	            File selectedFile = fileChooser.getSelectedFile();
	            
	            String endResult = data.batchRead(selectedFile);
	            if (endResult.equals("1"))
	            	viewTable.initializeUI();
	            else if (endResult.equals("0"))
	            	JOptionPane.showMessageDialog(null, "Error trying to read selected file", "Invalid", 0);
	            else
	            	JOptionPane.showMessageDialog(null, "Duplicated name, "+endResult+" already exists.", "Invalid", 0);
	        }
		}
		if (ae.getSource() == deleteAll) {
			data = new Database();
			data.writeSamplesListToFile();
			viewTable.initializeUI();
		}
		if (ae.getSource() == export) {
			data.export(new File(JOptionPane.showInputDialog("enter filename")+".txt"));
			/*
			JFileChooser fileChooser = new JFileChooser();
	        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
	        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	        int result = fileChooser.showSaveDialog(null);
	        System.out.println(fileChooser.getCurrentDirectory()+"\\"+JOptionPane.showInputDialog("enter filename")+".txt");
	        File selectedFile = new File(fileChooser.getCurrentDirectory()+"\\"+JOptionPane.showInputDialog("enter filename")+".txt");
	        if (result == JFileChooser.APPROVE_OPTION) {
	            //File selectedFile = fileChooser.getSelectedFile();
	            
	            data.export(selectedFile);
	        }*/
		}
	}
}