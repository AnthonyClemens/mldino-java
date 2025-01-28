package io.github.anthonyclemens;

import java.awt.Font;
import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import com.evo.NEAT.ConnectionGene;
import com.evo.NEAT.com.evo.NEAT.config.NEAT_Config;

public class Statistics {
    private int score;
    private final Font font;
    private final TrueTypeFont ttf;
    private int generation;
    private int alive;
    private long memUsage;
    private ArrayList<ConnectionGene> connectionGenes;
    private final int inputNeurons;
    private final int outputNeuronIdx;
    private int bestScore;

    public Statistics() {
        this.font = new Font("Verdana", Font.BOLD, 20);
        this.ttf = new TrueTypeFont(font, true);
        this.generation = 1;
        this.inputNeurons = NEAT_Config.INPUTS + 1;
        this.outputNeuronIdx = NEAT_Config.INPUTS + 1000000;
    }

    public void update(float score, int bestScore) {
        this.score = (int) score - 10;
        this.bestScore = bestScore;
        this.memUsage = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024L * 1024L);
    }

    public int getScore() {
        return this.score;
    }

    public void render(Graphics g) {
        g.setFont(this.ttf);
        this.ttf.drawString(32.0f, 420.0f, "Score: " + this.score, Color.black);
        this.ttf.drawString(32.0f, 460.0f, "Generation: " + this.generation, Color.black);
        this.ttf.drawString(32.0f, 500.0f, "Alive: " + this.alive, Color.black);
        this.ttf.drawString(900.0f, 460.0f, "Best Score: " + this.bestScore, Color.black);
        this.ttf.drawString(900.0f, 500.0f, "Memory Usage: " + this.memUsage + "MB", Color.black);
        this.ttf.drawString(650.0f, 40.0f, "     Delta Y", Color.black);
        this.ttf.drawString(630.0f, 140.0f, "Vertical Vel", Color.black);
        this.ttf.drawString(650.0f, 90.0f, "     Delta X", Color.black);
        this.ttf.drawString(625.0f, 190.0f, "Game Speed", Color.black);
    }

    public void setConnectionGenes(ArrayList<ConnectionGene> connectionGenes) {
        this.connectionGenes = connectionGenes;
    }

    public void incrementGen() {
        this.generation++;
    }

    public int getGeneration() {
        return this.generation;
    }

    public void setAlive(int d) {
        this.alive = d;
    }
}

