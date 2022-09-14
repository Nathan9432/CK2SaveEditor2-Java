
public class ArtifactReal {
	@SuppressWarnings("unused")
	private int ID; // Unique identifier for artifact
    private String type; // References ArtifactBase
    private String name;
    private String desc;
    private String owner;
    private String org_owner;
    private String obtained;
    private String created; //Poss to be blank
    private String flDate;
    private String history;
    private boolean equipped; //Only important if it has a slot
    private boolean active;
    
    public ArtifactReal(int i, String tp, String nm, String dc, String own, String oo, String obt, String cre, String fd, String hist, boolean equ, boolean act){
        ID = i;
        type = tp;
        name = nm;
        desc = dc;
        owner = own;
        org_owner = oo;
        obtained = obt;
        created = cre;
        flDate = fd;
        equipped = equ;
        active = act;
        history = hist;
    }
    
    public String getType(){
        return type;
    }
    
    public void setOwner(String owner, String obtained){
        this.owner = owner;
        this.obtained = obtained;
        history += " " + owner + "=" + obtained; 
    }
    
    public String getOwner(){
        return owner;
    }
    
    public String getOrgOwner(){
        return org_owner;
    }
    
    public boolean isEquipped(){
        return equipped;
    }
    
    public void equip(){
        equipped = true;
    }
    
    public void unequip(){
        equipped = false;
    }
    
    public boolean isActive(){
        return active;
    }
    
    public String getFlagDate(){
        return flDate;
    }
    
    public String getCreated(){
        return created;
    }
    
    public String getObtained(){
        return obtained;
    }
    
    public boolean getEquipped(){
        return equipped;
    }
    
    public boolean getActive(){
        return active;
    }
    
    public String getName() {
    	return name;
    }
    
    public String getDesc() {
    	return desc;
    }
    
    public void addHistory(String hist) {
    	history += " " + hist;
    }
    
    public String getHistory() {
    	return history;
    }
}
