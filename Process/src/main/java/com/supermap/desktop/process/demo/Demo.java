package com.supermap.desktop.process.demo;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.ui.FormBaseChild;
import com.supermap.desktop.utilities.CoreResources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by yuanR on 2017/7/19.
 * 用户大会展台演示
 */
public class Demo extends FormBaseChild {




	public Demo() {
		this(ProcessProperties.getString("String_Demo"), null, null);
	}

	public Demo(String title, Icon icon, Component component) {
		super(title, icon, component);

		this.setBackground(Color.WHITE);

		DemoParameterButton demoParameterButton1 = new DemoParameterButton("变形金刚",
				CoreResources.getIcon("/123/1.png"), "G:\\新建文件夹\\太极\\缠丝劲\\陈正雷新架一路83式缠丝劲练习.flv");
		DemoParameterButton demoParameterButton2 = new DemoParameterButton("钢铁侠",
				CoreResources.getIcon("/123/1.png"), "");
		DemoParameterButton demoParameterButton3 = new DemoParameterButton("蜘蛛侠",
				CoreResources.getIcon("/123/1.png"), "");
		DemoParameterButton demoParameterButton4 = new DemoParameterButton("超人归来",
				CoreResources.getIcon("/123/1.png"), "");


		this.setLayout(new FlowLayout());
		this.add(demoParameterButton1);
		this.add(demoParameterButton2);
		this.add(demoParameterButton3);
		this.add(demoParameterButton4);

		ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String filePath = ((DemoParameterButton) e.getSource()).getFilePath();
				try {
					Desktop.getDesktop().open(new File(filePath));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		};
		demoParameterButton1.addActionListener(actionListener);
		demoParameterButton2.addActionListener(actionListener);
		demoParameterButton3.addActionListener(actionListener);
		demoParameterButton4.addActionListener(actionListener);

	}
}
