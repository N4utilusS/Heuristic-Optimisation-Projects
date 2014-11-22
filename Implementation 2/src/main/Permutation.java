package main;

/**
 * The class representing a solution, a permutation of the jobs.
 * @author anthonydebruyn
 *
 */
public class Permutation {
	private int[] jobs;
	private int[][] completionTimes;
	private int[] jobsWeightedTardiness;
	private int newValueIndex = 0;
	private Instance instance;

	/**
	 * Creates a simple permutation, with the jobs in the order they are declared (normal order, job 1 at spot 1...).
	 * Initialises all the vectors needed.
	 * @param instance The instance object of the current problem.
	 */
	public Permutation(Instance instance){
		super();
		this.instance = instance;

		int amountOfJobs = instance.getJobsAmount();
		this.jobs = new int[amountOfJobs];

		for (int i = 0; i < amountOfJobs; ++i)
			this.jobs[i] = i;

		this.jobsWeightedTardiness = new int[amountOfJobs];
		this.completionTimes = new int[instance.getJobsAmount()][instance.getMachineAmount()];
	}

	/**
	 * Creates a permutation from another one.
	 * Clones all the vectors from the mother permutation, but not the instance object.
	 * The newValueIndex is not copied. It is deduced in the mother permutation from the operation giving birth to this new permutation.
	 * This value indicates the index from which the vectors need to be recalculated (the completionTimes and jobsWeightedTardiness).
	 * This decreases resource demand on execution.</br>
	 * Ex.: For a swap operation on the mother between jobs i and j: newValueIndex = min(mother's newValueIndex, i, j).
	 * @param instance The instance object of the current problem.
	 * @param jobs The vector listing the jobs in the order of the solution permutation (first job at cell 0, second at 1...).
	 * @param otherCompletionTimes The completion times matrix from the mother permutation, to decrease resource demand.
	 * @param jobsWeightedTardiness The computed tardiness of each job from the mother permutation.
	 * @param newValueIndex The newValueIndex computed in the mother permutation to indicate the first index in the completion matrix and tardiness that needs recalculation.
	 */
	private Permutation(Instance instance, int[] jobs, int[][] otherCompletionTimes, int[] jobsWeightedTardiness, int newValueIndex){
		this.instance = instance;
		this.jobs = jobs.clone();
		this.completionTimes = deepCopyIntMatrix(otherCompletionTimes);
		this.jobsWeightedTardiness = jobsWeightedTardiness.clone();
		this.newValueIndex = newValueIndex;

	}

	/**
	 * Used to clone a 2D matrix of integers.
	 * @param input The matrix we want a copy from.
	 * @return The copy.
	 */
	public static int[][] deepCopyIntMatrix(int[][] input) {
		if (input == null)
			return null;
		int[][] result = new int[input.length][];
		for (int r = 0; r < input.length; r++) {
			result[r] = input[r].clone();
		}
		return result;
	}

	/**
	 * Computes and returns the total weighted tardiness.
	 * Calculates the new values of the completion times matrix and tardiness vectors from the newValueIndex index.
	 * @return The total weighted tardiness for this permutation.
	 */
	public int getTotalWeightedTardiness(){

		// No need to recalculate if nothing has changed since the previous call.
		if (this.newValueIndex == instance.getJobsAmount())
			return this.jobsWeightedTardiness[instance.getJobsAmount()-1];

		// Compute the completion times.
		this.computeCompletionTimes(this.instance.getJobsAmount()-1);

		// Compute the tardiness of each job.
		int currentSum = (this.newValueIndex > 0) ? this.jobsWeightedTardiness[this.newValueIndex-1] : 0;
		for (int i = this.newValueIndex; i < instance.getJobsAmount(); ++i){
			this.jobsWeightedTardiness[i] = currentSum + instance.getPriorityFor(this.jobs[i]) * Math.max(this.completionTimes[i][instance.getMachineAmount()-1] - instance.getDueDateFor(this.jobs[i]), 0);
			currentSum = this.jobsWeightedTardiness[i];
		}

		// Update the newValueIndex value, since the total weighted tardiness is up to date.
		// Thanks to this, each time this method will be called, no calculation will be needed to return the cost.
		this.newValueIndex = instance.getJobsAmount();

		return this.jobsWeightedTardiness[instance.getJobsAmount()-1];
	}

	/**
	 * To be called after getTotalWeightedTardiness()!
	 */
	public int getMakespan(){
		// No need to recalculate if nothing has changed since the previous call.
		if (this.newValueIndex == instance.getJobsAmount())
			return this.completionTimes[instance.getJobsAmount()-1][instance.getMachineAmount()-1];

		// Compute the completion times.
		this.computeCompletionTimes(this.instance.getJobsAmount()-1);

		// Update the newValueIndex value, since the total weighted tardiness is up to date.
		// Thanks to this, each time this method will be called, no calculation will be needed to return the cost.
		this.newValueIndex = instance.getJobsAmount();

		return this.completionTimes[instance.getJobsAmount()-1][instance.getMachineAmount()-1];
	}

