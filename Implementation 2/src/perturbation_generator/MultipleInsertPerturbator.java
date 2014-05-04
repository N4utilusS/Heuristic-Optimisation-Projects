package perturbation_generator;

import java.util.Random;

import main.Permutation;

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
