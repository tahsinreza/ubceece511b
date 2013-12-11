package ca.ubc.ece.eec511b.project.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.ListIterator;
import java.util.Vector;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

public class GraphDataXML {
	
	public final static String ROOT_TAG = "GRAPH";
	public final static String NODE_TAG = "NODE";
	public final static String EDGE_TAG = "EDGE";
	
	public final static String NODE_TYPE_ATTR = "TYPE"; 
	public final static String NODE_NAME_ATTR = "NAME"; 
	public final static String NODE_VALUE_ATTR = "VALUE"; 
	
	public final static String EDGE_TYPE_ATTR = "TYPE";	
	public final static String EDGE_NAME_ATTR = "NAME"; 
	public final static String EDGE_VALUE_ATTR = "VALUE"; 
	
	private Document doc;
	private Element root;
	
	/**
	 * Constructor
	 */
	public GraphDataXML() {
		this.root = new Element(GraphDataXML.ROOT_TAG);		
		this.doc = new Document(this.root);
	}
	
	/**
	 * Set the document object
	 * @param doc
	 */
	public void setDocument(Document doc) {
		this.doc = doc;
		if(this.doc == null)
			throw new NullPointerException();
		this.root = this.doc.getRootElement();
	}
	
	/**
	 * Get document
	 * @return the document object
	 */
	public Document getDocument() {
		return this.doc;
	}
	
	/**
	 * Get root element
	 * @return the root element
	 */
	public Element getRootElement() {
		return this.root;		
	}
	
	/**
	 * Add element
	 * @param parent
	 * @param elementTag
	 * @return the newly created elemnt
	 */
	public Object addElement(Object parent, String elementTag) {
		Element element = new Element(elementTag);
		((Element)parent).addContent(element);
		return element;
	}
	
	/**
	 * Add attribute
	 * @param element
	 * @param attributeName
	 * @param attributeValue
	 * @return this element modified 
	 */
	public Object addAttribute(Object element, String attributeName, String attributeValue) {
		return ((Element)element).setAttribute(new Attribute(attributeName, attributeValue));
	}
		
	/**
	 * Get parent element
	 * @param element
	 * @return Element
	 */
	public Element getParentElement(Element element) {
		return element.getParentElement();
	}	

	/**
	 * Get attribute value
	 * @param element
	 * @param attributeName
	 * @return String
	 */
	public String getAttributeValue(Object element, String attributeName) {		
		return ((Element)element).getAttributeValue(attributeName);
	}	
	
	/**
	 * Get elements by tag name
	 * @param element
	 * @param tagName
	 * @return List
	 */
	public List getElementsByTagName(Element element, String tagName) {
		return this.getElementsByTagName(element, tagName, new Vector());
	}
	
	/**
	 * Get elements by tag name
	 * @param element
	 * @param tagName
	 * @param result
	 * @return List
	 */
	private List getElementsByTagName(Element element, String tagName, List result) {
		if(result == null) {
			result = new Vector();
		}
		if(element.getName().equalsIgnoreCase(tagName))
			result.add(element);	
		List list = element.getChildren();
		ListIterator listIterator  = list.listIterator();
		while(listIterator.hasNext()) {
			Element e = (Element)listIterator.next();
			getElementsByTagName(e, tagName, result);			
			//System.out.println(e.toString());
		}
		return result;
	}
	
	/**
	 * Get elements by tag name
	 * @param element
	 * @param tagName
	 * @param attributes
	 * @return List
	 */
	public List getElementsByTagName(Element element, String tagName, Attribute[] attributes) {
		return this.getElementsByTagName(element, tagName, null, attributes);
	}
	
	/**
	 * Get elements by tag name
	 * @param element
	 * @param tagName
	 * @param result
	 * @param attributes
	 * @return List
	 */
	private List getElementsByTagName(Element element, String tagName, List result, Attribute[] attributes) {
		if(result == null) {
			result = new Vector();
		}
		if(element.getName().equalsIgnoreCase(tagName)) {
			boolean hasAttributes = true;
			for(int i = 0; i < attributes.length; i++) {
				Attribute a = attributes[i];
				String aN = a.getName();
				String aV = a.getValue();
				
				Attribute aE = null;
								
				if((aE = element.getAttribute(aN)) != null) {
					if(aE.getValue().equalsIgnoreCase(aV)) {
						result.add(element);
						break;
					} else {
						hasAttributes = false;
						//break;
					}	
				} else {
					hasAttributes = false;
					//break;
				}	
			}
			
			/*
			if(hasAttributes) {	
				result.add(element);
			}*/	
		}	
		List list = element.getChildren();
		ListIterator listIterator  = list.listIterator();
		while(listIterator.hasNext()) {
			Element e = (Element)listIterator.next();
			getElementsByTagName(e, tagName, result, attributes);			
			//System.out.println(e.toString());
		}
		return result;
	}
		
	/**
	 * Iterate through all the elements in the document
	 * @param element
	 */
	public void getElements(Element element) {		
		List list = element.getChildren();
		ListIterator listIterator  = list.listIterator();
		//System.out.println(element.toString());
		while(listIterator.hasNext()) {
			Element e = (Element)listIterator.next();
			getElements(e);			
			//System.out.println(e.toString());
		}
	}
	
	/**
	 * Load an XML file
	 * @param xmlFileName
	 * @return Document
	 */	
	public Document loadXMLFile(String xmlFileName) {
		try {
			return new SAXBuilder().build(new FileInputStream(new File(xmlFileName)));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	/**
	 * Write XML document to file
	 * @param document
	 * @param fileName
	 */
	public void writeDocumentToFile(Document document, String fileName) {		
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		File file = null;	
		
		try {
			file = new File(fileName);
			if(file.exists()) {
				file.delete();
				file.createNewFile();
			} else { 
				file.createNewFile();
			}
			fileWriter = new FileWriter(file);
			bufferedWriter = new BufferedWriter(fileWriter);			
			XMLOutputter xmlOutputter = new XMLOutputter();
			xmlOutputter.output(document, bufferedWriter);						
		} catch (IOException e) {
			e.printStackTrace();
		} finally {			
			if(bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			if(fileWriter != null) {
				try {
					fileWriter.close();						
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Get document as string
	 * @param document
	 * @return String
	 */
	public String getDocumentAsString(Document document) {		
	      XMLOutputter serializer = new XMLOutputter();
	      return serializer.outputString(document);
	}
	
	/**
	 * Convert a XML String to a Document object
	 * @param xmlString
	 * @return Document
	 */
	public Document stringToDocument(String xmlString) {
		try {
			return new SAXBuilder().build(new StringReader(xmlString));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
			
	/**
	 * 
	 */
	public void sampleOutput() {
		try {
	      XMLOutputter serializer = new XMLOutputter();
	      serializer.output(doc, System.out);
	    } catch (IOException e) {
	      System.err.println(e);
	    }
	}
	
	/**
	 * 
	 */
	public void sample() {
		root = new Element("GREETINGS");
		root.setText("Hello JDOM!");
		doc = new Document(root);
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("GraphDataXML");
		GraphDataXML graphDataXML = new GraphDataXML();
		graphDataXML.sample();
		graphDataXML.sampleOutput();
		graphDataXML.writeDocumentToFile(graphDataXML.getDocument(), "C:/local/graph_data_xml.XML");		
	
	}
}
