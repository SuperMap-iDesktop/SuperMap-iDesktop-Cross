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
import com.supermap.desktop.ui.controls.TextFields.NumTextFieldLegit;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Created By Chens on 2017/8/18 0018
 */
@ParameterPanelDescribe(parameterPanelType = ParameterType.HISTOGRAM)
public class ParameterHistogramPanel extends SwingPanel implements IParameterPanel {
	private ParameterHistogram parameterHistogram;
	private JCheckBox checkBox;
	private JLabel label;
	private NumTextFieldLegit numCount;
	private HistogramPanel histogramPanel;

	public ParameterHistogramPanel(final IParameter parameterHistogram) {
		super(parameterHistogram);
		this.parameterHistogram = (ParameterHistogram) parameterHistogram;
		initComponent();
		this.parameterHistogram.setGroupCount(Integer.parseInt(numCount.getText()));
		this.parameterHistogram.setCreate(checkBox.isSelected());

		panel.setLayout(new GridBagLayout());
		panel.add(checkBox, new GridBagConstraintsHelper(0, 0, 2, 1).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(0,0,5,0));
		panel.add(label, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(0, 1).setFill(GridBagConstraints.NONE).setAnchor(GridBagConstraints.WEST).setInsets(0,0,5,25));
		panel.add(numCount, new GridBagConstraintsHelper(1, 1, 1, 1).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setInsets(0,20,5,0));
		panel.add(histogramPanel, new GridBagConstraintsHelper(0, 2, 2, 1).setWeight(1, 1).setFill(GridBagConstraints.BOTH).setInsets(0));
		initListener();
	}

	private void initComponent() {
		histogramPanel =new HistogramPanel();
		checkBox = new JCheckBox();
		label = new JLabel();
		numCount = new NumTextFieldLegit();
		checkBox.setText(ProcessProperties.getString("String_CheckBox_CreateHistogram"));
		label.setText(ProcessProperties.getString("String_Label_GroupCount"));
		numCount.setText("5");
		numCount.setMinValue(1);
		numCount.setBit(-1);
		label.setVisible(false);
		numCount.setVisible(false);
		parameterHistogram.setCreate(false);
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
		checkBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (checkBox.isSelected()) {
					label.setVisible(true);
					numCount.setVisible(true);
					parameterHistogram.setCreate(true);
				} else {
					label.setVisible(false);
					numCount.setVisible(false);
					parameterHistogram.setCreate(false);
				}
			}
		});
		numCount.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				parameterHistogram.setGroupCount(Integer.parseInt(numCount.getBackUpValue()));
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				parameterHistogram.setGroupCount(Integer.parseInt(numCount.getBackUpValue()));
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				parameterHistogram.setGroupCount(Integer.parseInt(numCount.getBackUpValue()));
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

			g.setFont(new Font(null, Font.PLAIN, 15));
			g.setColor(Color.BLACK);
			g.drawLine(30, 0, 30, height - 15); // 画Y坐标
			g.drawLine(30, height - 15, width-20, height - 15);// 画X坐标
			g.drawLine(30,0,20,10);
			g.drawLine(30,0,40,10);
			g.drawLine(width-10,height-15,width-20,height-25);
			g.drawLine(width-10,height-15,width-20,height-5);
			g.drawString(ProcessProperties.getString("String_Histogram_Frequency"),0,20);
			g.drawString(ProcessProperties.getString("String_Histogram_Interval"),width-30,height-25);

			g.setFont(new Font(null, Font.PLAIN, 10));
			double hInterval = (width - 40) / groupCount;
			g.drawString("0", 10, height);
			double maxFrequency = 0;
			for (int i = 0; i < infos.length; i++) {
				g.drawString((double)((int) (infos[i].getRangeMaxValue() * 100)) / 100 + "", (int) hInterval * (i + 1), height);
				maxFrequency = maxFrequency > gridHistogram.getFrequencies()[i] ? maxFrequency : gridHistogram.getFrequencies()[i];
			}
			g.drawString(maxFrequency+"",10,0);
			for (int i = 0; i < infos.length; i++) {
				int heightRange = (int) ((height - 40) * gridHistogram.getFrequencies()[i] / maxFrequency);
				g.setColor(Color.YELLOW);
				g.fillRect(30+(int) hInterval * i,height-15-heightRange, (int) hInterval,heightRange);
				g.setColor(Color.BLACK);
				g.drawRect(30+(int) hInterval * i,height-15-heightRange, (int) hInterval,heightRange);
				g.drawString(gridHistogram.getFrequencies()[i] + "", 30 + (int) hInterval * i, height - 18 - heightRange);
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(panel.getWidth(), 300);
		}
	}
}


