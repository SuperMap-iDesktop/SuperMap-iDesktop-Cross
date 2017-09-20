package com.supermap.desktop.process.core;

import com.supermap.desktop.Application;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.enums.RunningStatus;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.process.events.StatusChangeEvent;
import com.supermap.desktop.process.events.StatusChangeListener;
import com.supermap.desktop.process.loader.DefaultProcessLoader;
import com.supermap.desktop.process.loader.IProcessLoader;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameters;
import com.supermap.desktop.process.parameter.interfaces.datas.InputData;
import com.supermap.desktop.process.parameter.interfaces.datas.Inputs;
import com.supermap.desktop.process.parameter.interfaces.datas.OutputData;
import com.supermap.desktop.process.parameter.interfaces.datas.Outputs;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.event.EventListenerList;
import java.text.MessageFormat;
import java.util.ArrayList;

/**
 * Created by highsad on 2017/1/5.
 * 将进度条提示信息的设置在此基类中实现，不额外设置时，显示默认值-yuanR2017.9.11
 */
public abstract class AbstractProcess implements IProcess {

	protected volatile RunningStatus status = RunningStatus.NORMAL;
	private EventListenerList listenerList = new EventListenerList();
	private Workflow workflow;
	private Inputs inputs = new Inputs(this);
	private Outputs outputs = new Outputs(this);
	private int serialID = 0;

	private String RUNNING_MESSAGE = ProcessProperties.getString("String_Running");
	private String COMPLETED_MESSAGE = ProcessProperties.getString("String_Completed");
	private String FAILED_MESSAGE = ProcessProperties.getString("String_Failed");

	protected String getRUNNING_MESSAGE() {
		return RUNNING_MESSAGE;
	}

	protected String getCOMPLETED_MESSAGE() {
		return COMPLETED_MESSAGE;
	}

	protected String getFAILED_MESSAGE() {
		return FAILED_MESSAGE;
	}

	private ArrayList<IReadyChecker<IProcess>> processReadyCheckerList = new ArrayList<>();

	public AbstractProcess() {
		setSerialID(hashCode());
	}

	@Override
	public int getSerialID() {
		return this.serialID;
	}

	@Override
	public void setSerialID(int serialID) {
		this.serialID = serialID;
	}

	@Override
	public Workflow getWorkflow() {
		return this.workflow;
	}

	@Override
	public void setWorkflow(Workflow workflow) {
		if (this.workflow != null && this.workflow != workflow) {
			getParameters().unbindWorkflow(this.workflow);
		}
		Workflow oldWorkflow = this.workflow;
		this.workflow = workflow;
		checkReadyState(new ReadyEvent(this, false));
		workflowChanged(oldWorkflow, workflow);
		if (this.workflow != null) {
			getParameters().bindWorkflow(this.workflow);
		}
	}

	protected void workflowChanged(Workflow oldWorkflow, Workflow workflow) {
	}

	@Override
	public abstract IParameters getParameters();

	@Override
	public synchronized final boolean run() {
		boolean isSuccessful = false;
		try {
			//	运行前，必要参数值是否异常判断(包括源数据、结果数据、必填参数)-yuanR2017.9.12
			if (isInputDataReady() && isOutputDataReady() && isReady(new ReadyEvent(this, true))) {
				setStatus(RunningStatus.RUNNING);
				fireRunning(new RunningEvent(this, 0,
						StringUtilities.isNullOrEmptyString(getRUNNING_MESSAGE()) ? RUNNING_MESSAGE : getRUNNING_MESSAGE()
				));
				isSuccessful = execute();
				if (isSuccessful) {
					fireRunning(new RunningEvent(this, 100,
							StringUtilities.isNullOrEmptyString(getCOMPLETED_MESSAGE()) ? COMPLETED_MESSAGE : getCOMPLETED_MESSAGE()
					));
					setStatus(RunningStatus.COMPLETED);
				} else if (!isCancelled()) {
					fireRunning(new RunningEvent(this, 0,
							StringUtilities.isNullOrEmptyString(getFAILED_MESSAGE()) ? FAILED_MESSAGE : getFAILED_MESSAGE()
					));
					setStatus(RunningStatus.EXCEPTION);
				}
			} else {
				setStatus(RunningStatus.EXCEPTION);
			}
		} catch (Exception e) {
			fireRunning(new RunningEvent(this, 0, FAILED_MESSAGE));
			Application.getActiveApplication().getOutput().output(e);
			setStatus(RunningStatus.EXCEPTION);
		}
		return isSuccessful;
	}

