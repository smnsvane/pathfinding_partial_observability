package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class DataReader
{
	public static List<TileImpl> readFile(String filename)
	{
		File file 				= new File(filename);
		BufferedReader bf		= null;
		List<TileImpl> tiles	= new ArrayList<TileImpl>();
		
		try { bf = new BufferedReader(new FileReader(file)); }
		catch (FileNotFoundException e)
		{
			System.err.println(filename+" not found");
			e.printStackTrace();
			System.exit(1);
		}
		
		try
		{
			while (bf.ready())
			{
				String line = bf.readLine();
				if (line.equals("") || line.charAt(0) == '#') continue;
				String[] elements = line.split("\\s");
				// convert string to enum
				TileType fieldType = TileType.valueOf(elements[2]);
				tiles.add(new TileImpl(Integer.parseInt(elements[0]),
						Integer.parseInt(elements[1]), fieldType));
			}
		}
		catch (IOException e) { e.printStackTrace(); }
		return tiles;
	}
}
