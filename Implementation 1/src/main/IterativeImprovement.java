package main;
import initial_solution.InitialSolutionGenerator;
import initial_solution.RandomInitialSolutionGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import pivoting_manager.AbstractPivotingManager;
import pivoting_manager.FirstPivotingManager;

import neighbourhood_generator.AbstractNeighbourhoodGenerator;
import neighbourhood_generator.TransposeNeighbourhoodGenerator;


public class IterativeImprovement {
	
	public final static int TRANSPOSE_MODE = 0;
	public final static int EXCHANGE_MODE = 1;
	public final static int INSERT_MODE = 2;
	
	public final static int FIRST_MODE = 0;
	public final static int BEST_MODE = 1;
	
	public final static int RANDOM_INIT = 0;
	public final static int SLACK_INIT = 1;

	
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
		
		// Start the timer
		
		// Find the size of the problem (#jobs and #machines), and other information
		getInformation(file);
		
		// Get the initial solution/permutation
		InitialSolutionGenerator initialSolutionGenerator;
		
		switch (this.initMode){
		case RANDOM_INIT:
			initialSolutionGenerator = new RandomInitialSolutionGenerator();
			break;
		case SLACK_INIT:
			initialSolutionGenerator = new RandomInitialSolutionGenerator();	// TODO replace by other
			break;
		default:
			initialSolutionGenerator = new RandomInitialSolutionGenerator();
		}
		
		Permutation permutation = initialSolutionGenerator.getInitialSolution(this.jobsAmount);
		
		// Get the neighbourhood generator
		AbstractNeighbourhoodGenerator neighbourhoodGenerator;
		
		switch (this.neighbourhoodMode){
		case TRANSPOSE_MODE:
			neighbourhoodGenerator = new TransposeNeighbourhoodGenerator();
			break;
		case EXCHANGE_MODE:
			neighbourhoodGenerator = new ExchangeNeighbourhoodGenerator();
			break;
		case INSERT_MODE:
			neighbourhoodGenerator = new InsertNeighbourhoodGenerator();
		default:
			neighbourhoodGenerator = new TransposeNeighbourhoodGenerator();
		}
		
		// Get the pivoting manager
		AbstractPivotingManager pivotingManager;
		
		switch (this.pivotingMode){
		case FIRST_MODE:
			pivotingManager = new FirstPivotingManager(neighbourhoodGenerator);
			break;
		case BEST_MODE:
			pivotingManager = new BestPivotingManager(neighbourhoodGenerator);
			break;
		default:
			pivotingManager = new FirstPivotingManager(neighbourhoodGenerator);
		}
		
		// Iterate while not local optimum
		Permutation newPermutation;
		
		do {
			
		} while (newPermutation != permutation);
		
		// End the timer
	}

	/**
	 * Get the information from the file provided, to set the size of the problem.
	 * Get the amount of jobs and machines.
	 * @param file The file containing the information for the instances.
	 */
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
			
			// Instanciate the matrices:
			this.processingTimes = new int[this.jobsAmount][this.machineAmount];
			this.completionTimes = new int[this.jobsAmount][this.machineAmount];
			
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
			
			// Get the due dates and the priorities:
			this.dueDates = new int[this.jobsAmount];
			this.priorities = new int[this.jobsAmount];
			
			if (!scanner.hasNext())
				throw new Exception("Reldue string is missing in the file: " + file.getPath());
			scanner.next();
			
			for (int i = 0; i < this.jobsAmount; ++i){
				
				if (!scanner.hasNextInt())
					throw new Exception("-1 is missing in the file: " + file.getPath());
				scanner.next();
				
				if (!scanner.hasNextInt())
					throw new Exception("Due date is missing in the file: " + file.getPath());
				this.dueDates[i] = scanner.nextInt();
				if (this.dueDates[i] < 1)
					throw new Exception("Due date negative for job " + i + " in the file: " + file.getPath());
				
				if (!scanner.hasNextInt())
					throw new Exception("-1 is missing in the file: " + file.getPath());
				scanner.next();
				
				if (!scanner.hasNextInt())
					throw new Exception("Due date is missing in the file: " + file.getPath());
				this.priorities[i] = scanner.nextInt();
				if (this.priorities[i] < 1)
					throw new Exception("Priority negative for job " + i + " in the file: " + file.getPath());
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not available.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}
}
