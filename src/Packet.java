public class Packet {
    private int sequenceNumber;
    private int ackNumber;
    private String message;
    private boolean isAck;
    private boolean isNack;
    private boolean isMessage;
    //may need to change it later
    private String checksum;

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
