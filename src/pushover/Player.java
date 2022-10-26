package pushover;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
public class Player extends Entity {
    public int lives_left;
    public int grid_ID;
    private int movement_timer;
    private int sprite_update_timer;
    private boolean pushed_boulder;
    private String direction;
    private boolean walking;
    private Vector velocity;
    public int powerup_timer;
    public String powerup_type;

    public Player(Grid grid_point){
        super(grid_point.getX(),grid_point.getY());
        this.lives_left=3;
        this.movement_timer=0;
        this.walking=false;
        this.pushed_boulder=false;
        this.grid_ID=grid_point.getID();
        this.direction=Pushover.PLAYER_F_RES;
        this.velocity=new Vector(0,0);
        this.powerup_timer=0;
        this.powerup_type="";
        grid_point.setEntity("Player");
        addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_F_RES));
    }
    /**
     * Moves the player in the given direction to a new grid tile
     * @param grid_point_new
     * @param grid_point_old
     * @param direction
     * @returns true if successful move, false if no move made
     */
    public boolean move(Grid grid_point_new, Grid grid_point_old, int direction){
        float dir_x=1.0f,dir_y=1.0f;
        //Update player sprite direction
        changePlayerDirection(direction);
        switch(direction){
            //Left
            case 0:
                dir_x=-1.0f; dir_y=0.f;
                break;
            //Right
            case 1:
                dir_x=1.0f;  dir_y=0.0f;
                break;
            //Down (Forward)
            case 2:
                dir_x=0.0f;  dir_y=1.0f;
                break;
            //Up
            case 3:
                dir_x=0.0f;  dir_y=-1.0f;
                break;
        }
        //If the grid point cannot be moved onto
        if(!grid_point_new.walkable)    return false;
        if(this.pushed_boulder)         return false;
        if(this.movement_timer > 0)     return false;
        grid_point_old.setEntity("");
        grid_point_new.setEntity("Player");
        this.grid_ID = grid_point_new.getID();
        //Gets the direction from the old to the new grid 'point'
        //Deep snow tile, half the speed.
        if(grid_point_new.getType().equals("DEEP_SNOW")) {
            this.velocity = new Vector(dir_x * (float)(32.0f / 300.0f), dir_y * (float)(32.0f / 300.0f));
            this.movement_timer=300;
        }
        //Blank tile
        else{
            this.velocity = new Vector(dir_x * (float)(32.0f / 200.0f), dir_y * (float)(32.0f / 200.0f));
            //Set movement timer to 200 ms
            this.movement_timer=200;
        }
        if(this.powerup_timer > 0 && powerup_type.equals("Speed-powerup")){
            this.velocity = new Vector(velocity.getX() * 2, velocity.getY() * 2);
            this.movement_timer/=2;
        }
        return true;
    }

    public void update(Pushover pushover, int delta){
        movement_timer-=delta;
        sprite_update_timer-=delta;
        powerup_timer-=delta;
        if(movement_timer > 0)  {
            updateSpriteWalking();
            //If the player walked and didn't push a boulder
            if(!pushed_boulder)     translate(this.velocity.scale(delta));
        }
        //Else, movement timer has ended. be stationary
        else {
            this.walking=true;
            this.pushed_boulder=false;
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
        this.sprite_update_timer=100;
        if(this.powerup_timer > 0 && powerup_type.equals("Speed-powerup")) this.sprite_update_timer/=2;
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

    /**
     * Changes the player's sprite based on their direction they face
     * @param direction
     */
    private void changePlayerDirection(int direction){
        removeImage(ResourceManager.getImage(this.direction));
        switch(direction){
            //Left
            case 0:
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_L_RES));
                this.direction=Pushover.PLAYER_L_RES;
                break;
            //Right
            case 1:
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_R_RES));
                this.direction=Pushover.PLAYER_R_RES;
                break;
            //Down (Forward)
            case 2:
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_F_RES));
                this.direction=Pushover.PLAYER_F_RES;
                break;
            //Up
            case 3:
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.PLAYER_B_RES));
                this.direction=Pushover.PLAYER_B_RES;
                break;
        }
    }

    /**
     * Pushes the boulder entity, applying some features to the player
     * @param direction
     */
    public void pushBoulder(int direction){
        movement_timer=600;
        pushed_boulder=true;
        //Change player direction
        changePlayerDirection(direction);
    }
    /**
     * @return movement timer
     */
    public int getRemainingTime(){
        return movement_timer;
    }
}
