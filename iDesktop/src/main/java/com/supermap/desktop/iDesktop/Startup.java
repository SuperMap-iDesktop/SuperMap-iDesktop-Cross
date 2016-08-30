package com.supermap.desktop.iDesktop;

import org.apache.felix.main.Main;

import javax.swing.*;

/**
 * Created by highsad on 2016/8/3.
 */
public class Startup {
	public static void main(String[] args) {
		try {
			System.out.println(1);
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			System.out.println(2);
			Main.main(args);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
