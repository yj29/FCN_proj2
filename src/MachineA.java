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
        packet.setSequenceNumber(seq++);
        packet.setMessage(message);
        packet.setIsMessage(true);
        packet.setIsAck(false);
        packet.setIsNack(false);
        return packet;
    }

    public void sendPacketToUnreliableTransLinkSimulator(Packet packet) {
        UnreliableTransLinkSimulator.setPacketInLink(packet);
    }
}
