package classesPrinpales;

import java.util.ArrayList;

public class Document extends ArrayList<Mot>
{
	public String titre;
	
	public Document() 
	{
		titre = null;
	}
	
	public Document(String t)
	{
		titre = t;
	}
	
	//Ajout d'un mot dans la ArrayList
	public void putMot(String m)
	{
		Mot x = new Mot(m);
		add(x);
	}
	
	//Affichage du titre duocument et tout les mots qu'il contient
	public String toString()
	{
		return titre + super.toString();
	}
}