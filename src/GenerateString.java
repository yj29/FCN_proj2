import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.Queue;

public class GenerateString {
    static Queue<String> queue = new LinkedList<String>();
    int size = 10;
    SecureRandom secureRandom = new SecureRandom();
    static String set = "abcdefghijklmnopqrstuvwxyz";

    public void generateString() {
        while (true) {
            while (queue.size() < size) {
                String generatedString = "";
                for (int i = 0; i < 2; i++) {
                    generatedString += String.valueOf(set.charAt(secureRandom.nextInt(set.length())));
                }
                queue.offer(generatedString);
                System.out.println("String with message:" + generatedString + " generated.." + queue.size());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("Error putting thread to sleep in class " + e.getClass().getName());
                }
            }
        }
    }
}
