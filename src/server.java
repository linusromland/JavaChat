import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class server {

    public static void main(String[] args) throws Exception {



        Scanner input = new Scanner(System.in);
        ServerSocket sersock = new ServerSocket(8545);
        System.out.println("server running...");



        ArrayList<Receive> users = new ArrayList<Receive>();

        while(true){

            Socket sock = sersock.accept( );
            users.add( new Receive("mackelito", sock));
            TheThreads t1 = new TheThreads(sock, users);
            t1.run();

        }






    }
}
class Receive{
    public static String user;
    public static Socket sock;

    public Receive(String userin, Socket sockin) {
        user = userin;
        sock = sockin;
    }

    public static BufferedReader read() throws IOException {
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        return receiveRead;
    }
}

class TheThreads extends Thread{

    public static Socket sock;
    public static ArrayList<Receive> users = new ArrayList<Receive>();

    public TheThreads(Socket sockin, ArrayList usersin){
        users = usersin;
        sock = sockin;
    }

    @Override
    public void run(){
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    try {
                        String receiveMessage;
                        if((receiveMessage = user.read().readLine()) != null) {
                            System.out.println(receiveMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }});

    }
}