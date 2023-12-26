package ch.faetzminator.aocutil;

public final class PuzzleUtil {

    private PuzzleUtil() {
    }

    public static Timer start() {
        final Timer timer = new Timer();
        timer.start();
        System.out.println("Calculating...");
        return timer;
    }

    public static void end(final long solution) {
        end(solution, null);
    }

    public static void end(final long solution, final Timer timer) {
        System.out.println("Solution: " + solution);
        if (timer != null) {
            timer.stop();
            System.out.println("Time: " + timer.getElapsedFormatted());
        }
    }
}
