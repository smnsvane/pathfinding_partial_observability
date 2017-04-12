package core;

import java.awt.Color;

public enum TileType {

	CLEAR			(Color.WHITE),
	SOLID			(Color.BLACK),
	SHADOW			(Color.GRAY),
	AGENT			(Color.BLUE),
	GOAL			(Color.RED),
	NODE			(Color.GREEN),
	DISABLED_NODE	(Color.CYAN);

	private Color color;
	public Color getColor() { return color; }

	private TileType(Color color) {
		this.color = color;
	}
}
