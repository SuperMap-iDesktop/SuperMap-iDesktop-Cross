package com.supermap.desktop.process.core;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by lixiaoyao on 2017/2/24.
 */
public class UniversalMatrix {
    private HashMap<String, SecondMatrix> twoDimensionArray = new HashMap<>();
    private HashMap<String, Object> nodeHashMap = new HashMap<>();

    /*
    * 根据节点名称获取节点
    *@param nodeName 节点名称
     */
    public Object getNode(String nodeName) {
        return nodeHashMap.get(nodeName);
    }

    /*
   * 加入节点
   *@param object 节点  每个process需存储需要一个标志，目前暂定process title
    */
    public void addNode(Object object) {
        if (!nodeHashMap.containsKey(((IProcess)object).getTitle())){
            nodeHashMap.put(((IProcess)object).getTitle(),object);
        }
    }

    /*
    * 加入节点       测试用
    */
    public void addNode(String nodeName) {
        if (!nodeHashMap.containsKey(nodeName)) {
            nodeHashMap.put(nodeName,(Object)nodeName);
        }
    }

    /*
    * 加入节点与节点之间的关系
    *@param nodeName 前一个节点名称
    * @param nodeName2 后一个节点名称
    * @param relationshipSate 两个节点间关系,必须大于0
     */
    public void addProcessRelationship(String nodeName, String nodeName2, Integer relationshipSate) {
        if (!isHasCurrentNode(nodeName)) {
            //Application.getActiveApplication().getOutput().output(nodeName+ProcessProperties.getString("String_IsNotHaveCurrentNode"));
            return;
        }
        if (!isHasCurrentNode(nodeName2)) {
            //Application.getActiveApplication().getOutput().output(nodeName2+ProcessProperties.getString("String_IsNotHaveCurrentNode"));
            return;
        }
        if (twoDimensionArray.containsKey(nodeName)) {
            twoDimensionArray.get(nodeName).twoDimension.put(nodeName2, relationshipSate);
        } else {
            SecondMatrix secondMatrix = new SecondMatrix();
            secondMatrix.twoDimension.put(nodeName2, relationshipSate);
            twoDimensionArray.put(nodeName, secondMatrix);
        }
    }

    private boolean isHasCurrentNode(String nodeName) {
        boolean result = true;
        if (!this.nodeHashMap.containsKey(nodeName)) {
            result = false;
        }
        return result;
    }

    /*
   * 获取当前节点的下一个节点，如果下一个节点同级的有两个或两个以上，则都返回
   *@param nodeName 节点名称
    */
    public ArrayList getNextNode(String nodeName) {
        ArrayList<String> result = new ArrayList<>();
        if (twoDimensionArray.containsKey(nodeName)) {
            SecondMatrix tempMatrix = twoDimensionArray.get(nodeName);
            Object[] arrayList = (Object[]) tempMatrix.twoDimension.keySet().toArray();
            for (int i = 0; i < arrayList.length; i++) {
                if (tempMatrix.twoDimension.get(arrayList[i]) > 0) {
                    result.add((String) arrayList[i]);
                }
            }
        }
        return result;
    }

    /*
   * 获取当前节点的上一个节点，如果上一个节点同级的有两个或两个以上，则都返回
   *@param nodeName 节点名称
    */
    public ArrayList getPreNode(String nodeName) {
        ArrayList<String> result = new ArrayList<>();
        Object[] arrayList = (Object[]) this.twoDimensionArray.keySet().toArray();
        for (int i = 0; i < arrayList.length; i++) {
            String tempString = (String) arrayList[i];
            SecondMatrix secondMatrix = this.twoDimensionArray.get(tempString);
            if (secondMatrix.twoDimension.containsKey(nodeName)) {
                result.add(tempString);
            }
        }
        return result;
    }

    /*
    * 获取当前节点以后的所有节点，直到某个节点的下个节点有两个或两个以上或者某个节点没有下一个节点，则返回
    *@param nodeName 节点名称
     */
    public ArrayList getAllNextNode(String nodeName) {
        ArrayList<String> result = new ArrayList<>();
        boolean state = true;
        while (state) {
            if (!twoDimensionArray.containsKey(nodeName)) {
                break;
            }
            SecondMatrix tempMatrix = twoDimensionArray.get(nodeName);
            if (tempMatrix.twoDimension == null) {
                break;
            }
            if (tempMatrix.twoDimension.size() >= 2) {
                state = false;
                break;
            } else {
                Object[] arrayList = (Object[]) tempMatrix.twoDimension.keySet().toArray();
                if (arrayList.length == 1) {
                    result.add((String) arrayList[0]);
                    nodeName = (String) arrayList[0];
                } else if (arrayList.length == 0) {
                    state = false;
                    break;
                }
            }
        }
        return result;
    }

