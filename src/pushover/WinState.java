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
    public void enter(GameContainer container, StateBasedGame game) {
		Pushover pushover = (Pushover)game;
        pushover.level++;
        if(pushover.level==3)   pushover.level=1;
    }
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {

    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		Pushover pushover = (Pushover)game;
        g.drawImage(ResourceManager.getImage(Pushover.WIN_SCREEN_RES), 0, 0);
        g.drawString("Lives Left: "+pushover.lives_left, 100,10);
        g.drawString("Enemies left: "+pushover.enemy_count, 250,10);
        g.drawString("Level: "+pushover.level, 390,10);    
        g.drawString("Score: "+pushover.score, 480,10);		
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();
		Pushover pushover = (Pushover)game;
		//Await user input to start the game
		if (input.isKeyDown(Input.KEY_SPACE)){ 	
            pushover.enterState(Pushover.PLAYINGSTATE, new EmptyTransition(), new HorizontalSplitTransition());
        }
    }
}
