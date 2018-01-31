import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class DrawLetters extends Panel
{
	protected Image entryImage;
	protected Graphics entryGraphics;
	protected int lastX = -1;
	protected int lastY = -1;
	protected Sample sample;
	protected int rectLeft;
	protected int rectRight;
	protected int rectTop;
	protected int rectBottom;
	protected double ratioX;
	protected double ratioY;
	protected int pixelMap[];
	protected int HEIGHT=145;
	protected int WIDTH=130;
	DrawLetters()
	{

		enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK | AWTEvent.COMPONENT_EVENT_MASK);
	}
	protected void initImage()
	{
		//System.out.println("getBounds().width"+WIDTH);
		//System.out.println("HEIGHT"+HEIGHT);
		entryImage = createImage(WIDTH, HEIGHT);
		entryGraphics = entryImage.getGraphics();
		entryGraphics.setColor(Color.white);
		entryGraphics.fillRect(0, 0, getBounds().width, HEIGHT);
		//entryGraphics.setColor(Color.red);
		//entryGraphics.drawRect(0, 0, WIDTH, HEIGHT);
	}
	public void paint (Graphics g)
	{

		if (entryImage == null)
			initImage();
		g.drawImage(entryImage, 0, 0, this);
		g.setColor(Color.black);
		g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
		g.setColor(Color.blue);
		g.drawRect(rectLeft, rectTop, rectRight - rectLeft,rectBottom - rectTop);
	}
	protected void processMouseEvent(MouseEvent e)
	{

		if (e.getID() != MouseEvent.MOUSE_PRESSED)
			return;
		lastX = e.getX();
		lastY = e.getY();
	}
	protected void processMouseMotionEvent(MouseEvent e)
	{
		if (e.getID() != MouseEvent.MOUSE_DRAGGED)
			return;
		entryGraphics.setColor(Color.black);
		entryGraphics.drawLine(lastX, lastY, e.getX(), e.getY());
		Graphics g = getGraphics();
		g.drawImage(entryImage, 0, 0, this);
		lastX = e.getX();
		lastY = e.getY();
		g.setColor(Color.BLUE);
		g.drawRect(0, 0, WIDTH - 1, HEIGHT - 1);
	}
	public void setSample(Sample s)
	{
		sample = s;
	}

	public Sample getSample()
	{
		return sample;
	}
	protected boolean hLineClear(int y)
	{
		int w = entryImage.getWidth(this);
		for (int i = 0; i < w; i++)
		{
			if (pixelMap[(y * w) + i] != -1)
				return false;
		}
		return true;
	}
	protected boolean vLineClear(int x)
	{
		int w = entryImage.getWidth(this);
		int h = entryImage.getHeight(this);
		for (int i = 0; i < h; i++)
		{
			if (pixelMap[(i * w) + x] != -1)
				return false;
		}

		return true;
	}
	protected void findBounds(int w, int h)
	{
		for (int y = 0; y < h; y++)
		{
			if (!hLineClear(y))
			{
				rectTop = y;
				break;
			}
		}
		for (int y = h - 1; y >= 0; y--)
		{
			if (!hLineClear(y))
			{
				rectBottom = y;
				break;
			}

		}
		for (int x = 0; x < w; x++)
		{
			if (!vLineClear(x))
			{
				rectLeft = x;
				break;
			}

		}
		for (int x = w - 1; x >= 0; x--)
		{
			if (!vLineClear(x))
			{
				rectRight = x;
				break;
			}

		}
	}

	protected boolean downSampleQuadrant(int x, int y)
	{
		int w = entryImage.getWidth(this);
		int startX = (int) (rectLeft + (x * ratioX));
		int startY = (int) (rectTop + (y * ratioY));
		int endX = (int) (startX + ratioX);
		int endY = (int) (startY + ratioY);
		for (int yy = startY; yy <= endY; yy++)
		{
			for (int xx = startX; xx <= endX; xx++)
			{
				int loc = xx + (yy * w);
				if (pixelMap[loc] != -1)
					return true;
			}
		}
		return false;
	}

	public void downSample()
	{
		//System.out.println("WIDTH"+WIDTH);
		//System.out.println("HEIGHT"+HEIGHT);
		int w = entryImage.getWidth(this);
		int h = entryImage.getHeight(this);
		PixelGrabber grabber = new PixelGrabber(entryImage, 0, 0, w, h, true);
		try
		{
			grabber.grabPixels();
			pixelMap = (int[]) grabber.getPixels();
			findBounds(w, h);
			SampleData data = sample.getData();
			ratioX = (double) (rectRight - rectLeft) / (double) data.getWidth();
			ratioY = (double) (rectBottom - rectTop) / (double) data.getHeight();
			for (int y = 0; y < data.getHeight(); y++)
			{
				for (int x = 0; x < data.getWidth(); x++)
				{
					if (downSampleQuadrant(x, y))
						data.setData(x, y, true);
					else
						data.setData(x, y, false);
				}
			}
			sample.repaint();
			repaint();
		}
		catch (InterruptedException e)
		{}
	}

	public void clear()
	{
		this.entryGraphics.setColor(Color.white);
		this.entryGraphics.fillRect(0, 0, WIDTH, HEIGHT);
		//entryGraphics.setColor(Color.red);
		//entryGraphics.drawRect(0, 0, WIDTH, HEIGHT);
		this.rectBottom = this.rectTop = this.rectLeft = this.rectRight = 0;
		repaint();
	}
}
