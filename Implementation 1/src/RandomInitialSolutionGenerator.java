import java.util.Random;


public class RandomInitialSolutionGenerator implements InitialSolutionGenerator {

	@Override
	public Permutation getInitialSolution(int jobsAmount) {

		Permutation p = new Permutation(jobsAmount);
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
			p.swap(index, i);
		}
		
		return p;
	}

}
