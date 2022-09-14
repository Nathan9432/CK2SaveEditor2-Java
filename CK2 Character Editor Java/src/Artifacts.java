import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JOptionPane;

public class Artifacts {
	private HashMap<String, ArtifactBase> baseArtifacts;
	private HashMap<Integer, ArtifactReal> realArtifacts;
	private int[] slots;
	private HashMap<Integer, ArtifactReal> books;
	private HashMap<Integer, ArtifactReal> ownedArtifacts;
	private String charId;
	private String date;

	public Artifacts() {
		baseArtifacts = new HashMap<>();
		realArtifacts = new HashMap<>();
		slots = new int[8]; // weapon, cer_weapon, scepter, crown, wrist, neck, torso, cer_torso
		books = new HashMap<>(); // Four book slots
		ownedArtifacts = new HashMap<>();
	}

	// Defines baseArtifact, a template that realArtifacts are pulled from
	public void setBase(String nm, int dip, int mil, int ste, int inr, int lea, String[] fl, boolean st, int sl) {
		baseArtifacts.put(nm, new ArtifactBase(nm, dip, mil, ste, inr, lea, fl, sl));
	}

	// Defines realArtifact, an instantiated artifact in the game world
	public void setReal(int i, String tp, String nm, String dc, String own, String oo, String obt, String cre, String fd, String hist, boolean equ, boolean act) {
		realArtifacts.put(i, new ArtifactReal(i, tp, dc, nm, own, oo, obt, cre, fd, hist, equ, act));
	}
	
	public void setInfo(String c, String d) {
		charId = c;
		date = d;
	}

	// Adds real artifact to owned list 
	public void setOwned(int i, boolean transfer) {
		ArtifactReal artR = realArtifacts.get(i);
		if (transfer) {
			artR.unequip();
			artR.setOwner(charId, date);
		}
		ownedArtifacts.put(i, artR);
	}

	// Returns all base artifacts, used for selector to create artifacts
	public String[] getArtifactsBase() {
		Set<String> tSet = baseArtifacts.keySet();
		return tSet.toArray(new String[baseArtifacts.size()]);
	}

	// Returns all real artifacts, used for selector to move or destroy artifacts
	public String[] getArtifactsReal() {
		Integer[] prekeys = realArtifacts.keySet().toArray(new Integer[realArtifacts.size()]);
		ArrayList<String> result = new ArrayList<>();
		for (int key : prekeys) {
			if (ownedArtifacts.containsKey(key)) continue; // Removes owned artifacts for third dropdown
			result.add(key + ": " + getName(key));
		}
		return result.toArray(new String[result.size()]);
	}
	
	public String[] getArtifactsOwned() {
		Integer[] prekeys = ownedArtifacts.keySet().toArray(new Integer[ownedArtifacts.size()]);
		ArrayList<String> result = new ArrayList<>();
		for (int key : prekeys){
			result.add(key + ": " + getName(key) + (ownedArtifacts.get(key).isEquipped()?" (E)":""));
		}
		return result.toArray(new String[result.size()]);
	}

	// Returns the set of all artifact IDs
	public int[] getArtifactsIds(){
		int[] result = new int[realArtifacts.size()];
		Object[] prekeys = realArtifacts.keySet().toArray(); // Turns hashmap of real artifacts into an array
		for (int i=0; i<realArtifacts.size(); i++){
			result[i] = ((Integer) prekeys[i]);
		}
		return result;
	}

	// Gets a base artifact
	public ArtifactBase getArtifact(String nm){
		return baseArtifacts.get(nm);
	}

	// Gets a real artifact
	public ArtifactReal getArtifact(int i){
		return realArtifacts.get(i);
	}

	public boolean existsArtifact(String nm){
		return baseArtifacts.containsKey(nm);
	}

	public boolean existsArtifact(int i){
		return realArtifacts.containsKey(i);
	}

	public void removeArtifact(int i){
		if (ownedArtifacts.containsValue(realArtifacts.remove(i)))
			ownedArtifacts.remove(i);
	}

	public ArtifactBase getStats(int i){
		return baseArtifacts.get(realArtifacts.get(i).getType());
	}

