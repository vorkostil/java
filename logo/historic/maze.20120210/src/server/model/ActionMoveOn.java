package server.model;

public class ActionMoveOn implements IAction {

	IReactiveObject target_ = null;
	PhysicalObject source_ = null;
	
	public ActionMoveOn(IReactiveObject target_, PhysicalObject source_) {
		super();
		this.target_ = target_;
		this.source_ = source_;
	}

	public PhysicalObject getSource() {
		return source_;
	}
	
	@Override
	public void run() {
		target_.applyAction(this);
	}

}
