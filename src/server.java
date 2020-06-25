import javax.swing.*;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class server {
    //initializes ArrayList of groups
    public static ArrayList<Group> groups = new ArrayList<>();


    public static void main(String[] args) throws Exception {

        //initializes the variables
        Boolean portSet = false;
        ServerSocket serSock = null;
        int port = 8545;

        //asks for what port the server would run on
        System.out.println("On what port should the server run on?");
        System.out.println("Press enter for default! (Port 8545)");
        while (!portSet) { //loop that runs until the port is set and accepted
            String portString = new Scanner(System.in).nextLine();
            if (portString.equals("")) { //if enter is pressed, sets default 8545
                port = 8545;
            } else {
                port = Integer.parseInt(portString);
            }
            try {
                //opens the port for access to server.
                serSock = new ServerSocket(port);
                portSet = true;
            } catch (Exception e) {
                System.out.println("That port isn't allowed, try again!");
            }
        }
        System.out.println("Server running on port " + port);

        /**
         * loop that accepts new users and inserts them in groups
         */
        while (true) {
            ServerSocket finalSerSock = serSock;
            Thread t = new Thread(new Runnable() {

                /**
                 * Creates the user, sets the username and also prints the logged in users in the group.
                 */
                @Override
                public void run() {
                    Socket sock = null;
                    try {
                        sock = finalSerSock.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    User tempUser = null;
                    try {
                        tempUser = new User(sock); //creates tempUser for use while creation of Group
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    tempUser.out.println("What group?");
                    String receiveRead = null;
                    try {
                        receiveRead = tempUser.in.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (!CheckGroups(receiveRead)) { //checks if group exist, else creates it
                        groups.add(new Group(receiveRead));
                    }
                    int groupNumber = CheckGroupNumber(receiveRead);
                    Group tmpGroup = groups.get(groupNumber);
                    int userNumber = tmpGroup.addUser(tempUser);
                    tmpGroup.users.get(userNumber).CreateUser(sock, groupNumber);
                }

                /**
                 * checks if group exist
                 * @param receiveMessage
                 * @return
                 */
                public boolean CheckGroups(String receiveMessage) {
                    for (Group group : groups) {
                        if (group.name.equals(receiveMessage)) {
                            return true;
                        }
                    }
                    return false;
                }

                /**
                 * check what group number a group have
                 * @param input
                 * @return
                 */
                public int CheckGroupNumber(String input) {
                    for (int i = 0; i < groups.size(); i++) {
                        if (groups.get(i).name.equals(input)) {
                            return i;
                        }
                    }
                    return 0;
                }
            });
            t.start();

        }
    }


}




