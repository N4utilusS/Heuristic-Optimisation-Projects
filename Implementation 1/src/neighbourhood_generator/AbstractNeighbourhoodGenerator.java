package neighbourhood_generator;
import main.Permutation;


public abstract class AbstractNeighbourhoodGenerator {
	private Permutation permutation;

	public Permutation getNextNeighbour(Permutation permutation){
		if (this.permutation == null || this.permutation != permutation){
			this.resetNeighbourhood();
			this.permutation = permutation;
		}
		return null;
	}
	
	abstract void resetNeighbourhood();
}
