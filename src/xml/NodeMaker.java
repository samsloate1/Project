package xml;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import prquadtree.PRQuadTree;

import Structures.City;
import avltree.AVLTree;

public class NodeMaker {
	Document results = null;
	public NodeMaker(Document results){
		this.results = results;
	}
	
	public Node makeNode(boolean success,String error,String commandType,
			List<Node> parameterList,List<Node> outputList){
		Element head = null;
		if(success){
			head = results.createElement("success");
		}else{		
			head = results.createElement("error");
			head.setAttribute("type", error);
		}
		Element command = results.createElement("command");
		command.setAttribute("name", commandType);
		Element parameters = results.createElement("parameters");
		Element output = results.createElement("output");
		for(Node p: parameterList){
			parameters.appendChild(p);
		}
		head.appendChild(parameters);
		for(Node out: outputList){
			output.appendChild(out);
		}
		head.appendChild(output);
		return head;
	}
	
	public Node deleteCityXml(boolean success,boolean unmapped,
			String error,String name){
		
		List<Node> parameterList = new LinkedList<Node>();
		List<Node> outputList = new LinkedList<Node>();
		Element nameXml = results.createElement("name");
		nameXml.setAttribute("value", name);
		parameterList.add(nameXml);
		if(unmapped){
			//TODO unmapped stuff
		}
		return makeNode(success,error,"deleteCity",parameterList,outputList);
		
	}
	/**
	 * Builds the xml for city creation
	 * @param error
	 * @param name
	 * @param x
	 * @param y
	 * @param rad
	 * @param color
	 * @return
	 */
	public Node createCityXml(boolean success,String error,String name,
			int x,int y, int rad, String color){
		//Sets all the parameters
		List<Node> parameterList = new LinkedList<Node>();
		Element nameXml = results.createElement("name");
		nameXml.setAttribute("value", name);
		parameterList.add(nameXml);
		Element xXml = results.createElement("x");
		xXml.setAttribute("value", Integer.toString(x));
		parameterList.add(xXml);
		Element yXml = results.createElement("y");
		yXml.setAttribute("value", Integer.toString(y));
		parameterList.add(yXml);
		Element radiusXml = results.createElement("radius");
		radiusXml.setAttribute("value", Integer.toString(rad));
		parameterList.add(radiusXml);
		Element colorXml = results.createElement("color");
		colorXml.setAttribute("value",color);
		parameterList.add(colorXml);
		List<Node> outputList = new LinkedList<Node>();
		return makeNode(success,error,"createCity",parameterList,outputList);
	}

	public Node clearAllXml() {
		List<Node> parameterList = new  LinkedList<Node>();
		List<Node> output = new LinkedList<Node>();
		return makeNode(true,null,"clearAll",parameterList,output);
		
	}
	
	public Element cityElement(City city){
		Element cityElement = results.createElement("city");
		cityElement.setAttribute("name", city.getName());
		cityElement.setAttribute("x",Float.toString(city.x));
		cityElement.setAttribute("y", Float.toString(city.y));
		cityElement.setAttribute("color", city.getColor());
		cityElement.setAttribute("radius", Float.toString(city.getRadius()));
		return cityElement;
	}
	
	public Node listCitiesName(TreeMap<String,City> cityNames){
		List<Node> parameterList = new LinkedList<Node>();
		Element sortBy = results.createElement("sortBy");
		sortBy.setAttribute("value", "name");
		parameterList.add(sortBy);
		
		List<Node> output = new LinkedList<Node>();
		if(cityNames.size()== 0){
			return makeNode(false, "noCitiesToList", "listCities", parameterList, output);
		}
		
		for(Entry<String, City> c: cityNames.entrySet()){
			City city = c.getValue();
			Element cityElement = cityElement(city);
			output.add(0, cityElement);
		}

		return makeNode(true,null, "listCities", parameterList, output);
	}
	public Node listCitiesPoint(TreeMap<Point2D.Float,City> coordinates){
		List<Node> parameterList = new LinkedList<Node>();
		Element sortBy = results.createElement("sortBy");
		sortBy.setAttribute("value", "name");
		parameterList.add(sortBy);
		
		List<Node> output = new LinkedList<Node>();
		if(coordinates.size()== 0){
			return makeNode(false, "noCitiesToList", "listCities", parameterList, output);
		}
		
		for(Entry<Point2D.Float, City> c: coordinates.entrySet()){
			//TODO add to the beginning to get descending order
			City city = c.getValue();
			Element cityElement = cityElement(city);
			output.add( cityElement);
			//TODO check out the ordering	
		}
		return makeNode(true,null,"listCities",parameterList,output);//TODO create cityList element with all the  cities
	}

