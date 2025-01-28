package io.github.anthonyclemens;


import java.util.Random;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;

public class Obstacle{
    private final Image[] largeCacti = {new Image("Cactus/LargeCactus1.png"), new Image("Cactus/LargeCactus2.png"), new Image("Cactus/LargeCactus3.png")};
    private final Image[] smallCacti = {new Image("Cactus/SmallCactus1.png"), new Image("Cactus/SmallCactus2.png"), new Image("Cactus/SmallCactus3.png")};

    private Image image;
    private int x;
    private int y;
    private int obstaclesJumped;
    private final Random r;

    public Obstacle() throws SlickException {
        r = new Random();
        this.obstaclesJumped = 0;
        genCactus();
    }

    public final void genCactus() {
        int cactiSize = this.r.nextInt(2);
        int cactiIndex = this.r.nextInt(3);
        this.image = (cactiSize==1) ? largeCacti[cactiIndex] : smallCacti[cactiIndex];
        this.y = (cactiSize==1) ? 300 : 325;
    }

    public void render(Graphics g){
        g.drawImage(this.image, x, y);
    }

    public Rectangle getRect(){
        return new Rectangle(this.x, this.y, this.image.getWidth(),this.image.getHeight());
    }

    public void update(int x){
        if(x==0){
            genCactus();
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
}
