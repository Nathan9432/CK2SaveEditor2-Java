import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

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
	}
}
