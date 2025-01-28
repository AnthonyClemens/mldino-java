import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import com.evo.NEAT.ConnectionGene;
import com.evo.NEAT.Genome;

public class GenomeTest {

    @Test
    public void testToString() {
        Genome genome = new Genome();

        // Populate the connectionGeneList with some ConnectionGene objects
        ArrayList<ConnectionGene> connectionGeneList = new ArrayList<>();
        connectionGeneList.add(new ConnectionGene(1, 2, 1, 0.5f, true));
        connectionGeneList.add(new ConnectionGene(2, 3, 2, -0.3f, false));
        connectionGeneList.add(new ConnectionGene(3, 4, 3, 1.2f, true));

        genome.setConnectionGeneList(connectionGeneList);

        // Expected string output
        String expected = "Genome{fitness=0.0, connectionGeneList=[ConnectionGene{into=1, out=2, innovation=1, weight=0.5, enabled=true}, ConnectionGene{into=2, out=3, innovation=2, weight=-0.3, enabled=false}, ConnectionGene{into=3, out=4, innovation=3, weight=1.2, enabled=true}], nodeGenes={}}";

        // Assert that the toString method produces the expected result
        assertEquals(expected, genome.toString());
    }
}
