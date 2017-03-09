package com.supermap.desktop.process.graphics.graphs;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

/**
 * Created by lixiaoyao on 2017/2/28.
 */
public class FocusPointCalculator {

    /**
     * 计算椭圆与输入线段的焦点坐标
     *
     * @param inputEllipse       要计算的椭圆
     * @param inputLinePrePoint  计算的线段的某一个端点的坐标
     * @param inputLineNextPoint 计算的线段的另一个端点的坐标
     */
    public ArrayList<Point2D> calculaor(Ellipse2D inputEllipse, Point2D inputLinePrePoint, Point2D inputLineNextPoint) {
        ArrayList<Point2D> finalResult = new ArrayList<>();
        Point2D tempResult[] = new Point2D[2];
        double a = inputEllipse.getWidth() / 2;
        double b = inputEllipse.getHeight() / 2;
        if (Double.compare(a, b) == -1) {
            double temp = a;
            a = b;
            b = temp;
        }
        //  进行坐标转换，转换到中心点为(0,0)坐标系进行计算
        inputLinePrePoint = new Point2D.Double(inputLinePrePoint.getX() - a, inputLinePrePoint.getY() - b);
        inputLineNextPoint = new Point2D.Double(inputLineNextPoint.getX() - a, inputLineNextPoint.getY() - b);

        double k = (inputLinePrePoint.getY() - inputLineNextPoint.getY()) / (inputLinePrePoint.getX() - inputLineNextPoint.getX());  //计算线段斜率
        double m = inputLinePrePoint.getY() - k * inputLinePrePoint.getX();    //   计算线段截距
        double derta = 4 * a * a * a * a * k * k * m * m - 4 * (b * b + a * a * k * k) * (a * a * m * m - a * a * b * b); //  根据韦达定理来判断是否相交或者相交是交于一个焦点还是两个焦点

        Point2D pointNew1;
        Point2D pointNew2;
        double x1;
        double x2;
        double y1;
        double y2;

        if (Double.compare(derta, 0) == 0) {   // 一个焦点
            x1 = ((0 - 2 * a * a * k * m) + Math.sqrt(derta)) / (2 * (b * b + a * a * k * k));
            y1 = k * x1 + m;

            pointNew1 = new Point2D.Double(x1, y1);
            tempResult[0] = pointNew1;
        } else if (Double.compare(derta, 0) == 1) { //  两个焦点
            x1 = ((0 - 2 * a * a * k * m) + Math.sqrt(derta)) / (2 * (b * b + a * a * k * k));
            x2 = ((0 - 2 * a * a * k * m) - Math.sqrt(derta)) / (2 * (b * b + a * a * k * k));
            y1 = k * x1 + m;
            y2 = k * x2 + m;

            pointNew1 = new Point2D.Double(x1, y1);
            pointNew2 = new Point2D.Double(x2, y2);
            tempResult[0] = pointNew1;
            tempResult[1] = pointNew2;
        }
        isFocusPointInInputLine(tempResult, inputLinePrePoint.getX(), inputLineNextPoint.getX());
        focusPointSort(tempResult, inputEllipse.getCenterX(), inputEllipse.getCenterY());
        //  转为动态数组，同时对焦点坐标进行转换，由中心点为(0,0)坐标系转为当前椭圆的坐标系
        for (int i = 0; i < tempResult.length; i++) {
            if (tempResult[i] != null) {
                Point2D tempPoint = new Point2D.Double(tempResult[i].getX() + a, tempResult[i].getY() + b);
                finalResult.add(tempPoint);
            }
        }
        return finalResult;
    }


