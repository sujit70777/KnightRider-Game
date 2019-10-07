package nightrider;

import java.awt.*;



public class Road {
	
	private int x;
	private int y;
	private int dx;
	private int dy;
	private int speed;
	private int width;
	private int height;
	private Color RoadColor;
	
	
	
	
	public Road(int type)
	{
		speed = 15;
		if(type == 1){
			y=-200;
			x = GamePanel.WIDTH/2-2;
			width = 5;
			height = 150;
			RoadColor = new Color(255,255,255).brighter();
			dy =speed;
		}
		if(type == 2){
			y=-100;
			x = GamePanel.WIDTH/2-175;
			width = 10;
			height = 50;
			RoadColor = new Color(255,255,255).brighter();
			dy = speed;
		}
		if(type == 3){
			y=-100;
			x = GamePanel.WIDTH/2+160;
			width = 10;
			height = 50;
			RoadColor = new Color(255,255,255).brighter();
			dy = speed;
		}
		
		
		
	}
	public double getx(){ return x;}
	public double gety(){ return y;}
	
	public boolean update(){
		if(GamePanel.Pause){
			y += dy*0;
			
		}
		else if(GamePanel.player.isrecovering()){
			y += dy*0.6;
		}else{
		y += dy;
		}
		
		if(y> GamePanel.HEIGHT){
			return true;
		}
		return false;
		
	}
	
	public void draw (Graphics2D g){
		
		g.setColor(RoadColor.darker());
	g.drawRect(x, y, width, height);
		g.setColor(RoadColor.brighter());
		g.fillRect(x, y, width, height);
	}

}
