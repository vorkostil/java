package server.model;

import server.model.IAction;

interface IReactiveObject
{
	public boolean applyAction(IAction action);
}