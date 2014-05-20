package client;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import server.model.Environment;
import server.model.object.ItemWithPicture;
import server.model.object.PhysicalObject;
import server.model.object.Player;
import server.model.object.TriggerEndGame;
import server.model.object.TriggerModifyPhysicalAttributes;
import server.model.object.TriggerModifyPhysicalAttributesWithPicture;
import server.model.object.Wall;
import client.object.GraphicalItem;
import client.object.GraphicalItemWithPicture;
import client.object.GraphicalPlayer;
import client.visitor.AbstractVisitor;

public class GraphicalEnvironment {

	List<GraphicalItem> gItems_ = new ArrayList<GraphicalItem>();
	List<GraphicalItemWithPicture> gItemsWithPicture_ = new ArrayList<GraphicalItemWithPicture>();
	//GraphicalPlayer gPlayer_ = null;
	
	public GraphicalEnvironment(Environment env, List<AbstractVisitor> visitors) 
	{
		for(Object object : env.getObjects())
		{
			if (object instanceof Wall)
			{
				gItems_.add(new GraphicalItem((PhysicalObject) object, visitors,true, Color.RED));
			}
			else if (object instanceof Player)
			{
				//gPlayer_ = new GraphicalPlayer((Player) object, visitors);
				gItemsWithPicture_.add(new GraphicalItemWithPicture((Player) object, visitors));
			}
			else if (object instanceof TriggerModifyPhysicalAttributesWithPicture)
			{
				gItemsWithPicture_.add(new GraphicalItemWithPicture((TriggerModifyPhysicalAttributesWithPicture) object, visitors));
			}
			else if (object instanceof TriggerModifyPhysicalAttributes)
			{
				gItems_.add(new GraphicalItem((PhysicalObject) object, visitors,true, Color.GREEN));
			}
			else if (object instanceof TriggerEndGame)
			{
				gItems_.add(new GraphicalItem((PhysicalObject) object, visitors,true, Color.BLUE));
			}
			else if (object instanceof ItemWithPicture)
			{
				gItemsWithPicture_.add(new GraphicalItemWithPicture((ItemWithPicture) object, visitors));
			}
		}
	}

	public void draw(Graphics g, int x, int y, int width, int height) 
	{
		for (GraphicalItem item : gItems_)
		{
			item.draw(g,x,y,width,height);
		}
		for (GraphicalItemWithPicture item : gItemsWithPicture_)
		{
			item.draw(g,x,y,width,height);
		}
//		gPlayer_.draw(g,x,y,width,height);
	}
}
