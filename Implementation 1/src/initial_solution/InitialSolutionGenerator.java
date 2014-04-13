package initial_solution;

import main.Instance;
import main.Permutation;

/**
 * Interface implemented by all the initial solution generators.
 * These classes are used to generate the first solution used by the algorithms.
 * @author anthonydebruyn
 *
 */
public interface InitialSolutionGenerator {

	/**
	 * Generates the first solution to be used by the algorithm, and returns it.
	 * Must be implemented in each implementing class.
	 * @param instance The instance object of the current problem.
	 * @return The first solution (permutation).
	 * @see Permutation
	 */
	Permutation getInitialSolution(Instance instance);
}
