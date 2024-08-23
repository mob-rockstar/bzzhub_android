package smartdev.bzzhub.repository.model;


public class EventBusMessage {
    public static class MessageType {

        public final static int ResetPasswordCompleted = 0X01;  //Uploaded Profile
        public final static int CompanyConnectionChanged = 0X02;  //Uploaded Profile
        public final static int PDF_SELECTED = 0x03;  //Uploaded Profile
    }

    private int messageType; //type of data
    private String json;

    public EventBusMessage(int messageType, String json) {
        this.messageType = messageType;
        this.json = json;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
