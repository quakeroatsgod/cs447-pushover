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

public class FreezeScreenState extends BasicGameState {
    private int timer;
    @Override
    public int getID() {
        return Pushover.FREEZESCREENSTATE;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        timer=2500;
    }
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        Pushover pushover = (Pushover)game;
        for(Grid grid_cell : pushover.grid)  grid_cell.render(g);
        pushover.player.render(g);
        pushover.boulder.render(g);
        for(Enemy enemy : pushover.enemies) enemy.render(g);
        g.drawString("Lives Left: "+pushover.lives_left, 100,10);
        g.drawString("Enemies left: "+pushover.enemy_count, 250,10);
        g.drawString("Level: "+pushover.level, 400,10);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Input input = container.getInput();
		Pushover pushover = (Pushover)game;
        int state = pushover.state;
        //Wait for game over timer to finish
        if(timer <= 0){
            //Reset state tracker
            pushover.state=0;
            //Determine which state to move to
            switch(state){
                //Gameover
                case 1: pushover.enterState(Pushover.GAMEOVERSTATE, new EmptyTransition(), new HorizontalSplitTransition());
                    break;
                //Win
                case 2: pushover.enterState(Pushover.WINSTATE, new EmptyTransition(), new HorizontalSplitTransition());
                    break;
                //Restart level
                case 3: pushover.enterState(Pushover.PLAYINGSTATE, new EmptyTransition(), new EmptyTransition());
                break;
            }
        }
        else {
            timer-=delta;
            pushover.player.update(pushover, delta);
            pushover.boulder.update(pushover, delta);
            for(Enemy enemy : pushover.enemies)   {
                enemy.timeUpdate(pushover, delta);
            }
        }
    }
}