    /**
     * 计算圆角矩形与输入线段的焦点坐标
     *
     * @param inputRoundRectangle 要计算的圆角矩形
     * @param inputLinePrePoint   计算的线段的某一个端点的坐标
     * @param inputLineNextPoint  计算的线段的另一个端点的坐标
     */
    public ArrayList<Point2D> calculaor(RoundRectangle2D inputRoundRectangle, Point2D inputLinePrePoint, Point2D inputLineNextPoint) {
        ArrayList<Point2D> finalResult = new ArrayList<>();
        Point2D tempResult[] = new Point2D[2];

        double k = (inputLinePrePoint.getY() - inputLineNextPoint.getY()) / (inputLinePrePoint.getX() - inputLineNextPoint.getX());  //计算线段斜率
        double m = inputLinePrePoint.getY() - k * inputLinePrePoint.getX();    //   计算线段截距

        double centreX = inputRoundRectangle.getCenterX();
        double centreY = inputRoundRectangle.getCenterY();
        double trueHeight = inputRoundRectangle.getHeight() / 2 - inputRoundRectangle.getArcHeight();
        double trueWidth = inputRoundRectangle.getWidth() / 2 - inputRoundRectangle.getArcWidth();

        Point2D pointTop1 = new Point2D.Double(centreX - trueWidth, centreY + inputRoundRectangle.getHeight() / 2);
        Point2D pointTop2 = new Point2D.Double(centreX + trueWidth, centreY + inputRoundRectangle.getHeight() / 2);
        Point2D pointDown1 = new Point2D.Double(centreX - trueWidth, centreY - inputRoundRectangle.getHeight() / 2);
        Point2D pointDown2 = new Point2D.Double(centreX + trueWidth, centreY - inputRoundRectangle.getHeight() / 2);
        Point2D pointLeft1 = new Point2D.Double(centreX - inputRoundRectangle.getWidth() / 2, centreY + trueHeight);
        Point2D pointLeft2 = new Point2D.Double(centreX - inputRoundRectangle.getWidth() / 2, centreY - trueHeight);
        Point2D pointRight1 = new Point2D.Double(centreX + inputRoundRectangle.getWidth() / 2, centreY + trueHeight);
        Point2D pointRight2 = new Point2D.Double(centreX + inputRoundRectangle.getWidth() / 2, centreY - trueHeight);

        if (calRoundRectangleLinesFocus(tempResult, pointTop1, pointTop2, inputLinePrePoint, inputLineNextPoint)) {
            isFocusPointInInputLine(tempResult, inputLinePrePoint.getX(), inputLineNextPoint.getX());
            focusPointSort(tempResult, centreX, centreY);
            fixedArrayToArrayList(tempResult, finalResult);
            return finalResult;
        }

        if (calRoundRectangleLinesFocus(tempResult, pointDown1, pointDown2, inputLinePrePoint, inputLineNextPoint)) {
            isFocusPointInInputLine(tempResult, inputLinePrePoint.getX(), inputLineNextPoint.getX());
            focusPointSort(tempResult, centreX, centreY);
            fixedArrayToArrayList(tempResult, finalResult);
            return finalResult;
        }

        if (calRoundRectangleLinesFocus(tempResult, pointLeft1, pointLeft2, inputLinePrePoint, inputLineNextPoint)) {
            isFocusPointInInputLine(tempResult, inputLinePrePoint.getX(), inputLineNextPoint.getX());
            focusPointSort(tempResult, centreX, centreY);
            fixedArrayToArrayList(tempResult, finalResult);
            return finalResult;
        }

        if (calRoundRectangleLinesFocus(tempResult, pointRight1, pointRight2, inputLinePrePoint, inputLineNextPoint)) {
            isFocusPointInInputLine(tempResult, inputLinePrePoint.getX(), inputLineNextPoint.getX());
            focusPointSort(tempResult, centreX, centreY);
            fixedArrayToArrayList(tempResult, finalResult);
            return finalResult;
        }

        if (calRoundRectangleArcsAngleFocus(tempResult, inputRoundRectangle.getArcWidth(), inputRoundRectangle.getArcHeight(), pointRight1, pointTop2, k, m)) {
            isFocusPointInInputLine(tempResult, inputLinePrePoint.getX(), inputLineNextPoint.getX());
            focusPointSort(tempResult, centreX, centreY);
            fixedArrayToArrayList(tempResult, finalResult);
            return finalResult;
        }

        if (calRoundRectangleArcsAngleFocus(tempResult, inputRoundRectangle.getArcWidth(), inputRoundRectangle.getArcHeight(), pointTop1, pointLeft1, k, m)) {
            isFocusPointInInputLine(tempResult, inputLinePrePoint.getX(), inputLineNextPoint.getX());
            focusPointSort(tempResult, centreX, centreY);
            fixedArrayToArrayList(tempResult, finalResult);
            return finalResult;
        }

        if (calRoundRectangleArcsAngleFocus(tempResult, inputRoundRectangle.getArcWidth(), inputRoundRectangle.getArcHeight(), pointLeft2, pointDown1, k, m)) {
            isFocusPointInInputLine(tempResult, inputLinePrePoint.getX(), inputLineNextPoint.getX());
            focusPointSort(tempResult, centreX, centreY);
            fixedArrayToArrayList(tempResult, finalResult);
            return finalResult;
        }

        if (calRoundRectangleArcsAngleFocus(tempResult, inputRoundRectangle.getArcWidth(), inputRoundRectangle.getArcHeight(), pointDown2, pointRight2, k, m)) {
            isFocusPointInInputLine(tempResult, inputLinePrePoint.getX(), inputLineNextPoint.getX());
            focusPointSort(tempResult, centreX, centreY);
            fixedArrayToArrayList(tempResult, finalResult);
            return finalResult;
        }

        isFocusPointInInputLine(tempResult, inputLinePrePoint.getX(), inputLineNextPoint.getX());
        focusPointSort(tempResult, centreX, centreY);
        fixedArrayToArrayList(tempResult, finalResult);
        return finalResult;
    }

