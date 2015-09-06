package prquadtree;

public class WhiteNode<T> implements Node<T>{
	
	private static WhiteNode instance = null;
	public static WhiteNode getInstance(){
		if(instance == null)
			instance = new WhiteNode();
		return instance;
	}
	private WhiteNode(){
		
	}
}
