package exceptions;

/*
 * MoteurRechercheException
 * 	CorpusExeption <-
 * 	TfIDfException
 * 	Bm25Exception
 */

public class CorpusException extends MoteurRechercheException
{
	public CorpusException(String f)
	{
		super("CorpusExcepetion : Le fichier '" + f + "' contenant les documents est nul.");
	}
}