package pushover;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
public class Enemy extends Entity {
    public int movement_timer;
    public int grid_ID;
    private int sprite_update_timer;
    private boolean walking;
    private String direction;
    private Vector velocity;
    public Enemy(Grid grid_point){
        super(grid_point.getX(),grid_point.getY());
        addImageWithBoundingBox(ResourceManager.getImage(Pushover.ENEMY_1_F_RES));
        this.movement_timer=0;
        this.walking=false;
        this.grid_ID=grid_point.getID();
        this.direction=Pushover.ENEMY_1_F_RES;
        this.velocity=new Vector(0,0);
        grid_point.setEntity("Enemy");
    }

    /**
     * Moves the enemy in the given direction to a new grid tile
     * @param grid_point_new
     * @param grid_point_old
     * @param direction
     * @returns true if successful move, false if no move made
     */
    public boolean move(Grid grid_point_new, Grid grid_point_old, int direction){
        float dir_x=1.0f,dir_y=1.0f;
        //Update enemy sprite direction
        changeEnemyDirection(direction);
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
        // if(this.pushed_boulder)         return false;
        if(this.movement_timer > 0)     return false;
        grid_point_old.setEntity("");
        grid_point_new.setEntity("Enemy");
        this.grid_ID = grid_point_new.getID();
        //Gets the direction from the old to the new grid 'point'
        this.velocity = new Vector(dir_x * (float)(32.0f / 600.0f), dir_y * (float)(32.0f / 600.0f));
        //Set movement timer to 300 ms
        this.movement_timer=600;
        return true;
    }

    public void update(Pushover pushover, int delta){
        movement_timer-=delta;
        sprite_update_timer-=delta;
        if(movement_timer > 0)  {
            updateSpriteWalking();
            //If the player walked and didn't push a boulder
            // if(!pushed_boulder)     
            translate(this.velocity.scale(delta));
        }
        //Else, movement timer has ended. be stationary
        else {
            this.walking=true;
            // this.pushed_boulder=false;
            updateSpriteWalking();
            this.setX(pushover.grid.get(this.grid_ID).getX());
            this.setY(pushover.grid.get(this.grid_ID).getY());
            this.velocity = new Vector(0.0f,0.0f);
        }
    }
    /**
     * Part of the update loop for enemies. Calculates the path to take to the player's position
     * Uses 
     * @param pushover
     * @param delta
     */
    public void pathToPlayer(Pushover pushover, int delta){
        
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
                case Pushover.ENEMY_1_LM_RES:
                case Pushover.ENEMY_1_L_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.ENEMY_1_L_RES));
                    this.direction=Pushover.ENEMY_1_L_RES;
                    break;
                case Pushover.ENEMY_1_RM_RES:
                case Pushover.ENEMY_1_R_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.ENEMY_1_R_RES));
                    this.direction=Pushover.ENEMY_1_R_RES;
                    break;
                case Pushover.ENEMY_1_FM_RES:
                case Pushover.ENEMY_1_F_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.ENEMY_1_F_RES));
                    this.direction=Pushover.ENEMY_1_F_RES;
                    break;
                case Pushover.ENEMY_1_BM_RES:
                case Pushover.ENEMY_1_B_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.ENEMY_1_B_RES));
                    this.direction=Pushover.ENEMY_1_B_RES;
                    break;
            }
            this.walking=false;
        }
        //Switch from stationary sprite to walking
        else{
            switch(this.direction){
                case Pushover.ENEMY_1_LM_RES:
                case Pushover.ENEMY_1_L_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.ENEMY_1_LM_RES));
                    this.direction=Pushover.ENEMY_1_LM_RES;
                    break;
                case Pushover.ENEMY_1_RM_RES:
                case Pushover.ENEMY_1_R_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.ENEMY_1_RM_RES));
                    this.direction=Pushover.ENEMY_1_RM_RES;
                    break;
                case Pushover.ENEMY_1_FM_RES:
                case Pushover.ENEMY_1_F_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.ENEMY_1_FM_RES));
                    this.direction=Pushover.ENEMY_1_FM_RES;
                    break;
                case Pushover.ENEMY_1_BM_RES:
                case Pushover.ENEMY_1_B_RES:
                    addImageWithBoundingBox(ResourceManager.getImage(Pushover.ENEMY_1_BM_RES));
                    this.direction=Pushover.ENEMY_1_BM_RES;
                    break;
            }
            this.walking=true;
        }
        this.sprite_update_timer=300;
    }
    /**
     * Clear any and all ENEMY sprites. A bit over kill, but it ensures smooth sprite movement
     */
    private void clearSprites(){
        removeImage(ResourceManager.getImage(Pushover.ENEMY_1_LM_RES));
        removeImage(ResourceManager.getImage(Pushover.ENEMY_1_L_RES));
        removeImage(ResourceManager.getImage(Pushover.ENEMY_1_RM_RES));
        removeImage(ResourceManager.getImage(Pushover.ENEMY_1_R_RES));
        removeImage(ResourceManager.getImage(Pushover.ENEMY_1_FM_RES));
        removeImage(ResourceManager.getImage(Pushover.ENEMY_1_F_RES));
        removeImage(ResourceManager.getImage(Pushover.ENEMY_1_BM_RES));
        removeImage(ResourceManager.getImage(Pushover.ENEMY_1_B_RES));
    }

    /**
     * Changes the enemy's sprite based on their direction they face
     * @param direction
     */
    private void changeEnemyDirection(int direction){
        removeImage(ResourceManager.getImage(this.direction));
        switch(direction){
            //Left
            case 0:
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.ENEMY_1_L_RES));
                this.direction=Pushover.ENEMY_1_L_RES;
                break;
            //Right
            case 1:
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.ENEMY_1_R_RES));
                this.direction=Pushover.ENEMY_1_R_RES;
                break;
            //Down (Forward)
            case 2:
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.ENEMY_1_F_RES));
                this.direction=Pushover.ENEMY_1_F_RES;
                break;
            //Up
            case 3:
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.ENEMY_1_B_RES));
                this.direction=Pushover.ENEMY_1_B_RES;
                break;
        }
    }
    /**
     * @return movement timer
     */
    public int getRemainingTime(){
        return movement_timer;
    }
}
