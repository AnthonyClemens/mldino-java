package io.github.anthonyclemens;

import java.util.ArrayList;
import java.util.List;

import com.evo.NEAT.Environment;
import com.evo.NEAT.Genome;

public class DinoEnvironment implements Environment {
    private List<Dino> dinos;

    public DinoEnvironment(List<Dino> dinos) {
        this.dinos = dinos;
    }

    @Override
    public void evaluateFitness(ArrayList<Genome> population) {
    }
}