import java.awt.Color;

//This is the class that takes some coordinates and turns it into a 6 sided rectangular prism 
public class Box {
  //Coords
	double x, y, z, width, length, height, rotation = Math.PI*0.75;
  //Color
	Color c;
  //Array of our polygons
	TDpoly[] Polys = new TDpoly[6];
	
	public Box(double x, double y, double z, double width, double length, double height, Color c)
	{
    //Makes the 6 polygons using the variables to change values to like rotate them and move them and stuff to make a rectangular prism 
		Polys[0] = new TDpoly(new double[]{x, x+width, x+width, x}, new double[]{y, y, y+length, y+length},  new double[]{z, z, z, z},c);
		Display.TDPolygons.add(Polys[0]);
		Polys[1] = new TDpoly(new double[]{x, x+width, x+width, x}, new double[]{y, y, y+length, y+length},  new double[]{z+height, z+height, z+height, z+height}, c);
		Display.TDPolygons.add(Polys[1]);
		Polys[2] = new TDpoly(new double[]{x, x, x, x}, new double[]{y, y+length, y+length, y},  new double[]{z, z, z+height, z+height}, c);
		Display.TDPolygons.add(Polys[2]);
		Polys[3] = new TDpoly(new double[]{x+width, x+width, x+width, x+width}, new double[]{y, y+length, y+length, y},  new double[]{z, z, z+height, z+height}, c);
		Display.TDPolygons.add(Polys[3]);
		Polys[4] = new TDpoly(new double[]{x, x, x+width, x+width}, new double[]{y+length, y+length, y+length, y+length},  new double[]{z, z+height, z+height, z}, c);
		Display.TDPolygons.add(Polys[4]);
		Polys[5] = new TDpoly(new double[]{x, x, x+width, x+width}, new double[]{y, y, y, y},  new double[]{z, z+height, z+height, z}, c);
		Display.TDPolygons.add(Polys[5]);

    //Sets vars
		this.c = c;
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.length = length;
		this.height = height;
	}

  //empty constructor
  public Box(){
    
  }
}
