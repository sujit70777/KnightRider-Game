package nightrider;

import javax.swing.JPanel;





import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	public static int WIDTH = 800;
	public static int HEIGHT = 720;
	public static boolean Pause;

	private Thread thread;
	private boolean running;

	private BufferedImage image;
	private Graphics2D g;

	private int FPS = 30;
	private double avarageFPS;

	private boolean Start;
	private boolean ReStart;
	private boolean GameOver;

	public static Player player;
	public static Player player2;

	public static ArrayList<Road> roadBlocks;
	public static ArrayList<Roadlight> roadlights;
	public static ArrayList<Bullets> bullets;
	public static ArrayList<Enemy> enemies;
	public static ArrayList<Explosion> explosions;
	public static ArrayList<text> texts;

	private boolean enemyComing;
	private long enemyComingTimer;
	private long enemyComingDelay;
	public long RoundStartTime;
	public long RoundStartTimeDiff;
	public int RoundNumber;
	private boolean RoundStrat;
	public boolean NextRound;
	public long NextRoundTimer;
	public long NextRoundTimeDelay;
	public long NextRoundTimeDiff;
	
	private long scoretimer;
	private boolean SETscore;
	private long scoredelay;
	

	public int RoundDelay = 2000;

	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}

	public void addNotify() {
		super.addNotify();
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}

	// public boolean getPause(){ return Pause;}

	@Override
	public void run() {
		running = true;
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_BGR);
		g = (Graphics2D) image.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Pause = false;
		Start = false;
		GameOver = false;

		player = new Player();
		player2 = new Player();

		roadBlocks = new ArrayList<Road>();
		roadlights = new ArrayList<Roadlight>();
		bullets = new ArrayList<Bullets>();
		enemies = new ArrayList<Enemy>();
		explosions = new ArrayList<Explosion>();
		texts = new ArrayList<text>();

		enemyComing = false;
		enemyComingTimer = System.nanoTime();

		RoundStartTime = 0;
		RoundStartTimeDiff = 0;
		RoundDelay = 1000;
		RoundNumber = 0;
		RoundStrat = true;
		scoretimer = System.nanoTime();
		scoredelay = 500;

		NextRound = false;
		
		NextRoundTimeDelay = 60000;
		NextRoundTimeDiff = 0;

		while (running) {
			if (GameOver) {

				GameOver();
				gameDraw();

			} else if (!Start) {

				GameStart();
				gameDraw();

			} else {
				try {
					gameUpadte();
					gameRender();
				} catch (Exception ex) {
				}

				gameDraw();

				try {
					thread.sleep(FPS);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}

	}

	// ----------------------------------------------------------------------
	// =---------------------------GAME UPDATE-----------------------------

	private void gameUpadte() {

		player.update();
		player2.update();

		// ----------------------------ROAD UPDATE----------------
		for (int i = 0; i < roadBlocks.size(); i++) {
			boolean remove = roadBlocks.get(i).update();
			if (remove) {
				roadBlocks.remove(i);
				i--;
			}
		}

		// ------------------------ROAD'S LIGHT UPDATE------------------
		for (int i = 0; i < roadlights.size(); i++) {
			boolean remove = roadlights.get(i).update();
			if (remove) {
				roadlights.remove(i);
				i--;
			}
		}

		// ---------------------------BULLETS UPDATE
		// ----------------------------
		for (int i = 0; i < bullets.size(); i++) {
			boolean remove = bullets.get(i).update();
			if (remove) {
				bullets.remove(i);
				i--;
			}
		}

		// -----------------------ENEMY UPDATE ---------------------------

		for (int i = 0; i < enemies.size(); i++) {
			boolean remove = enemies.get(i).update();
			if (remove) {
				enemies.remove(i);
				i--;
			}
		}

		// ---------------EMEMY EXPLORSIONS UPDATE -------------*************

		for (int i = 0; i < explosions.size(); i++) {
			boolean remove = explosions.get(i).update();

			if (remove) {
				explosions.remove(i);
				i--;
			}
		}

		// ---------------TEXT UPDATE -------------*************

		for (int i = 0; i < texts.size(); i++) {
			boolean remove = texts.get(i).update();

			if (remove) {
				texts.remove(i);
				i--;
			}
		}

		// ---------------ENEMY DEAD---------------------------

		for (int i = 0; i < bullets.size(); i++) {

			Bullets b = bullets.get(i);
			double bx = b.getx();
			double by = b.gety() + 15;
			double bw = b.getwidth();

			for (int j = 0; j < enemies.size(); j++) {
				Enemy e = enemies.get(j);
				double ex = e.getx();
				double ey = e.gety();
				double ew = e.getwidth();
				double eh = e.getheight();

				if ((bx + 3 >= ex && bx <= ex + ew) && by < ey + eh && by > ey) {
					e.hits();
					bullets.remove(i);
					i--;
					break;
				}
			}
		}

		for (int i = 0; i < enemies.size(); i++) {
			if (enemies.get(i).isDead()) {
				Enemy e = enemies.get(i);
				enemies.remove(i);
				i--;
				player.addScore(e.getType() + e.getPoints());
				explosions.add(new Explosion(e.getx(), e.gety(), e.getwidth(),
						e.getheight()));
			}
		}

		// -----------PLAYER DEAD ----------------------------------------

		// -----------------------DEAD OF PLAYER---------*******]

		if (!player.isrecovering() && player.getHeight() == 120
				&& player.getWidth() == 50) {

			int Dpx = player.getx();
			int Dpy = player.gety();
			int Dpw = player.getWidth();
			int Dph = player.getHeight();

			for (int i = 0; i < enemies.size(); i++) {
				Enemy e = enemies.get(i);

				double ex = e.getx();
				double ey = e.gety();
				double ew = e.getwidth();
				double eh = e.getheight();

				if ((Dpx + Dpw > ex && Dpx < ex + ew) && Dpy < ey + eh
						&& Dpy + Dph > ey) {
					player.livelost();

				}
			}
		}

		if (enemyComing) {
			long elasped4 = (System.nanoTime() - enemyComingTimer) / 1000000;
			if (elasped4 > enemyComingDelay) {
				if (!GamePanel.Pause) {
					double random = Math.random();
					if (random < 0.3) {
						GamePanel.enemies.add(new Enemy(1, 8));
					} else if (random < 0.6) {
						GamePanel.enemies.add(new Enemy(2, 8));
					} else if (random < 1) {
						GamePanel.enemies.add(new Enemy(3, 8));
					}

				}
				enemyComingTimer = System.nanoTime();

			}

		}

		if (RoundNumber == 0 && Start) {
			RoundNumber++;
			RoundStartTime = System.nanoTime();
			NextRoundTimer = System.nanoTime();
		//	RoundStrat = false;
		}
		if (RoundNumber == 1) {
			enemyComingDelay = 2000;
		}
		if (RoundNumber == 2) {
			enemyComingDelay = 1500;
		}
		if (RoundNumber == 3) {
			enemyComingDelay = 1000;
		}

		// -------------ROUND'S UPDATING
		// :)----**************************************
		if ((GamePanel.player.getHeight() == 120 && GamePanel.player.getWidth() == 50)
				&& !GamePanel.Pause && RoundStartTime == 0 && NextRound && Start) {
			enemyComing = true;
			RoundStrat = true;
			SETscore = true;

			NextRound = false;
			NextRoundTimeDiff = (System.nanoTime() - NextRoundTimer) / 1000000;
			if (NextRoundTimeDiff > NextRoundTimeDelay) {

				NextRoundTimer = System.nanoTime();
				RoundNumber++;

				RoundStartTime = System.nanoTime();
				RoundStrat = false;

			}

		}

		else {
			RoundStartTimeDiff = (System.nanoTime() - RoundStartTime) / 1000000;
			if (RoundStartTimeDiff > RoundDelay) {
				RoundStrat = true;
				NextRound = true;
				RoundStartTime = 0;
				RoundStartTimeDiff = 0;
				enemyComing = false;
			}
		}

		// if(RoundStrat && NextRound){
		//
		// }

		if (player.IsDead()) {
			GameOver = true;
			NextRound = false;
			for (int i = 0; i < enemies.size(); i++) {
				Enemy e = enemies.get(i);
				enemies.remove(i);
			}

		}
		if (RoundNumber == 4) {

			GameOver = true;
			NextRound = false;
			for (int i = 0; i < enemies.size(); i++) {
				Enemy e = enemies.get(i);
				enemies.remove(i);
			}

		}
		
		if(SETscore && !Pause){
			long elaps =(System.nanoTime() - scoretimer )/1000000;
			if(elaps>scoredelay){
				player.addScore(1);
				scoretimer =System.nanoTime();
			}
			
		}

	}

	// #########################################################################
	// ------------------------------GAME RENDERING ----------------------------

	private void gameRender() {

		// ------------------------------GAME BACKLGROUND--------------------
		g.setColor(new Color(0, 1, 80));
		g.fillRect(0, 0, WIDTH - 600, HEIGHT);

		// g.setColor(new Color(50,255,49));
		g.fillRect(600, 0, WIDTH - 600, HEIGHT);

		g.setColor(new Color(204, 204, 204).darker());
		g.fillRect(200, 0, WIDTH - 400, HEIGHT);

		g.setColor(new Color(53, 50, 255));
		g.fillRect(600, 0, WIDTH - 780, HEIGHT);
		g.fillRect(180, 0, WIDTH - 780, HEIGHT);
		//
		g.setStroke(new BasicStroke(1));
		g.setColor(new Color(53, 50, 255).darker());
		g.drawRect(180, 0, WIDTH - 780, HEIGHT);
		g.drawRect(600, 0, WIDTH - 780, HEIGHT);

		// ---------------PLAYER SCORES----------------

		g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 22));
		g.setColor(new Color(255, 255, 255));
		g.drawString("SCORE", WIDTH - 120, 30);
		g.setColor(new Color(0, 255, 0));
		g.drawString("" + player.getscore(), WIDTH - 120, 60);

		player2.draw2(g);
		g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 42));
		g.setColor(new Color(0, 255, 6));
		g.drawString("X " + player.getLives(), 70, 80);

		// --------------------------- ROAD DRAWING ---------------

		for (int i = 0; i < roadBlocks.size(); i++) {
			roadBlocks.get(i).draw(g);
		}

		// --------------------BULLETS DRAWING ----------------

		for (int i = 0; i < bullets.size(); i++) {
			bullets.get(i).draw(g);
		}

		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
		}

		// -----------------------PLAYER DRAWING -------------------
		if (player.getWidth() > 50 || player.getHeight() > 120) {

			for (int i = 0; i < roadlights.size(); i++) {
				roadlights.get(i).draw(g);
			}
			player.draw(g);
		} else {
			player.draw(g);

			for (int i = 0; i < roadlights.size(); i++) {
				roadlights.get(i).draw(g);
			}

		}

		// --------------------ENEMY EXPLOSION DRAWING-----------************

		for (int i = 0; i < explosions.size(); i++) {
			explosions.get(i).draw(g);
		}

		// --------------------TEXT DRAWING-----------************

		for (int i = 0; i < texts.size(); i++) {
			texts.get(i).draw(g);
		}

		// -----------------MY ROUNDS NUMBER DRAWING :)
		// -----------************---------***

		if (RoundStartTime != 0) {

			g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 45));
			String s = "- R O U N D " + RoundNumber + " - ";
			// int length = (int)g.getFontMetrics().getStringBounds(s,
			// g).getWidth();
			int alpha = (int) (255 * Math.sin(3.14 * RoundStartTimeDiff
					/ RoundDelay));
			if (alpha > 255) {
				alpha = 255;
			}
			g.setColor(new Color(0, 0, 138, alpha));

			g.drawString(s, WIDTH / 2 - 150, HEIGHT / 2-150);
			// g.drawString(s, WIDTH/2-length/2, HEIGHT/2);

		}

		// ---------------DRAW ROUND TIMER -----------------
		if (NextRoundTimer != 0) {
			g.setColor(new Color(255, 0, 0));
			// g.drawRect(0, 710, 900, 8);
			g.fillRect(0, 710, (int) (880 - 880.0 * NextRoundTimeDiff
					/ NextRoundTimeDelay), 8);
		}
		
		if(Pause){
			g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 80));
			g.setColor(new Color(0, 0, 138, 255));

			g.drawString("PAUSE", WIDTH / 2 - 150, HEIGHT / 2-150);
			
		}else{
			g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 45));
			g.setColor(new Color(0, 0, 138, 0));

			g.drawString("PAUSE", WIDTH / 2 - 150, HEIGHT / 2-150);
		}

	}

	// *****************************************************************************************************************************
	// ----------------------------------------------------------- MY GAME START
	// SCREEN----------- ******************------===-=-=-=-----=-=\-=\-\=
	// ******************************************************************************************************************************

	private void GameStart() {

		int Space = 100;
		int high = 200;

		g.setColor(Color.BLACK.darker());
		g.fillRect(0, 0, WIDTH, HEIGHT);

		g.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 125));

		String w1 = "NIGHT";
		
		String w7 = "RIDER";
		

		String C = " : ";

		String T1 = "ENTER";
		String T11 = "START GAME";

		String T2 = "      S";
		String T21 = "SHOOT";

		String T3 = "LEFT ARROW / NUM 4";
		String T31 = "MOVE LEFT";

		String T4 = " RIGHT ARROW / NUM 6";
		String T41 = "MOVE RIGHT";

		String T5 = " UP ARROW / NUM 8";
		String T51 = "MOVE UP";

		String T6 = " DOWN ARROW / NUM 5";
		String T61 = "MOVE DOWN";

		String T7 = "SPACE";
		String T71 = "PAUSE GAME";

		// ---------------------------------------PRINTING Night TEXT
		// ----------------********************---

		g.setColor(new Color(2, 0, 140).brighter());
		g.drawString(w1, WIDTH / 5-0, high-30);

		

		// ------------------------------PRINTING Rider
		// TEXT----------************
		g.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 105));

		g.setColor(new Color(255, 0, 0).brighter());
		g.drawString(w7, WIDTH / 5 + 30, high + 70);

		

		// -------------------------------------PRINTING INSTRUCTIONS
		// --------------***********

		g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 50));

		g.setColor(Color.ORANGE.brighter());
		g.drawString(T1, WIDTH / 4-60, high + 180);

		g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 20));

		g.setColor(new Color(103,255,105).brighter());
		g.drawString(T2, WIDTH / 3 + 50, high + 225);
		g.drawString(T3, WIDTH / 6 - 20, high + 250);
		g.drawString(T4, WIDTH / 6 - 38, high + 275);
		g.drawString(T5, WIDTH / 6 + 0, high + 300);
		g.drawString(T6, WIDTH / 6 - 42, high + 325);
		g.drawString(T7, WIDTH / 3 + 15, high + 350);

		// --------------------PRINTING COLORNS ( : )-------------------------
		g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 50));

		g.setColor(Color.WHITE.brighter());
		g.drawString(C, WIDTH / 2 - 70, high + 180);

		g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 20));
		g.setColor(Color.WHITE.brighter());
		g.drawString(C, WIDTH / 2, high + 225);
		g.drawString(C, WIDTH / 2, high + 250);
		g.drawString(C, WIDTH / 2, high + 275);
		g.drawString(C, WIDTH / 2, high + 300);
		g.drawString(C, WIDTH / 2, high + 325);
		g.drawString(C, WIDTH / 2, high + 350);

		// --------------------PRINTING INSTRUCTION LIST ( :
		// )-------------------------
		g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 50));
		g.setColor(new Color(99, 255, 50).brighter());
		g.drawString(T11, WIDTH / 2 - 30, high + 180);

		g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 20));

		g.setColor(new Color(99, 255, 50).brighter());
		g.drawString(T21, WIDTH / 2 + 20, high + 225);
		g.drawString(T31, WIDTH / 2 + 20, high + 250);
		g.drawString(T41, WIDTH / 2 + 20, high + 275);
		g.drawString(T51, WIDTH / 2 + 20, high + 300);
		g.drawString(T61, WIDTH / 2 + 20, high + 325);
		g.drawString(T71, WIDTH / 2 + 20, high + 350);

	}

	// *****************************************************************************************************************************
	// ----------------------------------------------------------- MY GAME OVER
	// SCREEN----------- ******************------===-=-=-=-----=-=\-=\-\=
	// ******************************************************************************************************************************
	private void GameOver() {

		// ---------------- DRAWING GAME OVER AT END OF GEME
		// ---------**************------

		g.setColor(Color.BLACK);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.red);
		g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 100));
		String s1 = "GAME  OVER";
		int length = (int) g.getFontMetrics().getStringBounds(s1, g).getWidth();
		g.drawString(s1, WIDTH / 2 - length / 2, HEIGHT / 2);

		

		// ---------------- DRAWING FINAL SCORE AT END OF GEME
		// ---------**************------

		g.setColor(Color.white);
		g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 32));
		String s2 = "FINAL SCORES: " + player.getscore();
		int length21 = (int) g.getFontMetrics().getStringBounds(s1, g)
				.getWidth();
		g.drawString(s2, WIDTH / 2 - length21 / 2-20, HEIGHT / 2 + 70);

		// ---------------- DRAWING FINAL SCORE AT END OF GEME
		// ---------**************------

		g.setColor(Color.GREEN.brighter());
		g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 35));
		String s3 = "PRESS ENTER TO RUN AGAIN..";
		int length22 = (int) g.getFontMetrics().getStringBounds(s1, g)
				.getWidth();
		g.drawString(s3, WIDTH / 2 - length22 / 2 - 130, HEIGHT / 2 + 130);

	}

	// --------------------------GAME DRAW
	// --------------------------------------------

	private void gameDraw() {
		Graphics g2 = this.getGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();

	}

	@Override
	public void keyPressed(KeyEvent key) {
		int keyCode = key.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_NUMPAD4) {
			player.setLeft(true);
		}
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_NUMPAD6) {
			player.setRight(true);
		}
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_NUMPAD8) {
			player.setUp(true);
		}
		if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_NUMPAD5) {
			player.setDown(true);
		}
		if (keyCode == KeyEvent.VK_L) {
			if (player.getLight()) {
				player.setLight(false);
			} else {
				player.setLight(true);
			}

		}
		if (keyCode == KeyEvent.VK_V) {
			if (player.getVanish()) {
				player.setVanish(false);
			} else {
				player.setVanish(true);
			}

		}
		if (keyCode == KeyEvent.VK_J) {
			player.setJump(true);
		}
		if (keyCode == KeyEvent.VK_SPACE) {
			Pause = true;
		}
		if (keyCode == KeyEvent.VK_ENTER) {
			Pause = false;
			Start = true;
			Start = true;
			if ((GameOver = true && RoundNumber > 10)
					|| (GameOver = true && player.IsDead())) {
				GameOver = false;
				RoundStartTime = 0;
				RoundStartTimeDiff = 0;
				RoundNumber = 0;
				NextRoundTimer = System.nanoTime();
				NextRoundTimeDiff = 0;
				scoretimer = System.nanoTime();
				SETscore = false;
			
				player.setPlayer();
				player.setRecoveringtimer(0);
				player.setLives(3);
				player.setScore(0);
				for (int i = 0; i < enemies.size(); i++) {
					Enemy e = enemies.get(i);
					enemies.remove(i);
				}
			}
		}
		if (keyCode == KeyEvent.VK_S) {
			player.setfiring(true);
		}

	}

	@Override
	public void keyReleased(KeyEvent key) {
		int keyCode = key.getKeyCode();
		if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_NUMPAD4) {
			player.setLeft(false);
		}
		if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_NUMPAD6) {
			player.setRight(false);
		}
		if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_NUMPAD8) {
			player.setUp(false);
		}
		if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_NUMPAD5) {
			player.setDown(false);
		}
		if (keyCode == KeyEvent.VK_J) {
			player.setJump(false);
		}
		if (keyCode == KeyEvent.VK_S) {
			player.setfiring(false);
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
