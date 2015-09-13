package prquadtree.test;

import prquadtree.PRQuadTree;
import Structures.City;

public class DistanceTest {
	public static void test1(){
		City collegePark = new City("College Park", "ORANGE", 20, 20,0);
		PRQuadTree<City> tree = new PRQuadTree<City>(256, 256, collegePark);
		City DC = new City("DC","BLUE",150,150,0);
		City KC = new City("KC", "ORANGE", 10, 10,0);
		City OC= new City("OC", "ORANGE", 40, 40,0);
		City NYC = new City("NYC", "ORANGE", 75, 75,0);
		tree.insert(DC);
		tree.insert(KC);
		tree.insert(OC);
		tree.insert(NYC);
		System.out.println(tree.toString());
		City c = tree.nearestCity(50, 50);
		System.out.println(c.getName());
		tree.saveMap("image");
	}
	public static void main (String[]args){
		test1();
	}
}
