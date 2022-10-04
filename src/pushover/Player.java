package pushover;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
public class Player extends Entity {
    public int lives_left;
    public int grid_ID;
    private int movement_timer;
    private int sprite_update_timer;
    private String direction;
    private Boolean walking;
    private Vector velocity;

    public Player(Grid grid_point){
        super(grid_point.getX(),grid_point.getY());
        this.lives_left=3;
        this.movement_timer=0;
        this.walking=false;
        this.grid_ID=grid_point.getID();
        this.direction=Pushover.PLAYER_F_RES;
        this.velocity=new Vector(0,0);
        grid_point.setEntity(this);
        addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_F_RES));
    }
    //TODO maybe goes here?
    public void move(Grid grid_point_new, Grid grid_point_old, int direction){
        float dir_x=1.0f,dir_y=1.0f;
        //Update player sprite direction
        // removeImage(ResourceManager.getImage(this.direction));
        switch(direction){
            //Left
            case 0:
                // addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_L_RES));
                this.direction=Pushover.PLAYER_L_RES;
                dir_x=-1.0f; dir_y=0.f;
                break;
            //Right
            case 1:
                // addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_R_RES));
                this.direction=Pushover.PLAYER_R_RES;
                dir_x=1.0f;  dir_y=0.0f;
                break;
            //Down (Forward)
            case 2:
                // addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_F_RES));
                this.direction=Pushover.PLAYER_F_RES;
                dir_x=0.0f;  dir_y=1.0f;
                break;
            //Up
            case 3:
                // addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_B_RES));
                this.direction=Pushover.PLAYER_B_RES;
                dir_x=0.0f;  dir_y=-1.0f;
                break;
        }
        //If the grid point cannot be moved onto
        if(!grid_point_new.walkable)     return;
        if(this.movement_timer > 0)     return;
        grid_point_old.setEntity(null);
        grid_point_new.setEntity(this);
        this.grid_ID = grid_point_new.getID();
        //Gets the direction from the old to the new grid 'point'
        this.velocity = new Vector(dir_x * (float)(32.0f / 150.0f), dir_y * (float)(32.0f / 150.0f));
        // this.setX(grid_point_new.getX());
        // this.setY(grid_point_new.getY());
        //Set movement timer to 150 ms
        this.movement_timer=150;
    }

    public void update(Pushover pushover, int delta){
        movement_timer-=delta;
        sprite_update_timer-=delta;
        if(movement_timer > 0)  {
            updateSpriteWalking();
            translate(this.velocity.scale(delta));
        }
        //Else, movement timer has ended. be stationary
        else {
            this.walking=true;
            updateSpriteWalking();
            this.setX(pushover.grid.get(this.grid_ID).getX());
            this.setY(pushover.grid.get(this.grid_ID).getY());
            this.velocity = new Vector(0.0f,0.0f);
        }
    }

    /**
     * Updates the player sprite accordingly while walking.
     */
    private void updateSpriteWalking(){
        //Don't change sprite if timer not up yet
        if(this.sprite_update_timer > 0)    return;
        //Switch from stationary sprite to walking sprite
        // removeImage(ResourceManager.getImage(this.direction));
        clearSprites();
        //Switch from walking sprite to stationary
        if(this.walking) {
            switch(this.direction){
                case Pushover.PLAYER_LM_RES:
                case Pushover.PLAYER_L_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_L_RES));
                    this.direction=Pushover.PLAYER_L_RES;
                    break;
                case Pushover.PLAYER_RM_RES:
                case Pushover.PLAYER_R_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_R_RES));
                    this.direction=Pushover.PLAYER_R_RES;
                    break;
                case Pushover.PLAYER_FM_RES:
                case Pushover.PLAYER_F_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_F_RES));
                    this.direction=Pushover.PLAYER_F_RES;
                    break;
                case Pushover.PLAYER_BM_RES:
                case Pushover.PLAYER_B_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_B_RES));
                    this.direction=Pushover.PLAYER_B_RES;
                    break;
            }
            this.walking=false;
        }
        //Switch from stationary sprite to walking
        else{
            switch(this.direction){
                case Pushover.PLAYER_LM_RES:
                case Pushover.PLAYER_L_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_LM_RES));
                    this.direction=Pushover.PLAYER_LM_RES;
                    break;
                case Pushover.PLAYER_RM_RES:
                case Pushover.PLAYER_R_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_RM_RES));
                    this.direction=Pushover.PLAYER_RM_RES;
                    break;
                case Pushover.PLAYER_FM_RES:
                case Pushover.PLAYER_F_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_FM_RES));
                    this.direction=Pushover.PLAYER_FM_RES;
                    break;
                case Pushover.PLAYER_BM_RES:
                case Pushover.PLAYER_B_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_BM_RES));
                    this.direction=Pushover.PLAYER_BM_RES;
                    break;
            }
            this.walking=true;
        }
        this.sprite_update_timer=75;
    }
    /**
     * Clear any and all player sprites. A bit over kill, but it ensures smooth sprite movement
     */
    private void clearSprites(){
        removeImage(ResourceManager.getImage(Pushover.PLAYER_LM_RES));
        removeImage(ResourceManager.getImage(Pushover.PLAYER_L_RES));
        removeImage(ResourceManager.getImage(Pushover.PLAYER_RM_RES));
        removeImage(ResourceManager.getImage(Pushover.PLAYER_R_RES));
        removeImage(ResourceManager.getImage(Pushover.PLAYER_FM_RES));
        removeImage(ResourceManager.getImage(Pushover.PLAYER_F_RES));
        removeImage(ResourceManager.getImage(Pushover.PLAYER_BM_RES));
        removeImage(ResourceManager.getImage(Pushover.PLAYER_B_RES));
    }
}
