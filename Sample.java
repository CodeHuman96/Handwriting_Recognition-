import java.awt.*;
public class Sample extends Panel
{
 	SampleData data;
  	Sample(int width, int height)
  	{
    	data = new SampleData(' ', width, height);
  	}
  	SampleData getData()
  	{
    	return data;
  	}
  	void setData(SampleData data)
  	{
    	this.data = data;
  	}
  	public void paint(Graphics g)
  	{
    	if (data == null)
      		return;
    	int x, y;
    	int vcell = getBounds().height / data.getHeight();
    	int hcell = getBounds().width / data.getWidth();

   		g.setColor(Color.white);
    	g.fillRect(0, 0, getBounds().width, getBounds().height);
    	g.setColor(Color.black);

    	for (y = 0; y < data.getHeight(); y++)
      		g.drawLine(0, y * vcell, getBounds().width, y * vcell);
    	for (x = 0; x < data.getWidth(); x++)
      		g.drawLine(x * hcell, 0, x * hcell, getBounds().height);
    	for (y = 0; y < data.getHeight(); y++)
    	{
      		for (x = 0; x < data.getWidth(); x++)
      		{
        		if (data.getData(x, y))
          			g.fillRect(x * hcell, y * vcell, hcell, vcell);
      		}
    	}

    	g.setColor(Color.black);
    	g.drawRect(0, 0, getBounds().width - 1, getBounds().height - 1);
  }
}
