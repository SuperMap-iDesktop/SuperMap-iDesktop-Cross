package com.supermap.desktop.process.parameter.definedClass;

/**
 * Created By Chens on 2017/8/22 0022
 * 统计数据类
 */
public class StatisticsCollection {
	private double max;
	private double min;
	private double mean;
	private double standardDeviation;
	private double variance;

	public StatisticsCollection() {
	}

	public StatisticsCollection(double max, double min, double mean, double standardDeviation, double variance) {
		this.max = max;
		this.min = min;
		this.mean = mean;
		this.standardDeviation = standardDeviation;
		this.variance = variance;
	}

	public double getMax() {
		return max;
	}

	public double getMin() {
		return min;
	}

	public double getMean() {
		return mean;
	}

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public double getVariance() {
		return variance;
	}

	public void setMax(double max) {
		this.max = max;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}

	public void setVariance(double variance) {
		this.variance = variance;
	}
}
