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
	public static void testInsertSame(){
		City collegePark = new City("College Park", "ORANGE", 20, 20,0);
		PRQuadTree<City> tree = new PRQuadTree<City>(256, 256, collegePark);
		City DC = new City("DC","BLUE",30,30,0);
		tree.insert(DC);
		System.out.println(tree.toString());
	}
	//basic delete test
	public static void deleteCity(){
		City collegePark = new City("College Park", "ORANGE", 20, 20,0);
		PRQuadTree<City> tree = new PRQuadTree<City>(256, 256, collegePark);
		System.out.println("Added: " + tree.toString());
		tree.delete(collegePark);
		System.out.println("Post Delete: " + tree.toString());
	}
	
	public static void advDelete(){
		City collegePark = new City("College Park", "ORANGE", 20, 20,0);
		PRQuadTree<City> tree = new PRQuadTree<City>(256, 256, collegePark);
		City DC = new City("DC","BLUE",150,150,0);
		tree.insert(DC);
		System.out.println("two elements different quadrants : " + tree.toString());
		tree.delete(DC);
		System.out.println("After deleting one entry: " + tree.toString());
	}
	public static void main(String[]args){
		//testInsertSame();
		//deleteCity();
		advDelete();
	}

}
