package neighbourhood_generator;

import main.Permutation;

public class ExchangeNeighbourhoodGenerator extends AbstractNeighbourhoodGenerator {

	private int i = 0;
	private int j = 1;
	
	@Override
	public Permutation getNextNeighbour(Permutation permutation){
		super.getNextNeighbour(permutation);
		
		if (this.j == permutation.size()){
			++i;
			j = i + 1;
		}
		
		if (this.i == permutation.size()-1)
			return null;
		
		return permutation.swap(i, j++);
	}

	@Override
	void resetNeighbourhood() {
		this.i = 0;
		this.j = 1;
	}

}
