package pushover;
import java.util.ArrayList;
import java.util.ResourceBundle;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class Pushover extends StateBasedGame {
    public static final int STARTUPSTATE = 0;
    public static final int PLAYINGSTATE  = 1;
    public int level=1;
    public final int ScreenWidth;
    public final int ScreenHeight;
    public ArrayList<Grid> grid;
    public Player player;
    //TODO maybe 2 boulders instead of 1
    public Boulder boulder;
    //Resource strings
    public static final String STARTUP_SCREEN_RES = "pushover/res/startup.png";
    public static final String WALL_RES = "pushover/res/wall.png";
    public static final String BLANK_RES = "pushover/res/blank-1.png";
    public static final String PLAYER_F_RES = "pushover/res/player/player-forward.png";
    //If there is an "M", it's the "moving" texture
    public static final String PLAYER_FM_RES = "pushover/res/player/player-forward-moving.png";
    public static final String PLAYER_R_RES = "pushover/res/player/player-right.png";
    public static final String PLAYER_RM_RES = "pushover/res/player/player-right-moving.png";
    public static final String PLAYER_B_RES = "pushover/res/player/player-back.png";
    public static final String PLAYER_BM_RES = "pushover/res/player/player-back-moving.png";
    public static final String PLAYER_L_RES = "pushover/res/player/player-left.png";
    public static final String PLAYER_LM_RES = "pushover/res/player/player-left-moving.png";
    public static final String SNOWBALL_RES = "pushover/res/snowball.png";
    
    /*
    * Creates the Pushover game frame.
    */
    public Pushover(String title, int width, int height){
        super(title);
        ScreenHeight = height;
        ScreenWidth = width;
        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
        grid = new ArrayList<Grid>(50);
    }
    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new StartUpState());
        addState(new PlayingState());

        //Preload resources here
        ResourceManager.loadImage(STARTUP_SCREEN_RES);
        ResourceManager.loadImage(WALL_RES);
        ResourceManager.loadImage(BLANK_RES);
        ResourceManager.loadImage(PLAYER_L_RES);
        ResourceManager.loadImage(PLAYER_LM_RES);
        ResourceManager.loadImage(PLAYER_R_RES);
        ResourceManager.loadImage(PLAYER_RM_RES);
        ResourceManager.loadImage(PLAYER_B_RES);
        ResourceManager.loadImage(PLAYER_BM_RES);
        ResourceManager.loadImage(PLAYER_F_RES);
        ResourceManager.loadImage(PLAYER_FM_RES);
        ResourceManager.loadImage(SNOWBALL_RES);
    }

    public static void main(String[] args){
        AppGameContainer app;
        try{
            //512 +20 buffer space
            app = new AppGameContainer((new Pushover("Pushover", 660, 692)));
            app.setDisplayMode(660,692,false);
            Entity.antiAliasing=false;
            app.setVSync(true);
            app.start();
        } catch(SlickException e){
            e.printStackTrace();
        }
    }
}
