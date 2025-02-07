import java.awt.Graphics;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.View;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.concurrent.TimeUnit;

import java.awt.Image;
import java.io.IOException;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.color.*;

//This is the main class, well not the "main" class but you know what I mean
public class Display extends JPanel implements KeyListener{

    //My absolute metric ton of variables, I did my best to organize them


    //Arraylists holding all the shapes of each type
    //While Boxes and Pyramids are technically made out of TDpoly's, it made more sense to put them each into their own arraylist
    static ArrayList<TDpoly> TDPolygons = new ArrayList<TDpoly>();
    static ArrayList<Pyramid> Pyramids = new ArrayList<Pyramid>();
    static ArrayList<Box> Boxes = new ArrayList<Box>();
    static Box tempBox = new Box();

    //ViewFrom is where the player(camera) is located in the map
    // ViewTo is where the player is looking
    static double[] ViewFrom = new double[]{-10, 12, 2};
    static double[] ViewTo = new double[]{0, 0, 200};
    static double[] LightDir = new double[] {1, 1, 1};

    //Used for movement and camera views
    static double zoom = 1000, MinZoom = 500, MaxZoom = 2500, MovementSpeed = 0.1;

    //Used for FPS checks and overall performance regulation
    static double MaxFPS = 144;
    static double thisFPS = 0;
    static double PauseTime = 1000/60;
    static double LastRefresh = 0;
    static double Checks = 0;
    
  //Used for vector manipulations and changing the position of the "sun" thats used for lighting
    double VertLook = -3;
    double HorLook = 0;
    double SunPos = 0;

  //Determines the order that the polygons are drawn in on the screen
    static int[] NewOrder;
    static boolean[] Keys = new boolean[13];

  //Used to keep track of jump height and when youre in a jump or in a fall
    static boolean inJump = false, inFall = false;
    static double hTracker = 0.2;
    static double startHeight = 0;

  //Custom Colors
    Color darkG = new Color(250, 250, 250);
    Color lightG = new Color(200, 200, 200);
    Color brown = new Color(140, 70, 20);
    Color darkGreen = new Color(0, 100, 0);

  //Used for hit detection
    static boolean canPass = true;
    static boolean canStand;
    static double ViewFromDupe[] = new double[3];
  
  //Keeps track of the last time a certain key was pressed
    static long lastPressed = 0;

  //Outlines boolean which sets outlines on or off, I dont have a keyPressed to change this because outlines lag the system so hard I dont even want the option available
    static Boolean Outlines = true;

  //Gui images
    static Image treeHotbar = null;
    static Image boxHotbar = null;
    static Image handBox = null;
    static Image handTree = null;
  
  //Used for GUI scaling, its pretty scuffed but I implemented the GUI in a pretty short timeframe so I think its okay
    static double ssDivide = (Main.ss.getWidth() * Main.ss.getHeight())/(2560*1440);

    public Display()
    {

      //Scaling code thats pretty bad and bandaid but I did this last second because I realized my scaling was really bad when the windows were larger
        if(Main.ss.getWidth()*Main.ss.getHeight() > 1280*720){
          ssDivide /= 2;
        }

      //Starts out with the cube being held
        if(!Keys[11] && !Keys[12]){
            Keys[11] = true;
        }

      //Initializes all the images
      //Do you like my art? 
        try{
            treeHotbar = ImageIO.read(new File("treeHotbar.png"));
        } catch (IOException e){
            e.printStackTrace();
        }

        try{
            boxHotbar = ImageIO.read(new File("boxHotbar.png"));
            } catch (IOException e){
            e.printStackTrace();
        }
        try{
            handBox = ImageIO.read(new File("holdingBox.png"));
        } catch (IOException e){
            e.printStackTrace();
        }

        try{
            handTree = ImageIO.read(new File("holdingTree.png"));
            } catch (IOException e){
            e.printStackTrace();
        }

        this.addKeyListener(this);
		    setFocusable(true);		


      //Adds a patch of trees
        for(int treeI = 0; treeI < 8; treeI++){
          double treeXrand = (Math.random()*19) + -4;
          double treeYrand = (Math.random()*19) + -4;
          Boxes.add(new Box(treeXrand+0.625, treeYrand+0.625, -0.1, 0.75, 0.75, 1.5, brown));
          Pyramids.add(new Pyramid(treeXrand, treeYrand, 1.4, 2, 2, 5, new Color((int)Math.random()*128, (int)Math.random()*128, (int)Math.random()*128)));
        }

      //Adds some boxes to parkour on
        Boxes.add(new Box(-10, 0, 0, 5, 5, 1, lightG));
        Boxes.add(new Box(-18, 0, 0, 5, 5, 2, lightG));
        Boxes.add(new Box(-25, 0, 0, 5, 5, 3, lightG));
        Boxes.add(new Box(-33, 0, 0, 5, 5, 4, lightG));
        Boxes.add(new Box(-41, 0, 0, 5, 5, 5, lightG));

      //Floor generation and Grass generation
        for(int i = -14; i<15; i++){
            for(int j = -14; j<15; j++){
              
                
                TDPolygons.add( new TDpoly(new double[]{i, i, i+1, i+1}, new double[]{j, j+1, j+1, j}, new double[]{-0.1, -0.1, -0.1, -0.1}, darkGreen) );
                
                for(int k = 14; k < 15; k++){
                    int min = i;
                    int max = i+1;
                    int range = max - min;
                    double rand = (Math.random()*range) + min;
                    double jrand = Math.random() + j;
                    
                    TDPolygons.add( new TDpoly(new double[]{rand, rand, rand+0.05, rand+0.05}, new double[]{jrand, jrand+0.05, jrand+0.05, jrand}, new double[]{-0.1, -0.1, -0.1, 0.5}, Color.green) );
                }

                for(int k = 15; k < 15; k++){
                    int min = i;
                    int max = i+1;
                    int range = max - min;
                    double rand = (Math.random()*range) + min;
                    double jrand = Math.random() + j;
                      
                    Pyramids.add( new Pyramid(rand, jrand, -0.1, 0.05, 0.05, 0.5, Color.green) );
                }
              }
            }
        }


