package org.example;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AssetManager {
    private BufferedImage imgTitle, imgGameOver, imgYouWin, imgRight, imgLeft, backgroundImage;
    private BufferedImage imgBear, imgPlant, imgTv, imgCat, imgToaster;
    
    private ObjectAsset catAsset;
    private List<ObjectAsset> badAssets = new ArrayList<>();

    public void loadAllAssets() {
        try {
            imgTitle = ImageIO.read(getClass().getResourceAsStream("/title.png"));
            imgGameOver  = ImageIO.read(getClass().getResourceAsStream("/game_over.png"));
            imgYouWin = ImageIO.read(getClass().getResourceAsStream("/you_win.png"));
            imgRight = ImageIO.read(getClass().getResourceAsStream("/pompiere_destra.png"));
            imgLeft  = ImageIO.read(getClass().getResourceAsStream("/pompiere_sinistra.png"));
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/sfondo_palazzo.png"));

            imgBear = ImageIO.read(getClass().getResourceAsStream("/bear.png"));
            imgPlant = ImageIO.read(getClass().getResourceAsStream("/plant.png"));
            imgTv = ImageIO.read(getClass().getResourceAsStream("/tv.png"));
            imgCat = ImageIO.read(getClass().getResourceAsStream("/cat.png"));
            imgToaster = ImageIO.read(getClass().getResourceAsStream("/toaster.png"));

            catAsset = new ObjectAsset(imgCat, "cat");

            badAssets.add(new ObjectAsset(imgBear, "bear"));
            badAssets.add(new ObjectAsset(imgPlant, "plant"));
            badAssets.add(new ObjectAsset(imgTv, "tv"));
            badAssets.add(new ObjectAsset(imgToaster, "toaster"));

        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Errore nel caricamento delle immagini: " + e.getMessage());
        }
    }

    public BufferedImage getImgRight() { return imgRight; }
    public BufferedImage getImgLeft() { return imgLeft; }
    public BufferedImage getBackgroundImage() { return backgroundImage; }
    public ObjectAsset getCatAsset() { return catAsset; }
    public List<ObjectAsset> getBadAssets() { return badAssets; }
    // --- NUOVI GETTER PER SCHERMATE STATO ---
    public BufferedImage getImgTitle() { return imgTitle; }
    public BufferedImage getImgGameOver() { return imgGameOver; }
    public BufferedImage getImgYouWin() { return imgYouWin; }
}