package com.supermap.desktop.process.tasks;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.process.enums.RunningStatus;
import com.supermap.desktop.process.events.StatusChangeEvent;
import com.supermap.desktop.process.events.StatusChangeListener;
import com.supermap.desktop.process.events.WorkflowChangeEvent;
import com.supermap.desktop.process.events.WorkflowChangeListener;

import javax.swing.Timer;
import javax.swing.event.EventListenerList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 第一版实现先默认把 Workflow 的所有节点都直接罗列出来，取消单个执行的按钮
 * 后续版本优化为任务管理器中的任务在构建工作流的时候就可以直接同步当前的任务状态（哪些可以执行、哪些需要等待前置条件）等
 * 并支持任务单个执行，点击则从所有前置节点开始，知道运行完成当前节点为止
 * Created by highsad on 2017/6/14.
 */
public class TasksManager {
	private final static int WORKFLOW_STATE_NORMAL = 0;
	private final static int WORKFLOW_STATE_RUNNING = 1;
	private final static int WORKFLOW_STATE_COMPLETED = 2;
	private final static int WORKFLOW_STATE_INTERRUPTED = 3;

	public final static int WORKER_STATE_RUNNING = 1;
	public final static int WORKER_STATE_READY = 2;
	public final static int WORKER_STATE_WAITING = 3;
	public final static int WORKER_STATE_COMPLETED = 4;
	public final static int WORKER_STATE_CANCELLED = 5;
	public final static int WORKER_STATE_EXCEPTION = 6;

	private final Lock lock = new ReentrantLock();
	private volatile int status = WORKFLOW_STATE_NORMAL;

//	private IWorkflowExecutor executor;

	private Timer scheduler;
	private Workflow workflow;
	private Map<IProcess, ProcessWorker> workersMap = new ConcurrentHashMap<>();

	private Vector<IProcess> waiting = new Vector<>();
	private Vector<IProcess> ready = new Vector<>();
	private Vector<IProcess> running = new Vector<>();
	private Vector<IProcess> completed = new Vector<>();
	private Vector<IProcess> cancelled = new Vector<>();
	private Vector<IProcess> exception = new Vector<>();
	private Map<Integer, Vector<IProcess>> workerQueueMaps = new HashMap<>();

	private EventListenerList listenerList = new EventListenerList();

	private ProcessStatusChangeListener processStatusChangeListener = new ProcessStatusChangeListener();

	private WorkflowChangeListener workflowChangeListener = new WorkflowChangeListener() {
		@Override
		public void workflowChange(WorkflowChangeEvent e) {
			if (e.getType() == WorkflowChangeEvent.ADDED) {
				processAdded(e.getProcess());
			} else if (e.getType() == WorkflowChangeEvent.REMOVED) {
				processRemoved(e.getProcess());
			}
		}
	};

	public TasksManager(Workflow workflow) {
		this.workflow = workflow;

		this.workerQueueMaps.put(WORKER_STATE_WAITING, this.waiting);
		this.workerQueueMaps.put(WORKER_STATE_READY, this.ready);
		this.workerQueueMaps.put(WORKER_STATE_RUNNING, this.running);
		this.workerQueueMaps.put(WORKER_STATE_COMPLETED, this.completed);
		this.workerQueueMaps.put(WORKER_STATE_CANCELLED, this.cancelled);
		this.workerQueueMaps.put(WORKER_STATE_EXCEPTION, this.exception);

		this.scheduler = new Timer(500, new SchedulerActionListener());
//		this.executor = new DefaultWorkflowExecutor();
		this.workflow.addWorkflowChangeListener(this.workflowChangeListener);
	}

	public int getStatus() {
		return status;
	}

	public Vector<ProcessWorker> getProcessWorkers(int workerState) {
		if (!this.workerQueueMaps.containsKey(workerState)) {
			return null;
		}

		Vector<ProcessWorker> workers = new Vector<>();
		Vector<IProcess> processes = this.workerQueueMaps.get(workerState);

		for (int i = 0; i < processes.size(); i++) {
			workers.add(this.workersMap.get(processes.get(i)));
		}
		return workers;
	}

