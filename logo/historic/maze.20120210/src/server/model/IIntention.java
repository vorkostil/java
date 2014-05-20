package server.model;

import java.util.List;

interface IIntention
{
	public boolean isValid(Environment environment);
	public List<IAction> createActions();
}