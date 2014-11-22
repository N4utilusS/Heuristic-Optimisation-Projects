package neighbourhood_generator;

import main.Permutation;

public class VNDTransposeExchangeInsert extends AbstractNeighbourhoodGenerator {
	
	private AbstractNeighbourhoodGenerator transpose;
	private AbstractNeighbourhoodGenerator exchange;
	private AbstractNeighbourhoodGenerator insert;
	
	public VNDTransposeExchangeInsert(){
		super();
		
		this.transpose = new TransposeNeighbourhoodGenerator();
		this.exchange = new ExchangeNeighbourhoodGenerator();
		this.insert = new InsertNeighbourhoodGenerator();
	}

	public Permutation getNextNeighbour(Permutation permutation){
		super.getNextNeighbour(permutation);
		
		Permutation newPermutation = null;
		
		newPermutation = this.transpose.getNextNeighbour(permutation);
		if (newPermutation == null)
			newPermutation = this.exchange.getNextNeighbour(permutation);
		if (newPermutation == null)
			newPermutation = this.insert.getNextNeighbour(permutation);
		
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
