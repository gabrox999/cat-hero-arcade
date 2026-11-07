package org.example;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Player implements GameConstants {
    private Vector2D pos;
    private double speed = 8.0;
    private boolean facingRight = true;

    public Player() {
        reset();
    }

    public void update(boolean left, boolean right, long freezeEndTime) {
        if (System.currentTimeMillis() >= freezeEndTime) {
            double x = pos.x();
            if (left) {
                x -= speed;
                facingRight = false;
            }
            if (right) {
                x += speed;
                facingRight = true;
            }
            x = Math.clamp(x, 0, WWIDTH - PLAYER_WIDTH);
            pos = new Vector2D(x, PLAYER_Y_FLOOR);
        }
    }

    public void render(Graphics g, BufferedImage imgRight, BufferedImage imgLeft) {
        BufferedImage currentImg = facingRight ? imgRight : imgLeft;
        if (currentImg != null) {
            g.drawImage(currentImg, (int) pos.x(), (int) pos.y(), PLAYER_WIDTH, PLAYER_HEIGHT, null);
        } else {
            g.setColor(java.awt.Color.RED);
            g.fillRect((int) pos.x(), (int) pos.y(), PLAYER_WIDTH, PLAYER_HEIGHT);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(
                (int) pos.x() + 30,
                (int) pos.y() + 30,
                PLAYER_WIDTH - 60,
                PLAYER_HEIGHT - 40
        );
    }

    public void reset() {
        this.pos = new Vector2D((WWIDTH / 2.0) - (PLAYER_WIDTH / 2.0), PLAYER_Y_FLOOR);
        this.facingRight = true;
    }

    public double getCenterX() {
        return pos.x() + (PLAYER_WIDTH / 2.0);
    }

    public double getCenterY() {
        return pos.y() + (PLAYER_HEIGHT / 2.0);
    }
}