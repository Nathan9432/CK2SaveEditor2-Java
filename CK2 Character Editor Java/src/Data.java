import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.swing.JOptionPane;

public class Data {
	private Modifiers dataMod;
	private Traits dataTra;
	private Artifacts dataArt;
	private String charId; // The ID number of the character that the user is editing, set in begin, not setup
	private String date;
	private String filename;
	private final String[] SLOT_POSITIONS = {"weapon", "ceremonial_weapon", "scepter", "crown", "wrist", "neck", "torso", "ceremonial_torso", "library"};
	
	public void vanillaSetup(String directory) {
		dataMod = new Modifiers();
		dataTra = new Traits();
		dataArt = new Artifacts();
		// TODO: Load data for modifiers
		// TODO: Load data for traits
		
		// Artifact setup
		File artFile = new File(directory + (directory.charAt(directory.length()-1) == '\\' ? "common\\artifacts" : "\\common\\artifacts"));
		for (File file : artFile.listFiles()) {
			if (file.getName().split(".")[1].equals("txt")) { // To exclude the .info file
				artifactSetup(file);
			}
		}
	}
	
	private void artifactSetup(File file) {
		try (BufferedReader br = new BufferedReader(new FileReader(file))){
			String line = br.readLine();
			while (line != null) {
				if (line.contains(" = {") && !line.startsWith("slots")) { // Line contains artifact name 
					String artName = line.substring(0, line.indexOf(" ")); // first section contains artifact name
					int dip = 0, mar = 0, ste = 0, inr = 0, lea = 0, slot = -1;
					String[] flags = {};
					boolean stackable = true;
					String[] effect;
					line = br.readLine();
					while (!line.equals("}")) {
						if (line.contains("diplomacy =") ||
								line.contains("martial =") ||
								line.contains("intrigue =") ||
								line.contains("learning =") ||
								line.contains("flags =") ||
								line.contains("stacking =") ||
								line.contains("slot =")) {
							effect = line.trim().split(" ");
							switch(line.substring(1,4)) {
								case "dip":
									dip = Integer.parseInt(effect[2]);
								case "mar":
									mar = Integer.parseInt(effect[2]);
								case "ste":
									ste = Integer.parseInt(effect[2]);
								case "int":
									inr = Integer.parseInt(effect[2]);
								case "lea":
									lea = Integer.parseInt(effect[2]);
								case "fla":
									flags = Arrays.copyOfRange(effect, 3, effect.length-1);
								case "sta":
									stackable = !line.contains("no");
								case "slo":
									slot = Arrays.asList(SLOT_POSITIONS).indexOf(effect[2]);
								default: break;
							}
						}
						line = br.readLine();
					}
					dataArt.setBase(artName, dip, mar, ste, inr, lea, flags, stackable, slot);
					/*
					 * A summary of the information submitted:
					 * name is how the artifact is defined, right at the start ("art_name = {")
					 * dip - lea defines how the artifact affects attributes; 0 is default
					 * I haven't seen flage used, but they are in the save file and give some hints about an artifact
					 * stackable is true by default unless specified
					 * sl is slot; can only have one slottable item per slot
					 */
				}
				line = br.readLine();
			}
		} catch (FileNotFoundException ex) {
			JOptionPane.showInternalMessageDialog(null, "File Not Found", "Unable to open file '"+filename+"'.", 0);
		} catch (IOException e) {
			JOptionPane.showInternalMessageDialog(null, "File Read Error", "Error reading file '"+filename+"'.", 0);
		}
	}
	
	public void modSetup(String directory) {
		// late TODO: Add mod support
	}
	
	public void modSetupFire() {
		// late TODO: Add mod support
	}
	
	public ArtifactReal getArtifact(int id) {
		return dataArt.getArtifact(id);
	}
	
	public ArtifactBase getArtifact(String id) {
		return dataArt.getArtifact(id);
	}
	
	public int getNextArtifactId() {
		return dataArt.getNextArtifactId();
	}
	
	public void setReal(int i, String tp, String nm, String dc, String own, String oo, String obt, String cre, String fd, String hist, boolean equ, boolean act) {
		dataArt.setReal(i, tp, nm, dc, own, oo, obt, cre, fd, hist, equ, act);
	}
	
