import java.io.*;
import java.net.Socket;

public class User {

    public PrintWriter out;
    public BufferedReader in;

    public User(Socket sock) throws IOException {
        System.out.println("New User Created!");
        out = new PrintWriter(sock.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        Thread object = new Thread(new MultithreadingDemo(server.users.size(), out));
        object.start();

    }
}

class MultithreadingDemo extends Thread {
    public int num;
    public PrintWriter curout;

    public MultithreadingDemo(int usrnum, PrintWriter out) {
        num = usrnum;
        curout = out;
    }

    public void run() {
        try {
            while (true) {
                String receiveMessage;
                if ((receiveMessage = server.users.get(num).in.readLine()) != null) {
                    System.out.println("From Client:" + receiveMessage);
                    for (int i = 0; i < server.users.size(); i++) {
                        if (server.users.get(i).out != curout){
                            server.users.get(i).out.println(receiveMessage);
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
