package neighbourhood_generator;

import main.Permutation;

public class VNDTransposeInsertExchange extends AbstractNeighbourhoodGenerator {

	private AbstractNeighbourhoodGenerator transpose;
	private AbstractNeighbourhoodGenerator insert;
	private AbstractNeighbourhoodGenerator exchange;
	
	public VNDTransposeInsertExchange(){
		super();
		
		this.transpose = new TransposeNeighbourhoodGenerator();
		this.insert = new InsertNeighbourhoodGenerator();
		this.exchange = new ExchangeNeighbourhoodGenerator();
	}

	public Permutation getNextNeighbour(Permutation permutation){
		super.getNextNeighbour(permutation);
		
		Permutation newPermutation = null;
		
		newPermutation = this.transpose.getNextNeighbour(permutation);
		if (newPermutation == null)
			newPermutation = this.insert.getNextNeighbour(permutation);
		if (newPermutation == null)
			newPermutation = this.exchange.getNextNeighbour(permutation);
		
		return newPermutation;
	}
	
	@Override
	public void resetNeighbourhood() {
		// Nothing to do here. The 3 sub generators will reset when they will see another incoming permutation.
	}

	@Override
	public int getNeighbourhoodSize(Permutation permutation) {
		return 0;
	}

}
