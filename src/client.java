import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client
{
    public static void main(String[] args) throws Exception
    {
        Scanner input = new Scanner(System.in);
        System.out.println("What server do you want to connect to?");
        System.out.println("Press enter for default! (romland.space)");
        String server = input.nextLine();
        if (server.equals("")){
            server = "romland.space";
        }

        Socket sock = new Socket(server, 8545);
        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
        OutputStream ostream = sock.getOutputStream();
        PrintWriter pwrite = new PrintWriter(ostream, true);

        InputStream istream = sock.getInputStream();
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

        System.out.println("Connected to " + server + "!");

        Thread object = new Thread(new Multithreading(receiveRead));
        object.start();

        String sendMessage;
        while(true)
        {
            if((sendMessage = keyRead.readLine()) != null){
                pwrite.println(sendMessage);
                pwrite.flush();
            }

        }
    }
}
class Multithreading extends Thread {
    public BufferedReader receiveRead;


    public Multithreading(BufferedReader in) {
        receiveRead = in;
    }

    public void run() {
        try {
            String receiveMessage;

            while (true) {
                if((receiveMessage = receiveRead.readLine()) != null) {
                    System.out.println(receiveMessage);
                }
            }

        } catch (Exception e) {
            // Throwing an exception
            System.out.println("Exception is caught");
        }
    }
}
