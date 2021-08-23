import java.util.concurrent.*;

public class TestaFatorial {

    private static final ExecutorService threadpool = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Fatorial task = new Fatorial(10);
        System.out.println("Enviando a tarefa...");

        Future<Long> future = threadpool.submit(task);
        System.out.println("Task is submitted");

        while (!future.isDone()) {
            System.out.println("Tarefa não terminada ainda...");
            // espera para tentar novamente
            Thread.sleep(1);
        }

        System.out.println("Tarefa finalizada!");
        long factorial = (long) future.get();
        System.out.println("Fatorial de 10 é: " + factorial);
        threadpool.shutdown();
    }

}

class Fatorial implements Callable<Long> {

    private final int number;

    Fatorial(int number) {
        this.number = number;
    }

    @Override
    public Long call() throws Exception, InterruptedException {
        long output = 0;
        output = factorial(number);
        return output;
    }

    private long factorial(int number) {
        if (number < 0) {
            throw new IllegalArgumentException("Number must be greater than zero");
        }
        long result = 1;
        while (number > 0) {
            result *= number;
            number--;
        }
        return result;
    }
}