	public final static int[] getWorkerStates() {
		return new int[]{WORKER_STATE_CANCELLED,
				WORKER_STATE_COMPLETED,
				WORKER_STATE_EXCEPTION,
				WORKER_STATE_READY,
				WORKER_STATE_RUNNING,
				WORKER_STATE_WAITING};
	}

//	public IWorkflowExecutor getExecutor() {
//		return executor;
//	}
//
//	public void setExecutor(IWorkflowExecutor executor) {
//		this.executor = executor;
//	}

	private void processAdded(IProcess process) {
		if (!this.workersMap.containsKey(process)) {
			ProcessWorker worker = new ProcessWorker(process);
			process.addStatusChangeListener(this.processStatusChangeListener);
			this.workersMap.put(process, worker);
		}
	}

	private void processRemoved(IProcess process) {
		if (this.workersMap.containsKey(process)) {
			ProcessWorker worker = this.workersMap.get(process);
			process.removeStatusChangeListener(this.processStatusChangeListener);
			this.workersMap.remove(worker.getProcess());
		}
	}

	public boolean execute() {
		try {
			if (this.status != WORKFLOW_STATE_NORMAL) {
				return false;
			}

			this.status = WORKFLOW_STATE_RUNNING;

			initialize();
//			this.workflow.setEdiitable(false);
			if (this.ready != null) {
				return false;
			}

			// 正在运行的时候禁止添加、删除节点，禁止调整连接关系和状态
			if (!this.scheduler.isRunning()) {
				this.scheduler.start();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}

		return true;
	}

	public void cancel() {
		// 待定
	}

	public void pause() {
		// 待定
	}

	private void initialize() {
		Vector<IProcess> processes = this.workflow.getProcesses();
		for (IProcess process :
				processes) {
			if (this.workflow.isLeadingProcess(process)) {
				this.ready.add(process);
			} else {
				this.waiting.add(process);
			}
		}
	}

	private synchronized void reset() {
		this.waiting.clear();
		this.ready.clear();
		this.running.clear();
		this.completed.clear();
		this.cancelled.clear();
		this.exception.clear();
		this.status = TasksManager.WORKFLOW_STATE_NORMAL;

		if (this.scheduler.isRunning()) {
			this.scheduler.stop();
		}

		Vector<IProcess> processes = this.workflow.getProcesses();
		for (IProcess process :
				processes) {
			process.reset();
		}
	}

	private void waitingToReady(IProcess process) {

		// 只有 waiting 才可以移动到 ready
		moveProcess(process, WORKER_STATE_WAITING, WORKER_STATE_READY);
	}

	private void readyToRunning(IProcess process) {

		// 只有 ready 才可以移动到 running
		moveProcess(process, WORKER_STATE_READY, WORKER_STATE_RUNNING);
	}

	private void runningToCompleted(IProcess process) {

		// 只有 running 才可以移动到 completed
		moveProcess(process, WORKER_STATE_RUNNING, WORKER_STATE_COMPLETED);
	}

	private void runningToCancelled(IProcess process) {
		moveProcess(process, WORKER_STATE_RUNNING, WORKER_STATE_CANCELLED);
	}

	private void runningToExceptionOccurred(IProcess process) {
		moveProcess(process, WORKER_STATE_RUNNING, WORKER_STATE_EXCEPTION);
	}

	private void moveProcess(IProcess process, int oldState, int newState) {
		if (!this.workerQueueMaps.containsKey(oldState) || !this.workerQueueMaps.containsKey(newState)) {
			return;
		}

		List<IProcess> source = this.workerQueueMaps.get(oldState);
		List<IProcess> destination = this.workerQueueMaps.get(newState);

		if (process != null && source != null && destination != null) {
			synchronized (source) {
				if (!source.contains(process)) {
					throw new UnsupportedOperationException();
				}

				source.remove(process);
			}

			synchronized (destination) {
				if (destination.contains(process)) {
					throw new UnsupportedOperationException();
				}

				destination.add(process);
			}

			fireWorkerStateChange(new WorkerStateChangedEvent(this, this.workersMap.get(process), oldState, newState));
		}
	}

	private boolean isReady(IProcess process) {
		boolean isReady = true;

		if (process != null) {
			Vector<IProcess> preProcesses = this.workflow.getFromProcesses(process);

			for (int i = 0; i < preProcesses.size(); i++) {
				isReady = preProcesses.get(i).getStatus() == RunningStatus.COMPLETED;
				if (isReady == false) {
					break;
				}
			}
		}
		return isReady;
	}

	private class ProcessStatusChangeListener implements StatusChangeListener {

		@Override
		public void statusChange(StatusChangeEvent e) {
			if (e.getStatus() == RunningStatus.COMPLETED) {
				handleCompleted(e.getProcess());
			} else if (e.getStatus() == RunningStatus.RUNNING) {
				handleRunning(e.getProcess());
			} else if (e.getStatus() == RunningStatus.EXCEPTION) {
				handleException(e.getProcess());
			} else if (e.getStatus() == RunningStatus.CANCELLED) {
				handleCancelled(e.getProcess());
			}
		}

		private void handleCompleted(IProcess process) {
			// 先处理自身状态
			runningToCompleted(process);

			// 再处理下级节点状态，检查下级节点是否所有前置节点都已执行完毕
			Vector<IProcess> nextProcesses = workflow.getToProcesses(process);

			for (int i = 0; i < nextProcesses.size(); i++) {

				// 该节点的所有前置节点均已执行完成准备就绪，就将节点移动到 ready
				if (isReady(nextProcesses.get(i))) {
					waitingToReady(nextProcesses.get(i));
				}
			}
		}

		private void handleRunning(IProcess process) {
			readyToRunning(process);
		}

		private void handleException(IProcess process) {

			// 先把自己移动到异常队列
			runningToExceptionOccurred(process);

			// 再把所有后续节点移动到异常队列
			Vector<IProcess> nextProcesses = workflow.getToProcesses(process);
			if (nextProcesses != null && nextProcesses.size() > 0) {
				for (IProcess nextProcess :
						nextProcesses) {
					if (!waiting.contains(nextProcess)) {

						// 如果后续节点不在 waiting 队列中，则抛一个异常
						throw new UnsupportedOperationException();
					}

					moveProcess(nextProcess, WORKER_STATE_WAITING, WORKER_STATE_EXCEPTION);
				}
			}
		}

		private void handleCancelled(IProcess process) {

			// 先把自己移动到取消队列
			runningToCancelled(process);

			// 再把所有后续节点移动到取消队列
			Vector<IProcess> nextProcesses = workflow.getToProcesses(process);
			if (nextProcesses != null && nextProcesses.size() > 0) {
				for (IProcess nextProcess :
						nextProcesses) {

					// throw an exception if any followed process is not contained in waiting list.
					if (!waiting.contains(nextProcess)) {
						throw new UnsupportedOperationException();
					}

					moveProcess(nextProcess, WORKER_STATE_WAITING, WORKER_STATE_CANCELLED);
				}
			}
		}
	}

	public void addWorkerStateChangeListener(WorkerStateChangedListener listener) {
		this.listenerList.add(WorkerStateChangedListener.class, listener);
	}

	public void removeWorkerStateChangeListener(WorkerStateChangedListener listener) {
		this.listenerList.remove(WorkerStateChangedListener.class, listener);
	}

	protected void fireWorkerStateChange(WorkerStateChangedEvent e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == WorkerStateChangedListener.class) {
				((WorkerStateChangedListener) listeners[i + 1]).workerStateChanged(e);
			}
		}
	}

	private class SchedulerActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				lock.lock();
				if (ready.size() > 0) {
					for (int i = ready.size() - 1; i >= 0; i--) {
						IProcess process = ready.get(i);
						workersMap.get(process).doWork();
					}
				}

				// 当等待队列、就绪队列、运行队列均已经清空，则停止任务调度，并输出日志
				if (waiting.size() == 0 && ready.size() == 0 && running.size() == 0) {
					scheduler.stop();

					if (workflow.getProcessCount() == completed.size()) {
						status = TasksManager.WORKFLOW_STATE_COMPLETED;
					} else {
						status = TasksManager.WORKFLOW_STATE_INTERRUPTED;
					}
				}
			} catch (Exception ex) {
				Application.getActiveApplication().getOutput().output(ex);
			} finally {
				lock.unlock();
			}


		}
	}

}