    /**
     * 中间结果静态数组转为动态数组
     *
     * @param tempResult  静态结果数组
     * @param finalResult 动态结果数组
     */
    private void fixedArrayToArrayList(Point2D tempResult[], ArrayList<Point2D> finalResult) {
        for (int i = 0; i < tempResult.length; i++) {
            if (tempResult[i] != null) {
                finalResult.add(tempResult[i]);
            }
        }
    }

    /**
     * 对算出焦点的结果进行过滤，只有在输入线段范围内的焦点才算是真正的焦点
     *
     * @param result              计算的焦点结果数组
     * @param inputLinePrePointX  计算的线段的某一个端点的X坐标
     * @param inputLineNextPointX 计算的线段的另一个端点的X坐标
     */
    private void isFocusPointInInputLine(Point2D result[], double inputLinePrePointX, double inputLineNextPointX) {
        if (Double.compare(inputLinePrePointX, inputLineNextPointX) == 1) {
            double temp = inputLinePrePointX;
            inputLinePrePointX = inputLineNextPointX;
            inputLineNextPointX = temp;
        }
        for (int i = 0; i < result.length; i++) {
            if (result[i] != null) {
                if (Double.compare(result[i].getX(), inputLinePrePointX) == -1 || Double.compare(result[i].getX(), inputLineNextPointX) == 1) {
                    result[i] = null;
                }
            }
        }
    }

    /**
     * 对算出焦点的结果进行排序，如果有两个结果，则返回数组中最近的焦点排在第一位，如果只有一个，则这个焦点肯定的是最近的
     *
     * @param result  计算的焦点结果数组
     * @param centerX 当前shape的中心X坐标
     * @param centerY 当前shape的中心Y坐标
     */
    private void focusPointSort(Point2D result[], double centerX, double centerY) {
        if (result[0] != null && result[1] != null) {
            double distance1 = Math.sqrt(Math.pow(result[0].getX() - centerX, 2) + Math.pow(result[0].getY() - centerY, 2));
            double distance2 = Math.sqrt(Math.pow(result[1].getX() - centerX, 2) + Math.pow(result[1].getY() - centerY, 2));
            if (Double.compare(distance1, distance2) == 1) {
                Point2D pointTemp = new Point2D.Double(result[0].getX(), result[0].getY());
                result[0] = new Point2D.Double(result[1].getX(), result[1].getY());
                result[1] = pointTemp;
            }
        } else if (result[0] == null && result[1] != null) {
            result[0] = new Point2D.Double(result[1].getX(), result[1].getY());
            result[1] = null;
        }
    }

