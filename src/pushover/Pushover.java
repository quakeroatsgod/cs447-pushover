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
    public static final int READYUPSTATE  = 1;
    public int level=1;
    public final int ScreenWidth;
    public final int ScreenHeight;
    /*
    * Creates the Pushover game frame.
    * */
    public Pushover(String title, int width, int height){
        super(title);
        ScreenHeight = height;
        ScreenWidth = width;
        Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
    }
    @Override
    public void initStatesList(GameContainer container) throws SlickException {
        addState(new StartUpState());

        //Preload resources here

    }

    public static void main(String[] args){
        AppGameContainer app;
        try{
            app = new AppGameContainer((new Pushover("Pushover", 800, 600)));
            app.setDisplayMode(800,600,false);
            Entity.antiAliasing=false;
            app.setVSync(true);
            app.start();
        } catch(SlickException e){
            e.printStackTrace();
        }
    }
}
