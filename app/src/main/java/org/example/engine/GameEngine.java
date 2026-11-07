package org.example.engine;

import org.example.GameConstants;
import org.example.GameState;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class GameEngine extends Canvas implements Runnable, KeyListener, GameConstants {
    private Thread gameThread;
    private volatile boolean running = false;
    private JFrame frame;

    private GamePanel currentGame;
    private GameState currentState = GameState.MENU;
    private long stateTransitionTime = 0;

    public GameEngine(GamePanel game, String windowTitle) {
        this.currentGame = game;

        Dimension size = new Dimension(WWIDTH, WHEIGHT);
        setPreferredSize(size);
        setMaximumSize(size);
        setMinimumSize(size);

        frame = new JFrame(windowTitle);
        frame.add(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        this.addKeyListener(this);
        this.setFocusable(true);
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        gameThread = new Thread(this, "GameEngine-LoopThread");
        gameThread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double targetFPS = 60.0;
        double nsPerTick = 1000000000.0 / targetFPS;
        double delta = 0;

        long timer = System.currentTimeMillis();
        int frames = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            while (delta >= 1) {
                if (currentState == GameState.PLAYING) {
                    currentGame.update();
                }
                delta--;
            }

            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                final int currentFrames = frames;
                java.awt.EventQueue.invokeLater(() -> 
                    frame.setTitle("Arcade Engine | FPS: " + currentFrames)
                );
                frames = 0;
            }
        }
        stop();
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Delegate rendering to the specific game implementation
        currentGame.render(g2d);

        g.dispose();
        bs.show();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_ESCAPE) {
            running = false;
            System.exit(0);
        }

        // 1. Se siamo nel MENU, un tasto avvia la partita
        if (currentState == GameState.MENU) {
            currentGame.reset();
            currentState = GameState.PLAYING;
            stateTransitionTime = System.currentTimeMillis(); // Registriamo il timestamp della transizione
            return;
        }

        // 2. Se siamo in GAME_OVER o VICTORY, applichiamo il COOLDOWN dei 2 secondi
        if (currentState == GameState.GAME_OVER || currentState == GameState.VICTORY) {

            // --- QUI VA IL COOLDOWN ---
            // Se sono passati meno di 2000ms (2 secondi) da quando è apparsa la schermata, ignora il tasto
            if (System.currentTimeMillis() - stateTransitionTime < 2000) {
                return;
            }

            // Se il tempo è scaduto, torniamo al MENU e avvisiamo il gioco per la musica
            currentState = GameState.MENU;
            stateTransitionTime = System.currentTimeMillis();

            currentGame.onReturnToMenu(); // Ripristina la musica retrowave
            return;
        }

        // 3. Inoltra i tasti di movimento durante il gioco (PLAYING)
        if (currentState == GameState.PLAYING) {
            currentGame.keyPressed(e);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (currentState == GameState.PLAYING) {
            currentGame.keyReleased(e); // Inoltra il rilascio del tasto per fermare il movimento
        }
    }
    @Override public void keyTyped(KeyEvent e) {}

    public GameState getCurrentState() { return currentState; }
    public void setCurrentState(GameState state) { 
        this.currentState = state; 
        this.stateTransitionTime = System.currentTimeMillis();
    }
    public long getStateTransitionTime() { return stateTransitionTime; }
}