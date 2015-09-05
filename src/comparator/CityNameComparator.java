package comparator;

import java.util.Comparator;

import Structures.City;

public class CityNameComparator implements Comparator<String> {

	@Override
	public int compare(String name1, String name2) {
		if(name1.compareTo(name2) > 0)
			return 1;
		else  if(name1.compareTo(name2)< 0)
			return -1;
		else 
			return 0;
	}

}
