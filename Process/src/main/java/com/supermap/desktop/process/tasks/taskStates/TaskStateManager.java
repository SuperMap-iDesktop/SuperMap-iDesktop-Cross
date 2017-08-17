package com.supermap.desktop.process.tasks.taskStates;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.process.enums.RunningStatus;
import com.supermap.desktop.process.events.StatusChangeEvent;
import com.supermap.desktop.process.events.StatusChangeListener;
import com.supermap.desktop.process.tasks.TasksManager;
import com.supermap.desktop.process.tasks.events.WorkerStateChangedEvent;
import com.supermap.desktop.process.tasks.events.WorkerStateChangedListener;

import javax.swing.event.EventListenerList;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author XiaJT
 */
public class TaskStateManager {

	private final TasksManager tasksManager;
	private final Workflow workflow;
	private TaskState waiting = new TaskState(TasksManager.WORKER_STATE_WAITING, ProcessProperties.getString("String_Waiting"));
	private TaskState ready = new TaskState(TasksManager.WORKER_STATE_READY, ProcessProperties.getString("String_Ready"));
	private TaskState running = new TaskState(TasksManager.WORKER_STATE_RUNNING, ProcessProperties.getString("String_Running"));
	private TaskState completed = new TaskState(TasksManager.WORKER_STATE_COMPLETED, ProcessProperties.getString("String_Completed"));
	private TaskState cancelled = new TaskState(TasksManager.WORKER_STATE_CANCELLED, ProcessProperties.getString("String_Cancelled"));
	private TaskState exception = new TaskState(TasksManager.WORKER_STATE_EXCEPTION, ProcessProperties.getString("String_Failed"));
	private TaskState warning = new TaskState(TasksManager.WORKER_STATE_WARNING, ProcessProperties.getString("String_warning"));


	private EventListenerList listenerList = new EventListenerList();

	private TaskState[] currentStates = new TaskState[]{
			waiting, ready, running, completed, cancelled, exception, warning
	};
	private ProcessStatusChangeListener processStatusChangeListener = new ProcessStatusChangeListener();
	private Lock lock = new ReentrantLock();

	public TaskStateManager(TasksManager tasksManager, Workflow workflow) {
		this.tasksManager = tasksManager;
		this.workflow = workflow;
	}

	public Vector<IProcess> get(int stateIndex) {
		try {
			lock.lock();
			for (TaskState currentState : currentStates) {
				if (currentState.getStateIndex() == stateIndex) {
					return currentState.getProcesses();
				}
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			lock.unlock();
		}
		return null;
	}

	public void addProcess(IProcess process) {
		if (process == null) {
			return;
		}
		waiting.addProcess(process);
		process.addStatusChangeListener(this.processStatusChangeListener);
	}

	public void removeProcess(IProcess process) {
		if (process == null) {
			return;
		}
		TaskState processState = getProcessState(process);
		process.removeStatusChangeListener(this.processStatusChangeListener);
		if (processState != null) {
			processState.removeProcess(process);
		}
	}

	public int getProcessStateIndex(IProcess process) {
		TaskState processState = getProcessState(process);
		if (processState != null) {
			return processState.getStateIndex();
		}
		return -1;
	}

	private TaskState getProcessState(IProcess process) {
		for (TaskState currentState : currentStates) {
			if (currentState.contain(process)) {
				return currentState;
			}
		}
		return null;
	}

	public void reset() {
		Vector<IProcess> processes = workflow.getProcesses();
		for (IProcess process : processes) {
			process.reset();
			if (process.isReady()) {
				moveProcess(process, TasksManager.WORKER_STATE_WAITING);
			}
		}
	}

	protected void fireWorkerStateChange(WorkerStateChangedEvent e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == WorkerStateChangedListener.class) {
				((WorkerStateChangedListener) listeners[i + 1]).workerStateChanged(e);
			}
		}
	}

	public void addWorkersChangedListener(WorkerStateChangedListener listener) {
		this.listenerList.add(WorkerStateChangedListener.class, listener);
	}

	public void removeWorkersChangedListener(WorkerStateChangedListener listener) {
		this.listenerList.remove(WorkerStateChangedListener.class, listener);
	}

	public void moveProcess(IProcess process, int resultState) {
		TaskState processState = getProcessState(process);
		TaskState resultTaskState = getState(resultState);
		if (processState != null && resultTaskState != null) {
			try {
				lock.lock();
				processState.moveProcessTo(process, getState(resultState));
				fireWorkerStateChange(new WorkerStateChangedEvent(tasksManager, tasksManager.getWorkerByProcess(process), processState.getStateIndex(), resultState));
			} catch (Exception e) {
				// todo 事务回退
			} finally {
				lock.unlock();
			}
		}
	}

	private TaskState getState(int resultState) {
		for (TaskState currentState : currentStates) {
			if (currentState.getStateIndex() == resultState) {
				return currentState;
			}
		}
		return null;
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
			} else if (e.getStatus() == RunningStatus.WARNING) {
				handleWarning(e.getProcess());
			} else if (e.getStatus() == RunningStatus.READY) {
				handleReady(e.getProcess());
			}
		}

		private void handleCompleted(IProcess process) {
			moveProcess(process, TasksManager.WORKER_STATE_COMPLETED);

			// 再处理下级节点状态，检查下级节点是否所有前置节点都已执行完毕
			Vector<IProcess> nextProcesses = workflow.getToProcesses(process);

			for (IProcess nextProcess : nextProcesses) {

				// 该节点的所有前置节点均已执行完成准备就绪，就将节点移动到 ready
				if (isReady(nextProcess) && nextProcess.isReady()) {
					moveProcess(nextProcess, TasksManager.WORKER_STATE_READY);
				}
			}
		}

		private boolean isReady(IProcess process) {
			boolean isReady = true;

			if (process != null) {
				Vector<IProcess> preProcesses = workflow.getFromProcesses(process);

				for (IProcess preProcess : preProcesses) {
					isReady = (preProcess.getStatus() == RunningStatus.COMPLETED);
					if (!isReady) {
						break;
					}
				}
			}
			return isReady;
		}

		private void handleRunning(IProcess process) {
			moveProcess(process, TasksManager.WORKER_STATE_RUNNING);
		}

		private void handleException(IProcess process) {

			// 先把自己移动到异常队列
			moveProcess(process, TasksManager.WORKER_STATE_EXCEPTION);

			// 再把所有后续节点移动到异常队列
			Vector<IProcess> nextProcesses = workflow.getToProcesses(process);
			if (nextProcesses != null && nextProcesses.size() > 0) {
				for (IProcess nextProcess :
						nextProcesses) {
					if (waiting.contain(process)) {
						moveProcess(nextProcess, TasksManager.WORKER_STATE_EXCEPTION);
					}

				}
			}
		}

		private void handleCancelled(IProcess process) {

			// 先把自己移动到取消队列
			moveProcess(process, TasksManager.WORKER_STATE_CANCELLED);

			// 再把所有后续节点移动到取消队列
			Vector<IProcess> nextProcesses = workflow.getToProcesses(process);
			if (nextProcesses != null && nextProcesses.size() > 0) {
				for (IProcess nextProcess :
						nextProcesses) {
					moveProcess(nextProcess, TasksManager.WORKER_STATE_CANCELLED);
				}
			}
		}
	}

	private void handleWarning(IProcess process) {
		moveProcess(process, TasksManager.WORKER_STATE_WARNING);
		// 后续节点需要移动吗？
	}

	private void handleReady(IProcess process) {
		moveProcess(process, TasksManager.WORKER_STATE_READY);
	}
}