  //Main method used to update the screen and implement the gui elements
    public void paintComponent(Graphics g)
    {
      //System.out.println("ViewTo " + ViewTo[0] + " ," + ViewTo[1] + " ," + ViewTo[2] + " ,");
      //System.out.println("ViewFrom " + ViewFrom[0] + " ," + ViewFrom[1] + " ," + ViewFrom[2] + " ,");
        g.setColor(new Color(140, 180, 180));
        g.fillRect(0, 0, (int)Main.ss.getWidth(), (int)Main.ss.getHeight());
        Controls();
        camControls();
        ControlSunAndLight();
        Calculator.SetPrederterminedInfo();

        for(int i = 0; i < TDPolygons.size(); i++){
            TDPolygons.get(i).updatePoly();
        }

        setOrder();

        for(int i = 0; i < NewOrder.length; i++){
            TDPolygons.get(NewOrder[i]).DrawablePolygon.drawPolygon(g);
        }

        jump();

        if(Keys[11] == true){
          g.drawImage(boxHotbar, (int)((Main.ss.getWidth()/2) - boxHotbar.getWidth(this)/2), (int)(Main.ss.getHeight() - (90 * ssDivide * 3)), (int)(boxHotbar.getWidth(this) * (ssDivide * 3)), (int)(boxHotbar.getHeight(this) * (ssDivide * 3)), this);
          g.drawImage(handBox, (int)Main.ss.getWidth()+ (int)(400 * ssDivide) -(int)(handBox.getWidth(this)*(ssDivide*3)), (int)Main.ss.getHeight() + (int)(400*ssDivide) -(int)(handBox.getHeight(this)*(ssDivide*3)), (int)(handBox.getWidth(this)*(ssDivide*3)), (int)(handBox.getHeight(this)*(ssDivide*3)), this);
        }
        else if(Keys[12] == true){
            g.drawImage(treeHotbar, (int)((Main.ss.getWidth()/2) - treeHotbar.getWidth(this)/2), (int)(Main.ss.getHeight() - (90 * ssDivide * 3)), (int)(treeHotbar.getWidth(this) * (ssDivide * 3)), (int)(treeHotbar.getHeight(this) * (ssDivide * 3)), this);
            g.drawImage(handTree, (int)Main.ss.getWidth()+ (int)(400 * ssDivide) -(int)(handTree.getWidth(this)*(ssDivide*3)), (int)Main.ss.getHeight() + (int)(400*ssDivide) -(int)(handTree.getHeight(this)*(ssDivide*3)), (int)(handTree.getWidth(this)*(ssDivide*3)), (int)(handTree.getHeight(this)*(ssDivide*3)), this);
        }
      
        drawCrosshair(g);
        g.drawString("FPS: " + (int)thisFPS, 25, 25);
        g.drawString("Controls:", 25, 40);
        g.drawString("WASD: Movement", 25, 55);
        g.drawString("Spacebar: Jump", 25, 70);
        g.drawString("Arrow Keys: Look around", 25, 85);
        g.drawString("Num Keys 1&2: choose item from hotbar", 25, 100);
        g.drawString("Enter Key: Place selected item", 25, 115); 
        g.drawString("Shift key: Sprint", 25, 130);
        PauseAndRefresh();
    } 

