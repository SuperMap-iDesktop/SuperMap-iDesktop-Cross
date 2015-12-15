package com.supermap.desktop.mapview.map.propertycontrols;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.security.Key;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.border.Border;

import org.omg.CORBA.PUBLIC_MEMBER;

import com.supermap.data.Size2D;
import com.supermap.desktop.mapview.MapViewProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.mapping.MapOverlapDisplayedOptions;

public class JPopupMenuOverlapsetting extends JPopupMenu {

	private static final long serialVersionUID = 1L;

	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

	public static final String ALLOW_POINT_OVER_LAP = "AllowPointOverlap";
	public static final String ALLOW_POINT_WITH_TEXT_DISPLAY = "AllowPointWithTextDisplay";
	public static final String ALLOW_TEXT_AND_POINT_OVER_LAP = "AllowTextAndPointOverlap";
	public static final String ALLOW_TEXT_OVER_LAP = "AllowTextOverlap";
	public static final String ALLOW_THEME_GRADUATED_SYMBOL_OVER_LAP = "AllowThemeGraduatedSymbolOverlap";
	public static final String ALLOW_THEME_GRAPH_OVER_LAP = "AllowThemeGraphOverlap";
	public static final String OVER_LAPPED_SPACE_SIZE = "OverlappedSpaceSize";

	private transient MapOverlapDisplayedOptions mapOverlapDisplayedOptions;
	/**
	 * 压盖选项面板
	 */
	private JPanel panelOverLapSetting;

	/**
	 * 压盖范围面板
	 */
	private JPanel panelOverLapRange;

	/**
	 * 点随标签显隐
	 */
	private JCheckBox checkBoxAllowPointWithTextDisplay;
	

	/**
	 * 显示被点压盖的点
	 */
	private JCheckBox checkBoxAllowPointOverlap;

	/**
	 * 显示点和文本相互压盖的对象
	 */
	private JCheckBox checkBoxAllowTextAndPointOverlap;

	/**
	 * 显示被文本压盖的文本
	 */
	private JCheckBox checkBoxAllowTextOverlap;

	/**
	 * 显示被压盖的等级符号
	 */
	private JCheckBox checkBoxAllowThemeGraduatedSymbolOverlap;

	/**
	 * 显示被压盖的统计符号
	 */
	private JCheckBox checkBoxAllowThemeGraphOverlap;

	/**
	 * 高（0.1mm）
	 */
	private JLabel labelHigh;

	/**
	 * 宽度(0.1mm)
	 */
	private JLabel labelWidth;

	/**
	 * 高度
	 */
	private JTextField textFieldHigh;

	/**
	 * 宽度
	 */
	private JTextField textFieldWidth;

	private transient ValueChangeActionListener actionListener = new ValueChangeActionListener();
	private transient ValueChangeKeyListener valueChangeKeyListener = new ValueChangeKeyListener();

	public JPopupMenuOverlapsetting() {
		super();
		initComponents();
		initListeners();
		initResources();
		initStates();
	}

