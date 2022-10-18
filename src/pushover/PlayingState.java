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
import java.util.Stack;

public class PlayingState extends BasicGameState {
    private boolean highlight_flag=false;
    @Override
    public int getID() {
        return Pushover.PLAYINGSTATE;
    }

    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        Pushover pushover = (Pushover)game;
    }

    /**
	 * This is called when the state is entered.
	 * @param container
	 * @param game
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
        highlight_flag=false;
        Pushover pushover = (Pushover)game;
        initLevel(pushover, pushover.level);

        pushover.player = new Player(pushover.grid.get(283));
        pushover.boulder = new Boulder(pushover.grid.get(285));
        pushover.enemies = new ArrayList<Enemy>(5);
        pushover.powerups = new ArrayList<Powerup>(2);
        
        // pushover.enemies.add(new Enemy(pushover.grid.get(44)));
        // pushover.enemies.add(new Enemy(pushover.grid.get(215)));
        // pushover.enemies.add(new Enemy(pushover.grid.get(264)));
        // pushover.enemies.add(new Enemy(pushover.grid.get(344)));
        pushover.enemies.add(new Enemy(pushover.grid.get(164)));
        pushover.powerups.add(new Powerup(pushover.grid.get(234), "Speed-powerup"));
        pushover.powerups.add(new Powerup(pushover.grid.get(214), "Freeze-powerup"));


    }
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        Pushover pushover = (Pushover)game;
        for(Grid grid_cell : pushover.grid)  grid_cell.render(g);
        pushover.player.render(g);
        pushover.boulder.render(g);
        for(Enemy enemy : pushover.enemies) enemy.render(g);
        for(Powerup powerup : pushover.powerups) powerup.render(g);
        g.drawString("Lives Left: "+pushover.lives_left, 100,10);
        g.drawString("Enemies left: "+pushover.enemy_count, 250,10);
        g.drawString("Level: "+pushover.level, 400,10);
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        Pushover pushover = (Pushover)game;
        Input input = container.getInput();
        checkInput(input, pushover);
        //If all enemies are destroyed, move to win state
        if(pushover.enemies.isEmpty()){
            pushover.state=2;
            pushover.enterState(Pushover.FREEZESCREENSTATE, new EmptyTransition(), new EmptyTransition());
        }
        //If there are still enemies left remaining
        else{
            for (Iterator<Enemy> en_iter = pushover.enemies.iterator(); en_iter.hasNext();) 
                if (en_iter.next().grid_ID == pushover.boulder.grid_ID){
                    en_iter.remove();
                    pushover.enemy_count--;
                }
            for(Enemy enemy : pushover.enemies)   {
                enemy.timeUpdate(pushover, delta);
                //Unhighlight any path tiles in order to prevent the same tile being lit up multiple times in a row.
                Stack<Integer> move_order= (Stack<Integer>)enemy.move_order.clone();
                while(!move_order.empty()){
                    Grid grid_point = pushover.grid.get(move_order.pop());
                    grid_point.unhighlight(pushover,highlight_flag);
                }
                //Move enemy if the movement timer is up
                if(enemy.getRemainingTime() <= 0) enemy.moveUpdate(pushover);
                //If an enemy is on the same tile as the player, move to game over.
                
                if(enemy.grid_ID==pushover.player.grid_ID)  {
                    pushover.lives_left--;
                    //Restart level if there are still lives left. Enter 
                    pushover.state = pushover.lives_left == 0 ? 1 : 3;
                    pushover.enterState(Pushover.FREEZESCREENSTATE, new EmptyTransition(), new EmptyTransition());
                }
                
            }
        }
        //Apply powerup and remove it from the game if the player steps on it
        for(Powerup powerup : pushover.powerups)    if (powerup.grid_ID == pushover.player.grid_ID) powerup.applyPowerup(pushover);
        for (Iterator<Powerup> pow_iter = pushover.powerups.iterator(); pow_iter.hasNext();) {
            if (pow_iter.next().grid_ID == pushover.player.grid_ID){
                pow_iter.remove();
            }
        }
        //I know this looks weird... unhighlight, and then re highlight the tiles? Well, this is
        //So that the highlight is only drawn once per tile. There's no method to check if a tile already has some texture
        // for(Grid grid_point : pushover.grid) grid_point.unhighlight(pushover,highlight_flag);
        //Highlight grid tiles
        if(highlight_flag){
            for(Enemy enemy : pushover.enemies)   {
                Stack<Integer> move_order= (Stack<Integer>)enemy.move_order.clone();
                while(!move_order.empty()){
                    Grid grid_point = pushover.grid.get(move_order.pop());
                    grid_point.highlight();
                }
            }
        }
        pushover.boulder.update(pushover,delta);
        int bol_ID = pushover.boulder.grid_ID;
        //If the boulder is stuck in a corner of walls, the player loses a life (since the
        //boulder can never get unstuck)
        if( (!pushover.grid.get(bol_ID-1).walkable && !pushover.grid.get(bol_ID+20).walkable)
        || (!pushover.grid.get(bol_ID-1).walkable && !pushover.grid.get(bol_ID-20).walkable)
        || (!pushover.grid.get(bol_ID+1).walkable && !pushover.grid.get(bol_ID+20).walkable)
        || (!pushover.grid.get(bol_ID+1).walkable && !pushover.grid.get(bol_ID-20).walkable)){
            pushover.lives_left--;
            //Restart level if there are still lives left. Enter 
            pushover.state = pushover.lives_left == 0 ? 1 : 3;
            pushover.enterState(Pushover.FREEZESCREENSTATE, new EmptyTransition(), new EmptyTransition());
        }
        pushover.player.update(pushover,delta);
    }

    /**
     * Checks user input from keyboard and determines what to do
     * @param input
     * @param pushover
     */
    private void checkInput(Input input, Pushover pushover){
        //Player moves left
        if(input.isKeyDown(Input.KEY_A)){
            //If the player is still frozen from moving the boulder
            if(pushover.player.getRemainingTime() <= 0){
                //Move boulder to left if it's there
                if(pushover.grid.get(pushover.player.grid_ID-20).getEntity().equals("Boulder")){
                    pushover.player.pushBoulder(0);
                    pushover.boulder.move((pushover.grid.get(pushover.boulder.grid_ID-20)),pushover.grid.get(pushover.boulder.grid_ID),0);
                }
                else    pushover.player.move((pushover.grid.get(pushover.player.grid_ID-20)),pushover.grid.get(pushover.player.grid_ID),0);
                return;
            }
        }
        //Player moves Right
        if(input.isKeyDown(Input.KEY_D)){
            //If the player is still frozen from moving the boulder
            if(pushover.player.getRemainingTime() <= 0){
                //Move boulder to right if it's there
                if(pushover.grid.get(pushover.player.grid_ID+20).getEntity().equals("Boulder")){
                    pushover.player.pushBoulder(1);
                    pushover.boulder.move((pushover.grid.get(pushover.boulder.grid_ID+20)),pushover.grid.get(pushover.boulder.grid_ID),1);
                }
                else    pushover.player.move((pushover.grid.get(pushover.player.grid_ID+20)),pushover.grid.get(pushover.player.grid_ID),1);
                return;
            }
        }   
        //Player moves Down
        if(input.isKeyDown(Input.KEY_S)){
            //If the player is still frozen from moving the boulder
            if(pushover.player.getRemainingTime() <= 0){
                //Move boulder to down if it's there
                if(pushover.grid.get(pushover.player.grid_ID+1).getEntity().equals("Boulder")){
                    pushover.player.pushBoulder(2);
                    pushover.boulder.move((pushover.grid.get(pushover.boulder.grid_ID+1)),pushover.grid.get(pushover.boulder.grid_ID),2);
                }
                else    pushover.player.move((pushover.grid.get(pushover.player.grid_ID+1)),pushover.grid.get(pushover.player.grid_ID),2);
                return;
            }
        }
        //Player moves Up
        if(input.isKeyDown(Input.KEY_W)){
            //If the player is still frozen from moving the boulder
            if(pushover.player.getRemainingTime() <= 0){
                //Move boulder to up if it's there
                if(pushover.grid.get(pushover.player.grid_ID-1).getEntity().equals("Boulder")){
                    pushover.player.pushBoulder(3);
                    pushover.boulder.move((pushover.grid.get(pushover.boulder.grid_ID-1)),pushover.grid.get(pushover.boulder.grid_ID),3);
                }
                else    pushover.player.move((pushover.grid.get(pushover.player.grid_ID-1)),pushover.grid.get(pushover.player.grid_ID),3);
                return;
            }
        }
        //------Cheat codes------------
        //Turn off enemy path highlighter
        if(input.isKeyDown(Input.KEY_O))    {
            highlight_flag=false;
            for(Grid grid_point : pushover.grid) grid_point.unhighlight(pushover, highlight_flag);
        }
        //Turn on enemy path highlighter
        if(input.isKeyDown(Input.KEY_P))    highlight_flag=true;

    }

    private void initLevel(Pushover pushover, int level){
        //Reset level by removing old grid
        pushover.grid.clear();
        Scanner sc=null;
        int ID_counter=0;
        switch(level){
            case 1: 
                try{sc = new Scanner(new File("src/pushover/res/grid-layout.txt"));}
                catch(Exception CannotOpenFile){
                    CannotOpenFile.printStackTrace();
                }
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
    }
    
}
