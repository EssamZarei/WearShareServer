package wearshareserver;

public class Association extends Entity{

    private Association next; 
    private Clothes headClothes;
    
        public Association(){
        this(1, "Ass", "1Ass", "X", "0000000000");
    } 

    public Association(int ID, String name, String password, String location, String phoneNumber) {
        super(ID, name, password, location, phoneNumber);
        this.headClothes = new Clothes();
    }
    
    
    
    

    public Clothes getHeadClothes() {
        return headClothes;
    }

    public void setHeadClothes(Clothes headClothes) {
        this.headClothes = headClothes;
    }
}