	/**
	 * 源数据是否存在
	 * yuanR2017.9.12
	 *
	 * @return
	 */
	private final boolean isInputDataReady() {
		Boolean isInPutDataReady = true;
		// 运行前，源数据和结果数据是否为空异常判断-yuanR2017.9.13
		InputData[] inputData = this.getParameters().getInputs().getDatas();
		for (InputData anInputData : inputData) {
			ArrayList<IParameter> iParameters = anInputData.getParameters();
			for (IParameter iParameter : iParameters) {
				if (!iParameter.isReady()) {
					Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_ParameterRequisiteUnFilled"), getTitle()));
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 结果数据是否存在
	 * yuanR2017.9.12
	 *
	 * @return
	 */
	private final boolean isOutputDataReady() {
//		Boolean isOutPutDataReady = true;
		// 运行前，源数据和结果数据是否为空异常判断-yuanR2017.9.13
		OutputData[] outputData = this.getParameters().getOutputs().getDatas();
		for (OutputData anOutputData : outputData) {
			ArrayList<IParameter> iParameters = anOutputData.getParameters();
			for (IParameter iParameter : iParameters) {
				if (!iParameter.isReady()) {
					Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_ParameterRequisiteUnFilled"), getTitle()));
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public final boolean isReady(ReadyEvent readyEvent) {
		if (!isReadyHook()) {
			return false;
		}
		// 参数是否准备就续-yuanR
		if (!getParameters().isReady()) {
			if (readyEvent.isOutputMessage()) {
				Application.getActiveApplication().getOutput().output(MessageFormat.format(ProcessProperties.getString("String_ParameterRequisiteUnFilled"), getTitle()));
			}
			return false;
		}
		ReadyEvent<IProcess> childReadyEvent = new ReadyEvent<>((IProcess) this, readyEvent.isOutputMessage());
		if (processReadyCheckerList.size() > 0) {
			for (IReadyChecker<IProcess> iProcessReadyChecker : processReadyCheckerList) {
				if (!iProcessReadyChecker.isReady(childReadyEvent)) {
					return false;
				}
			}
		}
		return true;
	}

	protected boolean isReadyHook() {
		return true;
	}

	@Override
	public boolean checkReadyState(ReadyEvent readyEvent) {
		if (isReady(readyEvent)) {
			setStatus(RunningStatus.READY);
			return true;
		} else {
			setStatus(RunningStatus.WARNING);
			return false;
		}
	}

	@Override
	public final void cancel() {
		if (this.status != RunningStatus.NORMAL || this.status == RunningStatus.CANCELLED) {
			return;
		}

		setStatus(RunningStatus.CANCELLED);
	}

	@Override
	public final boolean isCancelled() {
		return this.status == RunningStatus.CANCELLED;
	}

	@Override
	public final void setStatus(RunningStatus status) {
		if (this.status != status) {
			RunningStatus oldStatus = this.status;
			this.status = status;
			fireStatusChange(new StatusChangeEvent(this, this.status, oldStatus));
		}
	}

	public abstract boolean execute();

	@Override
	public void reset() {
		RunningStatus oldStatus = this.status;

		RunningStatus currentStatus = isReady(new ReadyEvent(this, false)) ? RunningStatus.READY : RunningStatus.WARNING;
		if (oldStatus != currentStatus) {
			setStatus(currentStatus);
			fireStatusChange(new StatusChangeEvent(this, getStatus(), oldStatus));
		}
	}

	@Override
	public Class<? extends IProcessLoader> getLoader() {
		return DefaultProcessLoader.class;
	}

	@Override
	public RunningStatus getStatus() {
		return this.status;
	}

	@Override
	public abstract String getKey();

	@Override
	public Inputs getInputs() {
		return this.inputs;
	}


	@Override
	public Outputs getOutputs() {
		return this.outputs;
	}

	@Override
	public void addProcessReadyChecker(IReadyChecker<IProcess> processReadyChecker) {
		if (processReadyChecker != null && !processReadyCheckerList.contains(processReadyChecker)) {
			processReadyCheckerList.add(processReadyChecker);
		}
	}

	@Override
	public void removeProcessReadyChecker(IReadyChecker<IProcess> processReadyChecker) {
		if (processReadyChecker != null) {
			processReadyCheckerList.remove(processReadyChecker);
		}
	}

	@Override
	public void addRunningListener(RunningListener listener) {
		this.listenerList.add(RunningListener.class, listener);
	}

	@Override
	public void removeRunningListener(RunningListener listener) {
		this.listenerList.remove(RunningListener.class, listener);
	}

	@Override
	public void addStatusChangeListener(StatusChangeListener listener) {
		this.listenerList.add(StatusChangeListener.class, listener);
	}

	@Override
	public void removeStatusChangeListener(StatusChangeListener listener) {
		this.listenerList.remove(StatusChangeListener.class, listener);
	}

	protected void fireRunning(RunningEvent e) {
		Object[] listeners = listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == RunningListener.class) {
				((RunningListener) listeners[i + 1]).running(e);
			}
		}
	}

	protected void fireStatusChange(StatusChangeEvent e) {
		Object[] listeners = this.listenerList.getListenerList();

		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == StatusChangeListener.class) {
				((StatusChangeListener) listeners[i + 1]).statusChange(e);
			}
		}
	}
}
