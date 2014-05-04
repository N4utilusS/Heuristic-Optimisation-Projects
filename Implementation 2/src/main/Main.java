package main;


import static main.Algorithm.*;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;

/**
 * The main class of the project, receiving the arguments of the user.
 * @author anthonydebruyn
 *
 */
public class Main {

	/**
	 * The starting point of the program. Reads the arguments and creates the wanted modes for the algorithm.</br>
	 * The '--first' and '--best' strings can be used to define the pivoting mode at launch.</br>
	 * The '--transpose', '--exchange' and '--insert' for the neighbourhood mode.</br>
	 * The '--random_init' and '--slack_init' for the initial solution generation mode.</br>
	 * </br>
	 * The '--file' string can be used followed by a filename to specify an instance file.</br>
	 * The '--batch' string can be used to specify a directory containing instance files.</br>
	 * @param args The arguments controlling the execution mode.
	 */
	public static void main(String[] args) {

		int pivotingMode = FIRST_MODE;
		int neighbourhoodMode = TRANSPOSE_MODE;
		int initMode = RANDOM_INIT;
		int ils = ILS_OFF;
		File file = null;
		boolean batch = false;
		boolean batch_vnd = false;
		boolean batch_sls = false;

		for (int i = 0; i < args.length && !batch; ++i){
			switch (args[i]){
			// The pivoting mode:
			case "--first":
				pivotingMode = FIRST_MODE;
				break;
			case "--best":
				pivotingMode = BEST_MODE;
				break;
				// The neighbourhood mode:
			case "--transpose":
				neighbourhoodMode = TRANSPOSE_MODE;
				break;
			case "--exchange":
				neighbourhoodMode = EXCHANGE_MODE;
				break;
			case "--insert":
				neighbourhoodMode = INSERT_MODE;
				break;
			case "--vnd_tei":
				neighbourhoodMode = VND_TRANSPOSE_EXCHANGE_INSERT_MODE;
				break;
			case "--vnd_tie":
				neighbourhoodMode = VND_TRANSPOSE_INSERT_EXCHANGE_MODE;
				break;
				// The initial solution mode:
			case "--random_init":
				initMode = RANDOM_INIT;
				break;
			case "--slack_init":
				initMode = SLACK_INIT;
				break;
				// The ILS activation:
			case "--ils":
				ils = ILS_ON;
				break;
				// The file mode:
			case "--file":
				file = new File(args[++i]);
				break;
			case "--batch":
				file = new File(args[++i]);
				batch = true;
				break;
			case "--batch_vnd":
				file = new File(args[++i]);
				batch_vnd = true;
				break;
			case "--bach_sls":
				file = new File(args[++i]);
				batch_sls = true;
				break;
			default:
				System.out.println("Unknown Argument: " + args[i]);
			}
		}

		if (batch){
			batch(file);
		} else if (batch_vnd){
			batch_vnd(file);
		} else if (batch_sls){
			batch_sls(file);
		} else {
			new Algorithm(pivotingMode, neighbourhoodMode, initMode, ils).findSolution(file);
		}
	}
	
