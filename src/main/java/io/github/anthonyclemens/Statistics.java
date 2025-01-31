package io.github.anthonyclemens;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;

import com.evo.NEAT.ConnectionGene;
import com.evo.NEAT.com.evo.NEAT.config.NEAT_Config;

public class Statistics {
    private int score;
    private final Font font;
    public final TrueTypeFont ttf;
    private int generation;
    private int alive;
    private long memUsage;
    private ArrayList<ConnectionGene> connectionGenes;
    private final int inputNeuronsC;
    private final int outputNeuronIdx;
    private int bestScore;
    private Map<Integer, Integer> neuronLayers;
    private Map<Integer, Set<Integer>> neuronInputs;
    private Set<Integer> visited;

    public Statistics() {
        this.font = new Font("Verdana", Font.BOLD, 20);
        this.ttf = new TrueTypeFont(font, true);
        this.generation = 1;
        this.inputNeuronsC = NEAT_Config.INPUTS + 1;
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
        if(this.connectionGenes!=null){
            this.renderNeuralNetwork(g);
        }
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


    private void renderNeuralNetwork(Graphics g) {
        Set<Integer> neuronIds = new HashSet<>();

        for (ConnectionGene gene : connectionGenes) {
            neuronIds.add(gene.getInto());
            neuronIds.add(gene.getOut());
        }

        List<Integer> inputNeurons = new ArrayList<>();
        List<Integer> outputNeurons = new ArrayList<>();
        int inputCount = this.inputNeuronsC;

        for (Integer id : neuronIds) {
            if (id >= 0 && id < inputCount) {
                inputNeurons.add(id);
            } else if (id >= 1000000) {
                outputNeurons.add(id);
            }
        }

        neuronLayers = new HashMap<>();
        neuronInputs = new HashMap<>();
        visited = new HashSet<>();

        for (ConnectionGene gene : connectionGenes) {
            if (gene.isEnabled()) {
                neuronInputs.computeIfAbsent(gene.getOut(), k -> new HashSet<>()).add(gene.getInto());
            }
        }

        for (Integer neuronId : inputNeurons) {
            neuronLayers.put(neuronId, 0);
        }

        Set<Integer> allNeurons = new HashSet<>(neuronIds);
        allNeurons.removeAll(inputNeurons);
        allNeurons.removeAll(outputNeurons);

        for (Integer neuronId : allNeurons) {
            assignLayer(neuronId);
        }

        int maxLayer = neuronLayers.values().stream().max(Integer::compareTo).orElse(0);
        int outputLayer = maxLayer + 1;
        for (Integer neuronId : outputNeurons) {
            neuronLayers.put(neuronId, outputLayer);
        }
        maxLayer = outputLayer;

        int width = 800;
        int height = 360;

        Map<Integer, Vector2f> neuronPositions = new HashMap<>();


        Map<Integer, List<Integer>> layers = new HashMap<>();
        for (Map.Entry<Integer, Integer> entry : neuronLayers.entrySet()) {
            int layer = entry.getValue();
            layers.computeIfAbsent(layer, k -> new ArrayList<>()).add(entry.getKey());
        }

        for (int layer = 0; layer <= maxLayer; layer++) {
            List<Integer> neuronsInLayer = layers.get(layer);
            if (neuronsInLayer == null) continue;

            float x = width * (layer / (float) maxLayer);

            for (int i = 0; i < neuronsInLayer.size(); i++) {
                Integer neuronId = neuronsInLayer.get(i);
                float y = height * ((i + 1) / (float) (neuronsInLayer.size() + 1));
                neuronPositions.put(neuronId, new Vector2f(x+300, y));
            }
        }

        float maxWeight = 0f;
        for (ConnectionGene gene : connectionGenes) {
            float weightMag = Math.abs(gene.getWeight());
            if (weightMag > maxWeight) {
                maxWeight = weightMag;
            }
        }
        if (maxWeight == 0f) {
            maxWeight = 1f;
        }

        for (ConnectionGene gene : connectionGenes) {
            if (gene.isEnabled()) {
                Vector2f fromPos = neuronPositions.get(gene.getInto());
                Vector2f toPos = neuronPositions.get(gene.getOut());

                if (fromPos != null && toPos != null) {
                    Color lineColor = gene.getWeight() >= 0 ? Color.green : Color.red;

                    float weightMagnitude = Math.abs(gene.getWeight());
                    lineColor = new Color(lineColor.r, lineColor.g, lineColor.b);

                    float lineWidth = 1f + 4f * (weightMagnitude / maxWeight);

                    g.setLineWidth(lineWidth);
                    g.setColor(lineColor);
                    g.drawLine(fromPos.x, fromPos.y, toPos.x, toPos.y);
                }
            }
        }

        g.setLineWidth(1f);
        for (Map.Entry<Integer, Vector2f> entry : neuronPositions.entrySet()) {
            int neuronId = entry.getKey();
            Vector2f pos = entry.getValue();

            boolean isLit = false;
            for (ConnectionGene gene : connectionGenes) {
                if (gene.isEnabled() && (gene.getInto() == neuronId || gene.getOut() == neuronId)) {
                    isLit = true;
                    break;
                }
            }

            if (isLit) {
                g.setColor(Color.yellow);
            } else {
                g.setColor(Color.gray);
            }

            g.fillOval(pos.x - 10, pos.y - 10, 32, 32);

            g.setColor(Color.black);
            String neuronString = "H"+String.valueOf(neuronId-inputCount);
            switch (neuronId) {
                case 0:
                    neuronString="dY";
                    break;
                case 1:
                    neuronString="dX";
                    break;
                case 2:
                    neuronString="jV";
                    break;
                case 3:
                    neuronString="gS";
                    break;
                default:
                    break;
            }
            if(neuronId==outputNeuronIdx){
                neuronString="Jump";
            }
            g.drawString(neuronString, pos.x - 5, pos.y - 5);
        }
    }

    private void assignLayer(int neuronId) {
        if (neuronLayers.containsKey(neuronId)) {
            return;
        }
        if (visited.contains(neuronId)) {
            neuronLayers.put(neuronId, neuronLayers.getOrDefault(neuronId, 1));
            return;
        }

        visited.add(neuronId);

        Set<Integer> inputs = neuronInputs.get(neuronId);
        if (inputs == null || inputs.isEmpty()) {
            neuronLayers.put(neuronId, 1);
        } else {
            int maxLayer = 0;
            for (Integer inputId : inputs) {
                assignLayer(inputId);
                int inputLayer = neuronLayers.getOrDefault(inputId, 0);
                if (inputLayer > maxLayer) {
                    maxLayer = inputLayer;
                }
            }
            neuronLayers.put(neuronId, maxLayer + 1);
        }

        visited.remove(neuronId);
    }
}

