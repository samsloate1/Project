package avltree.test;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.Comparator;

import Structures.City;
import avltree.AVLTree;
import comparator.CityLocationComparator;

public class AVLTest {
	public void test1(){
		Comparator<Point2D.Float> t = new CityLocationComparator();
		AVLTree<Point2D.Float,String> avl = new AVLTree<Point2D.Float,String>(t);
		Point2D.Float point = new Point2D.Float(0,0);
		avl.insert(point, "city 1");
	}
}
