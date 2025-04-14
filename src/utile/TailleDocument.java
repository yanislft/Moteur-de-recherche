package utile;
import java.util.Iterator;

import classesPrinpales.*;

public class TailleDocument 
{
	//Retourne le nombre de document que contient le corpus donné en argument 
	public int calculer(Corpus c)
	{
		if (c == null) {return 0;}
		
		return c.size();
	}
}