	private static void batch_sls(File file) {
		if (!file.isDirectory()){
			System.out.println("The file '" + file.getName() + "' is not a directory.");
			return;
		}

		// A filter to avoid the system hidden files, like '.DS_Store' on MAC OS.
		FilenameFilter filter = new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return !name.equals(".DS_Store");
			}

		};
		File[] files = file.listFiles(filter);

		// -------------------------------------------------------
		// We look at the 2 SLS algorithms (only slack init mode).
		// First the SA:
		// -------------------------------------------------------
		
		int pivotingMode = SA_MODE;
		int neighbourhoodMode = INSERT_MODE;
		int initMode = SLACK_INIT;

		Algorithm itImp = new Algorithm(pivotingMode, neighbourhoodMode, initMode, ILS_OFF);

		String name = INIT_MODES[initMode] + NEIGHBOURHOOD_MODES[neighbourhoodMode] + PIVOTING_MODES[pivotingMode] + ILS_MODES[ILS_OFF];
		String nameAvRelPerDev = "R-avRelPer" + INIT_MODES[initMode] + NEIGHBOURHOOD_MODES[neighbourhoodMode] + PIVOTING_MODES[pivotingMode] + ILS_MODES[ILS_OFF] + ".dat";

		try(BufferedWriter writer = new BufferedWriter(new FileWriter(name)); BufferedWriter writer2 = new BufferedWriter(new FileWriter(nameAvRelPerDev));){

			int averageRelativePercentageDeviation = 0;
			long sumOfComputationTime = 0;

			for (File instanceFile : files){
				// Run each algorithm 5 times on each instance:
				int relPerDev = 0;
				int cost = 0;
				
				Map<String, Object> results = null;
				for (int i = 0; i < 5; ++i) {
					results = itImp.findSolution(instanceFile);
					relPerDev += (int) results.get(RELATIVE_PERCENTAGE_DEVIATION);
					cost += (int) results.get(COST);
				}
				
				relPerDev /= 5;
				cost /= 5;

				// Change these 2 lines to change the content of the results files for each algorithm:
				writer.write(instanceFile.getName() + "\t" + relPerDev + "\t" + (long) results.get(COMPUTATION_TIME) + "\t" + cost + "\t" + (int) results.get(BEST_KNOWN) + "\n");
				writer2.write(relPerDev + "\n");	// Files used mainly to compute the p-values, contain only the relative percentage deviations.

				averageRelativePercentageDeviation += relPerDev;
				sumOfComputationTime += (long) results.get(COMPUTATION_TIME);
			}

			// At the end of each file:
			averageRelativePercentageDeviation /= files.length;
			writer.write(averageRelativePercentageDeviation + "\n");	// The average relative percentage deviation for the algorithm.
			writer.write(Long.toString(sumOfComputationTime) + "\n");	// The sum of the computation times for the algorithm.
			sumOfComputationTime /= files.length;
			writer.write(Long.toString(sumOfComputationTime) + "\n");	// The average of the computation times for the algorithm.
			writer.flush();
			writer2.flush();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

		// -------------------------------------------------------
		// Then the ILS:
		// -------------------------------------------------------

		pivotingMode = FIRST_MODE;
		neighbourhoodMode = INSERT_MODE;
		initMode = SLACK_INIT;

		itImp = new Algorithm(pivotingMode, neighbourhoodMode, initMode, ILS_ON);

		name = INIT_MODES[initMode] + NEIGHBOURHOOD_MODES[neighbourhoodMode] + PIVOTING_MODES[pivotingMode] + ILS_MODES[ILS_OFF];
		nameAvRelPerDev = "R-avRelPer" + INIT_MODES[initMode] + NEIGHBOURHOOD_MODES[neighbourhoodMode] + PIVOTING_MODES[pivotingMode] + ILS_MODES[ILS_OFF] + ".dat";

		try(BufferedWriter writer = new BufferedWriter(new FileWriter(name)); BufferedWriter writer2 = new BufferedWriter(new FileWriter(nameAvRelPerDev));){

			int averageRelativePercentageDeviation = 0;
			long sumOfComputationTime = 0;

			for (File instanceFile : files){
				// Run each algorithm 5 times on each instance:
				int relPerDev = 0;
				int cost = 0;

				Map<String, Object> results = null;
				for (int i = 0; i < 5; ++i) {
					results = itImp.findSolution(instanceFile);
					relPerDev += (int) results.get(RELATIVE_PERCENTAGE_DEVIATION);
					cost += (int) results.get(COST);
				}

				relPerDev /= 5;
				cost /= 5;

				// Change these 2 lines to change the content of the results files for each algorithm:
				writer.write(instanceFile.getName() + "\t" + relPerDev + "\t" + (long) results.get(COMPUTATION_TIME) + "\t" + cost + "\t" + (int) results.get(BEST_KNOWN) + "\n");
				writer2.write(relPerDev + "\n");	// Files used mainly to compute the p-values, contain only the relative percentage deviations.

				averageRelativePercentageDeviation += relPerDev;
				sumOfComputationTime += (long) results.get(COMPUTATION_TIME);
			}

			// At the end of each file:
			averageRelativePercentageDeviation /= files.length;
			writer.write(averageRelativePercentageDeviation + "\n");	// The average relative percentage deviation for the algorithm.
			writer.write(Long.toString(sumOfComputationTime) + "\n");	// The sum of the computation times for the algorithm.
			sumOfComputationTime /= files.length;
			writer.write(Long.toString(sumOfComputationTime) + "\n");	// The average of the computation times for the algorithm.
			writer.flush();
			writer2.flush();

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}


	}
	
	private static void batch_vnd(File file) {

		if (!file.isDirectory()){
			System.out.println("The file '" + file.getName() + "' is not a directory.");
			return;
		}

		// A filter to avoid the system hidden files, like '.DS_Store' on MAC OS.
		FilenameFilter filter = new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return !name.equals(".DS_Store");
			}

		};
		File[] files = file.listFiles(filter);

		// We look at all the VND algorithms (only first pivoting mode).
		int pivotingMode = FIRST_MODE;
		
		for (int initMode = 0; initMode <= 1; ++initMode){
			for (int neighbourhoodMode = VND_TRANSPOSE_EXCHANGE_INSERT_MODE; neighbourhoodMode <= VND_TRANSPOSE_INSERT_EXCHANGE_MODE; ++neighbourhoodMode){

				Algorithm itImp = new Algorithm(pivotingMode, neighbourhoodMode, initMode, ILS_OFF);

				String name = INIT_MODES[initMode] + NEIGHBOURHOOD_MODES[neighbourhoodMode] + PIVOTING_MODES[pivotingMode];
				String nameAvRelPerDev = "R-avRelPer" + INIT_MODES[initMode] + NEIGHBOURHOOD_MODES[neighbourhoodMode] + PIVOTING_MODES[pivotingMode] + ".dat";

				try(BufferedWriter writer = new BufferedWriter(new FileWriter(name)); BufferedWriter writer2 = new BufferedWriter(new FileWriter(nameAvRelPerDev));){

					int averageRelativePercentageDeviation = 0;
					long sumOfComputationTime = 0;

					for (File instanceFile : files){
						Map<String, Object> results = itImp.findSolution(instanceFile);

						// Change these 2 lines to change the content of the results files for each algorithm:
						writer.write(instanceFile.getName() + "\t" + results.get(RELATIVE_PERCENTAGE_DEVIATION) + "\t" + results.get(COMPUTATION_TIME) + "\t" + results.get(COST) + "\t" + results.get(BEST_KNOWN) + "\n");
						writer2.write(results.get(RELATIVE_PERCENTAGE_DEVIATION) + "\n");	// Files used mainly to compute the p-values, contain only the relative percentage deviations.

						averageRelativePercentageDeviation += (int) results.get(RELATIVE_PERCENTAGE_DEVIATION);
						sumOfComputationTime += (long) results.get(COMPUTATION_TIME);
					}

					// At the end of each file:
					averageRelativePercentageDeviation /= files.length;
					writer.write(averageRelativePercentageDeviation + "\n");	// The average relative percentage deviation for the algorithm.
					writer.write(Long.toString(sumOfComputationTime) + "\n");	// The sum of the computation times for the algorithm.
					sumOfComputationTime /= files.length;
					writer.write(Long.toString(sumOfComputationTime) + "\n");	// The average of the computation times for the algorithm.
					writer.flush();
					writer2.flush();

				} catch (IOException e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}

			}

		}
	}

	/**
	 * This method is used in case the batch string is used with a directory name.
	 * It calls each type of algorithm on each instance in the specified folder.
	 * The results are written in files with the algorithms modes as names.
	 * These files contain thus a line per instance.
	 * @param file The {@link File} object representing the folder containing all the instances.
	 */
	private static void batch(File file){
		if (!file.isDirectory()){
			System.out.println("The file '" + file.getName() + "' is not a directory.");
			return;
		}

		// A filter to avoid the system hidden files, like '.DS_Store' on MAC OS.
		FilenameFilter filter = new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return !name.equals(".DS_Store");
			}

		};
		File[] files = file.listFiles(filter);

		// We look at all the possible algorithms (not vnd).
		for (int initMode = 0; initMode <= 1; ++initMode){
			for (int neighbourhoodMode = 0; neighbourhoodMode <= 2; ++neighbourhoodMode){
				for(int pivotingMode = 0; pivotingMode <= 1; ++pivotingMode){

					Algorithm itImp = new Algorithm(pivotingMode, neighbourhoodMode, initMode, ILS_OFF);

					String name = INIT_MODES[initMode] + NEIGHBOURHOOD_MODES[neighbourhoodMode] + PIVOTING_MODES[pivotingMode];
					String nameAvRelPerDev = "R-avRelPer" + INIT_MODES[initMode] + NEIGHBOURHOOD_MODES[neighbourhoodMode] + PIVOTING_MODES[pivotingMode] + ".dat";

					try(BufferedWriter writer = new BufferedWriter(new FileWriter(name)); BufferedWriter writer2 = new BufferedWriter(new FileWriter(nameAvRelPerDev));){

						int averageRelativePercentageDeviation = 0;
						long sumOfComputationTime = 0;

						for (File instanceFile : files){
							Map<String, Object> results = itImp.findSolution(instanceFile);

							// Change these 2 lines to change the content of the results files for each algorithm:
							writer.write(instanceFile.getName() + "\t" + results.get(RELATIVE_PERCENTAGE_DEVIATION) + "\t" + results.get(COMPUTATION_TIME) + "\t" + results.get(COST) + "\t" + results.get(BEST_KNOWN) + "\n");
							writer2.write(results.get(RELATIVE_PERCENTAGE_DEVIATION) + "\n");	// Files used mainly to compute the p-values, contain only the relative percentage deviations.

							averageRelativePercentageDeviation += (int) results.get(RELATIVE_PERCENTAGE_DEVIATION);
							sumOfComputationTime += (long) results.get(COMPUTATION_TIME);
						}

						// At the end of each file:
						averageRelativePercentageDeviation /= files.length;
						writer.write(averageRelativePercentageDeviation + "\n");	// The average relative percentage deviation for the algorithm.
						writer.write(Long.toString(sumOfComputationTime) + "\n");	// The sum of the computation times for the algorithm.
						sumOfComputationTime /= files.length;
						writer.write(Long.toString(sumOfComputationTime) + "\n");	// The average of the computation times for the algorithm.
						writer.flush();
						writer2.flush();

					} catch (IOException e) {
						e.printStackTrace();
						System.out.println(e.getMessage());
					}

				}
			}
		}

	}

}
