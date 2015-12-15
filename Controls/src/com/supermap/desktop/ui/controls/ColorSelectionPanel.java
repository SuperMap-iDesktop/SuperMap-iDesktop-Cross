package com.supermap.desktop.ui.controls;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;

/**
 * 颜色选择面板
 * 
 * @author zhaosy
 * 
 */
public class ColorSelectionPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel jLabelStandard;
	private JLabel jLabelTheme;

	private JPanel jPanelFirstTheme;
	private JPanel jPanelTheme;
	private JPanel jPanelStandardColor;

	private JButton jButtonHistoryColor;

	private transient ControlButton jButtonScreen;
	private transient ControlButton jButtonOther;

	private static String fileTargetPath = System.getProperty("java.io.tmpdir");

	private final static String FIRST_THEME_COLOR = "FirstThemeColor.xml";

	private final static String THEME_COLOR = "ThemeColor.xml";

	private final static String STANDARD_COLOR = "StandColor.xml";

	private transient Border borderHistoryColor;

	/**
	 * 构造函数，颜色选择面板
	 */
	public ColorSelectionPanel() {
		super();
		if (fileTargetPath.charAt(fileTargetPath.length() - 1) != File.separatorChar) {
			fileTargetPath += Character.toString(File.separatorChar);
		}
		setSize(170, 205);
		setLayout(null);
		setBackground(new Color(226, 231, 238));
		add(getButtonScreen());
		add(getOtherColorButton());
		add(getThemePanel());
		add(getThemeLabel());
		add(getFirstThemePanel());
		add(getStandardLabel());
		add(getStandardColorPanel());
		setVisible(true);

	}

	public void selectColor(Color newColor) {
		// modify by xuzw 2010-09-20 UGOSPII-1506
		// 为了每次点击颜色按钮都触发事件，所以传入新颜色和null
		firePropertyChange("m_selectionColor", null, newColor);
	}

	/**
	 * 提取颜色按钮
	 * 
	 * @return
	 */
	private JButton getButtonScreen() {
		if (jButtonScreen == null) {
			jButtonScreen = new ControlButton(ControlsProperties.getString("String_GetColor"), InternalImageIconFactory.CURSOR_ICON);
			jButtonScreen.setBounds(0, 0, 170, 24);
			jButtonScreen.setBackground(new Color(226, 231, 238));
			jButtonScreen.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JDialog dialog = new ColorPick();
					dialog.addPropertyChangeListener("selectedColor", new PropertyChangeListener() {
						@Override
						public void propertyChange(PropertyChangeEvent evt) {
							Color color = (Color) evt.getNewValue();
							selectColor(color);
						}
					});

					dialog.setVisible(true);
				}
			});
		}
		return jButtonScreen;
	}

	/**
	 * @return m_ThemeLabel
	 */
	private JLabel getThemeLabel() {
		if (jLabelTheme == null) {
			jLabelTheme = new JLabel();
			jLabelTheme.setText(ControlsProperties.getString("String_ThemeColor"));
			jLabelTheme.setBackground(new Color(226, 231, 238));
			jLabelTheme.setBounds(5, 26, 66, 22);
		}
		return jLabelTheme;
	}

	/**
	 * 主题色第一行
	 * 
	 * @return m_firstThemePanel
	 */
	private JPanel getFirstThemePanel() {
		if (jPanelFirstTheme == null) {
			jPanelFirstTheme = new JPanel();
			jPanelFirstTheme = new ColorArrayPanel(FIRST_THEME_COLOR);
			jPanelFirstTheme.setBackground(new Color(255, 255, 255));
			jPanelFirstTheme.setBounds(0, 48, 170, 20);
			jPanelFirstTheme.addPropertyChangeListener("selectedColor", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Color color = (Color) evt.getNewValue();
					selectColor(color);
				}
			});
		}
		return jPanelFirstTheme;
	}

	/**
	 * 主题色
	 */
	private JPanel getThemePanel() {
		if (jPanelTheme == null) {
			jPanelTheme = new JPanel();
			jPanelTheme = new ColorArrayPanel(THEME_COLOR);
			jPanelTheme.setBackground(new Color(255, 255, 255));
			jPanelTheme.setBounds(0, 69, 170, 68);
			jPanelTheme.addPropertyChangeListener("selectedColor", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Color color = (Color) evt.getNewValue();
					selectColor(color);
				}
			});
		}
		return jPanelTheme;
	}

	/**
	 * @return m_customLabel
	 */
	private JLabel getStandardLabel() {
		if (jLabelStandard == null) {
			jLabelStandard = new JLabel();
			jLabelStandard.setText(ControlsProperties.getString("String_StandardColor"));
			jLabelStandard.setBackground(new Color(226, 231, 238));
			jLabelStandard.setBounds(5, 136, 55, 22);
		}
		return jLabelStandard;
	}

	/**
	 * 自定义颜色面板
	 * 
	 * @return
	 */
	private JPanel getStandardColorPanel() {
		if (jPanelStandardColor == null) {
			jPanelStandardColor = new JPanel();
			jPanelStandardColor = new ColorArrayPanel(STANDARD_COLOR);
			jPanelStandardColor.setBackground(new Color(255, 255, 255));
			jPanelStandardColor.setBounds(0, 157, 170, 20);
			jPanelStandardColor.addPropertyChangeListener("selectedColor", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					Color color = (Color) evt.getNewValue();
					selectColor(color);
				}
			});
		}
		return jPanelStandardColor;
	}

	/**
	 * 其他颜色按钮
	 * 
	 * @return
	 */
	private JButton getOtherColorButton() {
		if (jButtonOther == null) {
			jButtonOther = new ControlButton(ControlsProperties.getString("String_OtherColor"), InternalImageIconFactory.OTHER_COLOR);
			jButtonOther.setBackground(new Color(226, 231, 238));
			jButtonOther.setBounds(0, 178, 170, 27);
			jButtonOther.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Color color = JColorChooser.showDialog(null, ControlsProperties.getString("String_ChooseColor"), null);
					if (color != null) {
						selectColor(color);
					}
				}
			});
		}
		return jButtonOther;
	}

	protected class ColorArrayPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private transient Node m_nodeRed;
		private transient Node m_nodeGreen;
		private transient Node m_nodeBlue;
		private transient Node m_nodeText;
		private Color m_selectedColor;
		private int m_depth;
		private int m_ColorInt;
		private HashMap<Integer, Color> m_hashMapStand;
		private ArrayList<String> m_colorName;

		public ColorArrayPanel(String name) {
			m_hashMapStand = new HashMap<Integer, Color>();
			parserXml(name);
			GridBagLayout gridbagLayout = new GridBagLayout();
			GridBagConstraints constr = new GridBagConstraints();
			setLayout(gridbagLayout);
			ActionListener color_listener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					selectColor(((JButton) evt.getSource()).getBackground());
				}
			};

			constr.gridheight = 1;
			constr.gridwidth = 1;
			constr.anchor = GridBagConstraints.WEST;
			constr.fill = GridBagConstraints.WEST;
			constr.weightx = 50;
			constr.weighty = 50;
			int x = 0;
			int y = 0;
			// 渐变色填充按钮
			for (int j = 0; j < m_hashMapStand.size(); j++) {
				final JButton button = new JButton("");
				// 依据步进决定是横排还是竖排
				if (m_depth > 1) {
					y++;
					button.setBorder(null);
				} else {
					x++;
				}
				// 当行数大于预定值即新起一列
				if (y > m_depth) {
					y = y % m_depth;
					x++;
				}
				// 鼠标划过时动态添加和删除边框，按下时设置高亮边框，同时删除历史按钮边框
				final Border border = button.getBorder();
				button.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseEntered(MouseEvent e) {
						button.setBorder(BorderFactory.createLineBorder(new Color(250, 192, 43), 2));
					}

					@Override
					public void mouseExited(MouseEvent e) {
						if (!button.equals(jButtonHistoryColor)) {
							button.setBorder(border);
						}
					}

					@Override
					public void mouseReleased(MouseEvent e) {
						if (jButtonHistoryColor != null) {
							if (borderHistoryColor != null)
								jButtonHistoryColor.setBorder(BorderFactory.createLineBorder(new Color(122, 138, 153), 1));
							else
								jButtonHistoryColor.setBorder(null);

						}
						jButtonHistoryColor = button;
						borderHistoryColor = border;
					}
				});
				constr.gridy = y;
				constr.gridx = x;
				button.setPreferredSize(new Dimension(13, 13));
				button.setContentAreaFilled(false);
				button.setOpaque(true);
				button.setBackground(m_hashMapStand.get(j));
				gridbagLayout.setConstraints(button, constr);
				add(button);
				button.addActionListener(color_listener);
			}
		}

		public void selectColor(Color newColor) {
			Color oldColor = m_selectedColor;
			m_selectedColor = newColor;
			firePropertyChange("selectedColor", oldColor, newColor);
			m_selectedColor = null;
		}

		/**
		 * 解读XML文件
		 */
		private void parserXml(String name) {
			FileOutputStream fileOutputStream = null;
			BufferedReader bufferReader = null;
			try {

				String xmlFile = "";
				if (UIEnvironment.getResourcePath() == null) {
					InputStream inputStream = this.getClass().getResourceAsStream("/com/supermap/desktop/ui/controls/xml/colorpicksXML/" + name);
					Reader reader = new InputStreamReader(inputStream);
					bufferReader = new BufferedReader(reader);
					StringBuilder  stringBuilder = new StringBuilder ("");
					String string = "";
					while ((string = bufferReader.readLine()) != null) {
						stringBuilder = stringBuilder.append(string);
					}
					xmlFile = fileTargetPath + name;
					fileOutputStream = new FileOutputStream(xmlFile);
					fileOutputStream.write(stringBuilder.toString().getBytes());
				} else {
					xmlFile = UIEnvironment.getResourcePath() + "/xml/colorpicksXML/" + name;
				}
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document document = db.parse(xmlFile);
				NodeList employees = document.getChildNodes();
				m_ColorInt = -1;
				m_colorName = new ArrayList<String>();
				for (int i = 0; i < employees.getLength(); i++) {
					Node employee = employees.item(i);
					// 获取步进
					if (employee.getAttributes() != null) {
						m_depth = Integer.valueOf(employee.getAttributes().getNamedItem("depth").getNodeValue());
					}
					NodeList employeeInfo = employee.getChildNodes();
					for (int j = 0; j < employeeInfo.getLength(); j++) {
						Node node = employeeInfo.item(j);
						NamedNodeMap map = node.getAttributes();
						if (map != null) {
							m_ColorInt += 1;
							m_nodeRed = map.getNamedItem("r");
							m_nodeGreen = map.getNamedItem("g");
							m_nodeBlue = map.getNamedItem("b");
							int red = Integer.parseInt(m_nodeRed.getNodeValue());
							int green = Integer.parseInt(m_nodeGreen.getNodeValue());
							int blue = Integer.parseInt(m_nodeBlue.getNodeValue());
							m_nodeText = map.getNamedItem("text");
							String text = m_nodeText.getNodeValue();
							m_colorName.add(text);
							Color color = new Color(red, green, blue);
							m_hashMapStand.put(m_ColorInt, color);
						}
					}
				}
				File file = new File(fileTargetPath + name + ".xml");
				file.deleteOnExit();
			} catch (FileNotFoundException ex) {
				Application.getActiveApplication().getOutput().output(ex);
			} catch (ParserConfigurationException ex) {
				Application.getActiveApplication().getOutput().output(ex);
			} catch (SAXException ex) {
				Application.getActiveApplication().getOutput().output(ex);
			} catch (IOException ex) {
				Application.getActiveApplication().getOutput().output(ex);
			} finally {
				try {
					if (bufferReader != null) {
						bufferReader.close();
					}
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
				} catch (IOException ex) {
					Application.getActiveApplication().getOutput().output(ex);
				}
			}

		}
	}

}

