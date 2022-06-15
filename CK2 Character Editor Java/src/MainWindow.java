import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSpinner;

public class MainWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
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
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JButton initializeButton = new JButton("Initialize");
		springLayout.putConstraint(SpringLayout.NORTH, initializeButton, 10, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, initializeButton, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(initializeButton);
		
		JButton loadSaveButton = new JButton("Load Save");
		loadSaveButton.setEnabled(false);
		springLayout.putConstraint(SpringLayout.NORTH, loadSaveButton, 0, SpringLayout.NORTH, initializeButton);
		springLayout.putConstraint(SpringLayout.WEST, loadSaveButton, 6, SpringLayout.EAST, initializeButton);
		frame.getContentPane().add(loadSaveButton);
		
		JLabel lblDiplomacy = new JLabel("Diplomacy");
		springLayout.putConstraint(SpringLayout.NORTH, lblDiplomacy, 6, SpringLayout.SOUTH, initializeButton);
		springLayout.putConstraint(SpringLayout.EAST, lblDiplomacy, -10, SpringLayout.EAST, initializeButton);
		frame.getContentPane().add(lblDiplomacy);
		
		JLabel lblMartial = new JLabel("Martial");
		springLayout.putConstraint(SpringLayout.NORTH, lblMartial, 6, SpringLayout.SOUTH, loadSaveButton);
		springLayout.putConstraint(SpringLayout.WEST, lblMartial, 10, SpringLayout.WEST, loadSaveButton);
		frame.getContentPane().add(lblMartial);
		
		JLabel lblStewardship = new JLabel("Stewardship");
		springLayout.putConstraint(SpringLayout.NORTH, lblStewardship, 6, SpringLayout.SOUTH, loadSaveButton);
		springLayout.putConstraint(SpringLayout.WEST, lblStewardship, 6, SpringLayout.EAST, lblMartial);
		frame.getContentPane().add(lblStewardship);
		
		JLabel lblIntrigue = new JLabel("Intrigue");
		springLayout.putConstraint(SpringLayout.NORTH, lblIntrigue, 0, SpringLayout.NORTH, lblDiplomacy);
		springLayout.putConstraint(SpringLayout.WEST, lblIntrigue, 6, SpringLayout.EAST, lblStewardship);
		frame.getContentPane().add(lblIntrigue);
		
		JLabel lblLearning = new JLabel("Learning");
		springLayout.putConstraint(SpringLayout.NORTH, lblLearning, 0, SpringLayout.NORTH, lblDiplomacy);
		springLayout.putConstraint(SpringLayout.WEST, lblLearning, 6, SpringLayout.EAST, lblIntrigue);
		frame.getContentPane().add(lblLearning);
		
		JSpinner spinnerDip = new JSpinner();
		springLayout.putConstraint(SpringLayout.NORTH, spinnerDip, 6, SpringLayout.SOUTH, lblDiplomacy);
		springLayout.putConstraint(SpringLayout.WEST, spinnerDip, 0, SpringLayout.WEST, initializeButton);
		frame.getContentPane().add(spinnerDip);
		
		JSpinner spinnerMar = new JSpinner();
		springLayout.putConstraint(SpringLayout.WEST, spinnerMar, 6, SpringLayout.EAST, spinnerDip);
		springLayout.putConstraint(SpringLayout.SOUTH, spinnerMar, 0, SpringLayout.SOUTH, spinnerDip);
		frame.getContentPane().add(spinnerMar);
		
		JSpinner spinnerSte = new JSpinner();
		springLayout.putConstraint(SpringLayout.WEST, spinnerSte, 6, SpringLayout.EAST, spinnerMar);
		springLayout.putConstraint(SpringLayout.SOUTH, spinnerSte, 0, SpringLayout.SOUTH, spinnerDip);
		frame.getContentPane().add(spinnerSte);
		
		JSpinner spinnerInt = new JSpinner();
		springLayout.putConstraint(SpringLayout.WEST, spinnerInt, 6, SpringLayout.EAST, spinnerSte);
		springLayout.putConstraint(SpringLayout.SOUTH, spinnerInt, 0, SpringLayout.SOUTH, spinnerDip);
		frame.getContentPane().add(spinnerInt);
		
		JSpinner spinnerLea = new JSpinner();
		springLayout.putConstraint(SpringLayout.NORTH, spinnerLea, 6, SpringLayout.SOUTH, lblStewardship);
		springLayout.putConstraint(SpringLayout.WEST, spinnerLea, 6, SpringLayout.EAST, spinnerInt);
		frame.getContentPane().add(spinnerLea);
		
		JLabel showDiplomacy = new JLabel("0");
		springLayout.putConstraint(SpringLayout.NORTH, showDiplomacy, 6, SpringLayout.SOUTH, spinnerDip);
		springLayout.putConstraint(SpringLayout.WEST, showDiplomacy, 10, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(showDiplomacy);
		
		JLabel showMartial = new JLabel("0");
		springLayout.putConstraint(SpringLayout.NORTH, showMartial, 6, SpringLayout.SOUTH, spinnerDip);
		springLayout.putConstraint(SpringLayout.WEST, showMartial, 0, SpringLayout.WEST, lblDiplomacy);
		springLayout.putConstraint(SpringLayout.EAST, showMartial, 6, SpringLayout.WEST, lblDiplomacy);
		frame.getContentPane().add(showMartial);
		
		JLabel showStewardship = new JLabel("0");
		springLayout.putConstraint(SpringLayout.WEST, showStewardship, 0, SpringLayout.WEST, spinnerMar);
		springLayout.putConstraint(SpringLayout.SOUTH, showStewardship, 0, SpringLayout.SOUTH, showDiplomacy);
		frame.getContentPane().add(showStewardship);
		
		JLabel showIntrigue = new JLabel("0");
		springLayout.putConstraint(SpringLayout.WEST, showIntrigue, 6, SpringLayout.EAST, showStewardship);
		springLayout.putConstraint(SpringLayout.SOUTH, showIntrigue, 0, SpringLayout.SOUTH, showDiplomacy);
		frame.getContentPane().add(showIntrigue);
		
		JLabel showLearning = new JLabel("0");
		springLayout.putConstraint(SpringLayout.SOUTH, showLearning, 0, SpringLayout.SOUTH, showDiplomacy);
		springLayout.putConstraint(SpringLayout.EAST, showLearning, 0, SpringLayout.EAST, lblMartial);
		frame.getContentPane().add(showLearning);
	}
}
