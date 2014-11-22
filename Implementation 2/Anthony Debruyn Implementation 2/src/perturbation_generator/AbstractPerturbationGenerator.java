package perturbation_generator;

import main.Permutation;

/**
 * Class used to create perturbations in an algorithm, to escape from a local minimum.
 * This abstract class contains all the methods that inheriting classes must implement.
 * Each inheriting class is a kind of perturbation.
 * @author anthonydebruyn
 *
 */
public abstract class AbstractPerturbationGenerator {

	/**
	 * Takes one permutation as a parameter and returns the perturbed version to escape from local minimum.
	 * @param permutation Permutation to perturb.
	 * @return The perturbed permutation.
	 */
	public abstract Permutation perturb(Permutation permutation);
}
