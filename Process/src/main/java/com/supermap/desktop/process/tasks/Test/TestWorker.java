package com.supermap.desktop.process.tasks.Test;

import com.supermap.desktop.process.tasks.SingleProgress;
import com.supermap.desktop.process.tasks.Worker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by highsad on 2017/6/24.
 */
public class TestWorker extends Worker<SingleProgress> {
	@Override
	protected boolean doWork() {
		try {
			for (int i = 0; i < 100; i++) {
				if (isCancelled) {
					Thread.sleep(5000);
					break;
				}

				Thread.sleep(1000);
				SingleProgress singleProgress = new SingleProgress(i, String.valueOf(i), String.valueOf((100 - i) * 1));
				update(singleProgress);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("final");
		}


		return true;
	}
}
