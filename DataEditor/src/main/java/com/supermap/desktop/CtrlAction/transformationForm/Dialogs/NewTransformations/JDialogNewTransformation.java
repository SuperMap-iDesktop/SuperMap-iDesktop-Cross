package com.supermap.desktop.CtrlAction.transformationForm.Dialogs.NewTransformations;

import com.supermap.data.TransformationMode;
import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.dataeditor.DataEditorProperties;
import com.supermap.desktop.properties.CommonProperties;
import com.supermap.desktop.ui.controls.DialogResult;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;
import com.supermap.desktop.ui.controls.SmDialog;
import com.supermap.desktop.ui.controls.button.SmButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author XiaJT
 */
public class JDialogNewTransformation extends SmDialog {

	private JPanelChooseOriginal panelChooseOriginal = new JPanelChooseOriginal();
	private JPanelChooseRefer panelChooseRefer = new JPanelChooseRefer();
	private JPanelChooseTransformationFile panelChooseTransformationFile = new JPanelChooseTransformationFile();

	private JPanel panelCenter = new JPanel();
	private JPanel panelButtons = new JPanel();
	private SmButton buttonPre = new SmButton();
	private SmButton buttonNext = new SmButton();
	private SmButton buttonCancle = new SmButton();

	private int state = 0;
	private GridBagConstraintsHelper centerConstraints;

	public JDialogNewTransformation() {
		this.setSize(600, 450);
		init();
		this.setLocationRelativeTo(null);
	}

	private void init() {
		initLayout();
		initListeners();
		initResources();
		initComponentStates();
	}

	private void initLayout() {
		panelButtons.setLayout(new GridBagLayout());
		panelButtons.add(new JPanel(), new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 0).setFill(GridBagConstraints.HORIZONTAL).setInsets(0, 10, 0, 0));
		panelButtons.add(buttonPre, new GridBagConstraintsHelper(1, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(10, 0, 10, 0));
		panelButtons.add(buttonNext, new GridBagConstraintsHelper(2, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(10, 5, 10, 0));
		panelButtons.add(buttonCancle, new GridBagConstraintsHelper(3, 0, 1, 1).setWeight(0, 0).setAnchor(GridBagConstraints.EAST).setFill(GridBagConstraints.NONE).setInsets(10, 5, 10, 10));

		panelCenter.setLayout(new GridBagLayout());
		centerConstraints = new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setFill(GridBagConstraints.BOTH);
		panelCenter.add(panelChooseOriginal, centerConstraints);

		this.setLayout(new GridBagLayout());
		this.add(panelCenter, new GridBagConstraintsHelper(0, 0, 1, 1).setWeight(1, 1).setAnchor(GridBagConstraints.CENTER).setInsets(10, 10, 0, 10).setFill(GridBagConstraints.BOTH));
		this.add(panelButtons, new GridBagConstraintsHelper(0, 1, 1, 1).setWeight(1, 0).setAnchor(GridBagConstraints.CENTER).setInsets(10, 10, 0, 10).setFill(GridBagConstraints.HORIZONTAL));
	}

	private void initListeners() {
		buttonPre.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (state == 1) {
					panelCenter.removeAll();
					panelCenter.add(panelChooseOriginal, centerConstraints);
					setTitle(panelChooseOriginal.getPanelTitle());
					panelCenter.revalidate();
					panelChooseOriginal.setButtonNext(buttonNext);
					panelCenter.repaint();
					buttonPre.setVisible(false);
				} else if (state == 2) {
					panelCenter.removeAll();
					panelCenter.add(panelChooseRefer, centerConstraints);
					setTitle(panelChooseRefer.getPanelTitle());
					panelCenter.revalidate();
					panelCenter.repaint();
					panelChooseRefer.setButtonNext(buttonNext);
					buttonNext.setText(ControlsProperties.getString("String_NextWay"));
				}
				state--;
			}
		});
		buttonNext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (state == 0) {
					panelCenter.removeAll();
					panelCenter.add(panelChooseRefer, centerConstraints);
					setTitle(panelChooseRefer.getPanelTitle());
					panelCenter.revalidate();
					panelCenter.repaint();
					buttonPre.setVisible(true);
				} else if (state == 1) {
					panelCenter.removeAll();
					panelCenter.add(panelChooseTransformationFile, centerConstraints);
					setTitle(panelChooseTransformationFile.getPanelTitle());
					panelCenter.revalidate();
					panelCenter.repaint();
					buttonNext.setText(DataEditorProperties.getString("String_Finish"));
				} else if (state == 2) {
					buttonOkClicked();
				}
				state++;
			}
		});
		buttonCancle.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
	}

	private void buttonOkClicked() {
		dialogResult = DialogResult.OK;
		dispose();
	}

	private void initResources() {
		buttonPre.setText(ControlsProperties.getString("String_LastWay"));
		buttonNext.setText(ControlsProperties.getString("String_NextWay"));
		buttonCancle.setText(CommonProperties.getString(CommonProperties.Cancel));
	}

	private void initComponentStates() {
		setTitle(panelChooseOriginal.getPanelTitle());
		buttonPre.setVisible(false);
		panelChooseOriginal.setButtonNext(buttonNext);
	}

	public List<Object> getReferenceObjects() {
		return panelChooseRefer.getReferenceObjects();
	}

	public TransformationMode getTransformationMode() {
		return panelChooseTransformationFile.getTransformationMode();
	}

	public List<Object> getTargetObjects() {
		return panelChooseOriginal.getTargetObjects();
	}

	public boolean isSelectTransformationFile() {
		return panelChooseTransformationFile.isSelectedTransformationFile();
	}

	public String getSelectTransformationFilePath() {
		return panelChooseTransformationFile.getSelectedTransformationFilePath();
	}
}
