package it.algos.wam.daemons;

import it.algos.webbase.web.bootstrap.ABootStrap;
import it.sauronsoftware.cron4j.Scheduler;
import it.sauronsoftware.cron4j.Task;
import it.sauronsoftware.cron4j.TaskExecutionContext;

import javax.servlet.ServletContext;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Schedulatore che esegue periodicamente singoli task in appositi thread sul server.
 * Implementa il pattern Singleton.
 * Uso:
 * Per avviare il daemon e schedulare i task, invocare il metodo start().
 * Per fermare il daemon invocare il metodo stop().
 * Nel metodo execute() della classe WamTask, eseguire i task.
 *
 */
public class WamScheduler extends Scheduler {

	private final static Logger logger = Logger.getLogger(WamScheduler.class.getName());

	private static WamScheduler instance;
	public static final String DAEMON_NAME = "wam_daemon";

	private WamScheduler() {
		super();
	}

	class WamTask extends Task {

		@Override
		public void execute(TaskExecutionContext context) throws RuntimeException {
			logger.log(Level.INFO, "Start WAM daemon tasks");
			Runnable checker = new CompanyTask();
			checker.run();
			logger.log(Level.INFO, "End WAM daemon tasks");
		}
	}

	@Override
	public void start() throws IllegalStateException {
		if (!isStarted()) {
			super.start();

			// save daemon status flag into servlet context
			ServletContext svc = ABootStrap.getServletContext();
			svc.setAttribute(DAEMON_NAME, true);

			// Schedule task.
			schedule("0 * * * *", new WamTask());
			logger.log(Level.INFO, "WAM daemon attivato (eseguirà all'inizio di ogni ora).");

		}

	}

	@Override
	public void stop() throws IllegalStateException {
		if (isStarted()) {
			super.stop();

			// save daemon status flag into servlet context
			ServletContext svc = ABootStrap.getServletContext();
			svc.setAttribute(DAEMON_NAME, false);

			logger.log(Level.INFO, "WAM daemon disattivato.");

		}

	}


	public static WamScheduler getInstance() {
		if (instance == null) {
			instance = new WamScheduler();
		}
		return instance;
	}

}
