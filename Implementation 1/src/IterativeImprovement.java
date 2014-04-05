import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class IterativeImprovement {
	
	private int pivotingMode;
	private int neighbourhoodMode;
	private int initMode;
	
	private int[][] processingTimes;
	private int[][] completionTimes;
	private int[] dueDates;
	private int[] priorities;
	private int jobsAmount;
	private int machineAmount;
	
	IterativeImprovement(int pivotingMode, int neighbourhoodMode, int initMode){
		super();
		this.pivotingMode = pivotingMode;
		this.neighbourhoodMode = neighbourhoodMode;
		this.initMode = initMode;
	}
	
	void findSolution(File file){
		// Find the size of the problem (#jobs and #machines), and other information
		getInformation(file);
		
		// Get the initial solution/permutation
		InitialSolutionGenerator initialSolutionGenerator = new RandomInitialSolutionGenerator();
		Permutation permutation = initialSolutionGenerator.getInitialSolution(jobsAmount);
		
		// 
		
		// Iterate while not local optimum
	}

	private void getInformation(File file) {
		
		try(Scanner scanner = new Scanner(file)) {
			// Get the jobs amount:
			if (!scanner.hasNextInt())
				throw new Exception("Jobs amount is missing in the file: " + file.getPath());
			this.jobsAmount = scanner.nextInt();
			
			// Get the machine amount:
			if (!scanner.hasNextInt())
				throw new Exception("Machine amount is missing in the file: " + file.getPath());
			this.machineAmount = scanner.nextInt();
			
			// Get the processing times:
			for (int i = 0; i < this.jobsAmount; ++i){
				for (int j = 0; j < this.machineAmount; ++j){
					if (!scanner.hasNextInt())
						throw new Exception("Machine number is missing for job " + (i+1) + " in the file: " + file.getPath());
					if (!scanner.hasNextInt())
						throw new Exception("Machine processing time is missing for job " + (i+1) + ", machine " + (j+1) + ", in the file: " + file.getPath());
					this.processingTimes[i][j] = scanner.nextInt();
					if (this.processingTimes[i][j] < 0)
						this.processingTimes[i][j] = 0;
					
				}
			}
			
			// Get the 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not available.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}
}
