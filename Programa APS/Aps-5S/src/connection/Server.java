package connection;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
    private static ArrayList<BufferedWriter> clients = new ArrayList<BufferedWriter>();
    private static ArrayList<String> users = new ArrayList<String>();
    private Socket connection;
    private BufferedReader bufferReader;
    private String currentUser = "";

    public Server(Socket connection) {
        this.connection = connection;

        try {
            // Reader
            bufferReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {

            BufferedWriter bufferWriter = new BufferedWriter(new OutputStreamWriter(this.connection.getOutputStream()));

            clients.add(bufferWriter);
            currentUser = this.bufferReader.readLine();
            users.add(currentUser);

            String msg = "Text&" + currentUser + " Conectado";

            while (!("Text&Disconnect " + currentUser).equalsIgnoreCase(msg) && msg != null) {
                broadCast(msg);
                System.out.println(currentUser + " [Server(run)] " + msg);
                msg = this.bufferReader.readLine();
            }

            removeUser(currentUser);

            broadCast("Text&Usuario " + currentUser + " Desconectado");
        } catch (Exception e) {
            e.printStackTrace();
            removeUser(currentUser);
        }
    }

    private void broadCast(String msg) {
        for (BufferedWriter bufferClient : clients) {
            try {
                bufferClient.write(msg + "\r\n");
                System.out.println("[Broadcast] " + msg);
                bufferClient.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void removeUser(String user) {
        int index = users.indexOf(user);
        clients.remove(index);
        users.remove(index);
    }
}
