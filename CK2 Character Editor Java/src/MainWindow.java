import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.UnsupportedOperationException;

public class MainWindow {

	private JFrame frame;
	static Data database;
	static Status status;
	static String date;
	static int[] modifierAttributes;
	static int[] artifactAttributes;
	static int[] traitAttributes;
	static String charId;
	private JSpinner spnDip;
	private JSpinner spnMar;
	private JSpinner spnSte;
	private JSpinner spnInt;
	private JSpinner spnLea;
	private JLabel lblDiplomacyShow;
	private JLabel lblMartialShow;
	private JLabel lblStewardshipShow;
	private JLabel lblIntrigueShow;
	private JLabel lblLearningShow;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		database = new Data();
		status = Status.START;
		modifierAttributes = new int[5];
		artifactAttributes = new int[5];
		traitAttributes = new int[5];
		
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
	 * Updates the attribute labels under each spinner; may be called for any update 
	 */
	private void updateViewAttributes() {
		int dip = (Integer) spnDip.getValue();
		lblDiplomacyShow.setText(Integer.toString(dip + modifierAttributes[0] + artifactAttributes[0]));
		int mar = (Integer) spnMar.getValue();
		lblMartialShow.setText(Integer.toString(mar + modifierAttributes[1] + artifactAttributes[1]));
		int ste = (Integer) spnSte.getValue();
		lblStewardshipShow.setText(Integer.toString(ste + modifierAttributes[2] + artifactAttributes[2]));
		int inr = (Integer) spnInt.getValue();
		lblIntrigueShow.setText(Integer.toString(inr + modifierAttributes[3] + artifactAttributes[3]));
		int lea = (Integer) spnLea.getValue();
		lblLearningShow.setText(Integer.toString(lea + modifierAttributes[4] + artifactAttributes[4]));
	}
	