	// Returns attributes of all owned artifacts, used to find final attributes
	public int[] getAttributes(){
		int[] results = new int[5];
		ArtifactReal[] arts = ownedArtifacts.values().toArray(new ArtifactReal[ownedArtifacts.size()]);
		for (ArtifactReal art : arts) {
			if (isEquippable(art)? art.isEquipped(): art.isActive()) { // If an artifact is equippable, check if it is equipped. Otherwise, check if it's active
				int[] attr = baseArtifacts.get(art.getType()).getAttributes();
				for (int j=0; j<5; j++){
					results[j] += attr[j];
				}
			} // Not equipped/active, no effect
		}
		return results;
	}

	// Checks if slot is open.
	public boolean isOpen(int i){
		if (i<8 && i>-1) // Between 0 and 7, just show the slot
			return (slots[i] == 0); // 0 is open, number is full
		else if (i==8)
			return books.size() < 4; // If there's space for another book  
		else { // Just for safety's sake.
			System.out.println("Someone asked for a slot out of range!");
			return false;
		}
	}

	public void toggleequip(int id){ // Only usable on owned artifacts
		ArtifactReal artR = ownedArtifacts.get(id);
		int sl = baseArtifacts.get(artR.getType()).getSlot();
		if (sl != -1){ // Check if item isn't slotless
			if (artR.isEquipped()) { // Unequipping. Nice and painless
				artR.unequip();
				if (sl == 8) {
					books.remove(id);
				} else {
					slots[sl] = 0;
				}
			}
			else if (sl == 8) { // Library, ie books, need to be handled separately since there are four slots in the library
				if (books.size()>3) { // The library should not get to more than 4 books, but we like to be safe.
					int acc = 0;
					String[] library = new String[books.size()+1];
					Integer[] ownedBooks = books.keySet().toArray(new Integer[books.size()]);
					for (int ownedBook : ownedBooks) {
						library[acc] = ownedBook + ": " + getName(ownedBook);
						acc++;
					} // Old books added
					library[acc] = id + ": " + getName(artR); // New book added
					// acc exists to account for an improper case where there are more than four books in a character's library
					String selection = (String) JOptionPane.showInputDialog(null, "The character's library is full! Choose a book to remove:", "Library full", JOptionPane.INFORMATION_MESSAGE, null, library, library[acc]);
					if (!selection.equals(library[acc])) { // An old book has been selected
						int book = Integer.parseInt(selection.split(":")[0]);
						ownedArtifacts.get(book).unequip();
						books.remove(book);
						artR.equip();
						books.put(id, artR);
					} // The user has selected the new book to not be added... so we do nothing.
				} else {
					artR.equip();
					books.put(id, artR);
				}
			} else { // Non-library, ie anything normally considered an artifact
				if (slots[sl] != 0){
					int selection = JOptionPane.showConfirmDialog(null, "Do you want to switch from " + ownedArtifacts.get(slots[sl]).getName() + " to " + artR.getName() + "?");
					// 1 is explicit confirm; otherwise, do nothing
					if (selection == 1) {
						ownedArtifacts.get(slots[sl]).unequip();
						slots[sl] = id;
						artR.equip();
					}
				} else {
					artR.equip();
					slots[sl] = id;
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "Hey! Who told you you could try equipping a slotless item?\n\nReport this to the software author, Coolguybest, and include some information about how you got here.\nDon't worry, your save and changes should still be good.");
		}
		
		/*if (art.isEquipped()){
			art.unequip();
			slots[sl] = 0; // Slot is now open
		} else {
			if (slots[sl] != 0){
				realArtifacts.get(slots[sl]).unequip(); // How it works in-game. On equip, out with the old...
				art.equip(); // ...and in with the new.
				slots[sl] = id;
			}
		}*/
	}
	
	// Get the next open ID for artifacts
	public int getNextArtifactId(){
		Object[] prekeys = realArtifacts.keySet().toArray();
		int[] keys = new int[prekeys.length];
		for (int i=0; i<prekeys.length; i++){
			keys[i] = (int) prekeys[i];
		}
		Arrays.sort(keys);
		return keys[keys.length-1] +1;
	}
	
	public int getNumBooks() {
		return books.size();
	}
	
	public String getName(int artId) {
		return getName(realArtifacts.get(artId));
	}
	
	public String getName(ArtifactReal art) {
		String temp = art.getName();
		if ("".equals(temp)) {
			return baseArtifacts.get(art.getType()).getName();
		}
		return temp;
	}
	
	public boolean isEquippable(ArtifactReal art) {
		return baseArtifacts.get(art.getType()).getSlot() != -1;
	}
}
