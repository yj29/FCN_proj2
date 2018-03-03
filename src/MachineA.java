import javax.crypto.Mac;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yash Jain (yj8359)
 * Maninder Singh Jheeta (msj5913)
 */

public class MachineA {
    static int seq = 0;
    static Packet packet = null;
    static boolean hasPacket;
    static Map<Integer, Packet> hashMapOfPackets = new HashMap<Integer, Packet>();

    public static void receiveAckMessageFromURN(Packet packet) {
        MachineA.packet = packet;
        MachineA.hasPacket = true;
    }

    public String getMessageFromAboveLayer() {
        String out = null;
        if (!GenerateString.queue.isEmpty()) {
            try {
                out = GenerateString.queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return out;
    }

    public Packet preparePacket(String message) {
        Packet packet = new Packet();
        packet.setSequenceNumber(seq % 2);
        packet.setMessage(message);
        packet.setIsMessage(true);
        packet.setIsAck(false);
        packet.setIsNack(false);
        packet.setSimulatePacketLost(false);
        packet.setSimulateCorruptPacket(false);

        //set lost bit on
        if (seq % 5 == 1) {
            packet.setSimulatePacketLost(true);
            System.out.println(Starter.ANSI_BLUE + "    CASE - 2" + message + Starter.ANSI_RESET);
        }

        //set corrupt bit on
        if (seq % 5 == 2) {
            packet.setSimulateCorruptPacket(true);
            System.out.println(Starter.ANSI_BLUE + "    CASE - 3" + message + Starter.ANSI_RESET);
        }
        seq++;
        return packet;


    }

    public void sendPacketToUnreliableTransLinkSimulator(Packet packet) {
        UnreliableTransLinkSimulator.setPacketInLink(packet);
    }

    public void processStarter() {
        boolean shouldPreparePacket = true;
        int lastSeqSent = -1;
        while (true) {
            Packet packet = null;
            if (shouldPreparePacket) {
                //Get message from above layer
                String message = getMessageFromAboveLayer();
                System.out.println(Starter.ANSI_BLUE + "    MACHINE-A -> Message received from above layer " + message + Starter.ANSI_RESET);

                //Prepare packet using the message extracted above
                packet = preparePacket(message);
                hashMapOfPackets.put(packet.getSequenceNumber(), packet);
                System.out.println(Starter.ANSI_BLUE + "    MACHINE-A ->Packet prepared! Seq:" + packet.getSequenceNumber() + Starter.ANSI_RESET);
            } else {
                packet = hashMapOfPackets.get(Integer.valueOf(lastSeqSent));
                packet.setSimulatePacketLost(false);
                packet.setSimulateCorruptPacket(false);
            }

            //send packet to unreliable link
            lastSeqSent = packet.getSequenceNumber();
            System.out.println(Starter.ANSI_BLUE + "    MACHINE-A ->Packet with seq " + packet.getSequenceNumber() + " sent to unreliable network" + Starter.ANSI_RESET);
            sendPacketToUnreliableTransLinkSimulator(packet);

            // Wait to receive ack
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (MachineA.packet != null) {
                if (lastSeqSent != MachineA.packet.getAckNumber()) {
                    System.out.println(Starter.ANSI_BLUE + "    MACHINE-A -> Resending" + Starter.ANSI_RESET);
                    shouldPreparePacket = false;
                    MachineA.packet = null;
                    hasPacket = false;
                    continue;
                }
                if (MachineA.packet.getSimulateCorruptPacket()) {
                    System.out.println(Starter.ANSI_BLUE + "    MACHINE-A -> Resending" + Starter.ANSI_RESET);
                    shouldPreparePacket = false;
                    MachineA.packet = null;
                    hasPacket = false;
                    continue;
                }
                System.out.println(Starter.ANSI_BLUE + "    MACHINE-A -> Ack received with #" + MachineA.packet.getAckNumber() + Starter.ANSI_RESET);
                hashMapOfPackets.remove(packet.getAckNumber());
                MachineA.packet = null;
                hasPacket = false;
                shouldPreparePacket = true;
                System.out.println(Starter.ANSI_BLUE + "=====================================================" + Starter.ANSI_RESET);
            } else {
                System.out.println(Starter.ANSI_BLUE + "    MACHINE-A -> TIMEOUT...Ack not received.");
                System.out.println(Starter.ANSI_BLUE + "    MACHINE-A -> Resending packet.");
                shouldPreparePacket = false;
            }
        }
    }
}
