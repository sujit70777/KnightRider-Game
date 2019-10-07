package nightrider;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Roadlight {
	private int x;
	private int y;
	private int dx;
	private int dy;
	private int speed;
	private int width;
	private int height;
	private int width2;
	private int height2;
	private Color RoadLightColor;
	
	public Roadlight(int type){
		if(type==1){
			x = GamePanel.WIDTH-620;
		}
		if(type==2){
			x = GamePanel.WIDTH-200;
		}
		width = 20;
		height = 20;
		width2 = 3;
		height2 = 10;
		y=-30;
		dy = 15;
		RoadLightColor = Color.orange;
	}
	
	public boolean update (){
		if(GamePanel.Pause){
			y += dy*0;
			
		}
		else if(GamePanel.player.isrecovering()){
			y += dy*0.6;
		}
		else{
		y += dy;
		}
		if(y-50> GamePanel.HEIGHT){
			return true;
		}
		return false;
	}
	
	public void draw (Graphics2D g){
		g.setColor(new Color(204,204,204).darker());
		g.drawRect(x+8, y, width2, height2);
		g.setColor(new Color(204,204,204));
		g.fillRect(x+8, y, width2, height2);
		
		
		g.setColor(new Color(251,255,1));
		g.fillOval(x, y-20, width, height);
		g.setStroke(new BasicStroke(40));
		g.setColor(new Color(251,255,1,60));
		g.drawOval(x, y-20,width, height);
		g.setStroke(new BasicStroke(1));
	}

}