	public Node mapCity(String city, String error) {
		List<Node> parameterList = new LinkedList<Node>();
		List<Node> outputList  = new LinkedList<Node>();
		Element element = results.createElement("name");
		element.setAttribute("value", city);
		parameterList.add(element);
		// TODO Auto-generated method stub
		if(error == null)
			return makeNode(true,error, "mapCity", parameterList, outputList);
		else
			return makeNode(false,error, "mapCity", parameterList, outputList);

	}

	public Node unmapCity(String city, String error) {
		List<Node> parameterList = new LinkedList<Node>();
		List<Node> outputList  = new LinkedList<Node>();
		Element element = results.createElement("name");
		element.setAttribute("value", city);
		parameterList.add(element);
		// TODO Auto-generated method stub
		if(error == null)
			return makeNode(true,error, "unmapCity", parameterList, outputList);
		else
			return makeNode(false,error, "unmapCity", parameterList, outputList);
	}
	
	public Node printPRTree(PRQuadTree<City> pr,String error){
		List<Node> parameterList = new LinkedList<Node>();
		List<Node> outputList = new LinkedList<Node>();
		Element quadtreeElement = results.createElement("quadtree");
		if(error == null){
			pr.printQuadTree(quadtreeElement, results);
			outputList.add(quadtreeElement);
			return makeNode(true,error, "printPRQuadTree", parameterList, outputList);
		}else{
			return makeNode(false,error,"printPRQuadTree",parameterList,outputList);
		}
	}
	
	public Node saveMap(String name){
		List<Node> parameterList = new LinkedList<Node>();
		Element saveFile = results.createElement("name");
		saveFile.setAttribute("value",name);
		parameterList.add(saveFile);
		List<Node> outputList = new LinkedList<Node>();
		return makeNode(true,null,"saveMap",parameterList,outputList);
		
	}

	public Node rangeCities(String radius, String x, String y, String saveMap,
			TreeMap<String, City> cities,String error) {
		List<Node> parameterList = new LinkedList<Node>();
		Element xEl = results.createElement("x");
		xEl.setAttribute("value", x);
		Element yEl = results.createElement("y");
		yEl.setAttribute("value", x);
		Element rad = results.createElement("radius");
		rad.setAttribute("value", radius);
		parameterList.add(xEl);
		parameterList.add(yEl);
		parameterList.add(rad);
		if(!saveMap.equals("")){
			Element save = results.createElement("saveMap");
			save.setAttribute("name", saveMap);
			parameterList.add(save);
		}
		List<Node> output = new LinkedList<Node>();
		for(Entry<String, City> c: cities.entrySet()){
			City city = c.getValue();
			Element cityElement = cityElement(city);
			output.add(0, cityElement);
		}
		boolean success = false;
		if(error != null){
			success = true;
		}
		return makeNode(success,error,"rangeCities", parameterList,output);
	}

	public Node nearestCity(String x, String y, String error, City c) {
		List<Node> parameterList = new LinkedList<Node>();
		Element xEl = results.createElement("x");
		xEl.setAttribute("value", x);
		Element yEl = results.createElement("y");
		yEl.setAttribute("value", x);
		List<Node> output = new LinkedList<Node>();
		if(error == null){
			Element city = results.createElement("city");
			city.setAttribute("name", c.getName());
			city.setAttribute("x", Float.toString(c.x));
			city.setAttribute("y", Float.toString(c.y));
			city.setAttribute("color", c.getColor());
			city.setAttribute("radius", Integer.toString(c.getRadius()));
			return makeNode(true,null,"nearestCity",parameterList,output);
		}else{
			return makeNode(false,error,"nearestCity",parameterList,output);
		}
		
	}

	public void avlTree(AVLTree<City, String> avl, String error) {
		// TODO Auto-generated method stub
		
	}

}
