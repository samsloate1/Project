package Structures;

import java.awt.geom.Point2D;

/**
 * City object that contains name, color and xy coordinates of the city
 * @author Sam
 *
 */
public class City extends Point2D.Float{
	private String name = null;
	private String color = null;
	private int radius = -1;
	public City(String name,String color,int x ,int y, int radius){
		this.name = name;
		this.color = color;
		this.x = x;
		this.y = y;
		this.radius = radius;
	}
	public String getName() {
		return name;
	}
	public String getColor() {
		return color;
	}
	public int getRadius(){
		return radius;
	}
}
