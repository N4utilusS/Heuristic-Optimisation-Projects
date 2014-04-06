package pivoting_manager;
import main.Permutation;
import neighbourhood_generator.AbstractNeighbourhoodGenerator;


public abstract class AbstractPivotingManager {
	
	protected AbstractNeighbourhoodGenerator neighbourhoodGenerator;

	AbstractPivotingManager(AbstractNeighbourhoodGenerator neighbourhoodGenerator){
		this.neighbourhoodGenerator = neighbourhoodGenerator;
	}

	abstract Permutation getNewPermutation(Permutation permutation);
}
