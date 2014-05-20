package server.model;

public class ActionMoveOn implements IAction {

	PhysicalObject target_ = null;
	PhysicalObject source_ = null;
	
	public ActionMoveOn(PhysicalObject target_, PhysicalObject source_) {
		super();
		this.target_ = target_;
		this.source_ = source_;
	}

	@Override
	public void run() {
		target_.moveOn(source_);
	}

}
