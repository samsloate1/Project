package prquadtree.test;

import prquadtree.PRQuadTree;

import Structures.City;

public class PRQuadTreeInsert {

	//Tests basic construction with a single city
	public static void test1() {
		City collegePark = new City("College Park", "ORANGE", 20, 20,0);
		PRQuadTree<City> tree = new PRQuadTree<City>(256, 256, collegePark);
		City DC = new City("DC","BLUE",150,150,0);
		tree.insert(DC);
		System.out.println(tree.toString());
	}
	
	//Test more cities construction
	public static void test2(){
		City collegePark = new City("College Park", "ORANGE", 20, 20,0);
		PRQuadTree<City> tree = new PRQuadTree<City>(256, 256, collegePark);
		City DC = new City("DC","BLUE",30,30,0);
		tree.insert(DC);
		System.out.println(tree.toString());
	}
	public static void main(String[]args){
				test2();
	}

}
