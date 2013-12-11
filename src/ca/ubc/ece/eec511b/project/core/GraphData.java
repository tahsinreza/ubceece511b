package ca.ubc.ece.eec511b.project.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

public class GraphData {
	
	private GraphDataXML graphDataXML;
	
	/**
	 * 
	 * @param fileName
	 */
	public GraphData(String fileName) {
		this.graphDataXML = new GraphDataXML();
		Document doc = graphDataXML.loadXMLFile(fileName);
		graphDataXML.setDocument(doc);
	}
	
	/**
	 * 
	 */
	public GraphData() {
		this.graphDataXML = new GraphDataXML();
	}	
	
	/**
	 * 
	 * @return
	 */
	public GraphDataXML getGraphDataXML() {
		return this.graphDataXML;
	}
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	
	// Tests
	
	/**
	 * 
	 * @param xmlParentElement
	 */
	public void createGraphDataXML(Object xmlParentElement) {
		/* Write to XML document */
		Object xmlElement = graphDataXML.addElement(xmlParentElement, "NODE_TAG");
		graphDataXML.addAttribute(xmlElement, "NAME", "TEST_ONE");		
		Object xmlElement_0 = graphDataXML.addElement(xmlParentElement, "NODE_TAG");
		graphDataXML.addAttribute(xmlElement_0, "NAME", "TEST_TWO");
		graphDataXML.addAttribute(xmlElement_0, "COVERAGE", "80%");
		/* Write to XML document*/
	}
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	
	public void createXMLFile(GraphDataXML graphDataXML) {
		
	}
			
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
				
	public ArrayList<Node> nodes; // The graph
	public ArrayList<Edge> edges; // The graph
		
	public void createGraph(GraphDataXML graphDataXML) {		
		
		nodes = new ArrayList<Node>();
		edges = new ArrayList<Edge>();
		
		List edgeList = graphDataXML.getElementsByTagName(graphDataXML.getRootElement(), "EDGE");
		Iterator edgeListItr = edgeList.iterator();
		while(edgeListItr.hasNext()) {
			Element edge = (Element)edgeListItr.next();
			
			// Edge object
			Edge e = new Edge(edge);
			edges.add(e);
			// Edge object
			
			List edgeAttrList = edge.getAttributes();
			String naName = edge.getAttribute("NA").getValue();
			String nbName = edge.getAttribute("NB").getValue();			
			Element na = getElementByTagNameAttributeValue(graphDataXML, "NODE", "NAME", naName);
			Element nb = getElementByTagNameAttributeValue(graphDataXML, "NODE", "NAME", nbName);
				
			// Node object
			Node a;
			Node b;
				
			if((a = getNodeIfExists(naName, nodes)) == null) {
				a = new Node(na);
				nodes.add(a);					
			}	
				
			if((b = getNodeIfExists(nbName, nodes)) == null) {
				b = new Node(nb);
				nodes.add(b);					
			}	
				
			e.a = a; // Edge
			e.b = b; // Edge
			
			a.neighbours.add(b); // Node
			b.neighbours.add(a); // Node
				
			a.edgeList.add(e);  // Node
			b.edgeList.add(e);  // Node										
			// Node object
				
			//System.out.println(nb.getAttributeValue("TYPE"));
		}
	}	
	
	public Element getElementByTagNameAttributeValue(GraphDataXML graphDataXML, String tagName, String AttributeName, String AttributeValue) {
		Element element = null;
		List tl = graphDataXML.getElementsByTagName(graphDataXML.getRootElement(), tagName);
		Iterator tlItr = tl.iterator();
		while(tlItr.hasNext()) {
			Element e = (Element)tlItr.next();
			List attrList = e.getAttributes();
			String attrName = e.getAttribute(AttributeName).getName();
			if(attrName != null && e.getAttribute(AttributeName).getValue().equalsIgnoreCase(AttributeValue)) {						
				element = e;						
			}		
		}
		return element;
	}
	
	public Node getNodeIfExists(String uniqueID, ArrayList<Node> nodes) {		
		for(int i = 0; i < nodes.size(); i++) {
			Element nodeE = nodes.get(i).e;
			String nodeName = nodeE.getAttributeValue("NAME");
			if(uniqueID.equalsIgnoreCase(nodeName)) {
				return nodes.get(i);
			}
		}
		return null;
	}	
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	
	// Tests
	
	public void printNodes() {
		for(int i = 0; i < nodes.size(); i++) {
			Element nodeE = nodes.get(i).e;
			System.out.println(nodeE.getAttributeValue("NAME"));
		}
	}
	
	public void printEdges() {
		for(int i = 0; i < edges.size(); i++) {
			Element edgeE = edges.get(i).e;
			System.out.println("> " + edgeE.getAttributeValue("NAME"));
			System.out.println("a: " + edges.get(i).a.e.getAttributeValue("NAME"));
			System.out.println("b: " + edges.get(i).b.e.getAttributeValue("NAME"));
		}
	}
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	
	// Perform query on the graph
	
	public void runQuery() {
		ArrayList<Node> selectedTestCases = new ArrayList<Node>();
		
		double minCoverage = 26.0;
		double coverage = 0.0;
				
		for(int i = 0; i < nodes.size(); i++) {
			if(nodes.get(i).e.getAttribute("TYPE").getValue().equalsIgnoreCase("TEST")) {
				double cvrg = Double.valueOf(nodes.get(i).e.getAttribute("VALUE").getValue());
				//System.out.println("> " + cvrg);
				//System.out.println("> " + hasTestCaseNeigbour(nodes.get(i)));
				if(!hasTestCaseNeigbour(nodes.get(i))) {
					if(!(coverage >= minCoverage)) {
						selectedTestCases.add(nodes.get(i));
						coverage += cvrg;
						System.out.println(nodes.get(i).e.getAttribute("NAME").getValue());
					}
				}
			}		
		}
	}
	
