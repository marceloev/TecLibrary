package br.com.teclibrary.system.db;

import java.util.Timer;
import java.util.TimerTask;

public class CloseConnectionTask extends TimerTask {

    private final Timer timer = new Timer();
    private final ModelConnection modelConnection;

    public CloseConnectionTask(ModelConnection modelConnection, int timeOut) {
        this.timer.schedule(this, timeOut * 1000);
        this.modelConnection = modelConnection;
    }

    @Override
    public void run() {
        if (this.modelConnection.getEntityManagerFactory().isOpen()) {
            String closeMSG = String.format("O sistema está finalizando a conexão a seguir pois," +
                            " expirou o tempo máximo delimitado. O tempo máximo era %d e a sessão finalizada é %s",
                    modelConnection.getTimeOut(), modelConnection.toString());
            System.out.println(closeMSG);
            modelConnection.getEntityManagerFactory().close();
        }
    }

}