    /**
     * 计算圆角矩形中线段部分是否跟输入线段有焦点
     *
     * @param result                      计算的焦点结果数组
     * @param roundRectangleLinePointPre  圆角矩形中线段的一个端点坐标
     * @param roundRectangleLinePointNext 圆角矩形同一个线段的另一个端点坐标
     * @param inputLinePointPre           导入的线段的某一个端点的坐标
     * @param inputLinePointNext          导入的线段的另一个端点的坐标
     */
    private boolean calRoundRectangleLinesFocus(Point2D result[], Point2D roundRectangleLinePointPre, Point2D roundRectangleLinePointNext, Point2D inputLinePointPre, Point2D inputLinePointNext) {
        boolean isCanBack = false;
        Point2D tempResult[];
        Point2D tempPoint;

        tempResult = lineFocusPoint(roundRectangleLinePointPre, roundRectangleLinePointNext, inputLinePointPre, inputLinePointNext);
        if (tempResult[0] != null) {
            tempPoint = new Point2D.Double(tempResult[0].getX(), tempResult[0].getY());
            if (result[0] != null) {
                result[1] = tempPoint;
            } else {
                result[0] = tempPoint;
            }
        }
        if (result[0] != null && result[1] != null) {
            isCanBack = true;
        }
        return isCanBack;
    }

    /**
     * 计算圆角矩形中圆角胡部分是否跟输入线段有焦点
     *
     * @param result       计算的焦点结果数组
     * @param arcWidth     圆角矩形中圆角弧的宽度
     * @param arcHeight    圆角矩形圆角弧的高度
     * @param arcAnglePre  圆角矩形圆角弧的逆时针方向排在前面的点
     * @param arcAngleNext 圆角矩形圆角弧的逆时针方向排在前一个点后面的点，且两个是同一个圆角弧
     * @param k            导入的线段的斜率
     * @param m            导入的线段的截距
     */
    private boolean calRoundRectangleArcsAngleFocus(Point2D result[], double arcWidth, double arcHeight, Point2D arcAnglePre, Point2D arcAngleNext, double k, double m) {
        boolean isCanBack = false;
        Point2D tempResult[];
        Point2D tempPoint;

        tempResult = arcFocusPointForRoundRectangle(arcWidth, arcHeight, arcAnglePre, arcAngleNext, k, m);
        if (tempResult[0] != null) {
            tempPoint = new Point2D.Double(tempResult[0].getX(), tempResult[0].getY());
            if (result[0] != null) {
                result[1] = tempPoint;
            } else {
                result[0] = tempPoint;
            }
        }
        if (result[0] != null && result[1] != null) {
            isCanBack = true;
        }

        return isCanBack;
    }

    /**
     * 判断线段与线段之间是否有焦点的方法
     *
     * @param line1Point1 线段1的其中一个端点
     * @param line1Point2 线段1的另一个端点
     * @param line2Point1 线段2的其中一个端点
     * @param line2Point2 线段2的另一个端点
     */
    private Point2D[] lineFocusPoint(Point2D line1Point1, Point2D line1Point2, Point2D line2Point1, Point2D line2Point2) {
        Point2D result[] = new Point2D[1];
        double areaABC = trangleArea(line1Point1.getX(), line1Point2.getX(), line2Point1.getX(), line1Point1.getY(), line1Point2.getY(), line2Point1.getY());
        double areaABD = trangleArea(line1Point1.getX(), line1Point2.getX(), line2Point2.getX(), line1Point1.getY(), line1Point2.getY(), line2Point2.getY());
        if (Double.compare(areaABC * areaABD, 0) < 0) {
            double areaCDA = trangleArea(line2Point1.getX(), line2Point2.getX(), line1Point1.getX(), line2Point1.getY(), line2Point2.getY(), line1Point1.getY());
            double areaCDB = areaCDA + areaABC - areaABD;
            if (Double.compare(areaCDA * areaCDB, 0) < 0) {
                double t = areaCDA / (areaABD - areaABC);
                double focusX = line1Point1.getX() + t * (line1Point2.getX() - line1Point1.getX());
                double focusY = line1Point1.getY() + t * (line1Point2.getY() - line1Point1.getY());
                Point2D point2D = new Point2D.Double(focusX, focusY);
                result[0] = point2D;
            }
        }
        return result;
    }

