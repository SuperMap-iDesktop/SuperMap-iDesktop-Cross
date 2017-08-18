package com.supermap.desktop.ui.lbs.ui;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.controls.utilities.ComponentFactory;
import com.supermap.desktop.controls.utilities.ToolbarUIUtilities;
import com.supermap.desktop.lbs.HDFSDefine;
import com.supermap.desktop.lbs.WebHDFS;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.utilities.StringUtilities;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 下载,上传主界面
 *
 * @author
 */
public class JDialogHDFSFiles extends SmDialog {


	private JButton buttonOK;
	private JButton buttonCancel;
	private JPanelHDFSFiles panelHDFSFiles;
	private JTable table;
	private JPopupMenu tablePopupMenu;
	private JMenuItem menuPreviewCsv;

	private ActionListener cancelListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			removeAndDispose();
		}
	};

	private void removeAndDispose() {
		removeEvents();
		JDialogHDFSFiles.this.dispose();
	}

	private MouseAdapter tableMouseListener = new MouseAdapter() {
		@Override
		public void mouseClicked(MouseEvent e) {
			tableMouseClicked(e);
			if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 1) {
				HDFSDefine hdfsDefine = (HDFSDefine) ((HDFSTableModel) table.getModel()).getRowTagAt(panelHDFSFiles.getSelectRow());
				menuPreviewCsv.setEnabled(hdfsDefine.getName().endsWith(".csv"));
				tablePopupMenu.show(table, e.getX(), e.getY());
			}
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					ToolbarUIUtilities.updataToolbarsState();
				}
			});
		}

		@Override
		public void mouseReleased(MouseEvent e) {

		}

		public void tableMouseClicked(MouseEvent e) {
			if (table.getSelectedRow() != -1 && e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
				panelHDFSFiles.resetHDFSPath();
			}
		}
	};
	private ActionListener okListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (!StringUtilities.isNullOrEmpty(panelHDFSFiles.getURL()) && panelHDFSFiles.getTable().getSelectedRow() >= 0) {
				String name = (String) panelHDFSFiles.getTable().getModel().getValueAt(panelHDFSFiles.getTable().getSelectedRow(), 0);
				String url = panelHDFSFiles.getURL();
				if (!url.endsWith("/")) {
					url += "/";
				}
				WebHDFS.resultURL = url + name;
				dialogResult = DialogResult.OK;
				removeAndDispose();
			}
		}
	};

	public JDialogHDFSFiles() {
		initializeComponents();
		initializeLayout();
		registEvents();
		this.setTitle(CommonProperties.getString("String_SelectFile"));
		setLocationRelativeTo(null);
	}


	private void registEvents() {
		removeEvents();
		this.table = panelHDFSFiles.getTable();
		this.table.addMouseListener(this.tableMouseListener);
		this.buttonOK.addActionListener(this.okListener);
		this.buttonCancel.addActionListener(this.cancelListener);
		menuPreviewCsv.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				HDFSDefine hdfsDefine = (HDFSDefine) ((HDFSTableModel) table.getModel()).getRowTagAt(panelHDFSFiles.getSelectRow());
				DialogResult dialogResult = new JDialogPreviewCSV(panelHDFSFiles.getURL(), hdfsDefine, ((HDFSTableModel) table.getModel())).showDialog();
				if (dialogResult == DialogResult.OK) {
					panelHDFSFiles.refresh();
				}
			}
		});
	}

	private void removeEvents() {
		this.buttonOK.removeActionListener(this.okListener);
		this.buttonCancel.removeActionListener(this.cancelListener);
	}

	public void initializeComponents() {
		this.setSize(900, 600);
		this.panelHDFSFiles = new JPanelHDFSFiles();
		this.buttonOK = ComponentFactory.createButtonOK();
		this.buttonCancel = ComponentFactory.createButtonCancel();
		menuPreviewCsv = new JMenuItem(ControlsProperties.getString("String_Preview"));
		tablePopupMenu = new JPopupMenu();
		tablePopupMenu.add(menuPreviewCsv);
	}

	private void initializeLayout() {
		GroupLayout gLayout = new GroupLayout(this.getContentPane());
		gLayout.setAutoCreateContainerGaps(true);
		gLayout.setAutoCreateGaps(true);
		this.getContentPane().setLayout(gLayout);

		// @formatter:off
        gLayout.setHorizontalGroup(gLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(panelHDFSFiles, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(gLayout.createSequentialGroup()
                        .addGap(10, 10, Short.MAX_VALUE)
                        .addComponent(this.buttonOK, 60, 60, 60)
                        .addComponent(this.buttonCancel, 60, 60, 60))
        );

        gLayout.setVerticalGroup(gLayout.createSequentialGroup()
                .addComponent(panelHDFSFiles, 100, 200, Short.MAX_VALUE)
                .addGroup(gLayout.createParallelGroup(Alignment.CENTER)
                        .addComponent(this.buttonOK)
                        .addComponent(this.buttonCancel)));
        // @formatter:on
	}

	public DialogResult showDialog() {
		this.setVisible(true);
		return this.dialogResult;
	}

}
