package prquadtree;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cmsc420.drawing.CanvasPlus;

import Structures.City;

public class PRQuadTree<T extends Point2D.Float> implements Iterable<PRQuadTree.PRNode>{
	//width = x
	//height = y
	private int heightMax,heightMin,widthMax,widthMin = -1;
	private PRNode root = null;

	public interface PRNode{};
	interface PRInner extends PRNode{};
	interface PRLeaf extends PRNode{};
	static class WhiteNode implements PRLeaf{
		private static WhiteNode instance = null;
		public static WhiteNode getInstance(){
			if(instance == null)
				instance = new WhiteNode();
			return instance;
		}
		private WhiteNode(){

		}
	}
	public class BlackNode implements PRLeaf{
		public T element;

		public BlackNode(T elem){
			element = elem;
		}
	}
	public class GreyNode implements PRInner{

		public PRNode NE,NW,SW,SE;
		public int x,y,span;
		public GreyNode(int midX, int midY,int length){
			NE=NW=SW=SE = WhiteNode.getInstance();
			x = midX;
			y = midY;
			span = length;
		}
		public PRNode singleChild(){
			int count = 0;
			PRNode  ret= null;
			if(!(NW instanceof WhiteNode)){
				ret = NW;
				count++;
			}
			if(!(NE instanceof WhiteNode)){
				ret = NE;
				count++;
			}
			if(!(SW instanceof WhiteNode)){
				ret = SW;
				count++;
			}
			if(!(SE instanceof WhiteNode)){
				ret = SE;
				count++;
			}
			if(count > 1){
				return null;
			}
			return ret;
		}
	}

	public PRQuadTree(int spatialHeight, int spatialWidth,T elem){
		this.heightMax = spatialHeight;
		this.widthMax = spatialWidth;
		this.heightMin = 0;
		this.widthMin = 0;
		root = new BlackNode(elem);	
	}


	public PRNode insertHelper(PRNode node,T elem,int widthMin,int widthMax,
			int heightMin,int heightMax){
		if(node instanceof PRQuadTree.GreyNode){
			//TODO add the  node to one of the quadrants
			float x = elem.x;
			float y = elem.y;
			int midX = (widthMin+widthMax)/2;
			int midY = (heightMin+heightMax)/2;
			//For each direction
			if(x >midX && y >midY){//NE
				((PRQuadTree.GreyNode) node).NE = insertHelper(((PRQuadTree.GreyNode) node).NE,
							 elem, midX, widthMax, midY, heightMax);
			}else if(x >midX && y < midY){//SE
				((PRQuadTree.GreyNode) node).SE = insertHelper(((PRQuadTree.GreyNode) node).SE,
							 elem, midX, widthMax, heightMin, midY);
			}else if(x <midX && y <midY){//SW
				((PRQuadTree.GreyNode) node).SW = insertHelper(((PRQuadTree.GreyNode) node).SW,
						 elem,widthMin, midX, heightMin, midY);
			}else if(x <midX && y >midY){//NW
				((PRQuadTree.GreyNode) node).NW = insertHelper(((PRQuadTree.GreyNode) node).NW,
							 elem, widthMin,midX, midY, heightMax);
			}
		}
		if(node  instanceof PRQuadTree.BlackNode){
			//Store the current info
			BlackNode temp =  (BlackNode) node;
			//add a new grey node that splits the grid
		
			//put the info back  in  then call insertHelper  on this grey node again
			float x = temp.element.x;
			float y = temp.element.y;
			int midX = (widthMin+widthMax)/2;
			int midY = (heightMin+heightMax)/2;
			//span is the area in between which is same for height and width.
			//To get the boundries do midX+ span and midX - span
			int span = (widthMax -widthMin)/2;
			node = new GreyNode(midX,midY,span);
			//For each direction
			if(x >= midX && y >= midY){//NE
				((PRQuadTree.GreyNode)node).NE = temp;
			}else if(x >= midX && y < midY){//SE
				((PRQuadTree.GreyNode)node).SE = temp;
			}else if(x <midX && y <midY){//SW
				((PRQuadTree.GreyNode)node).SW = temp;
			}else if(x <midX && y >midY){//N
				((PRQuadTree.GreyNode)node).NW = temp;
			}
			node =  insertHelper(node,elem,widthMin,widthMax,heightMin,heightMax);
		}
		if(node instanceof PRQuadTree.WhiteNode){
			//TODO put the city in this node
			node = new BlackNode(elem);
		}
		return node;//TODO this method
		
	}
	public boolean insert(T elem){
		//Failures
		if(elem.x > widthMax || elem.x < widthMin ||
				elem.y> heightMax || elem.y<heightMin){
			return false;
		}
		//First checks for an empty tree
		root = insertHelper(root,elem, widthMin, widthMax, heightMin, heightMax);
		
		return true;//TODO figure this out
	}

