package pushover;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
public class Boulder extends Entity {
    public int grid_ID;
    private int movement_timer;
    private Vector velocity;

    public Boulder(Grid grid_point){
        super(grid_point.getX(), grid_point.getY());
        this.grid_ID=grid_point.getID();
        this.movement_timer=0;
        this.velocity=new Vector(0,0);
        grid_point.setEntity("Boulder");
        addImageWithBoundingBox(ResourceManager.getImage(Pushover.SNOWBALL_RES));
    }
    /**
     * Moves the boulder in the given direction to a new grid tile
     * @param grid_point_new
     * @param grid_point_old
     * @param direction
     */
    public void move(Grid grid_point_new, Grid grid_point_old, int direction){
        float dir_x=1.0f,dir_y=1.0f;
        switch(direction){
            case 0:
                dir_x=-1.0f; dir_y=0.f;
                break;
            case 1:
                dir_x=1.0f;  dir_y=0.0f;
                break;
            case 2:
                dir_x=0.0f;  dir_y=1.0f;
                break;
            case 3:
                dir_x=0.0f;  dir_y=-1.0f;
                break;
        }
        if(!grid_point_new.walkable)     return;
        if(this.movement_timer > 0)     return;
        grid_point_old.setEntity("");
        grid_point_new.setEntity("Boulder");
        this.grid_ID = grid_point_new.getID();
        //Gets the direction from the old to the new grid 'point'
        this.velocity = new Vector(dir_x * (float)(32.0f / 150.0f), dir_y * (float)(32.0f / 150.0f));
        this.movement_timer=150;
    }

    public void update(Pushover pushover, int delta){
        movement_timer-=delta;
        if(movement_timer > 0)  {
            translate(this.velocity.scale(delta));
        }
        //Else, movement timer has ended. be stationary
        else {
            this.setX(pushover.grid.get(this.grid_ID).getX());
            this.setY(pushover.grid.get(this.grid_ID).getY());
            this.velocity = new Vector(0.0f,0.0f);
        }
    }
    
}
