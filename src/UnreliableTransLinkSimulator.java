import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Yash Jain (yj8359)
 * Maninder Singh Jheeta (msj5913)
 */

public class UnreliableTransLinkSimulator {
    static Packet packetFromA;
    static Packet packetFromB;
    static BlockingQueue<Packet> queue = new LinkedBlockingQueue<Packet>();

    /**
     * Method that is being called everytime to receive packet in URN.
     * This method simulates the packet lost, corrupt and packet delaying in URN
     *
     * @param packet
     */
    public static void setPacketInLink(final Packet packet) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("        URN -> Packet received in unreliable network");

        //simulate packet loss
        if (packet.getSimulatePacketLost()) {
            if (packet.getIsMessage()) {
                packetFromA = null;
                MachineA.shouldRead = true;
            } else {
                packetFromB = null;
                MachineA.shouldRead = true;
            }
            System.out.println("        URN -> Packet with seq: " + packet.getSequenceNumber() + " lost in URN");
            return;
        }

        //simulate packet corrupt
        if (packet.getSimulateCorruptPacket()) {
            // Add logic to change checksum to corrupt the incoming packet
            if (packet.getIsMessage()) {
                String checkSum = packet.getChecksum();
                String corrupt;
                corrupt = checkSum.substring(0, checkSum.length());
                if (checkSum.charAt(checkSum.length() - 1) == '1') {
                    corrupt += '0';
                } else {
                    corrupt += '1';
                }
                packet.setChecksum(corrupt);
            }
        }

        //simulate delaying ack
        if (packet.getIsAck() && packet.getShouldDelayAck()) {
            System.out.println("        URN -> Delaying ack in URN");
            Thread t = new Thread() {
                @Override
                public void run() {
                    // System.out.println("Here");
                    UnreliableTransLinkSimulator.queue.offer(packet);
                    // System.out.println("Here" + queue.size());
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // System.out.println("here");
                    //MachineA.shouldRead = true;
                }
            };
            t.start();
            return;
        }

        if (packet.getIsMessage()) {
            packetFromA = packet;
        } else {
            packetFromB = packet;
        }


        // If it is a normal message send it to MachineB
        if (packet.getIsMessage()) {
            sendPacketToMachineB();
            UnreliableTransLinkSimulator.packetFromA = null;
            return;
        }

        // If it is an ack send it to MachineA
        if (packet.getIsAck()) {
            //sendPacketToMachineA();
            UnreliableTransLinkSimulator.queue.offer(packet);
            // System.out.println("Hhh" + queue.size());
            UnreliableTransLinkSimulator.packetFromB = null;
            MachineA.shouldRead = true;
            return;
        }

    }

    private static void sendPacketToMachineA() {
        System.out.println("        URN -> Sending packet(ack) to MachineA");
        // UnreliableTransLinkSimulator.queue.offer(packetFromB);
        //MachineA.receiveAckMessageFromURN(packetFromB);
    }

    private static void sendPacketToMachineB() {
        System.out.println("        URN -> Sending packet to MachineB");
        MachineB.recievePacketFromUnreliableNetwork(packetFromA);
    }

}
