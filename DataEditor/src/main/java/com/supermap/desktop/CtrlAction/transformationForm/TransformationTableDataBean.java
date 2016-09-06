package com.supermap.desktop.CtrlAction.transformationForm;

import java.awt.*;

/**
 * @author XiaJT
 */
public class TransformationTableDataBean {

	private Point pointOriginal;
	private Point pointRefer;
	private Double ResidualX;
	private Double ResidualY;
	private Double ResidualTotal;
	private boolean isSelected;

	public Point getPointOriginal() {
		return pointOriginal;
	}

	public void setPointOriginal(Point pointOriginal) {
		this.pointOriginal = pointOriginal;
	}

	public Point getPointRefer() {
		return pointRefer;
	}

	public void setPointRefer(Point pointRefer) {
		this.pointRefer = pointRefer;
	}

	public double getResidualX() {
		return ResidualX;
	}

	public void setResidualX(double residualX) {
		ResidualX = residualX;
	}

	public double getResidualY() {
		return ResidualY;
	}

	public void setResidualY(double residualY) {
		ResidualY = residualY;
	}

	public Double getResidualTotal() {
		return ResidualTotal;
	}

	public void setResidualTotal(double residualTotal) {
		ResidualTotal = residualTotal;
	}

	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Object getIsSelected() {
		return isSelected;
	}
}
