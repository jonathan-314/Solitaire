package Solitaire;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Solitaire extends JPanel implements MouseListener {
	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * solitaire instance
	 */
	public static Solitaire game;

	/**
	 * JFrame
	 */
	JFrame jf;

	/**
	 * width of screen
	 */
	int screenWidth = getToolkit().getScreenSize().width;

	/**
	 * height of screen
	 */
	int screenHeight = getToolkit().getScreenSize().height;

	/**
	 * Deck of cards
	 */
	ArrayList<Card> deck = new ArrayList<Card>();

	/**
	 * Which index of the deck is currently revealed
	 */
	int deckIndex = -1;

	/**
	 * Playing field
	 */
	@SuppressWarnings("unchecked")
	ArrayList<Card>[] field = new ArrayList[7];

	/**
	 * Which card(s) have been selected by the user
	 */
	ArrayList<Card> selected = new ArrayList<Card>();

	/**
	 * Which column has been selected
	 */
	int selectedColumn = -2;

	/**
	 * Suits of the cards
	 */
	static final String[] suits = { "D", "H", "S", "C" };

	/**
	 * Graphical representation of each suit
	 */
	static final Polygon[] suitsPolygon = { new Polygon(new int[] { -4, 0, 4, 0 }, new int[] { 0, 8, 0, -8 }, 4),
			new Polygon(new int[] { 0, -6, -3, 0, 3, 6 }, new int[] { 5, -5, -5, -4, -5, -5 }, 6),
			new Polygon(new int[] { -2, -2, -6, 0, 6, 2, 2 }, new int[] { 6, 2, 2, -6, 2, 2, 6 }, 7),
			new Polygon(new int[] { -2, -2, -6, -6, -2, -2, 2, 2, 6, 6, 2, 2 },
					new int[] { -6, -2, -2, 2, 2, 6, 6, 2, 2, -2, -2, -6 }, 12) };

	/**
	 * String representation of names of cards
	 */
	static final String[] names = { "", "A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K" };

	/**
	 * Is the mouse button being held down
	 */
	boolean mousePressed = false;

	/**
	 * current x coordinate of the position of the mouse
	 */
	int currentx;

	/**
	 * current y coordinate of the position of the mouse
	 */
	int currenty;

	/**
	 * previous x coordinate of the position of the mouse
	 */
	int previousx;

	/**
	 * previous y coordinate of the position of the mouse
	 */
	int previousy;

	/**
	 * The final stack of cards of each suit
	 */
	int[] pile = { 0, 0, 0, 0, 0 };

	/**
	 * starting time, used for timing
	 */
	long startTime = 0;

	/**
	 * elapsed time, used for timing
	 */
	long time = 0;

	/**
	 * is the game over?
	 */
	boolean gameOver = false;

	/**
	 * solitaire constructor
	 */
	public Solitaire() {
		addMouseListener(this);
		setFocusable(true);
	}

	/**
	 * initializes the playing field
	 */
	public void start() {
		for (int i = 0; i < 7; i++)
			field[i] = new ArrayList<Card>();
		deck.clear();
		for (int i = 0; i < 4; i++)
			for (int j = 1; j <= 13; j++)
				deck.add(new Card(j, i));
		Random ran = new Random(); // random permutation of the cards
		for (int i = 0; i < 52; i++) {
			int index = ran.nextInt(52 - i);
			Card picked = deck.remove(index);
			deck.add(picked); // removes random card and adds to end
		}
		for (int i = 0; i < 7; i++) {
			for (int j = i; j < 7; j++) {
				Card c = deck.remove(0);
				field[j].add(c);
				c.revealed = i == j ? true : false;
				c.x = 100 * j + 200;
				c.y = 50 * i + 50;
			}
		}

		jf = new JFrame("solitaire");
		jf.setVisible(true);
		jf.setSize(getToolkit().getScreenSize());
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(game);
		startTime = System.currentTimeMillis();
		while (true) {
			if (!gameOver)
				time = System.currentTimeMillis() - startTime;
			jf.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Game over!
	 */
	public void gameOver() {
		gameOver = true;
		JOptionPane.showMessageDialog(this, "Game Over! You win!");
		System.exit(ABORT);
	}

	/**
	 * paint function, called every frame
	 */
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screenWidth, screenHeight);

		if (pile[0] == 13 && pile[1] == 13 && pile[2] == 13 && pile[3] == 13 && pile[4] == 0) {
			pile[4] = 1;
			gameOver();
		}
		if (mousePressed) {
			currentx = MouseInfo.getPointerInfo().getLocation().x;
			currenty = MouseInfo.getPointerInfo().getLocation().y - 44;
			for (Card c : selected) {
				c.x += currentx - previousx;
				c.y += currenty - previousy;
			}
			previousx = currentx;
			previousy = currenty;
		}
		g.setColor(Color.YELLOW);
		g.drawString("" + deck.size(), 1100, 100);
		g.drawString("" + deckIndex, 1100, 120);
		g.drawOval(screenWidth / 2 - 50, 8, 15, 15);
		g.drawString("C", screenWidth / 2 - 50 + 3, 20);
		g.drawString("Jonathan Guo", screenWidth / 2 - 50 + 20, 20);
		
		for (int i = 0; i < 8; i++)
			g.drawLine(100 * i + 190, 50, 100 * i + 190, 720);

		// field
		for (int i = 0; i < 7; i++) {
			ArrayList<Card> column = field[i];
			for (Card c : column) {
				if (c.revealed) { // card is revealed
					c.drawSelf(g);
				} else { // card is not revealed
					g.setColor(Color.DARK_GRAY);
					g.fillRect(c.x, c.y, 80, 100);
					g.setColor(Color.BLUE);
					g.drawRect(c.x, c.y, 80, 100);
				}
			}
		}
		// pile
		for (int i = 0; i < 4; i++) {
			g.setColor(Color.WHITE);
			g.fillRect(950, 125 * i + 50, 80, 100);
			g.setColor(Color.BLUE);
			g.drawRect(950, 125 * i + 50, 80, 100);
			if (pile[i] > 0) {
				if (i < 2)
					g.setColor(Color.RED);
				else
					g.setColor(Color.BLACK);
				g.drawString(names[pile[i]], 975, 125 * i + 75);
				Polygon icon = suitsPolygon[i];
				int[] iconx = new int[icon.npoints];
				int[] icony = new int[icon.npoints];
				for (int k = 0; k < icon.npoints; k++) {
					iconx[k] = 998 + icon.xpoints[k];
					icony[k] = 125 * i + 71 + icon.ypoints[k];
				}
				g.fillPolygon(iconx, icony, icon.npoints);
			}
		}
		// deck
		for (int i = 0; i < 2; i++) {
			if (deck.isEmpty())
				continue;
			if (i == 1 && deckIndex == -1)
				continue;
			if (i == 0 && deckIndex == deck.size() - 1)
				continue;
			g.setColor(Color.WHITE);
			if (i == 0)
				g.setColor(Color.DARK_GRAY);
			g.fillRect(50, 125 * i + 50, 80, 100);
			g.setColor(Color.BLUE);
			g.drawRect(50, 125 * i + 50, 80, 100);
			if (deckIndex >= 0 && i == 1 && !deck.isEmpty()) {
				Card c = deck.get(deckIndex);
				g.setColor(c.red ? Color.RED : Color.BLACK);
				g.drawString(c.name, 75, 200);
				Polygon icon = suitsPolygon[c.suit];
				int[] iconx = new int[icon.npoints];
				int[] icony = new int[icon.npoints];
				for (int k = 0; k < icon.npoints; k++) {
					iconx[k] = 98 + icon.xpoints[k];
					icony[k] = 196 + icon.ypoints[k];
				}
				g.fillPolygon(iconx, icony, icon.npoints);
			}
		}
		for (Card c : selected)
			c.drawSelf(g);

		g.setColor(Color.BLACK);
		g.setFont(new Font("Helvetica", 30, 30));
		long seconds = (time / 1000) % 60;
		long minutes = (time / 1000) / 60;
		String timeDisplay = Long.toString(seconds);
		if (timeDisplay.length() == 1)
			timeDisplay = "0" + timeDisplay;
		timeDisplay = minutes + ":" + timeDisplay;
		g.drawString(timeDisplay, 1100, 160);
	}

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		game = new Solitaire();
		game.start();
	}

	/**
	 * Card class
	 * 
	 * @author jonguo6
	 */

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		int mousex = e.getX();
		int mousey = e.getY();
		int column = (mousex - 190) / 100;
		if (mousex < 190) {
			// deck
			if (mousey < 162) {
				deckIndex++;
				if (deckIndex == deck.size())
					deckIndex = -1;
			} else if (deckIndex >= 0) {
				Card c = deck.remove(deckIndex);
				selected.add(c);
				c.x = 50;
				c.y = 175;
				deckIndex--;
				selectedColumn = -1;
			}
		} else if (mousex > 890) {
			// right side, do nothing
		} else if (column < 7) {
			selectedColumn = column;
			selected.clear();
			ArrayList<Card> stack = field[column];
			for (int i = 0; i < stack.size(); i++) {
				Card c = stack.get(i);
				int add = 50;
				if (i == stack.size() - 1)
					add = 100;
				if (c.y + add >= mousey && c.revealed)
					selected.add(c);
			}
			for (int j = 0; j < selected.size(); j++)
				field[column].remove(selected.get(j));
		}
		mousePressed = true;
		previousx = mousex;
		previousy = mousey;
		currentx = mousex;
		currenty = mousey;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int mousey = e.getX();
		int column = (mousey - 190) / 100;
		boolean found = false;
		if (mousey < 190) {
			// deck, do nothing
		} else if (mousey > 890 || column >= 7) {
			// right side
			if (selected.size() == 1) {
				Card c = selected.get(0);
				if (c.value - 1 == pile[c.suit]) {
					found = true;
					pile[c.suit]++;
				}
			}
		} else {
			ArrayList<Card> temp = field[column];
			if (selected.isEmpty())
				return;
			Card first = selected.get(0);
			if (selectedColumn == -1)
				first.revealed = true;
			if (temp.isEmpty()) {
				if (first.value == 13) {
					found = true;
					for (Card c : selected) {
						field[column].add(c);
						c.x = 100 * column + 200;
						c.y = 50 * field[column].indexOf(c) + 50;
					}
				}
			} else {
				Card last = temp.get(temp.size() - 1);
				if (last.y <= first.y && first.value + 1 == last.value) {
					if (first.red != last.red) {
						found = true;
						for (Card c : selected) {
							field[column].add(c);
							c.x = 100 * column + 200;
							c.y = 50 * field[column].indexOf(c) + 50;
						}
					}
				}
			}
		}
		if (!found) { // invalid drop
			if (selectedColumn == -1) {
				ArrayList<Card> newDeck = new ArrayList<Card>();
				if (deckIndex == -1)
					newDeck.add(selected.get(0));
				for (int i = 0; i < deck.size(); i++) {
					newDeck.add(deck.get(i));
					if (i == deckIndex)
						newDeck.add(selected.get(0));
				}
				deck.clear();
				for (Card c : newDeck)
					deck.add(c);
				deckIndex++;
			} else {
				for (Card c : selected) {
					field[selectedColumn].add(c);
					c.x = 100 * selectedColumn + 200;
					c.y = 50 * field[selectedColumn].indexOf(c) + 50;
				}
			}
		} else {
			if (selectedColumn != -1)
				if (!field[selectedColumn].isEmpty())
					field[selectedColumn].get(field[selectedColumn].size() - 1).revealed = true;
		}
		mousePressed = false;
		selectedColumn = -2;
		selected.clear();
		if (deck.isEmpty()) {
			boolean finished = true;
			for (int i = 0; i < 7; i++) {
				for (Card c : field[i]) {
					if (!c.revealed) {
						finished = false;
						break;
					}
				}
			}
			if (finished) {
				deck.clear();
				for (int i = 0; i < 7; i++)
					field[i].clear();
				for (int j = 0; j < 4; j++)
					pile[j] = 13;
				gameOver();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}