package wearshareserver;

public class Store extends Entity{

 // Class fields
    private String promotions;
    private String code;

    // Constructor
    public Store(){
        this(1, "Store", "1Store", "X", "0000000000", "50", "Store");
    }
    public Store(int ID, String name, String password, String location, String phoneNumber, String promotions, String code) {
        super(ID, name, password, location, phoneNumber);
        this.promotions = promotions;
        this.code = code;
    }
    
    
        // Getter and setter methods

    public String getPromotions() {
        return promotions;
    }

    public void setPromotions(String promotions) {
        this.promotions = promotions;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
}
