/**
 * @author Yash Jain (yj8359)
 * Maninder Singh Jheeta (msj5913)
 */

public class Starter extends Thread {
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_GREEN = "\u001B[32m";

    public static void main(String[] args) {
        //thread that initiates the method to continuously generate message
        // mimic of the application layer
        Thread thread = new Thread() {
            @Override
            public void run() {
                GenerateString generateString = new GenerateString();
                generateString.generateString();
            }
        };
        thread.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Starts the process
        MachineA machineA = new MachineA();
        machineA.processStarter();
    }
}
