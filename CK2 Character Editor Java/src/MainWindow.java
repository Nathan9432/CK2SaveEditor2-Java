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
import javax.swing.JComboBox;
import javax.swing.JCheckBox;

public class MainWindow {

	private JFrame frame;
	private static Data database;
	private static Status status;
	private static String date;
	private static int[] modifierAttributes;
	private static int[] artifactAttributes;
	private static int[] traitAttributes;
	private static String charId;
	
	private JSpinner spnDip;
	private JSpinner spnMar;
	private JSpinner spnSte;
	private JSpinner spnInt;
	private JSpinner spnLea;
	private JLabel lblDiplomacy;
	private JLabel lblMartial;
	private JLabel lblStewardship;
	private JLabel lblIntrigue;
	private JLabel lblLearning;
	
	private JButton btnAddMod;
	
	private JLabel lblArtifactTypes;
	private JComboBox<String> selectorBaseArtifacts;
	private JButton btnCreateArtifact;
	private JLabel lblExistingArtifacts;
	private JComboBox<String> selectorRealArtifacts;
	private JButton btnChangeOwner;
	private JButton btnDeleteArtifact;
	private JLabel lblOwnedArtifacts;
	private JComboBox<String> selectorOwnedArtifacts;
	private JButton btnToggleEquip;
	private JButton btnDeleteOwnedArtifact;
	private JCheckBox showEquipped;
	private JCheckBox showActive;
	private JLabel lblArtOwner;
	private JLabel lblBooks;
	

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
		lblDiplomacy.setText(Integer.toString(dip + modifierAttributes[0] + artifactAttributes[0]));
		int mar = (Integer) spnMar.getValue();
		lblMartial.setText(Integer.toString(mar + modifierAttributes[1] + artifactAttributes[1]));
		int ste = (Integer) spnSte.getValue();
		lblStewardship.setText(Integer.toString(ste + modifierAttributes[2] + artifactAttributes[2]));
		int inr = (Integer) spnInt.getValue();
		lblIntrigue.setText(Integer.toString(inr + modifierAttributes[3] + artifactAttributes[3]));
		int lea = (Integer) spnLea.getValue();
		lblLearning.setText(Integer.toString(lea + modifierAttributes[4] + artifactAttributes[4]));
	}
	
	private void artifactSelectorUpdate() {
		if (!(selectorRealArtifacts.getItemCount() == 0) && (selectorRealArtifacts.getSelectedItem() != null)) {
			String choice = String.valueOf(selectorRealArtifacts.getSelectedItem());
			int id = Integer.parseInt(choice.split(":")[0]);
			ArtifactReal art = database.getArtifact(id);
			lblArtOwner.setText("Owner: " + art.getOwner());
			btnDeleteArtifact.setEnabled(true);
			btnChangeOwner.setEnabled(true);
		} else { // In case there aren't any artifacts in the dropdown; edge case
			lblArtOwner.setText("Owner: [   ]");
			btnDeleteArtifact.setEnabled(false);
			btnChangeOwner.setEnabled(false);
		}
	}
	
	private void ownedArtifactSelectorUpdate() {
		if (!(selectorOwnedArtifacts.getItemCount() == 0) && (selectorOwnedArtifacts.getSelectedItem() != null)) {
			String choice = String.valueOf(selectorOwnedArtifacts.getSelectedItem());
			int id = Integer.parseInt(choice.split(":")[0]);
			ArtifactReal art = database.getArtifact(id);
			showEquipped.setSelected(art.isEquipped());
			showActive.setSelected(art.isActive());
			if (database.getArtifact(art.getType()).getSlot() != -1) {
				btnToggleEquip.setText(art.isEquipped()?"Unequip":"Equip");
				btnToggleEquip.setEnabled(true);
			} else {
				btnToggleEquip.setText("Slotless");
				btnToggleEquip.setEnabled(false);
			}
			btnDeleteOwnedArtifact.setEnabled(true);
			int books = database.getNumBooks();
			lblBooks.setText("Books: [" + "|".repeat(books) + " ".repeat(4-books) + "]");
		} else { // In case there aren't any artifacts in the dropdown; edge case
			showEquipped.setSelected(false);
			showActive.setSelected(false);
			btnToggleEquip.setText("Equip");
			btnToggleEquip.setEnabled(false);
			btnDeleteOwnedArtifact.setEnabled(false);
		}
	}
	
	private void artifactSelectorUpdateList() {
		selectorRealArtifacts.removeAllItems();
		for (String realArtifact : database.getArtifactsReal())
			selectorRealArtifacts.addItem(realArtifact);
	}
	
	private void ownedArtifactSelectorUpdateList() {
		selectorOwnedArtifacts.removeAllItems();
		for (String ownedArtifact : database.getArtifactsOwned())
			selectorOwnedArtifacts.addItem(ownedArtifact);
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		// General components
		
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		btnAddMod = new JButton("Initialize Mod");
		btnAddMod.setEnabled(false);
		frame.getContentPane().add(btnAddMod);
		
		JButton initializeButton = new JButton("Initialize");
		springLayout.putConstraint(SpringLayout.NORTH, initializeButton, 0, SpringLayout.NORTH, btnAddMod);
		springLayout.putConstraint(SpringLayout.WEST, initializeButton, 6, SpringLayout.EAST, btnAddMod);
		springLayout.putConstraint(SpringLayout.EAST, initializeButton, -506, SpringLayout.EAST, frame.getContentPane());
		frame.getContentPane().add(initializeButton);
		
		JButton loadSaveButton = new JButton("Load Save");
		springLayout.putConstraint(SpringLayout.NORTH, loadSaveButton, 0, SpringLayout.NORTH, initializeButton);
		springLayout.putConstraint(SpringLayout.WEST, loadSaveButton, 6, SpringLayout.EAST, initializeButton);
		springLayout.putConstraint(SpringLayout.EAST, loadSaveButton, -421, SpringLayout.EAST, frame.getContentPane());
		loadSaveButton.setEnabled(false);
		frame.getContentPane().add(loadSaveButton);
		
		initializeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Prompt user for CK2 files location to load, eg traits, into data; activate loadSaveButton when done
				
				// TODO: Check if already in SAVE_LOADED, warn user about potentially lost save info
				
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
		
		// Attribute components
		
		JPanel panel = new JPanel();
		springLayout.putConstraint(SpringLayout.NORTH, panel, 37, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnAddMod, 0, SpringLayout.WEST, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, btnAddMod, -6, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.WEST, panel, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel, 360, SpringLayout.WEST, frame.getContentPane());
		frame.getContentPane().add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {65, 65, 65, 65, 65, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		lblDiplomacy = new JLabel("Diplomacy");
		GridBagConstraints gbc_lblDiplomacy = new GridBagConstraints();
		gbc_lblDiplomacy.insets = new Insets(0, 0, 5, 5);
		gbc_lblDiplomacy.gridx = 0;
		gbc_lblDiplomacy.gridy = 0;
		panel.add(lblDiplomacy, gbc_lblDiplomacy);
		springLayout.putConstraint(SpringLayout.NORTH, lblDiplomacy, 6, SpringLayout.SOUTH, initializeButton);
		springLayout.putConstraint(SpringLayout.EAST, lblDiplomacy, -10, SpringLayout.EAST, initializeButton);
		
		lblMartial = new JLabel("Martial");
		GridBagConstraints gbc_lblMartial = new GridBagConstraints();
		gbc_lblMartial.insets = new Insets(0, 0, 5, 5);
		gbc_lblMartial.gridx = 1;
		gbc_lblMartial.gridy = 0;
		panel.add(lblMartial, gbc_lblMartial);
		springLayout.putConstraint(SpringLayout.NORTH, lblMartial, 6, SpringLayout.SOUTH, loadSaveButton);
		springLayout.putConstraint(SpringLayout.WEST, lblMartial, 10, SpringLayout.WEST, loadSaveButton);
		
		lblStewardship = new JLabel("Stewardship");
		GridBagConstraints gbc_lblStewardship = new GridBagConstraints();
		gbc_lblStewardship.insets = new Insets(0, 0, 5, 5);
		gbc_lblStewardship.gridx = 2;
		gbc_lblStewardship.gridy = 0;
		panel.add(lblStewardship, gbc_lblStewardship);
		springLayout.putConstraint(SpringLayout.NORTH, lblStewardship, 6, SpringLayout.SOUTH, loadSaveButton);
		springLayout.putConstraint(SpringLayout.WEST, lblStewardship, 135, SpringLayout.WEST, frame.getContentPane());
		
		lblIntrigue = new JLabel("Intrigue");
		GridBagConstraints gbc_lblIntrigue = new GridBagConstraints();
		gbc_lblIntrigue.insets = new Insets(0, 0, 5, 5);
		gbc_lblIntrigue.gridx = 3;
		gbc_lblIntrigue.gridy = 0;
		panel.add(lblIntrigue, gbc_lblIntrigue);
		springLayout.putConstraint(SpringLayout.NORTH, lblIntrigue, 39, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblIntrigue, 200, SpringLayout.WEST, frame.getContentPane());
		
		lblLearning = new JLabel("Learning");
		GridBagConstraints gbc_lblLearning = new GridBagConstraints();
		gbc_lblLearning.insets = new Insets(0, 0, 5, 0);
		gbc_lblLearning.gridx = 4;
		gbc_lblLearning.gridy = 0;
		panel.add(lblLearning, gbc_lblLearning);
		springLayout.putConstraint(SpringLayout.NORTH, lblLearning, 39, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblLearning, 244, SpringLayout.WEST, frame.getContentPane());
		
		spnDip = new JSpinner();
		spnDip.setEnabled(false);
		spnDip.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				updateViewAttributes();
			}
		});
		GridBagConstraints gbc_spnDip = new GridBagConstraints();
		gbc_spnDip.fill = GridBagConstraints.HORIZONTAL;
		gbc_spnDip.insets = new Insets(0, 0, 5, 5);
		gbc_spnDip.gridx = 0;
		gbc_spnDip.gridy = 1;
		panel.add(spnDip, gbc_spnDip);
		springLayout.putConstraint(SpringLayout.WEST, spnDip, 0, SpringLayout.WEST, initializeButton);
		springLayout.putConstraint(SpringLayout.NORTH, spnDip, 6, SpringLayout.SOUTH, lblDiplomacy);
		
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
		springLayout.putConstraint(SpringLayout.NORTH, spnSte, 6, SpringLayout.SOUTH, lblMartial);
		
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
		springLayout.putConstraint(SpringLayout.NORTH, spnLea, 6, SpringLayout.SOUTH, lblStewardship);
		
		lblDiplomacy = new JLabel("-0-");
		GridBagConstraints gbc_lblDiplomacyShow = new GridBagConstraints();
		gbc_lblDiplomacyShow.insets = new Insets(0, 0, 0, 5);
		gbc_lblDiplomacyShow.gridx = 0;
		gbc_lblDiplomacyShow.gridy = 2;
		panel.add(lblDiplomacy, gbc_lblDiplomacyShow);
		springLayout.putConstraint(SpringLayout.NORTH, lblDiplomacy, 52, SpringLayout.SOUTH, initializeButton);
		springLayout.putConstraint(SpringLayout.WEST, lblDiplomacy, 10, SpringLayout.WEST, frame.getContentPane());
		
		lblMartial = new JLabel("-0-");
		GridBagConstraints gbc_lblMartialShow = new GridBagConstraints();
		gbc_lblMartialShow.insets = new Insets(0, 0, 0, 5);
		gbc_lblMartialShow.gridx = 1;
		gbc_lblMartialShow.gridy = 2;
		panel.add(lblMartial, gbc_lblMartialShow);
		
		lblStewardship = new JLabel("-0-");
		GridBagConstraints gbc_lblStewardshipShow = new GridBagConstraints();
		gbc_lblStewardshipShow.insets = new Insets(0, 0, 0, 5);
		gbc_lblStewardshipShow.gridx = 2;
		gbc_lblStewardshipShow.gridy = 2;
		panel.add(lblStewardship, gbc_lblStewardshipShow);
		springLayout.putConstraint(SpringLayout.WEST, lblStewardship, 46, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblStewardship, -35, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, spnMar, -6, SpringLayout.NORTH, lblStewardship);
		
		lblIntrigue = new JLabel("-0-");
		GridBagConstraints gbc_lblIntrigueShow = new GridBagConstraints();
		gbc_lblIntrigueShow.insets = new Insets(0, 0, 0, 5);
		gbc_lblIntrigueShow.gridx = 3;
		gbc_lblIntrigueShow.gridy = 2;
		panel.add(lblIntrigue, gbc_lblIntrigueShow);
		springLayout.putConstraint(SpringLayout.WEST, lblIntrigue, 66, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblIntrigue, -35, SpringLayout.NORTH, panel);
		
		lblLearning = new JLabel("-0-");
		GridBagConstraints gbc_lblLearningShow = new GridBagConstraints();
		gbc_lblLearningShow.gridx = 4;
		gbc_lblLearningShow.gridy = 2;
		panel.add(lblLearning, gbc_lblLearningShow);
		springLayout.putConstraint(SpringLayout.WEST, lblLearning, 139, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, lblLearning, -35, SpringLayout.NORTH, panel);
		springLayout.putConstraint(SpringLayout.SOUTH, spnInt, -6, SpringLayout.NORTH, lblLearning);
		
		JPanel panel_1 = new JPanel();
		springLayout.putConstraint(SpringLayout.SOUTH, panel, -6, SpringLayout.NORTH, panel_1);
		springLayout.putConstraint(SpringLayout.NORTH, panel_1, 101, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, panel_1, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, panel_1, 0, SpringLayout.EAST, panel);
		frame.getContentPane().add(panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);
		
		// Artifact Components
		
		lblArtifactTypes = new JLabel("Artifact Types");
		GridBagConstraints gbc_lblArtifactTypes = new GridBagConstraints();
		gbc_lblArtifactTypes.anchor = GridBagConstraints.WEST;
		gbc_lblArtifactTypes.insets = new Insets(0, 0, 5, 0);
		gbc_lblArtifactTypes.gridx = 0;
		gbc_lblArtifactTypes.gridy = 0;
		panel_1.add(lblArtifactTypes, gbc_lblArtifactTypes);
		
		selectorBaseArtifacts = new JComboBox<String>();
		selectorBaseArtifacts.setEnabled(false);
		GridBagConstraints gbc_baseArtifactSelector = new GridBagConstraints();
		gbc_baseArtifactSelector.insets = new Insets(0, 0, 5, 0);
		gbc_baseArtifactSelector.fill = GridBagConstraints.HORIZONTAL;
		gbc_baseArtifactSelector.gridx = 0;
		gbc_baseArtifactSelector.gridy = 1;
		panel_1.add(selectorBaseArtifacts, gbc_baseArtifactSelector);
		
		btnCreateArtifact = new JButton("Create Artifact");
		btnCreateArtifact.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String choice = String.valueOf(selectorBaseArtifacts.getSelectedItem());
				int id = database.getNextArtifactId();
				String type = database.getArtifact(choice).getName();
				String nm = "", desc = "", owner = charId, org_owner = charId, obtained = date, created = date, flagDate = date;
				String history = charId+"="+date;
				boolean equipped = false, active = true;
				
				database.setReal(id, type, nm, desc, owner, org_owner, obtained, created, flagDate, history, equipped, active);
				database.setOwned(id, false);
				artifactAttributes = database.getAttributesArt();
				updateViewAttributes();
				ownedArtifactSelectorUpdateList();
				ownedArtifactSelectorUpdate();
			}
		});
		btnCreateArtifact.setEnabled(false);
		GridBagConstraints gbc_createArtifact = new GridBagConstraints();
		gbc_createArtifact.anchor = GridBagConstraints.WEST;
		gbc_createArtifact.insets = new Insets(0, 0, 5, 0);
		gbc_createArtifact.gridx = 0;
		gbc_createArtifact.gridy = 2;
		panel_1.add(btnCreateArtifact, gbc_createArtifact);
		
		lblExistingArtifacts = new JLabel("Existing Artifacts (not owned by the character being edited)");
		GridBagConstraints gbc_lblExistingArtifacts = new GridBagConstraints();
		gbc_lblExistingArtifacts.anchor = GridBagConstraints.WEST;
		gbc_lblExistingArtifacts.insets = new Insets(0, 0, 5, 0);
		gbc_lblExistingArtifacts.gridx = 0;
		gbc_lblExistingArtifacts.gridy = 3;
		panel_1.add(lblExistingArtifacts, gbc_lblExistingArtifacts);
		
		selectorRealArtifacts = new JComboBox<String>();
		selectorRealArtifacts.setEnabled(false);
		GridBagConstraints gbc_realArtifactSelector = new GridBagConstraints();
		gbc_realArtifactSelector.insets = new Insets(0, 0, 5, 0);
		gbc_realArtifactSelector.fill = GridBagConstraints.HORIZONTAL;
		gbc_realArtifactSelector.gridx = 0;
		gbc_realArtifactSelector.gridy = 4;
		panel_1.add(selectorRealArtifacts, gbc_realArtifactSelector);
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 0);
		gbc_panel_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_3.gridx = 0;
		gbc_panel_3.gridy = 5;
		panel_1.add(panel_3, gbc_panel_3);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		btnChangeOwner = new JButton("Give to me");
		GridBagConstraints gbc_changeOwner = new GridBagConstraints();
		gbc_changeOwner.insets = new Insets(0, 0, 0, 5);
		gbc_changeOwner.gridx = 0;
		gbc_changeOwner.gridy = 0;
		panel_3.add(btnChangeOwner, gbc_changeOwner);
		btnChangeOwner.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String choice = String.valueOf(selectorRealArtifacts.getSelectedItem());
				database.setOwned(Integer.parseInt(choice.split(":")[0]), true);
				
				artifactAttributes = database.getAttributesArt();
				updateViewAttributes();
				artifactSelectorUpdate();
				artifactSelectorUpdateList(); // Candidate for manual addition/removal
				ownedArtifactSelectorUpdateList(); // Candidate for manual addition/removal
			}
		});
		btnChangeOwner.setEnabled(false);
		
		btnDeleteArtifact = new JButton("Delete Item");
		GridBagConstraints gbc_deleteArtifact = new GridBagConstraints();
		gbc_deleteArtifact.insets = new Insets(0, 0, 0, 5);
		gbc_deleteArtifact.gridx = 1;
		gbc_deleteArtifact.gridy = 0;
		panel_3.add(btnDeleteArtifact, gbc_deleteArtifact);
		btnDeleteArtifact.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String choice = String.valueOf(selectorRealArtifacts.getSelectedItem());
				database.removeArtifact(Integer.parseInt(choice.split(":")[0]));
				
				//selectorRealArtifacts.removeItem(selectorRealArtifacts.getSelectedItem());
				// Will people complain if the dropdown constantly resets? I'll keep this in case I want to make it an option later on. 
				
				artifactSelectorUpdate();
				artifactSelectorUpdateList();
			}
		});
		btnDeleteArtifact.setEnabled(false);
		
		lblArtOwner = new JLabel("Owner: [   ]");
		GridBagConstraints gbc_showArtOwner = new GridBagConstraints();
		gbc_showArtOwner.gridx = 2;
		gbc_showArtOwner.gridy = 0;
		panel_3.add(lblArtOwner, gbc_showArtOwner);
		
		lblOwnedArtifacts = new JLabel("Owned Artifacts");
		GridBagConstraints gbc_lblOwnedArtifacts = new GridBagConstraints();
		gbc_lblOwnedArtifacts.anchor = GridBagConstraints.WEST;
		gbc_lblOwnedArtifacts.insets = new Insets(0, 0, 5, 0);
		gbc_lblOwnedArtifacts.gridx = 0;
		gbc_lblOwnedArtifacts.gridy = 6;
		panel_1.add(lblOwnedArtifacts, gbc_lblOwnedArtifacts);
		
		selectorOwnedArtifacts = new JComboBox<String>();
		selectorOwnedArtifacts.setEnabled(false);
		GridBagConstraints gbc_ownedArtifactsSelector = new GridBagConstraints();
		gbc_ownedArtifactsSelector.insets = new Insets(0, 0, 5, 0);
		gbc_ownedArtifactsSelector.fill = GridBagConstraints.HORIZONTAL;
		gbc_ownedArtifactsSelector.gridx = 0;
		gbc_ownedArtifactsSelector.gridy = 7;
		panel_1.add(selectorOwnedArtifacts, gbc_ownedArtifactsSelector);
		
		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 5, 0);
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 8;
		panel_1.add(panel_2, gbc_panel_2);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_2.rowHeights = new int[] {0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);
		
		btnToggleEquip = new JButton("Equip");
		btnToggleEquip.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String choice = String.valueOf(selectorOwnedArtifacts.getSelectedItem());
				database.toggleEquip(Integer.parseInt(choice.split(":")[0]));
				
				artifactAttributes = database.getAttributesArt();
				updateViewAttributes();
				ownedArtifactSelectorUpdate();
			}
		});
		btnToggleEquip.setEnabled(false);
		GridBagConstraints gbc_toggleEquip = new GridBagConstraints();
		gbc_toggleEquip.anchor = GridBagConstraints.WEST;
		gbc_toggleEquip.insets = new Insets(0, 0, 0, 5);
		gbc_toggleEquip.gridx = 0;
		gbc_toggleEquip.gridy = 0;
		panel_2.add(btnToggleEquip, gbc_toggleEquip);
		
		btnDeleteOwnedArtifact = new JButton("Delete Owned Item");
		btnDeleteOwnedArtifact.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String choice = String.valueOf(selectorOwnedArtifacts.getSelectedItem());
				database.removeArtifact(Integer.parseInt(choice.split(":")[0]));
				// Automatically removes from owned if it's in there! Good job, past me!
				
				//selectorRealArtifacts.removeItem(selectorRealArtifacts.getSelectedItem());
				
				ownedArtifactSelectorUpdate();
				ownedArtifactSelectorUpdateList();
			}
		});
		btnDeleteOwnedArtifact.setEnabled(false);
		GridBagConstraints gbc_deleteOwnedItem = new GridBagConstraints();
		gbc_deleteOwnedItem.insets = new Insets(0, 0, 0, 5);
		gbc_deleteOwnedItem.gridx = 1;
		gbc_deleteOwnedItem.gridy = 0;
		panel_2.add(btnDeleteOwnedArtifact, gbc_deleteOwnedItem);
		
		JPanel panel_4 = new JPanel();
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 9;
		panel_1.add(panel_4, gbc_panel_4);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_4.rowHeights = new int[]{0, 0};
		gbl_panel_4.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);
		
		showEquipped = new JCheckBox("Equipped");
		GridBagConstraints gbc_showEquipped = new GridBagConstraints();
		gbc_showEquipped.insets = new Insets(0, 0, 0, 5);
		gbc_showEquipped.gridx = 0;
		gbc_showEquipped.gridy = 0;
		panel_4.add(showEquipped, gbc_showEquipped);
		showEquipped.setEnabled(false);
		
		showActive = new JCheckBox("Active");
		GridBagConstraints gbc_showActive = new GridBagConstraints();
		gbc_showActive.insets = new Insets(0, 0, 0, 5);
		gbc_showActive.gridx = 1;
		gbc_showActive.gridy = 0;
		panel_4.add(showActive, gbc_showActive);
		showActive.setEnabled(false);
		
		lblBooks = new JLabel("Books: [    ]");
		GridBagConstraints gbc_lblBooks = new GridBagConstraints();
		gbc_lblBooks.gridx = 2;
		gbc_lblBooks.gridy = 0;
		panel_4.add(lblBooks, gbc_lblBooks);
		
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
		springLayout.putConstraint(SpringLayout.SOUTH, panel_1, -21, SpringLayout.SOUTH, btnSave);
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
				
				// Set up artifacts
				for (String baseArtifact : database.getArtifactsBase())
					selectorBaseArtifacts.addItem(baseArtifact);
				for (String realArtifact : database.getArtifactsReal())
					selectorRealArtifacts.addItem(realArtifact);
				for (String ownedArtifact : database.getArtifactsOwned())
					selectorOwnedArtifacts.addItem(ownedArtifact);
				
				// Enable components
				spnDip.setEnabled(true);
				spnMar.setEnabled(true);
				spnSte.setEnabled(true);
				spnInt.setEnabled(true);
				spnLea.setEnabled(true);
				artifactAttributes = database.getAttributesArt();
				// Enable artifact elements
				selectorBaseArtifacts.setEnabled(true);
				selectorRealArtifacts.setEnabled(true);
				btnCreateArtifact.setEnabled(true);
				btnSave.setEnabled(true);
				updateViewAttributes();
				artifactSelectorUpdate();
				ownedArtifactSelectorUpdate();
			}
		});
	}
}
