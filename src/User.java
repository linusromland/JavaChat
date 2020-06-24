import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class User {

    public PrintWriter out;
    public BufferedReader in;
    public String username;
    public int groupNumber;
    public int loggedInNumber;
    public Boolean loggedIn = true;
    private ArrayList<User> users;


    public User(Socket sock) throws IOException {
        System.out.println("User creation in progress");
        out = new PrintWriter(sock.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    }

    private boolean CheckUsername(String receiveMessage) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).username.equals(receiveMessage)) {
                loggedInNumber = i;
                return false;
            }
        }

        return true;
    }

    public void CreateUser(Socket sock, int num) {
        groupNumber = num;
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                users = server.groups.get(groupNumber).users;
                out.println("Whats is your username?");
                out.flush();
                String receiveMessage;
                username = "Guest";
                Boolean usernameset = false;
                while (!usernameset) {
                    try {
                        if ((receiveMessage = in.readLine()) != null) {
                            if (CheckUsername(receiveMessage)) {
                                username = receiveMessage;
                                out.println("Username is set to " + username);
                                out.flush();
                                usernameset = true;
                            } else if (!users.get(loggedInNumber).loggedIn){
                                System.out.println("already logged in f u");
                            }else {
                                out.println("Sorry, that username is taken, try again!");
                                out.flush();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                System.out.println("User \"" + username + "\" created!");
                users = server.groups.get(groupNumber).users;
                for (User user : users) {
                    System.out.println("Users in group: " + user.username + "with id: " + (users.size() - 1));
                }
                Thread object = new Thread(new CommunicationThread(users.size() - 1, out, username, groupNumber));
                object.start();
            }
        });
        t.start();
    }

}

class CommunicationThread extends Thread {
    public int userNumber;
    public PrintWriter out;
    public String userName;
    public int groupNumber;

    public CommunicationThread(int userNumberIN, PrintWriter outIN, String userNameIN, int groupNumberIN) {
        userNumber = userNumberIN;
        out = outIN;
        userName = userNameIN;
        groupNumber = groupNumberIN;
    }

    public void run() {
        try {
            while (true) {
                String receiveMessage;
                ArrayList<User> users = server.groups.get(groupNumber).users;

                if ((receiveMessage = users.get(userNumber).in.readLine()) != null) {
                    System.out.println("From " + users.get(userNumber).username + ":" + receiveMessage);
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).out != out) {
                            users.get(i).out.println("From " + users.get(userNumber).username + ":" + receiveMessage);
                            users.get(i).out.flush();
                        }
                    }

                } else {
                    server.groups.get(groupNumber).users.get(userNumber).loggedIn = false;
                    System.out.println("User \"" + userName + "\" logged out!");
                    break;
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
}

