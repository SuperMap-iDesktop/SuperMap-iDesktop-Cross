package com.supermap.desktop.ui.controls;

import com.supermap.desktop.Application;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentUIUtilities;
import com.supermap.desktop.controls.utilities.ControlsResources;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * 文件选择组合控件
 *
 * @author hanyz
 */
public class JFileChooserControl extends JComponent {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private JTextField textEditor;
	private JButton button;
	private JFileChooser fileChooser;
	private boolean isUpdatedByChooser = false;
	private ArrayList<FileChooserPathChangedListener> fileChangedListeners = new ArrayList<>();
	private ArrayList<FileChooserPathCommittedListener> fileCommittedListeners = new ArrayList<>();
	private ArrayList<FileChooserButtonListener> buttonListeners = new ArrayList<>();


	public JFileChooserControl() {
		this(null);
	}

	// 初始化带有默认文件路径的文件选择控件
	public JFileChooserControl(String filePath) {
		initComponent();
		setComponentName();
		setPath(filePath);
		registerListener();
	}

	public boolean addFileChangedListener(FileChooserPathChangedListener listener) {
		if (listener != null) {
			return fileChangedListeners.add(listener);
		}
		return false;
	}

	public boolean removePathChangedListener(FileChooserPathChangedListener listener) {
		return fileChangedListeners.remove(listener);
	}

	public boolean addFileCommiteddListener(FileChooserPathCommittedListener listener) {
		if (listener != null) {
			return fileCommittedListeners.add(listener);
		}
		return false;
	}

	public boolean removeFileCommiteddListener(FileChooserPathCommittedListener listener) {
		return fileCommittedListeners.remove(listener);
	}

	public boolean addButtonListener(FileChooserButtonListener listener) {
		if (listener != null) {
			return buttonListeners.add(listener);
		}
		return false;
	}

	public boolean removeButtonListener(FileChooserButtonListener listener) {
		return buttonListeners.remove(listener);
	}

	private void firePathChanged() {
		for (FileChooserPathChangedListener listener : fileChangedListeners) {
			listener.pathChanged();
		}
	}

	private void firePathCommitted() {
		for (FileChooserPathCommittedListener listener : fileCommittedListeners) {
			listener.pathCommitted();
		}
	}

	private void fireButtonClicked() {
		for (FileChooserButtonListener listener : buttonListeners) {
			listener.buttonClicked();
		}
	}


	public void setPath(String filePath) {
		this.textEditor.setText(filePath);
	}

	public String getPath() {
		String text = this.textEditor.getText();
		if (!StringUtilities.isNullOrEmpty(text)) {
			File file = new File(text);
			file.mkdirs();
		}
		return text;
	}

	public void setFileChooser(JFileChooser chooser) {
		this.fileChooser = chooser;
	}

	/*
	public boolean isPathLegal(boolean isEmptyAllowed){
		String path = this.textEditor.getText();
		if(StringUtilities.isNullOrEmpty(path)){
			return isEmptyAllowed;
		}
		return false;
	}
	*/

	@Override
	public void setEnabled(boolean flag) {
		this.textEditor.setEnabled(flag);
		this.button.setEnabled(flag);
	}

	private void initComponent() {
		this.textEditor = new JTextField(40);
		this.textEditor.setBackground(Color.white);
		this.textEditor.setAutoscrolls(true);
		this.textEditor.setHorizontalAlignment(JTextField.LEFT);

		Dimension buttonDimension = new Dimension(23, 23);
		this.button = new JButton();
		this.button.setPreferredSize(buttonDimension);
		this.button.setIcon(ControlsResources.getIcon("/controlsresources/Image_DatasetGroup_Normal.png"));
		this.button.setToolTipText(ControlsProperties.getString("String_Select"));
		this.button.setBorder(BorderFactory.createEtchedBorder(1));
		this.button.setContentAreaFilled(false);
		this.button.setBorderPainted(false);
		this.button.setFocusPainted(false);
		this.button.setFocusable(false);
		// FIXME: 2017/4/20 参数传入什么
		this.fileChooser = new SmFileChoose("");

		this.setLayout(new GridBagLayout());
		this.add(this.textEditor, new GridBagConstraintsHelper(0, 0, 2, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.HORIZONTAL).setWeight(2, 0));
		this.add(this.button, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.WEST).setFill(GridBagConstraints.NONE).setWeight(0, 0).setInsets(0, 5, 0, 0));
	}

	private void setComponentName() {
		ComponentUIUtilities.setName(this.textEditor, "FileChooserControl_textEditor");
		ComponentUIUtilities.setName(this.button, "FileChooserControl_button");
	}

	private void registerListener() {
		textEditor.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				if (!isUpdatedByChooser) {
					firePathChanged();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (!isUpdatedByChooser) {
					firePathChanged();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				if (!isUpdatedByChooser) {
					firePathChanged();
				}
			}
		});
		addTextCommittedListener(textEditor);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireButtonClicked();
				if (fileChooser == null) {
					fileChooser = new SmFileChoose("");
				}
				//文件选择器置回上次使用目录
				if (!StringUtilities.isNullOrEmpty(textEditor.getText())) {
					File file = new File(textEditor.getText());
					try {
						if (file.getAbsoluteFile().getParentFile().isDirectory()) {
							fileChooser.setCurrentDirectory(file.getAbsoluteFile().getParentFile());
						}
					} catch (Exception e2) {
						try {
							if (file.getAbsoluteFile().isDirectory()) {
								fileChooser.setCurrentDirectory(file.getAbsoluteFile());
							}
						} catch (Exception e1) {
						}
					}

				}
				//弹出文件选择器
				int state;
				if (fileChooser instanceof SmFileChoose) {
					state = ((SmFileChoose) fileChooser).showDefaultDialog();
				} else {
					state = fileChooser.showDialog((Component) Application.getActiveApplication().getMainFrame(), fileChooser.getApproveButtonText());
				}
				if (state == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile() != null) {
					String path = fileChooser.getSelectedFile().getAbsolutePath();
					if (fileChooser instanceof SmFileChoose) {
						path = ((SmFileChoose) fileChooser).getFilePath();
					}
					/*if (new File(path).isDirectory() && !path.endsWith(File.separator)) {
						path += File.separator;
					}*/
					try {
						//isUpdatedByChooser避免setText两次触发firePathChanged：document remove、document insert
						isUpdatedByChooser = true;
						textEditor.setText(path);
						firePathChanged();
					} finally {
						isUpdatedByChooser = false;
					}
				}
			}
		});
	}

	private void addTextCommittedListener(JTextComponent textComponent) {
		if (textComponent == null) return;
		textComponent.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				firePathCommitted();
			}
		});

		textComponent.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_ENTER) {
					firePathCommitted();
				}
			}
		});
	}


}