	private void initComponents() {
		this.checkBoxAllowPointOverlap = new JCheckBox("PointShowWithLabel");
		this.checkBoxAllowThemeGraphOverlap = new JCheckBox("ShowCountSymbolUnder");
		this.checkBoxAllowThemeGraduatedSymbolOverlap = new JCheckBox("ShowLeverSymbolUnder");
		this.checkBoxAllowTextAndPointOverlap = new JCheckBox("PointoverLapWithText");
		this.checkBoxAllowPointWithTextDisplay = new JCheckBox("ShowPointUnderPoind");
		this.checkBoxAllowTextOverlap = new JCheckBox("ShowTextUnderText");
		this.labelHigh = new JLabel("High(0.1mm):");
		this.labelWidth = new JLabel("Width(0.1mm):");
		this.textFieldHigh = new JTextField();
		this.textFieldWidth = new JTextField();

		this.panelOverLapSetting = new JPanel();
		this.panelOverLapRange = new JPanel();

		this.add(panelOverLapSetting);
		this.add(panelOverLapRange);
		// @formatter:off
		Border titleBorderSetting = BorderFactory.createTitledBorder("OverLapSetting");            
		panelOverLapSetting.setBorder(titleBorderSetting);   
		panelOverLapSetting.setLayout(new GridBagLayout());
		panelOverLapSetting.add(checkBoxAllowPointWithTextDisplay,      new GridBagConstraintsHelper(0, 0).setInsets(0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setWeight(1, 1));
		panelOverLapSetting.add(checkBoxAllowPointOverlap,       new GridBagConstraintsHelper(0, 1).setInsets(0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setWeight(1, 1));
		panelOverLapSetting.add(checkBoxAllowTextAndPointOverlap, new GridBagConstraintsHelper(0, 2).setInsets(0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setWeight(1, 1));
		panelOverLapSetting.add(checkBoxAllowTextOverlap,        new GridBagConstraintsHelper(0, 3).setInsets(0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setWeight(1, 1));
		panelOverLapSetting.add(checkBoxAllowThemeGraduatedSymbolOverlap,     new GridBagConstraintsHelper(0, 4).setInsets(0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setWeight(1, 1));
		panelOverLapSetting.add(checkBoxAllowThemeGraphOverlap,     new GridBagConstraintsHelper(0, 5).setInsets(0).setFill(GridBagConstraints.HORIZONTAL).setAnchor(GridBagConstraints.WEST).setWeight(1, 1));
		
		Border titleBorderRange=BorderFactory.createTitledBorder("OverLapRange");            
		panelOverLapRange.setBorder(titleBorderRange);   
		panelOverLapRange.setLayout(new GridBagLayout());
		panelOverLapRange.add(labelWidth,     new GridBagConstraintsHelper(0,0).setFill(GridBagConstraints.HORIZONTAL).setInsets(0).setAnchor(GridBagConstraints.WEST).setWeight(1, 1));
		textFieldWidth.setPreferredSize(new Dimension(200,20));
		panelOverLapRange.add(textFieldWidth, new GridBagConstraintsHelper(1,0).setFill(GridBagConstraints.HORIZONTAL).setInsets(0).setAnchor(GridBagConstraints.WEST).setWeight(1, 1));
		panelOverLapRange.add(labelHigh,      new GridBagConstraintsHelper(0,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(0).setAnchor(GridBagConstraints.WEST).setWeight(1, 1));
		textFieldHigh.setPreferredSize(new Dimension(200,20));
		panelOverLapRange.add(textFieldHigh,  new GridBagConstraintsHelper(1,1).setFill(GridBagConstraints.HORIZONTAL).setInsets(0).setAnchor(GridBagConstraints.WEST).setWeight(1, 1));
		// @formatter:on

	}

	private void initListeners() {
		this.checkBoxAllowPointOverlap.addActionListener(actionListener);
		this.checkBoxAllowPointWithTextDisplay.addActionListener(actionListener);
		this.checkBoxAllowTextAndPointOverlap.addActionListener(actionListener);
		this.checkBoxAllowTextOverlap.addActionListener(actionListener);
		this.checkBoxAllowThemeGraduatedSymbolOverlap.addActionListener(actionListener);
		this.checkBoxAllowThemeGraphOverlap.addActionListener(actionListener);
		this.textFieldHigh.addKeyListener(valueChangeKeyListener);
		this.textFieldWidth.addKeyListener(valueChangeKeyListener);
	}

	private void initResources() {
		Border titleBorderSetting = BorderFactory.createTitledBorder(MapViewProperties.getString("String_OverLapSetting"));            
		panelOverLapSetting.setBorder(titleBorderSetting);   
		Border titleBorderRange=BorderFactory.createTitledBorder(MapViewProperties.getString("String_OverLapRange"));            
		panelOverLapRange.setBorder(titleBorderRange);  
		
		checkBoxAllowPointWithTextDisplay.setText(MapViewProperties.getString("String_AllowPointWithTextDisplay"));
		checkBoxAllowPointOverlap.setText(MapViewProperties.getString("String_PointWithTextDisplay"));
		checkBoxAllowTextAndPointOverlap.setText(MapViewProperties.getString("String_TextAndPointOverlap"));
		checkBoxAllowTextOverlap.setText(MapViewProperties.getString("String_TextOverlap"));
		checkBoxAllowThemeGraduatedSymbolOverlap.setText(MapViewProperties.getString("String_ThemeGraduatedSymbolOverlap"));
		checkBoxAllowThemeGraphOverlap.setText(MapViewProperties.getString("String_ThemeGraphOverlap"));
		labelWidth.setText(MapViewProperties.getString("Label_High"));
		labelHigh.setText(MapViewProperties.getString("Label_width"));
	}

	/**
	 * 根据属性初始化控件信息
	 */
	private void initStates() {
		if (mapOverlapDisplayedOptions != null) {
			this.checkBoxAllowPointOverlap.setSelected(mapOverlapDisplayedOptions.getAllowPointOverlap());
			this.checkBoxAllowPointWithTextDisplay.setSelected(mapOverlapDisplayedOptions.getAllowPointWithTextDisplay());
			this.checkBoxAllowTextAndPointOverlap.setSelected(mapOverlapDisplayedOptions.getAllowTextAndPointOverlap());
			this.checkBoxAllowTextOverlap.setSelected(mapOverlapDisplayedOptions.getAllowTextOverlap());
			this.checkBoxAllowThemeGraduatedSymbolOverlap.setSelected(mapOverlapDisplayedOptions.getAllowThemeGraduatedSymbolOverlap());
			this.checkBoxAllowThemeGraphOverlap.setSelected(mapOverlapDisplayedOptions.getAllowThemeGraphOverlap());
			this.textFieldHigh.setText(String.valueOf(mapOverlapDisplayedOptions.getOverlappedSpaceSize().getHeight()));
			this.textFieldWidth.setText(String.valueOf(mapOverlapDisplayedOptions.getOverlappedSpaceSize().getWidth()));
		}
	}

	public boolean isPropertyChanged(MapOverlapDisplayedOptions mapOverlapDisplayedOptions) {
		return mapOverlapDisplayedOptions.getAllowPointOverlap() == this.mapOverlapDisplayedOptions.getAllowPointOverlap()
				|| mapOverlapDisplayedOptions.getAllowPointWithTextDisplay() == this.mapOverlapDisplayedOptions.getAllowPointWithTextDisplay()
				|| mapOverlapDisplayedOptions.getAllowTextAndPointOverlap() == this.mapOverlapDisplayedOptions.getAllowTextAndPointOverlap()
				|| mapOverlapDisplayedOptions.getAllowTextOverlap() == this.mapOverlapDisplayedOptions.getAllowTextOverlap()
				|| mapOverlapDisplayedOptions.getAllowThemeGraduatedSymbolOverlap() == this.mapOverlapDisplayedOptions.getAllowThemeGraduatedSymbolOverlap()
				|| mapOverlapDisplayedOptions.getAllowThemeGraphOverlap() == this.mapOverlapDisplayedOptions.getAllowThemeGraphOverlap()
				|| mapOverlapDisplayedOptions.getOverlappedSpaceSize().equals(this.mapOverlapDisplayedOptions.getOverlappedSpaceSize());

	}

	public MapOverlapDisplayedOptions getMapOverlapDisplayedOptions() {
		return this.mapOverlapDisplayedOptions;
	}

	public void setMapOverlapDisplayedOptions(MapOverlapDisplayedOptions mapOverlapDisplayedOptions) {
		this.mapOverlapDisplayedOptions = mapOverlapDisplayedOptions;
		initStates();
	}

	public class ValueChangeActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == checkBoxAllowPointOverlap) {
				mapOverlapDisplayedOptions.setAllowPointOverlap(checkBoxAllowPointOverlap.isSelected());
				changeSupport.firePropertyChange(ALLOW_POINT_OVER_LAP, null, checkBoxAllowPointOverlap.isSelected());
			} else if (e.getSource() == checkBoxAllowPointWithTextDisplay) {
				mapOverlapDisplayedOptions.setAllowPointWithTextDisplay(checkBoxAllowPointWithTextDisplay.isSelected());
				changeSupport.firePropertyChange(ALLOW_POINT_WITH_TEXT_DISPLAY, null, checkBoxAllowPointWithTextDisplay.isSelected());
			} else if (e.getSource() == checkBoxAllowTextAndPointOverlap) {
				mapOverlapDisplayedOptions.setAllowTextAndPointOverlap(checkBoxAllowTextAndPointOverlap.isSelected());
				changeSupport.firePropertyChange(ALLOW_TEXT_AND_POINT_OVER_LAP, null, checkBoxAllowTextAndPointOverlap.isSelected());
			} else if (e.getSource() == checkBoxAllowTextOverlap) {
				mapOverlapDisplayedOptions.setAllowTextOverlap(checkBoxAllowTextOverlap.isSelected());
				changeSupport.firePropertyChange(ALLOW_TEXT_OVER_LAP, null, checkBoxAllowTextOverlap.isSelected());
			} else if (e.getSource() == checkBoxAllowThemeGraduatedSymbolOverlap) {
				mapOverlapDisplayedOptions.setAllowThemeGraduatedSymbolOverlap(checkBoxAllowThemeGraduatedSymbolOverlap.isSelected());
				changeSupport.firePropertyChange(ALLOW_THEME_GRADUATED_SYMBOL_OVER_LAP, null, checkBoxAllowThemeGraduatedSymbolOverlap.isSelected());
			} else if (e.getSource() == checkBoxAllowThemeGraphOverlap) {
				mapOverlapDisplayedOptions.setAllowThemeGraphOverlap(checkBoxAllowThemeGraphOverlap.isSelected());
				changeSupport.firePropertyChange(ALLOW_THEME_GRAPH_OVER_LAP, null, checkBoxAllowThemeGraphOverlap.isSelected());
			}
		}
	}

	public class ValueChangeKeyListener implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {
			char keyChar = e.getKeyChar();
			if (keyChar == KeyEvent.VK_ESCAPE || keyChar == KeyEvent.VK_ENTER) {
				JPopupMenuOverlapsetting.this.setVisible(false);
			} else if ((keyChar >= KeyEvent.VK_0 && keyChar <=KeyEvent.VK_9) || keyChar == KeyEvent.VK_MINUS) {
				// 有用 勿删
			} else if (keyChar == KeyEvent.VK_PERIOD) {
				String value = ((JTextField) e.getComponent()).getText();
				if (value.indexOf(".") != -1) {
					e.consume();
				}
			} else {
				e.consume();
			}

		}

		@Override
		public void keyPressed(KeyEvent e) {
			// do nothing
		}

		@Override
		public void keyReleased(KeyEvent e) {
			Component component = e.getComponent();
			double value = 0;
			if (component == textFieldHigh) {
				try {
					value = Double.valueOf(textFieldHigh.getText());
				} catch (Exception e1) {
					e.consume();
				}
				Size2D size2d = new Size2D(mapOverlapDisplayedOptions.getOverlappedSpaceSize());
				size2d.setHeight(value);
				mapOverlapDisplayedOptions.setOverlappedSpaceSize(size2d);
				// 要是后面有需要的话，这里可以改的详细点说明是高度变，这里没需求先不改
				changeSupport.firePropertyChange(JPopupMenuOverlapsetting.OVER_LAPPED_SPACE_SIZE, null, value);
			} else if (component == textFieldWidth) {
				try {
					value = Double.valueOf(textFieldWidth.getText());
				} catch (Exception e1) {
					e.consume();
				}
				Size2D size2d = new Size2D(mapOverlapDisplayedOptions.getOverlappedSpaceSize());
				size2d.setWidth(value);
				mapOverlapDisplayedOptions.setOverlappedSpaceSize(size2d);
				changeSupport.firePropertyChange(JPopupMenuOverlapsetting.OVER_LAPPED_SPACE_SIZE, null, value);
			}
		}

	}

	public void removePropertyChangerListeners(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	public void addPropertyChangerListeners(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}
}
