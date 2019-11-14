// Evan Knapke
// cs419
// assign3a

import java.io.*;
import java.net.*;
import java.util.*;


public class assign3Server {
	public static void main(String args[]) throws IOException {
		
		System.out.println("Creating a server socket on port 4192");
		ServerSocket serverSocket = new ServerSocket(4192);
		
		while (true) {
			Socket socket = serverSocket.accept();
			ClientHandler t = new ClientHandler(socket);
			t.start();
		}
	} //end main
} // end class assign3Server

class ClientHandler extends Thread {
	private Socket sk; // a socket that has been connected to a client

	public ClientHandler(Socket sok) {
		sk = sok;
	} //end constructor
	
	public void run() {
	
		PrintStream pr = null;
		Scanner in = null;
		
		try {
			pr = new PrintStream(sk.getOutputStream());
			in = new Scanner(sk.getInputStream());
		} catch (IOException e) {}
		
		pr.println("What is your name?");
		String name = in.nextLine();
		
		System.out.println("Starting a dialog with " + name); // displayed on server screen
		pr.println("Hi " + name + ". In your mind, choose an integer in 1...1000.");
		pr.println("I will show your chosen number within 10 guesses.");
				
		boolean guessed = false;
		int min = -1;
		int max = 1001;
		int n;

		while (!guessed) {
			n = (min+max)/2;
			
			if (n == min || n == max) {
				pr.println("You cheated, " + name);
				break;
			}

			pr.println(n + "? Enter one character: C (correct) or H (too high) or L (too low)");
			String input = in.nextLine();
			
			if (input.equals("H")) {
				max = n;
			} else if (input.equals("L")) {
				min = n;
			} else if (input.equals("C")) {
				pr.println("Thank you, " + name);
				guessed = true;
			} else {
				pr.println("Incorrect response, " + name);
			}
		} // end while loop

		pr.close(); 
		in.close();
		try {
			sk.close();
		} catch (IOException e) {}	

		System.out.println("Ended dialog with " + name); //displayed on server screen
	} // end run

} //end class ClientHandler