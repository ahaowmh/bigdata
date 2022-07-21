package homework.sixth;

public class Message {
    private String sender;
    private String receiver;
    private String information;
    private int time;

    public Message() {
    }

    public Message(String sender, String receiver, String information, int time) {
        this.sender = sender;
        this.receiver = receiver;
        this.information = information;
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    //重写toString
    @Override
    public String toString () {
        return "Message{" + "sender='" + sender + '\'' + ", receiver='" + receiver + '\'' +
                ", information='" + information + '\'' + ", time=" + time + '}';

    }
}
