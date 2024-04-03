import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.applet.Applet;
import java.applet.AudioClip;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * The main class for the Schmetris applet. Game engine, audio clip loader, key
 * listener.
 * 
 * @author Sam Scott
 */
public class Schmetriss extends JApplet implements ActionListener, KeyListener {

	public static void main(String[] args) {
		Schmetriss s = new Schmetriss();
		JFrame f = new JFrame("Schmetris");
		f.setContentPane(s);
		s.setPreferredSize(new Dimension(375,470));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setResizable(false);
		s.init();
		s.requestFocusInWindow();
		f.setVisible(true);
	}
	/**
	 * Y location of the main board
	 */
	static final int BOARD_Y = 20;
	/**
	 * X location of the main board
	 */
	static final int BOARD_X = 20;
	/*
	 * Y location of the score board
	 */
	static final int SCORE_Y = 20;
	/**
	 * X location of the score board
	 */
	static final int SCORE_X = 280;
	/**
	 * Level 1 delay (speed) for game (in milliseconds)
	 */
	static final int START_DELAY = 600;
	/**
	 * Minimum delay (speed) for game (in milliseconds)
	 */
	static final int FASTEST_TIME = 60;
	/**
	 * Basic time increment for game engine (in milliseconds)
	 */
	static final int TIME_INC = 30;
	/**
	 * Background color
	 */
	static final Color BACKGROUND_COLOR = Color.black;
	/**
	 * row * this constant = basic score for a cleared row
	 */
	static final int SCORE_ROWMULT = 20;
	/**
	 * row * this constant = basic score for landing a piece
	 */
	static final int SCORE_DROPROWMULT = 5;
	/**
	 * every row you fast drop through scores this many points
	 */
	static final int SCORE_DROPINC = 5;
	/**
	 * pause before starting a game
	 */
	static final int COUNTDOWN_START = 3000;
	/**
	 * speedup factor for the game when advancing levels
	 */
	static final double SPEED_LEVELMULT = 0.8;
	/**
	 * level to begin putting down random X-blocks
	 */
	static final int RANDBLOCKLEVEL = 4;
	/**
	 * initial wait time between X-blocks (milliseconds)
	 */
	static final int RANDBLOCKTIME = 10000;
	/**
	 * speedup factor for X-blocks when advancing levels
	 */
	static final double RANDBLOCKMULT = .8;
	/**
	 * double buffering
	 */
	Image buffer;
	/**
	 * the tetris board
	 */
	TetrisBoard board;
	/**
	 * the score board
	 */
	TetrisScore score;
	/**
	 * the current piece in play
	 */
	Piece currentPiece;
	/**
	 * the current game speed (delay between moves in milliseconds)
	 */
	int delay;
	/**
	 * keeps track of amount of time game has been idle (used for deciding when
	 * next move should be made by the main game loop)
	 */
	int waitTime;
	/**
	 * value of last key press (from getKeyCode)
	 */
	int keyPress = 0;
	/**
	 * value of current key held down (from getKeyCode)
	 */
	int keyDown = 0;
	/**
	 * fast drop score for the current piece
	 */
	int pieceScore;
	/**
	 * when multiple levels cleared, this counter keeps track of how many have
	 * been cleared so far.
	 */
	int multiRow;
	/**
	 * the current game level
	 */
	int level;
	/**
	 * number of milliseconds to go before starting the game
	 */
	int countDown;
	/**
	 * number of milliseconds to go before a new X-block appears
	 */
	int randBlockCountdown;
	/**
	 * is game on or not?
	 */
	boolean gameOver = true;
	/**
	 * play music?
	 */
	boolean music = false;
	/**
	 * play sound effects?
	 */
	boolean sounds = true;
	/**
	 * are soundclips still loading?
	 */
	boolean soundLoading = true;
	/**
	 * main game music
	 */
	AudioClip musicA;
	/**
	 * game over music
	 */
	AudioClip musicB;
	/**
	 * level up sound
	 */
	AudioClip levelUp;
	/**
	 * piece advancing sound
	 */
	AudioClip advance;
	/**
	 * move left/right sound
	 */
	AudioClip move;
	/**
	 * clear row sound
	 */
	AudioClip killLine;
	/**
	 * rotate piece sound
	 */
	AudioClip rotate;
	/**
	 * game over sound
	 */
	AudioClip gameOverSound;
	/**
	 * piece lands / is made permanent sound
	 */
	AudioClip makePermanent;
	/**
	 * random block appearance sound
	 */
	AudioClip randBlock;

