import java.io.*;
import java.net.Socket;

public class User {

    public PrintWriter out;
    public BufferedReader in;
    public String username;

    public User(Socket sock) throws IOException {
        System.out.println("New User Created!");
        out = new PrintWriter(sock.getOutputStream(), true);
        out.println("Whats is your username?");
        out.flush();
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        String receiveMessage;
        username = "Guest";
        Boolean usernameset = false;
        while(!usernameset){
        if ((receiveMessage = in.readLine()) != null) {
            if (CheckUsername(receiveMessage)) {
                username = receiveMessage;
                out.println("Username is set to " + username);
                out.flush();
                usernameset = true;
            } else {
                out.println("Sorry, that username is taken, try again!");
                out.flush();
            }
        }
        }
        Thread object = new Thread(new MultithreadingDemo(server.users.size(), out, username));
        object.start();


    }

    private boolean CheckUsername(String receiveMessage) {
        for (int i = 0; i < server.users.size(); i++) {
            if (server.users.get(i).username.equals(receiveMessage)){
                return false;
            }
        }

        return true;
    }

}

class MultithreadingDemo extends Thread {
    public int num;
    public PrintWriter curout;
    public String name;

    public MultithreadingDemo(int usrnum, PrintWriter out, String username) {
        num = usrnum;
        curout = out;
        name = username;
    }

    public void run() {
        try {
            while (true) {
                String receiveMessage;
                if ((receiveMessage = server.users.get(num).in.readLine()) != null) {
                    System.out.println("From Client:" + receiveMessage);
                    for (int i = 0; i < server.users.size(); i++) {
                        if (server.users.get(i).out != curout){
                            server.users.get(i).out.println("From "+ server.users.get(num).username + ":" + receiveMessage);
                            server.users.get(i).out.flush();
                        }
                    }

                }
            }

        } catch (Exception e) {
            // Throwing an exception
            System.out.println("Exception is caught");
        }
    }
}
