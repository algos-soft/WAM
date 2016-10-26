package it.algos.wam.migration;

import it.algos.webbase.web.bootstrap.ABootStrap;
import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import javax.servlet.ServletContext;

/**
 * Created by gac on 21 ott 2016.
 * <p>
 * Migrazione dalla vecchia versione di webambulanze
 */
public class DaemonMigration extends Scheduler {


    public static final String DAEMON_NAME = "DaemonMigrazione";
    private static DaemonMigration instance;


    private DaemonMigration() {
        super();
    }// end of constructor


    public static DaemonMigration getInstance() {
        if (instance == null) {
            instance = new DaemonMigration();
        }// fine del blocco if
        return instance;
    }// end of static method


    @Override
    public void start() throws IllegalStateException {
        if (!isStarted()) {
            super.start();

            // save daemons status flag into servlet context
            ServletContext svc = ABootStrap.getServletContext();
            svc.setAttribute(DAEMON_NAME, true);

            // Schedule task
            // Ogni ora
            schedule("5 * * * *", new TaskCicloBio());
        }// fine del blocco if
    }// end of method


    @Override
    public void stop() throws IllegalStateException {
        if (isStarted()) {
            super.stop();
            // save daemons status flag into servlet context
            ServletContext svc = ABootStrap.getServletContext();
            svc.setAttribute(DAEMON_NAME, false);
        }// fine del blocco if
    }// end of method


    class TaskCicloBio extends Task {
        @Override
        public void execute(TaskExecutionContext context) throws RuntimeException {
            new Migration();
        }// end of method
    }// end of inner class

}// end of class
