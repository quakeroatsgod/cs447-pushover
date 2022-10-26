package pushover;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Grid extends Entity{
    private String entity;
    private String type;
    private int ID;
    public boolean walkable;
    public float travel_cost;
    //Blank grid point to be a placeholder
    public Grid(int ID){
        this.ID=ID;
        this.travel_cost=Integer.MAX_VALUE;
    }
    //Regular grid tile in generation
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
                this.travel_cost=-1.0f;
                break;
            //Similar to the wall, but with a tree texture
            case "TREE":
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.BLANK_RES));
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.TREE_RES));
                this.walkable=false;
                this.travel_cost=-1.0f;
                break;
            //Extra cost to move on it for players, half cost for enemies
            case "DEEP_SNOW":
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.DEEP_SNOW_RES));
                this.walkable=true;
                this.travel_cost=0.0f;
                break;
            //Tile with no special properties, anything can move on it.
            default:
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.BLANK_RES));
                this.walkable=true;
                this.travel_cost=1.0f;
                break;
        }

    }
    /**
     * Removes the highlighted texture from the grid
     * @param pushover
     * @param highlight_flag
     */
    public void unhighlight(Pushover pushover, boolean highlight_flag){
        removeImage(ResourceManager.getImage(Pushover.PATH_HIGHLIGHT_RES));
    }
    
    public void highlight(){
        addImageWithBoundingBox(ResourceManager.getImage(Pushover.PATH_HIGHLIGHT_RES));
    }
    public int getID(){
        return this.ID;
    }
    public void setID(int ID){
        this.ID=ID;
    }

    /**
     * Sets Sets the entity of the grid tile
     * @param entity
     */
    public void setEntity(String entity){
        this.entity=entity;
        if(entity.equals("Boulder")) this.walkable = false;
        else    this.walkable = true;
    }

    public String getEntity(){
        return this.entity;
    }
    
    public String getType(){
        return this.type;
    }
}
