package server.model;

import java.util.List;

import server.model.IIntention;

interface ICognitiveObject
{
	public List<IIntention> process(long time);
}