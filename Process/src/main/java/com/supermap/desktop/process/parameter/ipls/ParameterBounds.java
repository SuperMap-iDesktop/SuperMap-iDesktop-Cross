package com.supermap.desktop.process.parameter.ipls;

import com.supermap.data.Dataset;
import com.supermap.data.Rectangle2D;
import com.supermap.desktop.Application;
import com.supermap.desktop.process.parameter.interfaces.ISelectionParameter;
import com.supermap.desktop.properties.CoreProperties;
import com.supermap.desktop.utilities.ClipBoardUtilties;
import com.supermap.desktop.utilities.DoubleUtilities;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author XiaJT
 */
public class ParameterBounds extends ParameterCombine implements ISelectionParameter {
	private ParameterNumber parameterLeft = new ParameterNumber(CoreProperties.getString("String_LabelLeft"));
	private ParameterNumber parameterBottom = new ParameterNumber(CoreProperties.getString("String_LabelBottom"));
	private ParameterNumber parameterRight = new ParameterNumber(CoreProperties.getString("String_LabelRight"));
	private ParameterNumber parameterTop = new ParameterNumber(CoreProperties.getString("String_LabelTop"));

	private ParameterButton parameterCopy = new ParameterButton(CoreProperties.getString("String_CopySymbolOrGroup"));
	private ParameterButton parameterPaste = new ParameterButton(CoreProperties.getString("String_PasteSymbolOrGroup"));


	private boolean isSelectedItem = false;

	public ParameterBounds() {
		initComponents();
		initLayout();
		initResources();
		initComponentState();
		initListener();
	}

	private void initComponents() {

	}

	private void initLayout() {
		ParameterCombine parameterCombineLeft = new ParameterCombine();
		parameterCombineLeft.addParameters(parameterLeft, parameterBottom, parameterRight, parameterTop);
		ParameterCombine parameterCombineRight = new ParameterCombine();
		parameterCombineRight.addParameters(parameterCopy, parameterPaste, new ParameterCombine(), new ParameterCombine());
		parameterCombineRight.setWeightIndex(2);

		this.setCombineType(HORIZONTAL);
		this.addParameters(parameterCombineLeft, parameterCombineRight);
		this.setWeightIndex(0);
	}

	private void initResources() {

	}

	private void initComponentState() {
		parameterLeft.setSelectedItem("0");
		parameterBottom.setSelectedItem("0");
		parameterRight.setSelectedItem("0");
		parameterTop.setSelectedItem("0");
	}

	private void initListener() {
		parameterCopy.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ClipBoardUtilties.setBounds(new Rectangle2D(
						DoubleUtilities.stringToValue(parameterLeft.getSelectedItem()),
						DoubleUtilities.stringToValue(parameterBottom.getSelectedItem()),
						DoubleUtilities.stringToValue(parameterRight.getSelectedItem()),
						DoubleUtilities.stringToValue(parameterTop.getSelectedItem())
				));
				Application.getActiveApplication().getOutput().output(CoreProperties.getString("String_MapBounds_Has_Been_Copied"));
			}
		});
		parameterPaste.setActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Rectangle2D bounds = ClipBoardUtilties.getBounds();
				if (bounds != null) {
					setSelectedItem(bounds);
				}
			}
		});

		PropertyChangeListener propertyChangeListener = new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (!isSelectedItem && evt.getPropertyName().equals(ParameterTextField.PROPERTY_VALE)) {
					firePropertyChangeListener(new PropertyChangeEvent(ParameterBounds.this, PROPERTY_VALE, null, getSelectedItem()));
				}
			}
		};
		parameterLeft.addPropertyListener(propertyChangeListener);
		parameterBottom.addPropertyListener(propertyChangeListener);
		parameterRight.addPropertyListener(propertyChangeListener);
		parameterTop.addPropertyListener(propertyChangeListener);
	}

	@Override
	public void setSelectedItem(Object item) {
		isSelectedItem = true;
		try {
			if (item == null) {
				item = new Rectangle2D(0, 0, 0, 0);
			}
			if (item instanceof Dataset) {
				item = ((Dataset) item).getBounds();
			}
			if (item instanceof Rectangle2D) {
				parameterLeft.setSelectedItem(((Rectangle2D) item).getLeft());
				parameterBottom.setSelectedItem(((Rectangle2D) item).getBottom());
				parameterRight.setSelectedItem(((Rectangle2D) item).getRight());
				parameterTop.setSelectedItem(((Rectangle2D) item).getTop());
				firePropertyChangeListener(new PropertyChangeEvent(this, PROPERTY_VALE, null, item));
			}
		} catch (Exception e) {
			Application.getActiveApplication().getOutput().output(e);
		} finally {
			isSelectedItem = false;
		}
	}

	@Override
	public Rectangle2D getSelectedItem() {
		return new Rectangle2D(
				Double.valueOf(parameterLeft.getSelectedItem()),
				Double.valueOf(parameterBottom.getSelectedItem()),
				Double.valueOf(parameterRight.getSelectedItem()),
				Double.valueOf(parameterTop.getSelectedItem()));
	}

	@Override
	public void setEnabled(boolean enabled) {
		parameterLeft.setEnabled(enabled);
		parameterBottom.setEnabled(enabled);
		parameterRight.setEnabled(enabled);
		parameterTop.setEnabled(enabled);
		parameterPaste.setEnabled(enabled);
	}
}
