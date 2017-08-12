package com.supermap.desktop.process.tasks;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.process.enums.RunningStatus;
import com.supermap.desktop.process.events.StatusChangeEvent;
import com.supermap.desktop.process.events.StatusChangeListener;
import com.supermap.desktop.process.events.WorkflowChangeEvent;
import com.supermap.desktop.process.events.WorkflowChangeListener;
import com.supermap.desktop.process.tasks.events.WorkerStateChangedListener;
import com.supermap.desktop.process.tasks.events.WorkersChangedEvent;
import com.supermap.desktop.process.tasks.events.WorkersChangedListener;
import com.supermap.desktop.process.tasks.taskStates.TaskStateManager;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	public final static int WORKER_STATE_WARNING = 7;

	private final Lock lock = new ReentrantLock();
	private volatile int status = WORKFLOW_STATE_NORMAL;

//	private IWorkflowExecutor executor;

	private Timer scheduler;
	private Workflow workflow;
	private Map<IProcess, ProcessWorker> workersMap = new ConcurrentHashMap<>();

//	private Vector<IProcess> waiting = new Vector<>();
//	private Vector<IProcess> ready = new Vector<>();
//	private Vector<IProcess> running = new Vector<>();
//	private Vector<IProcess> completed = new Vector<>();
//	private Vector<IProcess> cancelled = new Vector<>();
//	private Vector<IProcess> exception = new Vector<>();
//	private Vector<IProcess> warning = new Vector<>();
//	private Map<Integer, Vector<IProcess>> workerQueueMaps = new HashMap<>();

	private EventListenerList listenerList = new EventListenerList();

	private TaskStateManager taskStateManager;

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
	private StatusChangeListener checkReRunListener = new StatusChangeListener() {
		@Override
		public void statusChange(StatusChangeEvent e) {
			if (e.getStatus() == RunningStatus.RUNNING) {
				if (getStatus() != WORKFLOW_STATE_RUNNING && getStatus() != WORKFLOW_STATE_RERUNNING) {
					// 如果是当前不是running则说明是重新启动的。
					status = WORKFLOW_STATE_RERUNNING;
					if (!scheduler.isRunning()) {
						workflow.setEditable(false);
						scheduler.start();
					}
				}
			}
		}
	};
	;

	public TasksManager(Workflow workflow) {
		this.workflow = workflow;

		loadWorkflow(workflow);
		taskStateManager = new TaskStateManager(this, workflow);

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
		return taskStateManager.get(workerState);
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
			process.addStatusChangeListener(checkReRunListener);
			this.workersMap.put(process, worker);
			taskStateManager.addProcess(process);
			fireWorkersChanged(new WorkersChangedEvent(this, worker, WorkersChangedEvent.ADD));
		}
	}

	/**
	 * @param process
	 */
	private void processRemoved(IProcess process) {
		if (this.workersMap.containsKey(process)) {
			fireWorkersChanged(new WorkersChangedEvent(this, workersMap.get(process), WorkersChangedEvent.REMOVE));
			process.removeStatusChangeListener(checkReRunListener);
			this.workersMap.remove(process);
			taskStateManager.removeProcess(process);
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
		for (IProcess process : processes) {
			if (this.workflow.isLeadingProcess(process) && process.isReady()) {
				taskStateManager.moveProcess(process, WORKER_STATE_READY);
			}
		}
	}

	private synchronized void reset() {
		this.workflow.setEditable(true);

		taskStateManager.reset();

		if (this.scheduler.isRunning()) {
			this.scheduler.stop();
		}

		this.status = TasksManager.WORKFLOW_STATE_NORMAL;
	}

	public void addWorkerStateChangeListener(WorkerStateChangedListener listener) {
		taskStateManager.addWorkersChangedListener(listener);
	}

	public void removeWorkerStateChangeListener(WorkerStateChangedListener listener) {
		taskStateManager.removeWorkersChangedListener(listener);
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
				Vector<IProcess> ready = taskStateManager.get(WORKER_STATE_READY);
				// TODO: 2017/8/12
				if (ready.size() > 0) {
					for (int i = ready.size() - 1; i >= 0; i--) {
						IProcess process = ready.get(i);
						workersMap.get(process).execute();
					}
				} else {
					// TODO: 2017/8/9 ready已经没有了，但是waitting还有需要处理下
				}

				// 当等待队列、就绪队列、运行队列均已经清空，则停止任务调度，并输出日志
				if (taskStateManager.get(WORKER_STATE_WAITING).size() == 0 && ready.size() == 0 && taskStateManager.get(WORKER_STATE_RUNNING).size() == 0) {
					scheduler.stop();
					workflow.setEditable(true);

					if (workflow.getProcessCount() == taskStateManager.get(WORKER_STATE_COMPLETED).size()) {
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
