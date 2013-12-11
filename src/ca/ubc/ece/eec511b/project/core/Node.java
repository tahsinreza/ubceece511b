package ca.ubc.ece.eec511b.project.core;

import java.util.ArrayList;

import org.jdom.Element;

public class Node {
	public ArrayList<Edge> edgeList;
	public ArrayList<Node> neighbours;
	public Element e;
	public TestCaseNode testCaseNode;
	
	// Later, make all the class instances private and write accessor methods. 
		
	public Node(TestCaseNode testCaseNode) {
		this.testCaseNode = testCaseNode;
		edgeList = new ArrayList<Edge>();
		neighbours = new ArrayList<Node>();
	}
	
	// For the XML version
	public Node(Element e) {
		this.e = e;
		edgeList = new ArrayList<Edge>();
		neighbours = new ArrayList<Node>();
	}
	
	// Test
	public void printNeighbours() {
		for(int i = 0; i < neighbours.size(); i++) {
			Element nodeE = neighbours.get(i).e;
			System.out.println(nodeE.getAttributeValue("NAME"));
		}
	}
	
	// Test
	public void printNeighbours2() {
		for(int i = 0; i < neighbours.size(); i++) {
			TestCaseNode t = neighbours.get(i).testCaseNode;
			System.out.println(t.getStatementCoverage());
		}
	}
	
	// Test
	public void printEdgeList() {
		for(int i = 0; i < edgeList.size(); i++) {
			Element edgeE = edgeList.get(i).e;
			System.out.println(edgeE.getAttributeValue("NAME"));
		}
	}
	
	// Test
	public void printEdgeList2() {
		for(int i = 0; i < edgeList.size(); i++) {
			double edgeWeight = edgeList.get(i).weight;
			System.out.println(edgeWeight);
		}
	}
}
