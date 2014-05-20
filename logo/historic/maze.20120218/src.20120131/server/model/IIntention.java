package server.model;

interface IIntention
{
	public boolean isValid(Environment environment);
	public IAction createAction();
}