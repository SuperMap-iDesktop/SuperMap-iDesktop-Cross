package com.supermap.desktop.CtrlAction.transformationForm.beans;

import com.supermap.data.Point2D;

/**
 * @author XiaJT
 */
public class TransformationTableDataBean {

	private String ID;
	private Point2D pointOriginal;
	private Point2D pointRefer;
	private Double ResidualX;
	private Double ResidualY;
	private Double ResidualTotal;
	private boolean isSelected = true;

	public Point2D getPointOriginal() {
		return pointOriginal;
	}

	public void setPointOriginal(Point2D pointOriginal) {
		this.pointOriginal = pointOriginal;
	}

	public Point2D getPointRefer() {
		return pointRefer;
	}

	public void setPointRefer(Point2D pointRefer) {
		this.pointRefer = pointRefer;
	}

	public Double getResidualX() {
		return ResidualX;
	}

	public void setResidualX(double residualX) {
		ResidualX = residualX;
	}

	public Double getResidualY() {
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

	public String getID() {
		return ID;
	}

	public void setID(String ID) {
		this.ID = ID;
	}
}
