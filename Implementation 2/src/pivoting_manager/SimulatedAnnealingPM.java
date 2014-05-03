package pivoting_manager;

import neighbourhood_generator.AbstractNeighbourhoodGenerator;
import main.Permutation;

public class SimulatedAnnealingPM extends AbstractPivotingManager {

	public SimulatedAnnealingPM(AbstractNeighbourhoodGenerator neighbourhoodGenerator) {
		super(neighbourhoodGenerator);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Permutation getNewPermutation(Permutation permutation) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void init(Permutation permutation) {
		// Here, this method is used to compute the initial temperature:
		// We get the mean tardiness increase:
		
		
		// Save the initial temperature:
	}

}
