import java.util.Random;
import java.util.concurrent.*;

public class TestaSomaNumerosAleatorios {

    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        GerarNumeroAleatorio2 tarefa1 = new GerarNumeroAleatorio2();
        GerarNumeroAleatorio2 tarefa2 = new GerarNumeroAleatorio2();
        GerarNumeroAleatorio2 tarefa3 = new GerarNumeroAleatorio2();

        System.out.println("Processando a tarefa ...");
        Future<Integer> futureT1 = threadpool.submit(tarefa1);
        Future<Integer> futureT2 = threadpool.submit(tarefa2);
        Future<Integer> futureT3 = threadpool.submit(tarefa3);

        while (!futureT1.isDone() && futureT2.isDone() && futureT3.isDone()) {
            System.out.println("As tarefas ainda não foram processadas!");
            // sleep for 1 millisecond before checking again
            Thread.sleep(1);
        }

        System.out.println("Tarefa completa!");
        long valor = futureT1.get();
        valor += futureT2.get() + futureT3.get();
        System.out.println("A soma dos valores gerados são: " + valor);
        threadpool.shutdown();
    }

}

class GerarNumeroAleatorio2 implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        Random random = new Random();
        Integer number = random.nextInt(100);
        System.out.println("Valor gerado: " + number);
        return number;
    }
}
