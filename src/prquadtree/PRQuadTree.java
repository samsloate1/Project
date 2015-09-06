package prquadtree;

import java.awt.geom.Point2D;
import java.util.Comparator;

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
	public boolean delete(T elem){
		//Pass the city from the dictionary
		if(elem.x > widthMax || elem.x < widthMin ||
				elem.y> heightMax || elem.y<heightMin){
			return false;
		}
		root = deleteHelper(root,elem,widthMin,widthMax,heightMin,heightMax);
		return false;
	}
	
	public T find(T elem){
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
