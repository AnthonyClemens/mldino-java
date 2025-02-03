package io.github.anthonyclemens;


import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class Obstacle{
    private final Image[] largeCacti = {new Image("Cactus/LargeCactus1.png"), new Image("Cactus/LargeCactus2.png"), new Image("Cactus/LargeCactus3.png")};
    private final Image[] smallCacti = {new Image("Cactus/SmallCactus1.png"), new Image("Cactus/SmallCactus2.png"), new Image("Cactus/SmallCactus3.png")};
    private final Image[] bird = {new Image("Bird/bird1.png"), new Image("Bird/bird2.png")};

    private Image image;
    private int x;
    private int y;
    private int obstaclesJumped;
    private float type;
    private final Random r;
    private int stepIndex;

    public Obstacle() throws SlickException {
        r = new Random();
        this.obstaclesJumped = 0;
        this.stepIndex = 0;
        if(this.r.nextInt(1)==1){
            genCactus();
        }else{
            genBird();
        }
    }

    private void genCactus() {
        this.type=0.0f;
        int cactiSize = this.r.nextInt(2);
        int cactiIndex = this.r.nextInt(3);
        this.image = (cactiSize==1) ? largeCacti[cactiIndex] : smallCacti[cactiIndex];
        this.y = (cactiSize==1) ? 300 : 325;
    }

    private void genBird(){
        this.type=1.0f;
        this.image = bird[0];
        this.y = 220;
    }

    public void render(Graphics g){
        g.drawImage(this.image, x, y);
        //g.drawRect(x, y, this.image.getWidth(), this.image.getHeight());
    }

    public Rectangle getRect(){
        return new Rectangle(this.x, this.y, this.image.getWidth(),this.image.getHeight());
    }

    public void update(int x){
        this.stepIndex+=1;
        if(this.stepIndex>=10){
            this.stepIndex=0;
        }
        if(this.image.getHeight()==80){
            this.image = bird[this.stepIndex / 5];
        }
        if(x==0){
            if(this.r.nextInt(2)==1){
                genCactus();
            }else{
                genBird();
            }
            this.obstaclesJumped++;
        }
        this.x = x+1200;
    }

    public int getObstaclesJumped(){
        return this.obstaclesJumped;
    }

    public void reset(){
        this.obstaclesJumped = 0;
        genCactus();
    }

    public float getObstacleType(){
        return this.type;
    }
}
