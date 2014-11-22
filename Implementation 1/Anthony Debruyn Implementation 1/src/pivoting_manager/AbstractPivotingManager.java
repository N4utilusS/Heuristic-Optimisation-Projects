package pivoting_manager;
import main.Permutation;
import neighbourhood_generator.AbstractNeighbourhoodGenerator;

/**
 * Abstract class extended by all the pivoting managers.
 * Defines the behaviour of all pivoting managers.
 * The pivoting managers use neighbourhood generators to get neighbours, and then choose among these to return the best solution of the algorithm.
 * They return the previous solution if no improvement was possible.
 * @author anthonydebruyn
 *
 */
public abstract class AbstractPivotingManager {
	
	protected AbstractNeighbourhoodGenerator neighbourhoodGenerator;

	/**
	 * The constructor dealing with the fields belonging to this class.
	 * @param neighbourhoodGenerator The neighbourhood generator to use.
	 */
	public AbstractPivotingManager(AbstractNeighbourhoodGenerator neighbourhoodGenerator){
		this.neighbourhoodGenerator = neighbourhoodGenerator;
	}

	/**
	 * The method used by the algorithm to get a new best solution from the previous one, passed as a parameter.
	 * @param permutation The previous best solution.
	 * @return The new best solution, the previous one if no better solution was found (local maximum).
	 */
	public abstract Permutation getNewPermutation(Permutation permutation);
}
