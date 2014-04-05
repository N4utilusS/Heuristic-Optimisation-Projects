
public class Main {

	/**
	 * The main class of the project, receiving the arguments of the user.
	 * @param args The arguments controlling the execution mode.
	 */
	public static void main(String[] args) {
		System.out.println("Arguments:\n");
		
		for (int i = 0; i < args.length; ++i){
			switch (args[i]){
			case "--first":
				
				break;
			case "--best":
				
				break;
			case "--transpose":
				
				break;
			case "--exchange":
				
				break;
			case "--insert":
				
				break;
			case "--random_init":
				
				break;
			case "--slack_init":
				
				break;
				
			default:
				System.out.println("Unknown Argument: " + args[i]);
			}
		}
	}

}
