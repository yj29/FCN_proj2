public class MachineA {
    static int seq = 0;

    public String getMessageFromAboveLayer() {
        if (!GenerateString.queue.isEmpty()) {
            return GenerateString.queue.poll();
        }
        return null;
    }

    public Packet preparePacket(String message) {
        Packet packet = new Packet();
        packet.setSequenceNumber(seq);
        packet.setMessage(message);
        packet.setIsMessage(true);
        packet.setIsAck(false);
        packet.setIsNack(false);
        packet.setSimulatePacketLost(false);
        packet.setSimulateCorruptPacket(false);

        //set lost bit on
        if (seq % 5 == 2) {
            packet.setSimulatePacketLost(true);
        }

        //set corrupt bit on
        if (seq % 5 == 4) {
            packet.setSimulateCorruptPacket(true);
        }

        return packet;


    }

    public void sendPacketToUnreliableTransLinkSimulator(Packet packet) {
        UnreliableTransLinkSimulator.setPacketInLink(packet);
    }
}
