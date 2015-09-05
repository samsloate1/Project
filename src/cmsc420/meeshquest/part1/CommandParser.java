package cmsc420.meeshquest.part1;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import xml.NodeMaker;

import Structures.City;

import comparator.CityLocationComparator;
import comparator.CityNameComparator;

public class CommandParser {
	
	private TreeMap<String,City> nameToCity = null;
	private TreeMap<Point2D.Float,City> cityLocations = new TreeMap<Point2D.Float,City>();
	int spatialHeight = -1;
	int spatialWidth = -1;
	Document results = null;
	
	public CommandParser(int spatialHeight, int spatialWidth, Document results){
		CityNameComparator nameCompare = new CityNameComparator();
		CityLocationComparator locationCompare = new CityLocationComparator();
		nameToCity = new TreeMap<String,City>(nameCompare);
		cityLocations = new TreeMap<Point2D.Float,City>(locationCompare);
		this.results = results;
	}
	
	public Node createCity(Element commandNode) {
		//Check if the city exists, if not add the city
		String name = commandNode.getAttribute("name");
		int x = Integer.parseInt(commandNode.getAttribute("x"));
		int y = Integer.parseInt(commandNode.getAttribute("y"));
		int rad= Integer.parseInt(commandNode.getAttribute("radius"));
		String color = commandNode.getAttribute("color");
		NodeMaker maker = new NodeMaker(results);
		if(nameToCity.containsKey(name)){
			String errorType = "duplicateCityName";
			return maker.createCityXml(true, errorType, name, x, y, rad, color);
		}else{
			//TODO change to PR QuadTree
			Point2D.Float location = new Point2D.Float(x,y);
			if(cityLocations.containsKey(location)){
				String errorType = "duplicateCityCoordinates";
				return maker.createCityXml(false, errorType, name, x, y, rad, color);
			}else{
				City newCity = new City(name, color, x, y,rad);
				nameToCity.put(name, newCity);
				System.out.println(nameToCity);
				cityLocations.put(new Point2D.Float(x, y), newCity);
				return maker.createCityXml(true,null,name,x,y,rad,color);
			}
		}
	}

	public Node deleteCity(Element commandNode) {
		NodeMaker maker = new NodeMaker(results);
		String name = commandNode.getAttribute("name");
		City cityRem = nameToCity.get(name);
		if(cityRem != null){
			//TODO remove from quadtree as well
			float x = cityRem.x;
			float y = cityRem.y;
			cityLocations.remove(new Point2D.Float(x, y));
			nameToCity.remove(name);		
			return maker.deleteCityXml(true, false, null, name);
		}else{
			String error = "cityDoesNotExist";
			return maker.deleteCityXml(true, false, error, name);
		}
		
	}

	public Node clearAll(Element commandNode) {
		NodeMaker maker = new NodeMaker(results);
		cityLocations.clear();
		nameToCity.clear();
		return maker.clearAllXml();
		//TODO clear the PRquad tree
		
		
	}

	public Node listCities(Element commandNode) {
		String sortBy = commandNode.getAttribute("sortBy");
		NodeMaker maker = new NodeMaker(results);
		if(sortBy.equals("name")){
			return maker.listCitiesName(nameToCity);
		}else if(sortBy.equals("coordinate")){
			return maker.listCitiesPoint(cityLocations);
		}else{
			//TODO figure out this case, get clarification
			return null;
		}
		
	}

	public void mapCity(Element commandNode) {
		// TODO Auto-generated method stub
		
	}

	public void unmapCity(Element commandNode) {
		// TODO Auto-generated method stub
		
	}

	public void printPRQuadTree(Element commandNode) {
		// TODO Auto-generated method stub
		
	}

	public void saveMap(Element commandNode) {
		// TODO Auto-generated method stub
		
	}

	public void rangeCities(Element commandNode) {
		// TODO Auto-generated method stub
		
	}

	public void nearestCity(Element commandNode) {
		// TODO Auto-generated method stub
		
	}
	
}
