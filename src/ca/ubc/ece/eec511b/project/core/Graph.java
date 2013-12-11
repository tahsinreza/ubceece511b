package ca.ubc.ece.eec511b.project.core;

import java.util.ArrayList;

public class Graph {
	public ArrayList<Node> nodes; 
	public ArrayList<Edge> edges;
	
	public Graph(ArrayList<Node> nodes, ArrayList<Edge> edges) {
		this.nodes = nodes;
		this.edges = edges; 
	}
}
