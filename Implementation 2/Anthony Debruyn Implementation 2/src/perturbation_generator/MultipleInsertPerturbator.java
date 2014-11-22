package perturbation_generator;

import java.util.Random;

import main.Permutation;

/**
 * Perturbation method using the insertion method to return a perturbed permutation.
 * The perturbation consists in 4 random insertion in a row.
 * @author anthonydebruyn
 *
 */
public class MultipleInsertPerturbator extends
		AbstractPerturbationGenerator {
	private Random rand;
	
	public MultipleInsertPerturbator() {
		this.rand = new Random();
	}

	@Override
	public Permutation perturb(Permutation permutation) {
		
		permutation = permutation.insert(rand.nextInt(permutation.size()), rand.nextInt(permutation.size()));
		permutation = permutation.insert(rand.nextInt(permutation.size()), rand.nextInt(permutation.size()));
		permutation = permutation.insert(rand.nextInt(permutation.size()), rand.nextInt(permutation.size()));
		permutation = permutation.insert(rand.nextInt(permutation.size()), rand.nextInt(permutation.size()));
		
		return permutation;
	}

}
