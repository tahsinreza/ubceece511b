package ca.ubc.ece.eec511b.project.core;

import org.jdom.Element;

public class Edge {
	public Node a;
	public Node b;
	public Element e;
	public double weight;
	public String directionType;
	
	// Later, make all the class instances private and write accessor methods. 
	
	public Edge(Element e) {
		this.e = e;
	}
	
	public Edge(Node a, Node b) {
		this.a = a;
		this.b = b;
	}
	
	public Edge(Node a, Node b, double weight) {
		this.a = a;
		this.b = b;
		this.weight = weight;
	}
	
	public Edge(Node a, Node b, double weight, String directionType) {
		this.a = a;
		this.b = b;
		this.weight = weight;
		this.directionType = directionType;
	}
}
