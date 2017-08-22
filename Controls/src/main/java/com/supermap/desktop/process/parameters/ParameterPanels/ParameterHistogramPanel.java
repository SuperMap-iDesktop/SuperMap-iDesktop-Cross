package com.supermap.desktop.process.parameters.ParameterPanels;

import com.supermap.analyst.spatialanalyst.GridHistogram;
import com.supermap.analyst.spatialanalyst.HistogramSegmentInfo;
import com.supermap.desktop.process.ProcessProperties;
import com.supermap.desktop.process.enums.ParameterType;
import com.supermap.desktop.process.parameter.interfaces.AbstractParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameter;
import com.supermap.desktop.process.parameter.interfaces.IParameterPanel;
import com.supermap.desktop.process.parameter.interfaces.ParameterPanelDescribe;
import com.supermap.desktop.process.parameter.ipls.ParameterHistogram;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created By Chens on 2017/8/18 0018
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.HISTOGRAM)
public class ParameterHistogramPanel extends SwingPanel implements IParameterPanel {
	private ParameterHistogram parameterHistogram;
	private HistogramPanel histogramPanel;

	public ParameterHistogramPanel(final IParameter parameterHistogram) {
		super(parameterHistogram);
		this.parameterHistogram = (ParameterHistogram) parameterHistogram;
		histogramPanel =new HistogramPanel();
		panel.setLayout(new GridBagLayout());
		panel.add(histogramPanel, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH));
		initListener();
	}

	private void initListener() {
		parameter.addPropertyListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(AbstractParameter.PROPERTY_VALE)) {
					histogramPanel.setGridHistogram(evt.getNewValue() == null ? null : (GridHistogram) evt.getNewValue());
				}
			}
		});
	}

	/**
	 * 直方图面板
	 */
	private class HistogramPanel extends JPanel{
		private GridHistogram gridHistogram;

		public void setGridHistogram(GridHistogram gridHistogram) {
			this.gridHistogram = gridHistogram;
			repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			if (null == gridHistogram) {
				return;
			}
			super.paintComponent(g);
			int width = (int) getPreferredSize().getWidth();
			int height = (int) getPreferredSize().getHeight();
			int groupCount = gridHistogram.getGroupCount();
			HistogramSegmentInfo[] infos = gridHistogram.getSegmentInfos();
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, width, height);

			g.setFont(new Font(null, Font.PLAIN, 10));
			g.setColor(Color.BLACK);
			g.drawLine(10, 0, 10, height - 15); // 画Y坐标
			g.drawLine(10, height - 15, width, height - 15);// 画X坐标
			g.drawLine(10,0,0,10);
			g.drawLine(10,0,20,10);
			g.drawLine(width,height-15,width-10,height-25);
			g.drawLine(width,height-15,width-10,height-5);
			g.drawString(ProcessProperties.getString("String_Histogram_Frequency"),20,10);
			g.drawString(ProcessProperties.getString("String_Histogram_Interval"),width-10,height-25);

			double hInterval = (width - 10) / groupCount;
			g.drawString("0", 10, height);
			double maxFrequency = 0;
			for (int i = 0; i < infos.length; i++) {
				g.drawString(infos[i].getRangeMaxValue() + "", 10 + (int) hInterval * (i + 1), height);
				maxFrequency = maxFrequency > gridHistogram.getFrequencies()[i] ? maxFrequency : gridHistogram.getFrequencies()[i];
			}
			g.drawString(maxFrequency+"",10,0);
			for (int i = 0; i < infos.length; i++) {
				int heightRange = (int) ((height - 40) * gridHistogram.getFrequencies()[i] / maxFrequency);
				g.setColor(Color.YELLOW);
				g.fillRect(10+(int) hInterval * i,height-15-heightRange, (int) hInterval,heightRange);
				g.setColor(Color.BLACK);
				g.drawRect(10+(int) hInterval * i,height-15-heightRange, (int) hInterval,heightRange);
				g.drawString(gridHistogram.getFrequencies()[i] + "", 10 + (int) hInterval * i, height - 18 - heightRange);
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(panel.getWidth(), 300);
		}
	}
}


