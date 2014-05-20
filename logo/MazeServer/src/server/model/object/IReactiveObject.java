package server.model.object;

import server.model.action.IAction;

public interface IReactiveObject
{
	public boolean applyAction(IAction action);
}