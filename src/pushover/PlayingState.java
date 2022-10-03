package pushover;

import java.util.Iterator;
import jig.ResourceManager;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.EmptyTransition;
import org.newdawn.slick.state.transition.HorizontalSplitTransition;
import jig.Vector;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class PlayingState extends BasicGameState {
    @Override
    public int getID() {
        return Pushover.PLAYINGSTATE;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        Pushover pushover = (Pushover)game;
        Scanner sc=null;
        try{sc = new Scanner(new File("src/pushover/res/grid-layout.txt"));}
        catch(Exception CannotOpenFile){
            CannotOpenFile.printStackTrace();
        }
        for(int x=0; x<20; x++){
            for(int y=0; y<20; y++){
                try{
                    if(sc.hasNext()){
                        pushover.grid.add(new Grid(sc.next(),x,y));
                    }
                }catch(NullPointerException e){ e.printStackTrace();}
            }
        }
        sc.close();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        Pushover pushover = (Pushover)game;
        for(Grid grid_cell : pushover.grid)  grid_cell.render(g);
        // for (Bang bang : breakout.explosions)	bang.render(g);

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        
    }
}
