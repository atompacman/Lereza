package com.atompacman.lereza.report;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.atompacman.atomlog.Log.Verbose;
import com.atompacman.lereza.exception.TimerException;
import com.atompacman.toolkat.exception.Throw;

public class Report {

	//======================================= FIELDS =============================================\\

	// Timers
	private final List<Procedure>	runningProc;
	private final List<Procedure> 	stoppedProc;
	private final Set<String> 		startedCPs;

	// Internal
	private Procedure 	currProc;
	private int			currLvl;
	private boolean		paused;
	private Verbose		verbose;
	


	//======================================= METHODS ============================================\\

	//---------------------------------- PUBLIC CONSTRUCTOR --------------------------------------\\

	public Report() {
		this.runningProc = new LinkedList<>();
		this.stoppedProc = new LinkedList<>();
		this.startedCPs  = new HashSet<>();

		this.currProc 	 = null;
		this.currLvl 	 = 0;
		this.paused		 = false;
		this.verbose	 = Verbose.INFOS;
	}


	//-------------------------------------- PROCEDURES ------------------------------------------\\

	public Report start(Checkpoint cp) {
		if (paused) {
			Throw.aRuntime(TimerException.class, "Method \"resume()\" "
					+ "must be called before starting new procedures");
		}

		if (!startedCPs.add(cp.info().getName())) {
			Throw.aRuntime(TimerException.class, "Cannot start "
					+ "two procedures for the same checkpoint");
		}

		currProc = new Procedure(cp, currLvl);
		currProc.click();
		runningProc.add(currProc);

		add(new LogEntry(cp.info().getName(), Verbose.INFOS, 1));

		return this;
	}

	public Report startChild(Checkpoint cp) {
		++currLvl;
		return start(cp);
	}

	public void pause() {
		if (currProc == null) {
			Throw.aRuntime(TimerException.class, "A procedure must be running before pausing");
		}
		if (paused) {
			Throw.aRuntime(TimerException.class, "Procedures are already paused");
		}
		paused = true;

		for (Procedure procedure : runningProc) {
			procedure.click();
		}
	}

	public void resume() {
		if (!paused) {
			Throw.aRuntime(TimerException.class, "Procedures must be paused before resuming");
		}
		paused = false;

		for (Procedure procedure : runningProc) {
			procedure.click();
		}
	}

	public Report stop() {
		if (currProc == null) {
			Throw.aRuntime(TimerException.class, "Cannot stop a "
					+ "procedure: Every procedures have been stopped");
		}
		if (paused) {
			Throw.aRuntime(TimerException.class, "Method \"resume()\" "
					+ "must be called before stopping procedures");		
		}

		currProc.click();
		stoppedProc.add(runningProc.remove(runningProc.size() - 1));
		
		if (runningProc.isEmpty()) {
			currProc = null;
			currLvl = 0;
		} else {			
			currProc = runningProc.get(runningProc.size() - 1);
			currLvl = currProc.getLvl();
		}
		
		return this;
	}


	//----------------------------------------- LOG ----------------------------------------------\\

	public void setVerbose(Verbose verbose) {
		this.verbose = verbose;
	}
	
	public void log(String format, Object...args) {
		add(new LogEntry(String.format(format, args), verbose));
	}
	
	public void log(Verbose verbose, String format, Object...args) {
		add(new LogEntry(String.format(format, args), verbose));
	}
	
	public void log(Verbose verbose, int titleSpacing, String format, Object...args) {
		add(new LogEntry(String.format(format, args), verbose, titleSpacing));
	}
	
	
	//------------------------------------ SIGNAL ANOMALY ----------------------------------------\\

	public void signal(Anomaly anomaly) {
		add(new AnomalyOccurrence(anomaly.info()));
	}

	public void signal(Anomaly anomaly, String details) {
		add(new AnomalyOccurrence(anomaly.info(), details));
	}
	
	public void signal(Anomaly anomaly, String format, Object...args) {
		add(new AnomalyOccurrence(anomaly.info(), String.format(format, args)));
	}
	
	
	//----------------------------------------- ADD ----------------------------------------------\\

	private void add(Observation obs) {
		if (currProc == null) {
			Throw.aRuntime(TimerException.class, "Cannot add an "
					+ "observation when no procedure is running");
		}
		currProc.addObservation(obs);
		obs.log();
	}
	
	
	//--------------------------------------- GETTERS --------------------------------------------\\

	public Checkpoint getCurrCheckpoint() {
		if (currProc == null) {
			throw new IllegalStateException("Cannot get current "
					+ "checkpoint: no procedures are running");
		}
		return currProc.getCp();
	}
}
