package com.example.pong_apart.gameobject;

import android.content.Context;
import android.graphics.Canvas;

import androidx.core.content.ContextCompat;

import com.example.pong_apart.Game;
import com.example.pong_apart.GameLoop;
import com.example.pong_apart.Utils;

/**
 * Player is the main character of the game. The user can control
 * player with a touch joystick. Player is a class extension of
 * Circle which is an extension of GameObject
 */
public class Player extends Circle{

    public static final double SPEED_PIXEL_PER_SECOND = 400.0;
    private static final double MAX_SPEED = SPEED_PIXEL_PER_SECOND / GameLoop.MAX_UPS;
    private final Game game;

    public Player(Context context, Game game, double positionX, double positionY, double size)  {
        super(context, ContextCompat.getColor(context, R.color.player), positionX, positionY, size);
        this.game = game;
    }
    @Override
    public void update() {
        //update velocity based on teh actuator of the joystick
        velocityX = game.getJoyStickActuatorX() * MAX_SPEED;
        velocityY = game.getJoyStickActuatorY() * MAX_SPEED;

        //update the position
        positionX += velocityX;
        positionY += velocityY;

        // Update direction
        if (velocityX != 0 || velocityY != 0) {
            // Normalize velocity to get direction (unit vector of velocity)
            double distance = Utils.getDistanceBetweenPoints(0, 0, velocityX, velocityY);
            directionX = velocityX/distance;
            directionY = velocityY/distance;
        }
    }


    public void draw(Canvas canvas, GameDisplay gameDisplay) {
        animator.draw(
                canvas,
                gameDisplay,
                this
        );
    }
    
}
