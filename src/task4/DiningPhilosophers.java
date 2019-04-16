package task4;
import java.util.Scanner;

/**
 * Class DiningPhilosophers
 * The main starter.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 * 
 * Gabriel Albuquerque Ribeiro - 40105717
 * Programming Assignment 3 - COMP 346
 * Task 4
 */
public class DiningPhilosophers
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */

	/**
	 * This default may be overridden from the command line
	 */
	public static final int DEFAULT_NUMBER_OF_PHILOSOPHERS = 4;

	/**
	 * Dining "iterations" per philosopher thread
	 * while they are socializing there
	 */
	public static final int DINING_STEPS = 10;

	/**
	 * Our shared monitor for the philosphers to consult
	 */
	public static Monitor soMonitor = null;

	/*
	 * -------
	 * Methods
	 * -------
	 */

	/**
	 * Main system starts up right here
	 */
	public static void main(String[] argv)
	{
		try
		{
			/*
			 * TODO:
			 * Should be settable from the command line
			 * or the default if no arguments supplied.
			 */
			
			// Gets the number of philosophers wanted by the user or use default
			int iPhilosophers = getNumberOfPhiloWanted();

			// Make the monitor aware of how many philosophers there are
			soMonitor = new Monitor(iPhilosophers);

			// Space for all the philosophers
			Philosopher aoPhilosophers[] = new Philosopher[iPhilosophers];

			// Let 'em sit down
			for(int j = 0; j < iPhilosophers; j++)
			{
				aoPhilosophers[j] = new Philosopher();
				aoPhilosophers[j].start();
			}

			System.out.println
			(
				iPhilosophers +
				" philosopher(s) came in for a dinner."
			);

			// Main waits for all its children to die...
			// I mean, philosophers to finish their dinner.
			for(int j = 0; j < iPhilosophers; j++)
				aoPhilosophers[j].join();

			System.out.println("All philosophers have left. System terminates normally.");
		}
		catch(InterruptedException e)
		{
			System.err.println("main():");
			reportException(e);
			System.exit(1);
		}
	} // main()

	/**
	 * Asks the user to insert the number of philosophers wanted and returns it
	 * Returns the default number of philosophers (4) in case input is lesser than 0 or if input is invalid
	 * @return
	 */
	private static int getNumberOfPhiloWanted() {
		// TODO Auto-generated method stub
		try{
			Scanner input = new Scanner(System.in);
			
			System.out.println("Set the number of philosophers: ");
			String line = input.nextLine();
			input.close();
			
			int num = Integer.parseInt(line);	
			
			if(num <= 0){	
			System.out.println(num + " is not a positive decimal integer, using default number of philosophers");
				return DEFAULT_NUMBER_OF_PHILOSOPHERS;
			} else {
				return num;
			}
			
			}//exception in case the input is invalid
			catch(NumberFormatException e){
				System.out.println("Using the default number of philosophers (4).");
				return DEFAULT_NUMBER_OF_PHILOSOPHERS;
			}
	}

	/**
	 * Outputs exception information to STDERR
	 * @param poException Exception object to dump to STDERR
	 */
	public static void reportException(Exception poException)
	{
		System.err.println("Caught exception : " + poException.getClass().getName());
		System.err.println("Message          : " + poException.getMessage());
		System.err.println("Stack Trace      : ");
		poException.printStackTrace(System.err);
	}
}

// EOF
