package utile;
import java.util.Iterator;

import classesPrinpales.*;

public class TailleMot 
{
	//Retourne le nombre de mot que contient le corpus donné en argument
	public int calculer(Corpus c)
	{
		Iterator<Document> it = c.iterator();
		int i = 0;
		
		while (it.hasNext())
		{
			Document d = it.next();
			i = i + d.size();
		}
		
		return i;
	}
}