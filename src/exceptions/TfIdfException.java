package exceptions;

/*
 * MoteurRechercheException
 * 	CorpusExeption
 * 	TfIDfException <-
 * 	Bm25Exception
 */

public class TfIdfException extends MoteurRechercheException 
{
	public TfIdfException()
	{
		super("Le corpus traité est nul ou vide de documents");
	}
}