package comparator;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.Comparator;

import Structures.City;

/**
 * Comparator that compares first  by the y coordinate and if equivalent compares the x
 * @author Sam
 *
 */
public class CityLocationComparator implements Comparator<Point2D.Float> {
	@Override
	public int compare(Float o1, Float o2) {
		if(o1.y > o2.y){
			return 1;
		}else if (o1.y <o2.y){
			return -1;
		}else{
			if(o1.x > o2.x)
				return 1;
			else if (o1.x < o2.x)
				return -1;
			else
				return 0;
		}
	}
}
