package main;

/**
 * Represents an instance of the PFSP problem.
 * Contains the processing times, the due dates, the priorities and the instance number if several instances have the same jobs and machines numbers.
 * @author anthonydebruyn
 *
 */
public class Instance {
	
	private int[][] processingTimes;
	private int[] dueDates;
	private int[] priorities;
	private int instanceNumber;
	
	/**
	 * Constructor taking all the fields of the instance directly.
	 * @param processingTimes The processing times of the different machines for all jobs.
	 * @param dueDates The due dates for all the jobs.
	 * @param priorities The priorities for all the jobs.
	 * @param instanceNumber The instance number, if several instances have the same jobs and machines numbers.
	 */
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
	
	public String toString(){
		String str = "";
		
		str += "Instance Number: " + instanceNumber + "\n";
		str += "Processing Times:\n";
		
		for (int i = 0; i < this.getJobsAmount(); ++i){
			for (int j = 0; j < this.getMachineAmount(); ++j){
				str += this.processingTimes[i][j] + "\t";
			}
			str += "\n";
		}
		
		str += "Due Dates:\n";
		
		for (int i = 0; i < this.getJobsAmount(); ++i){
			str += this.dueDates[i] + "\t";
		}
		
		str += "\nPriorities:\n";
		
		for (int i = 0; i < this.getJobsAmount(); ++i){
			str += this.priorities[i] + "\t";
		}
		
		return str;
	}
}
