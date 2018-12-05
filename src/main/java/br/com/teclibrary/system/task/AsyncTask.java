package br.com.teclibrary.system.task;

public class AsyncTask extends Thread {

    private final Runnable runnable;

    public AsyncTask(Runnable runnable) {
        super(runnable);
        this.runnable = runnable;
    }

    public void trigger() {
        this.start();
    }

    public void trigger(Integer delayInSeconds) throws Exception {
        Thread.sleep(delayInSeconds * 1000);
        this.trigger();
    }
}
