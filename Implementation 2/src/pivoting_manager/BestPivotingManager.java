package pivoting_manager;

import neighbourhood_generator.AbstractNeighbourhoodGenerator;
import main.Permutation;

/**
 * Pivoting manager extending {@link AbstractPivotingManager}.
 * Selects the best (better) neighbour found as the next best solution.
 * @author anthonydebruyn
 *
 */
public class BestPivotingManager extends AbstractPivotingManager {

	/**
	 * Constructor taking the neighbourhood generator to use.
	 * @param neighbourhoodGenerator The neighbourhood generator to use.
	 */
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
