package main;
import initial_solution.InitialSolutionGenerator;
import initial_solution.RandomInitialSolutionGenerator;
import initial_solution.SlackInitialSolutionGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import neighbourhood_generator.AbstractNeighbourhoodGenerator;
import neighbourhood_generator.ExchangeNeighbourhoodGenerator;
import neighbourhood_generator.InsertNeighbourhoodGenerator;
import neighbourhood_generator.TransposeNeighbourhoodGenerator;
import neighbourhood_generator.VNDTransposeExchangeInsert;
import neighbourhood_generator.VNDTransposeInsertExchange;
import pivoting_manager.AbstractPivotingManager;
import pivoting_manager.BestPivotingManager;
import pivoting_manager.FirstPivotingManager;

/**
 * Main class of the algorithm.
 * Iterates through the solutions until the run time is over limit.
 * This class uses an {@link AbstractNeighbourhoodGenerator} together with a {@link AbstractPivotingManager} to get new solutions based on the current one.
 * @author anthonydebruyn
 *
 */
public class Algorithm {

	public final static int TRANSPOSE_MODE = 0;
	public final static int EXCHANGE_MODE = 1;
	public final static int INSERT_MODE = 2;
	public final static int VND_TRANSPOSE_EXCHANGE_INSERT_MODE = 3;
	public final static int VND_TRANSPOSE_INSERT_EXCHANGE_MODE = 4;

	public final static int FIRST_MODE = 0;
	public final static int BEST_MODE = 1;

	public final static int RANDOM_INIT = 0;
	public final static int SLACK_INIT = 1;
	
	public final static String RELATIVE_PERCENTAGE_DEVIATION = "Relative Percentage Deviation";
	public final static String COMPUTATION_TIME = "Computation Time";
	public final static String BEST_KNOWN = "Best Known";
	public final static String COST = "Cost";
	
	public final static String[] INIT_MODES = {
		"--random",
		"--slack"
	};
	
	public final static String[] NEIGHBOURHOOD_MODES = {
		"--transpose",
		"--exchange",
		"--insert",
		"--vnd_tei",
		"--vnd_tie"
	};
	
	public final static String[] PIVOTING_MODES = {
		"--first",
		"--best",
		"--sa"
	};


	private int pivotingMode;
	private int neighbourhoodMode;
	private int initMode;
	private Instance instance;


	/**
	 * The constructor receiving the functioning mode of the algorithm.
	 * @param pivotingMode The id of the pivoting mode, see the constants.
	 * @param neighbourhoodMode The id of the neighbourhood mode, see the constants.
	 * @param initMode The id of the first solution generation mode, see the constants.
	 */
	Algorithm(int pivotingMode, int neighbourhoodMode, int initMode){
		super();
		this.pivotingMode = pivotingMode;
		this.neighbourhoodMode = neighbourhoodMode;
		this.initMode = initMode;
	}

	/**
	 * Get a solution from the algorithm.
	 * The returned solution is a local maximum.
	 * The mode of the algorithm must be defined with the constructor.
	 * @param file The file of the instance of the problem.
	 * @return A Map<String, Object> containing the properties of the computation and solution (see the constants).
	 * RELATIVE_PERCENTAGE_DEVIATION, 
	 * COMPUTATION_TIME, 
	 * BEST_KNOWN, 
	 * COST
	 */
	public Map<String, Object> findSolution(File file){

		// Start the timer
		long timerStart = System.currentTimeMillis();

		// Find the size of the problem (#jobs and #machines), and other information
		getInformation(file);
		//System.out.println(this.instance);

		// Get the runtime.
		long runTime = getRunTime();

		// Get the initial solution/permutation
		InitialSolutionGenerator initialSolutionGenerator = getInitialSolutionGenerator();

		Permutation permutation = initialSolutionGenerator.getInitialSolution(this.instance);
		//Permutation permutation = new Permutation(this.instance);
		//System.out.println(permutation);

		// Get the neighbourhood generator
		AbstractNeighbourhoodGenerator neighbourhoodGenerator = getNeighbourhoodGenerator();

		// Get the pivoting manager
		AbstractPivotingManager pivotingManager = getPivotingManager(neighbourhoodGenerator);
		pivotingManager.init(permutation);

		// Iterate while not local optimum
		Permutation newPermutation = permutation;

		int stepCounter = 0;
		
		do {
			permutation = newPermutation;
			newPermutation = pivotingManager.getNewPermutation(permutation);
			stepCounter++;
		} while (System.currentTimeMillis() - timerStart < runTime);

		int bestKnown = getBestKnown();

		// Print some results on the console.
		System.out.println(file.getName() + "\t" + this.initMode + "-" + this.neighbourhoodMode + "-" + this.pivotingMode + "\t" + newPermutation.getTotalWeightedTardiness() + "\t" + bestKnown + "\tin " + runTime + " ms\tin " + stepCounter + " steps");
		
		// Send the results back.
		Map<String, Object> results = new HashMap<String, Object>();
		int relPerDev = (int) (100.0f * (float)(newPermutation.getTotalWeightedTardiness() - bestKnown) / (float)(bestKnown));
		results.put(RELATIVE_PERCENTAGE_DEVIATION, relPerDev);
		results.put(COMPUTATION_TIME, runTime);
		results.put(BEST_KNOWN, bestKnown);
		results.put(COST, newPermutation.getTotalWeightedTardiness());
		
		return results;
	}
	

	private long getRunTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Get an instance of the pivoting manager corresponding to the chosen mode.
	 * @param neighbourhoodGenerator The neighbourhood generator to use with the returned pivoting manager.
	 * @return The wanted pivoting manager. If the id is not known, the default one is returned (first pivoting manager).
	 */
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

	/**
	 * Get an instance of the neighbourhood generator corresponding to the chosen mode.
	 * @return The wanted neighbourhood generator. If the id is not known, the default one is returned (transpose neighbourhood generator).
	 */
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
		case VND_TRANSPOSE_EXCHANGE_INSERT_MODE:
			neighbourhoodGenerator = new VNDTransposeExchangeInsert();
			break;
		case VND_TRANSPOSE_INSERT_EXCHANGE_MODE:
			neighbourhoodGenerator = new VNDTransposeInsertExchange();
			break;
		default:
			neighbourhoodGenerator = new TransposeNeighbourhoodGenerator();
		}
		
		return neighbourhoodGenerator;
	}

	/**
	 * Get an instance of the initial solution generator corresponding to the chosen mode.
	 * @return The wanted initial solution generator. If the id is not known, the default one is returned (random initial solution generator).
	 */
	private InitialSolutionGenerator getInitialSolutionGenerator() {
		InitialSolutionGenerator initialSolutionGenerator = null;
		
		switch (this.initMode){
		case RANDOM_INIT:
			initialSolutionGenerator = new RandomInitialSolutionGenerator();
			break;
		case SLACK_INIT:
			initialSolutionGenerator = new SlackInitialSolutionGenerator();
			break;
		default:
			initialSolutionGenerator = new RandomInitialSolutionGenerator();
		}
		
		return initialSolutionGenerator;
	}

	/**
	 * Get the information from the instance file provided, to set the size of the problem.
	 * Get the amount of jobs and machines, the processing times, the due dates, the priorities and the instance number 
	 * (the number at the end of the filename, if there are several instances with the same number of jobs and machines).
	 * Prints errors on the console for certain known errors, like a missing number.
	 * @param file The file containing the information for the instance.
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

	/**
	 * Get the best known solution from the file containing all the best solutions.
	 * @return An integer equal to the cost of the best solution found for the current instance.
	 */
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