	public boolean hasTestCaseNeigbour(Node node) {
		for(int i = 0; i < node.neighbours.size(); i++) {
			if(node.neighbours.get(i).e.getAttribute("TYPE").getValue().equalsIgnoreCase("TEST"))
				return true;
		}
		return false;
	}
	
	public ArrayList<Node> sortTestCasesByValue(ArrayList<Node> list) {
		ArrayList<Node> sortedList = new ArrayList<Node>();
		ArrayList<Node> cloneList = new ArrayList<Node>();
		
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).e.getAttribute("TYPE").getValue().equalsIgnoreCase("TEST")) {
				cloneList.add(list.get(i));
			}
		}	
		//System.out.println(cloneList.size());
		
		while(cloneList.size() > 0) {
			Node n = getMinValueNode(cloneList);
			sortedList.add(n);
			cloneList.remove(n);			
		}
		
		// Test
		/*for(int i = 0; i < sortedList.size(); i++) {
			Element nodeE = sortedList.get(i).e;
			System.out.println(nodeE.getAttribute("VALUE").getValue());
		}*/
		
		return sortedList;
	}
	
	public Node getMinValueNode(ArrayList<Node> list) {
		Node min = null;
		double minValue = 0.0;
		if(list.size() > 0) { 
			min = list.get(0);
			minValue = Double.valueOf(min.e.getAttribute("VALUE").getValue());
		}	
		for(int i = 0; i < list.size(); i++) {
			double val = Double.valueOf(list.get(i).e.getAttribute("VALUE").getValue()); 
			if(val < minValue) {
				min = list.get(i);
				minValue = val;
			}
		}
		return min;
	}	
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	
	public void processGraph(GraphDataXML graphDataXML) {
		ArrayList<Element> testNodeList = new ArrayList<Element>();
		List nodeList = graphDataXML.getElementsByTagName(graphDataXML.getRootElement(), "NODE");
		Iterator nodeListItr = nodeList.iterator();
		while(nodeListItr.hasNext()) {
			Element node = (Element)nodeListItr.next();
			List nodeAttrList = node.getAttributes();
			String attrName = node.getAttribute("TYPE").getName();
			if(attrName != null && node.getAttribute("TYPE").getValue().equalsIgnoreCase("TEST")) {						
				testNodeList.add(node);						
			}		
		}
		
		for(int i = 0; i < testNodeList.size(); i++) {
			Element node = testNodeList.get(i); 
			String nodeName  = node.getAttribute("NAME").getValue();
			List edgeList = node.getChildren("ARC");
			for(int j = 0; j<testNodeList.size(); j++) {
				Element tempNode = testNodeList.get(j); 
				String teamNodeName  = node.getAttribute("NAME").getValue();
				if(!(nodeName.equalsIgnoreCase(teamNodeName))) {
					List tempRdgeList = node.getChildren("ARC");	
					
				} else
					continue;
			}
		}
	}
	
	/**
	 * Return a list of related edges	
	 * @param edgeList
	 * @param tempRdgeList
	 * @return
	 */
	public boolean isRelated(List edgeList, List tempRdgeList) {
		return false;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("GraphData");	
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
				
		// Write to XML file
		String gDataFileName = "C:/local/3_graph_data_xml.XML";
		GraphData gD = new GraphData();		
		GraphDataXML gDXML = gD.getGraphDataXML();
		//graphData.createGraphDataXML(graphDataXML.getRootElement());
		//System.out.println(graphDataXML.getRootElement().getName()); // GRAPH
		//graphDataXML.getElementsByTagName(graphDataXML.getRootElement(), "NODE_TAG");
		gDXML.writeDocumentToFile(gDXML.getDocument(), gDataFileName);
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
		
		// Read XML file
		String graphDataFileName = "C:/local/2_graph_data_xml.XML";
		GraphData graphData = new GraphData(graphDataFileName);
		GraphDataXML graphDataXML = graphData.getGraphDataXML();
		//System.out.println(graphDataXML.getRootElement().getName());
		
		/*
		List l = graphDataXML.getElementsByTagName(graphDataXML.getRootElement(), "NODE");
		
		double coverage = 75;
		
		Iterator lIterator = l.iterator();
		while(lIterator.hasNext()) {
			Element e = (Element)lIterator.next();
			List al = e.getAttributes();
				String aN = e.getAttribute("TYPE").getName();
				if(aN != null && e.getAttribute("TYPE").getValue().equalsIgnoreCase("TEST")) {
						System.out.println(al.toString());
				}		
		}		
		System.out.println(l.toString());
		*/
		
		//graphData.processGraph(graphDataXML);		
		//System.out.println(graphData.getElementByTagNameAttributeValue(graphDataXML, "EDGE", "TYPE", "TT").getAttributeValue("NA"));
		
		//------//
		
		// Create graph object		
		graphData.createGraph(graphDataXML);			
		//graphData.printNodes();
		//graphData.printEdges();
		//graphData.nodes.get(3).printNeighbours();
		//System.out.println(graphData.nodes.get(1).edgeList.get(1).e.getAttributeValue("NAME")); 
		//graphData.runQuery();
		
		graphData.sortTestCasesByValue(graphData.nodes);
		 
		graphDataXML.sampleOutput(); 		
	}
}
