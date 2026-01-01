package com.stickwar.legacy.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Asset manager for stolen stickman game assets.
 * Handles loading and managing all visual assets from the original stickman game.
 */
public class StolenAssetManager {
    private static StolenAssetManager instance;
    
    // Character assets
    public Texture stickmanTexture;
    public Texture graveTexture;
    public TextureAtlas characterAtlas;
    
    // Background assets
    public Texture[] backgrounds;
    public Texture currentBackground;
    
    // UI assets
    public Texture backButton;
    public Texture startButton;
    public Texture selectCharacter;
    public Texture selectSword;
    public Texture leftArrow;
    public Texture rightArrow;
    
    // Animation frames
    public Array<TextureRegion> stickmanFrames;
    public TextureRegion graveFrame;
    
    public static StolenAssetManager getInstance() {
        if (instance == null) {
            instance = new StolenAssetManager();
        }
        return instance;
    }
    
    public void loadAssets() {
        // Load character assets
        loadCharacterAssets();
        
        // Load background assets
        loadBackgroundAssets();
        
        // Load UI assets
        loadUIAssets();
        
        // Setup animations
        setupAnimations();
    }
    
    private void loadCharacterAssets() {
        stickmanTexture = new Texture(Gdx.files.internal("assets/stolen/characters/stickman.png"));
        graveTexture = new Texture(Gdx.files.internal("assets/stolen/characters/grave.png"));
        
        // Create texture atlas for character animations
        characterAtlas = new TextureAtlas();
        // TODO: Split stickman.png into individual frames
    }
    
    private void loadBackgroundAssets() {
        backgrounds = new Texture[6];
        backgrounds[0] = new Texture(Gdx.files.internal("assets/stolen/backgrounds/Background001.png"));
        backgrounds[1] = new Texture(Gdx.files.internal("assets/stolen/backgrounds/Background002.png"));
        backgrounds[2] = new Texture(Gdx.files.internal("assets/stolen/backgrounds/background.png"));
        backgrounds[3] = new Texture(Gdx.files.internal("assets/stolen/backgrounds/background2.png"));
        backgrounds[4] = new Texture(Gdx.files.internal("assets/stolen/backgrounds/background_image1.png"));
        backgrounds[5] = new Texture(Gdx.files.internal("assets/stolen/backgrounds/bg2.png"));
        
        // Set default background
        currentBackground = backgrounds[0];
    }
    
    private void loadUIAssets() {
        backButton = new Texture(Gdx.files.internal("assets/stolen/ui/backMenuButton1.png"));
        startButton = new Texture(Gdx.files.internal("assets/stolen/ui/startButton1.png"));
        selectCharacter = new Texture(Gdx.files.internal("assets/stolen/ui/selectCharacter.png"));
        selectSword = new Texture(Gdx.files.internal("assets/stolen/ui/selectSword.png"));
        leftArrow = new Texture(Gdx.files.internal("assets/stolen/ui/leftarrow.png"));
        rightArrow = new Texture(Gdx.files.internal("assets/stolen/ui/rightarrow.png"));
    }
    
    private void setupAnimations() {
        stickmanFrames = new Array<>();
        
        // TODO: Extract animation frames from stickman.png
        // For now, use the whole image as a single frame
        stickmanFrames.add(new TextureRegion(stickmanTexture));
        
        graveFrame = new TextureRegion(graveTexture);
    }
    
    public void setBackground(int index) {
        if (index >= 0 && index < backgrounds.length) {
            currentBackground = backgrounds[index];
        }
    }
    
    public TextureRegion getStickmanFrame(int index) {
        if (index >= 0 && index < stickmanFrames.size) {
            return stickmanFrames.get(index);
        }
        return stickmanFrames.first();
    }
    
    public void dispose() {
        // Dispose character assets
        if (stickmanTexture != null) stickmanTexture.dispose();
        if (graveTexture != null) graveTexture.dispose();
        if (characterAtlas != null) characterAtlas.dispose();
        
        // Dispose background assets
        for (Texture background : backgrounds) {
            if (background != null) background.dispose();
        }
        
        // Dispose UI assets
        if (backButton != null) backButton.dispose();
        if (startButton != null) startButton.dispose();
        if (selectCharacter != null) selectCharacter.dispose();
        if (selectSword != null) selectSword.dispose();
        if (leftArrow != null) leftArrow.dispose();
        if (rightArrow != null) rightArrow.dispose();
    }
}
