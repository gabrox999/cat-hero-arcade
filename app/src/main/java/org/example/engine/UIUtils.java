package org.example.engine;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class UIUtils {
    
    // Metodo universale per l'HUD (Punteggio, Record, ecc.)
    public static void drawHUDText(Graphics2D g2d, String label, int value, int x, int y) {
        g2d.setFont(new Font("Arial", Font.BOLD, 22));
        String text = label + ": " + value;
        
        // Ombra nera
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, x + 2, y + 2);
        
        // Testo principale
        g2d.setColor(Color.YELLOW);
        g2d.drawString(text, x, y);
    }

    // Metodo universale per la scritta lampeggiante centrata
    public static void drawFlashingText(Graphics2D g2d, String text, int screenWidth, int y, long transitionTime, boolean ignoreCooldown) {
        // Se c'è un cooldown di transizione e non è ancora scaduto, non disegna la scritta
        if (!ignoreCooldown && System.currentTimeMillis() - transitionTime < 2000) {
            return;
        }

        // Effetto lampeggiante basato sul tempo (cambia ogni 500ms)
        if ((System.currentTimeMillis() / 500) % 2 == 0) {
            g2d.setFont(new Font("SansSerif", Font.BOLD, 22));
            FontMetrics metrics = g2d.getFontMetrics();
            int x = (screenWidth - metrics.stringWidth(text)) / 2;
            
            // Ombra per una perfetta leggibilità su qualsiasi sfondo o immagine
            g2d.setColor(Color.BLACK);
            g2d.drawString(text, x + 2, y + 2);
            
            // Testo principale bianco
            g2d.setColor(Color.WHITE);
            g2d.drawString(text, x, y);
        }
    }
}