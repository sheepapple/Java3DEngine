import java.awt.Color;

public class Pyramid {
  //Vars
	double x, y, z, width, length, height, rotation = Math.PI*0.75;
	double[] RotAdd = new double[4];
	Color c;
	double x1, x2, x3, x4, x5, y1, y2, y3, y4, y5;
	TDpoly[] Polys = new TDpoly[5];
	double[] angle;

  //Pyramid constuctor
	public Pyramid(double x, double y, double z, double width, double length, double height, Color c)
	{
    //Creates pyramid using one set of coordinates and the length width and height inputs 
		Polys[0] = new TDpoly(new double[]{x, x+width, x+width, x}, new double[]{y, y, y+length, y+length},  new double[]{z, z, z, z},c);
		Display.TDPolygons.add(Polys[0]);
		Polys[1] = new TDpoly(new double[]{x, x, x+width}, new double[]{y, y, y, y},  new double[]{z, z+height, z+height},c);
		Display.TDPolygons.add(Polys[1]);
		Polys[2] = new TDpoly(new double[]{x+width, x+width, x+width}, new double[]{y, y, y+length},  new double[]{z, z+height, z+height},c);
		Display.TDPolygons.add(Polys[2]);
		Polys[3] = new TDpoly(new double[]{x, x, x+width}, new double[]{y+length, y+length, y+length},  new double[]{z, z+height, z+height},c);
		Display.TDPolygons.add(Polys[3]);
		Polys[4] = new TDpoly(new double[]{x, x, x}, new double[]{y, y, y+length},  new double[]{z, z+height, z+height},c);
		Display.TDPolygons.add(Polys[4]);
		
		this.c = c;
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.length = length;
		this.height = height;
		
		setRotAdd();
		updatePoly();
	}
	
	void setRotAdd()
	{
    //Used to calculate the rotation of the pyramid
		angle = new double[6];
		
		double xdif = - width + 0.00001;
		double ydif = - length + 0.00001;
		
		angle[0] = Math.atan(ydif/xdif);
		
		if(xdif<0)
			angle[0] += Math.PI;
		
		xdif = width + 0.00001;
		ydif = - length + 0.00001;
		
		angle[1] = Math.atan(ydif/xdif);
		
		if(xdif<0)
			angle[1] += Math.PI;

		xdif = width + 0.00001;
		ydif = length + 0.00001;
		
		angle[2] = Math.atan(ydif/xdif);
		
		if(xdif<0)
			angle[2] += Math.PI;
		
		xdif = - width + 0.00001;
		ydif = length + 0.00001;
		
		angle[3] = Math.atan(ydif/xdif);
		
		if(xdif<0)
			angle[3] += Math.PI;		
		
		RotAdd[0] = angle[0] + 0.25 * Math.PI;
		RotAdd[1] =	angle[1] + 0.25 * Math.PI;
		RotAdd[2] = angle[2] + 0.25 * Math.PI;
		RotAdd[3] = angle[3] + 0.25 * Math.PI;
	}

	void updatePoly()
	{
    //Updates the pyramid polygon
    //I probably couldve just updated this using a preexisting update method but this was easier for me
    //The rotation variables arent really used in my program but the dude in the tutorial I was following was using them and I kept it anyways because I thought I might've wanted to use it later
		for(int i = 0; i < 5; i++)
		{
			Display.TDPolygons.add(Polys[i]);
			Display.TDPolygons.remove(Polys[i]);
		}
		
		double radius = Math.sqrt(width*width + length*length);
		
			   x1 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[0]);
			   x2 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[1]);
			   x3 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[2]);
			   x4 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[3]);
			   x5 = x+width*0.5;
			   
			   y1 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[0]);
			   y2 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[1]);
			   y3 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[2]);
			   y4 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[3]);
			   y5 = y+length*0.5;
   
		Polys[0].x = new double[]{x1, x2, x3, x4};
		Polys[0].y = new double[]{y1, y2, y3, y4};
		Polys[0].z = new double[]{z, z, z, z};
			   
		Polys[1].x = new double[]{x1, x5, x2};
		Polys[1].y = new double[]{y1, y5, y2};
		Polys[1].z = new double[]{z, z+height, z};

		Polys[2].x = new double[]{x3, x2, x5};
		Polys[2].y = new double[]{y3, y2, y5};
		Polys[2].z = new double[]{z, z, z+height};

		Polys[3].x = new double[]{x3, x5, x4};
		Polys[3].y = new double[]{y3, y5, y4};
		Polys[3].z = new double[]{z, z+height, z};

		Polys[4].x = new double[]{x1, x4, x5};
		Polys[4].y = new double[]{y1, y4, y5};
		Polys[4].z = new double[]{z, z, z+height};
		
	}
    
}
