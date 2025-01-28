import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.evo.NEAT.ConnectionGene;

public class StatisticsTest {

    @org.junit.Test
    public void testGenerateNeuronGraph() {
        // Example connection genes
        ArrayList<ConnectionGene> connectionGenes = new ArrayList<>();
        connectionGenes.add(new ConnectionGene(4, 1000003, 195334, 1.6020633f, true));
        connectionGenes.add(new ConnectionGene(3, 5, 214535, 1.004169f, false));
        connectionGenes.add(new ConnectionGene(6, 1000003, 234823, 2.1356313f, true));
        connectionGenes.add(new ConnectionGene(2, 6, 234822, -0.043453246f, false));
        connectionGenes.add(new ConnectionGene(2, 5, 234821, 1.6317682f, true));
        connectionGenes.add(new ConnectionGene(2, 4, 195333, 0.41663983f, true));
        connectionGenes.add(new ConnectionGene(2, 7, 355343, -1.5866153f, true));
        connectionGenes.add(new ConnectionGene(5, 1000003, 214536, 0.4540048f, true));
        connectionGenes.add(new ConnectionGene(3, 9, 285770, 1.3300796f, true));
        connectionGenes.add(new ConnectionGene(9, 5, 285771, -1.51515f, true));
        connectionGenes.add(new ConnectionGene(3, 8, 315221, -1.0699317f, true));
        connectionGenes.add(new ConnectionGene(0, 4, 228304, -0.43243486f, false));
        connectionGenes.add(new ConnectionGene(3, 7, 282320, 0.96776885f, true));
        connectionGenes.add(new ConnectionGene(7, 4, 282321, 0.2527448f, true));
        connectionGenes.add(new ConnectionGene(5, 8, 283356, -1.0986165f, true));
        connectionGenes.add(new ConnectionGene(8, 6, 283357, 1.0179913f, false));
        connectionGenes.add(new ConnectionGene(3, 1000003, 196381, 0.56772196f, false));
        connectionGenes.add(new ConnectionGene(0, 5, 259932, 1.5742713f, true));
        connectionGenes.add(new ConnectionGene(2, 9, 297826, 1.7493944f, true));
        connectionGenes.add(new ConnectionGene(2, 1000003, 359, 1.6779227f, false));
        connectionGenes.add(new ConnectionGene(6, 8, 285091, 0.4335f, true));
        connectionGenes.add(new ConnectionGene(3, 4, 197742, -0.7882f, true));
        connectionGenes.add(new ConnectionGene(1, 1000003, 194354, -0.014556445f, true));
        connectionGenes.add(new ConnectionGene(1, 5, 265141, -1.3650057f, true));
        connectionGenes.add(new ConnectionGene(3, 6, 311607, 1.3084853f, true));
        connectionGenes.add(new ConnectionGene(4, 7, 286461, -1.4614688f, true));
        connectionGenes.add(new ConnectionGene(5, 6, 254079, -1.0522835f, false));
        connectionGenes.add(new ConnectionGene(0, 1000003, 232765, 1.3077482f, false));

        // Generate the graph
        Statistics statistics = new Statistics(connectionGenes);
        try {
            statistics.generateNeuronGraph("NeuronGraph");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Graph generation failed");
        }

        // Check if the file was created (you might want to verify the content too)
        File file = new File("NeuronGraph.png");
        assertTrue("PNG file was not created",file.exists());
    }
}
