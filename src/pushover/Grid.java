package pushover;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Grid extends Entity{
    private String entity;
    private String type;
    private int ID;
    public boolean walkable;
    public int travel_cost;
    public Grid(String texture, int x_pos, int y_pos, int ID){
        //Multiply grid's position with the initializor iterator plus a screen offset.
        super(x_pos*32+24,y_pos*32+64);
        this.entity="";
        this.ID=ID;
        this.type=texture;
        switch(texture){
            case "BORDER":
            case "WALL":
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.WALL_RES));
                this.walkable=false;
                //The walkable checker makes sure you can't walk on a wall, so
                //a cost of -1 is ok.
                this.travel_cost=-1;
                break;
            //Extra cost to move on it
            case "DEEP_SNOW":
                this.walkable=true;
                this.travel_cost=2;
                break;
            //Tile with no special properties, anything can move on it.
            default:
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.BLANK_RES));
                this.walkable=true;
                this.travel_cost=1;
                break;
        }
    }
    public int getID(){
        return this.ID;
    }
    public void setEntity(String entity){
        this.entity=entity;
        if(entity.equals("Boulder")) {
            this.walkable = false;
        }
        else{
                this.walkable = true;
            }
    }
    public String getEntity(){
        return this.entity;
    }
}
