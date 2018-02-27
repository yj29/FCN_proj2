/**
 * @author Yash Jain (yj8359)
 * Maninder Singh Jheeta (msj5913)
 */

public class UnreliableTransLinkSimulator {
    static Packet packet;

    public static void setPacketInLink(Packet packet) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("        URN -> Packet received in unreliable network");
        if (packet.getSimulatePacketLost()) {
            UnreliableTransLinkSimulator.packet = null;
            System.out.println("        URN -> Packet with seq: " + packet.getSequenceNumber() + " lost in URN");
            return;
        }
        if (packet.getSimulateCorruptPacket()) {
            // Add logic to change checksum to corrupt the incoming packet

        }
        UnreliableTransLinkSimulator.packet = packet;

        // If it is a normal message send it to MachineB
        if (packet.getIsMessage()) {
            sendPacketToMachineB();
        }

        // If it is an ack send it to MachineA
        if (packet.getIsAck()) {
            sendPacketToMachineA();
        }

    }

    private static void sendPacketToMachineA() {
        System.out.println("        URN -> Sending packet(ack) to MachineA");
        MachineA.receiveAckMessageFromURN(packet);
    }

    private static void sendPacketToMachineB() {
        System.out.println("        URN -> Sending packet to MachineB");
        MachineB.recievepacketFromUnreliableNetwork(packet);
    }

}
