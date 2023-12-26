package ch.faetzminator.aocutil;

public class Timer {

    private boolean running;
    private long start = -1L;
    private long end = -1L;

    public void start() {
        if (running) {
            throw new IllegalArgumentException("already started");
        }
        running = true;
        start = System.currentTimeMillis();
        end = -1L;
    }

    public void stop() {
        if (!running) {
            throw new IllegalArgumentException("not started");
        }
        running = false;
        end = System.currentTimeMillis();
    }

    public long getElapsedMs() {
        if (running) {
            return System.currentTimeMillis() - start;
        }
        return end - start;
    }

    public String getElapsedFormatted() {
        final long elapsed = getElapsedMs() / 1000L;
        final int seconds = (int) (elapsed % 60L);
        final int minutes = (int) (elapsed / 60L % 60L);
        final long hours = elapsed / 60L / 60L;
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }
}
