package nightrider;

import javax.swing.JFrame;

public class Main_Game {
	
	public static void main(String[] arg){
		JFrame frame = new JFrame("--NIGHTRIDER--");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(new GamePanel());
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		frame.pack();
		
		
	}
	
}
