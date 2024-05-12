
package wearshareserver;

public class Clothes {
        // Class fields
    private int ID;
    private String type;
    private double size;
    //private BufferedImage image;
    private Clothes next; 

    // Constructor
    public Clothes (){
        this(1, "HeadClothes", 1);
    }
    
    public Clothes(int ID, String type, double size) {
        this.ID = ID;
        this.type = type;
        this.size = size;
        this.next = null;
        //this.image = image;
    }
    
    
        // Getter and setter methods

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

//    public BufferedImage getImage() {
//        return image;
//    }
//
//    public void setImage(BufferedImage image) {
//        this.image = image;
//    }

    public Clothes getNext() {
        return next;
    }

    public void setNext(Clothes next) {
        this.next = next;
    }
    
}
