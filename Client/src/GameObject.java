public class GameObject {
    public static Game newGame(String username, Integer sumPlayer){
        Game game = new Game();
        game.setNewGame(true);
        game.setUsername(username);
        game.setSumPlayer(sumPlayer);
        return game;
    }
    public static Game joinGame(Integer idRoom, String username){
        Game game = new Game();
        game.setNewGame(false);
        game.setUsername(username);
        game.setIdRoom(idRoom);
        return game;
    }
}
