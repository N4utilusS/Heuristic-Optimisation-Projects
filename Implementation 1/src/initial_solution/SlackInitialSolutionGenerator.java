package initial_solution;

import main.Instance;
import main.Permutation;

public class SlackInitialSolutionGenerator implements InitialSolutionGenerator {

	@Override
	public Permutation getInitialSolution(Instance instance) {
		Permutation initial = new Permutation(instance);
		Permutation current;
		
		for (int i = 0; i < instance.getJobsAmount()-1; i++){
			// Explore each possible solutions for the given i first jobs.
			for (int j = i; j < instance.getJobsAmount()-1; j++){
				current = initial.insert(i, instance.getJobsAmount()-1); // Shift all remaining jobs to analyse the earlyness of another job for the ith place.
				if (current.getWeightedEarlyness(i) < initial.getWeightedEarlyness(i))
					initial = current;
			}
		}
		
		return initial;
	}

}
