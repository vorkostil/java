package server.model.intention;

import java.util.List;

import server.model.Environment;
import server.model.action.IAction;

public interface IIntention
{
	public boolean isValid(Environment environment);
	public List<IAction> createActions();
}