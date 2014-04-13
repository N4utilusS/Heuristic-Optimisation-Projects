package main;


import static main.IterativeImprovement.*;



import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Map;

public class Main {

	/**
	 * The main class of the project, receiving the arguments of the user.
	 * @param args The arguments controlling the execution mode.
	 */
	public static void main(String[] args) {

		int pivotingMode = FIRST_MODE;
		int neighbourhoodMode = TRANSPOSE_MODE;
		int initMode = RANDOM_INIT;
		File file = null;
		boolean batch = false;

		for (int i = 0; i < args.length && !batch; ++i){
			switch (args[i]){
			case "--first":
				pivotingMode = FIRST_MODE;
				break;
			case "--best":
				pivotingMode = BEST_MODE;
				break;
			case "--transpose":
				neighbourhoodMode = TRANSPOSE_MODE;
				break;
			case "--exchange":
				neighbourhoodMode = EXCHANGE_MODE;
				break;
			case "--insert":
				neighbourhoodMode = INSERT_MODE;
				break;
			case "--random_init":
				initMode = RANDOM_INIT;
				break;
			case "--slack_init":
				initMode = SLACK_INIT;
				break;
			case "--file":
				file = new File(args[++i]);
				break;
			case "--batch":
				file = new File(args[++i]);
				batch = true;
				break;
			default:
				System.out.println("Unknown Argument: " + args[i]);
			}
		}

		if (batch){
			batch(file);
		} else {
			new IterativeImprovement(pivotingMode, neighbourhoodMode, initMode).findSolution(file);
		}
	}

	private static void batch(File file){
		if (!file.isDirectory()){
			System.out.println("The file '" + file.getName() + "' is not a directory.");
			return;
		}
		
		//File[] files = file.listFiles();
		FilenameFilter filter = new FilenameFilter(){

			@Override
			public boolean accept(File dir, String name) {
				return !name.equals(".DS_Store");
			}
			
		};
		File[] files = file.listFiles(filter);
		
		// We look at all the possible algorithms.
		for (int initMode = 0; initMode <= 1; ++initMode){
			for (int neighbourhoodMode = 0; neighbourhoodMode <= 2; ++neighbourhoodMode){
				for(int pivotingMode = 0; pivotingMode <= 1; ++pivotingMode){
					
					IterativeImprovement itImp = new IterativeImprovement(pivotingMode, neighbourhoodMode, initMode);
					
					String name = INIT_MODES[initMode] + NEIGHBOURHOOD_MODES[neighbourhoodMode] + PIVOTING_MODES[pivotingMode];
					String nameAvRelPerDev = "R-avRelPer" + INIT_MODES[initMode] + NEIGHBOURHOOD_MODES[neighbourhoodMode] + PIVOTING_MODES[pivotingMode] + ".dat";
					
					try(BufferedWriter writer = new BufferedWriter(new FileWriter(name)); BufferedWriter writer2 = new BufferedWriter(new FileWriter(nameAvRelPerDev));){

						int averageRelativePercentageDeviation = 0;
						long sumOfComputationTime = 0;
						
						for (File instanceFile : files){
							Map<String, Object> results = itImp.findSolution(instanceFile);
							
							// Change this line to change the content of the results files for each algorithm:
							writer.write(instanceFile.getName() + "\t" + results.get(RELATIVE_PERCENTAGE_DEVIATION) + "\t" + results.get(COMPUTATION_TIME) + "\t" + results.get(COST) + "\t" + results.get(BEST_KNOWN) + "\n");
							writer2.write(results.get(RELATIVE_PERCENTAGE_DEVIATION) + "\n");
							
							averageRelativePercentageDeviation += (int) results.get(RELATIVE_PERCENTAGE_DEVIATION);
							sumOfComputationTime += (long) results.get(COMPUTATION_TIME);
						}
						
						averageRelativePercentageDeviation /= files.length;
						writer.write(averageRelativePercentageDeviation + "\n");
						writer.write(Long.toString(sumOfComputationTime) + "\n");
						sumOfComputationTime /= files.length;
						writer.write(Long.toString(sumOfComputationTime) + "\n");
						writer.flush();
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		
	}

}
