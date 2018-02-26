public class Starter extends Thread {
    public static void main(String[] args) {
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


        System.out.println("Here");
        MachineA machineA = new MachineA();
        //Get message from above layer
        String message = machineA.getMessageFromAboveLayer();
        System.out.println("Message received from above layer " + message);

        //Prepare packet using the mesasge extracted above
        Packet packet = machineA.preparePacket(message);
        System.out.println("Packet prepated!");

        //send packet to unreliable link
        machineA.sendPacketToUnreliableTransLinkSimulator(packet);
        System.out.println("Packet sent to unreliable network");


    }
}