	public void setOwned(int i, boolean tran) {
		dataArt.setOwned(i, tran);
	}
	
	public int[] getAttributesArt() {
		return dataArt.getAttributes();
	}
	
	public void removeArtifact(int id) {
		dataArt.removeArtifact(id);
	}
	
	public void toggleEquip(int id) {
		dataArt.toggleequip(id);
	}
	
	public String[] getArtifactsBase() {
		return dataArt.getArtifactsBase();
	}
	
	public String[] getArtifactsReal() {
		return dataArt.getArtifactsReal();
	}
	
	public String[] getArtifactsOwned()	{
		return dataArt.getArtifactsOwned();
	}
	public int getNumBooks() {
		return dataArt.getNumBooks();
	}
	
	public int[] begin(String charId, String filename) {
		this.charId = charId;
		this.filename = filename;
		int[] attributes = new int[5];
		try (BufferedReader br = new BufferedReader(new FileReader(filename))){
			String line = "";
			
			while (line != null) { // To capture the date
				line = br.readLine();
				if (line.contains("\tdate=")){
						break;
				}
			}
			date = line.split("\"")[1];
			
			while (line != null) { // To find the character the user defined
				line = br.readLine();
				if (line.contains("\t\t"+charId+"="))
					break;
			}
			
			while (line != null) { // To find the character's attributes
				line = br.readLine();
				if (line.contains("att={"))
					break;
			}
			
			// Format: "\t\tatt={ a b c d e }"
			String[] attributesTemp = line.substring(8, line.length()-1).split(" ");
			for (int i=0; i<5; i++)
				attributes[i] = Integer.parseInt(attributesTemp[i]);
			// Traits are immediately after attributes
			
			// Modifiers are some time later
			
			// Modifier section ends with a line that does not deal with modifiers
			
			// Artifacts are near the end of the file
			while (line != null) {
				line = br.readLine();
				if (line.equals("\tartifacts="))
					break;
			}
			while (line != null ) {
				if (line.equals("\t}"))
					break;
				// Getting the ID
				int id = Integer.parseInt(line.substring(2, line.length()-1));
				br.readLine();
				line = br.readLine();
				
				String type = line.split("\"")[1];
				line = br.readLine();
				
				String name = "";
				String description = "";
				if (line.contains("name")) { // Name might not be explicitly defined
					name = line.split("\"")[1];
					line = br.readLine();
				}
				if (line.contains("desc")) { // Description might not be explicitly defined
					name = line.split("\"")[1];
					line = br.readLine();
				}
				
				String owner = line.split("=")[1];
				line = br.readLine();
				
				String originalOwner = line.split("=")[1];
				line = br.readLine();
				
				String flagDate = "";
				if (line.contains("flags")) {
					br.readLine();
					line = br.readLine();
					flagDate = line.split("=")[1];
					while (line != null) {
						line = br.readLine();
						if (line.contains("}"))
							break;
					}
					line = br.readLine();
				}
				
				String created = "";
				if (line.contains("created")) {
					created = line.split("\"")[1];
					line = br.readLine();
				}
				
				String obtained = line.split("\"")[1];
				line = br.readLine();
				
				boolean equipped = false;
				boolean active = false;
				if (line.contains("equipped")) {
					equipped = line.contains("yes");
					line = br.readLine();
				}
				if (line.contains("active")) {
					active = line.contains("yes");
					line = br.readLine();
				}
				
				String history = "";
				if (line.contains("history")) {
					br.readLine();
					line = br.readLine();
					history = line.substring(4);
				}
				
				dataArt.setReal(id, type, name, description, owner, originalOwner, obtained, created, flagDate, history, equipped, active);
				if (owner.equals(charId))
					dataArt.setOwned(id, false);
				br.readLine();
			}
			dataArt.setInfo(charId, date);
		}
		catch (FileNotFoundException ex) {
			JOptionPane.showMessageDialog(null, "Unable to open file '"+filename+"'.");
		}
		catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "Error reading file '"+filename+"'.");
		}
		return attributes;
	}

	public void save(int[] attributes) {
		String fileName = filename.substring(0, filename.length()-4) + "_output.ck2";
		String line;
		String check;
		String attributeString = "\t\t\tatt={";
		for (int i=0; i<5; i++) {
			attributeString += String.valueOf(attributes[i]);
			if (i != 4) attributeString += " ";
		}
		attributeString += "}\n";
		// TODO: build the traits line
		try (BufferedReader br = new BufferedReader(new FileReader(filename)); PrintWriter pw = new PrintWriter(new FileWriter (fileName))) {
			line = br.readLine();
			check = "\t\t" + charId + "=";
			
			while(line != null) { // To find the correct character
				if (line.equals(check))
					break;
				pw.print(line+"\n");
				line = br.readLine();
				// TODO: If EOF is reached, throw an error because the character wasn't there
				// Technically, it should always be found because this was the file loaded earlier, but might as well have the safety check 
			}
			
			while (line != null) { // Find the attributes of the right character
				if (line.contains("att={"))
					break;
				pw.print(line+"\n");
				line = br.readLine();
			}
			
			pw.print(attributeString); // Attributes
			line = br.readLine();
			// TODO: Traits line, immediately afterwards
			/*while (line != null) {
				if (line.startsWith("\t\t\taction=") || line.contains("md="))
					break;
				pw.print(line+"\n");
				line = br.readLine();
			}
			line = br.readLine();*/
			// TODO: Inserting Modifiers
			// TODO: Inserting Artifacts
			
			//line = br.readLine();
			
			// Saving artifacts
			// Finding artifact insertion point
			while (line != null) {
				if (line.equals("\tartifacts="))
					break;
				else pw.print(line+"\n");
				line = br.readLine();
			}
			pw.print("\tartifacts=\n");
			pw.print("\t{\n");
			
			
			int[] artIds = dataArt.getArtifactsIds();
			for (int artId : artIds) {
				ArtifactReal artifact = dataArt.getArtifact(artId);
				pw.print("\t\t" + artId + "=\n\t\t{\n");
				String type = artifact.getType();
				pw.print("\t\t\ttype=" + type + "\n");
				pw.print("\t\t\towner=" + artifact.getOwner() + "\n");
				pw.print("\t\t\torg-owner=" + artifact.getOrgOwner() + "\n");
				String[] flags = dataArt.getArtifact(type).getFlags();
				if (flags.length != 0) {
					pw.print("\t\t\tflags=\n\t\t\t{\n");
					for (String flag : flags) 
						pw.print("\t\t\t\t" + flag + "=" + artifact.getFlagDate() + "\n");
					pw.print("\t\t\t}\n");
				}
				
				if (!"".equals(artifact.getCreated()))
					pw.print("\t\t\tcreated=" + artifact.getCreated() + "\n");
				pw.print("\t\t\tobtained=" + artifact.getObtained());
				if (artifact.getEquipped())
					pw.print("\t\t\tequipped=yes\n");
				if (artifact.getActive())
					pw.print("\t\t\tactive=yes\n");
				
				pw.print("\t\t\thistory=\n\t\t\t{\n");
				pw.print("\t\t\t\t" + artifact.getHistory()+"\n"); // Already collated
				pw.print("\t\t\t}\n\t\t}\n");
			}
			
			line = br.readLine();
			while (line.startsWith("\t\t")) { // Bypass the artifacts section which has been overwritten
				line = br.readLine();
			}
			
			while (line != null) { // Dump the last of the information to the writer  
				pw.print(line+"\n");
				line = br.readLine();
			}
			br.close();
			pw.flush();
			pw.close();
			JOptionPane.showInternalMessageDialog(null, "Save Successful", "File '"+fileName+"' has been saved successfully.", 2);
		} catch (FileNotFoundException e) {
			JOptionPane.showInternalMessageDialog(null, "File Not Found", "Unable to open file '"+filename+"'.", 0);
		} catch (IOException e) {
			JOptionPane.showInternalMessageDialog(null, "File Read Error", "Error reading file '"+filename+"'.", 0);
		}
	}
}
