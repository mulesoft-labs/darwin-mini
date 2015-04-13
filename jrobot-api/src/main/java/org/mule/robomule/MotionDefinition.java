/**
 * (c) 2003-2014 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package org.mule.robomule;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MotionDefinition {

    final int MILLIS_PER_FRAME = 8;
    final int UNIT_PADDING_TIME = 100;
    final int RECOVERY_TIME = 300;
    
    private Map<RobotMotion, Motion> motionMap = new HashMap<RobotMotion, Motion>();
    
    
    public static void main(String[] args){
        MotionDefinition motionDefinition = new MotionDefinition();
        motionDefinition.load(MotionDefinition.class.getClassLoader().getResourceAsStream("motionFile.xml"));
    }
    
    public MotionDefinition() {

    }

    public void load(InputStream inputStream) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();
            final NodeList callFlows = doc.getElementsByTagName("callFlow");
            for (int index = 0; index < callFlows.getLength(); index++) {
                Node callFlow = callFlows.item(index);
                if (callFlow.getNodeType() == Node.ELEMENT_NODE) {
                    Element callFlowElement = (Element) callFlow;
                    String callIndex = callFlowElement.getAttribute("callIndex");
                    String name = callFlowElement.getAttribute("flow");
                    
                    String path = "//Flow[@name='" + name + "']";
                    XPathExpression expr = xpath.compile(path + "/units/unit");
                    NodeList unitsNodes = (NodeList) expr.evaluate(doc,XPathConstants.NODESET);
                    int totalMotionTime = 0;
                    for (int indexUnit = 0; indexUnit < unitsNodes.getLength();indexUnit++)
                    {
                        Element unitElement = (Element) unitsNodes.item(indexUnit);
                        int exitSpeed = Integer.parseInt(unitElement.getAttribute("exitSpeed"));
                        int loop = Integer.parseInt(unitElement.getAttribute("loop"));
                        String pageName = unitElement.getAttribute("main");
                        XPathExpression expr2 = xpath.compile("//Page[@name ='" + pageName  + "']/steps/step[last()]");
                        Element stepElement = (Element) expr2.evaluate(doc,XPathConstants.NODE);
                        int lastFrame = Integer.parseInt(stepElement.getAttribute("frame"));
                        totalMotionTime += (lastFrame * MILLIS_PER_FRAME + UNIT_PADDING_TIME) * exitSpeed * loop; 
                    }
                    registerMotion(name, Integer.parseInt(callIndex),totalMotionTime);
                    
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerMotion(String name, Integer callIndex,Integer wait) {
        System.out.println(name + " - "+ callIndex + "-  "+wait);
        String realName = name.replaceAll(" ", "_");
        RobotMotion motionToRegister = RobotMotion.valueOf(realName);
        Motion motion = new Motion(callIndex,wait,realName);
        motionMap.put(motionToRegister, motion);
    }


    public Integer getActionCode(RobotMotion name) {
        if (motionMap.containsKey(name)) {
            return motionMap.get(name).getRobotId();
        } else {
            throw new RuntimeException("Invalid motion name " + name);
        }
    }
    
    public long getWaitTime(RobotMotion name){
        if (motionMap.containsKey(name)) {
            return motionMap.get(name).getWaitTime();
        } else {
            throw new RuntimeException("Invalid motion name " + name);
        }
    }
    
    
}
