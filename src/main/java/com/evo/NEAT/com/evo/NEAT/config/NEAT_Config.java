package com.evo.NEAT.com.evo.NEAT.config;

/**
 * Created by vishnughosh on 01/03/17.
 */
public class NEAT_Config {

    public static final int INPUTS = 2; // Number of input nodes - 1
    public static final int OUTPUTS = 2; // Number of output nodes
    public static final int HIDDEN_NODES = 1000000; // Number of hidden nodes in the network
    public static final int POPULATION = 2000; // Number of individuals in the population

    public static final float COMPATIBILITY_THRESHOLD = 2.0f; // Threshold for determining if two individuals are compatible
    public static final float EXCESS_COEFFICENT = .5f; // Coefficient for excess genes in compatibility calculation
    public static final float DISJOINT_COEFFICENT = .5f; // Coefficient for disjoint genes in compatibility calculation
    public static final float WEIGHT_COEFFICENT = 0.4f; // Coefficient for weight differences in compatibility calculation

    public static final float STALE_SPECIES = 20; // Number of generations a species can remain stagnant before being removed

    public static final float STEPS = 0.05f; // Step size for adjusting weights during mutation
    public static final float PERTURB_CHANCE = 0.7f; // Chance of weight perturbation during mutation
    public static final float WEIGHT_CHANCE = 0.8f; // Chance of mutating weights
    public static final float WEIGHT_MUTATION_CHANCE = 0.7f; // Chance of a weight mutation occurring
    public static final float NODE_MUTATION_CHANCE = 0.03f; // Chance of a new node mutation occurring
    public static final float CONNECTION_MUTATION_CHANCE = 0.05f; // Chance of a new connection mutation occurring
    public static final float BIAS_CONNECTION_MUTATION_CHANCE = 0.02f; // Chance of a new bias connection mutation occurring
    public static final float DISABLE_MUTATION_CHANCE = 0.25f; // Chance of disabling a gene during mutation
    public static final float ENABLE_MUTATION_CHANCE = 0.25f; // Chance of enabling a gene during mutation
    public static final float CROSSOVER_CHANCE = 0.20f; // Chance of crossover occurring during reproduction

    public static final int STALE_POOL = 15; // Number of generations an individual can remain stagnant before being removed from the pool

}
