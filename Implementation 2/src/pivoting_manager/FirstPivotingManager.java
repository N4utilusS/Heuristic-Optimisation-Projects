package pivoting_manager;
import main.Permutation;
import neighbourhood_generator.AbstractNeighbourhoodGenerator;

/**
 * Pivoting manager extending {@link AbstractPivotingManager}.
 * Selects the first better neighbour found as the next best solution.
 * @author anthonydebruyn
 *
 */
public class FirstPivotingManager extends AbstractPivotingManager {

	/**
	 * The constructor taking the neighbourhood generator to use.
	 * @param neighbourhoodGenerator The neighbourhood generator to use.
	 */
	public FirstPivotingManager(AbstractNeighbourhoodGenerator neighbourhoodGenerator) {
		super(neighbourhoodGenerator);
	}

	@Override
	public Permutation getNewPermutation(Permutation permutation) {
		Permutation newPermutation;
		
		do {
			newPermutation = this.neighbourhoodGenerator.getNextNeighbour(permutation);
		} while ((newPermutation != null) && (newPermutation.getTotalWeightedTardiness() >= permutation.getTotalWeightedTardiness() || newPermutation.getMakespan() >= permutation.getMakespan()));
		
		return ((newPermutation == null) ? permutation : newPermutation);
	}

}
