package com.example.pong_apart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.net.wifi.aware.DiscoverySession;
import android.util.DisplayMetrics;
import android.util.Log;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ac.robinson.bettertogether.api.messaging.BroadcastMessage;

/**
 * Game manages all object in the game and is responsible for updating all states and render
 * all objects to screen
 */

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final TileMap tilemap;
    private final Player player;
    private GameLoop gameLoop;
    private List<Enemy> enemyList = new ArrayList<>();
    private List<Spell>spellList = new ArrayList<>();
 	private int numberOfSpellsToCast = 0;
    private GameOver gameOver;
    private Performance performance;
    private GameDisplay gameDisplay;
    private double actuatorX = 0.0;
    private double actuatorY = 0.0;
    private MainActivity context;

    public Game(Context context) {
        super(context);
        this.context = (MainActivity) context;
        //Get SurfaceHolder and add callback
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this, surfaceHolder);
        // Initialize game panels
        performance = new Performance(context, gameLoop);
        gameOver = new GameOver(context);

        //initialise new player
        SpriteSheet spriteSheet = new SpriteSheet(context);

        Sprite[] spriteArray = new Sprite[3];
        spriteArray[0] = new Sprite(spriteSheet, new Rect(0*64, 0, 1*64, 64));
        spriteArray[1] = new Sprite(spriteSheet, new Rect(1*64, 0, 2*64, 64));
        spriteArray[2] = new Sprite(spriteSheet, new Rect(2*64, 0, 3*64, 64));
        Animator animator = new Animator(spriteArray);
        player = new Player(context, this, 2*500, 500, 32, animator);


        // Initialize display and center it around the player
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        gameDisplay = new GameDisplay(displayMetrics.widthPixels, displayMetrics.heightPixels, player);
        // Initialize Tilemap
        tilemap = new TileMap(spriteSheet);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        Log.d("Game.java", "surfaceCreated()");
        if (gameLoop.getState().equals(Thread.State.TERMINATED)) {
            gameLoop = new GameLoop(this, surfaceHolder);
        }
        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d("Game.java", "surfaceChanged()");
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        Log.d("Game.java", "surfaceDestroyed()");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        tilemap.draw(canvas, gameDisplay);
        BroadcastMessage spellMessage = new BroadcastMessage(MessageType.JOYSTICK_DRAW, null);
        context.sendMessage(spellMessage);
        // Draw player
        player.draw(canvas, gameDisplay);
        for(Enemy enemy: enemyList) {
            enemy.draw(canvas, gameDisplay);
        }
        for(Spell spell: spellList) {
            spell.draw(canvas, gameDisplay);
        }

        // Draw game panels
        performance.draw(canvas);

        // Draw Game over if the player is dead
        if (player.getHealthPoint() <= 0) {
            gameOver.draw(canvas);
        }
    }

    public void update() {

        // Stop updating the game if the player is dead
       if (player.getHealthPoint() <= 0) {
            return;
        }

        player.update();
        BroadcastMessage spellMessage = new BroadcastMessage(MessageType.JOYSTICK_UPDATE, null);
        context.sendMessage(spellMessage);
        if(Enemy.isReadySpawn()) {
            enemyList.add(new Enemy(getContext(), player));
        }

        // Update states of all enemies
        for (Enemy enemy : enemyList) {
            enemy.update();
        }

        // Update states of all spells
        while (numberOfSpellsToCast > 0) {
            spellList.add(new Spell(getContext(), player));
            numberOfSpellsToCast --;
        }
        for (Spell spell : spellList) {
            spell.update();
        }

        // Iterate through enemyList and Check for collision between each enemy and the player and
        // spells in spellList.
        Iterator<Enemy> iteratorEnemy = enemyList.iterator();
        while (iteratorEnemy.hasNext()) {
            Circle enemy = iteratorEnemy.next();
            if (Circle.isColliding(enemy, player)) {
                // Remove enemy if it collides with the player
                iteratorEnemy.remove();
                player.setHealthPoint(player.getHealthPoint() - 1);
                continue;
            }
            Iterator<Spell>iteratorSpell = spellList.iterator();
            while (iteratorSpell.hasNext()) {
                Spell spell = iteratorSpell.next();
                if(Circle.isColliding(spell, enemy)) {
                    iteratorEnemy.remove();
                    iteratorSpell.remove();
                    break;
                }
            }
        }
        // Update gameDisplay so that it's center is set to the new center of the player's
        // game coordinates
        gameDisplay.update();
    }
    public void pause() {
        gameLoop.stopLoop();
    }

    public void JoyStickCoordinates(double actuatorX, double actuatorY) {
        this.actuatorX = actuatorX;
        this.actuatorY = actuatorY;
    }
    public double getJoyStickActuatorX() {
        return actuatorX;
    }
    public double getJoyStickActuatorY() {
        return actuatorY;
    }

    public void setNumberOfSpellsToCast(int numberOfSpellsToCast) {
        this.numberOfSpellsToCast = numberOfSpellsToCast;
    }
    public int getNumberofSpellsToCast() {
        return numberOfSpellsToCast;
    }
}
