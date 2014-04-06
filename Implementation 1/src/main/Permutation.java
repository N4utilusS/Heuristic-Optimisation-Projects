package main;

public class Permutation {
	private int[] jobs;
	
	public Permutation(int amountOfJobs){
		super();
		this.jobs = new int[amountOfJobs];
		
		for (int i = 0; i < amountOfJobs; ++i)
			this.jobs[i] = i;
	}
	
	public void swap(int i, int j){
		int temp = jobs[i];
		jobs[i] = jobs[j];
		jobs[j] = temp;
	}
	
	void insert(int jobNumber, int placeNumber){
		int temp = this.jobs[jobNumber];
		
		for (int i = jobNumber; i < placeNumber; ++i){
			this.jobs[i] = this.jobs[i+1];
		}
		
		this.jobs[placeNumber] = temp;
	}
	
	public int size(){
		return this.jobs.length;
	}
}
