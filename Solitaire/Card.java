package Solitaire;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

public class Card {

	/**
	 * Numerical value of the card (A=1, J=11, Q=12, K=13)
	 */
	int value;

	/**
	 * String representation of value
	 */
	String name;

	/**
	 * is this card red
	 */
	boolean red;

	/**
	 * which suit this card is
	 * <p>
	 * diamond = 0, heart = 1, spade = 2, club = 3
	 * </p>
	 */
	int suit;

	/**
	 * Is this card in the deck
	 */
	boolean deck = true;

	/**
	 * Location of the card
	 */
	int x, y;

	/**
	 * Is this card revealed
	 */
	boolean revealed = false;

	/**
	 * Card constructor
	 * 
	 * @param value value of the card
	 * @param suit  suit of the card
	 */
	public Card(int value, int suit) {
		this.suit = suit;
		this.value = value;
		name = Solitaire.names[value];
		red = (this.suit < 2);
	}

	/**
	 * draws self
	 * 
	 * @param g graphics
	 */
	public void drawSelf(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(this.x, this.y, 80, 100);
		g.setColor(Color.BLUE);
		g.drawRect(this.x, this.y, 80, 100);
		g.setColor(this.red ? Color.RED : Color.BLACK);
		g.drawString(this.name, this.x + 25, this.y + 25);
		Polygon icon = Solitaire.suitsPolygon[this.suit];
		int[] iconx = new int[icon.npoints];
		int[] icony = new int[icon.npoints];
		for (int k = 0; k < icon.npoints; k++) {
			iconx[k] = this.x + 48 + icon.xpoints[k];
			icony[k] = this.y + 21 + icon.ypoints[k];
		}
		g.fillPolygon(iconx, icony, icon.npoints);
	}
}