package nightrider;

import java.awt.*;






public class Player {
	
	private int x;
	private int y;
	private int dx;
	private int dy;
	private int speed;
	
	private int width ;
	private int height;
	private int lives=3;
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;

	private boolean recover;
	private long recoverTime;
	
	private int score;
	
	
	private int rx;
	private int rx2;
	private int ry;
	private int rdx;
	private int rdx2;
	private int rdyy;
	private int rdxx;
	
	private int rspeed;
	
	private Color rcolor;
	private boolean light;
	private boolean jump;
	private boolean vanish;
	
	private int rwidth ;
	private int rheight;
	
	private Color body;
	private Color glass;
	private Color block;
	private Color redLight;
	private Color border;
	private int opacity;
	private boolean runningroad;
	private long runningTimer;
	private long runningTimer2;
	private long runningDelay;
	private long runningDelay2;
	
	private boolean roadlight;
	private long roadlightTimer;
	private long roadlightDelay;
	
	private boolean firing;
	private long firingTimer;
	private long firingDelay;
	
	
	public Player(){
		x = GamePanel.WIDTH/2-135;
		y = GamePanel.HEIGHT/2+50;
		rx = x+30;
		
		ry = y+3;
		
		width =270;
		height =340;
//		width =50;
//		height =120;
		rx2 = 20;
		dx=0;
 		dy=0;
		speed = 9;
		opacity = 30;
		body = new Color(1,20,144);
		block = new Color(251,254,101).brighter();
		redLight = new Color(255,0,48).brighter();
		glass = new Color(1,0,1).brighter();
		border = new Color(1,0,0,190);
		
		recover = false;
		recoverTime = 0;
		
		score = 0;
		
		rwidth = 5;
		rheight = 3;
		rcolor = new Color(255,0,48);
		rdx=2;
		rdx2=2;

		rdxx=0;
		rdyy=0;
		
		runningroad = true;
		
		runningTimer = System.nanoTime();
		runningTimer2 = System.nanoTime();
		runningDelay = 300;
		runningDelay2 = 700;
		
		roadlight = true;
		roadlightTimer = System.nanoTime();
		roadlightDelay = 600;
		
		firing = false;
		firingTimer = System.nanoTime();
		firingDelay = 250;
		
		
	}
	
	
	public int getx(){ return x;}
	public int gety(){ return y;}
	public int getWidth(){ return width;}
	public int getHeight(){ return height;}
	public boolean getLight() { return light;}
	public boolean getVanish() { return vanish;}
	public int getLives(){return lives;}
	
	
	
	
	public void setLeft(boolean b){ 
		left = b;
		}
	public void setRight(boolean b){
		right = b;
		}
	public void setUp(boolean b){ 
		up = b;
		}
	public void setDown(boolean b){
		down = b;
		}
	public void setLight(boolean b){
		light = b;
		}
	public void setJump (boolean b){
		jump = b;
	}
	public void setVanish (boolean b){
		vanish = b;
	}
	public void setfiring(boolean b){
		firing = b;
		}
	public void setLives(int b) {
		lives = b;
	}
	public void setScore(int b) {
		score = b;
	}
	public void setRecoveringtimer(int b) {
		recoverTime = b;
	}
	public void setPlayer() {
		x = GamePanel.WIDTH/2-135;
		y = GamePanel.HEIGHT/2+50;
		rx = x+30;
		
		ry = y+3;
		
		width =270;
		height =340;
	}
	
	
	//-----RECOVERING PLAYER-------------*********
	
	public boolean isrecovering(){
		return recover;
	}
	
	public boolean IsDead(){
		return lives<=0;
	}
	
	// ------------ DEAD OF PLAYER-------------*******
	
	public void livelost(){
		lives--;
		recover = true;
		recoverTime = System.nanoTime();
	}
	public int getscore(){
		return score;
	}
	
	public void addScore(int i){
		score +=i;
	}
	
	
	
