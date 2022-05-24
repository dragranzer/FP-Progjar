public class GameObject {
    public static Game newGame(String username){
        Game game = new Game();
        game.setNewGame(true);
        game.setUsername(username);
        return game;
    }
}
