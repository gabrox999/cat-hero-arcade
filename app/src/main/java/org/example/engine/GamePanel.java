package org.example.engine;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public interface GamePanel {
    void init();
    void update();
    void render(Graphics2D g2d);
    void reset();

    // Aggiungi questi due metodi per l'input
    void keyPressed(KeyEvent e);
    void keyReleased(KeyEvent e);

    void onReturnToMenu();
}