	/**
	 * load audio clips, create image buffer, board, and scoreboard, add key
	 * listeners, start timer and music.
	 */
	public void init() {
		System.out.println("init start");
		// make sure music is loaded before starting
		System.out.println("init check 1");

		//musicB = getAudioClip(getCodeBase(), "Fatboy Slim .mid");
		//musicA = getAudioClip(getCodeBase(), "chemicalbrothers_loopsoffury.mid");
		// spin off a thread for loading the sound effects
		new SoundLoader();
		System.out.println("init check 2");
		// create and start stuff
		buffer = createImage(getWidth(), getHeight());
		System.out.println("init check 3");
		board = new TetrisBoard(this, BOARD_X, BOARD_Y);
		System.out.println("init check 4");
		score = new TetrisScore(SCORE_X, SCORE_Y);
		System.out.println("init check 5");
		addKeyListener(this);
		new Timer(TIME_INC, this).start(); // timer for main game loop
		playMusic(musicB, false);
		//new Thread(this).start(); // request focus after a short delay so the
		// user doesn't have to click on the applet
		System.out.println("init finish");
	}

	/**
	 * wait half a second, then request focus
	 */
	//	public void run() {
	//		try {
	//			Thread.sleep(100);
	//		} catch (Exception e) {
	//
	//		}
	//		requestFocusInWindow();
	//	}

	/**
	 * stop the music on exit
	 */
	public void stop() {
		stopSoundClip(musicA);
		stopSoundClip(musicB);
	}

	/**
	 * loads the sound effects in a separate thread
	 * 
	 * @author Sam Scott
	 */
	public class SoundLoader implements Runnable {

		/**
		 * set the sound loading flag and start the thread
		 */
		SoundLoader() {
			soundLoading = true;
			repaint();
			new Thread(this).start();
		}

		/**
		 * load all the sound effects
		 */
		public void run() {

			advance = Applet.newAudioClip(Schmetriss.class.getResource("rim.wav"));
			//advance = getAudioClip(getCodeBase(), "rim.wav");
			move = Applet.newAudioClip(Schmetriss.class.getResource("rim.wav"));
			//move = getAudioClip(getCodeBase(), "rim.wav");
			makePermanent = Applet.newAudioClip(Schmetriss.class.getResource("PAD_LightWaves.wav"));
			//makePermanent = getAudioClip(getCodeBase(), "PAD_LightWaves.wav");
			killLine = Applet.newAudioClip(Schmetriss.class.getResource("VT_BD_short2.wav"));
			//killLine = getAudioClip(getCodeBase(), "VT_BD_short2.wav");
			rotate = Applet.newAudioClip(Schmetriss.class.getResource("sd7.wav"));
			//rotate = getAudioClip(getCodeBase(), "sd7.wav");
			levelUp = Applet.newAudioClip(Schmetriss.class.getResource("FX_WindChimes.wav"));
			//levelUp = getAudioClip(getCodeBase(), "FX_WindChimes.wav");
			gameOverSound = Applet.newAudioClip(Schmetriss.class.getResource("RD_Crash_2.wav"));
			//gameOverSound = getAudioClip(getCodeBase(), "RD_Crash_2.wav");
			randBlock = Applet.newAudioClip(Schmetriss.class.getResource("FX_Scratch.wav"));
			//randBlock = getAudioClip(getCodeBase(), "FX_Scratch.wav");
			soundLoading = false;
			musicB = Applet.newAudioClip(Schmetriss.class.getResource("FatboySlim.mid"));
			musicA = Applet.newAudioClip(Schmetriss.class.getResource("chemicalbrothers_loopsoffury.mid"));
			if (gameOver && music)
				playMusic(musicB, false);
			else if (music)
				playMusic(musicA, true);
			repaint();
		}
	}

	/**
	 * called to start a new game
	 */
	public void initGame() {
		score.resetScore();
		board.clear();
		currentPiece = null;
		delay = START_DELAY;
		waitTime = 0;
		pieceScore = 0;
		multiRow = 0;
		level = 1;
		stopSoundClip(musicB);
		playMusic(musicA, true);
		countDown = COUNTDOWN_START;
		randBlockCountdown = 0;
		gameOver = false; // should happen last
	}

