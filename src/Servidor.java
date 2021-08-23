import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Implementação de um servidor bem simples para demonstrar as várias
 * formas de implementar programação paralela com Java.
 */
public class Servidor implements Runnable {

    private final ServerSocket serverSocket;
    private final ExecutorService pool;

    List<Future> requisicoes = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        // cria uma Thread nova executando o servidor
        System.out.println("Servidor no ar");
        new Thread(new Servidor(8000, 3)).run();
    }

    public Servidor(int port, int poolSize) throws IOException {
        serverSocket = new ServerSocket(port);
        pool = Executors.newFixedThreadPool(poolSize);
    }

    // run the service
    @Override
    public void run() {
        try {
            // cria uma tarefa para a verificação das requisições
            pool.execute(new VerificaRequisicao(requisicoes));
            while (true) {
                System.out.println("Nova requisição!");
                Future req = pool.submit(new TrataRequisicao(serverSocket.accept()));
                // armazena todas as requisições
                requisicoes.add(req);
            }
        } catch (IOException e) {
            pool.shutdown();
        }
    }
}

// classe que executa a cada 10 segundos e verifica o status das requisições
class VerificaRequisicao implements Runnable {

    private final List<Future> requisicoes;

    VerificaRequisicao(List<Future> requisicoes) {
        this.requisicoes = requisicoes;
    }

    @Override
    public void run() {
        while (true) {
            int somaTerminadas = 0;
            int somaCanceladas = 0;
            int somaEmExecucao = 0;
            try {
                Thread.sleep(10000);
                for (Future f : requisicoes) {
                    if (f.isDone()) {
                        somaTerminadas++;
                    } else if (f.isCancelled()) {
                        somaCanceladas++;
                    } else if (!f.isDone()) {
                        somaEmExecucao++;
                    }
                }
                System.out.println("Terminadas: " + somaTerminadas);
                System.out.println("Canceladas: " + somaCanceladas);
                System.out.println("Execução: " + somaEmExecucao);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

// classe que recebe a requisição do cliente e a responde
class TrataRequisicao implements Runnable {

    private final Socket server;

    public TrataRequisicao(Socket server) {
        this.server = server;
    }

    @Override
    public void run() {
        try {
            System.out.println("Conectado a: " + server.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(server.getInputStream());
            System.out.println(in.readUTF());

            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            Thread.sleep(5000);
            out.writeUTF("Sua conexão terminou! Tchau!");
            server.close();
        } catch (SocketTimeoutException e) {
            System.out.println("Socket timed out");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
