package nodeSetting;

import core.TileImpl;
import core.TileMapImpl;
import static core.TileType.*;

public class TunnelNodeAlgorithm extends SimpleNodeAlgorithm
{
	private TileMapImpl knowledge;
	
	@Override
	public boolean isValidNode(TileImpl tile, TileMapImpl knowledge)
	{
		if (super.isValidNode(tile, knowledge))
			return true;
		
		this.knowledge = knowledge;
		
		int x = tile.getX(), y = tile.getY();
		return templateTunnel1(x, y) || templateTunnel2(x, y) ||
			templateTunnel3(x, y) || templateTunnel4(x, y);
	}
	
	boolean templateTunnel1(int x, int y)
	{
		return
		//top row
		
		tileIsSolid(x, y-1)	&&
		
		//middle row
		
		
		
		//bottom row
		
		tileIsSolid(x, y+1)	;
		
	}
	boolean templateTunnel2(int x, int y)
	{
		return
		//top row
		
		
		
		//middle row
		tileIsSolid(x-1, y)	&&
		
		tileIsSolid(x+1, y)	;
		//bottom row
		
		
		
	}
	boolean templateTunnel3(int x, int y)
	{
		return
		//top row
		tileIsSolid(x-1, y-1)	&&
		
		
		//middle row
		
		
		
		//bottom row
		
		
		tileIsSolid(x+1, y+1)	;
	}
	boolean templateTunnel4(int x, int y)
	{
		return
		//top row
		
		
		tileIsSolid(x+1, y-1)	&&
		//middle row
		
		
		
		//bottom row
		tileIsSolid(x-1, y+1)	;
		
		
	}
	
	boolean tileIsSolid(int x, int y)
	{
		try
		{
			return knowledge.getTile(x, y).getType().equals(SOLID);
		}
		catch (ArrayIndexOutOfBoundsException e) { return false; }
	}
}
