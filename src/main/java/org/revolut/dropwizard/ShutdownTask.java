package org.revolut.dropwizard;

import io.dropwizard.servlets.tasks.Task;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ShutdownTask extends Task {

    public ShutdownTask() {
        super("shutdown");
    }

    @Override
    public void execute(Map<String, List<String>> map, PrintWriter printWriter) {
        new Timer().schedule(new TimerTask() {
            public void run() {
                System.exit(0);
            }
        }, 3000);
    }
}
