package nightrider;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;



public class Bullets {
	
	private int x;
	private int y;
	private int width;
	private int height;
	
	
	private double dx;
	private double dy;
	private double speed;
	private Color color1;
	
	
	
	
	public Bullets(int x, int y){
		this.x = x;
		this.y = y;
		
		speed =20;
		dy =  speed;
		width = 2;
		height = 8;
		
	}
	public double getx(){ return x;}
	public double gety(){ return y;}
	public double getwidth(){ return width;}

	
	public boolean update(){
		if(GamePanel.Pause){
			
			y -= dy*0;
		}
		else{
		
		y -= dy;
		}
		if( y<-10){
			return true;
		}
		return false;
		
	}
	
	public void draw (Graphics2D g){
		g.setColor(new Color(251,2,1).brighter());
		g.fillRect(x, y, width, height);
		g.fillArc(x, y-7, width, 15, 0,180);
		g.setStroke(new BasicStroke(1));
		g.setColor(new Color(2,0,0).darker());
		g.drawRect(x, y, width, height);
		g.drawArc(x, y-8, width, 15, 0,180);
		g.setStroke(new BasicStroke(1));
		
	}


}
