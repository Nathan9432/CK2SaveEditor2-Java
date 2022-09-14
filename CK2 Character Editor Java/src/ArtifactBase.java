/*
 * Defines the basic information about an artifact that may be instantiated- that is, all artifacts of a given type (by name) will take this data
 */
public class ArtifactBase {
	private String name;
    private int dipMod;
    private int marMod;
    private int steMod;
    private int intMod;
    private int leaMod;
    private String[] flags;
    private boolean stackable;
    private int slot; //weapon, ceremonial_weapon, scepter, crown, wrist, neck, torso, ceremonial_torso, library
    //-1 is slotless
    
    public ArtifactBase(String nm, int d, int m, int s, int i, int l, String[] f, int sl){
        name = nm;
        dipMod = d;
        marMod = m;
        steMod = s;
        intMod = i;
        leaMod = l;
        flags = f;
        slot = sl;
    }
    
    public String getName(){
        return name;
    }
    
    public int[] getAttributes(){
        return new int[] { dipMod, marMod, steMod, intMod, leaMod };
    }
    
    public String[] getFlags(){
        return flags;
    }
    
    public boolean isStackable(){
        return stackable;
    }
    
    public int getSlot(){
        return slot;
    }
}
