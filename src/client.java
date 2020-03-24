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

        String receiveMessage, sendMessage;
        while(true)
        {
            if((sendMessage = keyRead.readLine()) != null){
                pwrite.println(sendMessage);
                pwrite.flush();
            }
            if((receiveMessage = receiveRead.readLine()) != null) {
                System.out.println("From Server:" +receiveMessage);
            }
        }
    }
}                        