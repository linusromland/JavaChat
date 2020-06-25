import java.util.ArrayList;

public class Group {
    //initializes variables for every group

    public ArrayList<User> users = new ArrayList<>();
    public String name;

    /**
     * inserts the group name and prints that the user is created
     * @param inputName
     */
    public Group(String inputName){
        name = inputName;
        System.out.println("Group \"" + name + "\" created!");
    }

    /**
     * Adds user to the arraylist of users in the group
     * @param tmpUser
     * @return
     */
    public int addUser(User tmpUser){
        users.add(tmpUser);
        return users.size()-1;
    }
}
