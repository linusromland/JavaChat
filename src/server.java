import javax.swing.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class server {

    public static ArrayList<Group> groups = new ArrayList<>();


    public static void main(String[] args) throws Exception {

        ServerSocket serSock = new ServerSocket(8545);
        System.out.println("Server running...");

        while (true) {
            Socket sock = serSock.accept();
            User tempUser = new User(sock);
            tempUser.out.println("What group?");
            String receiveRead = tempUser.in.readLine();
            if (!CheckGroups(receiveRead)){
                groups.add(new Group(receiveRead));
            }
            int groupNumber = CheckGroupNumber(receiveRead);
            Group tmpGroup = groups.get(groupNumber);
            int userNumber = tmpGroup.addUser(tempUser);
            tmpGroup.users.get(userNumber).CreateUser(sock, groupNumber);

        }

    }
    public static boolean CheckGroups(String receiveMessage) {
        for(Group group:groups){
            if (group.name.equals(receiveMessage)) {
                return true;
            }
        }
        return false;
    }
    public static int CheckGroupNumber(String input){
        for (int i = 0; i < groups.size(); i++) {
            if (groups.get(i).name.equals(input)){
                return i;
            }
        }
        return 0;
    }

}




