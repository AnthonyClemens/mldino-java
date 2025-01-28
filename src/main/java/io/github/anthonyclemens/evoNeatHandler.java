package io.github.anthonyclemens;

import com.evo.NEAT.Pool;

public class evoNeatHandler {
    private Pool pool;

    public evoNeatHandler(){
        this.pool = new Pool();
        this.pool.initializePool();
    }

    public void evolve(DinoEnvironment env){
        this.pool.evaluateFitness(env);
        this.pool.breedNewGeneration();
    }

    public Pool getPool() {
        return this.pool;
    }

}
