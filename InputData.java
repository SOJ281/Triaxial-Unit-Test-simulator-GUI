//import javax.swing.BorderFactory;
import javax.swing.JFrame;
/*import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
*/
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;  
//import java.util.*;

public class InputData extends JFrame implements ActionListener {
    public class inLabel extends JLabel{
        inLabel(String explanation, String title) {
            super(title);
            setToolTipText(explanation); //displays text when the mouse hovers over it
        }
    }
    ValidatedField name = new ValidatedField();
    
    ValidatedField cpi = new ValidatedField(0.0, 600.0);
    ValidatedField p0i = new ValidatedField(0.0, 600.0);
    ValidatedField Mi = new ValidatedField(0.0, 100.0);
    ValidatedField li = new ValidatedField(0.0, 100.0);
    ValidatedField ki = new ValidatedField(0.0, 100.0);
    ValidatedField Ni = new ValidatedField(0.0, 100.0);
    ValidatedField nui = new ValidatedField(0.0, 100.0);
 
    public JButton submit = new JButton("Submit");

    public InputData(String sample) {
    	setLocationRelativeTo(null);
    	setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
    	setTitle("Input"); //sets the title for the pane
        setSize(300, 400); //sets the JPanel size
        
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.2;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        add(new inLabel("Name", "Name"), c);
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.8;
        c.gridx = 1;
        add(name, c);
        
        setupTable(c);
        
    }
    
    public InputData() {
    	setLocationRelativeTo(null);
    	setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
    	setTitle("Input"); //sets the title for the pane
        setSize(300, 400); //sets the JPanel size
        setupTable(c);
    }
    
    public void setupTable(GridBagConstraints c) {
        
        
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.2;
        c.weighty = 0.5;
        c.fill = GridBagConstraints.BOTH;
        c.gridy = 1;
        add(new inLabel("Mi", "Critical Friction Angle M"), c);
        c.gridy = 2;
        add(new inLabel("li", "Lamda (\u03bb)"), c);
        c.gridy = 3;
        add(new inLabel("ki", "Kappa (\u03ba)"), c);
        c.gridy = 4;
        add(new inLabel("Ni", "N"), c);
        c.gridy = 5;
        add(new inLabel("nui", "poissons ratio  (\u03bd)"), c);
        
        c.weightx = 0.8;
        c.gridx = 1;
        c.gridy = 1;
        add(Mi, c);
        c.gridy = 2;
        add(li, c);
        c.gridy = 3;
        add(ki, c);
        c.gridy = 4;
        add(Ni, c);
        c.gridy = 5;
        add(nui, c);
        
        c.fill = GridBagConstraints.PAGE_END;
        c.gridy = 6;
        submit.addActionListener(this); //adds action listener
        add(submit, c);
    }
    /*
    public TriaxialTest getTestData() {
        return new TriaxialTest(cpi.getFig(), p0i.getFig(), Mi.getFig(), li.getFig(), ki.getFig(), Ni.getFig(), nui.getFig(), Integer.parseInt(a.getText()), Integer.parseInt(iter.getText()), strsteps.getFig());
    }*/
    
    public TriaxialTest getSampleSave() {
        return new TriaxialTest(name.validatePresence(), Mi.getFig(), li.getFig(), ki.getFig(), Ni.getFig(), nui.getFig());
    }
    
    public TriaxialTest getSample() {
        return new TriaxialTest(Mi.getFig(), li.getFig(), ki.getFig(), Ni.getFig(), nui.getFig());
    }
    
    
    public void actionPerformed(ActionEvent ae) {
        /*
        if (ae.getSource() == subSelect) {
            String[] data = map.get(subSelect.getSelectedItem());
            cpi.setText(data[0]);
            p0i.setText(data[1]);
            Mi.setText(data[2]);
            li.setText(data[3]);
            ki.setText(data[4]);
            Ni.setText(data[5]);
            nui.setText(data[6]);
            a.setText(data[7]);
            iter.setText(data[8]);
            strsteps.setText(data[9]);

        }*/
	}

}