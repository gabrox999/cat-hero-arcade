package org.example;

import java.util.prefs.Preferences;
import java.util.function.Predicate;

public class ScoreManager {
    private int score = 0;
    private int highScore = 0;
    private final String prefKeyHighScore;
    
    // Condizione di vittoria personalizzabile tramite funzione/lambda
    private Predicate<ScoreManager> victoryCondition;

    public ScoreManager(String prefKeyHighScore) {
        this.prefKeyHighScore = prefKeyHighScore;
        loadHighScore();
    }

    private void loadHighScore() {
        Preferences prefs = Preferences.userNodeForPackage(ScoreManager.class);
        highScore = prefs.getInt(prefKeyHighScore, 0);
    }

    public void checkAndUpdateHighScore() {
        if (score > highScore) {
            highScore = score;
            try {
                Preferences prefs = Preferences.userNodeForPackage(ScoreManager.class);
                prefs.putInt(prefKeyHighScore, highScore);
            } catch (Exception e) {
                System.out.println("Errore salvataggio high score: " + e.getMessage());
            }
        }
    }

    public void addScore(int value) { score += value; }
    public void deductScore(int value) { score -= value; }
    
    public void reset() {
        score = 0;
    }

    // Permette al gioco di iniettare la sua regola di vittoria personalizzata
    public void setVictoryCondition(Predicate<ScoreManager> victoryCondition) {
        this.victoryCondition = victoryCondition;
    }

    // Valuta la condizione di vittoria se è stata impostata
    public boolean checkVictory() {
        if (victoryCondition != null) {
            return victoryCondition.test(this);
        }
        return false;
    }

    // Getter
    public int getScore() { return score; }
    public int getHighScore() { return highScore; }
}