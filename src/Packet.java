/**
 * @author Yash Jain (yj8359)
 * Maninder Singh Jheeta (msj5913)
 */
public class Packet {
    private int sequenceNumber;
    private int ackNumber;
    private String message;
    private boolean isAck;
    private boolean isNack;
    private boolean isMessage;
    private boolean simulatePacketLost;
    private boolean simulateCorruptPacket;
    private boolean isDuplicateInAck;
    private boolean shouldDelayAck;
    //may need to change it later
    private String checksum;

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public boolean getShouldDelayAck() {
        return shouldDelayAck;
    }

    public void setShouldDelayAck(boolean shouldDelayAck) {
        this.shouldDelayAck = shouldDelayAck;
    }

    public boolean getDuplicateInAck() {
        return isDuplicateInAck;
    }

    public void setDuplicateInAck(boolean duplicateInAck) {
        isDuplicateInAck = duplicateInAck;
    }

    public boolean getSimulatePacketLost() {
        return simulatePacketLost;
    }

    public void setSimulatePacketLost(boolean simulatePacketLost) {
        this.simulatePacketLost = simulatePacketLost;
    }

    public boolean getSimulateCorruptPacket() {
        return simulateCorruptPacket;
    }

    public void setSimulateCorruptPacket(boolean simulateCorruptPacket) {
        this.simulateCorruptPacket = simulateCorruptPacket;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public int getAckNumber() {
        return ackNumber;
    }

    public void setAckNumber(int ackNumber) {
        this.ackNumber = ackNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsAck() {
        return isAck;
    }

    public void setIsAck(boolean ack) {
        isAck = ack;
    }

    public boolean getIsNack() {
        return isNack;
    }

    public void setIsNack(boolean nack) {
        isNack = nack;
    }

    public boolean getIsMessage() {
        return isMessage;
    }

    public void setIsMessage(boolean message) {
        isMessage = message;
    }

}