	public void update () {
		
		if(left){
			dx = -speed;
		}
		if(right){
			dx= speed;
		}
		if(up){
			dy = -speed;
		}
		if(down){
			dy = speed;
		}
		
		if(GamePanel.Pause){
			x += dx*0;
			y += dy*0;
			rx += dx*0;
			ry = y+5*0;
			rx +=  rdx*0;
			rx2 +=  rdx2*0;
		}else{
		x += dx;
		y += dy;
		rx += dx;
		ry = y+5;
		rx +=  rdx;
		rx2 +=  rdx2;
		}
		if (x<202){ 
			x=202;
			rx=220 +rdx;
			
		}
		if(y<2){
			y=2;
		}
		if(x>GamePanel.WIDTH-width-202){
			x = GamePanel.WIDTH -width-202;
			rx = GamePanel.WIDTH -width-190 +rdx;
			
		}
		if(y>GamePanel.HEIGHT-height){
			y = GamePanel.HEIGHT-height;
		}
		dy=0;
		dx=0;
		
		if(rx<=x+10){
			rdx = -rdx;
		}
		if(rx>=x+35){
			rdx = -rdx;
		}
		
		if(rx2<=20){
			rdx2 = -rdx2;
		}
		if(rx2>=45){
			rdx2 = -rdx2;
		}
		
		
		if(jump && !GamePanel.Pause){
			
				height+=2;
				width+=2;
				x-=1;
				rx-=1;
				light = false;
			
		}else{
			if((height>120 || width>50) && !GamePanel.Pause){
				if(height>120){
					height  -=2;
				}
				if( width>50){
					width -=2;
				}
				x+=1;
				rx+=1;
				light = false;
				
			}
			
			
		}
		
		if(vanish){

			
			body = new Color(1,20,144,opacity);
			block = new Color(251,254,101,opacity);
			redLight = new Color(255,0,48,opacity);
			glass = new Color(1,0,1,opacity);
			border = new Color(1,0,0,opacity);
			
		}
		if(recover){
			body = new Color(104,0,0).darker();
			block = new Color(251,254,101).brighter();
			redLight = new Color(255,0,48).brighter();
			glass = new Color(1,0,1).brighter();
			border = new Color(1,0,0,190);
			
		}
		else{
			
			body = new Color(1,20,144);
			block = new Color(251,254,101).brighter();
			redLight = new Color(255,0,48).brighter();
			glass = new Color(1,0,1).brighter();
			border = new Color(1,0,0,190);
			
		}
		
	// ----------------- POWER BULLET FIRING----------*************
		
		if(recover){
		long elapsed = (System.nanoTime()-recoverTime)/1000000;
		if(elapsed>2200){
			recover= false;
			recoverTime = 0;
		}
		}
	
		
		if(runningroad){
			long elasped = (System.nanoTime()-runningTimer)/1000000;
			if(elasped > runningDelay){
				if(!GamePanel.Pause){
				GamePanel.roadBlocks.add(new Road(2));
				GamePanel.roadBlocks.add(new Road(3));
				}
				runningTimer = System.nanoTime();
				
			}
			
		}
		if(runningroad){
			long elasped2 = (System.nanoTime()-runningTimer2)/1000000;
			if(elasped2 > runningDelay2){
				if(!GamePanel.Pause){
				GamePanel.roadBlocks.add(new Road(1));
				}
				runningTimer2 = System.nanoTime();
				
			}
			
		}
		
		if(roadlight){
			long elasped3 = (System.nanoTime()-roadlightTimer)/1000000;
			if(elasped3 > roadlightDelay){
				if(!GamePanel.Pause){
				GamePanel.roadlights.add(new Roadlight(1));
				GamePanel.roadlights.add(new Roadlight(2));
				}
				roadlightTimer = System.nanoTime();
				
			}
			
		}
		
		
		if(firing){
			long elasped4 = (System.nanoTime()-firingTimer)/1000000;
			if(elasped4 > firingDelay){
				if(!GamePanel.Pause){
					GamePanel.bullets.add(new Bullets(x+10, y));
					GamePanel.bullets.add(new Bullets(x+35, y));
				}
				firingTimer = System.nanoTime();
				
			}
			
		}
	}
	
	
	public void draw ( Graphics2D g){
	g.setColor(border);
	g.drawRoundRect(x-1, y, width+1, height,15,15);
	g.drawArc(x, y-1, width, 15, 0,180);
	
	
	if(light){
		g.setColor(new Color(255,255,255,120));
		g.fillArc(x-20, y-90, 50, 200, 57,65);
		g.fillArc(x+20, y-90, 50, 200, 57,65);
		
	}
	
		
		g.setColor(body);
		g.fillRoundRect(x, y, width, height,15,15);

		
		
		g.setColor(block);
		g.fillRect(x+28, y, width-45, height);
		g.fillRect(x+18, y, width-45, height);	
	
		
		g.setColor(glass);
		int x1[] = {x+ width-45,x+ width-42,x+ width-9,x+ width-5,x+ width-45};
		int y1[] = {y+ height-80,y+ height-65,y+ height-65,y+ height-80,y+ height-80};
		int n1 =5;
		g.fillPolygon(x1, y1, n1);
		
		int x2[] = {x+ width-43,x+ width-46,x+ width-4,x+ width-8,x+ width-43};
		int y2[] = {y+height-35,y+height-17,y+height-17,y+height-35,y+height-35};
		int n2 =5;
		g.fillPolygon(x2, y2, n2);
		
		g.fillArc(x-5, y+40, width-40,height - 60, 270,180);
		g.fillArc(x+45, y+40, width-40,height - 60, 90,180);
		
		
	//	g.setColor(new Color(0,0,0).brighter());
		g.fillRect(x+10, y+5, width-20,  height-117);
		
		g.setColor(redLight);
		g.fillRect(rx, y+5, width-44,height-117);
		
//		g.setColor(new Color(255,255,255));
//		g.fillOval(x+10, y+12, 8, 2);
//		g.fillOval(x+10, y+20, 8, 2);
//		g.fillOval(x+10, y+28, 8, 2);
//		g.fillOval(x+32, y+12, 8, 2);
//		g.fillOval(x+32, y+20, 8, 2);
//		g.fillOval(x+32, y+28, 8, 2);
		
		
		
		
	}

	
	public void draw2 ( Graphics2D g){
		
		x= 10;
		y = 10;
		
		
		width = 50;
		height = 120;
		body = new Color(1,20,144);
		block = new Color(251,254,101).brighter();
		redLight = new Color(255,0,48).brighter();
		glass = new Color(1,0,1).brighter();
		border = new Color(1,0,0,190);
		
		
		
		g.setColor(border);
		g.drawRoundRect(x-1, y, width+1, height,15,15);
		g.drawArc(x, y-1, width, 15, 0,180);
		
		
		if(light){
			g.setColor(new Color(255,255,255,120));
			g.fillArc(x-20, y-90, 50, 200, 57,65);
			g.fillArc(x+20, y-90, 50, 200, 57,65);
			
		}
		
			
			g.setColor(body);
			g.fillRoundRect(x, y, width, height,15,15);

			
			
			g.setColor(block);
			g.fillRect(x+28, y, width-45, height);
			g.fillRect(x+18, y, width-45, height);	
		
			
			g.setColor(glass);
			int x1[] = {x+ width-45,x+ width-42,x+ width-9,x+ width-5,x+ width-45};
			int y1[] = {y+ height-80,y+ height-65,y+ height-65,y+ height-80,y+ height-80};
			int n1 =5;
			g.fillPolygon(x1, y1, n1);
			
			int x2[] = {x+ width-43,x+ width-46,x+ width-4,x+ width-8,x+ width-43};
			int y2[] = {y+height-35,y+height-17,y+height-17,y+height-35,y+height-35};
			int n2 =5;
			g.fillPolygon(x2, y2, n2);
			
			g.fillArc(x-5, y+40, width-40,height - 60, 270,180);
			g.fillArc(x+45, y+40, width-40,height - 60, 90,180);
			
			
		//	g.setColor(new Color(0,0,0).brighter());
			g.fillRect(x+10, y+5, width-20,  height-117);
			
			g.setColor(redLight);
			g.fillRect(rx2, y+5, width-44,height-117);
			
//			g.setColor(new Color(255,255,255));
//			g.fillOval(x+10, y+12, 8, 2);
//			g.fillOval(x+10, y+20, 8, 2);
//			g.fillOval(x+10, y+28, 8, 2);
//			g.fillOval(x+32, y+12, 8, 2);
//			g.fillOval(x+32, y+20, 8, 2);
//			g.fillOval(x+32, y+28, 8, 2);
			
			
			
			
		}



}
