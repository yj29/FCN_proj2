/**
 * @author Yash Jain (yj8359)
 * Maninder Singh Jheeta (msj5913)
 */

public class UnreliableTransLinkSimulator {
    static Packet packetFromA;
    static Packet packetFromB;

    public static void setPacketInLink(Packet packet) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("        URN -> Packet received in unreliable network");

        //simulate packet loss
        if (packet.getSimulatePacketLost()) {
            if (packet.getIsMessage()) {
                packetFromA = null;
            } else {
                packetFromB = null;
            }
            System.out.println("        URN -> Packet with seq: " + packet.getSequenceNumber() + " lost in URN");
            return;
        }

        //simulate packet corrupt
        if (packet.getSimulateCorruptPacket()) {
            // Add logic to change checksum to corrupt the incoming packet

        }

        //simulate delaying ack
        if (packet.getIsAck() && packet.getShouldDelayAck()) {
            System.out.println("        URN -> Delaying ack in URN");

            try {
                Thread.sleep(9000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        }

        // If it is an ack send it to MachineA
        if (packet.getIsAck()) {
            sendPacketToMachineA();
            UnreliableTransLinkSimulator.packetFromB = null;
        }

    }

    private static void sendPacketToMachineA() {
        System.out.println("        URN -> Sending packet(ack) to MachineA");
        MachineA.receiveAckMessageFromURN(packetFromB);
    }

    private static void sendPacketToMachineB() {
        System.out.println("        URN -> Sending packet to MachineB");
        MachineB.recievePacketFromUnreliableNetwork(packetFromA);
    }

}
