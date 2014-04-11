package pivoting_manager;
import main.Permutation;
import neighbourhood_generator.AbstractNeighbourhoodGenerator;


public abstract class AbstractPivotingManager {
	
	protected AbstractNeighbourhoodGenerator neighbourhoodGenerator;

	public AbstractPivotingManager(AbstractNeighbourhoodGenerator neighbourhoodGenerator){
		this.neighbourhoodGenerator = neighbourhoodGenerator;
	}

	public abstract Permutation getNewPermutation(Permutation permutation);
}
