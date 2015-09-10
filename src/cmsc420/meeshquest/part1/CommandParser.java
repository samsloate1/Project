package cmsc420.meeshquest.part1;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cmsc420.drawing.CanvasPlus;

import prquadtree.PRQuadTree;
import prquadtree.PRQuadTree.GreyNode;
import prquadtree.PRQuadTree.PRNode;

import xml.NodeMaker;

import Structures.City;

import comparator.CityLocationComparator;
import comparator.CityNameComparator;

public class CommandParser {

	private TreeMap<String,City> nameToCity = null;
	private HashSet<String> mappedCities = null;
	private PRQuadTree<City> prtree = null;
	private TreeMap<Point2D.Float,City> cityLocations = new TreeMap<Point2D.Float,City>();
	int spatialHeight = -1;
	int spatialWidth = -1;
	Document results = null;
	CanvasPlus canvas;
	NodeMaker maker;


	public CommandParser(int spatialHeight, int spatialWidth, Document results){
		CityNameComparator nameCompare = new CityNameComparator();
		CityLocationComparator locationCompare = new CityLocationComparator();
		nameToCity = new TreeMap<String,City>(nameCompare);
		cityLocations = new TreeMap<Point2D.Float,City>(locationCompare);
		mappedCities = new HashSet<String>();
		this.spatialWidth = spatialWidth;
		this.spatialHeight = spatialHeight;
		this.results = results;
		maker = new NodeMaker(results);
	}

	public Node createCity(Element commandNode) {
		//Check if the city exists, if not add the city
		String name = commandNode.getAttribute("name");
		int x = Integer.parseInt(commandNode.getAttribute("x"));
		int y = Integer.parseInt(commandNode.getAttribute("y"));
		int rad= Integer.parseInt(commandNode.getAttribute("radius"));
		String color = commandNode.getAttribute("color");
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
				cityLocations.put(new Point2D.Float(x, y), newCity);
				return maker.createCityXml(true,null,name,x,y,rad,color);
			}
		}
	}

	public Node deleteCity(Element commandNode) {
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
		cityLocations.clear();
		nameToCity.clear();
		return maker.clearAllXml();
		//TODO clear the PRquad tree


	}

	public Node listCities(Element commandNode) {
		String sortBy = commandNode.getAttribute("sortBy");
		if(sortBy.equals("name")){
			return maker.listCitiesName(nameToCity);
		}else if(sortBy.equals("coordinate")){
			return maker.listCitiesPoint(cityLocations);
		}else{
			//TODO figure out this case, get clarification
			return null;
		}

	}

	public Node mapCity(Element commandNode) {
		String name = commandNode.getAttribute("name");
		String error = null;
		if(mappedCities.contains(name)){
			error = "cityAlreadyMapped";
			return maker.mapCity(name,error);
		}else{
			City city = nameToCity.get(name);
			if(city == null){
				error= "nameNotInDictionary";
				return maker.mapCity(name, error);
			}else{
				if(city.x > spatialWidth || city.x < 0 || 
						city.y	> spatialHeight || city.y < 0){
					error = "cityOutOfBounds";
					return maker.mapCity(name, error);
				}else{
					if(prtree == null){
						prtree = new PRQuadTree<City>(spatialWidth, spatialHeight, city);
					}else{
						prtree.insert(city);
					}
					mappedCities.add(name);
					return maker.mapCity(name, error);
				}
			}
		}
	}

	public Node unmapCity(Element commandNode) {
		String name = commandNode.getAttribute("name");
		String error = null;
		City city = nameToCity.get(name);
		if(city ==null){
			//TODO errror remove from mappedlist
			error = "nameNotInDictionary";
			return maker.unmapCity(name,error);
		}else if(!mappedCities.contains(name)){
			error = "cityNotMapped";
			return maker.unmapCity(name,error);
		}else{
			prtree.delete(city);
			mappedCities.remove(name);
			return maker.unmapCity(name,null);
		}

	}

	public Node printPRQuadTree(Element commandNode) {
		String error = null;
		if(mappedCities.isEmpty()){
			error = "mapIsEmpty";
		}
		return maker.printPRTree(prtree, error);

	}

	public Node saveMap(Element commandNode,boolean circle,int xCirc, int yCirc, int radius) {
		String name = commandNode.getAttribute("name");
		canvas = new CanvasPlus(name,spatialWidth,spatialHeight);
		canvas.addRectangle(0, 0, spatialWidth, spatialHeight, Color.black, false);
		for(PRQuadTree.PRNode n : prtree){
			if(n instanceof PRQuadTree.BlackNode){
				City c = (City)((PRQuadTree.BlackNode)n).element;	
				canvas.addPoint(c.getName(), c.x, c.y, Color.black);
			}
			if(n instanceof PRQuadTree.GreyNode){
				int x = ((PRQuadTree.GreyNode)n).x;
				int y = ((PRQuadTree.GreyNode)n).y;
				int span = ((PRQuadTree.GreyNode)n).span;
				System.out.println("x: " + x);
				System.out.println("y: " + y);
				System.out.println("span" + span);
				canvas.addLine(x, y, x+span, y, Color.black);
				canvas.addLine(x, y, x-span, y, Color.black);
				canvas.addLine(x, y, x, y+span, Color.black);
				canvas.addLine(x, y, x, y-span, Color.black);
			}
		}
		if(circle){
			canvas.addCircle(xCirc, yCirc, radius, Color.black,false);
		}
		try {
			canvas.save(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return maker.saveMap(name);

	}

	public Node rangeCities(Element commandNode) {
		String radius = commandNode.getAttribute("radius");
		String x =  commandNode.getAttribute("x");
		String y = commandNode.getAttribute("y");
		String saveMap = commandNode.getAttribute("saveMap");
		if(saveMap.equals("")){
			saveMap(commandNode,true,
					Integer.parseInt(x),Integer.parseInt(y),Integer.parseInt(radius));
		}
		TreeMap<String,City> cities= prtree.rangeCities(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(radius));
		String error = null;
		if(cities.size() ==0){
			error = "noCitiesExistInRange";
		}
		return maker.rangeCities(radius,x,y,saveMap,cities, error);

	}

	public Node nearestCity(Element commandNode) {
		String x = commandNode.getAttribute("x");
		String y = commandNode.getAttribute("y");
		String error = null;
		if(mappedCities.isEmpty() ){
			error = "mapIsEmpty";
		}
		City c = prtree.nearestCity(Integer.parseInt(x), Integer.parseInt(y));
		return maker.nearestCity(x,y,error,c);
	}

}
