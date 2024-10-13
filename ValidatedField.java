import javax.swing.*;
//import java.awt.*;
//import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.*;
import java.awt.Color;
import javax.swing.BorderFactory;
//import javax.swing.border.Border;
//import java.time.LocalDate;// import the LocalDate class
//import javafx.scene.*;

public class ValidatedField extends JTextField implements KeyListener, FocusListener  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2880713544232370126L;
	Color badmintonBlue = new Color(-16750678); //standard blue shade used throughout system
    double lower;
    double higher;
    
    public ValidatedField(double l, double h) {
        super();
        lower = l;
        higher = h;
    }
    public ValidatedField() {
        super();
    }
    
    
    
    //validates the amount entered in the transactions field
	public Double getFig(){
		addFocusListener(this); //adds focuslistener
		validatePresence().equals(null); //finds out if # or nothing was entered
		try {
			Double tempVal = Double.parseDouble(getText()); //attempts to parse value entered
			return tempVal; //returns the results of the final checks
		} catch (Exception e) {
			errorMessage("A number wasn't entered!"); //displays error message with comment
			return null; //returns null value
		}
	}
	
	/*
	//validates the amount entered in the transactions field
	public int getInt() {
		addFocusListener(this);
		if (getText().isEmpty()) { 
			errorMessage("Nothing was entered");
			return null;
		}
		try {
			return Integer.parseInt(getText()); 
		} catch (Exception e) {
			errorMessage("Invalid entry!"); //displays error message with comment
			return null; //returns null value
		}
	}*/

	
	//presence and hashtag check for all data entered into the system
	public String validatePresence() {
		addFocusListener(this); //adds focuslistener
		if (getText().isEmpty()) { //validation in the event of nothing entered
			errorMessage("Nothing was entered"); //displays error message with comment
			return null; //returns null value
		}
		return getText(); //returns valid text
	}
	
	//builds conditions for error message
	public void errorMessage(String messageText){
		setBorder(BorderFactory.createLineBorder(Color.RED, 3)); //add a red border to fields
		setVisible(true);	//sets as visible
		
		UIManager.put("Button.background", Color.RED);
		UIManager.put("Button.foreground", Color.BLACK);
		JOptionPane.showMessageDialog(null, messageText, "Invalid", 0);
		UIManager.put("Button.background", badmintonBlue);
		UIManager.put("Button.foreground", Color.white);
	}

	public void loseInvalid() {
		//resets field appearance
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		setVisible(true);
	}
	
	public void keyReleased(KeyEvent kevt) { //if a key is released
	}
	public void keyPressed(KeyEvent kevt) { //if a key is pressed
	}
	public void keyTyped(KeyEvent kevt) { //if a specific key is pressed
	}
	
	public void focusGained(FocusEvent fe) {
		loseInvalid();
	}
	public void focusLost(FocusEvent fe) {
	}
}