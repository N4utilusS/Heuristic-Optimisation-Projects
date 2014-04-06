package neighbourhood_generator;

import main.Permutation;

public class InsertNeighbourhoodGenerator extends AbstractNeighbourhoodGenerator {

	private int i = 0;
	private int j = 1;
	@Override
	public Permutation getNextNeighbour(Permutation permutation){
		super.getNextNeighbour(permutation);
		
		if (j == permutation.size()){
			i++;
			j = 0;
		}
		
		if (i == permutation.size())
			return null;
		return permutation.insert(i, j++);
	}
	
	@Override
	void resetNeighbourhood() {
		this.i = 0;
		this.j = 1;
	}

}
