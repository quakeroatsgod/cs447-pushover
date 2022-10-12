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
import pushover.Pushover;

public class WinState extends BasicGameState {
    @Override
    public int getID() {
        return Pushover.WINSTATE;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        g.drawImage(ResourceManager.getImage(Pushover.WIN_SCREEN_RES), 0, 0);		
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();
		Pushover pushover = (Pushover)game;
		//Await user input to start the game
		if (input.isKeyDown(Input.KEY_SPACE)){ 	
            pushover.enterState(Pushover.STARTUPSTATE, new EmptyTransition(), new HorizontalSplitTransition());
    
        }
    }
}
