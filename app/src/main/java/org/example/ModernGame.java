package org.example;

import org.example.engine.GameEngine;
import org.example.engine.GamePanel;
import org.example.engine.UIUtils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModernGame implements GamePanel, GameConstants {
    private GameEngine engine;
    
    private AssetManager assetManager;
    private ScoreManager scoreManager;
    private SoundManager soundManager;
    private Player player;

    private List<FallingObject> fallingObjects = new ArrayList<>();
    private List<Particle> particles = new ArrayList<>();
    private Random random = new Random();

    private long freezeEndTime = 0;
    private int spawnCounter = 0;
    private long lastSpawnTime = 0;
    private long spawnIntervalMs = 1500;
    private boolean left, right;
    private int catsSpawned = 0;

    public ModernGame() {
        assetManager = new AssetManager();
        scoreManager = new ScoreManager(PREF_KEY_HIGH_SCORE);
        
        // --- INIEZIONE DELLA FUNZIONE DI VITTORIA PERSONALIZZATA ---
        scoreManager.setVictoryCondition(sm -> catsSpawned >= TOTAL_CATS);

        soundManager = new SoundManager();
        player = new Player();
    }

    @Override
    public void init() {
        assetManager.loadAllAssets();
        soundManager.playMusic("retrowave_music.wav");
        
        // Avvia il motore grafico e di gioco
        engine = new GameEngine(this, "Cat Hero | 80s Arcade Rescue");
        engine.start();
    }

    @Override
    public void reset() {
        scoreManager.reset();
        spawnCounter = 0;
        freezeEndTime = 0;
        catsSpawned = 0;
        fallingObjects.clear();
        particles.clear();
        player.reset();
        left = false;
        right = false;
        soundManager.playMusic("retrowave_music.wav");
    }

    @Override
    public void keyPressed(java.awt.event.KeyEvent e) {
        int key = e.getKeyCode();
        if (key == java.awt.event.KeyEvent.VK_LEFT || key == java.awt.event.KeyEvent.VK_A) {
            left = true;
        }
        if (key == java.awt.event.KeyEvent.VK_RIGHT || key == java.awt.event.KeyEvent.VK_D) {
            right = true;
        }
    }

    @Override
    public void keyReleased(java.awt.event.KeyEvent e) {
        int key = e.getKeyCode();
        if (key == java.awt.event.KeyEvent.VK_LEFT || key == java.awt.event.KeyEvent.VK_A) {
            left = false;
        }
        if (key == java.awt.event.KeyEvent.VK_RIGHT || key == java.awt.event.KeyEvent.VK_D) {
            right = false;
        }
    }

    @Override
    public void onReturnToMenu() {
        soundManager.playMusic("retrowave_music.wav");
    }

    @Override
    public void update() {
        player.update(left, right, freezeEndTime);

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSpawnTime > spawnIntervalMs) {
            if (!scoreManager.checkVictory()) {
                spawnFallingObject();
            }
            lastSpawnTime = currentTime;
        }

        // Aggiorna oggetti cadenti e collisioni
        for (int i = fallingObjects.size() - 1; i >= 0; i--) {
            FallingObject obj = fallingObjects.get(i);
            obj.update();

            if (player.getBounds().intersects(obj.getBounds())) {
                if (obj.getName().equals("cat")) {
                    scoreManager.addScore(1);
                    soundManager.playEffect("meow.wav");
                } else {
                    scoreManager.deductScore(1);
                    soundManager.playEffect("ouch.wav");
                    freezeEndTime = System.currentTimeMillis() + 500;

                    for (int p = 0; p < 20; p++) {
                        particles.add(new Particle(player.getCenterX(), player.getCenterY(), random));
                    }

                    if (scoreManager.getScore() <= -1) {
                        scoreManager.checkAndUpdateHighScore();
                        engine.setCurrentState(GameState.GAME_OVER);
                        soundManager.playMusic("game_over_music.wav");
                        break;
                    }
                }
                fallingObjects.remove(i);
                continue;
            }

            if (obj.isOffScreen()) {
                fallingObjects.remove(i);
            }
        }

        // Aggiorna particelle
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle particle = particles.get(i);
            particle.update();
            if (particle.isDead()) particles.remove(i);
        }

        // Controllo della Condizione di Vittoria
        if (scoreManager.checkVictory() && fallingObjects.isEmpty() && engine.getCurrentState() == GameState.PLAYING) {
            scoreManager.checkAndUpdateHighScore();
            engine.setCurrentState(GameState.VICTORY);
            soundManager.playMusic("victory_music.wav");
        }
    }

    private void spawnFallingObject() {
        List<ObjectAsset> badAssets = assetManager.getBadAssets();
        ObjectAsset catAsset = assetManager.getCatAsset();
        if (badAssets.isEmpty() || catAsset == null) return;

        double[] fixedX = { 100, 211, 322, 433, 544, 655, 766, 877, 988, 1100 };
        int bonusCount = Math.min(5, scoreManager.getScore() / 3);
        int objectsToSpawn = Math.min(2 + random.nextInt(2 + bonusCount), fixedX.length);

        List<Integer> availableLanes = new ArrayList<>();
        for (int i = 0; i < fixedX.length; i++) availableLanes.add(i);
        java.util.Collections.shuffle(availableLanes, random);

        for (int i = 0; i < objectsToSpawn; i++) {
            if (catsSpawned >= TOTAL_CATS) break;

            ObjectAsset asset;
            spawnCounter++;

            if (spawnCounter >= 5) {
                asset = catAsset;
                spawnCounter = 0;
                catsSpawned++;
            } else {
                asset = badAssets.get(random.nextInt(badAssets.size()));
            }

            fallingObjects.add(new FallingObject(fixedX[availableLanes.get(i)], 50, asset, scoreManager.getScore(), random));
        }

        spawnIntervalMs = Math.max(700, 1600 - (scoreManager.getScore() * 50));
    }

    @Override
    public void render(Graphics2D g2d) {
        GameState state = engine.getCurrentState();

        if (state == GameState.MENU) {
            BufferedImage title = assetManager.getImgTitle();
            if (title != null) {
                g2d.drawImage(title, 0, 0, WWIDTH, WHEIGHT, null);
            } else {
                g2d.setColor(Color.BLACK);
                g2d.fillRect(0, 0, WWIDTH, WHEIGHT);
            }
            renderPressAnyKey(g2d);
        } else {
            BufferedImage bg = assetManager.getBackgroundImage();
            if (bg != null) {
                g2d.drawImage(bg, 0, 0, WWIDTH, WHEIGHT, null);
            } else {
                g2d.setColor(Color.DARK_GRAY);
                g2d.fillRect(0, 0, WWIDTH, WHEIGHT);
            }

            for (FallingObject obj : new ArrayList<>(fallingObjects)) {
                obj.render(g2d);
            }
            player.render(g2d, assetManager.getImgRight(), assetManager.getImgLeft());

            for (Particle particle : new ArrayList<>(particles)) {
                particle.render(g2d);
            }

            // HUD Punteggio e Record tramite UIUtils
            UIUtils.drawHUDText(g2d, "PUNTEGGIO", scoreManager.getScore(), 25, 45);
            UIUtils.drawHUDText(g2d, "RECORD", scoreManager.getHighScore(), 25, 75);

            if (state == GameState.GAME_OVER) {
                BufferedImage gameOver = assetManager.getImgGameOver();
                if (gameOver != null) g2d.drawImage(gameOver, 0, 0, WWIDTH, WHEIGHT, null);
                renderPressAnyKey(g2d);
            } else if (state == GameState.VICTORY) {
                BufferedImage youWin = assetManager.getImgYouWin();
                if (youWin != null) g2d.drawImage(youWin, 0, 0, WWIDTH, WHEIGHT, null);
                renderPressAnyKey(g2d);
            }
        }
    }

    private void renderPressAnyKey(Graphics2D g2d) {
        GameState state = engine.getCurrentState();
        int targetY = WHEIGHT - 50;
        if (state == GameState.MENU) {
            UIUtils.drawFlashingText(g2d, "PRESS ANY KEY TO START", WWIDTH, targetY, 0, true);
        } else {
            UIUtils.drawFlashingText(g2d, "PRESS ANY KEY TO RESTART", WWIDTH, targetY, engine.getStateTransitionTime(), false);
        }
    }
    public static void main(String[] args) {
        new ModernGame().init();
    }
}