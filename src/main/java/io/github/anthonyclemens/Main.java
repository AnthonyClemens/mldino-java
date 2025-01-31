package io.github.anthonyclemens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.Game;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import com.evo.NEAT.ConnectionGene;
import com.evo.NEAT.Genome;



public class Main implements Game{

    private int dinosAlive;
    private Track track;
    private final ArrayList<Dino> dinos = new ArrayList<>();
    private Obstacle obstacle;
    private Statistics stats;
    private evoNeatHandler evo;
    private DinoEnvironment dinoEnv;
    private int bestScore;
    private static String checkpointString = null;
    private static int updateInterval = 120;
    public static void main(String[] args){
        if(args.length>0){
            checkpointString=args[0];
        }
        try {
            AppGameContainer app = new AppGameContainer(new Main());
            app.setDisplayMode(1200, 600, false);
            app.setVSync(true);
		    app.setAlwaysRender(true);
		    app.setShowFPS(false);
		    app.setMaximumLogicUpdateInterval(updateInterval);
		    app.setTargetFrameRate(updateInterval/2);
            app.setIcon("Dino/DinoJump.png");
            app.start();
        } catch (SlickException e){
            System.err.println("Failed to create Slick2D Container");
        }
    }

    @Override
    public boolean closeRequested() {
        return true;
    }

    @Override
    public String getTitle() {
        return "Machine Learning Dino";
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        System.out.println("Initializing Track");
        track = new Track();
        System.out.println("Initializing Obstacle");
        obstacle = new Obstacle();
        System.out.println("Initializing Statistics");
        stats = new Statistics();
        System.out.println("Initializing evoNeatHandler");
        evo = new evoNeatHandler();
        System.out.print("Initializing Dinos");
        if(checkpointString!=null){
            initializeDinosFromCheckpoint(checkpointString);
            System.out.println(" from checkpoint: "+checkpointString);
        }else{
            initializeDinos();
            System.out.println();
        }
        System.out.println("Initializing Dino Environment");
        dinoEnv = new DinoEnvironment(dinos);
        System.out.println("Evolving Dinos");
        evo.evolve(dinoEnv);
        bestScore = 0;
    }

    private void initializeDinos(){
        evo.getPool().getSpecies().stream()
            .flatMap(s -> s.getGenomes().stream())
            .forEach(g -> {
                try {
                    dinos.add(new Dino(g));
                } catch (SlickException e) {
                    System.err.println("Failed to create Dinos, most likely asset issues");
                }
        });
    }
    private void initializeDinosFromCheckpoint(String filename){
        ArrayList<ConnectionGene> loadedGenome = Genome.readFromfile(filename);
        evo.getPool().getSpecies().stream()
            .flatMap(s -> s.getGenomes().stream())
            .forEach(g -> {
                try {
                    Dino d = new Dino(g);
                    d.getGenome().setConnectionGeneList(loadedGenome);
                    dinos.add(d);
                } catch (SlickException e) {
                    System.err.println("Failed to create Dinos, most likely asset issues");
                }
        });
        stats.setConnectionGenes(loadedGenome);
    }
    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        track.render(g);
        obstacle.render(g);
        dinos.stream()
            .filter(Dino::getAlive)
            .limit(50)
            .forEach(d -> d.render(g));
        stats.render(g);
        stats.ttf.drawString( 0.0f, 0.0f,String.valueOf(container.getFPS()),Color.black);
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        track.update();
        obstacle.update(track.getX());
        updateDinos();
        checkCollision();
        dinosAlive = dinos.size()-(int)dinos.stream()
                                    .filter(d -> !d.getAlive())
                                    .count();
        if(dinosAlive == 0){
            evolveNewGeneration();
        }
        stats.setAlive(dinosAlive);
        stats.update(track.getGameSpeed()+obstacle.getObstaclesJumped(), this.bestScore);
        if(container.getInput().isKeyPressed(Input.KEY_ADD)){
            updateInterval=9999;
            container.setMaximumLogicUpdateInterval(updateInterval);
		    container.setTargetFrameRate(updateInterval/2);
            container.setVSync(false);
        }
        if(container.getInput().isKeyPressed(Input.KEY_MINUS)){
            updateInterval=120;
            container.setMaximumLogicUpdateInterval(updateInterval);
		    container.setTargetFrameRate(updateInterval/2);
            container.setVSync(true);
        }
    }

    private void updateDinos(){
        dinos.stream()
            .filter(Dino::getAlive)
            .forEach(aiD -> {
                float[] inputs = {
                aiD.getRectangle().getY()-obstacle.getRect().getY(),
                obstacle.getRect().getX()-aiD.getRectangle().getX(),
                aiD.getV(),
                track.getGameSpeed(),
            };
            float[] output = aiD.getGenome().evaluateNetwork(inputs);
            aiD.AIjump(output[0]);
            aiD.updateFitnessBasedOnJump(this.obstacle.getRect().getX());
            });
    }

    private void checkCollision(){
        dinos.stream()
            .filter(d -> d.getRectangle().intersects(obstacle.getRect()))
            .forEach(d -> d.kill(track.getDistance()*2f - (d.getTotalJumps() * 0.1f)));
    }

    private void evolveNewGeneration(){
        ArrayList<ConnectionGene> bestGenomeConnections;
        Collections.sort(dinos,Comparator.comparing(Dino::getFitness));
        System.out.println("Generation: "+stats.getGeneration());
        System.out.println("Best Genome Fitness: "+this.dinos.get(0).getFitness()+"\n");
        bestGenomeConnections = this.dinos.get(0).getGenome().getConnectionGeneList();
        if(stats.getScore()>this.bestScore){
            this.bestScore=stats.getScore();
            this.dinos.get(0).getGenome().writeTofile("Generation"+stats.getGeneration());
            System.out.println("New High Score: "+this.bestScore+"\n");
        }
        stats.setConnectionGenes(bestGenomeConnections);
        stats.incrementGen();
        obstacle.reset();
        track.reset();
        dinos.stream()
            .forEach(Dino::unloadImages);
        dinos.clear();
        initializeDinos();
        evo.evolve(dinoEnv);
    }
}