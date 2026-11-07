package org.example;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class Particle {
    private double x, y;
    private double vx, vy; // Velocità orizzontale e verticale
    private int alpha = 255; // Trasparenza iniziale (opacità)
    private int size;
    private Color color;
    private double lifeDecay;

    public Particle(double startX, double startY, Random random) {
        this.x = startX;
        this.y = startY;

        // Direzione casuale a raggiera (verso l'alto e i lati)
        double angle = random.nextDouble() * Math.PI * 2;
        double speed = 2.0 + random.nextDouble() * 4.0;
        this.vx = Math.cos(angle) * speed;
        this.vy = Math.sin(angle) * speed - 1.5; // Spinta leggera verso l'alto

        this.size = 8 + random.nextInt(8); // Dimensione stellina
        this.color = random.nextBoolean() ? Color.YELLOW : Color.ORANGE;
        this.lifeDecay = 8.0 + random.nextDouble() * 6.0; // Velocità di dissolvenza
    }

    public void update() {
        x += vx;
        y += vy;
        vy += 0.2; // Leggera gravità che tira giù le particelle
        alpha -= lifeDecay; // Riduce l'opacità gradualmente
        if (alpha < 0) alpha = 0;
    }

    public void render(Graphics2D g2d) {
        if (alpha <= 0) return;
        g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha));
        
        // Disegna una piccola stellina/rombo o cerchietto luminoso
        g2d.fillOval((int) x, (int) y, size, size);
    }

    public boolean isDead() {
        return alpha <= 0;
    }
}