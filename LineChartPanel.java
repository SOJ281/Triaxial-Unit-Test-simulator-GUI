import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.BorderFactory;
import javax.swing.JFrame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class LineChartPanel extends JPanel implements ActionListener {
	//Does what it says, for displaying data
	public class ScrollableJTable extends JScrollPane {
		JTable table = new JTable(200,2);
		//JScrollPane pane;
		Object[][] rowData;
	    public ScrollableJTable() {
	    	super();
	    	//setSize(50, 50);
	    	//setMaximumSize(new Dimension(100,100));
	    	//table.setSize(100,100);
	    	getViewport().add(table);
	    }
	    
	    private void initializeUI() {
	    	getViewport().remove(table);
	        //setLayout(new BorderLayout());
	        //setPreferredSize(new Dimension(500, 250));
	        //table = new JTable(200, 2);
	        rowData = new Object[systData[0].length][2];
	        for (int i = 0; i < systData[0].length; i++) {
	        	rowData[i][0] = new Double(Math.round(systData[0][i] * 1000.0) / 1000.0);
	        	rowData[i][1] = new Double(Math.round(systData[1][i] * 1000.0) / 1000.0);
	        }

	        table = new JTable(rowData, columnNames) {
	            @Override
	            public boolean isCellEditable(int row, int column) {
	               //all cells false
	               return false;
	            }
	        };

	        // Turn off JTable's auto resize so that JScrollPane will show a horizontal
	        // scroll bar.
	        //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	        
	        //pane = new JScrollPane(table);
	        //add(pane, BorderLayout.CENTER);
	        //table.revalidate();
	        //table.repaint();
	        revalidate();
	        repaint();
	        getViewport().add(table);
	    }
	}
	    
	Object[] columnNames = new Object[2];
    JFreeChart chart;
    ChartPanel chartPanel;
    XYPlot plot;
    XYDataset dataset;
    XYSeries series;
    JButton saveData = new JButton("Export Data");
    JButton saveSubData = new JButton("Export subset");
    JButton savePNG = new JButton("Save PNG");
    JButton toggleView = new JButton("Switch View");
    WriteDataToExcel st = new WriteDataToExcel();
    ScrollableJTable tableView = new ScrollableJTable();
    double[][] systData;
    double[][] fullData;
    String thisTitle = "not";
    String thisXTitle;
    String thisYTitle;
    
    JPanel upper = new JPanel();
    JPanel lower = new JPanel();
    
    JPanel sContainer = new JPanel();
    JSlider slider = new JSlider(100, 7500, 7500);
    ValidatedField iterField = new ValidatedField();

    
    public LineChartPanel() {
    	upper.setLayout(new GridBagLayout());
    	setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();
        chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        chartPanel.setBackground(Color.white);
        
        
        con.fill = GridBagConstraints.BOTH;
        con.weightx = 1;
        con.weighty = 1;
        con.gridx = 0;
        con.gridy = 0;
        con.gridwidth = 3;
        add(chartPanel, con);
        add(upper, con);
        
        con.gridwidth = 1;
        con.gridx = 0;
        upper.add(new JLabel(), con);
        con.fill = GridBagConstraints.BOTH;
        con.weightx = 1;
        con.weighty = 1;
        con.gridx = 1;
        con.ipadx = 100;      //make this component tall
        tableView.setVisible(false);
        upper.add(tableView, con);
        con.gridx = 2;
        con.ipadx = 0;      //make this component tall
        con.fill = GridBagConstraints.BOTH;
        upper.add(new JLabel(), con);
        upper.setBorder(BorderFactory.createMatteBorder(
                1, 5, 1, 1, Color.black));
        
        
        lower.setVisible(false);
        lower.setBorder(BorderFactory.createMatteBorder(
                1, 0, 0, 0, Color.black));
        //lower.setLayout(new GridBagLayout());
        
        con.gridwidth = 1;
        con.fill = GridBagConstraints.BOTH;
        con.weightx = 0.5;
        con.weighty = 0.1;
        con.gridx = 0;
        con.gridy = 0;
        saveData.addActionListener(this); //adds action listener
        lower.add(saveData, con);
        
        con.fill = GridBagConstraints.BOTH;
        con.weightx = 0.5;
        con.weighty = 0.1;
        con.gridx = 1;
        con.gridy = 0;
        savePNG.addActionListener(this); //adds action listener
        lower.add(savePNG, con);
        
        con.gridx = 2;
        con.gridy = 0;
        toggleView.addActionListener(this); //adds action listener
        lower.add(toggleView, con);
        
        con.gridx = 3;
        con.gridy = 0;
        
        sContainer.setBorder(BorderFactory.createLineBorder(Color.black));
        lower.add(sContainer);
        //BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        sContainer.add(new JLabel("Iterations"));
        /*
        slider.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
               Point p = e.getPoint();
               double percent = p.x / ((double) getWidth());
               int range = slider.getMaximum() - slider.getMinimum();
               double newVal = range * percent;
               int result = (int)(slider.getMinimum() + newVal);
               slider.setValue(result);
            }
         });*/
        //adjustment.addActionListener(this); //adds action listener
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent event) {
                updateUI();
                iterField.setText(slider.getValue()+"");
              }
            });
        //slider.setText("ef");
        sContainer.add(slider);
        
        con.gridx = 4;
        iterField.setText("7500");
        iterField.setColumns(3);
        sContainer.add(iterField);
        iterField.addKeyListener(new KeyListener() {
        	public void keyTyped(KeyEvent kevt) {}
	        public void keyReleased(KeyEvent kevt) {}
	    	public void keyPressed(KeyEvent kevt) { //if a key is pressed
	            if (kevt.getKeyCode() == KeyEvent.VK_ENTER) { //if key pressed
	    			if (iterField.isFocusOwner()) { //if focus owner
	    				try {
	    					slider.setValue(Integer.parseInt(iterField.getText()));
	    				} catch (Exception e) {
	    					iterField.errorMessage("Invalid input."); //displays error message with comment
	    				}
	    			}
	            }
	    	}
        });
        
        saveSubData.addActionListener(this); //adds action listener
        lower.add(saveSubData, con);
        
        
        con.gridx = 0;
        con.gridy = 1;
        con.gridwidth = 3;
        con.weighty = 0.01;
        add(lower, con);
    }

    //Takes data, labels to populate a new graph with
    public void initUI(double[][] dataSet, String title, String Xtitle, String Ytitle) {
    	thisTitle = title;
    	fullData = dataSet;
    	thisXTitle = Xtitle;
    	thisYTitle = Ytitle;
    	lower.setVisible(true);
    	updateUI();
    }
    
    //Updates the graph data if something changes
    public void updateUI() {
    	if (thisTitle != null) {
	    	systData = filteredDataset(fullData);
	        dataset = createDataset(systData, thisTitle);
	        chart = createChart(dataset, thisTitle, thisXTitle, thisYTitle);
	        try {chartPanel.setChart(chart);} catch(Exception e) {}
	        try {chartPanel.repaint();} catch(Exception e) {}
	        try {tableView.initializeUI();} catch(Exception e) {e.printStackTrace();}
	        //chartPanel.setMouseWheelEnabled(true);
    	}
    }
    
    //Returns dataset based on the number of iterations
    public double[][] filteredDataset(double[][] dataSet) {
    	double[][] newDataSet = new double[2][(int)slider.getValue()];
    	for (int i = 0; i < newDataSet[0].length; i++) {
    		newDataSet[0][i] = dataSet[0][i];
    		newDataSet[1][i] = dataSet[1][i];
    	}
    	return newDataSet;
    }

    //Converts dataset into an XYDataset type
    public XYDataset createDataset(double[][] dataSet, String name) {
        series = new XYSeries(name);
        for (int i = 0; i < dataSet[0].length; i++)
            series.add(dataSet[0][i], dataSet[1][i]);
            
        XYSeriesCollection dabaset = new XYSeriesCollection();
        dabaset.addSeries(series);

        return dabaset;
    }

    public JFreeChart createChart(XYDataset dataset, String title, String Xtitle, String Ytitle) {
        chart = ChartFactory.createXYLineChart(
                title,
                Xtitle,
                Ytitle,
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        columnNames[0] = Xtitle;
        columnNames[1] = Ytitle;
        

        plot = chart.getXYPlot();
        
        //plot.setDomainPannable(true);
        //plot.setRangePannable(true);
        
        plot.getDomainAxis().setLabelPaint(Color.black);
        plot.getDomainAxis().setAxisLinePaint(Color.black);
        plot.getDomainAxis().setTickLabelPaint(Color.black);
        plot.getDomainAxis().setTickMarkPaint(Color.black);
        //plot.setDomainTickBandPaint(Color.black);
        plot.getRangeAxis().setLabelPaint(Color.black);
        plot.getRangeAxis().setAxisLinePaint(Color.black);
        plot.getRangeAxis().setTickLabelPaint(Color.black);
        plot.getRangeAxis().setTickMarkPaint(Color.black);
        //plot.setRangeTickBandPaint(Color.black);
        
        //chart.getPlot().getDomainAxis().setLabelPaint(Color.black);

        /*
        LineAndShapeRenderer renderer = (LineAndShapeRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setAutoPopulateSeriesStroke(false);
        renderer.setDefaultStroke(new BasicStroke(3.0f));
        
        XYItemRenderer renderer = plot.getRenderer();
        renderer.setDefaultStroke(new BasicStroke(2.0f));
        ((AbstractRenderer) renderer).setAutoPopulateSeriesStroke(false);
        */
        //XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        XYItemRenderer renderer = plot.getRenderer(0);
        renderer.setSeriesPaint(0, Color.red);
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        //renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesShape(0, new Rectangle(40, 40));
        //renderer.setSeriesOutlineStroke(0, new BasicStroke(2.0f));

        plot.setRenderer(renderer);
        plot.setBackgroundPaint(new GradientPaint(0, 0, Color.white, 100, 100, Color.green.brighter().brighter()));
        //plot.setBackgroundPaint(Color.white);

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.BLACK);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.BLACK);

        chart.getLegend().setFrame(BlockBorder.NONE);

        chart.setTitle(new TextTitle(title,new Font("Serif", java.awt.Font.BOLD, 18)));
        chart.getXYPlot().setDataset(chart.getXYPlot().getDataset());
        //chart.repaint();

        return chart;
    }
    
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == saveData) {
            try {
                st.createTable(fullData, thisTitle, thisTitle);
            } catch(Exception e) { 
                e.printStackTrace();
            }
        }
        if (ae.getSource() == saveSubData) {
            try {
                st.createTable(systData, thisTitle, thisTitle);
            } catch(Exception e) { 
                e.printStackTrace();
            }
        }
        if (ae.getSource() == savePNG) {
            try {
                chartPanel.doSaveAs();
            } catch(Exception e) {}
        }
        
        if (ae.getSource() == toggleView) {
            try {
                if (tableView.isVisible()) {
                	tableView.setVisible(false);
                	chartPanel.setVisible(true);
                } else {
                	tableView.setVisible(true);
                	chartPanel.setVisible(false);
                }
            } catch(Exception e) {}
        }
        
    }
}