	public PRNode deleteHelper(PRNode node, T elem,int widthMin,int widthMax,
			int heightMin,int heightMax){
		float x = elem.x;
		float y = elem.y;
		
		if(node instanceof PRQuadTree.GreyNode){
			int xMid = (widthMin + widthMax)/2;
			int yMid = (heightMin+heightMax)/2;
			if(x > xMid && y > yMid){//NE
				((PRQuadTree.GreyNode)node).NE = deleteHelper(((PRQuadTree.GreyNode)node).NE,
						elem,xMid,widthMax,yMid,heightMax);
			}else if(x >xMid && y<yMid){//SE
				((PRQuadTree.GreyNode)node).SE = deleteHelper(((PRQuadTree.GreyNode)node).SE,
						elem,xMid,widthMax,yMid,heightMax);
			}else if( x< xMid && y < yMid){//SW
				((PRQuadTree.GreyNode)node).SW = deleteHelper(((PRQuadTree.GreyNode)node).SW,
						elem,xMid,widthMax,yMid,heightMax);
			}else if( x < xMid && y >yMid){//NW
				((PRQuadTree.GreyNode)node).NW = deleteHelper(((PRQuadTree.GreyNode)node).NW,
						elem,xMid,widthMax,yMid,heightMax);
			}
			PRNode s = ((PRQuadTree.GreyNode)node).singleChild();
			if(s!= null){
				node = s;
			}
		}	
		else if(node instanceof PRQuadTree.BlackNode){
			Point2D.Float nodeValues = ((PRQuadTree.BlackNode) node).element;
			if(nodeValues.x == elem.x && nodeValues.y ==elem.y){
				node = WhiteNode.getInstance();
			}
		}
		return node;
	}
	/**
	 * Deletes an element from the tree
	 * @param elem
	 * @return
	 */
	public boolean delete(T elem){
		//Pass the city from the dictionary
		if(elem.x > widthMax || elem.x < widthMin ||
				elem.y> heightMax || elem.y<heightMin){
			return false;
		}
		root = deleteHelper(root,elem,widthMin,widthMax,heightMin,heightMax);
		return false;
	}

	
	public boolean saveMap(String fileName){
		CanvasPlus canvas = new CanvasPlus(fileName,widthMax,heightMax);
		canvas.addRectangle(widthMin, heightMin, widthMax, heightMax, Color.black, false);
		
		try {
			canvas.save(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	
	public PRNode findHelper(PRNode node, T elem,int widthMin,int widthMax,
			int heightMin,int heightMax){
		float x = elem.x;
		float y = elem.y;
		
		if(node instanceof PRQuadTree.GreyNode){
			int xMid = (widthMin + widthMax)/2;
			int yMid = (heightMin+heightMax)/2;
			if(x > xMid && y > yMid){//NE
				((PRQuadTree.GreyNode)node).NE = findHelper(((PRQuadTree.GreyNode)node).NE,
						elem,xMid,widthMax,yMid,heightMax);
			}else if(x >xMid && y<yMid){//SE
				((PRQuadTree.GreyNode)node).SE = findHelper(((PRQuadTree.GreyNode)node).SE,
						elem,xMid,widthMax,yMid,heightMax);
			}else if( x< xMid && y < yMid){//SW
				((PRQuadTree.GreyNode)node).SW = findHelper(((PRQuadTree.GreyNode)node).SW,
						elem,xMid,widthMax,yMid,heightMax);
			}else if( x < xMid && y >yMid){//NW
				((PRQuadTree.GreyNode)node).NW = findHelper(((PRQuadTree.GreyNode)node).NW,
						elem,xMid,widthMax,yMid,heightMax);
			}
		}	
		else if(node instanceof PRQuadTree.BlackNode){
			Point2D.Float nodeValues = ((PRQuadTree.BlackNode) node).element;
			return node;
		}else if (node instanceof PRQuadTree.WhiteNode){
			return node;
		}
		
		return node;
	}
	/**
	 * Finds an element(point) and returns the node that it is in. If the node is a point, then the quadrant is returned
	 * @param elem
	 * @return
	 */
	public PRNode find(T elem){
		if(elem.x > widthMax || elem.x < widthMin ||
				elem.y> heightMax || elem.y<heightMin){
			return null;
		}
		return findHelper(root, elem, widthMin, widthMax, heightMin, heightMax);
	}
	
	public Node PRNodeprint(PRNode node,Document results){
	
		if(node instanceof WhiteNode){
			Element white = results.createElement("white");
			return white;
		}
		if(node instanceof PRQuadTree.BlackNode){
			Element black = results.createElement("black");
			City city = (City) ((PRQuadTree.BlackNode) node).element;
			black.setAttribute("name", city.getName());
			black.setAttribute("x", Float.toString(city.x));
			black.setAttribute("y", Float.toString(city.y));
			return black;
		}
		if(node instanceof PRQuadTree.GreyNode){
			//TODO verify the ordering
			Element greyRoot = results.createElement("gray");
			greyRoot.setAttribute("x", Integer.toString(((PRQuadTree.GreyNode) node).x));
			greyRoot.setAttribute("y", Integer.toString(((PRQuadTree.GreyNode) node).y));
			greyRoot.appendChild(PRNodeprint(((PRQuadTree.GreyNode) node).NE,results));
			greyRoot.appendChild(PRNodeprint(((PRQuadTree.GreyNode) node).SE,results));
			greyRoot.appendChild(PRNodeprint(((PRQuadTree.GreyNode) node).SW,results));
			greyRoot.appendChild(PRNodeprint(((PRQuadTree.GreyNode) node).NW,results));
			return greyRoot;
		}
		return null;
	}
	
	public void printQuadTree(Element quadTree,Document results){
		quadTree.appendChild(PRNodeprint(root,results));
	}
	
	
	public TreeMap<String,City> rangeCities(int x, int y ,int radius){
		TreeMap<String,City> tree = new TreeMap<String,City>();
		for(PRNode n : this){
			if(n instanceof PRQuadTree.BlackNode){
				City city = (City) ((PRQuadTree.BlackNode) n).element;
				if(Point2D.Float.distance(x, y, city.x, city.y) <= radius){
					tree.put(city.getName(),city);
				}
			}
		}
		return tree;
	}
	
	public double distance(float x1,float y1,float x2, float y2){
		float distance = ((x1-x2)*(x1-x2)) + ((y1-y2)*(y1-y2));
		return Math.sqrt(distance);
	}
	
	public double closestDistance (int span, int xGrey,int yGrey,int x,int y){
		int xMax = xGrey + span;
		int xMin= xGrey -span;
		int yMax= yGrey +span;
		int yMin = yGrey-span;
		double distance = -1;
		//greater then x box
		if(x > xMax){
			//greater then y box
			if( y  > yMax){
				distance = distance(x,y,xMax,yMax);
			}else if(y < yMin){//less then y box
				distance = distance(x,y,xMax,yMin);
			}else{//in Y range
				distance = distance(x,y,xMax,y);
			}
		}else if (x < xMin){//smaller then x 
			if(y> yMax){//greater then y 
				distance = distance(x,y,xMin,yMax);
			}else if (y < yMin){//less then y
				distance = distance(x,y,xMin,yMin);
			}else{// in  y range
				distance = distance(x,y,xMin,y);
			}
		}else{//in x range
			if( y > yMax){
				distance = distance(x,y,x,yMax);

			}else if(y < yMin){
				distance = distance(x,y,x,yMin);
			}else{
				distance = distance(x,y,x,y);
			}
		}
		return distance;
	}

	public double getDistance(PRNode node, int x, int y){
		double distance = Double.MAX_VALUE;
		if(node instanceof PRQuadTree.BlackNode){
			Point2D.Float p = ((PRQuadTree.BlackNode) node).element;
			distance = distance(x,y,p.x,p.y);
		}else if(node instanceof PRQuadTree.GreyNode){
			int xGr = ((PRQuadTree.GreyNode) node).x;
			int yGr = ((PRQuadTree.GreyNode) node).y;
			int span = ((PRQuadTree.GreyNode) node).span;
			distance = closestDistance(span,xGr,yGr,x,y);
		}
		return distance;
	}
	
	public void enqueueChildren(PRNode node,PriorityQueue<Double> distanceQ,
			HashMap<Double,PRNode> distanceToElement,int x,int y,double distance){
		
		double branchDistance = getDistance(node,x,y);
		distanceQ.add(branchDistance);
		distanceToElement.put(branchDistance, node);
	}
	
	
	public City nearestCity(int x, int y){
		//TODO this method, have check to see if element already exists so both can be returned
		HashMap<Double,PRNode> distanceToElement = new HashMap<Double,PRNode>();
		PriorityQueue<Double> distanceQ = new  PriorityQueue<Double>();
		PRNode node = root;
		double initDistance = getDistance(node,x ,y );
		distanceQ.add(initDistance);
		distanceToElement.put(initDistance, root);
		while(!distanceQ.isEmpty()){
			double distance =  distanceQ.remove();
			PRNode p = distanceToElement.get(distance);
			if( p instanceof PRQuadTree.BlackNode){
				while(distanceQ.peek() == distance){
					distanceToElement.remove(distance);
					distanceQ.remove();
				}
				return (City)((PRQuadTree.BlackNode) p).element;
			}else if(p instanceof PRQuadTree.GreyNode){
				enqueueChildren(((PRQuadTree.GreyNode) p).NE,distanceQ,
						distanceToElement,x,y,distance);
				enqueueChildren(((PRQuadTree.GreyNode) p).NW,distanceQ,
						distanceToElement,x,y,distance);
				enqueueChildren(((PRQuadTree.GreyNode) p).SE,distanceQ,
						distanceToElement,x,y,distance);
				enqueueChildren(((PRQuadTree.GreyNode) p).SW,distanceQ,
						distanceToElement,x,y,distance);
			}
			
		}
		return null;
	}
	public boolean clearAll(){
		root = WhiteNode.getInstance();
		//TODO decide how to return
		return true;
	}
	
	public String toStringHelp(PRNode curr){
		String info = "";
		if(curr instanceof PRInner){
			info+= "NE: " + toStringHelp(((GreyNode)curr).NE);
			info+= "SE: " +toStringHelp(((GreyNode)curr).SE);
			info+= "SW: " + toStringHelp(((GreyNode)curr).SW);
			info+="NW: " +  toStringHelp(((GreyNode)curr).NW);
			info += "\n";
		}
		if(curr instanceof PRQuadTree.WhiteNode){
			return info;
		}
		
		if(curr instanceof PRQuadTree.BlackNode){
			info+= "x: " +((BlackNode)curr).element.x + "  ";
			info += "y: " + ((BlackNode)curr).element.y + "  ";
		}

		return info;

	}
	@Override
	public String toString(){
		return toStringHelp(root); 
	}


	@Override
	public Iterator<PRQuadTree.PRNode> iterator() {
		// TODO Auto-generated method stub
		return new PRIterator(root);
	}
}
