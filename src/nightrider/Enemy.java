package nightrider;
import java.awt.*;




public class Enemy {
	
	private int x;
	private int y;
	private int dx;
	private int dy;
	private int width;
	private int height;
	private int speed;
	private int health;
	private int points;
	private int type;
	private int Rank;
	private Color color;
	private boolean ready;
	private boolean dead;
	
	private boolean hit;
	private long hitTimer;
	
    public Enemy(int type , int speed){
		
    	this.type = type;
		
		if(type == 1){
			width = 50;
			height = 90;
			color = new Color(255,0,255).brighter();
			dy =speed;
			health = 4;
			points = 5;
			
		}
		if(type == 2){
			width = 50;
			height = 150;
			color = new Color(0,255,255).brighter();
			dy = speed ;
			health = 5;
			points = 8;
		}
		if(type == 3){
			width = 60;
			height = 120;
			color = new Color(255,255,0).brighter();
			dy = speed;
			health = 6;
			points = 12;
		}
		x = (int) (Math.random() * 340 +205);
		y = -160;
		ready = false;
		 dead = false;
		 
		 hit = false;
		 hitTimer =0;
		
    }
    
    public double getx(){ return x;}
	public double gety(){ return y;}
	public int getType(){return type;}
	public int getPoints(){return points;}
	 public double getwidth(){ return width;}
	 public double getheight(){ return height;}
	
	
	public boolean isDead(){
		return dead;
	}
	public void hits(){
                
		health--;
		if(health <= 0){
			dead = true;
		}
		hit = true;
		hitTimer = System.nanoTime();
	}
	
	public boolean update(){
		if(GamePanel.Pause){
			x += dx*0;
			y += dy*0;
		}else{
                    x += dx;
                    y += dy;
                }
		
		
		if(!ready){
			if(x>200 && x < 538 && y>-170 && y < GamePanel.HEIGHT +200){
				ready =true;
			}
		}
		if(x<200 && dx<0){
			dx=-dx;
		}
		
		if(x>600 && dx>0){
			dx =-dx;
		}
		if(hit){
			long elapsed = (System.nanoTime()-hitTimer)/1000000;
			if(elapsed>50){
				hit = false;
				hitTimer = 0;
			}
		}
		if( y> GamePanel.HEIGHT+200){
			return true;
		}
		return false;
	}
	
public void draw (Graphics2D g){
	if(hit){
		g.setColor(color.darker());
		g.drawRect(x, y, width, height);
			g.setColor(Color.red);
			g.fillRect(x, y, width, height);
		
	}else{
		g.setColor(color.darker());
		g.drawRect(x, y, width, height);
			g.setColor(color.brighter());
			g.fillRect(x, y, width, height);
	}
		
		
	}
	
	

}
