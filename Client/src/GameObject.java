public class GameObject {
    public static Game newGame(String username){
        Game game = new Game();
        game.setNewGame(true);
        game.setUsername(username);
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
