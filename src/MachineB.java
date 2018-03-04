import javax.crypto.Mac;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yash Jain (yj8359)
 * Maninder Singh Jheeta (msj5913)
 */

public class MachineB {
    static Packet packet;
    static boolean hasPacket = false;
    static int lastCorrectMessage = -1;
    static int numberOfPacketsReceived = 0;
    static Map<Integer, Packet> packetsReceived = new HashMap<Integer, Packet>();

    public static void recievePacketFromUnreliableNetwork(Packet packet) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MachineB.packet = packet;
        MachineB.hasPacket = true;
        //numberOfPacketsReceived++;
        System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Packet received with seq" + MachineB.packet.getSequenceNumber() + Starter.ANSI_RESET);
        Packet ackPacket = processReceivedPacket();
        MachineB.packet = null;
        MachineB.hasPacket = false;

        //send ack
        System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Sending Ack to Unreliable network" + Starter.ANSI_RESET);
        sendAckToUnreliableNetwork(ackPacket);
    }

    private static void sendAckToUnreliableNetwork(Packet ackPacket) {
        UnreliableTransLinkSimulator.setPacketInLink(ackPacket);
    }

    private static Packet processReceivedPacket() {
        System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Computing checksum of received packet." + Starter.ANSI_RESET);
        boolean isValidPacket = checkCheckSum();
        boolean isNotDuplicate = checkDuplicate();
        Packet packetToSend = null;
        if (isNotDuplicate && isValidPacket) {
            System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Correct packet, no checksum error" + Starter.ANSI_RESET);
            lastCorrectMessage = packet.getSequenceNumber();
            numberOfPacketsReceived++;
            packetToSend = prepareAck(packet.getSequenceNumber());
            System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Ack prepared" + Starter.ANSI_RESET);
            packetsReceived.put(MachineB.packet.getSequenceNumber(), MachineB.packet);
            MachineB.packet = null;
            MachineB.hasPacket = false;

        } else if (!isNotDuplicate) {
            System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Duplicate packet" + Starter.ANSI_RESET);
            packetToSend = prepareAck(lastCorrectMessage);
            packetToSend.setSimulatePacketLost(false);
            packetToSend.setSimulateCorruptPacket(false);
            packetToSend.setShouldDelayAck(false);
            System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Ack prepared" + Starter.ANSI_RESET);
        } else if (!isValidPacket) {
            System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Corrupt packet, checksum error" + Starter.ANSI_RESET);
            packetToSend = prepareAck(lastCorrectMessage);
            System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Ack prepared" + Starter.ANSI_RESET);
        }
        return packetToSend;
    }

    private static boolean checkDuplicate() {
        Packet t = MachineB.packetsReceived.get(MachineB.packet.getSequenceNumber());
        if (MachineB.packetsReceived.size() > 0 && t != null && t.equals(MachineB.packet)) {
            System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Duplicate packet..discard" + Starter.ANSI_RESET);
            return false;
        }
        return true;
    }

    private static Packet prepareAck(int ackNumber) {
        //I might be missing to set some fields here..cross check
        Packet packet = new Packet();
        packet.setIsAck(true);
        packet.setIsMessage(false);
        packet.setAckNumber(ackNumber);
        packet.setSimulatePacketLost(false);
        packet.setSimulateCorruptPacket(false);
        packet.setDuplicateInAck(false);

        //if (numberOfPacketsReceived < 10) {
        if (numberOfPacketsReceived % 4 == 0) {
            System.out.println(Starter.ANSI_GREEN + "            CASE-4" + Starter.ANSI_RESET);
            packet.setSimulatePacketLost(true);
        }
        if (numberOfPacketsReceived % 5 == 0) {
            packet.setSimulateCorruptPacket(true);
            System.out.println(Starter.ANSI_GREEN + "            CASE-5" + Starter.ANSI_RESET);
        }
        if (numberOfPacketsReceived % 6 == 0) {
            System.out.println(Starter.ANSI_GREEN + "            CASE-6" + Starter.ANSI_RESET);
            packet.setShouldDelayAck(true);
        }
        /*} else {
            if (numberOfPacketsReceived % 1 == 0) {
                System.out.println("In here.......");
                packet.setShouldDelayAck(true);
            }
        }*/
        return packet;
    }

    // Incomplete method
    private static boolean checkCheckSum() {
        //add logic to confirm checksum
        if (MachineB.packet.getSimulateCorruptPacket()) {
            return false;
        }
        return true;
        //System.out.println("HHHHHHHHHHHHHH");
        //return Checksum.isChecksumValid(MachineB.packet.getMessage(), MachineB.packet.getChecksum());
    }

}