	/**
	 * Computes the completion times for the current permutation to the 'maxJobNumber'th job.
	 * The parameter is mainly used in the case of the slack heuristic where we do not need to compute all the matrix.
	 * @param maxJobNumber The maximum index of the completion times matrix to compute.
	 */
	private void computeCompletionTimes(int maxJobNumber){
		maxJobNumber = Math.min(maxJobNumber, this.instance.getJobsAmount()-1);
		maxJobNumber = Math.max(0, maxJobNumber);

		if (this.newValueIndex == 0){

			// Calculate the first column of the completion matrix.
			this.completionTimes[0][0] = instance.getProcessingTimeFor(this.jobs[0], 0);

			for (int i = 1; i < instance.getMachineAmount(); ++i){
				this.completionTimes[0][i] = this.completionTimes[0][i-1] + instance.getProcessingTimeFor(this.jobs[0], i);
			}
		}

		// Calculate the first line of the completion matrix.
		for (int i = Math.max(1, this.newValueIndex); i <= maxJobNumber; ++i){
			this.completionTimes[i][0] = this.completionTimes[i-1][0] + instance.getProcessingTimeFor(this.jobs[i], 0);
		}

		for (int i = Math.max(1, this.newValueIndex); i <= maxJobNumber; ++i){
			for (int j = 1; j < instance.getMachineAmount(); ++j){
				completionTimes[i][j] = Math.max(completionTimes[i-1][j], completionTimes[i][j-1]) + instance.getProcessingTimeFor(this.jobs[i], j);
			}
		}
	}

	/**
	 * Creates another permutation from this one with the jobs i and j swapped.
	 * @param i The index of the first job to be swapped.
	 * @param j The index of the second job to be swapped.
	 * @return The new permutation, with the newValueIndex = min(this.newValueIndex, i, j).
	 */
	public Permutation swap(int i, int j){

		int[] jobs = this.jobs.clone();
		int temp = jobs[i];
		jobs[i] = jobs[j];
		jobs[j] = temp;

		int min = Math.min(i, j);
		min = Math.min(this.newValueIndex, min);

		return new Permutation(this.instance, jobs, this.completionTimes, this.jobsWeightedTardiness, min);
	}

	/**
	 * Creates another permutation from this one with the 'jobNumber'th job inserted at the 'placeNumber'th spot.
	 * All the other jobs between the 2 are shifted.
	 * @param jobNumber The index of the job to take.
	 * @param placeNumber The spot number to put the job taken. These spot numbers correspond to the order of the jobs in the solution.
	 * @return The new permutation, with the newValueIndex = min(this.newValueIndex, jobNumber, placeNumber).
	 */
	public Permutation insert(int jobNumber, int placeNumber){
		int[] jobs = this.jobs.clone();
		int temp = jobs[jobNumber];

		if (placeNumber >= jobNumber){
			for (int i = jobNumber; i < placeNumber; ++i){
				jobs[i] = jobs[i+1];
			}
		} else {
			for (int i = jobNumber; i > placeNumber; --i){
				jobs[i] = jobs[i-1];
			}
		}


		jobs[placeNumber] = temp;

		int min = Math.min(jobNumber, placeNumber);
		min = Math.min(this.newValueIndex, min);

		return new Permutation(this.instance, jobs, this.completionTimes, this.jobsWeightedTardiness, min);
	}

	/**
	 * Returns the size of the current solution, the number of jobs.
	 * @return Guess what.
	 */
	public int size(){
		return this.jobs.length;
	}

	public String toString(){
		String str = "";

		for (int i = 0; i < this.size() -1; ++i){
			str += this.jobs[i] + ",";
		}
		str += this.jobs[this.size()-1]; //Last job, no need for a final comma.
//		str += "\n";
//		for (int i = 0; i < this.size(); ++i){
//			str += this.jobsWeightedTardiness[i] + ",";
//		}
		
		return str;

//		return str + " --> " + this.getTotalWeightedTardiness();
	}

	/**
	 * Computes and returns the weighted earliness of the permutation/solution, at 'jobNumber'th spot (0->jobsAmount-1).
	 * The value is computed for the 'jobNumber'th job.
	 * @param jobNumber The index of the job we want the weighted earliness from (index corresponds to the position in time of the job, 2 = 3rd job in time).
	 * @return The value of the weighted earliness for the given job.
	 */
	public int getWeightedEarliness(int jobNumber){
		if (this.newValueIndex <= jobNumber)
			this.computeCompletionTimes(jobNumber);
		// Here the newValueIndex is not updated to jobNumber, since we still need to update the jobsWeightedTardiness vector when 
		// we want the total weighted tardiness (update from the current index newIndexValue).

		return this.instance.getPriorityFor(this.jobs[jobNumber]) * (this.instance.getDueDateFor(this.jobs[jobNumber]) - this.completionTimes[jobNumber][this.instance.getMachineAmount()-1]);
	}
}
