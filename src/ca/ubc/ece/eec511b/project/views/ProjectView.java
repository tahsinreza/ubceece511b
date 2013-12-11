package ca.ubc.ece.eec511b.project.views;


import java.awt.Color;
import java.awt.Event;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;

import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormAttachment;

//import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
//import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;

import org.eclipse.ui.part.ViewPart;

import ca.ubc.ece.eec511b.project.core.*;

public class ProjectView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "ca.ubc.ece.eec511b.project.views.ProjectView";

	private static String EMPTYSTRING = "";
	private static int MARGIN = 5;
	private static int FIFTY_PERCENT = 50;
	private static int ONE_HUNDRED_PERCENT = 100;
	
	private List testSuiteList; 
	private Button[] selectionOptions; 
	private Combo[] comboBoxsMin;	
	private String coverageOptionName;
	private String minCoverage;	
	private Button generateTestSuite;
	private List newTestSuiteList; 
	private Button[] queryOptions;
		
	private Text sourceFileText, targetFileText;
	private List todoItemsList;
	private Button  sourceFileButton, generateButton, targetFileButton, compareButton;
	private Label label_1;	
	private Composite parent;
		
	private String sourceFileName;
	private String targetFileName;
			
	public  ProjectView(){		
	}
		
	@Override
	public void setFocus() {
	}

	private void setParent(Composite parent) {
		this.parent = parent;
	}

	private Composite getParent() {
		return parent;
	}

	private void showMessage(String message) {
		MessageDialog.openInformation(
			getParent().getShell(),
			"Message",
			message);
	}
	
	public void createPartControl(Composite parent) {
		FormData data;		
		FormLayout layout= new FormLayout();
		setParent(parent);

		layout.marginWidth = MARGIN;
		layout.marginHeight = MARGIN;
		parent.setLayout(layout);
		
		// List of Test Suites
		testSuiteList = new List(getParent(), SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
				
		String [] testSuiteName = ProjectController.getTestSuitesName();
		for(int i = 0; i < testSuiteName.length; i++ ) {
			testSuiteList.add(testSuiteName[i]);
		}
		
		data = new FormData();	
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(30, 0);
		data.bottom = new FormAttachment(40,0);
		testSuiteList.setLayoutData(data);
		
		testSuiteList.addSelectionListener(new TodoItemsListSelectionListener());
		testSuiteList.setEnabled(true);
		
		// Coverage Types		
		String[] selectionOptionsName = {
				"Statement Coverage", "Branch Coverage",
				"Def-use Pair Count"
		};
		selectionOptions = new Button[selectionOptionsName.length];
		comboBoxsMin = new Combo[selectionOptionsName.length];		
		minCoverage = "0";
		coverageOptionName = "";
		
		// Selection Options 
		for(int i = 0; i < selectionOptions.length; i++) {
			selectionOptions[i] = new Button(getParent(), SWT.CHECK);			
			selectionOptions[i].setText(selectionOptionsName[i]);
			data = new FormData();	
			if(i == 0) 
				data.top = new FormAttachment(MARGIN - 3, MARGIN);
			else
				data.top = new FormAttachment(selectionOptions[i-1], MARGIN);
			data.left = new FormAttachment(testSuiteList, MARGIN);
			selectionOptions[i].setLayoutData(data);				
			selectionOptions[i].setEnabled(true);
			boolean minMaxComboBoxEnabled = selectionOptions[i].getSelection();	
						
			// Combobox Min
			
			Label label_min = new Label(getParent(), SWT.PUSH);
			label_min.setText("Min");
			data = new FormData();	
			if(i == 0) 
				data.top = new FormAttachment(MARGIN - 3, MARGIN);
			else
				data.top = new FormAttachment(selectionOptions[i-1], MARGIN);
			data.left = new FormAttachment(selectionOptions[0], 5*MARGIN);
			//data.right = new FormAttachment(ONE_HUNDRED_PERCENT, 0);
			label_min.setLayoutData(data);		
			label_min.setEnabled(minMaxComboBoxEnabled);
			
			comboBoxsMin[i] = new Combo(getParent(), SWT.PUSH);		
			for(int j = 0; j < 100; j++) {
				comboBoxsMin[i].add(Integer.toString(j));
			}	
			data = new FormData();	
			if(i == 0) 
				data.top = new FormAttachment(MARGIN - 3, MARGIN);
			else
				data.top = new FormAttachment(selectionOptions[i-1], MARGIN);
			data.left = new FormAttachment(label_min, MARGIN);		
			comboBoxsMin[i].addSelectionListener(new ComboBoxSelectionListener(comboBoxsMin[i]));
			comboBoxsMin[i].setLayoutData(data);
			comboBoxsMin[i].setEnabled(minMaxComboBoxEnabled);
						
			selectionOptions[i].addSelectionListener(new SelectionOptionsButtonSelectionListener(selectionOptions[i], label_min, comboBoxsMin[i]));			
		}		
		
		// Add a horizontal separator
		
		Label separatorA = new Label(getParent(), SWT.SEPARATOR | SWT.HORIZONTAL);
		data = new FormData();
		data.top = new FormAttachment(selectionOptions[selectionOptions.length-1], MARGIN);
		data.left = new FormAttachment(testSuiteList, MARGIN);
		separatorA.setLayoutData(data);
		separatorA.setEnabled(true);
		
		// Query Options
		
		String[] queryOptionsName = {
				"Checked Coverage", "Edge-aware",
				"Auto Minimization"
		};
		
		queryOptions = new Button[queryOptionsName.length];
		
		for(int i = 0; i < queryOptionsName.length; i++) {
			queryOptions[i] = new Button(getParent(), SWT.CHECK);			
			queryOptions[i].setText(queryOptionsName[i]);
			data = new FormData();	
			if(i == 0)
				data.top = new FormAttachment(separatorA, MARGIN);
				//data.top = new FormAttachment(selectionOptions[selectionOptions.length-1], MARGIN);
			else
				data.top = new FormAttachment(queryOptions[i-1], MARGIN);
			data.left = new FormAttachment(testSuiteList, MARGIN);
			queryOptions[i].setLayoutData(data);
			//selectionOptions[i].addSelectionListener(new SelectionOptionsButtonSelectionListener(selectionOptionsName[i]));	
			queryOptions[i].setEnabled(true);			
			queryOptions[i].addSelectionListener(new QuerynOptionsButtonSelectionListener(queryOptions[i]));
		}	
		
		// Generate New Test Suite
		
		generateTestSuite = new Button(getParent(), SWT.PUSH);
		generateTestSuite.setText("Generate Test Suite");
		data = new FormData();	
		data.top = new FormAttachment(43, 0);
		data.left = new FormAttachment(0, 0);		
		generateTestSuite.setLayoutData(data);
		generateTestSuite.addSelectionListener(new GenerateTestSuiteButtonSelectionListener());
		generateTestSuite.setEnabled(true);
		
		// List of Test Cases in the New Test Suite 
		
		newTestSuiteList = new List(getParent(), SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
		
		data = new FormData();	
		data.top = new FormAttachment(50, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(60, 0);
		//data.bottom = new FormAttachment(40,0);
		newTestSuiteList.setLayoutData(data);		
		newTestSuiteList.addSelectionListener(new TodoItemsListSelectionListener());
		newTestSuiteList.setEnabled(true);
				
		/*------------------------------------------------------------------------*/
		
		sourceFileName = "";		
				
		sourceFileText = new Text(getParent(), SWT.SINGLE | SWT.BORDER);
		data = new FormData();	
		data.top = new FormAttachment(0, 0);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(FIFTY_PERCENT,0);
		//sourceFileText.setLayoutData(data);
		//sourceFileText.addModifyListener(new SourceFileTextModifyListener());
		//sourceFileText.addFocusListener(new ToDoItemTextFocusListener());
				
		sourceFileButton = new Button(getParent(), SWT.PUSH);
		sourceFileButton.setText("Select Source File");
		data = new FormData();	
		data.top = new FormAttachment(0, MARGIN);
		data.left = new FormAttachment(sourceFileText, MARGIN);
		//sourceFileButton.setLayoutData(data);
		//sourceFileButton.addSelectionListener(new SourceFileButtonSelectionListener());
		//sourceFileButton.setEnabled(true);
				
		generateButton = new Button(getParent(), SWT.PUSH);
		generateButton.setText("Generate Fingerprint");
		data = new FormData();	
		data.top = new FormAttachment(sourceFileButton, MARGIN);
		data.left = new FormAttachment(0, 0);
		//generateButton.setLayoutData(data);
		//generateButton.addSelectionListener(new GenerateButtonSelectionListener());
		//generateButton.setEnabled(false);
		
		label_1 = new Label(getParent(), SWT.PUSH);
		label_1.setText("No fingerprint generated yet...");
		data = new FormData();	
		data.top = new FormAttachment(sourceFileButton, MARGIN);
		data.left = new FormAttachment(generateButton, MARGIN);
		data.right = new FormAttachment(ONE_HUNDRED_PERCENT, 0);
		//label_1.setLayoutData(data);		
		//label_1.setEnabled(false);
				
		targetFileText = new Text(getParent(), SWT.SINGLE | SWT.BORDER);
		data = new FormData();	
		data.top = new FormAttachment(generateButton, MARGIN);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(FIFTY_PERCENT, 0);
		//targetFileText.setLayoutData(data);
		//targetFileText.addModifyListener(new TargetFileTextModifyListener());
		//--sourceFileText.addFocusListener(new ToDoItemTextFocusListener());
		
		targetFileButton = new Button(getParent(), SWT.PUSH);
		targetFileButton.setText("Select Target File");
		data = new FormData();	
		data.top = new FormAttachment(generateButton, MARGIN);
		data.left = new FormAttachment(targetFileText, MARGIN);		
		//targetFileButton.setLayoutData(data);
		//targetFileButton.addSelectionListener(new TargetFileButtonSelectionListener());
		//targetFileButton.setEnabled(false);
		
		compareButton = new Button(getParent(), SWT.PUSH);
		compareButton.setText("Compare");
		data = new FormData();	
		data.top = new FormAttachment(targetFileText, MARGIN);
		data.left = new FormAttachment(0, 0);
		//compareButton.setLayoutData(data);
		//compareButton.addSelectionListener(new CompareButtonSelectionListener());
		//compareButton.setEnabled(false);	
		
		todoItemsList = new List(getParent(), SWT.MULTI | SWT.BORDER | SWT.V_SCROLL);
				
		data = new FormData();	
		data.top = new FormAttachment(compareButton, MARGIN);
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(FIFTY_PERCENT, 0);
		data.bottom = new FormAttachment(ONE_HUNDRED_PERCENT,0);
		//todoItemsList.setLayoutData(data);
		
		//todoItemsList.addSelectionListener(new TodoItemsListSelectionListener());		
		//todoItemsList.setEnabled(true);		
		
	}
	
	public class SourceFileButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent event) {			
			FileDialog fileDialog = new FileDialog(getParent().getShell(), SWT.OPEN);
			fileDialog.setFilterExtensions(new String[] {"*.jar"});
			String separator = "/";
			String filterPath = "/";
			String platform = SWT.getPlatform();
			if (platform.equals("win32") || platform.equals("wpf")) {
				separator = "\\";
				filterPath = "c:\\";
			}
			fileDialog.setFilterPath (filterPath);
			fileDialog.setText("Select JAR File");
			fileDialog.open();			
			sourceFileName = fileDialog.getFilterPath() + separator + fileDialog.getFileName();
			sourceFileText.setText(sourceFileName);
			generateButton.setEnabled(true);
		}
	}
	
	public class TargetFileButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent event) {			
			FileDialog fileDialog = new FileDialog(getParent().getShell(), SWT.OPEN);
			fileDialog.setFilterExtensions(new String[] {"*.jar"});
			String separator = "/";
			String filterPath = "/";
			String platform = SWT.getPlatform();
			if (platform.equals("win32") || platform.equals("wpf")) {
				separator = "\\";
				filterPath = "c:\\";
			}
			fileDialog.setFilterPath (filterPath);
			fileDialog.setText("Select JAR File");
			fileDialog.open();			
			targetFileName = fileDialog.getFilterPath() + separator + fileDialog.getFileName();
			targetFileText.setText(targetFileName);
			compareButton.setEnabled(true);
		}
	}
	
	public class GenerateButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent event) {			
			if(sourceFileText.getText().equals(EMPTYSTRING))
				showMessage("Plesae select a JAR file");
			else {
				label_1.setEnabled(true);								
				//iFingerprintGeneratorDetector = new IFingerprintGeneratorDetectorImpl();
				label_1.setText("Generating fingerprint ... ");				
				long before = System.currentTimeMillis();
				//fingerPrint = iFingerprintGeneratorDetector.generateFingerprintFrom(sourceFileName);
				long after = System.currentTimeMillis();
				long elapsedTime = after - before;
				//label_1.setText("Fingerprint [" + fingerPrint.getGeneratorId() + "] has been generated in " + elapsedTime + "ms");				
				targetFileButton.setEnabled(true);
			}	
		}
	}
	
	public class CompareButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent event) {			
			if(targetFileText.getText().equals(EMPTYSTRING))
				showMessage("Plesae select a JAR file");
			else {
				long before = System.currentTimeMillis();
				//IFingerprintResult fingerprintResult = iFingerprintGeneratorDetector.generateFingerprintResultFrom(fingerPrint, targetFileName);
				long after = System.currentTimeMillis();
				//todoItemsList.add(fingerprintResult.displayString());
				//String result = fingerprintResult.displayString();
				todoItemsList.removeAll();
				//String[] resultArray = result.split("\n");
				//for(int i = 0; i < resultArray.length; i++) {
				//	todoItemsList.add(resultArray[i]);
					//testSuiteList.add("Test Suite");
				//}
				//long elapsedTime = after - before;
				//todoItemsList.add("Comparision completed in " + elapsedTime + "ms");
				//System.out.println(result);
				//System.out.println(resultArray.length);
				//todoItemsList.setEnabled(false);
			}	
		}
	}
	
	public class SourceFileTextModifyListener implements ModifyListener {	
		public void modifyText(ModifyEvent e) {		
		}
	}
	
	public class TargetFileTextModifyListener implements ModifyListener {	
		public void modifyText(ModifyEvent e) {		
		}
	}
	
	/*------------------------------------------------------------------------*/
		
	public class GenerateTestSuiteButtonSelectionListener extends SelectionAdapter {
		public void widgetSelected(SelectionEvent event) {		
			
				// Coverage Options
				/*String[] coverageOptionsName = {
						"Statement Coverage", "Branch Coverage",
						"Def-use Pair Count"
				};*/
				double minCvg = Double.parseDouble(minCoverage);
								
				// Query Options
				boolean isChecked = queryOptions[0].getSelection();
				boolean isEdgeAware = queryOptions[1].getSelection();
				boolean autoMinimization = queryOptions[2].getSelection();
				
				ArrayList<Node> selectedTestCases = new ArrayList<Node>();
				selectedTestCases.clear(); 
												
				try {
					if(autoMinimization) {
						selectedTestCases = ProjectController.getMinimizedTestSuite3(coverageOptionName);				
					} else {
						if(isEdgeAware) {							
							selectedTestCases = ProjectController.getMinimizedTestSuite2(coverageOptionName, minCvg, isChecked);
						} else {
							selectedTestCases = ProjectController.getMinimizedTestSuite(coverageOptionName, minCvg, isChecked);
						}
					}					
				} catch (Exception e) {					
					e.printStackTrace();
				}
								 
				newTestSuiteList.removeAll(); 
				
				for(int i = 0; i < selectedTestCases.size(); i++) {					
					TestCaseNode testCaseNode = selectedTestCases.get(i).testCaseNode;
					newTestSuiteList.add("Test Case: " + testCaseNode.getTestCaseID() + " " + testCaseNode.stmCov.toString());
				}
		}
	}	
	
	public class TodoItemsListSelectionListener extends SelectionAdapter {	
		public void widgetSelected(SelectionEvent event) {
			//targetTestSuite = testSuiteList.getItem(testSuiteList.getSelectionIndex());			
			//showMessage(targetTestSuite);
		}
	}
	
	public class SelectionOptionsButtonSelectionListener extends SelectionAdapter {
		private String string;
		private String isSelcetd;
		private Button coverageOption; 
		private Label labelMin; 
		//private Label labelMax; 
		private Combo comboBoxMin; 
		//private Combo comboBoxMax;
		
		/*SelectionOptionsButtonSelectionListener(Button selectionOptions, Label labelMin, Label labelMax, Combo comboBoxMin, Combo comboBoxMax) {
			this.string = selectionOptions.getText();		
			isSelcetd = ""+selectionOptions.getSelection();
			this.selectionOptions = selectionOptions;
			this.labelMin = labelMin;
			this.labelMax = labelMax;
			this.comboBoxMin = comboBoxMin;
			this.comboBoxMax = comboBoxMax;
		}*/
		
		SelectionOptionsButtonSelectionListener(Button coverageOption, Label labelMin, Combo comboBoxMin) {					
			this.coverageOption = coverageOption;
			this.string = coverageOption.getText();
			isSelcetd = ""+coverageOption.getSelection();			
			this.labelMin = labelMin;
			//this.labelMax = labelMax;
			this.comboBoxMin = comboBoxMin;
			//this.comboBoxMax = comboBoxMax;
		}
		
		public void widgetDeselected() {
			labelMin.setEnabled(coverageOption.getSelection());
			comboBoxMin.setEnabled(coverageOption.getSelection());
		}
		
		public void widgetSelected(SelectionEvent event) {			
			//showMessage(string);
			//coverageOption = coverageOption.getText();
			isSelcetd = ""+coverageOption.getSelection();			
			labelMin.setEnabled(coverageOption.getSelection());
			//labelMax.setEnabled(selectionOptions.getSelection());
			comboBoxMin.setEnabled(coverageOption.getSelection());
			//comboBoxMax.setEnabled(selectionOptions.getSelection());	
			
			coverageOptionName = coverageOption.getText();
			
			// Deselect the other options
			for(int i = 0; i < selectionOptions.length; i++) {				
				if(selectionOptions[i].getText().equalsIgnoreCase(coverageOption.getText())) {
					continue;
				} else {
					selectionOptions[i].setSelection(false);		
					comboBoxsMin[i].setEnabled(false);					
				}
			}
		}
	}
	
	public class ComboBoxSelectionListener extends SelectionAdapter {
		Combo combo;
		ComboBoxSelectionListener(Combo combo) {
			this.combo = combo;
		}
		public void widgetSelected(SelectionEvent event) {			
			//showMessage(combo.getItem(combo.getSelectionIndex()));
			minCoverage = combo.getItem(combo.getSelectionIndex());			
		}
	}
	
	public class QuerynOptionsButtonSelectionListener extends SelectionAdapter {
		private Button queryOption;
		QuerynOptionsButtonSelectionListener(Button queryOption) {
			this.queryOption = queryOption;
		}
		public void widgetSelected(SelectionEvent event) {
			//showMessage(queryOption.getText());	
				
			if(queryOption.getText().equalsIgnoreCase(queryOptions[2].getText())) {
				queryOptions[0].setSelection(false);
				queryOptions[1].setSelection(false);		
				
				comboBoxsMin[0].setEnabled(false);	
				comboBoxsMin[1].setEnabled(false);	
				comboBoxsMin[2].setEnabled(false);	
			} else 
				queryOptions[2].setSelection(false);
		}		
	}
}