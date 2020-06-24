import java.net.*;
import java.util.ArrayList;

public class server {

    public static ArrayList<User> users = new ArrayList<>();


    public static void main(String[] args) throws Exception {

        ServerSocket sersock = new ServerSocket(8545);
        System.out.println("server running...");

        while (true) {
            Socket sock = sersock.accept();
            users.add(new User(sock));
        }

    }
}




