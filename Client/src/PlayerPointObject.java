public class PlayerPointObject {
    public static PlayerPoint sendOperate(String operation, String username, Integer point, Integer idRoom){
        PlayerPoint playerpoint = new PlayerPoint();
        playerpoint.setOperation(operation);
        playerpoint.setUsername(username);
        playerpoint.setPoint(point);
        playerpoint.setIdRoom(idRoom);
        return playerpoint;
    }
}
