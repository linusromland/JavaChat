import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class User {


    //initializes variables for every user
    public PrintWriter out;
    public BufferedReader in;
    public String username;
    public int groupNumber;
    public int loggedInNumber;
    public Boolean loggedIn = true;
    private ArrayList<User> users;

    /**
     * Starting the creation of the user
     * Creates the input and output stream! (PrintWriter and BufferedReader)
     * @param sock
     * @throws IOException
     */
    public User(Socket sock) throws IOException {
        System.out.println("User creation in progress");
        out = new PrintWriter(sock.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
    }



    public void CreateUser(Socket sock, int num) {
        groupNumber = num;
        Thread t = new Thread(new Runnable() {

            /**
             * Creates the user, sets the username and also prints the logged in users in the group.
             */
            @Override
            public void run() {
                users = server.groups.get(groupNumber).users;
                out.println("Whats is your username?"); //sends msg to client
                out.flush();
                String receiveMessage;
                username = "Guest";
                Boolean usernameset = false;
                while (!usernameset) { //checks if username is already set
                    try {
                        if ((receiveMessage = in.readLine()) != null) { //inserts the received msg to variable
                            if (CheckUsername(receiveMessage)) { //checks if a user is already using the name
                                username = receiveMessage; //sets username of the user
                                out.println("Username is set to " + username);
                                out.flush();
                                usernameset = true; //sets the variable and kills the loop
                            }else { //if username exist and in use.
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


    /**
     * Checks if username is already taken. And if so inserts the user ID to the varible.
     * @param receiveMessage
     * @return
     */
    private boolean CheckUsername(String receiveMessage) {
        for (int i = 0; i < users.size(); i++) { //loops thru all the users
            if (users.get(i).username.equals(receiveMessage)) { //checks if the username is equal to the input
                loggedInNumber = i; //sets the usernames id to variable
                return false;
            }
        }

        return true;
    }

}

class CommunicationThread extends Thread {

    //initializes variables for the Communication Thread

    public int userNumber;
    public PrintWriter out;
    public String userName;
    public int groupNumber;

    //inserts the variables for use in the thread and class
    public CommunicationThread(int userNumberIN, PrintWriter outIN, String userNameIN, int groupNumberIN) {
        userNumber = userNumberIN;
        out = outIN;
        userName = userNameIN;
        groupNumber = groupNumberIN;
    }
    //the actual method that does the work in the thread
    public void run() {
        try {
            while (true) {
                //initializes variables
                String receiveMessage;
                ArrayList<User> users = server.groups.get(groupNumber).users;

                //checks if message is received
                if ((receiveMessage = users.get(userNumber).in.readLine()) != null) {
                    sendMessage(users, receiveMessage); //Sends msg
                } else { //if the users logs out, aka closes the client
                    server.groups.get(groupNumber).users.get(userNumber).loggedIn = false; //sets the user variable logged in to false
                    System.out.println("User \"" + userName + "\" logged out!");
                    break; //breaks the loop, stops the checking if users sent msg.
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }

    /**
     * Send the message to the console and all the users in the group.
     * @param users
     * @param msg
     */
    public void sendMessage(ArrayList<User> users, String msg){
        System.out.println("From " + users.get(userNumber).username + ":" + msg); //prints the message in the console

        for (int i = 0; i < users.size(); i++) { //Loops thru all the users and sends the message
            if (users.get(i).out != out) {
                users.get(i).out.println("From " + users.get(userNumber).username + ":" + msg);
                users.get(i).out.flush();
            }
        }
    }
}

