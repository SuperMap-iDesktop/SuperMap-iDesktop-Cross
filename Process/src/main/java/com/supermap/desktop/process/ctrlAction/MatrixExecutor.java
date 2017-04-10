package com.supermap.desktop.process.ctrlAction;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.NodeMatrix;
import com.supermap.desktop.process.meta.MetaProcess;
import com.supermap.desktop.process.tasks.TaskStore;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by highsad on 2017/4/8.
 */
public class MatrixExecutor {
	private NodeMatrix matrix;
	private Timer timer;
	private List<IProcess> waiting = new CopyOnWriteArrayList<>();
	private List<IProcess> ready = new CopyOnWriteArrayList<>();
//	private List<IProcess> running = new CopyOnWriteArrayList<>();
//	private List<IProcess> done = new CopyOnWriteArrayList<>();

	public MatrixExecutor(NodeMatrix matrix) {
		this.matrix = matrix;
		this.timer = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				invokeWaiting();
			}
		});

		List starts = this.matrix.getAllStartNodes();
		if (starts.size() > 0) {
			for (Object o : starts) {
				if (o instanceof IProcess) {
					this.ready.add((IProcess) o);
				}
			}
		}

		starts = this.matrix.getSingleNodes();
		if (starts.size() > 0) {
			for (Object o : starts) {
				if (o instanceof IProcess) {
					this.ready.add((IProcess) o);
				}
			}
		}

		for (IProcess process : ready) {
			List next = this.matrix.getNextNodes(process);
			for (Object o : next) {
				if (o instanceof IProcess) {
					this.waiting.add((IProcess) o);
				}
			}
		}
	}

	public void run() {
		for (final IProcess process : ready) {
			runProcess(process);
		}
		this.timer.start();
	}

	private void runProcess(final IProcess process) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				TaskStore.getTask(process).doWork();
			}
		});
		thread.start();
	}

	private void invokeWaiting() {
		try {
			if (waiting.size() == 0) {
				this.timer.stop();
			}

			IProcess[] waitingArr = waiting.toArray(new IProcess[waiting.size()]);
			for (IProcess process : waitingArr) {
				List pre = this.matrix.getPreNodes(process);
				boolean isReady = true;

				for (Object o : pre) {
					if (o instanceof IProcess) {
						MetaProcess p = (MetaProcess) o;
						if (p.isFinished()) {
							if (this.ready.contains(p)) {
								this.ready.remove(p);
							}
						} else {
							isReady = false;
						}
					}
				}

				if (isReady) {
					this.waiting.remove(process);
					this.ready.add(process);
					runProcess(process);
					List next = this.matrix.getNextNodes(process);
					for (Object o : next) {
						if (o instanceof IProcess) {
							this.waiting.add((IProcess) o);
						}
					}

				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}
	}
}
