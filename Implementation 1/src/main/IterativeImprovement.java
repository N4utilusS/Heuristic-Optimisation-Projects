package main;
import initial_solution.InitialSolutionGenerator;
import initial_solution.RandomInitialSolutionGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import neighbourhood_generator.AbstractNeighbourhoodGenerator;
import neighbourhood_generator.ExchangeNeighbourhoodGenerator;
import neighbourhood_generator.InsertNeighbourhoodGenerator;
import neighbourhood_generator.TransposeNeighbourhoodGenerator;
import pivoting_manager.AbstractPivotingManager;
import pivoting_manager.BestPivotingManager;
import pivoting_manager.FirstPivotingManager;


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
	private Instance instance;


	IterativeImprovement(int pivotingMode, int neighbourhoodMode, int initMode){
		super();
		this.pivotingMode = pivotingMode;
		this.neighbourhoodMode = neighbourhoodMode;
		this.initMode = initMode;
	}

	void findSolution(File file){

		// Start the timer
		long timer = System.currentTimeMillis();

		// Find the size of the problem (#jobs and #machines), and other information
		getInformation(file);
		//System.out.println(this.instance);

		// Get the initial solution/permutation
		InitialSolutionGenerator initialSolutionGenerator = getInitialSolutionGenerator();

		Permutation permutation = initialSolutionGenerator.getInitialSolution(this.instance);
		//Permutation permutation = new Permutation(this.instance);
		//System.out.println(permutation);

		// Get the neighbourhood generator
		AbstractNeighbourhoodGenerator neighbourhoodGenerator = getNeighbourhoodGenerator();

		// Get the pivoting manager
		AbstractPivotingManager pivotingManager = getPivotingManager(neighbourhoodGenerator);

		// Iterate while not local optimum
		Permutation newPermutation = permutation;

		int stepCounter = 0;
		
		do {
			permutation = newPermutation;
			newPermutation = pivotingManager.getNewPermutation(permutation);
			stepCounter++;
		} while (newPermutation != permutation);	// Local optimum when the permutations are the same (same pointer).

		// End the timer
		timer = System.currentTimeMillis() - timer;
		int bestKnown = this.getBestKnown();

		System.out.println(file.getName() + "\t" + newPermutation.getTotalWeightedTardiness() + "\t" + bestKnown + "\tin " + timer + " ms\tin " + stepCounter + " steps");
	}
	

	private AbstractPivotingManager getPivotingManager(AbstractNeighbourhoodGenerator neighbourhoodGenerator) {
		
		AbstractPivotingManager pivotingManager = null;

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
		
		return pivotingManager;
	}

	private AbstractNeighbourhoodGenerator getNeighbourhoodGenerator() {
		AbstractNeighbourhoodGenerator neighbourhoodGenerator = null;

		switch (this.neighbourhoodMode){
		case TRANSPOSE_MODE:
			neighbourhoodGenerator = new TransposeNeighbourhoodGenerator();
			break;
		case EXCHANGE_MODE:
			neighbourhoodGenerator = new ExchangeNeighbourhoodGenerator();
			break;
		case INSERT_MODE:
			neighbourhoodGenerator = new InsertNeighbourhoodGenerator();
			break;
		default:
			neighbourhoodGenerator = new TransposeNeighbourhoodGenerator();
		}
		
		return neighbourhoodGenerator;
	}

	private InitialSolutionGenerator getInitialSolutionGenerator() {
		InitialSolutionGenerator initialSolutionGenerator = null;
		
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
		
		return initialSolutionGenerator;
	}

	/**
	 * Get the information from the file provided, to set the size of the problem.
	 * Get the amount of jobs and machines.
	 * @param file The file containing the information for the instances.
	 */
	private void getInformation(File file) {
		int jobsAmount;
		int machineAmount;

		try(Scanner scanner = new Scanner(file)) {
			// Get the jobs amount:
			if (!scanner.hasNextInt())
				throw new Exception("Jobs amount is missing in the file: " + file.getPath());
			jobsAmount = scanner.nextInt();

			// Get the machine amount:
			if (!scanner.hasNextInt())
				throw new Exception("Machine amount is missing in the file: " + file.getPath());
			machineAmount = scanner.nextInt();

			// Instanciate the matrices:
			int[][] processingTimes = new int[jobsAmount][machineAmount];

			// Get the processing times:
			for (int i = 0; i < jobsAmount; ++i){
				for (int j = 0; j < machineAmount; ++j){
					if (!scanner.hasNextInt())
						throw new Exception("Machine number is missing for job " + (i+1) + " in the file: " + file.getPath());
					scanner.nextInt();
					if (!scanner.hasNextInt())
						throw new Exception("Machine processing time is missing for job " + (i+1) + ", machine " + (j+1) + ", in the file: " + file.getPath());
					processingTimes[i][j] = scanner.nextInt();
					if (processingTimes[i][j] < 0)
						processingTimes[i][j] = 0;

				}
			}

			// Get the due dates and the priorities:
			int[] dueDates = new int[jobsAmount];
			int[] priorities = new int[jobsAmount];

			if (!scanner.hasNext())
				throw new Exception("Reldue string is missing in the file: " + file.getPath());
			scanner.next();

			for (int i = 0; i < jobsAmount; ++i){

				if (!scanner.hasNextInt())
					throw new Exception("-1 is missing in the file: " + file.getPath());
				scanner.next();

				if (!scanner.hasNextInt())
					throw new Exception("Due date is missing in the file: " + file.getPath());
				dueDates[i] = scanner.nextInt();
				if (dueDates[i] < 1)
					throw new Exception("Due date negative for job " + i + " in the file: " + file.getPath());

				if (!scanner.hasNextInt())
					throw new Exception("-1 is missing in the file: " + file.getPath());
				scanner.next();

				if (!scanner.hasNextInt())
					throw new Exception("Due date is missing in the file: " + file.getPath());
				priorities[i] = scanner.nextInt();
				if (priorities[i] < 1)
					throw new Exception("Priority negative for job " + i + " in the file: " + file.getPath());
			}

			// Get the instance number (the last number in the file name).
			String number = file.getName().substring(file.getName().lastIndexOf("_")+1);

			this.instance = new Instance(processingTimes, dueDates, priorities, Integer.parseInt(number));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("File not available.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

	private int getBestKnown(){
		String prefix = instance.getJobsAmount() + "x" + instance.getMachineAmount() + " " + instance.getInstanceNumber() + " ";
		int bestKnown = 0;
		
		File best = new File("best_known.txt");
		try(Scanner scanner = new Scanner(best)){
			boolean stop = false;

			while (!stop && scanner.hasNextLine()){
				String line = scanner.nextLine();

				if (line.startsWith(prefix)){
					bestKnown = Integer.parseInt(line.substring(prefix.length()));
					stop = true;
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bestKnown;
	}
}
