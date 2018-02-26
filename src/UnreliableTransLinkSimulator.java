public class UnreliableTransLinkSimulator {
    static Packet packet;

    public static void setPacketInLink(Packet packet) {
        if (packet.getSimulatePacketLost()) {
            return;
        }
        if (packet.getSimulateCorruptPacket()) {
            // Add logic to change checksum to corrupt the incoming packet

        }

        UnreliableTransLinkSimulator.packet = packet;

    }

}
