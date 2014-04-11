package pivoting_manager;

import neighbourhood_generator.AbstractNeighbourhoodGenerator;
import main.Permutation;

public class BestPivotingManager extends AbstractPivotingManager {

	public BestPivotingManager(AbstractNeighbourhoodGenerator neighbourhoodGenerator) {
		super(neighbourhoodGenerator);
	}

	@Override
	public Permutation getNewPermutation(Permutation permutation) {
		Permutation newPermutation;
		Permutation bestPermutation = permutation;

		while ((newPermutation = this.neighbourhoodGenerator.getNextNeighbour(permutation)) != null){
			if (newPermutation.getTotalWeightedTardiness() < bestPermutation.getTotalWeightedTardiness())
				bestPermutation = newPermutation;
		}

		return bestPermutation;
	}

}
