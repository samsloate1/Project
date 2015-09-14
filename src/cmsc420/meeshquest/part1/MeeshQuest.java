package cmsc420.meeshquest.part1;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import comparator.CityLocationComparator;
import comparator.CityNameComparator;

import Structures.City;

import cmsc420.xml.XmlUtility;

public class MeeshQuest {

	
    public static void main(String[] args) {
    	
    	Document results = null;
    	try {
			results = XmlUtility.getDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
        	Document doc = XmlUtility.validateNoNamespace(System.in);
        	//Build the new XML with the results
        	Element resultElement = results.createElement("results");
        	results.appendChild(resultElement);
        	//Gets the top level command node
        	Element commandNode = doc.getDocumentElement();
        	//Get the height and width
        	int spatialHeight = Integer.parseInt(commandNode.getAttribute("spatialHeight"));
        	int spatialWidth = Integer.parseInt(commandNode.getAttribute("spatialWidth"));
        	CommandParser command = new CommandParser(spatialHeight,spatialWidth,results);
        	//TODO if there is an error here the print a fatal error, pass these values
        	//List of all the commands
        	final NodeList nl = commandNode.getChildNodes();
        	for (int i = 0; i < nl.getLength(); i++) {
        		if (nl.item(i).getNodeType() == Document.ELEMENT_NODE) {
        			commandNode = (Element) nl.item(i);
        			//Process command nodes here Use a switch statement
        			processCommands(command,commandNode,results,spatialHeight,spatialWidth,resultElement);
        		}
        	}
        } catch (SAXException | IOException | ParserConfigurationException e) {
        	e.printStackTrace();
        	/* TODO: Process fatal error here */
        	results.appendChild(results.createElement("fatalError"));
        	
		} finally {
            try {
				XmlUtility.print(results);
			} catch (TransformerException e) {
				e.printStackTrace();
			}
        }
    }
    
    /**
     * Processes the individual commands using a switch statement
     * @param resultElement 
     * @param comandNode
     */
    private static void processCommands(CommandParser command,Element commandNode,Document results,
    		int spatialHeight,int spatialWidth, Element resultElement){
    	String commandType = commandNode.getNodeName();
    	//TODO create methods that will make cities and delete them
    	//Could have the functionality in a dictionary class if wanted
    	
    	switch(commandType){
    		case "createCity": resultElement.appendChild(command.createCity(commandNode));break;
    		case "deleteCity": resultElement.appendChild(command.deleteCity(commandNode));break;
    		case "clearAll": resultElement.appendChild(command.clearAll(commandNode));break;
    		case "listCities": resultElement.appendChild(command.listCities(commandNode));break;
    		case "mapCity" : resultElement.appendChild(command.mapCity(commandNode));break;
    		case "unmapCity" :resultElement.appendChild(command.unmapCity(commandNode));break;
    		case "printPRQuadtree": resultElement.appendChild(command.printPRQuadTree(commandNode));break;
    		case "saveMap": resultElement.appendChild(command.saveMap(commandNode,false,0,0,0));break;
    		case "rangeCities": resultElement.appendChild((command.rangeCities(commandNode)));break;
    		case "nearestCity": resultElement.appendChild(command.nearestCity(commandNode));break;
    		default: break;
    	}
    	
    }
}
