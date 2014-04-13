package initial_solution;

import main.Instance;
import main.Permutation;

/**
 * Used to generate a first solution with the SLACK heuristic.
 * A solution is first created. Then for each spot in the job queue, we put the job which minimises the weighted earliness of the current partial solution.
 * @author anthonydebruyn
 *
 */
public class SlackInitialSolutionGenerator implements InitialSolutionGenerator {

	@Override
	public Permutation getInitialSolution(Instance instance) {
		Permutation initial = new Permutation(instance);
		Permutation current;
		
		for (int i = 0; i < instance.getJobsAmount()-1; i++){
			// Explore each possible solutions for the given i first jobs.
			for (int j = i; j < instance.getJobsAmount()-1; j++){
				current = initial.insert(i, instance.getJobsAmount()-1); // Shift all remaining jobs to analyse the earlyness of another job for the ith place.
				if (current.getWeightedEarliness(i) < initial.getWeightedEarliness(i))
					initial = current;
			}
		}
		
		return initial;
	}

}
