package server.model.object;

import java.awt.Polygon;

import server.model.Environment;


public class TriggerModifyPhysicalAttributesWithPicture extends TriggerModifyPhysicalAttributes {

	private String imagePath_ = null;
	
	public TriggerModifyPhysicalAttributesWithPicture(String name, boolean visible, boolean crossable, boolean active, String imagePath) {
		super(name, visible, crossable, active);
		imagePath_ = imagePath;
	}

	public TriggerModifyPhysicalAttributesWithPicture(String line, Environment environment) {
/*
 * 		TriggerModifyPhysicalAttributesWithPicture triggerOpen1 = new TriggerModifyPhysicalAttributesWithPicture("triggerOpen1", true, true, true,"pictures/maze1/item_trigger_open.jpg");
		Polygon ptp1 = new Polygon();
		ptp1.addPoint(214,90);
		ptp1.addPoint(293,90);
		ptp1.addPoint(293,109);
		ptp1.addPoint(214,109);
		triggerOpen1.setModel(ptp1);
		triggerOpen1.setTarget(environment_.getPhysicalObject("item1"));
		triggerOpen1.addModification(Attributes.aVISIBLE, false);
		triggerOpen1.addModification(Attributes.aACTIVE, false);
		environment_.addPhysicalObject(triggerOpen1);
*/
		String values[] = line.split(";");
		name_ = values[0];
		setVisibility(Boolean.parseBoolean(values[1]));
		setCrossable(Boolean.parseBoolean(values[2]));
		setActive(Boolean.parseBoolean(values[3]));
		setPolicy(values[4]);
		imagePath_ = values[5];
		
		Polygon model = new Polygon();
		int sizeModel = Integer.parseInt(values[6]);
		for (int i = 0; i < sizeModel; ++i)
			model.addPoint(Integer.parseInt(values[i+7].split(",")[0]),Integer.parseInt(values[i+7].split(",")[1]));
		setModel(model);
		
		setTarget(environment.getPhysicalObject(values[7 + sizeModel]));
		int sizeModification = Integer.parseInt(values[8 + sizeModel]);
		for (int i = 0; i < sizeModification; ++i) {
			addModification(Attributes.valueOf(values[i + 9 + sizeModel].split(",")[0]), Boolean.parseBoolean(values[i + 9 + sizeModel].split(",")[1]));
		}
		
	}

	public String getImagePath() {
		return imagePath_;
	}
}
