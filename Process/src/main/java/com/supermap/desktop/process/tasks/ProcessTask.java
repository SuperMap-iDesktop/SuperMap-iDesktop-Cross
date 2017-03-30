package com.supermap.desktop.process.tasks;

import com.supermap.analyst.spatialanalyst.InterpolationAlgorithmType;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.dialog.SmOptionPane;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.core.IProcess;
import com.supermap.desktop.process.events.RunningEvent;
import com.supermap.desktop.process.events.RunningListener;
import com.supermap.desktop.process.meta.MetaKeys;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessInterpolator;
import com.supermap.desktop.process.meta.metaProcessImplements.MetaProcessOverlayAnalyst;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.progress.RoundProgressBar;
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
 * <p>
 * 背景色1:rgb 251,251,251
 * 背景色2:rgb 255,255,255
 * 进度条底色：rgb: 215,215,215
 * 进度条色：rgb ：39，162，223
 * 进度条取消暂停色：rgb ：190，190，190
 */
public class ProcessTask extends JPanel implements IProcessTask, IContentModel {

    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 23;
    private static final Color DEFAULT_BACKGROUNDCOLOR = new Color(215, 215, 215);
    private static final Color DEFAULT_FOREGROUNDCOLOR = new Color(39, 162, 223);
    private static final Color CACEL_FOREGROUNDCOLOR = new Color(190, 190, 190);

    private transient SwingWorker<Boolean, Object> worker;
    private volatile String message = "";
    private volatile String remainTime = "";
    private volatile int percent = 0;
    private volatile boolean isStop = false;

    private volatile RoundProgressBar progressBar;
    private volatile JLabel labelTitle;
    private volatile JLabel labelMessage;
    private volatile JLabel labelRemaintime;
    private volatile JButton buttonRun;
    private volatile JButton buttonRemove;
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
                buttonRun.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/Image_finish_now.png"));
                buttonRun.removeActionListener(cancelListener);
                isFinished = true;
            } else {
                updateProgress(e.getProgress(), String.valueOf(e.getRemainTime()), e.getMessage());
            }
        }
    };
    private ActionListener removeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            removeItem();
        }
    };

    private void removeItem() {
        if (!isFinished()) {
            SmOptionPane optionPane = new SmOptionPane();
            if (optionPane.showConfirmDialog(MessageFormat.format(ProcessProperties.getString("String_RemoveWarning"), process.getTitle())) == JOptionPane.OK_OPTION) {
                setStop(true);
                removeTask();
            } else {
                return;
            }
        } else {
            removeTask();
        }

    }

    private void removeTask() {
        if (ProcessTask.this.getParent() instanceof TasksManagerContainer) {
            TasksManagerContainer container = (TasksManagerContainer) ProcessTask.this.getParent();
            container.removeItem(ProcessTask.this);
            removeEvents();
        }
    }

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
        progressBar = new RoundProgressBar();
        progressBar.setBackgroundColor(DEFAULT_BACKGROUNDCOLOR);
        progressBar.setForegroundColor(DEFAULT_FOREGROUNDCOLOR);
        progressBar.setDigitalColor(labelTitle.getBackground());
        progressBar.setDrawString(true);
        labelMessage = new JLabel("...");
        labelRemaintime = new JLabel("...");
        buttonRun = new JButton(ControlsResources.getIcon("/controlsresources/ToolBar/Image_run_now.png"));
        buttonRemove = new JButton(ControlsResources.getIcon("/controlsresources/ToolBar/Image_delete_now.png"));
        buttonRun.setToolTipText(CommonProperties.getString(CommonProperties.Run));
        buttonRemove.setToolTipText(CommonProperties.getString(CommonProperties.Delete));
        buttonRun.setContentAreaFilled(false);
        buttonRemove.setContentAreaFilled(false);
        ComponentUIUtilities.setName(labelTitle, "ProcessTask_labelTitle");
        ComponentUIUtilities.setName(progressBar, "ProcessTask_progressBar");
        ComponentUIUtilities.setName(labelMessage, "ProcessTask_labelMessage");
        ComponentUIUtilities.setName(labelRemaintime, "ProcessTask_labelRemaintime");
        ComponentUIUtilities.setName(buttonRun, "ProcessTask_buttonRun");
        ComponentUIUtilities.setName(buttonRemove, "ProcessTask_buttonRemove");
    }

    @Override
    public void initLayout() {
        Dimension dimension = new Dimension(18, 18);
        this.buttonRun.setPreferredSize(dimension);
        this.buttonRun.setMinimumSize(dimension);
        GroupLayout layout = new GroupLayout(this);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);
        layout.setHorizontalGroup(layout.createParallelGroup()
                .addComponent(this.labelTitle)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonRun, WIDTH, WIDTH, WIDTH)
                        .addComponent(buttonRemove, WIDTH, WIDTH, WIDTH)
                )
                .addGroup(layout.createSequentialGroup()
                        .addComponent(labelMessage)
                        .addComponent(labelRemaintime)
                )
        );
        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(this.labelTitle, WIDTH, WIDTH, WIDTH)
                .addGroup(layout.createParallelGroup()
                        .addComponent(progressBar, WIDTH, WIDTH, WIDTH)
                        .addComponent(buttonRun, WIDTH, WIDTH, WIDTH)
                        .addComponent(buttonRemove, WIDTH, WIDTH, WIDTH)
                )
                .addGroup(layout.createParallelGroup()
                        .addComponent(labelMessage, WIDTH, WIDTH, WIDTH)
                        .addComponent(labelRemaintime, WIDTH, WIDTH, WIDTH)
                )
        );
        this.setLayout(layout);
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
        this.buttonRemove.addActionListener(this.removeListener);
    }

    @Override
    public void removeEvents() {
        this.buttonRun.removeActionListener(this.cancelListener);
        this.buttonRemove.removeActionListener(this.removeListener);
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
                    buttonRun.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/Image_stop_now.png"));
                    buttonRun.setToolTipText(CommonProperties.getString(CommonProperties.Pause));
                    progressBar.setForegroundColor(CACEL_FOREGROUNDCOLOR);
                }
            });
        } else {
            doWork();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    buttonRun.setIcon(ControlsResources.getIcon("/controlsresources/ToolBar/Image_run_now.png"));
                    buttonRun.setToolTipText(CommonProperties.getString(CommonProperties.Run));
                    progressBar.setForegroundColor(DEFAULT_FOREGROUNDCOLOR);
                }
            });
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
                    progressBar.setProgress(percent);
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
