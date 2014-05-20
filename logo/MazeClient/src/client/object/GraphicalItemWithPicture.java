package client.object;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import server.model.object.ItemWithPicture;
import server.model.object.PhysicalObject;
import server.model.object.Player;
import server.model.object.TriggerModifyPhysicalAttributesWithPicture;
import client.visitor.AbstractVisitor;

public class GraphicalItemWithPicture 
{
	private PhysicalObject data_;
	private List<AbstractVisitor> displayer_ = null;
	private Image image_ = null;
	
	public GraphicalItemWithPicture(ItemWithPicture data, List<AbstractVisitor> visitors) 
	{
		displayer_ = visitors;
		data_ = data;
		try {
			image_ = ImageIO.read(new File(data.getImagePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GraphicalItemWithPicture(TriggerModifyPhysicalAttributesWithPicture data, List<AbstractVisitor> visitors) 
	{
		displayer_ = visitors;
		data_ = data;
		try {
			image_ = ImageIO.read(new File(data.getImagePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public GraphicalItemWithPicture(Player data, List<AbstractVisitor> visitors) 
	{
		displayer_ = visitors;
		data_ = data;
		try {
			image_ = ImageIO.read(new File(data.getImagePath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics g, int x, int y, int width, int height) 
	{
		for (AbstractVisitor visitor : displayer_)
		{
			visitor.visit(this,g,x,y,width,height);
		}
	}
	
	public boolean needDrawing(Rectangle rect)
	{
		for (int i = 0; i < data_.getModel().npoints - 1; ++i)
		{
			if (rect.intersectsLine(data_.getModel().xpoints[i], data_.getModel().ypoints[i],data_.getModel().xpoints[i+1], data_.getModel().ypoints[i+1]))
				return true;
		}
		return false;
	}

	public Image getImage() {
		return image_;
	}
	
	public int getX() {
		return data_.getBounds(null).x;
	}
	
	public int getY() {
		return data_.getBounds(null).y;
	}

	public boolean isVisible() {
		return data_.isVisible();
	}
}
