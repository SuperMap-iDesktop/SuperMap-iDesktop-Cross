package com.supermap.desktop.CtrlAction;


import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.Interface.IBaseItem;
import com.supermap.desktop.Interface.IForm;
import com.supermap.desktop.Interface.IFormMap;
import com.supermap.desktop.dialog.JDialogKernelDensity;
import com.supermap.desktop.implement.CtrlAction;
import com.supermap.desktop.messagebus.MessageBus;
import com.supermap.desktop.messagebus.MessageBus.MessageBusType;
import com.supermap.desktop.ui.lbs.WebHDFS;
import com.supermap.ui.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CtrlActionHeatMapRealtime extends CtrlAction {

    String topicName = "KernelDensity";
    String topicNameRespond = "KernelDensity_Respond";

    Rectangle2D rectangle;

    public CtrlActionHeatMapRealtime(IBaseItem caller, IForm formClass) {
        super(caller, formClass);
    }

    @Override
    public void run() {
        try {
            final MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
            activeMapControl.setTrackMode(TrackMode.TRACK);
            activeMapControl.setAction(Action.CREATERECTANGLE);
            activeMapControl.addMouseListener(controlMouseListener);
            activeMapControl.addTrackedListener(trackedListener);
        } catch (Exception ex) {
            Application.getActiveApplication().getOutput().output(ex);
        }
    }

    @Override
    public boolean enable() {
        Boolean enable = true;
        if (Application.getActiveApplication().getActiveForm() == null
                || !(Application.getActiveApplication().getActiveForm() instanceof IFormMap)) {
            enable = false;
        }
        return enable;
    }

    private void exitEdit() {
        MapControl activeMapControl = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
        activeMapControl.setAction(Action.SELECT2);
        activeMapControl.setTrackMode(TrackMode.EDIT);
    }

    private transient MouseListener controlMouseListener = new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            MapControl control = ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl();
            if (e.getButton() == MouseEvent.BUTTON3) {
                control.removeMouseListener(this);
                exitEdit();
            }
        }
    };

    private transient TrackedListener trackedListener = new TrackedListener() {

        @Override
        public void tracked(TrackedEvent arg0) {
            abstractTracked(arg0);
        }
    };

    private void abstractTracked(TrackedEvent arg0) {
        if (arg0.getGeometry() != null) {

            rectangle = arg0.getGeometry().getBounds().clone();
//			((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().removeTrackedListener(trackedListener);
//			exitEdit();

            CommandProducerThead thread = new CommandProducerThead();
            thread.start();
        } else {
            ((IFormMap) Application.getActiveApplication().getActiveForm()).getMapControl().addMouseListener(controlMouseListener);
        }
    }

    class CommandProducerThead extends Thread {

        @Override
        public void run() {
            try {
                doWork();
            } finally {
            }
        }
    }

    private void doWork() {
        double size = this.rectangle.getWidth() >= this.rectangle.getHeight() ? this.rectangle.getWidth() : this.rectangle.getHeight();
        String resolution = String.format("%f", size / 100);
        String radius = String.format("%f", size / 10);
        String command = JDialogKernelDensity.formatKernelDensity(
                MessageBusType.KernelDensityRealtime,
                WebHDFS.getHDFSFilePath(),
                WebHDFS.getHDFSOutputDirectry() + "kerneldensity" + System.currentTimeMillis() + ".grd",
                String.format("%f,%f,%f,%f", this.rectangle.getLeft(), this.rectangle.getBottom(), this.rectangle.getRight(), this.rectangle.getTop()),
                "10",
                ",",
                resolution,
                radius);

//		Runnable outPutRun = new Runnable() {
//		@Override
//		public void run() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置日期格式
        Application.getActiveApplication().getOutput().output(df.format(new Date()) + "  发送请求..."); //new Date()为获取当前系统时间
        Application.getActiveApplication().getOutput().output(command);
//		}
//	};
        MessageBus.producer(command);
    }

    class ResultConsumerThead extends Thread {

        @Override
        public void run() {
            try {
//				lbsResultConsumer consumer = new lbsResultConsumer();
//				consumer.doWork(topicNameRespond);
            } finally {
            }
        }
    }
}