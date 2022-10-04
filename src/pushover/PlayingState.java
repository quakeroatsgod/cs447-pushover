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
        int ID_counter=0;
        for(int x=0; x<20; x++){
            for(int y=0; y<20; y++){
                try{
                    if(sc.hasNext()){
                        pushover.grid.add(new Grid(sc.next(),x,y,ID_counter++));
                    }
                }catch(NullPointerException e){ e.printStackTrace();}
            }
        }
        sc.close();
    }

    /**
	 * This is called when the state is entered.
	 * @param container
	 * @param game
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
        Pushover pushover = (Pushover)game;
        pushover.player = new Player(pushover.grid.get(283));
    }
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        Pushover pushover = (Pushover)game;
        for(Grid grid_cell : pushover.grid)  grid_cell.render(g);
        pushover.player.render(g);

    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Pushover pushover = (Pushover)game;
        Input input = container.getInput();
        checkInput(input, pushover);
        pushover.player.update(pushover,delta);
    }
    private void checkInput(Input input, Pushover pushover){
        //Player moves left
        if(input.isKeyDown(Input.KEY_A)){
            pushover.player.move((pushover.grid.get(pushover.player.grid_ID-20)),pushover.grid.get(pushover.player.grid_ID),0);
            return;
        }
        //Player moves Right
        if(input.isKeyDown(Input.KEY_D)){
            pushover.player.move((pushover.grid.get(pushover.player.grid_ID+20)),pushover.grid.get(pushover.player.grid_ID),1);
            return;
        }   
        //Player moves Down
        if(input.isKeyDown(Input.KEY_S)){
            pushover.player.move((pushover.grid.get(pushover.player.grid_ID+1)),pushover.grid.get(pushover.player.grid_ID),2);
            return;
        }
        //Player moves Up
        if(input.isKeyDown(Input.KEY_W)){
            pushover.player.move((pushover.grid.get(pushover.player.grid_ID-1)),pushover.grid.get(pushover.player.grid_ID),3);
            return;
        }
    }

}
