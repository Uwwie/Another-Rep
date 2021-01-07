import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
public class ClientHandler implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<ClientHandler> clients;
    private boolean flag = true;
    private String name = "Unknown Client";
    private boolean flag2 = true;
    private Message m = new Message();
    private static boolean f3 = true;
    private boolean anotherflag = true;
    private String starter ="";
    private int z = 0;
    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients) throws IOException {
        this.client = clientSocket;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(),true);
    }
    @Override
    public void run() {
        try {
            while (f3) {
                if (anotherflag == true) {
                    for (ClientHandler aC : clients){
                        if (aC.m.name !=null){ starter = starter + aC.m.name+" "; ++z;}

                    }
                    if (starter.equals("")) starter = "0 people ";
                    if (z==0 || z>1)
                    out.println(starter+"are online");
                    else out.println(starter+ "is online");
                    anotherflag = false;
                }
                String request = in.readLine();
                if (!request.equals("quit")) {
                    int firstSpace = request.indexOf(" ");
                    if (flag == true && !request.startsWith("name ")) {
                        out.println("Write name");
                    } else if (flag == true && request.startsWith("name ")) {
                        flag = false;
                        name = request.substring(firstSpace + 1);
                        m.name = this.name;
                        m.date = new Date().toString();
                        outToAll(m.name + " joined chat " + m.date);
                    } else if (request.startsWith("say")) {
                        if (firstSpace != -1) {
                            flag2 = false;
                            m.date = new Date().toString();
                            m.message = request.substring(firstSpace + 1);
                            m.message = m.message + m.date;
                            outToAll(m.name + "@#$@" + m.message);
                            flag2 = true;
                        }
                    }
                } else {
                    if (m.name == null) m.name = name;
                    outToAll(m.name+"@#$@"+" I quit");f3 = false; }
            }
        } catch (IOException e) {
            System.err.println("IO exception in client handler");
            System.err.println(e.getStackTrace());
        } finally {
            out.close();
            try {
                in.close();
                clients.remove(this.client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void outToAll(String msg) {
        for (ClientHandler aClient : clients) {
            if (aClient.flag2 == true) aClient.out.println(msg);

        }
    }
}
