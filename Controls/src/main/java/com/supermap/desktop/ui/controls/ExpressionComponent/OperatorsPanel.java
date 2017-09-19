package com.supermap.desktop.ui.controls.ExpressionComponent;

import com.supermap.desktop.controls.ControlsProperties;
import com.supermap.desktop.ui.controls.GridBagConstraintsHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by lixiaoyao on 2017/9/18.
 */
public class OperatorsPanel extends JPanel {

	private JButton jButtonNot;
	private JButton jButtonOr;
	private JButton jButtonXOR;
	private JButton jButtonAnd;
	private JButton jButtonRightBracket;
	private JButton jButtonLeftBracket;
	private JButton jButtonLessOrEqual;
	private JButton jButtonMoreOrEqual;
	private JButton jButtonBracket;
	private JButton jButtonEqual;
	private JButton jButtonLess;
	private JButton jButtonMore;
	private JButton jButtonDivide;
	private JButton jButtonMultiply;
	private JButton jButtonSubtract;
	private JButton jButtonPlus;

	private ButtonOperatorListener buttonOperatorListener = null;

	public OperatorsPanel() {
		initComponents();
		initLayout();
	}

	private void initComponents() {
		this.jButtonPlus = new JButton("+");
		this.jButtonSubtract = new JButton("-");
		this.jButtonMultiply = new JButton("*");
		this.jButtonDivide = new JButton("/");
		this.jButtonAnd = new JButton("And");
		this.jButtonMore = new JButton(">");
		this.jButtonLess = new JButton("<");
		this.jButtonEqual = new JButton("=");
		this.jButtonBracket = new JButton("<>");
		this.jButtonNot = new JButton("Not");
		this.jButtonMoreOrEqual = new JButton(">=");
		this.jButtonLessOrEqual = new JButton("<=");
		this.jButtonLeftBracket = new JButton("(");
		this.jButtonRightBracket = new JButton(")");
		this.jButtonXOR = new JButton("Xor");
		this.jButtonOr = new JButton("Or");
	}

	private void initLayout() {
		this.setBorder(BorderFactory.createTitledBorder(ControlsProperties.getString("String_CommonOperator")));
		this.setLayout(new GridBagLayout());
		this.add(this.jButtonPlus, new GridBagConstraintsHelper(0, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.add(this.jButtonSubtract, new GridBagConstraintsHelper(1, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.add(this.jButtonMultiply, new GridBagConstraintsHelper(2, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.add(this.jButtonDivide, new GridBagConstraintsHelper(3, 0, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));

		this.add(this.jButtonMore, new GridBagConstraintsHelper(0, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.add(this.jButtonLess, new GridBagConstraintsHelper(1, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.add(this.jButtonEqual, new GridBagConstraintsHelper(2, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.add(this.jButtonBracket, new GridBagConstraintsHelper(3, 1, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));

		this.add(this.jButtonMoreOrEqual, new GridBagConstraintsHelper(0, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.add(this.jButtonLessOrEqual, new GridBagConstraintsHelper(1, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.add(this.jButtonLeftBracket, new GridBagConstraintsHelper(2, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.add(this.jButtonRightBracket, new GridBagConstraintsHelper(3, 2, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));

		this.add(this.jButtonAnd, new GridBagConstraintsHelper(0, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.add(this.jButtonXOR, new GridBagConstraintsHelper(1, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.add(this.jButtonOr, new GridBagConstraintsHelper(2, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
		this.add(this.jButtonNot, new GridBagConstraintsHelper(3, 3, 1, 1).setAnchor(GridBagConstraints.CENTER).setWeight(1, 1).setFill(GridBagConstraints.HORIZONTAL).setInsets(1));
	}

	public void registerEvents() {
		this.jButtonPlus.addMouseListener(this.numberMouseAdapter);
		this.jButtonSubtract.addMouseListener(this.numberMouseAdapter);
		this.jButtonMultiply.addMouseListener(this.numberMouseAdapter);
		this.jButtonDivide.addMouseListener(this.numberMouseAdapter);
		this.jButtonMore.addMouseListener(this.numberMouseAdapter);
		this.jButtonLess.addMouseListener(this.numberMouseAdapter);
		this.jButtonEqual.addMouseListener(this.numberMouseAdapter);
		this.jButtonBracket.addMouseListener(this.numberMouseAdapter);
		this.jButtonMoreOrEqual.addMouseListener(this.numberMouseAdapter);
		this.jButtonLessOrEqual.addMouseListener(this.numberMouseAdapter);
		this.jButtonLeftBracket.addMouseListener(this.numberMouseAdapter);
		this.jButtonRightBracket.addMouseListener(this.numberMouseAdapter);
		this.jButtonAnd.addMouseListener(this.numberMouseAdapter);
		this.jButtonNot.addMouseListener(this.numberMouseAdapter);
		this.jButtonXOR.addMouseListener(this.numberMouseAdapter);
		this.jButtonOr.addMouseListener(this.numberMouseAdapter);
	}

	private void removeEvents() {
		this.jButtonPlus.removeMouseListener(this.numberMouseAdapter);
		this.jButtonSubtract.removeMouseListener(this.numberMouseAdapter);
		this.jButtonMultiply.removeMouseListener(this.numberMouseAdapter);
		this.jButtonDivide.removeMouseListener(this.numberMouseAdapter);
		this.jButtonMore.removeMouseListener(this.numberMouseAdapter);
		this.jButtonLess.removeMouseListener(this.numberMouseAdapter);
		this.jButtonEqual.removeMouseListener(this.numberMouseAdapter);
		this.jButtonBracket.removeMouseListener(this.numberMouseAdapter);
		this.jButtonMoreOrEqual.removeMouseListener(this.numberMouseAdapter);
		this.jButtonLessOrEqual.removeMouseListener(this.numberMouseAdapter);
		this.jButtonLeftBracket.removeMouseListener(this.numberMouseAdapter);
		this.jButtonRightBracket.removeMouseListener(this.numberMouseAdapter);
		this.jButtonAnd.removeMouseListener(this.numberMouseAdapter);
		this.jButtonNot.removeMouseListener(this.numberMouseAdapter);
		this.jButtonXOR.removeMouseListener(this.numberMouseAdapter);
		this.jButtonOr.removeMouseListener(this.numberMouseAdapter);
	}

	public void addButtonOperatorListener(ButtonOperatorListener buttonOperatorListener) {
		this.buttonOperatorListener = buttonOperatorListener;
		registerEvents();
	}

	public void removeButtonOperatorListener(ButtonOperatorListener buttonOperatorListener) {
		removeEvents();
	}

	private MouseAdapter numberMouseAdapter = new LocalMouseAdapter();

	class LocalMouseAdapter extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (buttonOperatorListener != null) {
				buttonOperatorListener.buttonOperator_Click(((JButton) e.getSource()).getText());
			}
		}
	}
}