    /*
    * 删除当前节点以及跟这个节点相关的某个节点的连接关系
    *@param nodeName1 当前节点名称
    * @parm nodeName2  相关节点名称
     */
    public boolean deleteNodeRealtionship(String nodeName1, String nodeName2) {
        boolean result = true;
        if (!isHasCurrentNode(nodeName1)) {
            //Application.getActiveApplication().getOutput().output(nodeName1+ProcessProperties.getString("String_IsNotHaveCurrentNode"));
            result = false;
        }
        if (!isHasCurrentNode(nodeName2)) {
            //Application.getActiveApplication().getOutput().output(nodeName2 +ProcessProperties.getString("String_IsNotHaveCurrentNode"));
            result = false;
        }
        if (result) {
            SecondMatrix tempMatrix = twoDimensionArray.get(nodeName1);
            if (!tempMatrix.twoDimension.containsKey(nodeName2)) {
                //Application.getActiveApplication().getOutput().output(nodeName1 +ProcessProperties.getString("String_NodeIsNotConntct")+nodeName2);
                result = false;
            } else {
                tempMatrix.twoDimension.remove(nodeName2);
            }
        }
        return result;
    }

    /*
    * 删除当前节点以及跟这个节点相关的所有连接关系
    *@param nodeName 节点名称
     */
    public boolean deleteNode(String nodeName) {
        boolean result = true;
        if (!isHasCurrentNode(nodeName)) {
            //Application.getActiveApplication().getOutput().output(nodeName+ProcessProperties.getString("String_IsNotHaveCurrentNode"));
            result = false;
        } else {
            this.nodeHashMap.remove(nodeName);
            if (this.twoDimensionArray.containsKey(nodeName)) {
                this.twoDimensionArray.remove(nodeName);
            }
            Object[] arrayList = (Object[]) this.twoDimensionArray.keySet().toArray();
            for (int i = 0; i < arrayList.length; i++) {
                String temp = (String) arrayList[i];
                SecondMatrix secondMatrix = this.twoDimensionArray.get(temp);
                if (secondMatrix.twoDimension.containsKey(nodeName)) {
                    secondMatrix.twoDimension.remove(nodeName);
                    if (secondMatrix.twoDimension.size() == 0) {
                        this.twoDimensionArray.remove(temp);
                    }
                }
            }
        }
        return result;
    }

    /*
    * 获取当前连接关系中所有起始节点
     */
    public ArrayList getAllStartNode() {
        ArrayList<String> result = new ArrayList<>();
        Object[] arrayList = (Object[]) this.nodeHashMap.keySet().toArray();
        for (int i = 0; i < this.nodeHashMap.size(); i++) {
            String tempName = (String) arrayList[i];
            if (!twoDimensionArray.containsKey(tempName)) {
                continue;
            } else {
                boolean isHas = true;
                for (SecondMatrix matrix : twoDimensionArray.values()) {
                    if (matrix.twoDimension.containsKey(tempName)) {
                        isHas = true;
                        break;
                    } else {
                        isHas = false;
                    }
                }
                if (!isHas) {
                    result.add(tempName);
                }
            }

        }
        return result;
    }

    /*
    * 获取当前连接关系中所有结尾节点
     */
    public ArrayList getAllEndNode() {
        ArrayList<String> result = new ArrayList<>();
        Object[] arrayList = (Object[]) this.nodeHashMap.keySet().toArray();
        for (int i = 0; i < this.nodeHashMap.size(); i++) {
            String tempName = (String) arrayList[i];
            if (twoDimensionArray.containsKey(tempName)) {
                continue;
            } else {
                boolean isHas = true;
                for (SecondMatrix matrix : twoDimensionArray.values()) {
                    if (matrix.twoDimension.containsKey(tempName)) {
                        isHas = false;
                        break;
                    } else {
                        isHas = true;
                    }
                }
                if (!isHas) {
                    result.add(tempName);
                }
            }

        }
        return result;
    }

    /*
    * 获取当前连接关系中所有未参与连接的节点
     */
    public ArrayList getAllSingleDogNode() {
        ArrayList<String> result = new ArrayList<>();
        Object[] arrayList = (Object[]) this.nodeHashMap.keySet().toArray();
        for (int i = 0; i < this.nodeHashMap.size(); i++) {
            String tempName = (String) arrayList[i];
            if (twoDimensionArray.containsKey(tempName)) {
                continue;
            } else {
                boolean isHas = true;
                for (SecondMatrix matrix : twoDimensionArray.values()) {
                    if (matrix.twoDimension.containsKey(tempName)) {
                        isHas = true;
                        break;
                    } else {
                        isHas = false;
                    }
                }
                if (!isHas) {
                    result.add(tempName);
                }
            }
        }
        return result;
    }

