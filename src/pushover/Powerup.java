package pushover;
import jig.Entity;
import jig.ResourceManager;
import jig.Vector;
import java.util.Random;
import java.io.*;

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
        int time=0;
        switch(this.type){
            case "Speed-powerup":
                //Duration is 15-25 seconds
                time = Math.abs(((rng.nextInt() % 11)) * 1000) + 15000;
                pushover.player.powerup_timer=time;
                pushover.player.powerup_type=this.type;
                for(Enemy enemy : pushover.enemies) {
                    enemy.speed_timer=time;
                }
                break;
            case "Freeze-powerup":
                //Duration is 3-7 seconds
                time = Math.abs(((rng.nextInt() % 5)) * 1000) + 3000;
                for(Enemy enemy : pushover.enemies) {
                    enemy.freezeEnemy(time);
                }
                System.out.println(""+time);

                break;
        }
        
    }

    /**
     * Applies a powerup timer and the type of power to each enemy and the player.
     * Static method that is used in cheat codes
     * @param pushover
     */
    public static void applyPowerup(Pushover pushover, String powerup_type){
        Random rng = new Random();
        int time=0;
        switch(powerup_type){
            case "Speed-powerup":
                //Duration is 15-25 seconds
                time = Math.abs(((rng.nextInt() % 11)) * 1000) + 15000;
                pushover.player.powerup_timer=time;
                pushover.player.powerup_type=powerup_type;
                for(Enemy enemy : pushover.enemies) {
                    enemy.speed_timer=time;
                }
                break;
            case "Freeze-powerup":
                //Duration is 3-7 seconds
                time = Math.abs(((rng.nextInt() % 5)) * 1000) + 3000;
                System.out.println(time);
                for(Enemy enemy : pushover.enemies) {
                    enemy.freezeEnemy(time);
                }
                break;
        }
    }

    public String getType(){
        return this.type;
    }
}
