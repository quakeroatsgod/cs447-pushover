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
    public static final int WINSTATE = 2;
    public static final int GAMEOVERSTATE = 3;
    public static final int FREEZESCREENSTATE = 4;
    public int level=1;
    public int state=0;
    public final int ScreenWidth;
    public final int ScreenHeight;
    public ArrayList<Enemy> enemies;
    public ArrayList<Grid> grid;
    public ArrayList<Powerup> powerups;
    public Player player;
    public int lives_left=3;
    public int enemy_count=5;
    public Boulder boulder;
    public int score=0;
    //Resource strings
    public static final String STARTUP_SCREEN_RES = "pushover/res/startup.png";
    public static final String WALL_RES = "pushover/res/wall.png";
    public static final String BLANK_RES = "pushover/res/blank.png";
    public static final String SNOWBALL_RES = "pushover/res/snowball.png";
    public static final String WIN_SCREEN_RES = "pushover/res/win-screen.png";
    public static final String GAMEOVER_SCREEN_RES = "pushover/res/gameover-screen.png";
    public static final String DEEP_SNOW_RES = "pushover/res/deep-snow.png";
    public static final String SPEED_POWERUP_RES = "pushover/res/speed-powerup.png";
    public static final String FREEZE_POWERUP_RES = "pushover/res/freeze-powerup.png";
    public static final String ICE_CUBE_RES = "pushover/res/ice-cube.png";
    public static final String PATH_HIGHLIGHT_RES = "pushover/res/path-highlight.png";
    public static final String TREE_RES = "pushover/res/tree.png";
    //If there is an "M", it's the "moving" texture
    public static final String PLAYER_F_RES = "pushover/res/player/player-forward.png";
    public static final String PLAYER_FM_RES = "pushover/res/player/player-forward-moving.png";
    public static final String PLAYER_R_RES = "pushover/res/player/player-right.png";
    public static final String PLAYER_RM_RES = "pushover/res/player/player-right-moving.png";
    public static final String PLAYER_B_RES = "pushover/res/player/player-back.png";
    public static final String PLAYER_BM_RES = "pushover/res/player/player-back-moving.png";
    public static final String PLAYER_L_RES = "pushover/res/player/player-left.png";
    public static final String PLAYER_LM_RES = "pushover/res/player/player-left-moving.png";

    public static final String ENEMY_1_F_RES = "pushover/res/enemy/enemy-forward.png";
    public static final String ENEMY_1_FM_RES = "pushover/res/enemy/enemy-forward-moving.png";
    public static final String ENEMY_1_R_RES = "pushover/res/enemy/enemy-right.png";
    public static final String ENEMY_1_RM_RES = "pushover/res/enemy/enemy-right-moving.png";
    public static final String ENEMY_1_L_RES = "pushover/res/enemy/enemy-left.png";
    public static final String ENEMY_1_LM_RES = "pushover/res/enemy/enemy-left-moving.png";
    public static final String ENEMY_1_B_RES = "pushover/res/enemy/enemy-back.png";
    public static final String ENEMY_1_BM_RES = "pushover/res/enemy/enemy-back-moving.png";
    
    /*
    * Creates the Pushover game frame.
    */
    public Pushover(String title, int width, int height){
        super(title);
        ScreenHeight = height;
        ScreenWidth = width;
        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
        grid = new ArrayList<Grid>(50);
        enemies = new ArrayList<Enemy>(5);
        powerups = new ArrayList<Powerup>(2);
    }
    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new StartUpState());
        addState(new PlayingState());
        addState(new WinState());
        addState(new GameOverState());
        addState(new FreezeScreenState());

        //Preload resources here
        ResourceManager.loadImage(STARTUP_SCREEN_RES);
        ResourceManager.loadImage(WIN_SCREEN_RES);
        ResourceManager.loadImage(GAMEOVER_SCREEN_RES);
        ResourceManager.loadImage(DEEP_SNOW_RES);
        ResourceManager.loadImage(SPEED_POWERUP_RES);
        ResourceManager.loadImage(FREEZE_POWERUP_RES);
        ResourceManager.loadImage(ICE_CUBE_RES);
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
        ResourceManager.loadImage(ENEMY_1_F_RES);
        ResourceManager.loadImage(ENEMY_1_FM_RES);
        ResourceManager.loadImage(ENEMY_1_R_RES);
        ResourceManager.loadImage(ENEMY_1_RM_RES);
        ResourceManager.loadImage(ENEMY_1_L_RES);
        ResourceManager.loadImage(ENEMY_1_LM_RES);
        ResourceManager.loadImage(ENEMY_1_B_RES);
        ResourceManager.loadImage(ENEMY_1_BM_RES);
        ResourceManager.loadImage(PATH_HIGHLIGHT_RES);
        ResourceManager.loadImage(TREE_RES);
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
