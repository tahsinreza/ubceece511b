package ca.ubc.ece.eec511b.project.core;

import java.util.ArrayList;
import java.util.HashMap;

public class TestCaseNode {
	
	private String testCaseID;
	private int statementCount;
	private double statementCoverage;
	private String checkedMethoed;
	private String testSuiteName;
	
	public HashMap<String, String> stmCov; // <key:method><value:coverage>
	public ArrayList<String> stm; // Executed bytecode instructions
	
	public TestCaseNode(double statementCoverage) {		
		this.statementCoverage = statementCoverage;
		stmCov = new HashMap<String, String>();
		stm = new ArrayList<String>();
	}
	
	public TestCaseNode() {
		stmCov = new HashMap<String, String>();
		stm = new ArrayList<String>();
	}
	
	public TestCaseNode(String testCaseID) {
		stmCov = new HashMap<String, String>();
		this.testCaseID = testCaseID;
		stm = new ArrayList<String>();
	}
	
	public String getTestCaseID() {
		return testCaseID;
	}
	
	public void setTestSuiteName(String testSuiteName) {
		this.testSuiteName = testSuiteName;
	}
	
	public String getTestSuiteName() {
		return testSuiteName;
	}	
	
	public void setCheckedMethoed(String checkedMethoed) {
		this.checkedMethoed = checkedMethoed;
	}
	
	public String getCheckedMethoed() {
		return checkedMethoed;
	}
	
	public void setStatementCount(int statementCount) {
		this.statementCount = statementCount;
	}
	
	public int getStatementCount() {
		return statementCount;
	}
	
	public void setStatementCoverage(double statementCoverage) {
		this.statementCoverage = statementCoverage;
	}	
	
	public double getStatementCoverage() {
		return statementCoverage;
	}
}
