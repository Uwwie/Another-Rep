import java.io.*;
import java.net.Socket;
public class ServerConnection implements Runnable {
    private Socket server;
    private BufferedReader in;
    public ServerConnection(Socket s) throws IOException {
        server = s;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
    }
    @Override
    public void run() {
            try {
                while (true) {
                    String serverResponse = in.readLine();
                    int nameIndex = serverResponse.indexOf("@#$@");
                    if (serverResponse == null) break;
                    if (nameIndex == -1)
                    System.out.println("Server says: " + serverResponse);
                    else System.out.println(serverResponse.substring(0,nameIndex)+" says " + serverResponse.substring(nameIndex+4,serverResponse.length()));
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}