/**
 * 颜色选取类型，该类型负责提取当前显示屏幕的颜色
 * 
 * @author xuzw
 * 
 */
class ColorPick extends JDialog {
	private static final long serialVersionUID = 1L;
	private JPanel m_imagePanel;
	private Dimension m_screenSize;

	private transient Image m_backgroundImage = null;
	private transient Robot m_robot;
	private Color m_selectionColor;

	public ColorPick() {
		m_screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if(e.getButton() == MouseEvent.BUTTON1) {
					selectColor(m_robot.getPixelColor(e.getX(), e.getY()));
				}
				setVisible(false);
				setCursor(Cursor.getDefaultCursor());
				dispose();
			}
		});

		this.setUndecorated(true);
		this.setSize(m_screenSize.width, m_screenSize.height);
		this.setModal(true);
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		final Cursor m_cursor = toolkit.createCustomCursor(InternalImageIconFactory.COLOR_PICK.getImage(), new Point(0, 16), "");
		m_imagePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(m_backgroundImage, 0, 0, null);
				this.setCursor(m_cursor);
			}
		};
		m_imagePanel.setPreferredSize(m_screenSize);
		m_imagePanel.setLayout(null);
		this.getContentPane().add(m_imagePanel);
	}

	@Override
	public void show() {
		try {
			Rectangle rect = new Rectangle(0, 0, (int) m_screenSize.getWidth(), (int) m_screenSize.getHeight());
			this.m_robot = new Robot();
			m_backgroundImage = m_robot.createScreenCapture(rect);
			super.show();
		} catch (AWTException ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}

	// 颜色监听器
	public void selectColor(Color newColor) {
		Color oldColor = m_selectionColor;
		m_selectionColor = newColor;
		firePropertyChange("selectedColor", oldColor, newColor);
	}
}

