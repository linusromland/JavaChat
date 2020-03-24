import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client
{
    public static void main(String[] args) throws Exception
    {
        Scanner input = new Scanner(System.in);
        System.out.println("What ip is your server running at?");
        String servername = input.nextLine();
        Socket sock = new Socket(servername, 8545);
        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
        OutputStream ostream = sock.getOutputStream();
        PrintWriter pwrite = new PrintWriter(ostream, true);

        InputStream istream = sock.getInputStream();
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

        System.out.println("Connected to " + servername + "...");

        String receiveMessage, sendMessage;

        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                while(true){
                    try {
                        String receiveMessage;
                        if((receiveMessage = receiveRead.readLine()) != null) {
                            System.out.println("From Client: " + receiveMessage);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }});

        t.start();

        while (true) {
            if ((sendMessage = keyRead.readLine()) != null) {
                pwrite.println(sendMessage);
                pwrite.flush();
            }
        }
    }

}
