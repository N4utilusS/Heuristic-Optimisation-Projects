package initial_solution;

import java.util.Random;

import main.Instance;
import main.Permutation;

/**
 * Class used to return a random first solution.
 * A first permutation is created, then shuffled with the Fisher-Yates algorithm.
 * @author anthonydebruyn
 *
 */
public class RandomInitialSolutionGenerator implements InitialSolutionGenerator {

	@Override
	public Permutation getInitialSolution(Instance instance) {

		Permutation p = new Permutation(instance);
		return shuffleArray(p);
	}

	/**
	 * Shuffles the array, following the Fisher–Yates algorithm.
	 * @param p The permutation to shuffle.
	 */
	static Permutation shuffleArray(Permutation p)
	{
		Random rnd = new Random();
		for (int i = p.size() - 1; i > 0; i--)
		{
			int index = rnd.nextInt(i + 1);
			// Simple swap
			p = p.swap(index, i);
		}
		
		return p;
	}

}
