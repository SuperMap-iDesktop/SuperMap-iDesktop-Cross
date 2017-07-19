package com.supermap.desktop.process.demo;

import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.ui.FormBaseChild;

import javax.swing.*;
import java.awt.*;

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





	}
}
