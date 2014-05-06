package neighbourhood_generator;

import main.Permutation;

/**
 * Neighbourhood generator extending {@link AbstractNeighbourhoodGenerator}.
 * Generates neighbours with the insert algorithm.
 * @author anthonydebruyn
 *
 */
public class InsertNeighbourhoodGenerator extends AbstractNeighbourhoodGenerator {

	private int i = 0;
	private int j = 1;
	
	@Override
	public Permutation getNextNeighbour(Permutation permutation){
		super.getNextNeighbour(permutation);
		
		if (this.i-1 == this.j)
			j = j + 2;
		
		if (this.j == permutation.size()){
			i++;
			j = 0;
		}
		
		if (this.i-1 == this.j)	// For the case when i=1 and j=0, just after being entered for the first time in the previous if.
			j = j + 2;
		
		if (this.i == permutation.size())
			return null;
		
		return permutation.insert(i, j++);
	}
	
	@Override
	public void resetNeighbourhood() {
		this.i = 0;
		this.j = 1;
	}

	@Override
	public int getNeighbourhoodSize(Permutation permutation) {
		if (this.size > 0)
			return this.size;
		
		int temp = permutation.size()-1;
		this.size = temp*temp;
		return this.size;
	}

}
