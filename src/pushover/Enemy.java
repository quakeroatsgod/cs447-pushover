package pushover;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import java.util.ArrayList;
import java.util.Stack;

public class Enemy extends Entity {
    public int movement_timer;
    public int grid_ID;
    private int sprite_update_timer;
    private boolean walking;
    private String direction;
    private Vector velocity;
    public Stack<Integer> move_order;
    public int player_last_known_loc;
    public int freeze_timer;
    public boolean isFrozen;
    public int speed_timer;
    public boolean isSpeedy;

    public Enemy(Grid grid_point){
        super(grid_point.getX(),grid_point.getY());
        addImageWithBoundingBox(ResourceManager.getImage(Pushover.ENEMY_1_F_RES));
        this.movement_timer=0;
        this.walking=false;
        this.grid_ID=grid_point.getID();
        this.direction=Pushover.ENEMY_1_F_RES;
        this.velocity=new Vector(0,0);
        this.move_order = new Stack<Integer>();
        this.player_last_known_loc=-1;
        this.freeze_timer=0;
        this.speed_timer=0;
        this.isFrozen=false;
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
        if(this.isFrozen)   return false;
        //Invalid movement direction
        if(direction ==-1)  return false;
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
        if(this.movement_timer > 0)     return false;
        grid_point_old.setEntity("");
        grid_point_new.setEntity("Enemy");
        this.grid_ID = grid_point_new.getID();
        //Gets the direction from the old to the new grid 'point'
        //Deep snow tile, double speed
        if(grid_point_new.getType().equals("DEEP_SNOW")) {
            this.velocity = new Vector(dir_x * (float)(32.0f / 500.0f), dir_y * (float)(32.0f / 500.0f));
            this.movement_timer=500;
        }
        //Blank tile
        else{
            this.velocity = new Vector(dir_x * (float)(32.0f / 600.0f), dir_y * (float)(32.0f / 600.0f));
            //Set movement timer to 600 ms
            this.movement_timer=600;
        }
        //If speed powerup is applied
        if(this.speed_timer > 0){
            this.velocity = new Vector((float)(velocity.getX() * 1.2), (float)(velocity.getY() * 1.2));
            this.movement_timer/=1.2;
        }
        return true;
    }
    /**
     * Mainly updates timers wtih delta and adjust things that rely on time
     * @param pushover
     * @param delta
     */
    public void timeUpdate(Pushover pushover, int delta){
        movement_timer-=delta;
        sprite_update_timer-=delta;
        freeze_timer-=delta;
        speed_timer-=delta;
        if(freeze_timer <= 0){
            this.isFrozen=false;
            removeImage(ResourceManager.getImage(Pushover.ICE_CUBE_RES));
        }
        if(movement_timer > 0)  {
            updateSpriteWalking();
            translate(this.velocity.scale(delta));
        }
        //Else, movement timer has ended. be stationary
        else {
            // this.walking=true;
            if(!this.isFrozen)      updateSpriteWalking();
            this.setX(pushover.grid.get(this.grid_ID).getX());
            this.setY(pushover.grid.get(this.grid_ID).getY());
            this.velocity = new Vector(0.0f,0.0f);
        }
    }
    /**
     * An update method decoupled from timeUpdate. Updates
     * the movement stack, or gets a new one if needed
     * @param pushover
     */
    public void moveUpdate(Pushover pushover, boolean can_move){
        if(this.move_order.empty() || this.player_last_known_loc != pushover.player.grid_ID){
            //Get new path order stack
            this.move_order=this.pathToPlayer(pushover, pushover.grid.get(this.grid_ID),new ArrayList<Integer>(1),pushover.player.grid_ID);
            this.player_last_known_loc=pushover.player.grid_ID;
        }
        else if(this.grid_ID!=pushover.player.grid_ID){
            int direction=-1;
            if(can_move){
                int next_grid_ID=this.move_order.pop();
                //ifs in order of right, left, down, up directions
                if(next_grid_ID+20 == this.grid_ID) direction=0;
                if(next_grid_ID-20 == this.grid_ID) direction=1;
                if(next_grid_ID-1 == this.grid_ID) direction=2;
                if(next_grid_ID+1 == this.grid_ID) direction=3;
                //Checks if the movement cheat code is turned on or not
                this.move(pushover.grid.get(next_grid_ID), pushover.grid.get(this.grid_ID), direction);
            }
        }
    }
    /**
     * Part of the update loop for enemies. Calculates the path to take to the player's position using 
     * Uses 
     * @param pushover
     * @param start_grid_node
     * @param closed_list Nodes that have already been traveled to
     * @param player_grid_ID The grid tile the player is on
     */
    public Stack<Integer> pathToPlayer(Pushover pushover, Grid start_grid_node, ArrayList<Integer> closed_list, int player_grid_ID){
        Stack<Integer> new_order = new Stack<Integer>();
        Grid cheapest_node=new Grid(-1);
        ArrayList<Integer> grid_paths = new ArrayList<Integer>(4);
        //We can just skip the traversal if we are already at the player tile
        if(start_grid_node.getID()==player_grid_ID){
            new_order.push(start_grid_node.getID());
            return new_order;
        }
        else{
            // //Left,right,up,down paths
            grid_paths.add(start_grid_node.getID()-20);
            grid_paths.add(start_grid_node.getID()+20);
            grid_paths.add(start_grid_node.getID()-1);
            grid_paths.add(start_grid_node.getID()+1);
            for (int path : grid_paths){
                if(pushover.grid.get(path).walkable ){
                    float heuristic=getHeuristic(pushover.grid.get(path), pushover.grid.get(player_grid_ID));
                    if((pushover.grid.get(path).travel_cost + heuristic < cheapest_node.travel_cost)
                    && !closed_list.contains(path)){
                        cheapest_node.travel_cost=pushover.grid.get(path).travel_cost + heuristic;
                        cheapest_node.setID(path);
                    }
                }
            }
            //If somehow no path was chosen, just return the current stack as it is now
            if(cheapest_node.getID() == -1){
                return null;
            }else{
                ArrayList<Integer> closed=(ArrayList<Integer>)closed_list.clone();
                closed.add(cheapest_node.getID());
                new_order = pathToPlayer(pushover, pushover.grid.get(cheapest_node.getID()), closed, player_grid_ID);
                if(new_order == null)   return new Stack<Integer>();
                new_order.push(start_grid_node.getID());
            }
        }
        return new_order;
    }
    /**
     * Calculates heuristic distance from enemy to player
     * @param enemy_grid
     * @param player_grid
     * @return heuristic distance in float
     */
    private float getHeuristic(Grid enemy_grid, Grid player_grid){
        float a = player_grid.getX() - enemy_grid.getX();
        float b = player_grid.getY() - enemy_grid.getY();
        return (float) Math.sqrt((a*a) + (b*b));
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
        if(this.speed_timer > 0)  this.sprite_update_timer/=1.2;
        if(this.isFrozen)  addImageWithBoundingBox(ResourceManager.getImage(Pushover.ICE_CUBE_RES));
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
        removeImage(ResourceManager.getImage(Pushover.ICE_CUBE_RES));
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
    /** 
     * Adds the ice cube texture to an enemy's sprite
     */
    public void freezeEnemy(int duration){
        addImageWithBoundingBox(ResourceManager.getImage(Pushover.ICE_CUBE_RES));
        this.freeze_timer=duration;
        this.isFrozen=true; 
    }
}
