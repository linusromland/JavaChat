import java.io.*;
import java.net.*;
public class server
{

    public static void main(String[] args) throws Exception {
        ServerSocket sersock = new ServerSocket(8545);
        System.out.println("server running...");
        Socket sock = sersock.accept( );
        BufferedReader keyRead = new BufferedReader(new InputStreamReader(System.in));
        OutputStream ostream = sock.getOutputStream();
        PrintWriter pwrite = new PrintWriter(ostream, true);

        InputStream istream = sock.getInputStream();
        BufferedReader receiveRead = new BufferedReader(new InputStreamReader(istream));

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
            String sendMessage = "no";
            if ((sendMessage = keyRead.readLine()) != null) {
                pwrite.println(sendMessage);
                pwrite.flush();
            }
        }
    }
}


