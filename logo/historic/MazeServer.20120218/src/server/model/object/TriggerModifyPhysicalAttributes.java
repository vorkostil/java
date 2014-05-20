package server.model.object;

import java.util.ArrayList;
import java.util.List;

import server.model.action.ActionMoveOn;
import server.model.action.IAction;

public class TriggerModifyPhysicalAttributes extends Trigger {

	public enum Policy { pFOREVER, pONCE }

	public class Modification {
		PhysicalObject.Attributes attribute_;
		
		boolean newBooleanValue_ = false;
		
		public Modification(PhysicalObject.Attributes attribute, boolean newValue) {
			attribute_ = attribute;
			newBooleanValue_ = newValue;
		}
		
		public boolean getBooleanValue() {
			return newBooleanValue_;
		}
	}

	PhysicalObject target_ = null;
	List<Modification> modifications_ = new ArrayList<Modification>();
	Policy policy_ = Policy.pFOREVER;
	
	public TriggerModifyPhysicalAttributes(String name, boolean visible, boolean crossable, boolean active) {
		super(name, visible, crossable, active);
	}
	
	public void setTarget(PhysicalObject target) {
		target_ = target;
	}
	
	public void addModification(PhysicalObject.Attributes attribute, boolean newValue) {
		modifications_.add(new Modification(attribute, newValue));
	}

	@Override
	public boolean applyAction(IAction action) {
		if (action instanceof ActionMoveOn) {
			moveOn_(((ActionMoveOn)action).getSource());
			return true;
		}
		return false;
	}

	private void moveOn_(PhysicalObject source_) {
		for (Modification modif : modifications_) {
			switch (modif.attribute_) {
			case aACTIVE:
				target_.setActive(modif.getBooleanValue());
				break;
			case aCROSSABLE:
				target_.setCrossable(modif.getBooleanValue());
				break;
			case aVISIBLE:
				target_.setVisibility(modif.getBooleanValue());
				break;
			}
		}
		if (policy_ == Policy.pONCE) {
			setActive(false);
		}
	}

	public void setPolicy(Policy policy) {
		policy_ = policy;
	}

}
