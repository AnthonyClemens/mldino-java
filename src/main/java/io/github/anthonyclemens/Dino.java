package io.github.anthonyclemens;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

import com.evo.NEAT.Genome;

public class Dino {
    private final Random rand;
    private final Genome g;
    private Image image;
    private static final float X=80;
    private float y=310;
    private boolean jump;
    private boolean run;
    private static final float JUMPVELOCITY = 20;
    private int stepIndex;
    private float jumpV;
    private boolean alive;
    private final Color skinColor;
    private int totalJumps;

    private final Image[] dinoRunning = new Image[2];
    private Image dinoJump;

    public Dino(Genome g) throws SlickException {
        this.loadResources();
        this.image = dinoRunning[0];
        this.jump = false;
        this.run = true;
        this.stepIndex = 0;
        this.jumpV = JUMPVELOCITY;
        this.g = g;
        this.alive = true;
        this.rand = new Random();
        this.skinColor = randColor();
        this.totalJumps = 0;
    }
    private Color randColor(){
        return new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat());
    }

    private void loadResources() throws SlickException {
        this.dinoRunning[0] = new Image("Dino/DinoRun1.png");
        this.dinoRunning[1] = new Image("Dino/DinoRun2.png");
        this.dinoJump = new Image("Dino/DinoJump.png");
    }

    public void AIjump(double j){
        if(!this.jump)
            this.jump = (j>0.8);
        updateDino();
    }

    private void updateDino(){
        if(this.jump){
            this.jump();
        }
        if(this.run){
            this.run();
        }
        if (this.stepIndex >= 10){
            this.stepIndex = 0;
        }
    }

    private void run(){
        this.image = this.dinoRunning[this.stepIndex / 5];
        this.stepIndex += 1;
    }

    private void jump(){
        this.image = this.dinoJump;
        if(this.jumpV < -JUMPVELOCITY){
            this.jump = false;
            this.run = true;
            this.jumpV = JUMPVELOCITY;
            this.y = 310;
            this.totalJumps++;
        }else{
            this.y -= this.jumpV;
            this.jumpV -= 1.2;
        }
    }

    public Rectangle getRectangle(){
        return new Rectangle(X, this.y, this.dinoJump.getWidth(), this.dinoJump.getHeight());
    }

    public void render(Graphics g){
        g.drawImage(this.image, X, this.y, this.skinColor);
    }

    public Genome getGenome() {
        return this.g;
    }

    public Float getFitness() {
        return this.g.getFitness();
    }

    public boolean getAlive(){
        return this.alive;
    }

    public float getV(){
        return this.jumpV;
    }

    public void kill(float fitness){
        this.g.setFitness(fitness);
        this.alive = false;
    }

    public int getTotalJumps() {
        return this.totalJumps;
    }

    public void unloadImages(){
        try {
            for(Image i: dinoRunning){
                i.destroy();
            }
            dinoJump.destroy();
        } catch (SlickException e) {
        }
    }

    public void updateFitnessBasedOnJump(float obstacleX) {
        float dinoX = this.getRectangle().getX();
        float distanceToCactus = obstacleX - dinoX;

        if (this.jump && distanceToCactus > 0 && distanceToCactus <= 200) {
            this.g.setFitness(this.getFitness()+ 1f);
        } else if (this.jump && distanceToCactus > 100) {
            this.g.setFitness(this.getFitness()- .8f);
        }
        if(this.jump && obstacleX<0){
            this.g.setFitness(this.getFitness()- 10f);
        }
    }
}