    /**
     * 供lineFocusPoint调用的计算三角形面积的方法
     *
     * @param point1X 计算的焦点结果数组
     * @param point2X 圆角矩形中圆角弧的宽度
     * @param point3X 圆角矩形圆角弧的高度
     * @param point1Y 圆角矩形圆角弧的逆时针方向排在前面的点
     * @param point2Y 圆角矩形圆角弧的逆时针方向排在前一个点后面的点，且两个是同一个圆角弧
     * @param point3Y 导入的线段的斜率
     */
    private double trangleArea(double point1X, double point2X, double point3X, double point1Y, double point2Y, double point3Y) {
        double result = 0;
        result = (point1X - point3X) * (point2Y - point3Y) - (point1Y - point3Y) * (point2X - point3X);
        return result;
    }

    /**
     * 计算圆角弧与线段相交焦点的方法，因为是针对圆角矩形计算，所以圆角弧宽度长度一定要相等，否则不予以处理
     *
     * @param arcWidth  arcWidth为圆角弧的长度
     * @param arcHeight arcHeight为圆角弧的高度
     * @param pointSart 为按照逆时针，排在前面的圆弧角端点
     * @param pointEnd  同样是根据逆时针顺序排在后面的圆弧角端点，前者跟后者必须是同一个圆弧角上的端点
     * @param k         导入的线段的斜率
     * @param m         导入的线段的截距
     */
    private Point2D[] arcFocusPointForRoundRectangle(double arcWidth, double arcHeight, Point2D pointSart, Point2D pointEnd, double k, double m) {
        Point2D result[] = new Point2D[1];
        if (Double.compare(arcHeight, arcWidth) != 0) {
            return result;
        } else {
            int quard = 0;
            if (Double.compare(pointSart.getX(), pointEnd.getX()) == 1) {
                if (Double.compare(pointSart.getY(), pointEnd.getY()) == 1) {
                    quard = 2;
                } else {
                    quard = 1;
                }
            } else {
                if (Double.compare(pointSart.getY(), pointEnd.getY()) == 1) {
                    quard = 3;
                } else {
                    quard = 4;
                }
            }
            double centreX = 0;
            double centreY = 0;
            if (quard == 1 || quard == 3) {
                centreX = pointEnd.getX();
                centreY = pointSart.getY();
            } else if (quard == 2 || quard == 4) {
                centreX = pointSart.getX();
                centreY = pointEnd.getY();
            }

            double bPre = 2 * (k * m - centreX - k * centreY);
            double cPre = m * m - 2 * m * centreY + centreX * centreX + centreY * centreY - arcWidth * arcWidth;
            double aPre = 1 + k * k;
            double derTa = bPre * bPre - 4 * aPre * cPre;
            if (Double.compare(derTa, 0) == -1) {
                return result;
            } else if (Double.compare(derTa, 0) == 1) {
                double x1 = (0 - bPre + Math.sqrt(derTa)) / (2 * aPre);
                double y1 = k * x1 + m;
                double x2 = (0 - bPre - Math.sqrt(derTa)) / (2 * aPre);
                double y2 = k * x2 + m;
                if (isInArcRoundRectangleAngle(pointSart.getX(), pointEnd.getX(), quard, x1)) {
                    Point2D point2D1 = new Point2D.Double(x1, y1);
                    result[0] = point2D1;
                } else if (isInArcRoundRectangleAngle(pointSart.getX(), pointEnd.getX(), quard, x2)) {
                    Point2D point2D1 = new Point2D.Double(x2, y2);
                    result[0] = point2D1;
                }

            } else if (Double.compare(derTa, 0) == 0) {
                double x1 = (0 - bPre + Math.sqrt(derTa)) / (2 * aPre);
                double y1 = k * x1 + m;
                if (isInArcRoundRectangleAngle(pointSart.getX(), pointEnd.getX(), quard, x1)) {
                    Point2D point2D1 = new Point2D.Double(x1, y1);
                    result[0] = point2D1;
                }
            }
        }
        return result;
    }

