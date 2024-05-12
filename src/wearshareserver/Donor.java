package wearshareserver;

public class Donor extends Entity {
    // Class fields

    private int rewards;

    // Constructor
    public Donor() {
        this(1, "Donor", "1Donor", "X", "0000000000");
    }

    public Donor(int ID, String name, String password, String location, String phoneNumber) {
        super(ID, name, password, location, phoneNumber);
        this.rewards = 0;
    }

    public int getRewards() {
        return rewards;
    }

    public void setRewards(int rewards) {
        this.rewards = rewards;
    }
    
    
    
    
    
}
