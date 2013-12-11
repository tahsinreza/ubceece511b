package ca.ubc.ece.eec511b.project.core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ProjectController {	
	
	/**
	 * Create a test case node from a dynamic program slice
	 * @param fileName - The file name of the slice
	 * @param testCaseName - Unique test case ID  
	 * @param tokenCount - Property of the slice
	 * @return - a TestCaseNode object
	 * @throws Exception
	 */
	public static TestCaseNode createTestCaseNodeFromSlice(String fileName, String testCaseName, int tokenCount) throws Exception {			
		TestCaseNode t = new TestCaseNode(testCaseName); 
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = "";
		int lineCount = 0;
		while((line = br.readLine())!= null) {	
			lineCount++;
			String [] s = line.split(":| ");			
			if(s.length < 3)
				throw new Exception();			
			//String sA = s[0];
			//String sB = s[1];
			//String sC = s[2];
			//String sD = "";			
			//if(s.length == tokenCount) 
				//sD = s[3];			
			//String key = sB;
			//String value = line;
			//t.statemets.;			
			t.stm.add(line); // statement
			t.setStatementCount(lineCount);
			
			// What the test case checks is determined here (based on the information available on the slice)  
		    // Method level checks only (for now)
			// Single check for now (multiple checks in the future)
			// An assertion to be exact
			String filter_1 = "The dynamic slice for criterion";
			if(line.matches("(?i).*"+filter_1+".*")) {
				String [] s_1 = line.split(" [\\[]|]"); 
				//System.out.println(s_1[1]);
				t.setCheckedMethoed(s_1[1]);
			}
		}
		br.close();
		return t;
	}
	
	/**
	 * Compare two test cases based on Jaccard Coefficient
	 * Jaccard Coefficient, J(a,b) = a<intersection>b / a<union>b 
	 * Relation Coefficient, r(a,b) = J(a,b)
	 * @param a - a TestCaseNode object returned by createTestCaseNodeFromSlice
	 * @param b - a TestCaseNode object returned by createTestCaseNodeFromSlice
	 * @return - "-1.0" if there is no relation, "> 0.0" otherwise
	 * @throws Exception
	 */	
	public static double compareTestCaseNodes(TestCaseNode a, TestCaseNode b, char [] relationType) throws Exception {
		double relationCoefficient = -1.0;
		
		ArrayList<String> aStm = a.stm;	
		ArrayList<String> bStm = b.stm;
		
		Set<String> union = new HashSet<String>();
		union.addAll(aStm);		
		union.addAll(bStm);

		// Uni or bidirectional link
		int maxSize = 0;
		if(aStm.size() >  bStm.size())
			maxSize = aStm.size();
		else if(aStm.size() <  bStm.size())
			maxSize = bStm.size();
		
		if(maxSize < union.size())
			relationType[0] = 'b';
		// Uni or bidirectional link
					
		Set<String> intersection = new HashSet<String>();	
		intersection.addAll(aStm);
		intersection.retainAll(bStm);		
		
		if(union.size() < 1)
			throw new Exception();
		
		if(intersection.size() > 0) {
			double jaccardCoefficient = (double)intersection.size() / (double)union.size();		
			relationCoefficient = jaccardCoefficient; 
		}
		
		return relationCoefficient;
	}
	
	/**
	 * @param coverageOptionName
	 * @return
	 */
	public static ArrayList<Node> getMinimizedTestSuite3(String coverageOptionName) throws Exception {		
		return getMinimizedTestSuite3(createGraphOfTestCases(getTestCases(coverageOptionName)), 0, true);
	}
	
	/**
	 * 
	 * @param graph
	 * @param minimumCovergae
	 * @param isChecked
	 * @return
	 */
	public static ArrayList<Node> getMinimizedTestSuite3(Graph graph, double minimumCovergae, final boolean isChecked) {
		ArrayList<Node> nodes = graph.nodes; 
		ArrayList<Edge> edges = graph.edges; 
		
		ArrayList<Node> selectedTestCases = new ArrayList<Node>();
		selectedTestCases.clear();
		
		// If different test cases have different key sets then they should be combined.
		// This will give us all the possible "keys" (e.g., methods), whose coverage we 
		// are interested in. Now we can iterate over all the keys and identify test cases 
		// account for each individual key.
				
		// For now, getting the key set from a single node,
		// assuming all the nodes have identical key sets.
		// This will be improved in the future.	
		Node node = nodes.get(0);
		HashMap<String, String> stmCvg = node.testCaseNode.stmCov;		
		Set<String> keySet = stmCvg.keySet();
		Iterator<String> iterator = keySet.iterator();	
		
		// We perform coverage measure for each key
		while(iterator.hasNext()) {	
			String key = (String) iterator.next();
			//System.out.println(key);
			double coverageMeasure = 0.0;
			ArrayList<Node> tempTestCases = (ArrayList<Node>)nodes.clone();
			while(!tempTestCases.isEmpty()) {
		  //while(coverageMeasure < minimumCovergae) {
				if(tempTestCases.size() < 1) break;
				Node n = getMaxCoverageNode(tempTestCases, key, isChecked);	
				if(n == null) break;
				//System.out.println(n.testCaseNode.getTestCaseID());
				coverageMeasure = coverageMeasure + Double.parseDouble(n.testCaseNode.stmCov.get(key));				
				if(!selectedTestCases.contains(n))
					selectedTestCases.add(n);
				if(tempTestCases.contains(n))
					tempTestCases.remove(n);
				//System.out.println(coverageMeasure);
				
				// The extended algorithm
				if(!tempTestCases.isEmpty()) {
			  //if(coverageMeasure < minimumCovergae) {					
					for(int i = 0; i < n.edgeList.size(); i++) {
						//if(coverageMeasure >= minimumCovergae) break;
						if(tempTestCases.isEmpty()) break;
						
						Edge e = n.edgeList.get(i);
						if(e.directionType.equalsIgnoreCase("b")) {
							Node nNgb = null;						
							if(e.a == n) nNgb = e.b;
							else nNgb = e.a;
							
							if(isChecked) {
								if(nNgb.testCaseNode.getCheckedMethoed().equalsIgnoreCase(key)) {
									coverageMeasure = coverageMeasure + Double.parseDouble(nNgb.testCaseNode.stmCov.get(key));
									if(!selectedTestCases.contains(nNgb))
										selectedTestCases.add(nNgb);
									if(tempTestCases.contains(nNgb))
										tempTestCases.remove(nNgb);
								} else continue;
							} else {						
								coverageMeasure = coverageMeasure + Double.parseDouble(nNgb.testCaseNode.stmCov.get(key));
								if(!selectedTestCases.contains(nNgb))
									selectedTestCases.add(nNgb);
								if(tempTestCases.contains(nNgb))
									tempTestCases.remove(nNgb);
							}
						} //else continue;
						else if(e.directionType.equalsIgnoreCase("u")) {
							Node nNgb = null;						
							if(e.a == n) nNgb = e.b;
							else nNgb = e.a;
							if(tempTestCases.contains(nNgb))
								tempTestCases.remove(nNgb);
						} else continue;	
					}					
				}
			}
		}
		
		return selectedTestCases;
	}

	/**
	 * 
	 * @param coverageOptionName
	 * @param minimumCovergae
	 * @param isChecked
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Node> getMinimizedTestSuite2(String coverageOptionName, double minimumCovergae, final boolean isChecked) throws Exception {
		return getMinimizedTestSuite2(createGraphOfTestCases(getTestCases(coverageOptionName)), minimumCovergae, isChecked);
	}

	/**
	 * 
	 * @param graph
	 * @param minimumCovergae
	 * @param isChecked
	 * @return
	 */
	public static ArrayList<Node> getMinimizedTestSuite2(Graph graph, double minimumCovergae, final boolean isChecked) {
		ArrayList<Node> nodes = graph.nodes; 
		ArrayList<Edge> edges = graph.edges; 
		
		ArrayList<Node> selectedTestCases = new ArrayList<Node>();
		selectedTestCases.clear();
		
		// If different test cases have different key sets then they should be combined.
		// This will give us all the possible "keys" (e.g., methods), whose coverage we 
		// are interested in. Now we can iterate over all the keys and identify test cases 
		// account for each individual key.
				
		// For now, getting the key set from a single node,
		// assuming all the nodes have identical key sets.
		// This will be improved in the future.	
		Node node = nodes.get(0);
		HashMap<String, String> stmCvg = node.testCaseNode.stmCov;		
		Set<String> keySet = stmCvg.keySet();
		Iterator<String> iterator = keySet.iterator();	
		
		// We perform coverage measure for each key
		while(iterator.hasNext()) {	
			String key = (String) iterator.next();
			//System.out.println(key);
			double coverageMeasure = 0.0;
			ArrayList<Node> tempTestCases = (ArrayList<Node>)nodes.clone();
			while(coverageMeasure < minimumCovergae) {
				if(tempTestCases.size() < 1) break;
				Node n = getMaxCoverageNode(tempTestCases, key, isChecked);	
				if(n == null) break;
				//System.out.println(n.testCaseNode.getTestCaseID());
				coverageMeasure = coverageMeasure + Double.parseDouble(n.testCaseNode.stmCov.get(key));				
				if(!selectedTestCases.contains(n))
					selectedTestCases.add(n);
				if(tempTestCases.contains(n))
					tempTestCases.remove(n);
				//System.out.println(coverageMeasure);
				
				// The extended algorithm
				if(coverageMeasure < minimumCovergae) {					
					for(int i = 0; i < n.edgeList.size(); i++) {
						if(coverageMeasure >= minimumCovergae) break;
						
						Edge e = n.edgeList.get(i);
						if(e.directionType.equalsIgnoreCase("b")) {
							Node nNgb = null;						
							if(e.a == n) nNgb = e.b;
							else nNgb = e.a;
							
							if(isChecked) {
								if(nNgb.testCaseNode.getCheckedMethoed().equalsIgnoreCase(key)) {
									coverageMeasure = coverageMeasure + Double.parseDouble(nNgb.testCaseNode.stmCov.get(key));
									if(!selectedTestCases.contains(nNgb))
										selectedTestCases.add(nNgb);
									if(tempTestCases.contains(nNgb))
										tempTestCases.remove(nNgb);
								} else continue;
							} else {						
								coverageMeasure = coverageMeasure + Double.parseDouble(nNgb.testCaseNode.stmCov.get(key));
								if(!selectedTestCases.contains(nNgb))
									selectedTestCases.add(nNgb);
								if(tempTestCases.contains(nNgb))
									tempTestCases.remove(nNgb);
							}
						} else continue;	
					}
					
				}
			}
		}
			
		return selectedTestCases;
	}
	
	/**
	 * 
	 * @param coverageOptionName
	 * @param minimumCovergae
	 * @param isChecked
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Node> getMinimizedTestSuite(String coverageOptionName, double minimumCovergae, final boolean isChecked) throws Exception {		
		return getMinimizedTestSuite(createGraphOfTestCases(getTestCases(coverageOptionName)), minimumCovergae, isChecked);
	}
	
	/**
	 * 
	 * @param graph
	 * @param minimumCovergae
	 * @param isChecked
	 * @return
	 */
	public static ArrayList<Node> getMinimizedTestSuite(Graph graph, double minimumCovergae, final boolean isChecked) {
		ArrayList<Node> nodes = graph.nodes; 
		ArrayList<Edge> edges = graph.edges; 
					
		ArrayList<Node> selectedTestCases = new ArrayList<Node>();
		selectedTestCases.clear(); // making sure the list is empty
			
		// If different test cases have different key sets then they should be combined.
		// This will give us all the possible "keys" (e.g., methods), whose coverage we 
		// are interested in. Now we can iterate over all the keys and identify test cases 
		// account for each individual key.
				
		// For now, getting the key set from a single node,
		// assuming all the nodes have identical key sets.
		// This will be improved in the future.		
		Node node = nodes.get(0);
		HashMap<String, String> stmCvg = node.testCaseNode.stmCov;		
		Set<String> keySet = stmCvg.keySet();
		Iterator<String> iterator = keySet.iterator();	
		
		// We perform coverage measure for each key
		while(iterator.hasNext()) {			
			String key = (String) iterator.next();
			//System.out.println(key);
			double coverageMeasure = 0.0;
			ArrayList<Node> tempTestCases = (ArrayList<Node>)nodes.clone();
			while(coverageMeasure < minimumCovergae) {
				if(tempTestCases.size() < 1) break;
				Node n = getMaxCoverageNode(tempTestCases, key, isChecked);	
				if(n == null) break;
				//System.out.println(n.testCaseNode.getTestCaseID());
				coverageMeasure = coverageMeasure + Double.parseDouble(n.testCaseNode.stmCov.get(key));
				if(!selectedTestCases.contains(n))
					selectedTestCases.add(n);
				tempTestCases.remove(n);
				//System.out.println(coverageMeasure);
			}		
		}	
		
		return selectedTestCases;
	}
	
	/**
	 * Returns the current max coverage node
	 * @param nodes
	 * @param key
	 * @param isChecked
	 * @return
	 */
	public static Node getMaxCoverageNode(ArrayList<Node> nodes, String key, final boolean isChecked) {		
		if(isChecked) {
			Node maxCoverageNode = null;
			double maxCoverage = 0.0;
			for(int i = 0; i < nodes.size(); i++) {
				Node n = nodes.get(i);
				if(n.testCaseNode.getCheckedMethoed().equalsIgnoreCase(key)) {
					maxCoverageNode = n;
					HashMap<String, String> maxNodeStmCvg = maxCoverageNode.testCaseNode.stmCov;	
					String value = (String)maxNodeStmCvg.get(key);
					maxCoverage = Double.parseDouble(value);
					break;
				}	
			}
			if(maxCoverageNode == null) return maxCoverageNode;
			
			for(int i = 0; i < nodes.size(); i++) {
				Node n = nodes.get(i);	
				if(n.testCaseNode.getCheckedMethoed().equalsIgnoreCase(key)) {
					HashMap<String, String> sC = n.testCaseNode.stmCov;			
					String v = (String)sC.get(key);
					double d = Double.parseDouble(v);
					if(d > maxCoverage) {
						maxCoverageNode = n;
						maxCoverage = d;
					}
				} else continue;
			}
			return maxCoverageNode;
			
		} else {
			Node maxCoverageNode = nodes.get(0);
			HashMap<String, String> maxNodeStmCvg = maxCoverageNode.testCaseNode.stmCov;		
			String value = (String)maxNodeStmCvg.get(key);
			double maxCoverage = Double.parseDouble(value);
			
			for(int i = 0; i < nodes.size(); i++) {
				Node n = nodes.get(i);
				HashMap<String, String> sC = n.testCaseNode.stmCov;			
				String v = (String)sC.get(key);
				double d = Double.parseDouble(v);
				if(d > maxCoverage) {
					maxCoverageNode = n;
					maxCoverage = d;
				}
			}
			return maxCoverageNode; 
		}
	}
		
	/**
	 * Determine if two nodes are neighbours in the graph 
	 * @param a
	 * @param b
	 * @param edges
	 * @return
	 */
	public static boolean areNeighbours(Node a, Node b, ArrayList<Edge> edges) {		
		for(int i = 0; i < edges.size(); i++) {
			Edge e = edges.get(i);
			Node x = e.a; Node y = e.b;
			if((x == a && y == b) || (x == b && y == a)) {
				return true;				
			}	
		}	
		return false;
	}
	
	/**
	 * Create a graph of test cases
	 * @param testCaseNodes
	 * @return
	 * @throws Exception
	 */
	public static Graph createGraphOfTestCases(ArrayList<TestCaseNode> testCaseNodes) throws Exception {
		ArrayList<Node> nodes = new ArrayList<Node>(); 
		ArrayList<Edge> edges = new ArrayList<Edge>(); 
		
		// Create the graph nodes
		for(int i = 0; i < testCaseNodes.size(); i++) {
			nodes.add(new Node(testCaseNodes.get(i)));
		}
		
		// Create the graph
		// Actually, if two nodes are related, 
		// connect the already created nodes by creating an edge,
		// and update each node's neighbour and edge lists 
		for(int i = 0; i < nodes.size(); i++) {
			Node a = nodes.get(i);
			TestCaseNode tA = a.testCaseNode;
			for(int j = 0; j < nodes.size(); j++) {
				Node b = nodes.get(j);
				TestCaseNode tB = b.testCaseNode;
				if(a!=b) {	
					if (!areNeighbours(a, b, edges)) {
						char [] relationType = {'u'}; // By default, a link is unidirectional 
						double rAB = compareTestCaseNodes(tA, tB, relationType);
						System.out.print(tA.getTestCaseID() + " ");
						System.out.print(rAB + " " + relationType[0] + " ");
						System.out.println(tB.getTestCaseID() + " ");
						if(rAB > 0) {
							Edge e = new Edge(a, b, rAB, ("" + relationType[0]) );
							edges.add(e);
							
							a.neighbours.add(b); 
							b.neighbours.add(a); 
							
							a.edgeList.add(e);  
							b.edgeList.add(e);
						}
					}
				}
			}			
		}	
		return new Graph(nodes, edges);
	}	
	
	/**
	 * Obtain test cases from slice traces	
	 * @param coverageOptionName
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<TestCaseNode> getTestCases(String coverageOptionName) throws Exception {
		if(coverageOptionName.equalsIgnoreCase("Statement Coverage")) {
			return getTestCases1();
		} else if(coverageOptionName.equalsIgnoreCase("Def-use Pair Count")) {
			return getTestCases2();
		} else return null;
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<TestCaseNode> getTestCases() throws Exception {
		return getTestCases1(); // Statement Coverage
	}
	
	/**
	 * Obtain test cases from slice traces	
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<TestCaseNode> getTestCases2() throws Exception {
		// Create test case objects from the slice traces		
		String fileDir = "C:\\local\\javaslicer\\filtered_slice\\";		
		String [] assFiles = {
				"slicer_dump_A.doc_filtered",
				"slicer_dump_B.doc_filtered",
				"slicer_dump_C.doc_filtered",
				"slicer_dump_D.doc_filtered",
				"slicer_dump_E.doc_filtered",
				"slicer_dump_F.doc_filtered",
				"slicer_dump_G.doc_filtered",
		};	
		String [] assName = {"A","B","C","D","E","F","G"};
		String [] usFiles = {
				"slicer_dump_1.doc_filtered",
				"slicer_dump_2.doc_filtered",
				"slicer_dump_3.doc_filtered",
		};	
		String [] usName = {"1","2","3"};
		String [] umFiles = {
				"slicer_dump_a_filtered",
				"slicer_dump_b_filtered",
				"slicer_dump_c_filtered",
				"slicer_dump_d_filtered",
				"slicer_dump_e_filtered",
		};	
		String [] umName = {"aa","bb","cc","dd","ee"};
		
		ArrayList<TestCaseNode> testCaseNodes = new ArrayList<TestCaseNode>();
		for(int i = 0; i < assFiles.length; i++) {			
			testCaseNodes.add(createTestCaseNodeFromSlice(fileDir + assFiles[i], assName[i], 4));
		}	
		for(int i = 0; i < usFiles.length; i++) {
			testCaseNodes.add(createTestCaseNodeFromSlice(fileDir + usFiles[i], usName[i], 4));
		}	
		for(int i = 0; i < umFiles.length; i++) {
			testCaseNodes.add(createTestCaseNodeFromSlice(fileDir + umFiles[i], umName[i], 4));
		}			

		// Add coverage information to test case objects from trace
		addDefUsePairCountInfo(testCaseNodes);
		
		return testCaseNodes;
	}
	
	/**
	 * Add Def-use Pair Count Information
	 * @param testCaseNodes
	 * @throws Exception
	 */
	public static void addDefUsePairCountInfo(ArrayList<TestCaseNode> testCaseNodes) throws Exception {
		String fileDir = "C:\\local\\javaslicer\\coverage_trace\\";
		String fileName = fileDir + "defuse_pair_count";
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = "";
		while((line = br.readLine())!= null) {			
			String [] s = line.split(",");
			// s[0] - Test case name
			// s[i>0] - Fully qualified method signature:coverage value
			for(int i = 1; /* important */ i < s.length; i++) {
				String [] s_1 =  s[i].split(":");
				String key = s_1[0];
				String value = s_1[1];
				// System.out.println("key : " + key + " value: " + value);
				// Update the corresponding test case object 
				// with the coverage information
				for(int j = 0; j < testCaseNodes.size(); j++) {
					if(testCaseNodes.get(j).getTestCaseID().equals(s[0])) {
						testCaseNodes.get(j).stmCov.put(key, value);
					}						
				}
			}
		}	
		br.close();
	}
	
	/**
	 * Obtain test cases from slice traces	
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<TestCaseNode> getTestCases1() throws Exception {
		// Create test case objects from the slice traces		
		String fileDir = "C:\\local\\javaslicer\\filtered_slice\\";		
		String [] assFiles = {
				"slicer_dump_A.doc_filtered",
				"slicer_dump_B.doc_filtered",
				"slicer_dump_C.doc_filtered",
				"slicer_dump_D.doc_filtered",
				"slicer_dump_E.doc_filtered",
				"slicer_dump_F.doc_filtered",
				"slicer_dump_G.doc_filtered",
		};	
		String [] assName = {"A","B","C","D","E","F","G"};
		String [] usFiles = {
				"slicer_dump_1.doc_filtered",
				"slicer_dump_2.doc_filtered",
				"slicer_dump_3.doc_filtered",
		};	
		String [] usName = {"1","2","3"};
		String [] umFiles = {
				"slicer_dump_a_filtered",
				"slicer_dump_b_filtered",
				"slicer_dump_c_filtered",
				"slicer_dump_d_filtered",
				"slicer_dump_e_filtered",
		};	
		String [] umName = {"aa","bb","cc","dd","ee"};
		
		ArrayList<TestCaseNode> testCaseNodes = new ArrayList<TestCaseNode>();
		for(int i = 0; i < assFiles.length; i++) {			
			testCaseNodes.add(createTestCaseNodeFromSlice(fileDir + assFiles[i], assName[i], 4));
		}	
		for(int i = 0; i < usFiles.length; i++) {
			testCaseNodes.add(createTestCaseNodeFromSlice(fileDir + usFiles[i], usName[i], 4));
		}	
		for(int i = 0; i < umFiles.length; i++) {
			testCaseNodes.add(createTestCaseNodeFromSlice(fileDir + umFiles[i], umName[i], 4));
		}			

		// Add coverage information to test case objects from trace
		addStatementCoverageInfo(testCaseNodes);
		
		return testCaseNodes;
	}
	
	/**
	 * Add Statement Coverage Information
	 * @param testCaseNodes
	 * @throws Exception
	 */
	public static void addStatementCoverageInfo(ArrayList<TestCaseNode> testCaseNodes) throws Exception {
		String fileDir = "C:\\local\\javaslicer\\coverage_trace\\";
		String fileName = fileDir + "statement_coverage";
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = "";
		while((line = br.readLine())!= null) {			
			String [] s = line.split(",");
			// s[0] - Test case name
			// s[i>0] - Fully qualified method signature:coverage value
			for(int i = 1; /* important */ i < s.length; i++) {
				String [] s_1 =  s[i].split(":");
				String key = s_1[0];
				String value = s_1[1];
				// System.out.println("key : " + key + " value: " + value);
				// Update the corresponding test case object 
				// with the coverage information
				for(int j = 0; j < testCaseNodes.size(); j++) {
					if(testCaseNodes.get(j).getTestCaseID().equals(s[0])) {
						testCaseNodes.get(j).stmCov.put(key, value);
					}						
				}
			}
		}	
		br.close();
	}
	
	public static String[] getTestSuitesName() {
		String [] testSuitesName = {"TestSort [A-G]", "TestSwap [1-3]", "TestMinIndex [aa-ee]"};
		return testSuitesName;
	}	
}
