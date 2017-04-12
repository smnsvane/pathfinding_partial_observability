package core;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public abstract class SaveRestore
{
	public static void save(Serializable obj, String filename) throws IOException
	{
		FileOutputStream fileStream = new FileOutputStream(filename);
		ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
		objectStream.writeObject(obj);
		objectStream.close();
		fileStream.close();
	}
	public static Object restore(String filename) throws IOException, ClassNotFoundException
	{
		FileInputStream fileStream = new FileInputStream(filename);
		ObjectInputStream objectStream = new ObjectInputStream(fileStream);
		Object obj = objectStream.readObject();
		objectStream.close();
		fileStream.close();
		return obj;
	}
}
