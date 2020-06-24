import java.io.*;
import java.net.*;

public class client
{
    public static void main(String[] args) throws Exception
    {
        Socket sock = new Socket("romland.space", 8545);
        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
        OutputStream ostream = sock.getOutputStream();
        PrintWriter pwrite = new PrintWriter(ostream, true);

        InputStream istream = sock.getInputStream();
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

        System.out.println("Connected to server...");

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
