
import java.awt.Color;

public class TDpoly {

    Color c;
    double[] x;
    double[] y;
    double[] z;
    boolean inView = true;
    double[] CalcPos, newX, newY;
    PolygonObj DrawablePolygon;
    double AvgDist;

    public TDpoly(double[] x, double[] y, double[] z, Color color)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.c = color;
        createPolygon();
    }
    

    void createPolygon(){

        DrawablePolygon = new PolygonObj(new double[x.length], new double[x.length], c);
    }

    void updatePoly(){
        newX = new double[x.length];
        newY = new double[x.length];
        inView = true;
        for(int i = 0; i < x.length && Display.canPass != false; i++){
            CalcPos = Calculator.CalculatePositionP(Display.ViewFrom, Display.ViewTo, x[i], y[i], z[i]);
			newX[i] = (Main.ss.getWidth()/2 - Calculator.CalcFocusPos[0]) + CalcPos[0] * Display.zoom;
			newY[i] = (Main.ss.getHeight()/2 - Calculator.CalcFocusPos[1]) + CalcPos[1] * Display.zoom;			
			if(Calculator.t < 0)
				inView = false;
        }

        calcLighting();

        DrawablePolygon.inView = this.inView;
        DrawablePolygon.updatePolygonObj(newX, newY);
        AvgDist = getDist();
    }

    void calcLighting()
	{
		Plane lightingPlane = new Plane(this);
		double angle = Math.acos(((lightingPlane.NV.x * Display.LightDir[0]) + 
		    (lightingPlane.NV.y * Display.LightDir[1]) + (lightingPlane.NV.z * Display.LightDir[2]))
		        /(Math.sqrt(Display.LightDir[0] * Display.LightDir[0] + Display.LightDir[1] * Display.LightDir[1] + Display.LightDir[2] * Display.LightDir[2])));


        DrawablePolygon.lighting = 0.2 + 1 - Math.sqrt(Math.toDegrees(angle)/180);

        if(DrawablePolygon.lighting > 1){
			DrawablePolygon.lighting = 1;
        }

		if(DrawablePolygon.lighting < 0){
			DrawablePolygon.lighting = 0;
        }
	}

    double getDist(){
        double total = 0;
        for(int i = 0; i < x.length; i++){
            total += getDistToP(i);
        }
       return total/x.length; 
    }

    double getDistToP(int i){

        return Math.sqrt( (Display.ViewFrom[0] - x[i])*(Display.ViewFrom[0] - x[i]) + (Display.ViewFrom[1] - y[i])*(Display.ViewFrom[1] - y[i]) + (Display.ViewFrom[2] - z[i])*(Display.ViewFrom[2] - z[i]));
    }
} 