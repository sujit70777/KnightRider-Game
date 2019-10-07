package nightrider;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;



public class Explosion {
	
	private double x;
	private double y;
	private int r;
	private int MaxRadeus;

	
        
	
	
	public Explosion(double x, double y, double d, double e){
		this.x = x;
		this.y = y;
		this.r = (int)d;
		MaxRadeus = (int)e;
               
	}
	

	public boolean update(){
		if(GamePanel.Pause){
			y+=0;	
		}
		else{
		r +=2;
		}
		if(r>=MaxRadeus){
			return true;
		}
		return false;
	}
	public void draw(Graphics2D g){
		g.setColor(new Color(255,255,0,128));
		g.setStroke(new BasicStroke(10));
		g.drawOval((int)(x-r), (int)(y-r), 2*r, 2*r);
		g.setStroke(new BasicStroke(1));

	}

}
