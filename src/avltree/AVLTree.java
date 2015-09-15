package avltree;

import java.util.Comparator;

public class AVLTree<K,V>{
	private AVLNode<K,V> root;
	private Comparator<K> comparator;
	public AVLTree(Comparator<K> compare){
		root = null;
		setComparator(compare);
	}
	private class AVLNode<K1,V1>{
		K1 key;
		V1 value;
		int height;
		AVLNode<K1,V1> left;
		AVLNode<K1,V1> right;
		private AVLNode(K1 key,V1 value){
			this.key = key;
			this.value = value;
			left = null;
			right = null;
			height = 0;
		}
	}

	private int height(AVLNode<K,V> node){
		if(node == null){
			return -1;
		}
		else{
			return node.height;
		}
	}
	public int max(int a, int b){
		if(a >= b)
			return a;
		return b;
	}
	
	public void setComparator(Comparator compare){
		this.comparator =  compare;
	}
	private AVLNode<K,V> rightRotate(AVLNode<K,V> node){
		AVLNode<K,V> n = node.left;
		node.left = n.right;
		n.right = node;
		node.height = max(height(node.left),height(node.right)) +1;
		n.height = max(height(n.right),height(n)) + 1;
		return null;
	}
	private AVLNode<K,V> leftRotate(AVLNode<K,V> node){
		AVLNode<K,V> n = node.right;
		node.right = n.left;
		n.left = node;
		node.height = max(height(node.left),height(node.right)) +1;
		n.height = max(height(n.left),height(n)) + 1;
		return null;
	}
	private AVLNode<K,V> lrRotate(AVLNode<K,V> node){
		node.left = leftRotate(node.left);
		return rightRotate(node);
	}
	private AVLNode<K,V> rlRotate(AVLNode<K,V> node){
		node.right = rightRotate(node.right);
		return leftRotate(node);
	}
	private AVLNode<K,V> insert(K key,V value, AVLNode<K,V> node){
		if(node == null){
			node = new AVLNode<K,V>(key,value);
		}else if(comparator.compare(node.key,key) > 0){
			node.left = insert(key,value,node.left);
			if(height(node.left) - height(node.right) == 2){//TODO calculate the hiehgt difference
				if(comparator.compare(node.key, node.left.key) < 0){
					node = rightRotate(node);
				}else{
					node = lrRotate(node);
				}
				node.height = max(height(node.left),height(node.right))+1;
			}
		}else if (comparator.compare(node.key, key) <0){
			node.right = insert(key,value,node.right);
			if(height(node.right) - height(node.left) == 2){
				if(comparator.compare(node.key, node.left.key) > 0){
					node = leftRotate(node);
				}else{
					node = rlRotate(node);
				}
			}
			node.height = max(height(node.left),height(node.right))+1;
		}else{
			//TODO error handling
		}
		return node;
	}
	public boolean insert(K key, V value){
		insert(key,value,root);
		return true;
	}
	public void clearAll(){
		root = null;
	}
}