class FullScreen extends JDialog {
	private static final long serialVersionUID = 1L;
	private JPanel m_imagePanel;
	private Dimension m_screenSize;
	private transient Image m_backgroundImage = null;
	private transient Robot m_robot;
	private JDialog m_parent;

	public FullScreen(JDialog parent) {
		m_parent = parent;
		m_screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) {
				int x = evt.getX();
				int y = evt.getY();

				int leftX = m_parent.getLocationOnScreen().x;
				int rightX = m_parent.getLocationOnScreen().x + m_parent.getWidth();
				int topY = m_parent.getLocationOnScreen().y;
				int bottomY = m_parent.getLocationOnScreen().y + m_parent.getHeight();

				if (x > leftX && x < rightX && y > topY && y < bottomY) {
					dispose();
				} else {
					dispose();
					m_parent.dispose();
				}
			}
		});

		this.setUndecorated(true);
		this.setSize(m_screenSize.width, m_screenSize.height);
		this.setModal(true);
		m_imagePanel = new JPanel() {
			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(m_backgroundImage, 0, 0, null);
			}
		};
		m_imagePanel.setPreferredSize(m_screenSize);
		m_imagePanel.setLayout(null);
		this.getContentPane().add(m_imagePanel);
	}

	@Override
	public void show() {
		try {
			Rectangle rect = new Rectangle(0, 0, (int) m_screenSize.getWidth(), (int) m_screenSize.getHeight());
			this.m_robot = new Robot();
			m_backgroundImage = m_robot.createScreenCapture(rect);
			super.show();
		} catch (AWTException ex) {
			Application.getActiveApplication().getOutput().output(ex);
		}
	}
}