public class Cli {
    public static void login(){
        System.out.println("Login");
        System.out.println("Enter your username: ");
    }
    public static void welcome(){
        System.out.println("""
                            Welcome to Whyapp!
                            1. Chat
                            2. See Online Users
                            3. Play Game
                            4. Logout
                            """);
    }

    public static void channel(){
        System.out.println("""
                                    Select which channel do you want to send message :
                                    1. All
                                    2. Private
                                    """);
    }

    public static void typeMsg(){
        System.out.println("Type your message: ");
    }
}
