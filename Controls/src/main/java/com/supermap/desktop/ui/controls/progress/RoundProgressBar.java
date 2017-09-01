package com.supermap.desktop.ui.controls.progress;

import com.supermap.desktop.controls.ControlsProperties;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xie on 2017/3/30.
 */
public class RoundProgressBar extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * mini progress
	 */
	private int minimumProgress;

	/**
	 * max progress
	 */
	private int maximumProgress;

	/**
	 * current progress
	 */
	private int progress;

	/**
	 * background color for circle progress bar
	 */
	private Color backgroundColor;

	/**
	 * fore ground color for circle progress bar
	 */
	private Color foregroundColor;

	/**
	 * center digital color
	 */
	private Color digitalColor;
	/**
	 * add percent str or not
	 */
	private boolean isDrawString = false;

	private boolean isIndeterminate = false;
	private int percentLoc = 0;
	private boolean isReversed = false;
	private java.util.Timer updateIndeterminateTimer;
	private TimerTask updateIndeterminateTimerTask = new TimerTask() {
		@Override
		public void run() {
			if (isReversed) {
				percentLoc--;
			} else {
				percentLoc++;
			}

			if ((percentLoc == 100 && !isReversed)
					|| percentLoc == 0 && isReversed) {
				isReversed = !isReversed;
			}

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					repaint();
				}
			});
		}
	};

	public RoundProgressBar() {
		setMinimumProgress(0);
		setMaximumProgress(100);
		setProgress(0);
		setBackgroundColor(new Color(209, 206, 200));
		setForegroundColor(Color.green);
		setDigitalColor(Color.black);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D graphics2d = (Graphics2D) g;
		// set the rendering hint
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		graphics2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
		graphics2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		graphics2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		graphics2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		int x = 5;
		int y = (getHeight() / 2 - 5) > 0 ? (getHeight() / 2 - 5) : 5;
		graphics2d.setStroke(new BasicStroke(8f));
		graphics2d.setColor(backgroundColor);
//        graphics2d.drawArc(x, y, width, height, 0, 360);
		graphics2d.drawRoundRect(x, y, getWidth() - 20, 8, 8, 8);
		graphics2d.setColor(foregroundColor);

		if (isIndeterminate) {
			int progressWidth = getWidth() / 3;
			g.drawRoundRect((int) (x + (getWidth() - progressWidth - 20) * ((percentLoc + 0.0) / 100)), y, progressWidth, 8, 8, 8);
		} else if (progress > 0) {
			graphics2d.drawRoundRect(x, y, (int) ((getWidth() - 20) * ((progress + 0.0) / (getMaximumProgress() - getMinimumProgress()))), 8, 8, 8);
		}

		if (isDrawString()) {
			graphics2d.setFont(new Font(ControlsProperties.getString("String_Boldface"), Font.BOLD, 10));
			FontMetrics fontMetrics = graphics2d.getFontMetrics();
			int digitalWidth = fontMetrics.stringWidth(progress + "%");
			int digitalAscent = fontMetrics.getAscent();
			graphics2d.setColor(digitalColor);
			graphics2d.drawString(progress + "%", getWidth() / 2 - digitalWidth / 2, getHeight() / 2 + digitalAscent / 2);
		}
	}

	private void paintProgress(Graphics2D g) {
		int progressWidth = getWidth() / 4;

		int x = 5;
		int y = (getHeight() / 2 - 5) > 0 ? (getHeight() / 2 - 5) : 5;
		g.drawRoundRect((int) (x + (getWidth() - progressWidth - 20) * ((percentLoc + 0.0) / 100)), y, progressWidth, 8, 8, 8);

		if (isReversed) {
			percentLoc--;
		} else {
			percentLoc++;
		}

		if ((percentLoc == 100 && !isReversed)
				|| percentLoc == 0 && isReversed) {
			isReversed = !isReversed;
		}
	}

	/**
	 * @return
	 */
	public int getMinimumProgress() {
		return minimumProgress;
	}

	/**
	 * @param minimumProgress
	 */
	public void setMinimumProgress(int minimumProgress) {
		if (minimumProgress <= getMaximumProgress()) {
			this.minimumProgress = minimumProgress;
		}
	}

	/**
	 * @return maximumProgress
	 */
	public int getMaximumProgress() {
		return maximumProgress;
	}

	/**
	 * @param maximumProgress
	 */
	public void setMaximumProgress(int maximumProgress) {
		if (maximumProgress >= getMinimumProgress()) {
			this.maximumProgress = maximumProgress;
		}
	}

	/**
	 * @return progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * @param progress
	 */
	public void setProgress(int progress) {
		if (progress >= getMinimumProgress() && progress <= getMaximumProgress()) {
			this.progress = progress;
			this.repaint();
		}
	}

	public void updateProgressIndeterminate() {
		if (this.isIndeterminate) {
			stopUpdateProgressIndeterminate();
		}

		this.isIndeterminate = true;
		this.updateIndeterminateTimer = new Timer();
		this.updateIndeterminateTimer.schedule(this.updateIndeterminateTimerTask, 100, 20);
	}

	public void stopUpdateProgressIndeterminate() {
		if (this.updateIndeterminateTimer != null) {
			this.updateIndeterminateTimer.cancel();
			this.updateIndeterminateTimer = null;
		}
		this.percentLoc = 0;
		this.isReversed = false;
		this.isIndeterminate = false;
	}

	/**
	 * @return backgroundColor
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	/**
	 * @param backgroundColor
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.repaint();
	}

	/**
	 * @return foregroundColor
	 */
	public Color getForegroundColor() {
		return foregroundColor;
	}

	/**
	 * @param foregroundColor
	 */
	public void setForegroundColor(Color foregroundColor) {
		this.foregroundColor = foregroundColor;
		this.repaint();
	}

	/**
	 * @return digitalColor
	 */
	public Color getDigitalColor() {
		return digitalColor;
	}

	/**
	 * @param digitalColor
	 */
	public void setDigitalColor(Color digitalColor) {
		this.digitalColor = digitalColor;
		this.repaint();
	}


	public boolean isDrawString() {
		return isDrawString;
	}

	public void setDrawString(boolean drawString) {
		isDrawString = drawString;
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		final RoundProgressBar progressBar = new RoundProgressBar();
		new Thread() {
			@Override
			public void run() {
				int i = 0;
				while (i <= 1000) {
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//					progressBar.setProgress(i++);
					progressBar.repaint();
				}
			}
		}.start();
		frame.add(progressBar, BorderLayout.CENTER);
		frame.setSize(200, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
