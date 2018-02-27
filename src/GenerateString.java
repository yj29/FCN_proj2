import java.security.SecureRandom;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Yash Jain (yj8359)
 * Maninder Singh Jheeta (msj5913)
 */
public class GenerateString {
    //static Queue<String> queue = new LinkedList<String>();
    static int size = 10;
    static BlockingQueue<String> queue = new ArrayBlockingQueue<String>(size);

    SecureRandom secureRandom = new SecureRandom();
    static String set = "abcdefghijklmnopqrstuvwxyz";

    public void generateString() {
        while (true) {
            if (queue.size() == size) {
                continue;
            }
            String generatedString = "";
            for (int i = 0; i < 2; i++) {
                generatedString += String.valueOf(set.charAt(secureRandom.nextInt(set.length())));
            }

            try {
                queue.put(generatedString);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            System.out.println(Starter.ANSI_YELLOW + "String with message:" + generatedString + " generated.." + queue.size() + Starter.ANSI_RESET);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Error putting thread to sleep in class " + e.getClass().getName());

            }
        }
    }
}
