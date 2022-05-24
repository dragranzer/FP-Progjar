public class UserObject {
    public static User getLogout(String username){
        User userObj = new User();
        userObj.setName(username);
        userObj.setStatus(false);
        return userObj;
    }

    public static User getLogin(String username){
        User userObj = new User();
        userObj.setName(username);
        userObj.setStatus(true);
        return userObj;
    }
}
