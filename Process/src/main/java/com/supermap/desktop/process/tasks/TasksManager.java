package com.supermap.desktop.process.tasks;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.core.Workflow;
import com.supermap.desktop.process.enums.RunningStatus;
import com.supermap.desktop.process.events.StatusChangeEvent;
import com.supermap.desktop.process.events.StatusChangeListener;
import com.supermap.desktop.process.events.WorkflowChangeEvent;
import com.supermap.desktop.process.events.WorkflowChangeListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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
	private final static int NORMAL = 0;
	private final static int RUNNING = 1;
	private final static int COMPLETED = 2;
	private final static int INTERRUPTED = 3;

	private final Lock lock = new ReentrantLock();
	private volatile int status = NORMAL;

//	private IWorkflowExecutor executor;

	private Timer scheduler;
	private Workflow workflow;
	private Map<IProcess, ProcessWorker> tasksMap = new ConcurrentHashMap<>();

	private List<IProcess> waiting = new ArrayList<>();
	private List<IProcess> ready = new ArrayList<>();
	private List<IProcess> running = new ArrayList<>();
	private List<IProcess> completed = new ArrayList<>();
	private List<IProcess> cancelled = new ArrayList<>();
	private List<IProcess> exceptionOccurred = new ArrayList<>();

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
		this.scheduler = new Timer(500, new SchedulerActionListener());
//		this.executor = new DefaultWorkflowExecutor();
//		this.workflow.addWorkflowChangeListener(this.workflowChangeListener);
	}

	public int getStatus() {
		return status;
	}

//	public IWorkflowExecutor getExecutor() {
//		return executor;
//	}
//
//	public void setExecutor(IWorkflowExecutor executor) {
//		this.executor = executor;
//	}

	private void processAdded(IProcess process) {
		if (!this.tasksMap.containsKey(process)) {
			ProcessWorker worker = new ProcessWorker(process);
			process.addStatusChangeListener(this.processStatusChangeListener);
			this.tasksMap.put(process, worker);
		}
	}

	private void processRemoved(IProcess process) {
		if (this.tasksMap.containsKey(process)) {
			ProcessWorker worker = this.tasksMap.get(process);
			process.removeStatusChangeListener(this.processStatusChangeListener);
			this.tasksMap.remove(worker.getProcess());
		}
	}

	public boolean execute() {
		try {
			if (this.status != NORMAL) {
				return false;
			}

			this.status = RUNNING;

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
//		Vector<IProcess> processes = this.workflow.getProcesses();
//		for (IProcess process :
//				processes) {
//			if (this.workflow.isLeadingProcess(process)) {
//				this.ready.add(process);
//			} else {
//				this.waiting.add(process);
//			}
//		}
	}

	private synchronized void reset() {
//		this.waiting.clear();
//		this.ready.clear();
//		this.running.clear();
//		this.completed.clear();
//		this.cancelled.clear();
//		this.exceptionOccurred.clear();
//		this.status = TasksManager.NORMAL;
//
//		if (this.scheduler.isRunning()) {
//			this.scheduler.stop();
//		}
//
//		Vector<IProcess> processes = this.workflow.getProcesses();
//		for (IProcess process :
//				processes) {
//			process.reset();
//		}
	}

	private void waitingToReady(IProcess process) {

		// 只有 waiting 才可以移动到 ready
		moveProcess(process, waiting, ready);
	}

	private void readyToRunning(IProcess process) {

		// 只有 ready 才可以移动到 running
		moveProcess(process, ready, running);
	}

	private void runningToCompleted(IProcess process) {

		// 只有 running 才可以移动到 completed
		moveProcess(process, running, completed);
	}

	private void runningToCancelled(IProcess process) {
		moveProcess(process, running, cancelled);
	}

	private void runningToExceptionOccurred(IProcess process) {
		moveProcess(process, running, exceptionOccurred);
	}

	private void moveProcess(IProcess process, List<IProcess> source, List<IProcess> destination) {
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
		}
	}

	private boolean isReady(IProcess process) {
//		boolean isReady = true;
//
//		if (process != null) {
//			Vector<IProcess> preProcesses = this.workflow.getFromProcesses(process);
//
//			for (int i = 0; i < preProcesses.size(); i++) {
//				isReady = preProcesses.get(i).getStatus() == RunningStatus.COMPLETED;
//				if (isReady == false) {
//					break;
//				}
//			}
//		}
//		return isReady;
		return true;
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
//			// 先处理自身状态
//			runningToCompleted(process);
//
//			// 再处理下级节点状态，检查下级节点是否所有前置节点都已执行完毕
//			Vector<IProcess> nextProcesses = workflow.getToProcesses(process);
//
//			for (int i = 0; i < nextProcesses.size(); i++) {
//
//				// 该节点的所有前置节点均已执行完成准备就绪，就将节点移动到 ready
//				if (isReady(nextProcesses.get(i))) {
//					waitingToReady(nextProcesses.get(i));
//				}
//			}
		}

		private void handleRunning(IProcess process) {
			readyToRunning(process);
		}

		private void handleException(IProcess process) {
//
//			// 先把自己移动到异常队列
//			runningToExceptionOccurred(process);
//
//			// 再把所有后续节点移动到异常队列
//			Vector<IProcess> nextProcesses = workflow.getToProcesses(process);
//			if (nextProcesses != null && nextProcesses.size() > 0) {
//				for (IProcess nextProcess :
//						nextProcesses) {
//					if (!waiting.contains(nextProcess)) {
//
//						// 如果后续节点不在 waiting 队列中，则抛一个异常
//						throw new UnsupportedOperationException();
//					}
//
//					moveProcess(nextProcess, waiting, exceptionOccurred);
//				}
//			}
		}

		private void handleCancelled(IProcess process) {
//
//			// 先把自己移动到取消队列
//			runningToCancelled(process);
//
//			// 再把所有后续节点移动到取消队列
//			Vector<IProcess> nextProcesses = workflow.getToProcesses(process);
//			if (nextProcesses != null && nextProcesses.size() > 0) {
//				for (IProcess nextProcess :
//						nextProcesses) {
//
//					// throw an exception if any followed process is not contained in waiting list.
//					if (!waiting.contains(nextProcess)) {
//						throw new UnsupportedOperationException();
//					}
//
//					moveProcess(nextProcess, waiting, cancelled);
//				}
//			}
		}
	}

	private class SchedulerActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
//			try {
//				lock.lock();
//				if (ready.size() > 0) {
//					for (int i = ready.size() - 1; i >= 0; i--) {
//						IProcess process = ready.get(i);
//						tasksMap.get(process).doWork();
//					}
//				}
//
//				// 当等待队列、就绪队列、运行队列均已经清空，则停止任务调度，并输出日志
//				if (waiting.size() == 0 && ready.size() == 0 && running.size() == 0) {
//					scheduler.stop();
//
//					if (workflow.getProcessCount() == completed.size()) {
//						status = TasksManager.COMPLETED;
//					} else {
//						status = TasksManager.INTERRUPTED;
//					}
//				}
//			} catch (Exception ex) {
//				Application.getActiveApplication().getOutput().output(ex);
//			} finally {
//				lock.unlock();
//			}
//

		}
	}

}
