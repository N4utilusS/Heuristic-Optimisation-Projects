package main;

public class Instance {
	
	private int[][] processingTimes;
	private int[] dueDates;
	private int[] priorities;
	private int instanceNumber;
	
	public Instance(int[][] processingTimes, int[] dueDates, int[] priorities, int instanceNumber){
		this.processingTimes = processingTimes;
		this.dueDates = dueDates;
		this.priorities = priorities;
		this.instanceNumber = instanceNumber;
	}

	public int[][] getProcessingTimes() {
		return processingTimes;
	}

	public int[] getDueDates() {
		return dueDates;
	}

	public int[] getPriorities() {
		return priorities;
	}
	
	public int getJobsAmount(){
		return this.processingTimes.length;
	}
	
	public int getMachineAmount(){
		return this.processingTimes[0].length;
	}
	
	public int getProcessingTimeFor(int job, int machine){
		return this.processingTimes[job][machine];
	}
	
	public int getPriorityFor(int job){
		return this.priorities[job];
	}
	
	public int getDueDateFor(int job){
		return this.dueDates[job];
	}

	public int getInstanceNumber() {
		return instanceNumber;
	}
}
