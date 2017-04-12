package mapPurgers;

import java.io.IOException;
import java.util.List;

import core.Node;
import core.SaveRestore;
import core.TileImpl;
import core.TileMapImpl;

import static core.TileType.*;

public abstract class AbstractPurger
{
	private TileMapImpl map;
	public TileMapImpl getMap() { return map; }
	
	/** returns true if node was removed */
	public abstract boolean purge(Node tileNode);
	
	private String filename;
	public AbstractPurger(String filename)
	{
		this.filename = filename;
		
		try { map = (TileMapImpl) SaveRestore.restore(filename); }
		catch (Exception e) {  e.printStackTrace(); }
	}
	
	public void purge()
	{
		List<TileImpl> list = map.getAllTiles(NODE);
		for (int i = 0; i < list.size(); i++)
			if (list.get(i).getType().equals(DISABLED_NODE))
				list.remove(i--);
		
		for (int i = 0; i < list.size(); i++)
		{
			Node n = (Node) list.get(i);
			boolean removed = purge(n);
			if (removed)
				list.remove(i--);
		}
		
		try { SaveRestore.save(map, filename); }
		catch (IOException e) { e.printStackTrace(); }
	}
}