	/**
	 * smart method to play a sound if possible / appropriate
	 * 
	 * @param s
	 *            the sound to play
	 * @param loop
	 *            true if the sound should be looped
	 */
	public void playSound(AudioClip s, boolean loop) {
		if (s != null) { // only play if the sound is loaded
			s.stop();
			if (sounds) // only play if the sounds are turned on
				if (loop)
					s.loop();
				else
					s.play();
		}
	}

	/**
	 * smart method to play music if possible / appropriate
	 * 
	 * @param s
	 *            the sound to play
	 * @param loop
	 *            true if the sound should be looped
	 */
	public void playMusic(AudioClip m, boolean loop) {
		if (m != null) { // only play if the sound is loaded
			m.stop();
			if (music) // only play if the music is turned on
				if (loop)
					m.loop();
				else
					m.play();
		}
	}

	/**
	 * smart method to stop a sound if possible
	 * 
	 * @param s
	 *            the sound to stop
	 */
	public void stopSoundClip(AudioClip s) {
		if (s != null) // only stop if it's actually loaded
			s.stop();
	}

	/**
	 * turn sound effects on/off
	 */
	public void toggleSound() {
		if (sounds)
			sounds = false;
		else
			sounds = true;
		repaint();
	}

	/**
	 * turn music on/off
	 */
	public void toggleMusic() {
		if (music) {
			music = false;
			stopSoundClip(musicA);
			stopSoundClip(musicB);
		} else {
			music = true;
			if (gameOver)
				playMusic(musicB, false);
			else
				playMusic(musicA, true);
		}
		repaint();
	}

	/**
	 * draw the game screen
	 * 
	 * @param g
	 *            the graphics context
	 */
	public void paint(Graphics g) {
		// double buffering
		Graphics screengc = g;
		g = buffer.getGraphics();

		// required in japplets
		super.paint(g);

		// draw the background color
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, getWidth(), getHeight());

		// draw stuff
		board.draw(g);
		score.draw(g);
		if (currentPiece != null)
			currentPiece.draw(g, board);

		// a bit kludgy - draws the music / sound on off text
		g.setColor(Color.gray);
		g.setFont(new Font("SansSerif", Font.PLAIN, 9));
		if (music)
			g.drawString("m = music off", 25, 452);
		else
			g.drawString("m = music on", 25, 452);
		if (sounds)
			g.drawString("s = sounds off", 95, 452);
		else
			g.drawString("s = sounds on", 95, 452);
		if (soundLoading)
			g.drawString("sound effects loading", 50, 465);
		g.drawString("d = rotation direction", 170, 452);

		// Draws big text when game over or starting
		// -- also a bit kludgy
		if (gameOver) {
			g.setColor(Color.yellow);
			g.setFont(new Font("SansSerif", Font.BOLD, 50));
			g.drawString("Press", 50, 100);
			g.drawString("Enter", 50, 150);
			g.drawString("To", 50, 200);
			g.drawString("Start", 50, 250);
		} else if (countDown > 0) {
			g.setColor(Color.red);
			g.setFont(new Font("SansSerif", Font.BOLD, 50));
			g.drawString("Starting", 45, 150);
		}

