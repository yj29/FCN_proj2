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
    static boolean shouldRead = false;

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
        packet.setChecksum(Checksum.checkSum(message));

        //set lost bit on
        if (seq < 5) {
            if (seq % 5 == 1) {
                packet.setSimulatePacketLost(true);
                System.out.println(Starter.ANSI_BLUE + "    CASE - 2" + Starter.ANSI_RESET);
            }

            //set corrupt bit on
            if (seq % 5 == 2) {
                packet.setSimulateCorruptPacket(true);
                System.out.println(Starter.ANSI_BLUE + "    CASE - 3" + Starter.ANSI_RESET);
            }
        }
        seq++;
        return packet;


    }

    public void sendPacketToUnreliableTransLinkSimulator(final Packet packet) {
        //UnreliableTransLinkSimulator.setPacketInLink(packet);
        Thread t = new Thread() {
            @Override
            public void run() {
                UnreliableTransLinkSimulator.setPacketInLink(packet);
            }
        };
        t.start();

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
                packet.setChecksum(packet.getMessage());
            }

            //send packet to unreliable link
            lastSeqSent = packet.getSequenceNumber();
            System.out.println(Starter.ANSI_BLUE + "    MACHINE-A ->Packet with seq " + packet.getSequenceNumber() + " sent to unreliable network" + Starter.ANSI_RESET);
            sendPacketToUnreliableTransLinkSimulator(packet);

            // Wait to receive ack
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            long startTime = System.nanoTime();
            long endTime;
            while (!shouldRead) {
                endTime = System.nanoTime();
                if (((endTime - startTime) / 1000000) > 2000) break;
            }
            // shouldRead = false;
            //System.out.println("in here");

            if (shouldRead) {
                Packet ack = UnreliableTransLinkSimulator.queue.poll();
                if (ack != null) {
                    if (lastSeqSent != ack.getAckNumber()) {
                        System.out.println(Starter.ANSI_BLUE + "    MACHINE-A -> Resending" + Starter.ANSI_RESET);
                        shouldPreparePacket = false;
                        MachineA.packet = null;
                        hasPacket = false;
                        continue;
                    }
                    if (ack.getSimulateCorruptPacket()) {
                        System.out.println(Starter.ANSI_BLUE + "    MACHINE-A -> Resending" + Starter.ANSI_RESET);
                        shouldPreparePacket = false;
                        MachineA.packet = null;
                        hasPacket = false;
                        continue;
                    }
                    System.out.println(UnreliableTransLinkSimulator.queue.size());
                    System.out.println(Starter.ANSI_BLUE + "    MACHINE-A -> Ack received with #" + ack.getAckNumber() + Starter.ANSI_RESET);
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
                shouldRead = false;
            } else {
                System.out.println(Starter.ANSI_BLUE + "    MACHINE-A -> TIMEOUT...Ack not received case6.");
                System.out.println(Starter.ANSI_BLUE + "    MACHINE-A -> Resending packet.");
                shouldPreparePacket = false;
            }
        }
    }
}
