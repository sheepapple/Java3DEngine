public class Vector {
    double x;
    double y;
    double z;

  //Creates a "vector" its not really a typical vector but its what worked for me
    public Vector(double x, double y, double z){
        double Length = Math.sqrt(x*x + y*y + z*z);
        if(Length > 0){
        this.x = x/Length;
        this.y = y/Length;
        this.z = z/Length;
        }
    }

    //Used to calculate changes
    Vector Product(Vector V){

        Vector CVector = new Vector(
				y * V.z - z * V.y,
				z * V.x - x * V.z,
				x * V.y - y * V.x);
		return CVector;		
    }

}