    /**
     * 供arcFocusPointForRoundRectangle调用的判断计算出来的焦点是否在圆弧角范围内的方法
     *
     * @param stratX 圆弧逆时针方向起始点的X坐标
     * @param endX   同一个圆弧逆时针方向结尾点的X坐标
     * @param quard  当前圆弧所在象限
     * @param tempX  当前需要进行过滤的焦点的X坐标
     */
    private boolean isInArcRoundRectangleAngle(double stratX, double endX, int quard, double tempX) {
        boolean result = false;
        if (quard == 1 || quard == 2) {
            if (Double.compare(stratX, tempX) >= 0 && Double.compare(endX, tempX) <= 0) {
                result = true;
            }
        } else if (quard == 3 || quard == 4) {
            if (Double.compare(endX, tempX) >= 0 && Double.compare(stratX, tempX) <= 0) {
                result = true;
            }
        }
        return result;
    }

//    public static void main(String args[]) {
//        FocusPointCalculator focusPointCalculator = new FocusPointCalculator();
//        Ellipse2D ellipse2D = new Ellipse2D.Double(0, 0, 6, 4);
//        Point2D point2D1 = new Point2D.Double(1, 0);
//        Point2D point2D2 = new Point2D.Double(5, 4);
//        ArrayList<Point2D> result = focusPointCalculator.calculaor(ellipse2D, point2D1, point2D2);
//        System.out.println("椭圆与线段的焦点坐标......");
//        for (int i = 0; i < result.size(); i++) {
//            System.out.println(result.get(i).getX());
//            System.out.println(result.get(i).getY());
//        }
//
//        RoundRectangle2D roundRectangle2D = new RoundRectangle2D.Double(0, 0, 8, 4, 1, 1);
//        Point2D point1 = new Point2D.Double(-1, -5);
//        Point2D point2 = new Point2D.Double(1, 5);
//        result = focusPointCalculator.calculaor(roundRectangle2D, point1, point2);
//        System.out.println("圆角矩形与线段的焦点坐标......(不考虑圆角)");
//        for (int i = 0; i < result.size(); i++) {
//            System.out.println(result.get(i).getX());
//            System.out.println(result.get(i).getY());
//        }
//
//        RoundRectangle2D roundRectangle2D1 = new RoundRectangle2D.Double(0, 0, 4, 4, 1, 1);
//        Point2D pointNew1 = new Point2D.Double(2, 2);
//        Point2D pointNew2 = new Point2D.Double(0, -2);
//        result = focusPointCalculator.calculaor(roundRectangle2D1, pointNew1, pointNew2);
//        System.out.println("圆角矩形与线段的焦点坐标......(考虑圆角)");
//        for (int i = 0; i < result.size(); i++) {
//            System.out.println(result.get(i).getX());
//            System.out.println(result.get(i).getY());
//        }
//
//        RoundRectangle2D roundRectangle2D2 = new RoundRectangle2D.Double(0, 0, 8, 4, 1, 1);
//        Point2D point11 = new Point2D.Double(8, 3);
//        Point2D point21 = new Point2D.Double(-8, -1);
//        result = focusPointCalculator.calculaor(roundRectangle2D2, point21, point11);
//        System.out.println("圆角矩形与线段的焦点坐标......");
//        for (int i = 0; i < result.size(); i++) {
//            System.out.println(result.get(i).getX());
//            System.out.println(result.get(i).getY());
//        }
//    }
}

