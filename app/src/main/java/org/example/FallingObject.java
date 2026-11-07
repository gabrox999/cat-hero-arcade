package org.example;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

public class FallingObject implements GameConstants {
    private Vector2D pos;
    private double speedY;
    private BufferedImage image;
    private String name;
    private int width, height;
    private double rotationAngle = 0.0;
    private double rotationSpeed;

    public FallingObject(double x, double y, ObjectAsset asset, int score, Random random) {
        this.pos = new Vector2D(x, y);
        this.image = asset.image();
        this.name = asset.name();
        this.width = PLAYER_WIDTH / 2;
        this.height = PLAYER_WIDTH / 2;

        double difficultyMultiplier = 1.0 + (score * 0.08);
        this.speedY = (2.5 + random.nextDouble() * 2.0) * difficultyMultiplier;
        this.rotationSpeed = (random.nextDouble() - 0.5) * 0.06;
    }

    public void update() {
        pos = new Vector2D(pos.x(), pos.y() + speedY);
        rotationAngle += rotationSpeed;
    }

    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        double centerX = pos.x() + (width / 2.0);
        double centerY = pos.y() + (height / 2.0);

        g2d.rotate(rotationAngle, centerX, centerY);
        g2d.drawImage(image, (int) pos.x(), (int) pos.y(), width, height, null);
        g2d.dispose();
    }

    public boolean isOffScreen() {
        return pos.y() > WHEIGHT - 90;
    }

    public Rectangle getBounds() {
        return new Rectangle((int) pos.x(), (int) pos.y(), width, height);
    }

    public String getName() {
        return name;
    }
}