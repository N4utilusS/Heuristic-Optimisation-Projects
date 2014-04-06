package main;

public class Permutation {
	private int[] jobs;
	private int[][] completionTimes;
	private int newValueIndex = 0;
	
	public Permutation(int amountOfJobs){
		super();
		this.jobs = new int[amountOfJobs];
		
		for (int i = 0; i < amountOfJobs; ++i)
			this.jobs[i] = i;
	}
	
	public Permutation(int[] jobs, int[][] otherCompletionTimes, int newValueIndex){
		this.jobs = jobs;
		this.completionTimes = otherCompletionTimes;
		this.newValueIndex = newValueIndex;
	}
	
	public int getTotalWeightedTardiness(int[][] processingTimes, int[] dueDates, int[] priorities){
		
		// Update the newValueIndex value, since the total weighted tardiness is up to date.
		// Thanks to this, each time this method will be called, no calculation will be needed to return the cost.
		this.newValueIndex = processingTimes[0].length;
		return 0;
	}
	
	public Permutation swap(int i, int j){
		int[] jobs = this.jobs.clone();
		int temp = jobs[i];
		jobs[i] = jobs[j];
		jobs[j] = temp;
		return new Permutation(jobs, this.completionTimes, Math.min(i, j));
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
		return new Permutation(jobs, this.completionTimes, Math.min(jobNumber, placeNumber));
	}
	
	public int size(){
		return this.jobs.length;
	}
}
