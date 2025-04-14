package classesPrinpales;

import java.util.Objects;

public class Mot 
{
	String contenu;
	
	public Mot(String x)
	{
		contenu = x;
	}
	
	public String toString()
	{
		return contenu;
	}
	
	//Permet de comparer 2 Mots 
    @Override
    public boolean equals(Object o) 
    {
        if (this == o) {return true;}
        
        if (o == null || getClass() != o.getClass()) {return false;}
        
        Mot mot = (Mot) o;
        
        return Objects.equals(contenu, mot.contenu);
    }
    
    //Permet de générer un code de hashage du cotenu du mot 
    @Override
    public int hashCode() 
    {
        return Objects.hash(contenu);
    }
}