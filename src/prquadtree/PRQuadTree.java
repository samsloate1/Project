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

import cmsc420.drawing.CanvasPlus;

import Structures.City;

public class PRQuadTree<T extends Point2D.Float> {
	//width = x
	//height = y
	private int heightMax,heightMin,widthMax,widthMin = -1;
	private PRNode root = null;

	interface PRNode{};
	interface PRInner extends PRNode{};
	interface PRLeaf extends PRNode{};
	private static class WhiteNode implements PRLeaf{
		private static WhiteNode instance = null;
		public static WhiteNode getInstance(){
			if(instance == null)
				instance = new WhiteNode();
			return instance;
		}
		private WhiteNode(){

		}
	}
	private class BlackNode implements PRLeaf{
		T element;

		public BlackNode(T elem){
			element = elem;
		}
	}
	private class GreyNode implements PRInner{

		public PRNode NE,NW,SW,SE;
		public GreyNode(){
			NE=NW=SW=SE = WhiteNode.getInstance();
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

	
//
//	public boolean greyNode(PRNode curr, T elem,int  width,
//			int widthMax,int heightMin,int heightMax){
//		int xMid = (widthMax+widthMin)/2;
//		int yMid = (heightMax+heightMin)/2;
//		float x = elem.x;
//		float y = elem.y;
//		if( x > xMid && y > yMid){
//			//NE
//			return insertHelper(((PRQuadTree.GreyNode) curr).NE,elem,
//					xMid,widthMax,yMid,heightMax);
//		}else if( x >xMid && y < yMid  ){
//			//SE
//			return insertHelper(((PRQuadTree.GreyNode) curr).SE,elem,
//					xMid,widthMax,heightMin,yMid);			
//		}else if ( x< xMid && y <yMid){
//			//SW
//			return insertHelper(((PRQuadTree.GreyNode)curr).SW,elem,
//					widthMin,xMid,heightMin,yMid);
//		}else{
//			return insertHelper(((PRQuadTree.GreyNode)curr).NW,elem,
//					widthMin,xMid,yMid,heightMax);
//		}
//	}
//
//	public boolean blackNode(PRNode curr, T elem,int widthMin,int widthMax,
//			int  heightMin,	int heightMax){
//		BlackNode temp = (BlackNode) curr;
//		curr = new GreyNode();
//		int xMid = (widthMax+widthMin)/2;
//		int yMid = (heightMax+heightMin)/2;
//		float x = temp.element.x;
//		float y = temp.element.y;
//		if(elem.x == x && elem.y == y){
//			return false;
//		}
//		if( x > xMid && y > yMid){
//			((PRQuadTree.GreyNode)curr).NE = temp;
//			return insertHelper(((PRQuadTree.GreyNode) curr),elem,
//					xMid,widthMax,yMid,heightMax);
//		}else if( x >xMid && y < yMid  ){
//			//SE
//			((PRQuadTree.GreyNode)curr).SE = temp;
//			return insertHelper(((PRQuadTree.GreyNode) curr),elem,
//					xMid,widthMax,heightMin,yMid);			
//		}else if ( x< xMid && y <yMid){
//			//SW
//			((PRQuadTree.GreyNode)curr).SW = temp;
//			return insertHelper(((PRQuadTree.GreyNode)curr),elem,
//					widthMin,xMid,heightMin,yMid);
//		}else{
//			((PRQuadTree.GreyNode)curr).NW = temp;
//			return insertHelper(((PRQuadTree.GreyNode)curr),elem,
//					widthMin,xMid,yMid,heightMax);
//		}
//	}

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
			node = new GreyNode();
			//put the info back  in  then call insertHelper  on this grey node again
			float x = temp.element.x;
			float y = temp.element.y;
			int midX = (widthMin+widthMax)/2;
			int midY = (heightMin+heightMax)/2;
			//For each direction
			if(x > midX && y > midY){//NE
				((PRQuadTree.GreyNode)node).NE = temp;
			}else if(x >midX && y < midY){//SE
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
	
	
	private class DistanceComparator<T extends Point2D.Float> implements Comparator{
		float x = -1;
		float y = -1;
		public DistanceComparator(float x,float y ){
			this.x =x;
			this.y = y;
		}
		@Override
		public int compare(Object arg0, Object arg1) {
			Point2D.Float point1 = (Point2D.Float)arg0;
			Point2D.Float point2 = (Point2D.Float)arg1;
			double dist1 = Point2D.Float.distance(x, y, point1.x, point1.y);
			double dist2 = Point2D.Float.distance(x, y, point2.x, point2.y);
			if(dist1 > dist2){
				return 1;
			}else if(dist1 < dist2){
				return -1;
			}
			return 0;
		}
	}
//	public T findNearest(T elem,PRNode node){
//		DistanceComparator<T> dist = new DistanceComparator<T>(elem.x,elem.y);
//		//TODO figure out the logic
//		PriorityQueue<T> queue = new PriorityQueue<T>(20,dist);
//		HashMap<T,PRNode> map = new HashMap<T,PRNode>();
//		queue.add(root.distance());
//		//TODO figure out how to sort by the number
//		while(!queue.isEmpty()){
//			Point2D.Float curr = queue.remove();
//			Point2D.Float check = queue.peek();
//			while(check == curr){
//				queue.remove();
//				check = queue.peek();
//			}
//			check = queue.remove();
//			if(check != null){
//				if(map.containsKey(check)){
//					//return map.get(check);
//				}
//			}
//			//TODO add if this is a spatial object
//			if(curr instanceof PRQuadTree.BlackNode){
//				//TODO determine the distance and return
//				return  (T) (((PRQuadTree.BlackNode)map.get(curr)).element);
//			}else if(node instanceof PRQuadTree.GreyNode){
//				//TODO for all directions see if the distance is more
//				//need to add the distances then add to the hashmap as well;
//				queue.add(((PRQuadTree.GreyNode) node).NE);
//				queue.add(((PRQuadTree.GreyNode) node).SE);
//				queue.add(((PRQuadTree.GreyNode) node).SW);
//				queue.add(((PRQuadTree.GreyNode) node).NW);
//			}
//		}
//		return null;
//	}
	
	public boolean inRadius(float x,float y, float radius, float xTest,float yTest){
		if((((xTest-x)*(xTest-x)) +((yTest-y)*(yTest-y))) < (radius*radius)){
			return true;
		}
		return false;
	}

	private List<City> rangeCitiesHelper(PRNode node, float x, float y,
			float radius, int widthMin,int widthMax,int heightMin,int heightMax) {
		// TODO Auto-generated method stub
		LinkedList<City> cities = new LinkedList<City>();
		if(node instanceof PRInner){
			//Make sure that things inside this range can be in the radius.
			//Need to just check for the min or max depending on if x < or > 
			
		}
		if(node instanceof WhiteNode){
			return new LinkedList<City>();
		}
		if(node instanceof  PRQuadTree.BlackNode){
			Point2D.Float city = ((PRQuadTree.BlackNode) node).element;
			if(inRadius(x,y,radius,city.x,city.y)){
				List<City> l = new LinkedList<City>();
				return l;
				//TODO add this city to list
			}
		}
		return cities;
		
	}
	
	/**
	 * Checks to see if an element is in a tree
	 * @param elem
	 * @return
	 */
	public List<City> rangeCities(float x,float y, float radius,boolean saveMap){
		//rangeCitiesHelper(root,x,y,radius,saveMap);
		
		//TODO implement this
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
}