		// double buffering
		screengc.drawImage(buffer, 0, 0, null);
	}

	/**
	 * required for double buffering
	 */
	public void update(Graphics g) {
		paint(g);
	}

	/**
	 * Main game engine. Called by the timer every TIME_INC milliseconds.
	 * 
	 * @param e
	 *            unused
	 */
	public void actionPerformed(ActionEvent e) {
		// if game is not in progress, don't do anything
		if (!gameOver) {
			// if we are in game start countdown, don't do anything
			if (countDown > 0)
				countDown -= TIME_INC;
			// otherwise, do stuff
			else {
				// process countdown to the appearance of an X-block
				if (randBlockCountdown > 0) {
					randBlockCountdown -= TIME_INC;
					// this part is just screen stuff to get a nice-looking
					// countdown on the score board
					int temp = score.getRBTimeLeft();
					score
					.setRBTimeLeft((int) (randBlockCountdown / 1000.0 + 0.9999999999));
					if (score.getRBTimeLeft() != temp)
						repaint();
				}
				// increment the wait time (used to decide whether it's time to
				// auto-advance the piece)
				waitTime += TIME_INC;
				// flag to tell us whether user advanced the piece
				boolean advFlag = false;
				// process user input, if any
				if (keyPress == KeyEvent.VK_UP) {
					keyPress = 0; // force separate key presses 
					if (currentPiece != null) {
						if (currentPiece.rotate(board)) {
							playSound(rotate, false);
							repaint();
						}
					}
				} else if (keyPress == KeyEvent.VK_DOWN) {
					keyPress = keyDown; // allow holding down the key to advance piece
					if (currentPiece != null) {
						if (currentPiece.advance(board)) {
							pieceScore += SCORE_DROPINC;
							playSound(advance, false);
							repaint();
						}
					}
				} else if (keyPress == KeyEvent.VK_LEFT) {
					keyPress = 0; // force separate key presses 
					if (currentPiece != null) {
						if (currentPiece.moveLeft(board)) {
							playSound(move, false);
							repaint();
						}
					}
				} else if (keyPress == KeyEvent.VK_RIGHT) {
					keyPress = 0; // force separate key presses 
					if (currentPiece != null) {
						if (currentPiece.moveRight(board)) {
							playSound(move, false);
							repaint();
						}
					}
				}
				// if it is time, process an auto-drop and/or row clear
				if (waitTime >= delay) {
					waitTime = 0;
					// if there is no piece right now, check for row clearing possibilities
					if (currentPiece == null) {
						double row = board.clearRow();
						// if no row to clear, generate a new piece and check for game over
						if (row == -1) {
							multiRow = 0;
							currentPiece = score.getNextPiece(0,
									TetrisBoard.WIDTH / 2 - 2);
							// if new piece causes an instant collision, it's game over
							if (currentPiece.collision(board)) {
								gameOver = true;
								stopSoundClip(musicA);
								playSound(gameOverSound, false);
								playMusic(musicB, false);
							}
						} else {
							// cleared a row - score it
							playSound(killLine, false);
							int newLevel = score.lineCleared();
							score.add((int) (row * SCORE_ROWMULT
									* (int) Math.pow(2, multiRow) * level));
							// reset X-block countdown
							randBlockCountdown = (int) (RANDBLOCKTIME * Math
									.pow(RANDBLOCKMULT, level - RANDBLOCKLEVEL));
							multiRow++;
							// advance level if necessary
							if (newLevel > level) {
								level = newLevel;
								randBlockCountdown = (int) (RANDBLOCKTIME * Math
										.pow(RANDBLOCKMULT, level
												- RANDBLOCKLEVEL));
								playSound(levelUp, false);
								delay = (int) (delay * SPEED_LEVELMULT);
								if (delay < FASTEST_TIME)
									delay = FASTEST_TIME;
								board.levelUpAnim(level);
							}
						}
					} else {
						// There is a piece in play, so advance it
						if (!currentPiece.advance(board)) {
							// if the piece can't advance, make it permanent
							board.makePermanent(currentPiece);
							playSound(makePermanent, false);
							score.add((currentPiece.getTop() * SCORE_DROPROWMULT
									+ pieceScore)*level);
							pieceScore = 0;
							currentPiece = null; // remove the piece from play
							// create an X-Block if necessary
							if (level >= RANDBLOCKLEVEL
									&& randBlockCountdown <= 0) {
								if (board.randomBlock())
									playSound(randBlock, false);
								randBlockCountdown = (int) (RANDBLOCKTIME * Math
										.pow(RANDBLOCKMULT, level
												- RANDBLOCKLEVEL));
							}
						}
					}
				}
			}
		}
		repaint();
	}

	/**
	 * if a key is pressed, record it for later processing. Unless it's <Enter>
	 * to start the game, or m or s to toggle music/sound.
	 * 
	 * @param e
	 *            unused
	 */
	public void keyPressed(KeyEvent e) {
		keyPress = e.getKeyCode();
		keyDown = keyPress;
		if (gameOver && keyPress == KeyEvent.VK_ENTER)
			initGame();
		if (keyPress == (int) 'M')
			toggleMusic();
		else if (keyPress == (int) 'S')
			toggleSound();
		else if (keyPress == (int) 'D')
		{
			score.changeDirection();
			if (currentPiece != null)
				currentPiece.changeDirection();
		}
	}

	/**
	 * If a key is released, record that there is no key down.
	 */
	public void keyReleased(KeyEvent e) {
		keyDown = 0;
	}

	/**
	 * required for keylistener interface
	 */
	public void keyTyped(KeyEvent e) {
	}
}
