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

    //Resource strings
    public static final String STARTUP_SCREEN_RES = "pushover/res/startup.png";
    
    /*
    * Creates the Pushover game frame.
    */
    public Pushover(String title, int width, int height){
        super(title);
        ScreenHeight = height;
        ScreenWidth = width;
        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
    }
    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new StartUpState());
        addState(new PlayingState());

        //Preload resources here
        ResourceManager.loadImage(STARTUP_SCREEN_RES);
    }

    public static void main(String[] args){
        AppGameContainer app;
        try{
            app = new AppGameContainer((new Pushover("Pushover", 1000, 800)));
            app.setDisplayMode(1000,800,false);
            Entity.antiAliasing=false;
            app.setVSync(true);
            app.start();
        } catch(SlickException e){
            e.printStackTrace();
        }
    }
}
