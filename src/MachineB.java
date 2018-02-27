/**
 * @author Yash Jain (yj8359)
 * Maninder Singh Jheeta (msj5913)
 */

public class MachineB {
    static Packet packet;
    static boolean hasPacket = false;
    static int lastCorrectMessage = -1;

    public static void recievepacketFromUnreliableNetwork(Packet packet) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MachineB.packet = packet;
        MachineB.hasPacket = true;
        System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Packet received!" + Starter.ANSI_RESET);
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
        Packet packetToSend = null;
        if (isValidPacket) {
            System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Correct packet, no checksum error" + Starter.ANSI_RESET);
            lastCorrectMessage = packet.getSequenceNumber();
            packetToSend = prepareAck(packet.getSequenceNumber());
            System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Ack prepared" + Starter.ANSI_RESET);
            MachineB.packet = null;
            MachineB.hasPacket = false;
        } else {
            System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Corrupt packet, checksum error" + Starter.ANSI_RESET);
            packetToSend = prepareAck(lastCorrectMessage);
            System.out.println(Starter.ANSI_GREEN + "            MACHINE-B -> Ack prepared" + Starter.ANSI_RESET);
        }
        return packetToSend;
    }

    private static Packet prepareAck(int ackNumber) {
        //I might be missing to set some fields here..cross check
        Packet packet = new Packet();
        packet.setIsAck(true);
        packet.setIsMessage(false);
        packet.setAckNumber(ackNumber);
        packet.setSimulatePacketLost(false);
        packet.setSimulateCorruptPacket(false);
        return packet;
    }

    // Incomplete method
    private static boolean checkCheckSum() {
        //add logic to confirm checksum
        return true;
    }

}
