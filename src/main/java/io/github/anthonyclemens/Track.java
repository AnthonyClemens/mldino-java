package io.github.anthonyclemens;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Track {
    private final Image trackIMG;
    private final float trackW;
    private float distance;

    private int bgX=0;
    private static final int BGY=380;
    private float gameSpeed = 10;

    public Track() throws SlickException{
        this.trackIMG = new Image("Other/Track.png");
        this.trackW = trackIMG.getWidth();
        this.distance = 0;
    }

    public float getGameSpeed(){
        return this.gameSpeed;
    }

    public int getX(){
        return this.bgX;
    }
    public float getDistance(){
        return this.distance;
    }

    public void render(Graphics g){
        g.setBackground(Color.white);
        g.drawImage(trackIMG, bgX, BGY);
        g.drawImage(trackIMG, bgX + trackW, BGY);
    }

    public void update(){
        if(this.bgX==0){
            this.gameSpeed+=0.2;
        }
        this.bgX = (int) ((this.bgX <= -trackW) ? 0 : this.bgX - this.gameSpeed);
        this.distance+=0.001;
    }

    public void reset(){
        this.distance=0;
        this.bgX=0;
        this.gameSpeed=10;
    }
}
