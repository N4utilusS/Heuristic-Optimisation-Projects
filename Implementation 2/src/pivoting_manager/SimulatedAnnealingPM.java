package pivoting_manager;

import java.util.Random;

import main.Permutation;
import neighbourhood_generator.AbstractNeighbourhoodGenerator;

/**
 * Pivoting manager implementing the simulated annealing method.
 * @author anthonydebruyn
 *
 */
public class SimulatedAnnealingPM extends AbstractPivotingManager {
	public final static float INIT_FRACTION_OF_UPHILL_ACCEPTED_TRANSITION = 0.05f;
	public final static float Q_FACTOR = 2f;
	public final static float R_FACTOR = 0.7f;
	
	private float temperature;
	private Random rand;
	private int counter;
	
	public SimulatedAnnealingPM(AbstractNeighbourhoodGenerator neighbourhoodGenerator) {
		super(neighbourhoodGenerator);
		this.rand = new Random();
	}

	@Override
	public Permutation getNewPermutation(Permutation permutation) {
		Permutation returned;
		
		// Select a neighbour at random:
		int randNeighbour = rand.nextInt(neighbourhoodGenerator.getNeighbourhoodSize(permutation));
		
		while (randNeighbour > 0) {
			neighbourhoodGenerator.getNextNeighbour(permutation);
			randNeighbour--;
		}
		
		Permutation newPermutation = neighbourhoodGenerator.getNextNeighbour(permutation);
		
		// Get new permutation:
		if (newPermutation.getTotalWeightedTardiness() <= permutation.getTotalWeightedTardiness())
			returned = newPermutation;
		else if (rand.nextFloat() < Math.exp(permutation.getTotalWeightedTardiness() - newPermutation.getTotalWeightedTardiness() / this.temperature)){
			returned = newPermutation;
		} else
			returned = permutation;
			
		// Update temperature if needed:
		this.counter++;
		if (this.counter >= Q_FACTOR * neighbourhoodGenerator.getNeighbourhoodSize(permutation)) {
			this.temperature *= R_FACTOR;
			this.counter = 0;
		}
		
		return returned;
	}
	
	@Override
	public void init(Permutation permutation) {
		// Here, this method is used to compute the initial temperature:
		// We get the mean tardiness increase:
		Permutation newPermutation = null;
		int twt = permutation.getTotalWeightedTardiness();
		float meanTardinessIncrease = 0;
		int counter = 0;
		
		while ((newPermutation = neighbourhoodGenerator.getNextNeighbour(permutation)) != null) {
			if (newPermutation.getTotalWeightedTardiness() > twt) {
				meanTardinessIncrease += newPermutation.getTotalWeightedTardiness() - twt;
				counter++;
			}
		}
		
		meanTardinessIncrease /= counter;
		
		// Save the initial temperature:
		this.temperature = (float) (-meanTardinessIncrease/Math.log(INIT_FRACTION_OF_UPHILL_ACCEPTED_TRANSITION));
		
		// Reset the neighbourhood:
		this.neighbourhoodGenerator.resetNeighbourhood();
	}

}
