package no.jschief.lupercal.poc.waypointing.util;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PerfTimers {
    private HashMap<String, Timer> timers;

    public PerfTimers() {
        timers = new HashMap<String, Timer>();
    }

    public void addTimer(String timerName) {
        timers.put(timerName, new Timer());
    }

    public void startTimer(String timerName) {
        timers.get(timerName).start();
    }

    public void stopTimer(String timerName) {
        timers.get(timerName).stop();
    }

    public String checkElapsed(String timerName) {
        return "[PerfTimer:"+timerName+"] " + timers.get(timerName).getElapsed();
    }

    public String checkElapsedMS(String timerName) {
        return "[PerfTimer:"+timerName+"] " + timers.get(timerName).getMsElapsed();
    }

    public long getTotalElapsedMS() {
        long totalMS = 0;
        for(String s : timers.keySet()) {
            totalMS += timers.get(s).getMsElapsed();
        }
        return totalMS;
    }


    private class Timer {
        private long msStart;
        private long msElapsed;

        public Timer() {
        }

        public void start() {
            this.msStart = System.currentTimeMillis();
        }

        public void stop() {
            this.msElapsed += System.currentTimeMillis() - this.msStart;
        }

        public String getElapsed() {
            return String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(this.msElapsed),
                    TimeUnit.MILLISECONDS.toSeconds(this.msElapsed) - TimeUnit.MILLISECONDS.toSeconds( TimeUnit.MILLISECONDS.toMinutes(this.msElapsed) ) );
        }

        public long getMsElapsed() {
            return msElapsed;
        }

    }
}
