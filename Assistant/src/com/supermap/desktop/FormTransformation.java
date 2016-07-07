package com.supermap.desktop;

import com.supermap.data.Dataset;
import com.supermap.data.Datasource;
import com.supermap.desktop.Interface.IFormTransformation;
import com.supermap.desktop.enums.WindowType;
import com.supermap.desktop.ui.FormBaseChild;

import javax.swing.*;
import java.awt.*;

/**
 * @author XiaJT
 */
public class FormTransformation extends FormBaseChild implements IFormTransformation {
	public FormTransformation() {
		this(null);

	}

	public FormTransformation(String name) {
		this(name, null, null);
		setText(name);
	}

	public FormTransformation(String name, Icon icon, Component component) {
		super(name, icon, component);

	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public void setText(String text) {

	}

	@Override
	public WindowType getWindowType() {
		return WindowType.TRANSFORMATION;
	}

	@Override
	public boolean save() {
		return false;
	}

	@Override
	public boolean save(boolean notify, boolean isNewWindow) {
		return false;
	}

	@Override
	public boolean saveFormInfos() {
		return false;
	}

	@Override
	public boolean saveAs(boolean isNewWindow) {
		return false;
	}

	@Override
	public boolean isNeedSave() {
		return false;
	}

	@Override
	public void setNeedSave(boolean needSave) {

	}

	@Override
	public boolean isActivated() {
		return false;
	}

	@Override
	public void actived() {

	}

	@Override
	public void deactived() {

	}

	@Override
	public void windowShown() {

	}

	@Override
	public void windowHidden() {

	}

	@Override
	public void clean() {

	}

	@Override
	public void setTransformationDataset(Dataset transformationDataset) {

	}

	@Override
	public void addReferenceDataset(Dataset referenceDataset) {

	}

	@Override
	public void setResultDataSource(Datasource resultDatasource) {

	}

	@Override
	public void setResultDatasetName(String resultDatasetName) {

	}
}
