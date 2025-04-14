package exceptions;

/*
 * MoteurRechercheException
 * 	CorpusExeption
 * 	TfIDfException
 * 	Bm25Exception <-
 */

public class Bm25Exception extends MoteurRechercheException 
{

	public Bm25Exception() 
	{
		super("La requête est nulle ou vide.");
	}

}