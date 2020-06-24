import java.util.ArrayList;

public class Group {
    public ArrayList<User> users = new ArrayList<>();
    public String name;


    public Group(String inputName){
        name = inputName;
        System.out.println("Group \"" + name + "\" created!");
    }
    public int addUser(User tmpUser){
        users.add(tmpUser);
        return users.size()-1;
    }
}
