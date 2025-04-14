package exceptions;

/*
 * MoteurRechercheException <-
 * 	CorpusExeption
 * 	TfIDfException
 * 	Bm25Exception
 */

public class MoteurRechercheException extends Exception 
{
	public MoteurRechercheException(String err)
	{
		super("MoteurRechercheException; " + err);
	}
}