  //Draws the crosshair on the screen
    void drawCrosshair(Graphics g){
        int aimSight = 15;
        g.setColor(Color.white);

		 g.drawLine((int)(Main.ss.getWidth()/2 - aimSight), (int)(Main.ss.getHeight()/2), (int)(Main.ss.getWidth()/2 + aimSight), (int)(Main.ss.getHeight()/2));
		 g.drawLine((int)(Main.ss.getWidth()/2), (int)(Main.ss.getHeight()/2 - aimSight), (int)(Main.ss.getWidth()/2), (int)(Main.ss.getHeight()/2 + aimSight));	
    }



  //This is supposed to set the order the polygons are drawn on the screen, so that like things closer to you are drawn on top of things further, but -
  // - the code for it is quite terrible I'll admit, but I was too focused on other things to fix it up, in the future after I turn this in, I think this will be my priority to work on
    void setOrder()
	  {
		double[] k = new double[TDPolygons.size()];
		NewOrder = new int[TDPolygons.size()];
		
		for(int i=0; i<TDPolygons.size(); i++)
		{
			k[i] = TDPolygons.get(i).AvgDist;
			NewOrder[i] = i;
		}
		
	    double temp;
	    int tempr;	    
		for (int a = 0; a < k.length-1; a++)
			for (int b = 0; b < k.length-1; b++)
				if(k[b] < k[b + 1])
				{
					temp = k[b];
					tempr = NewOrder[b];
					NewOrder[b] = NewOrder[b + 1];
					k[b] = k[b + 1];
					   
					NewOrder[b + 1] = tempr;
					k[b + 1] = temp;
				}
	}


  //Pausing between frames so the system isnt overloaded, also can use the MaxFPS variable to change the maxiumum fps
    void PauseAndRefresh(){
        long timeSLU = (long) (System.currentTimeMillis() - LastRefresh); 
		Checks++;			
		if(Checks >= 15)
		{
      thisFPS = 1000.0/timeSLU/Checks*3;
      
			LastRefresh = System.currentTimeMillis();
			Checks = 0;
		}

		
		if(timeSLU < 1000.0/MaxFPS){

			try {
				Thread.sleep((long) (1000.0/MaxFPS - timeSLU));
			} catch (InterruptedException e){
				e.printStackTrace();
			}	
		}
				
		LastRefresh = System.currentTimeMillis();
		
		repaint();
    }

  //Controls sun, light and lighting
    void ControlSunAndLight()
	{
		SunPos += 0.01;		
		double mapSize = GenerateTerrain.mapSize * GenerateTerrain.mapSize;
		LightDir[0] = mapSize/2 - (mapSize/2 + Math.cos(SunPos) * mapSize * 10);
		LightDir[1] = mapSize/2 - (mapSize/2 + Math.sin(SunPos) * mapSize * 10);
		LightDir[2] = -200;

	}

