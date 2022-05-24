public class MessageObject {
    public static Message getMsg(String username, String receiver, String choice, String message) {
        Message messageObject = new Message();
        messageObject.setSender(username);
        messageObject.setReceiver(receiver);
        messageObject.setChannel(choice);
        messageObject.setText(message);
        messageObject.setRequest(false);
        return messageObject;
    }

    public static Message getList(String username){
        Message messageObject = new Message();
        messageObject.setSender(username);
        messageObject.setReceiver(username);
        messageObject.setRequest(true);
        return messageObject;
    }
}
