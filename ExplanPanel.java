import java.awt.*;
import javax.swing.*;

//Panel for explaining basic facts about the triaxial
//unit test
public class ExplanPanel extends JScrollPane /*implements ActionListener*/ {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6375100757628905191L;
	ImageIcon img = new ImageIcon(getClass().getClassLoader().getResource("images/rsz_anastri.png"));
	JLabel title1 = new JLabel("Triaxial Test Description");
	JTextArea desc = new JTextArea("The Monotonic Triaxial System in the laboratory is a load "
			+ "frame-based triaxial testing system. The system is composed of a load frame, a "
			+ "triaxial cell, water pressure controllers. The system can be configured to test "
			+ "specimens both in drained and undrained conditions and test specimens of different"
			+ " sizes. Different levels of back pressure can be adopted during the saturation stage."
			+ " Then a pressure differential can be implemented to simulate a consolidation stage and"
			+ " then sheared monotonically by increasing axial force under drained or undrained"
			+ " conditions."); 
	JPanel main = new JPanel();
	
	public ExplanPanel() {
		super();
		getVerticalScrollBar().setUnitIncrement(16);
		getViewport().add(main);
		setLayout(new ScrollPaneLayout());
		main.setBackground(Color.lightGray);
		main.setLayout(new BoxLayout(main, 1));
		//add(desc1);
		JLabel imgL = new JLabel(img);
		main.add(imgL);
		imgL.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		title1.setFont(new Font("Segoe UI", Font.BOLD, 18));
		main.add(title1);
		title1.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//main.add(desc1);
		//desc1.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		main.add(desc);
		desc.setMaximumSize(new Dimension(800, 400));
		desc.setLineWrap(true);
		desc.setEditable(false);
		desc.setWrapStyleWord(true);
		desc.setAlignmentX(Component.CENTER_ALIGNMENT);
	}

}