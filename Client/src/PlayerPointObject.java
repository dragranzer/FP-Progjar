public class PlayerPointObject {
    public static PlayerPoint sendOperate(String operation, String username, Integer point){
        PlayerPoint playerpoint = new PlayerPoint();
        playerpoint.setOperation(operation);
        playerpoint.setUsername(username);
        playerpoint.setPoint(point);
        return playerpoint;
    }
}
