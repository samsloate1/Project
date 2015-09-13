package prquadtree;

import java.util.Iterator;
import java.util.Stack;

import prquadtree.PRQuadTree.PRNode;

public class PRIterator implements Iterator<PRNode> {

	Stack<PRNode>nodeStack;
	PRNode node;
	public PRIterator(PRNode root){
		nodeStack= new Stack<PRNode>();
		nodeStack.push(root);
	}
	@Override
	public boolean hasNext() {
		return !nodeStack.isEmpty();
	}

	@Override
	public PRNode next() {
		node = nodeStack.pop();
		if(node instanceof PRQuadTree.GreyNode){
			nodeStack.push(((PRQuadTree.GreyNode) node).NE);
			nodeStack.push(((PRQuadTree.GreyNode) node).SE);
			nodeStack.push(((PRQuadTree.GreyNode) node).SW);
			nodeStack.push(((PRQuadTree.GreyNode) node).NW);
		}
		
		return node;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
