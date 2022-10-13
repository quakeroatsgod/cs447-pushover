package pushover;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import java.util.Random;

public class Powerup extends Entity{
    
    public int grid_ID;
    private String type;
    public Powerup(Grid grid_point,String texture){
        super(grid_point.getX(), grid_point.getY());
        this.grid_ID=grid_point.getID();
        this.type=texture;
        grid_point.setEntity(texture);
        switch(texture){
            case "Speed-powerup":
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.SPEED_POWERUP_RES));
                break;
            case "Freeze-powerup":
                addImageWithBoundingBox(ResourceManager.getImage(Pushover.FREEZE_POWERUP_RES));
                break;
        }
    }

    /**
     * Applies a powerup timer and the type of power to each enemy and the player.
     * @param pushover
     */
    public void applyPowerup(Pushover pushover){
        Random rng = new Random();
        switch(this.type){
            case "Speed-powerup":
                int time = ((rng.nextInt() % 10) + 15) * 1000;
                pushover.player.powerup_timer=time;
                pushover.player.powerup_type=this.type;
                for(Enemy enemy : pushover.enemies) {
                    enemy.powerup_timer=time;
                    enemy.powerup_type=type;
                }
                break;
            case "Freeze-powerup":
                break;
        }
    }
}
