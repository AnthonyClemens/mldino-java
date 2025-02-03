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
    private float y=300;
    private S state;
    private static final float JUMPVELOCITY = 22;
    private int stepIndex;
    private float jumpV;
    private boolean alive;
    private final Color skinColor;
    private int totalJumps;

    private final Image[] dinoRunning = new Image[2];
    private Image dinoJump;
    private final Image[] dinoDuck = new Image[2];
    enum S {
        JUMP, RUN, DUCK
    }

    public Dino(Genome g) throws SlickException {
        this.loadResources();
        this.image = dinoRunning[0];
        this.state = S.RUN;
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
        this.dinoDuck[0] = new Image("Dino/DinoDuck1.png");
        this.dinoDuck[1] = new Image("Dino/DinoDuck2.png");
    }

    public void aiDecision(float a, float b){
        if (a > 0.8f && b > 0.8f) {
            this.g.setFitness(this.getFitness()-.1f);
        } else if (a > 0.8f && this.state!=S.JUMP) {
            this.y=300;
            this.state=S.JUMP;
        } else if (b > 0.8f && this.state!=S.JUMP) {
            this.state=S.DUCK;
        } else {
            if(this.state!=S.JUMP){
                this.y=300;
                this.state=S.RUN;
                this.jumpV = JUMPVELOCITY;
            }
        }
        //System.out.println(this.state);
        updateDino();
    }


    private void updateDino(){
        this.stepIndex += 1;
        if (this.stepIndex >= 10){
            this.stepIndex = 0;
        }
        switch (this.state) {
            case RUN:
                this.run();
                break;
            case DUCK:
                this.duck();
                break;
            case JUMP:
                this.jump();
                break;
            default:
                throw new AssertionError();
        }
    }

    private void run(){
        this.image = this.dinoRunning[this.stepIndex / 5];
    }

    private void jump(){
        this.image = this.dinoJump;
        if(this.jumpV < -JUMPVELOCITY){
            this.state=S.RUN;
            this.jumpV = JUMPVELOCITY;
            this.y = 300;
            this.totalJumps++;
        }else{
            this.y -= this.jumpV;
            this.jumpV -= 1.1;
        }
    }

    private void duck(){
        this.y = 326;
        this.image = this.dinoDuck[this.stepIndex / 5];
    }

    public Rectangle getRectangle(){
        return new Rectangle(X, this.y, this.image.getWidth(), this.image.getHeight());
    }

    public void render(Graphics g){
        g.drawImage(this.image, X, this.y, this.skinColor);
        //g.setColor(skinColor);
        //g.drawRect(X, this.y, this.image.getWidth(), this.image.getHeight());
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
            for(Image i: dinoDuck){
                i.destroy();
            }
            dinoJump.destroy();
        } catch (SlickException e) {
        }
    }

    public void updateFitnessBasedOnDecision(float obstacleX, float obstacleY, int gameSpeed) {
        float d = Main.calculateDistance(obstacleX, obstacleY, X, this.y);
        float goalD = (gameSpeed/50)*600f;
        if(this.state!=S.RUN && obstacleX<0){
            this.g.setFitness(this.getFitness()- 100f);
        }
        if(this.state==S.DUCK && obstacleY==220 && (d<=goalD)){
            this.g.setFitness(this.getFitness()+1f);
        }
        if(this.state==S.JUMP && obstacleY!=220 && (d<=goalD)){
            this.g.setFitness(this.getFitness()+1f);
        }
        if(this.state==S.JUMP && obstacleY!=220 && (d>goalD)){
            this.g.setFitness(this.getFitness()-2f);
        }
        if(this.state==S.JUMP && obstacleY==220 && (d<goalD)){
            this.g.setFitness(this.getFitness()-2f);
        }
    }
}
