package com.supermap.desktop.geometryoperation.control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextFieldLocked extends JTextField {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private ImageIcon lockedIcon;
	private boolean isLocked = false;
	private Insets normalInsets = new Insets(0, 0, 0, 0);
	private Insets imageInsets = new Insets(0, 20, 0, 0);

	public TextFieldLocked() {
		this.lockedIcon = new ImageIcon(getClass().getResource("/mapeditorresources/iamge_lock.png"));
		this.setMargin(this.normalInsets);
	}

	public TextFieldLocked(String text) {
		super(text);
		this.lockedIcon = new ImageIcon(getClass().getResource("/mapeditorresources/iamge_lock.png"));
		this.setMargin(this.normalInsets);
	}

	@Override
	public void paintComponent(Graphics g) {
		Insets insets = getInsets();
		super.paintComponent(g);

		if (isLocked) {
			int iconWidth = this.lockedIcon.getIconWidth();
			int iconHeight = this.lockedIcon.getIconHeight();
			this.lockedIcon.paintIcon(this, g, (insets.left - iconWidth) / 2, (this.getHeight() - iconHeight) / 2);
		}
	}

	public boolean isLocked() {
		return this.isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
		if (this.isLocked) {
			setMargin(this.imageInsets);
		} else {
			setMargin(this.normalInsets);
		}

		repaint();
	}

	public static void main(String[] args) {
		final JFrame frame = new JFrame();
		frame.setSize(500, 500);
		frame.getContentPane().setLayout(null);

		final TextFieldLocked locked = new TextFieldLocked("test");
		locked.setPreferredSize(new Dimension(100, 23));
		locked.setSize(locked.getPreferredSize());
		locked.setLocation(200, 200);

		JButton button = new JButton("lock/unlock");
		button.setLocation(300, 400);
		button.setSize(button.getPreferredSize());
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				locked.setLocked(!locked.isLocked());
			}
		});

		frame.getContentPane().add(button);
		frame.getContentPane().add(locked);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				frame.setVisible(true);
			}
		});
	}
}
