public class Calculator {
	static double t = 0;
	static Vector W1, W2, ViewVector, RotationVector, DirectionVector, PlaneVector1, PlaneVector2;
	static Plane P;
	static double[] CalcFocusPos = new double[2];

  //Calculates and returns the new position of the polygons, used in the polygon class to update of the polygon
	static double[] CalculatePositionP(double[] ViewFrom, double[] ViewTo, double x, double y, double z)
	{		
		double[] projP = getProj(ViewFrom, ViewTo, x, y, z, P);
		double[] drawP = getDrawP(projP[0], projP[1], projP[2]);
		return drawP;
	}

  //Get the vector used to calculate the draw point
	static double[] getProj(double[] ViewFrom, double[] ViewTo, double x, double y, double z, Plane P)
	{
		Vector ViewToPoint = new Vector(x - ViewFrom[0], y - ViewFrom[1], z - ViewFrom[2]);

			   t = (P.NV.x*P.P[0] + P.NV.y*P.P[1] +  P.NV.z*P.P[2]
				 - (P.NV.x*ViewFrom[0] + P.NV.y*ViewFrom[1] + P.NV.z*ViewFrom[2]))
				 / (P.NV.x*ViewToPoint.x + P.NV.y*ViewToPoint.y + P.NV.z*ViewToPoint.z);

		x = ViewFrom[0] + ViewToPoint.x * t;
		y = ViewFrom[1] + ViewToPoint.y * t;
		z = ViewFrom[2] + ViewToPoint.z * t;
		
		return new double[] {x, y, z};
	}

  //Getting the draw point (x, y)
	static double[] getDrawP(double x, double y, double z)
	{
		double DrawX = W2.x * x + W2.y * y + W2.z * z;
		double DrawY = W1.x * x + W1.y * y + W1.z * z;		
		return new double[]{DrawX, DrawY};
	}

  //Get the rotation vector (used for changing view angle)
	static Vector getRotationVector(double[] ViewFrom, double[] ViewTo)
	{
		double dx = Math.abs(ViewFrom[0]-ViewTo[0]);
		double dy = Math.abs(ViewFrom[1]-ViewTo[1]);
		double xRot, yRot;
		xRot=dy/(dx+dy);		
		yRot=dx/(dx+dy);

		if(ViewFrom[1]>ViewTo[1])
			xRot = -xRot;
		if(ViewFrom[0]<ViewTo[0])
			yRot = -yRot;

			Vector V = new Vector(xRot, yRot, 0);
		return V;
	}

  //Set predetermined info, used for setting up the program when it intially starts so that nothing is null and it starts up properly
	static void SetPrederterminedInfo()
	{
		ViewVector = new Vector(Display.ViewTo[0] - Display.ViewFrom[0], Display.ViewTo[1] - Display.ViewFrom[1], Display.ViewTo[2] - Display.ViewFrom[2]);			
		DirectionVector = new Vector(1, 1, 1);				
		PlaneVector1 = ViewVector.Product(DirectionVector);
		PlaneVector2 = ViewVector.Product(PlaneVector1);
		P = new Plane(PlaneVector1, PlaneVector2, Display.ViewTo);

		RotationVector = Calculator.getRotationVector(Display.ViewFrom, Display.ViewTo);
		W1 = ViewVector.Product(RotationVector);
		W2 = ViewVector.Product(W1);

		CalcFocusPos = Calculator.CalculatePositionP(Display.ViewFrom, Display.ViewTo, Display.ViewTo[0], Display.ViewTo[1], Display.ViewTo[2]);
		CalcFocusPos[0] = Display.zoom * CalcFocusPos[0];
		CalcFocusPos[1] = Display.zoom * CalcFocusPos[1];
	}
}
