package com.supermap.desktop.Interface;

import com.supermap.data.Transformation;
import com.supermap.data.TransformationMode;
import com.supermap.desktop.enums.FormTransformationSubFormType;
import com.supermap.ui.Action;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author XiaJT
 */
public interface IFormTransformation extends IForm {

	void addReferenceObjects(List<Object> listObjects);

	void startAddPoint();

	boolean isAddPointing();

	JTable getTable();

	void centerOriginal();

	void deleteTableSelectedRow();

	void setAction(Action action);

	void addTargetObjects(List<Object> targetObject);

	Color getSelectedColor();

	void setSelectedColor(Color selectedColor);

	Color getUnSelectedColor();

	void setUnSelectedColor(Color unSelectedColor);

	Color getUnUseColor();

	void setUnUseColor(Color unUseColor);

	TransformationMode getTransformationMode();

	void setTransformationMode(TransformationMode transformationMode);

	Transformation getTransformation();

	void setTransformation(Transformation transformation);

	Object[] getTransformationObjects();

	String toXml();

	boolean fromXml(Document document);

	FormTransformationSubFormType getCurrentSubFormType();
}
