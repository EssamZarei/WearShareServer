
package wearshareserver;

public class Entity {
    protected int ID;
    protected String name;
    protected String password;
    protected String location;
    protected String phoneNumber;
    protected String info;

    // Constructors
    public Entity() {
        this(1, "Entity", "1Ent", "X", "0000000000", "");
    }

    public Entity(int ID, String name, String password, String location, String phoneNumber) {
        this(ID, name, password, location, phoneNumber, "");
    }

    public Entity(int ID, String name, String password, String location, String phoneNumber, String info) {
        this.ID = ID;
        this.name = name;
        this.password = password;
        this.location = location;
        this.phoneNumber = phoneNumber;
        this.info = info;
    }

    
    





    // Getter and Setter methods for private attributes
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

}
