package main;

import static main.IterativeImprovement.BEST_MODE;
import static main.IterativeImprovement.EXCHANGE_MODE;
import static main.IterativeImprovement.FIRST_MODE;
import static main.IterativeImprovement.INSERT_MODE;
import static main.IterativeImprovement.RANDOM_INIT;
import static main.IterativeImprovement.SLACK_INIT;
import static main.IterativeImprovement.TRANSPOSE_MODE;

import java.io.File;

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
		
		for (int i = 0; i < args.length; ++i){
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
			default:
				System.out.println("Unknown Argument: " + args[i]);
			}
		}
		
		new IterativeImprovement(pivotingMode, neighbourhoodMode, initMode).findSolution(file);
	}

}
