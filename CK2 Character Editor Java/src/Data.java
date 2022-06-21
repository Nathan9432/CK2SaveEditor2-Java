import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Data {
	private Modifiers dataMod;
	private Traits dataTra;
	private Artifacts dataArt;
	private String charId; // The ID number of the character that the user is editing, set in begin, not setup
	private String date;
	private String filename;
	
	public void vanillaSetup(String directory) {
		dataMod = new Modifiers();
		dataTra = new Traits();
		dataArt = new Artifacts();
		// TODO: Load data for modifiers
		// TODO: Load data for traits
		// TODO: Load data for artifacts
	}
	
	public void modSetup(String directory) {
		// TODO: Add mod support
	}
	
	public void modSetupFire() {
		// TODO: Add mod support
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
			while (line != null) { // To find the character the user wants
				line = br.readLine();
				if (line.contains("\t\t"+charId+"="))
					break;
			}
			while (line != null) { // To find the character's attributes
				line = br.readLine();
				if (line.contains("att={"))
					break;
			}
			String[] attributesTemp = line.substring(8, line.length()-1).split(" ");
			for (int i=0; i<5; i++)
				attributes[i] = Integer.parseInt(attributesTemp[i]);
			// Traits are immediately after attributes
			
			// Modifiers are some time later
			
			// Modifier section ends with a line that does not deal with modifiers
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
			while(line != null) { // Done to find the correct character
				if (line.equals(check))
					break;
				pw.print(line+"\n");
				line = br.readLine();
				// TODO: If you get to the end, throw an error because the person wasn't there
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
			/*while(line.startsWith("\t\t")) //Dump the rest of the artifacts 
				line = br.readLine(); */
			while (line != null) {
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
