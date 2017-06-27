package com.supermap.desktop.process.tasks;

import com.sun.org.apache.bcel.internal.generic.RET;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.Group;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class TasksManagerContainer extends JPanel implements WorkerStateChangedListener {

	/**
	 *
	 */
	private final static long serialVersionUID = 1L;
	private TasksManager tasksManager;

	private JPanel panelRunning;
	private JPanel panelReady;
	private JPanel panelWaiting;
	private JPanel panelCompleted;
	private JPanel panelCancelled;
	private JPanel panelException;

	public TasksManagerContainer() {
		initializeComponents();
		initializeResources();
	}

	public void setTasksManager(TasksManager tasksManager) {
		if (this.tasksManager != null) {
			unloadTasksManager();
		}

		this.tasksManager = tasksManager;
		loadTasksManager();
	}

	private void loadTasksManager() {
		if (this.tasksManager != null) {
			this.tasksManager.addWorkerStateChangeListener(this);
		}
	}

	private void unloadTasksManager() {
		this.tasksManager.removeWorkerStateChangeListener(this);
		this.tasksManager = null;
	}

	private void initializeComponents() {
		this.panelRunning = new JPanel();
		this.panelRunning.setBorder(new TitledBorder("正在执行"));
		this.panelRunning.setLayout(new BoxLayout(this.panelRunning, BoxLayout.Y_AXIS));

		this.panelReady = new JPanel();
		this.panelReady.setBorder(new TitledBorder("准备就绪"));
		this.panelReady.setLayout(new BoxLayout(this.panelReady, BoxLayout.Y_AXIS));

		this.panelWaiting = new JPanel();
		this.panelWaiting.setBorder(new TitledBorder("等待"));
		this.panelWaiting.setLayout(new BoxLayout(this.panelWaiting, BoxLayout.Y_AXIS));

		this.panelCompleted = new JPanel();
		this.panelCompleted.setBorder(new TitledBorder("已完成"));
		this.panelCompleted.setLayout(new BoxLayout(this.panelCompleted, BoxLayout.Y_AXIS));

		this.panelCancelled = new JPanel();
		this.panelCancelled.setBorder(new TitledBorder("已取消"));
		this.panelCancelled.setLayout(new BoxLayout(this.panelCancelled, BoxLayout.Y_AXIS));
		this.panelCancelled.setVisible(false);

		this.panelException = new JPanel();
		this.panelException.setBorder(new TitledBorder("异常中断"));
		this.panelException.setLayout(new BoxLayout(this.panelException, BoxLayout.Y_AXIS));
		this.panelException.setVisible(false);

		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		setLayout(groupLayout);

		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(this.panelRunning, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(this.panelReady, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(this.panelWaiting, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(this.panelCompleted, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(this.panelCancelled, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addComponent(this.panelException, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addComponent(this.panelRunning, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(this.panelReady, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(this.panelWaiting, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(this.panelCompleted, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(this.panelCancelled, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
				.addComponent(this.panelException, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE));
	}

	private void buildTasksUI() {
		if (this.tasksManager == null) {
			return;
		}

	}

	private JPanel getPanel(int panel) {
		switch (panel) {
			case TasksManager.WORKER_STATE_CANCELLED:
				return this.panelCancelled;
			case TasksManager.WORKER_STATE_COMPLETED:
				return this.panelCompleted;
			case TasksManager.WORKER_STATE_EXCEPTION:
				return this.panelException;
			case TasksManager.WORKER_STATE_READY:
				return this.panelReady;
			case TasksManager.WORKER_STATE_RUNNING:
				return this.panelRunning;
			case TasksManager.WORKER_STATE_WAITING:
				return this.panelWaiting;
			default:
				return null;
		}
	}

	private void initializeResources() {
		// this.buttonApply.setText(CommonProperties.getString("String_Button_Apply"));
	}

	public void clear() {

	}

	public static void main(String[] args) {
		TasksManagerContainer container = new TasksManagerContainer();

		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(container, BorderLayout.CENTER);
		frame.setSize(400, 900);
		frame.setVisible(true);
	}

	@Override
	public void workerStateChanged(WorkerStateChangedEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}

