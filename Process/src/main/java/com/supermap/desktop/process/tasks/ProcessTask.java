package com.supermap.desktop.process.tasks;

import com.supermap.analyst.spatialanalyst.InterpolationAlgorithmType;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessInterpolator;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessOverlayAnalyst;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.enums.OverlayAnalystType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;
import java.util.concurrent.CancellationException;

/**
 * Created by xie on 2017/2/15.
 * progress bar used for displaying process task progress
 */
public class ProcessTask extends JPanel implements IProcessTask, IContentModel {

    private static final long serialVersionUID = 1L;

    private transient SwingWorker<Boolean, Object> worker;
    private volatile String message = "";
    private volatile String remainTime = "";
    private volatile int percent = 0;
    private volatile boolean isStop;

    private volatile JProgressBar progressBar;
    private volatile JLabel labelTitle;
    private volatile JLabel labelMessage;
    private volatile JLabel labelRemaintime;
    private volatile JButton buttonRun = null;
    private volatile IProcess process;
    private volatile boolean isFinished;
    private ActionListener cancelListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            stop();
        }
    };
    private RunningListener runningListener = new RunningListener() {
        @Override
        public void running(RunningEvent e) {
            if (e.getProgress() >= 100) {
                updateProgress(100, String.valueOf(e.getRemainTime()), getFinishMessage());
                isFinished = true;
            } else {
                updateProgress(e.getProgress(), String.valueOf(e.getRemainTime()), e.getMessage());
            }
        }
    };

    public ProcessTask(IProcess process) {
        this.process = process;
        init();
    }

    private void init() {
        initComponents();
        initLayout();
        registEvents();
        initResouces();
        this.labelRemaintime.setVisible(false);
    }

    @Override
    public void initComponents() {
        labelTitle = new JLabel();
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        labelMessage = new JLabel("...");
        labelRemaintime = new JLabel("...");
        buttonRun = new JButton(ControlsResources.getIcon("/controlsresources/ToolBar/Image_Run.png"));
    }

    @Override
    public void initLayout() {
        Dimension dimension = new Dimension(18, 18);
        this.buttonRun.setPreferredSize(dimension);
        this.buttonRun.setMinimumSize(dimension);
        this.setLayout(new GridBagLayout());
        this.add(this.labelTitle, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.progressBar, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(1, 0));
        this.add(this.buttonRun, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraintsHelper.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.labelMessage, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(this.labelRemaintime, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setWeight(0, 0));
        this.add(new JPanel(), new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH).setWeight(1, 1));
    }

    @Override
    public void initResouces() {
        if (process.getKey().equals(MetaKeys.OVERLAY_ANALYST)) {
            OverlayAnalystType analystType = ((MetaProcessOverlayAnalyst) process).getAnalystType();
            switch (analystType) {
                case CLIP:
                    labelTitle.setText(ControlsProperties.getString("String_OverlayAnalyst_CLIP"));
                    break;
                case ERASE:
                    labelTitle.setText(ControlsProperties.getString("String_OverlayAnalyst_ERASE"));
                    break;
                case IDENTITY:
                    labelTitle.setText(ControlsProperties.getString("String_OverlayAnalyst_IDENTITY"));
                    break;
                case INTERSECT:
                    labelTitle.setText(ControlsProperties.getString("String_OverlayAnalyst_INTERSECT"));
                    break;
                case UNION:
                    labelTitle.setText(ControlsProperties.getString("String_OverlayAnalyst_UNION"));
                    break;
                case XOR:
                    labelTitle.setText(ControlsProperties.getString("String_OverlayAnalyst_XOR"));
                    break;
                case UPDATE:
                    labelTitle.setText(ControlsProperties.getString("String_OverlayAnalyst_UPDATE"));
                    break;
                default:
                    break;
            }
        } else if (process.getKey().equals(MetaKeys.INTERPOLATOR)) {
            InterpolationAlgorithmType type = ((MetaProcessInterpolator) process).getInterpolationAlgorithmType();
            if (type.equals(InterpolationAlgorithmType.IDW)) {
                labelTitle.setText(ControlsProperties.getString("String_Interpolator_IDW"));
            } else if (type.equals(InterpolationAlgorithmType.RBF)) {
                labelTitle.setText(ControlsProperties.getString("String_Interpolator_RBF"));
            } else if (type.equals(InterpolationAlgorithmType.KRIGING)) {
                labelTitle.setText(ControlsProperties.getString("String_Interpolator_KRIGING"));
            } else if (type.equals(InterpolationAlgorithmType.SimpleKRIGING)) {
                labelTitle.setText(ControlsProperties.getString("String_Interpolator_SimpleKRIGING"));
            } else if (type.equals(InterpolationAlgorithmType.UniversalKRIGING)) {
                labelTitle.setText(ControlsProperties.getString("String_Interpolator_UniversalKRIGING"));
            }
        } else {
            labelTitle.setText(process.getTitle());
        }
    }

    @Override
    public void registEvents() {
        removeEvents();
        this.buttonRun.addActionListener(this.cancelListener);
    }

    @Override
    public void removeEvents() {
        this.buttonRun.removeActionListener(this.cancelListener);
    }

    public void doWork() {
        try {
            process.addRunningListener(this.runningListener);
            process.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(final String message) {
        this.message = message;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                labelMessage.setText(message);
            }
        });
    }

    public synchronized boolean isFinished() {
        return isFinished;
    }

    public String getRemainTime() {
        return this.remainTime;
    }

    public void setRemainTime(final String remainTime) {
        this.remainTime = remainTime;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                labelRemaintime.setText(remainTime);
            }
        });
    }

    public int getPercent() {
        return this.percent;
    }

    public void setPercent(final int percent) {
        this.percent = percent;

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                progressBar.setValue(percent);
            }
        });
    }

    @Override
    public IProcess getProcess() {
        return process;
    }

    @Override
    public boolean isCancel() {
        return false;
    }

    @Override
    public void setCancel(boolean isCancel) {
        setStop(isCancel);
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;

        if (this.isStop) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    buttonRun.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/Image_Stop.png"));
                }
            });
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    buttonRun.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/Image_Run.png"));
                }
            });
            doWork();
        }
    }

    private void stop() {
        setStop(!isStop);
    }

    @Override
    public void updateProgress(final int percent, final String remainTime, final String message) throws CancellationException {
        if (this.isStop) {
            throw new CancellationException();
        } else {
            this.percent = percent;
            this.remainTime = remainTime;
            this.message = message;

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    progressBar.setValue(percent);
                    labelRemaintime.setText(MessageFormat.format(ControlsProperties.getString("String_RemainTime"), remainTime));
                    labelMessage.setText(message);
                }
            });
        }
    }

    @Override
    public void updateProgress(int percent, int totalPercent, String remainTime, String message) throws CancellationException {
        //do nothing
    }

    @Override
    public void updateProgress(int percent, String recentTask, int totalPercent, String message) throws CancellationException {
        //do nothing
    }


    public String getFinishMessage() {
        String result = "";
        if (process.getKey().equals(MetaKeys.IMPORT)) {
            result = ControlsProperties.getString("String_ImportProgressFinished");
        } else if (process.getKey().equals(MetaKeys.PROJECTION)) {
            result = ControlsProperties.getString("String_ProjectionProgressFinished");
        } else if (process.getKey().equals(MetaKeys.SPATIAL_INDEX)) {
            result = ControlsProperties.getString("String_SpatialIndexProgressFinished");
        } else if (process.getKey().equals(MetaKeys.BUFFER)) {
            result = ControlsProperties.getString("String_BufferProgressFinished");
        } else if (process.getKey().equals(MetaKeys.HEAT_MAP)) {
            result = ControlsProperties.getString("String_HeatMapFinished");
        } else if (process.getKey().equals(MetaKeys.KERNEL_DENSITY)) {
            result = ControlsProperties.getString("String_KernelDensityFinished");
        } else if (process.getKey().equals(MetaKeys.OVERLAY_ANALYST)) {
            OverlayAnalystType analystType = ((MetaProcessOverlayAnalyst) process).getAnalystType();
            switch (analystType) {
                case CLIP:
                    result = ControlsProperties.getString("String_OverlayAnalyst_CLIPFinished");
                    break;
                case ERASE:
                    result = ControlsProperties.getString("String_OverlayAnalyst_ERASEFinished");
                    break;
                case IDENTITY:
                    result = ControlsProperties.getString("String_OverlayAnalyst_IDENTITYFinished");
                    break;
                case INTERSECT:
                    result = ControlsProperties.getString("String_OverlayAnalyst_INTERSECTFinished");
                    break;
                case UNION:
                    result = ControlsProperties.getString("String_OverlayAnalyst_UNIONFinished");
                    break;
                case XOR:
                    result = ControlsProperties.getString("String_OverlayAnalyst_XORFinished");
                    break;
                case UPDATE:
                    result = ControlsProperties.getString("String_OverlayAnalyst_UPDATEFinished");
                    break;
                default:
                    break;
            }
        } else if (process.getKey().equals(MetaKeys.INTERPOLATOR)) {
            InterpolationAlgorithmType type = ((MetaProcessInterpolator) process).getInterpolationAlgorithmType();
            if (type.equals(InterpolationAlgorithmType.IDW)) {
                result = ControlsProperties.getString("String_Interpolator_IDWFinished");
            } else if (type.equals(InterpolationAlgorithmType.RBF)) {
                result = ControlsProperties.getString("String_Interpolator_RBFFinished");
            } else if (type.equals(InterpolationAlgorithmType.KRIGING)) {
                result = ControlsProperties.getString("String_Interpolator_KRIGINGFinished");
            } else if (type.equals(InterpolationAlgorithmType.SimpleKRIGING)) {
                result = ControlsProperties.getString("String_Interpolator_SimpleKRIGINGFinished");
            } else if (type.equals(InterpolationAlgorithmType.UniversalKRIGING)) {
                result = ControlsProperties.getString("String_Interpolator_UniversalKRIGINGFinished");
            }
        }
        return result;
    }
}
