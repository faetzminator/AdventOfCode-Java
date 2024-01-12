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
        final long elapsed = getElapsedMs();
        final int millis = (int) (elapsed % 1000L);
        final int seconds = (int) (elapsed / 1000L % 60L);
        final int minutes = (int) (elapsed / 1000L / 60L % 60L);
        final long hours = elapsed / 1000L / 60L / 60L;
        if (hours == 0 && minutes == 0) {
            return String.format("%02d:%02d.%03d", minutes, seconds, millis);
        }
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }
}
