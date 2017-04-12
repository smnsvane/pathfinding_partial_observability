package basic;

public class Updateloop implements Runnable
{
	Thread animator;
	final Updatable u;
	/** updates per second */
	final double ups;
	
	public Updateloop(Updatable u, double ups)
	{
		this.u = u;
		this.ups = ups;
	}
	
	public void start()
	{
		animator = new Thread(this);
		animator.start();
	}
	public void stop()
	{
		animator = null;
	}
	@Override
	public void run()
	{
		while (animator != null)
		{
			u.update();
			try { Thread.sleep((long)(1000/ups)); }
			catch (InterruptedException e)
			{
				System.err.println
				("animator thread (run loop in "+getClass()+") was interrupted");
				break;
			}
		}
	}
}
