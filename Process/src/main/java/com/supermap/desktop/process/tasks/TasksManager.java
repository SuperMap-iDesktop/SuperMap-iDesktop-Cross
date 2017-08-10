package com.supermap.desktop.process.tasks;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.process.enums.RunningStatus;
import com.supermap.desktop.process.events.StatusChangeEvent;
import com.supermap.desktop.process.events.StatusChangeListener;
import com.supermap.desktop.process.events.WorkflowChangeEvent;
import com.supermap.desktop.process.events.WorkflowChangeListener;
import com.supermap.desktop.process.tasks.events.WorkerStateChangedEvent;
import com.supermap.desktop.process.tasks.events.WorkerStateChangedListener;
import com.supermap.desktop.process.tasks.events.WorkersChangedEvent;
import com.supermap.desktop.process.tasks.events.WorkersChangedListener;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
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
	private final static int WORKFLOW_STATE_RERUNNING = 4;// 出错重新运行

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
		loadWorkflow(workflow);

		this.scheduler = new Timer(500, new SchedulerActionListener());
//		this.executor = new DefaultWorkflowExecutor();
		this.workflow.addWorkflowChangeListener(this.workflowChangeListener);
	}

	private void loadWorkflow(Workflow workflow) {
		Vector<IProcess> processes = workflow.getProcesses();
		for (int i = 0; i < processes.size(); i++) {
			addNewProcess(processes.get(i));
		}
	}

	public int getStatus() {
		return status;
	}

	public ProcessWorker getWorkerByProcess(IProcess process) {
		return workersMap.get(process);
	}

	public Workflow getWorkflow() {
		return this.workflow;
	}

	public Vector<IProcess> getProcesses(int workerState) {
		return this.workerQueueMaps.get(workerState);
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
		addNewProcess(process);
	}

	private void addNewProcess(IProcess process) {
		if (!this.workersMap.containsKey(process)) {
			ProcessWorker worker = new ProcessWorker(process);
			process.addStatusChangeListener(this.processStatusChangeListener);
			this.workersMap.put(process, worker);
			this.waiting.add(process);
			fireWorkersChanged(new WorkersChangedEvent(this, worker, WorkersChangedEvent.ADD));
		}
	}

	/**
	 * @param process
	 */
	private void processRemoved(IProcess process) {
		if (this.workersMap.containsKey(process)) {
			process.removeStatusChangeListener(this.processStatusChangeListener);
			fireWorkersChanged(new WorkersChangedEvent(this, workersMap.get(process), WorkersChangedEvent.REMOVE));
			this.workersMap.remove(process);

			// 执行过程中禁止删除节点
			this.waiting.remove(process);
			this.ready.remove(process);
			this.cancelled.remove(process);
			this.completed.remove(process);
			this.exception.remove(process);
			this.running.remove(process);
		}
	}

	public boolean run() {
		try {
			if (this.status == WORKFLOW_STATE_RUNNING) {
				return false;
			}

			if (this.status == WORKFLOW_STATE_COMPLETED || this.status == WORKFLOW_STATE_INTERRUPTED) {
				reset();
			}

			this.status = WORKFLOW_STATE_RUNNING;

			initialize();
//			this.workflow.setEdiitable(false);
			if (this.ready == null) {
				return false;
			}

			// 正在运行的时候禁止添加、删除节点，禁止调整连接关系和状态
			if (!this.scheduler.isRunning()) {
				this.workflow.setEditable(false);
				this.scheduler.start();
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		}

		return true;
	}

	public boolean isExecuting() {
		return this.status == WORKER_STATE_RUNNING;
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
				moveProcess(process, WORKER_STATE_WAITING, WORKER_STATE_READY);
			}
		}
	}

	private synchronized void reset() {
		this.workflow.setEditable(true);

		for (int state :
				this.workerQueueMaps.keySet()) {
			if (state == WORKER_STATE_WAITING) {
				continue;
			}

			Vector<IProcess> processes = this.workerQueueMaps.get(state);

			if (processes != null && processes.size() > 0) {
				for (int i = processes.size() - 1; i >= 0; i--) {
					IProcess process = processes.get(i);
					// 重置 process 自身状态
					process.reset();

					// 重置 ProcessWorker
//					this.workersMap.put(process, new ProcessWorker(process));
					moveProcess(processes.get(i), state, WORKER_STATE_WAITING);
				}
			}
		}

		if (this.scheduler.isRunning()) {
			this.scheduler.stop();
		}

		this.status = TasksManager.WORKFLOW_STATE_NORMAL;
	}

	private int getProcessState(IProcess process) {
		for (int i = WORKER_STATE_RUNNING; i <= WORKER_STATE_EXCEPTION; i++) {
			Vector<IProcess> iProcesses = workerQueueMaps.get(i);
			if (iProcesses.contains(process)) {
				return i;
			}
		}
		return -1;
	}

	private void waitingToReady(IProcess process) {

		// 如果出错则重新执行
		int processState = getProcessState(process);
		if (processState == WORKER_STATE_WAITING) {
			moveProcess(process, WORKER_STATE_WAITING, WORKER_STATE_READY);
		} else if (processState != -1 && processState != WORKER_STATE_RUNNING) {//&& processState != WORKER_STATE_COMPLETED
			// 有非等待状态时的任务就绪
//			if (status == WORKFLOW_STATE_RERUNNING) {
			// 重启时再跑那些后续的任务？
			moveProcess(process, processState, WORKER_STATE_READY);

//			}
		}
	}

	private void readyToRunning(IProcess process) {

		int currentState = getProcessState(process);
		if (currentState != -1) {
			moveProcess(process, currentState, WORKER_STATE_RUNNING);
		}
		if (getStatus() != WORKFLOW_STATE_RUNNING && getStatus() != WORKFLOW_STATE_RERUNNING) {
			// 如果是当前不是running则说明是重新启动的。
			status = WORKFLOW_STATE_RERUNNING;
			if (!this.scheduler.isRunning()) {
				this.workflow.setEditable(false);
				this.scheduler.start();
			}
		}
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
				isReady = (preProcesses.get(i).getStatus() == RunningStatus.COMPLETED);
				if (!isReady) {
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

//		private int getWorkerState(RunningStatus runningStatus) {
//			if (runningStatus == RunningStatus.RUNNING) {
//				return WORKER_STATE_RUNNING;
//			}
//
//			if (runningStatus == RunningStatus.CANCELLED) {
//				return WORKER_STATE_CANCELLED;
//			}
//
//			if (runningStatus == RunningStatus.COMPLETED) {
//				return WORKER_STATE_COMPLETED;
//			}
//
//			if (runningStatus == RunningStatus.EXCEPTION) {
//				return WORKER_STATE_EXCEPTION;
//			}
//
//			if (runningStatus == RunningStatus.NORMAL) {
//				return WORKER_STATE_WAITING;
//			}
//
//			return -1;
//		}

		private void handleCompleted(IProcess process) {
			// 先处理自身状态
			runningToCompleted(process);

			// 再处理下级节点状态，检查下级节点是否所有前置节点都已执行完毕
			Vector<IProcess> nextProcesses = workflow.getToProcesses(process);

			for (int i = 0; i < nextProcesses.size(); i++) {

				// 该节点的所有前置节点均已执行完成准备就绪，就将节点移动到 ready
				if (isReady(nextProcesses.get(i))) {//&& nextProcesses.get(i).getStatus() != RunningStatus.COMPLETED
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
					if (waiting.contains(nextProcess)) {
						moveProcess(nextProcess, WORKER_STATE_WAITING, WORKER_STATE_EXCEPTION);
					}

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

	public void addWorkersChangedListener(WorkersChangedListener listener) {
		this.listenerList.add(WorkersChangedListener.class, listener);
	}

	public void removeWorkersChangedListener(WorkersChangedListener listener) {
		this.listenerList.remove(WorkersChangedListener.class, listener);
	}

	protected void fireWorkersChanged(WorkersChangedEvent e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == WorkersChangedListener.class) {
				((WorkersChangedListener) listeners[i + 1]).workersChanged(e);
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
						workersMap.get(process).execute();
					}
				} else {
					// TODO: 2017/8/9

				}

				// 当等待队列、就绪队列、运行队列均已经清空，则停止任务调度，并输出日志
				if (waiting.size() == 0 && ready.size() == 0 && running.size() == 0) {
					scheduler.stop();
					workflow.setEditable(true);

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
