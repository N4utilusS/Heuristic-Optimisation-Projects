package neighbourhood_generator;
import main.Permutation;

/**
 * Abstract class defining the wanted behaviour of all neighbourhood generators.
 * The extending classes will generate permutations from a given permutation, to explore the solution space.
 * Each generator will generate its neighbourhood in its own way.
 * @author anthonydebruyn
 *
 */
public abstract class AbstractNeighbourhoodGenerator {
	private Permutation permutation;

	/**
	 * All generators must override this method, and must call it at the beginning of the overriding version.
	 * Calls the reset method if a new permutation is given, to reset the neighbourhood search.
	 * Returns the next neighbour for the given permutation.
	 * @param permutation The permutation we want the neighbours from.
	 * @return The next neighbour for the given permutation.
	 */
	public Permutation getNextNeighbour(Permutation permutation){
		if (this.permutation == null || this.permutation != permutation){
			this.resetNeighbourhood();
			this.permutation = permutation;
		}
		return null;
	}
	
	/**
	 * Used to reset the neighbourhood search, in case a new permutation is given to the generator.
	 */
	abstract void resetNeighbourhood();
}
