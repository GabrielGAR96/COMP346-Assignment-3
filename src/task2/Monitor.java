package task2;

/**
 * Class Monitor
 * To synchronize dining philosophers.
 *
 * @author Serguei A. Mokhov, mokhov@cs.concordia.ca
 * 
 * Gabriel Albuquerque Ribeiro - 40105717
 * Programming Assignment 3 - COMP 346
 */
public class Monitor
{
	/*
	 * ------------
	 * Data members
	 * ------------
	 */
	
	//number of philosophers
	int numberP;
	//boolean to store whether someone is talking or not 
	boolean someoneIsTalking;
	//array to store the chopsticks
	Chopstick chopsticks[];
	
	
	
	//class for the chopsticks
	//stores if it is picked up
	//and who was the last philosopher 
	//that picked it up
	private class Chopstick
	{
		public boolean pickedup;
		public int lastPickedUp;
		
		//constructor
		public Chopstick()
		{
			pickedup = false;
			lastPickedUp = 0;
		}

		//returns if someone else picked up the chopstick
		public boolean pickedUpByOther(final int piTID)
		{
			return lastPickedUp != piTID && pickedup;
		}
				
		//returns whether the philosopher was the last one to pick up the chopstick
		public boolean lastPickedUpByMe(final int piTID)
		{
			return lastPickedUp == piTID;
		}
		
		//picks up chopstick
		public void pickup(final int piTID)
		{
			pickedup = true;
			lastPickedUp = piTID;
		}
		//puts down chopstick
		public void putdown()
		{
			pickedup = false;
		}
		
	}


	/**
	 * Constructor
	 */
	public Monitor(int piNumberOfPhilosophers)
	{
		// TODO: set appropriate number of chopsticks based on the # of philosophers
		numberP = piNumberOfPhilosophers;
		chopsticks = new Chopstick[piNumberOfPhilosophers];
		
		//initialize the chopsticks
		for(int i = 0; i < chopsticks.length; i++)
		{
			chopsticks[i] = new Chopstick();
		}
		
	}

	/*
	 * -------------------------------
	 * User-defined monitor procedures
	 * -------------------------------
	 */
		

	/**
	 * Grants request (returns) to eat when both chopsticks/forks are available.
	 * Else forces the philosopher to wait()
	 */
	public synchronized void pickUp(final int piTID)
	{
		int chopID = piTID-1;
		
		while(true) //while the thread is running
		{	
			//case they are both unavailable to be picked up
			if(chopsticks[chopID].pickedUpByOther(piTID) || chopsticks[(chopID + 1) % numberP].pickedUpByOther(piTID))
			{
					if(!chopsticks[chopID].pickedup && !chopsticks[chopID].lastPickedUpByMe(piTID))
					{
						//chopstick on the left
						chopsticks[chopID].pickup(piTID);
					} 
					else if(!chopsticks[(chopID + 1) % numberP].pickedup && !chopsticks[(chopID + 1) % numberP].lastPickedUpByMe(piTID))
					{
						//chopstick on the right
						chopsticks[(chopID + 1) % numberP].pickup(piTID);
	
					}
				} else {
					//both chopsticks are now available
					chopsticks[chopID].pickup(piTID);
					chopsticks[(chopID + 1) % numberP].pickup(piTID);
					// exit the loop and eat
					break;
				}
				try {
					wait();
				} catch (InterruptedException e) {
					System.err.println("Monitor.pickUp():");
					DiningPhilosophers.reportException(e);
					System.exit(1);
				}
			
			}
		}
	

	/**
	 * When a given philosopher's done eating, they put the chopstiks/forks down
	 * and let others know they are available.
	 */
	public synchronized void putDown(final int piTID)
	{		
		chopsticks[piTID-1].putdown();
		chopsticks[piTID%numberP].putdown();
		notifyAll();		
		
	}

	/**
	 * Only one philopher at a time is allowed to philosophy
	 * (while she is not eating).
	 */
	public synchronized void requestTalk()
	{
		//wait while someone's talking
		while(someoneIsTalking) 
		{
			try 
			{
				wait();
			}catch (InterruptedException e) 
			{
				System.err.println("Monitor.requestTalk():");
				DiningPhilosophers.reportException(e);
				System.exit(1);
			}
		}
		someoneIsTalking = true;
	}

	/**
	 * When one philosopher is done talking stuff, others
	 * can feel free to start talking.
	 */
	public synchronized void endTalk()
	{
		//philosopher who requested is not talking anymore
		someoneIsTalking = false;
		notifyAll();
	}
	
}

	

// EOF