  //Key inputs
  // I used a boolean array to keep track of what was being pressed, and also so that if you hold down a key it stays inputted rather than inputting once and stopping
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_UP){
            Keys[0] = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            Keys[1] = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            Keys[2] = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            Keys[3] = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_W){
            Keys[4] = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_A){
            Keys[5] = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_S){
            Keys[6] = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_D){
            Keys[7] = true;
        }

      //Places tree or cube down depending on whats selected
        if(e.getKeyCode() == KeyEvent.VK_ENTER && Keys[8] != true){
            double[] intersect = calcInter();
            if(intersect != null){
                if(Keys[11]){
                    Boxes.add(new Box(intersect[0]-0.75/2, intersect[1]-0.75/2, intersect[2], 1, 1, 1, lightG));
                }
                else if(Keys[12]){
                    Boxes.add(new Box(intersect[0]-0.75/2, intersect[1]-0.75/2, -0.1, 0.75, 0.75, 1.5, brown));
                    Pyramids.add(new Pyramid(intersect[0]-1, intersect[1]-1, 1.4, 2, 2, 5, Color.green));
                }
            }
          Keys[8] = true;   
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE && inJump != true && inFall != true){
            startHeight = ViewFrom[2];
            hTracker = 0.25;
            inJump = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            System.exit(0);
        }
        if(e.getKeyCode() == KeyEvent.VK_SHIFT){
          Keys[10] = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_1){
            Keys[12] = false;
            Keys[11] = true;
        }
        if(e.getKeyCode() == KeyEvent.VK_2){
            Keys[11] = false;
            Keys[12] = true;
        }
     
    }

  
    public void keyTyped(KeyEvent e){

    }

  //Key releases
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_UP){
            Keys[0] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            Keys[1] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            Keys[2] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            Keys[3] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_W){
            Keys[4] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_A){
            Keys[5] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_S){
            Keys[6] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_D){
            Keys[7] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            Keys[8] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            Keys[9] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_SHIFT){
          Keys[10] = false;
        }
        if(e.getKeyCode() == KeyEvent.VK_1){
        }
        if(e.getKeyCode() == KeyEvent.VK_2){
        }
    }


  //Camera controls, (arrow keys)
    void camControls(){

        if(Keys[0]){
		VertLook += 0.03;
		}
		if(Keys[1]){
		HorLook -= 0.05;
		}
		if(Keys[2]){
		VertLook -= 0.03;	
		}
		if(Keys[3]){
		HorLook += 0.05;
		}

		if(VertLook>0.9)
		VertLook = 0.9;

		if(VertLook<-0.9)
			VertLook = -0.9;
		

        


    }

  // WASD asnd Shift keys
    void Controls()
	{
		Vector ViewVector = new Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2]);
		double xMove = 0, yMove = 0, zMove = 0;
		Vector VerticalVector = new Vector (0, 0, 1);
		Vector SideViewVector = ViewVector.Product(VerticalVector);

        double barrier = 3;

        double oldXMove = xMove;
        double oldYMove = yMove;
        double oldZMove = zMove;
        
		if(Keys[4])
		{
            xMove += ViewVector.x ;
			yMove += ViewVector.y ;
            ViewFromDupe[0] = ViewFrom[0] + xMove * MovementSpeed;
            ViewFromDupe[1] = ViewFrom[1] + yMove * MovementSpeed;
            ViewFromDupe[2] = ViewFrom[2] + zMove * MovementSpeed;
            cDetec();
            if(canPass == false){
			xMove -= ViewVector.x;
			yMove -= ViewVector.y;
            }

		}

		if(Keys[5])
		{
            xMove += SideViewVector.x;
            yMove += SideViewVector.y;

            ViewFromDupe[0] = ViewFrom[0] + xMove * MovementSpeed;
            ViewFromDupe[1] = ViewFrom[1] + yMove * MovementSpeed;
            ViewFromDupe[2] = ViewFrom[2] + zMove * MovementSpeed;
            cDetec();
            if(canPass == false){
                xMove -= SideViewVector.x ;
                yMove -= SideViewVector.y ;
            }
            

		}
			
		if(Keys[6])
		{
            xMove -= ViewVector.x ;
            yMove -= ViewVector.y ;
            ViewFromDupe[0] = ViewFrom[0] + xMove * MovementSpeed;
            ViewFromDupe[1] = ViewFrom[1] + yMove * MovementSpeed;
            ViewFromDupe[2] = ViewFrom[2] + zMove * MovementSpeed;
            cDetec();
            if(canPass == false){
                xMove += ViewVector.x ;
                yMove += ViewVector.y ;
            }


		}

		if(Keys[7])
		{

            xMove -= SideViewVector.x ;
            yMove -= SideViewVector.y ;

            ViewFromDupe[0] = ViewFrom[0] + xMove * MovementSpeed;
            ViewFromDupe[1] = ViewFrom[1] + yMove * MovementSpeed;
            ViewFromDupe[2] = ViewFrom[2] + zMove * MovementSpeed;
            cDetec();
            if(canPass == false){
                xMove += SideViewVector.x ;
                yMove += SideViewVector.y ;
            }

		}

        if(Keys[8]){

        }
    
        if(Keys[10]){
          MovementSpeed = 0.3;
        }
        if(!Keys[10] && MovementSpeed != 0.1){
          MovementSpeed = 0.1;
        }

            canPass = true;
	    	Vector MoveVector = new Vector(xMove, yMove, zMove);
		    MoveTo(ViewFrom[0] + MoveVector.x * MovementSpeed, ViewFrom[1] + MoveVector.y * MovementSpeed, ViewFrom[2] + MoveVector.z * MovementSpeed);
	}


  //Actually updates the new position of the player
    void MoveTo(double x, double y, double z)
	{
		ViewFrom[0] = x;
		ViewFrom[1] = y;
		ViewFrom[2] = z;
        updateView();
	}

  //Calculates the intercept between where the player is looking and the floor
  //In the future I would like to add support for placing blocks on top of other surfaces other than the floor, but it wasnt plausible within my timeframe
    double[] calcInter(){

        Vector ViewVector = new Vector(ViewTo[0] - ViewFrom[0], ViewTo[1] - ViewFrom[1], ViewTo[2] - ViewFrom[2]);

        double t = -ViewFrom[2] / ViewVector.z;
        if(t <= 0){
            return null;
        }

        double[] result = new double[]{
        (ViewFrom[0] + t*ViewVector.x),
        (ViewFrom[1] + t*ViewVector.y),
        (ViewFrom[2] + t*ViewVector.z)
        };
        return result;

    }

  //Allows the player to jump, the speed of the jump is affected by frame right as of now which I'm not sure if I can fix that because frame rate is the amount of times a second the code can run 
    void jump(){

        cDetec();
        if(!canStand){
            if(ViewFrom[2] < 2){
                ViewFrom[2] = 2;
                inJump = false;
                inFall = false;
            }
            if(hTracker < 0.02){
                inJump = false;
                inFall = true;
            }
            if(inJump){
                ViewFrom[2] += hTracker;
                hTracker /= 1.1;
            }
            if(inFall){
                hTracker = 0.1;
                ViewFrom[2] -= hTracker;
                if(hTracker < 0.1) hTracker *= 1.2;
                else hTracker *= 1.05;
            }
        }
        
        else{

      
            if(ViewFrom[2] <= tempBox.z+tempBox.height+2){
                ViewFrom[2] = tempBox.z+tempBox.height + 2;
                inJump = false;
                inFall = false;
            }

            if(hTracker < 0.02){
                inJump = false;
                inFall = true;
            }
            if(inJump){
                ViewFrom[2] += hTracker;
                hTracker /= 1.1;
            }
            if(inFall){
                hTracker = 0.1;
                ViewFrom[2] -= hTracker;
                hTracker *= 1.1;

            }
        }
    }


  //Collision detection (this was a damn nightmare) 
  //never again never again never again never again never again never again never again never again never again never again never again never again never again never again never again 
    void cDetec(){

        Box thisBox = new Box();
        double barrierSize = 0;
        for(Box b : Boxes){
            if(b.x-barrierSize < ViewFromDupe[0] && ViewFromDupe[0] < b.x+b.width+barrierSize
            && b.y-barrierSize < ViewFromDupe[1] && ViewFromDupe[1] < b.y+b.length+barrierSize){
                thisBox = b;
                break;
            }
        }

            if((ViewFromDupe[0] < thisBox.x-barrierSize || ViewFromDupe[0] > thisBox.x+thisBox.width+barrierSize || ViewFromDupe[1] < thisBox.y-barrierSize || ViewFromDupe[1] > thisBox.y+thisBox.length+barrierSize) && ViewFrom[2] > 2 && inJump == false){
                inFall = true;
                canStand = false;
                canPass = true;
            }

            else if (thisBox.x-barrierSize < ViewFromDupe[0] && ViewFromDupe[0] < thisBox.x+thisBox.width+barrierSize
            && thisBox.y-barrierSize < ViewFromDupe[1] && ViewFromDupe[1] < thisBox.y+thisBox.length+barrierSize && ViewFrom[2]-2 < thisBox.z){
                canPass = true;
                canStand = false;
            }

            else if (thisBox.x-barrierSize < ViewFromDupe[0] && ViewFromDupe[0] < thisBox.x+thisBox.width+barrierSize
            && thisBox.y-barrierSize < ViewFromDupe[1] && ViewFromDupe[1] < thisBox.y+thisBox.length+barrierSize && inJump != true && ViewFrom[2]-2 > (thisBox.z+thisBox.height)){
                tempBox = thisBox;
                canPass = false;
                canStand = true;
            }

            else if(thisBox.x-barrierSize < ViewFromDupe[0] && ViewFromDupe[0] < thisBox.x+thisBox.width+barrierSize
                && thisBox.y-barrierSize < ViewFromDupe[1] && ViewFromDupe[1] < thisBox.y+thisBox.length+barrierSize && ( ViewFrom[2] < (thisBox.z+thisBox.height) || ViewFrom[2] == 2))
            {
                canPass = false;
                canStand = false;
            }

            else{
                canPass = true;
                canStand = false;
                inFall = false;
            }

        
    }

  //Updates the view of the player, (where you are looking)
    void updateView()
	{
		double r = Math.sqrt(1 - (VertLook * VertLook));
		ViewTo[0] = ViewFrom[0] + r * Math.cos(HorLook);
		ViewTo[1] = ViewFrom[1] + r * Math.sin(HorLook);		
		ViewTo[2] = ViewFrom[2] + VertLook;
	}
}