package pivoting_manager;
import main.Permutation;
import neighbourhood_generator.AbstractNeighbourhoodGenerator;


public class FirstPivotingManager extends AbstractPivotingManager {

	public FirstPivotingManager(AbstractNeighbourhoodGenerator neighbourhoodGenerator) {
		super(neighbourhoodGenerator);
	}

	@Override
	public Permutation getNewPermutation(Permutation permutation) {
		Permutation newPermutation;
		
		do {
			newPermutation = this.neighbourhoodGenerator.getNextNeighbour(permutation);
			
		} while (newPermutation != null && newPermutation.getTotalWeightedTardiness() <= permutation.getTotalWeightedTardiness());
		
		return (newPermutation == null) ? permutation : newPermutation;
	}

}
