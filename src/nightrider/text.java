package nightrider;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class text {
	 private double x;
	    private double y;
	    private long time;
	    private String S;
	    private long Start;
	    
	    public text(double x, double y, long time, String S){
	        this.x = x;
	        this.y = y;
	        this.time = time;
	        this.S = S;
	        Start = System.nanoTime();
	    }
	    
	    public boolean update(){
	        long elapsed = (System.nanoTime() - Start)/1000000;
	        if(elapsed>time){
	            return true;
	        }
	        return false;
	    }
	    public void draw(Graphics2D g){
	        g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 18));
	        long elapsed = (System.nanoTime() - Start)/1000000;
	        int alpha = (int)(255* Math.sin(3.14* elapsed/time));
	        if(alpha >255){ alpha = 255;}
	        g.setColor(new Color(255,255,255,alpha));
	        int length = (int)g.getFontMetrics().getStringBounds(S, g).getWidth();
	        g.drawString(S, (int)(x-(length/2)), (int)y);
	    }

}
