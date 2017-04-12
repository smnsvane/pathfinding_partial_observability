package basic;

public abstract class AbstractEngineState implements IEngineState
{
	public final int SCREEN_WIDTH, SCREEN_HEIGHT;
	
	public AbstractEngineState(int screenWidth, int screenHeight)
	{
		SCREEN_WIDTH	= screenWidth;
		SCREEN_HEIGHT	= screenHeight;
	}
}
