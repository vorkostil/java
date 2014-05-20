package server.model.object;

import java.util.List;

import server.model.intention.IIntention;

interface ICognitiveObject
{
	public List<IIntention> process(long time);
}