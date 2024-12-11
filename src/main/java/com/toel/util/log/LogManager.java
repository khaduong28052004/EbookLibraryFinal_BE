package com.toel.util.log;

import java.util.ArrayList;

public class LogManager extends ThreadManager {
    @Override
    public void doProcess(ArrayList items) {
        LogThread logThread = new LogThread();
        logThread.setItems(items);
        executorService.submit(logThread);
    }
}