	private void artifactSelectorUpdate() {
		throw new UnsupportedOperationException();
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
		
		initializeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Prompt user for CK2 files location to load, eg traits, into data; activate loadSaveButton when done
				
				// TODO: Check if already in SAVE_LOADED, warn user about lost save info
				
				// Prompt user for file location
				String name = JOptionPane.showInputDialog(null,"Please enter the location of the CK2 base game files.\n(eg, D:\\SteamLibrary\\steamapps\\common\\Crusader Kings II)");
				
				// TODO: Check that folder exists and won't throw some error; alert user if it does
				
				if (name.equals("")) {
					JOptionPane.showInternalMessageDialog(null, "Name of error", "general error", 0);
				}
				
				// Fire off database.setup() with appropriate information
				database.vanillaSetup(name);
				
				// TODO: Implement loading mods
				// TODO: Sanity check- tell database to clear save information, just to be safe
				
				// Enable next button
				status = Status.DATA_LOADED;
				loadSaveButton.setEnabled(true);
			}
		});
		
		JPanel panel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panel, 6, SpringLayout.SOUTH, initializeButton);
		springLayout.putConstraint(SpringLayout.WEST, panel, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, panel, 64, SpringLayout.SOUTH, initializeButton);
		springLayout.putConstraint(SpringLayout.EAST, panel, 360, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {65, 65, 65, 65, 65, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		lblDiplomacyShow = new JLabel("Diplomacy");
		GridBagConstraints gbc_lblDiplomacy = new GridBagConstraints();
		gbc_lblDiplomacy.insets = new Insets(0, 0, 5, 5);
		gbc_lblDiplomacy.gridx = 0;
		gbc_lblDiplomacy.gridy = 0;
		panel.add(lblDiplomacyShow, gbc_lblDiplomacy);
		springLayout.putConstraint(SpringLayout.NORTH, lblDiplomacyShow, 6, SpringLayout.SOUTH, initializeButton);
		springLayout.putConstraint(SpringLayout.EAST, lblDiplomacyShow, -10, SpringLayout.EAST, initializeButton);
		
		lblMartialShow = new JLabel("Martial");
		GridBagConstraints gbc_lblMartial = new GridBagConstraints();
		gbc_lblMartial.insets = new Insets(0, 0, 5, 5);
		gbc_lblMartial.gridx = 1;
		gbc_lblMartial.gridy = 0;
		panel.add(lblMartialShow, gbc_lblMartial);
		springLayout.putConstraint(SpringLayout.NORTH, lblMartialShow, 6, SpringLayout.SOUTH, loadSaveButton);
		springLayout.putConstraint(SpringLayout.WEST, lblMartialShow, 10, SpringLayout.WEST, loadSaveButton);
		
		lblStewardshipShow = new JLabel("Stewardship");
		GridBagConstraints gbc_lblStewardship = new GridBagConstraints();
		gbc_lblStewardship.insets = new Insets(0, 0, 5, 5);
		gbc_lblStewardship.gridx = 2;
		gbc_lblStewardship.gridy = 0;
		panel.add(lblStewardshipShow, gbc_lblStewardship);
		springLayout.putConstraint(SpringLayout.NORTH, lblStewardshipShow, 6, SpringLayout.SOUTH, loadSaveButton);
		springLayout.putConstraint(SpringLayout.WEST, lblStewardshipShow, 135, SpringLayout.WEST, frame.getContentPane());
		
		lblIntrigueShow = new JLabel("Intrigue");
		GridBagConstraints gbc_lblIntrigue = new GridBagConstraints();
		gbc_lblIntrigue.insets = new Insets(0, 0, 5, 5);
		gbc_lblIntrigue.gridx = 3;
		gbc_lblIntrigue.gridy = 0;
		panel.add(lblIntrigueShow, gbc_lblIntrigue);
		springLayout.putConstraint(SpringLayout.NORTH, lblIntrigueShow, 39, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblIntrigueShow, 200, SpringLayout.WEST, frame.getContentPane());
		
		lblLearningShow = new JLabel("Learning");
		GridBagConstraints gbc_lblLearning = new GridBagConstraints();
		gbc_lblLearning.insets = new Insets(0, 0, 5, 0);
		gbc_lblLearning.gridx = 4;
		gbc_lblLearning.gridy = 0;
		panel.add(lblLearningShow, gbc_lblLearning);
		springLayout.putConstraint(SpringLayout.NORTH, lblLearningShow, 39, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblLearningShow, 244, SpringLayout.WEST, frame.getContentPane());
		
		spnDip = new JSpinner();
		spnDip.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateViewAttributes();
			}
		});
		spnDip.setEnabled(false);
		GridBagConstraints gbc_spnDip = new GridBagConstraints();
		gbc_spnDip.fill = GridBagConstraints.HORIZONTAL;
		gbc_spnDip.insets = new Insets(0, 0, 5, 5);
		gbc_spnDip.gridx = 0;
		gbc_spnDip.gridy = 1;
		panel.add(spnDip, gbc_spnDip);
		springLayout.putConstraint(SpringLayout.WEST, spnDip, 0, SpringLayout.WEST, initializeButton);
		springLayout.putConstraint(SpringLayout.NORTH, spnDip, 6, SpringLayout.SOUTH, lblDiplomacyShow);
		
		spnMar = new JSpinner();
		spnMar.setEnabled(false);
		spnMar.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateViewAttributes();
			}
		});
		GridBagConstraints gbc_spnMar = new GridBagConstraints();
		gbc_spnMar.fill = GridBagConstraints.HORIZONTAL;
		gbc_spnMar.insets = new Insets(0, 0, 5, 5);
		gbc_spnMar.gridx = 1;
		gbc_spnMar.gridy = 1;
		panel.add(spnMar, gbc_spnMar);
		springLayout.putConstraint(SpringLayout.WEST, spnMar, 46, SpringLayout.WEST, frame.getContentPane());
		
		spnSte = new JSpinner();
		spnSte.setEnabled(false);
		spnSte.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateViewAttributes();
			}
		});
		GridBagConstraints gbc_spnSte = new GridBagConstraints();
		gbc_spnSte.fill = GridBagConstraints.HORIZONTAL;
		gbc_spnSte.insets = new Insets(0, 0, 5, 5);
		gbc_spnSte.gridx = 2;
		gbc_spnSte.gridy = 1;
		panel.add(spnSte, gbc_spnSte);
		springLayout.putConstraint(SpringLayout.WEST, spnSte, 82, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, spnSte, 6, SpringLayout.SOUTH, lblMartialShow);
		
		spnInt = new JSpinner();
		spnInt.setEnabled(false);
		spnInt.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateViewAttributes();
			}
		});
		GridBagConstraints gbc_spnInt = new GridBagConstraints();
		gbc_spnInt.fill = GridBagConstraints.HORIZONTAL;
		gbc_spnInt.insets = new Insets(0, 0, 5, 5);
		gbc_spnInt.gridx = 3;
		gbc_spnInt.gridy = 1;
		panel.add(spnInt, gbc_spnInt);
		springLayout.putConstraint(SpringLayout.WEST, spnInt, 118, SpringLayout.WEST, frame.getContentPane());
		
		spnLea = new JSpinner();
		spnLea.setEnabled(false);
		spnLea.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateViewAttributes();
			}
		});
		GridBagConstraints gbc_spnLea = new GridBagConstraints();
		gbc_spnLea.fill = GridBagConstraints.HORIZONTAL;
		gbc_spnLea.insets = new Insets(0, 0, 5, 0);
		gbc_spnLea.gridx = 4;
		gbc_spnLea.gridy = 1;
		panel.add(spnLea, gbc_spnLea);
		springLayout.putConstraint(SpringLayout.WEST, spnLea, 154, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.NORTH, spnLea, 6, SpringLayout.SOUTH, lblStewardshipShow);
		
		lblDiplomacyShow = new JLabel("-0-");
		GridBagConstraints gbc_lblDiplomacyShow = new GridBagConstraints();
		gbc_lblDiplomacyShow.insets = new Insets(0, 0, 0, 5);
		gbc_lblDiplomacyShow.gridx = 0;
		gbc_lblDiplomacyShow.gridy = 2;
		panel.add(lblDiplomacyShow, gbc_lblDiplomacyShow);
		springLayout.putConstraint(SpringLayout.NORTH, lblDiplomacyShow, 52, SpringLayout.SOUTH, initializeButton);
		springLayout.putConstraint(SpringLayout.WEST, lblDiplomacyShow, 10, SpringLayout.WEST, frame.getContentPane());
		
		lblMartialShow = new JLabel("-0-");
		GridBagConstraints gbc_lblMartialShow = new GridBagConstraints();
		gbc_lblMartialShow.insets = new Insets(0, 0, 0, 5);
		gbc_lblMartialShow.gridx = 1;
		gbc_lblMartialShow.gridy = 2;
		panel.add(lblMartialShow, gbc_lblMartialShow);
		
		lblStewardshipShow = new JLabel("-0-");
		GridBagConstraints gbc_lblStewardshipShow = new GridBagConstraints();
		gbc_lblStewardshipShow.insets = new Insets(0, 0, 0, 5);
		gbc_lblStewardshipShow.gridx = 2;
		gbc_lblStewardshipShow.gridy = 2;
		panel.add(lblStewardshipShow, gbc_lblStewardshipShow);
		springLayout.putConstraint(SpringLayout.WEST, lblStewardshipShow, 46, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblStewardshipShow, -35, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, spnMar, -6, SpringLayout.NORTH, lblStewardshipShow);
		
		lblIntrigueShow = new JLabel("-0-");
		GridBagConstraints gbc_lblIntrigueShow = new GridBagConstraints();
		gbc_lblIntrigueShow.insets = new Insets(0, 0, 0, 5);
		gbc_lblIntrigueShow.gridx = 3;
		gbc_lblIntrigueShow.gridy = 2;
		panel.add(lblIntrigueShow, gbc_lblIntrigueShow);
		springLayout.putConstraint(SpringLayout.WEST, lblIntrigueShow, 66, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblIntrigueShow, -35, SpringLayout.NORTH, panel);
		
		lblLearningShow = new JLabel("-0-");
		GridBagConstraints gbc_lblLearningShow = new GridBagConstraints();
		gbc_lblLearningShow.gridx = 4;
		gbc_lblLearningShow.gridy = 2;
		panel.add(lblLearningShow, gbc_lblLearningShow);
		springLayout.putConstraint(SpringLayout.WEST, lblLearningShow, 139, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblLearningShow, -35, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, spnInt, -6, SpringLayout.NORTH, lblLearningShow);
		
		JButton btnSave = new JButton("Save Changes");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int[] attributes = new int[5];
				attributes[0] = (Integer) spnDip.getValue();
				attributes[1] = (Integer) spnMar.getValue();
				attributes[2] = (Integer) spnSte.getValue();
				attributes[3] = (Integer) spnInt.getValue();
				attributes[4] = (Integer) spnLea.getValue();
				database.save(attributes);
			}
		});
		btnSave.setEnabled(false);
		springLayout.putConstraint(SpringLayout.SOUTH, btnSave, -10, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnSave, -10, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(btnSave);
		
		loadSaveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//TODO: Prompt user for file location and name, then load file into data
				
				// Prompt user for save file location and name
				JFrame f = new JFrame();
				String name = JOptionPane.showInputDialog(f,"Please enter the location of the CK2 save file.\n(eg, D:\\Documents\\Patadox Interactive\\Crusader Kings II\\save games\\Save Name.ck2)");
				String charId = JOptionPane.showInputDialog(f,"Please enter the ID of the character you would like to edit.\n(eg, 2609867");
				
				// Load file into data, get attributes in return
				// TODO: Safety checks
				var result = database.begin(charId, name);
				
				// Load result into spinners
				spnDip.setValue(result[0]);
				spnMar.setValue(result[1]);
				spnSte.setValue(result[2]);
				spnInt.setValue(result[3]);
				spnLea.setValue(result[4]);
				
				// Enable components
				spnDip.setEnabled(true);
				spnMar.setEnabled(true);
				spnSte.setEnabled(true);
				spnInt.setEnabled(true);
				spnLea.setEnabled(true);
				btnSave.setEnabled(true);
				updateViewAttributes();
			}
		});
		
	}
}
