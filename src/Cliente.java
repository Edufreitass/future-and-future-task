import java.io.*;
import java.net.Socket;

/**
 * Cliente do servidor desenvolvido para testar as classes de execução em paralelo
 */
public class Cliente {

    public static void main(String[] args) {
        String serverName = "localhost";
        int port = 8000;
        try {
            System.out.println("Iniciando a conexão!");
            // faz a conexão
            Socket client = new Socket(serverName, port);
            System.out.println("Conectado a: " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();

            // envia a mensagem para o servidor
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("Olá servidor! " + client.getLocalSocketAddress());

            // recebe a resposta do servidor
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);

            // imprime a resposta
            System.out.println("Resposta: " + in.readUTF());
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