    private class SecondMatrix {
        HashMap<String, Integer> twoDimension = new HashMap<>();
    }


/*
*   下面测试的数据的拓扑图如下  a→b→c→d→e          f→g→h→e      e→i→l    e→j      x→y→z→w
*   前三个相互连接，后一个跟前三个没有连接关系，但处于同一个拓扑图中
 */

//    public static void main(String args[]) {
//        UniversalMatrix universalMatrix = new UniversalMatrix();
//        universalMatrix.addNode("a");
//        universalMatrix.addNode("b");
//        universalMatrix.addNode("c");
//        universalMatrix.addNode("d");
//        universalMatrix.addNode("e");
//        universalMatrix.addNode("f");
//        universalMatrix.addNode("g");
//        universalMatrix.addNode("h");
//        universalMatrix.addNode("i");
//        universalMatrix.addNode("j");
//        universalMatrix.addNode("l");
//        universalMatrix.addNode("x");
//        universalMatrix.addNode("y");
//        universalMatrix.addNode("z");
//        universalMatrix.addNode("w");
//
//        universalMatrix.addProcessRelationship("a", "b", 1);
//        universalMatrix.addProcessRelationship("b", "c", 1);
//        universalMatrix.addProcessRelationship("c", "d", 1);
//        universalMatrix.addProcessRelationship("d", "e", 1);
//        universalMatrix.addProcessRelationship("f", "g", 1);
//        universalMatrix.addProcessRelationship("g", "h", 1);
//        universalMatrix.addProcessRelationship("h", "e", 1);
//        universalMatrix.addProcessRelationship("e", "j", 1);
//        universalMatrix.addProcessRelationship("e", "i", 1);
//        universalMatrix.addProcessRelationship("i", "l", 1);
//        universalMatrix.addProcessRelationship("x", "y", 1);
//        universalMatrix.addProcessRelationship("y", "z", 1);
//        universalMatrix.addProcessRelationship("z", "w", 1);
//
//        ArrayList arrayList = universalMatrix.getNextNode("e");
//        System.out.println("e的下个节点为：");
//        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println(arrayList.get(i));
//        }
//
//        arrayList = universalMatrix.getPreNode("e");
//        System.out.println("e的上个节点为：");
//        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println(arrayList.get(i));
//        }
//
//        arrayList = universalMatrix.getAllStartNode();
//        System.out.println("当前连接关系中所有起始节点为：");
//        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println(arrayList.get(i));
//        }
//
//        arrayList = universalMatrix.getAllEndNode();
//        System.out.println("当前连接关系中所有结尾节点为：");
//        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println(arrayList.get(i));
//        }
//
//        arrayList = universalMatrix.getAllNextNode("a");
//        String temp = "a";
//        System.out.println("a所在串的所有节点为：");
//        for (int i = 0; i < arrayList.size(); i++) {
//            temp = temp + "→" + arrayList.get(i);
//        }
//        System.out.println(temp);
//
//        System.out.println("删除d,e连接后的，a所在串的所有节点为：");
//        universalMatrix.deleteNodeRealtionship("d", "e");
//        arrayList = universalMatrix.getAllNextNode("a");
//        temp = "a";
//        for (int i = 0; i < arrayList.size(); i++) {
//            temp = temp + "→" + arrayList.get(i);
//        }
//        System.out.println(temp);
//
//        arrayList = universalMatrix.getNextNode("x");
//        System.out.println("x的下个节点为：");
//        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println(arrayList.get(i));
//        }
//
//        System.out.println("x所在串所有节点为：");
//        arrayList = universalMatrix.getAllNextNode("x");
//        temp = "x";
//        for (int i = 0; i < arrayList.size(); i++) {
//            temp = temp + "→" + arrayList.get(i);
//        }
//        System.out.println(temp);
//
//        arrayList = universalMatrix.getAllSingleDogNode();
//        System.out.println("当前连接关系中所有未连接的节点为：");
//        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println(arrayList.get(i));
//        }
//
//        System.out.println("删除z节点后，x所在串所有节点为：");
//        universalMatrix.deleteNode("z");
//        arrayList = universalMatrix.getAllNextNode("x");
//        temp = "x";
//        for (int i = 0; i < arrayList.size(); i++) {
//            temp = temp + "→" + arrayList.get(i);
//        }
//        System.out.println(temp);
//
//        arrayList = universalMatrix.getAllStartNode();
//        System.out.println("当前连接关系中所有起始节点为：");
//        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println(arrayList.get(i));
//        }
//
//        arrayList = universalMatrix.getAllEndNode();
//        System.out.println("当前连接关系中所有结尾节点为：");
//        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println(arrayList.get(i));
//        }
//
//        arrayList = universalMatrix.getAllSingleDogNode();
//        System.out.println("当前连接关系中所有未连接的节点为：");
//        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println(arrayList.get(i));
//        }
//
//        arrayList = universalMatrix.getPreNode("y");
//        System.out.println("y的上个节点为：");
//        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println(arrayList.get(i));
//        }
//
//
//        arrayList = universalMatrix.getPreNode("w");
//        System.out.println("w的上个节点为：");
//        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println(arrayList.get(i));
//        }
//
//        arrayList = universalMatrix.getNextNode("w");
//        System.out.println("w的下个节点为：");
//        for (int i = 0; i < arrayList.size(); i++) {
//            System.out.println(arrayList.get(i));
//        }
//    }
}
