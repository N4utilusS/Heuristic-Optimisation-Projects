package neighbourhood_generator;

import main.Permutation;

/**
 * Neighbourhood generator extending {@link AbstractNeighbourhoodGenerator}.
 * Generates neighbours with the transpose algorithm.
 * @author anthonydebruyn
 *
 */
public class TransposeNeighbourhoodGenerator extends AbstractNeighbourhoodGenerator {
	private int i = 0;
	
	@Override
	public Permutation getNextNeighbour(Permutation permutation){
		super.getNextNeighbour(permutation);
		if (this.i == permutation.size()-1)
			return null;
		return permutation.swap(i, ++i);
	}

	@Override
	void resetNeighbourhood() {
		this.i = 0;
	}

}
