package main;

public class Permutation {
	private int[] jobs;
	private int[][] completionTimes;
	private int[] jobsWeightedTardiness;
	private int newValueIndex = 0;
	private Instance instance;

	public Permutation(Instance instance){
		super();
		this.instance = instance;

		int amountOfJobs = instance.getJobsAmount();
		this.jobs = new int[amountOfJobs];

		for (int i = 0; i < amountOfJobs; ++i)
			this.jobs[i] = i;

		this.jobsWeightedTardiness = new int[amountOfJobs];
		this.completionTimes = new int[instance.getJobsAmount()][instance.getMachineAmount()];
		this.getTotalWeightedTardiness();
	}

	private Permutation(Instance instance, int[] jobs, int[][] otherCompletionTimes, int[] jobsWeightedTardiness, int newValueIndex){
		this.instance = instance;
		this.jobs = jobs;
		this.completionTimes = otherCompletionTimes.clone();
		this.jobsWeightedTardiness = jobsWeightedTardiness.clone();
		this.newValueIndex = newValueIndex;
		
		this.getTotalWeightedTardiness();
	}

	public int getTotalWeightedTardiness(){

		// No need to recalculate if nothing has changed since the previous call.
		if (this.newValueIndex == instance.getJobsAmount())
			return this.jobsWeightedTardiness[instance.getJobsAmount()-1];

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

		return this.jobsWeightedTardiness[instance.getMachineAmount()-1];
	}
	
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

	public Permutation swap(int i, int j){
		
		int[] jobs = this.jobs.clone();
		int temp = jobs[i];
		jobs[i] = jobs[j];
		jobs[j] = temp;
		return new Permutation(this.instance, jobs, this.completionTimes, this.jobsWeightedTardiness, Math.min(i, j));
	}

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
		return new Permutation(this.instance, jobs, this.completionTimes, this.jobsWeightedTardiness, Math.min(jobNumber, placeNumber));
	}

	public int size(){
		return this.jobs.length;
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
		this.newValueIndex = 0;
	}

	public String toString(){
		String str = "";
		
		for (int i = 0; i < this.size(); ++i){
			str += this.jobs[i] + ",";
		}
		str += "\n";
		for (int i = 0; i < this.size(); ++i){
			str += this.jobsWeightedTardiness[i] + ",";
		}

		return str + " --> " + this.getTotalWeightedTardiness();
	}
	
	public int getWeightedEarlyness(int jobNumber){
		if (this.newValueIndex <= jobNumber)
			this.computeCompletionTimes(jobNumber);
		// Here the newValueIndex is not updated to jobNumber, since we still need to update the jobsWeightedTardiness vector when 
		// we want the total weighted tardiness (update from the current index newIndexValue).
		
		return this.instance.getPriorityFor(this.jobs[jobNumber]) * (this.instance.getDueDateFor(this.jobs[jobNumber]) - this.completionTimes[jobNumber][this.instance.getMachineAmount()-1]);
	}
}
