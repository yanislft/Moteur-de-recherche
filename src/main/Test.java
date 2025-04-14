package main;
import java.io.IOException;

import classesPrinpales.*;
import exceptions.Bm25Exception;
import exceptions.CorpusException;
import exceptions.TfIdfException;
import recherche.*;
import utile.TailleDocument;
import utile.TailleMot;

public class Test 
{
	public static void main (String[] arg) throws CorpusException, IOException, TfIdfException, Bm25Exception
	{
		//Partie A : Créer un corpus et afficher les détails avec la méthode toString
		System.out.println("\n---------- Partie A ----------\n");
		
		//booksummaries
		Corpus c = new Corpus("stemmed.txt", DataSets.WIKIPEDIA);
		System.out.println(c.toString());
		
		//Partie B : Test des méthodes tailleDocument et tailleMot
		System.out.println("\n---------- Partie B ----------\n");
		
		System.out.println("\nNombre de documents présents dans le corpus : " + new TailleDocument().calculer(c));
		System.out.println("Nombre de mots présents dans le corpus : " + new TailleMot().calculer(c) + "\n");
		
		//Partie C : TfIdf et Bm25
		System.out.println("\n---------- Partie C ----------\n");
		
		//TfIdf
		System.out.println("---- TFIDF ----\n");
		System.out.println("\n-- TF --\n");
		TfIdf t = new TfIdf();
		t.processCorpus(c);
		System.out.println("\n-- Process Query --\n");
		t.processQuery("world", 5);
		/*
		//BM25
		System.out.println("\n---- BM25 ----\n");
		System.out.println("\n-- TF --\n");
		Bm25 b = new Bm25();
		b.processCorpus(c);
		System.out.println("\n-- Process Query --\n");
		b.processQuery("world", 5);
		
		//Partie D : Diminution du vocabulaire
		System.out.println("\n---------- Partie D ----------\n");
		
		//TfIdf
		System.out.println("---- TFIDF ----\n");
		System.out.println("\n-- TF --\n");
		t.processCorpusV2(c);
		
		//BM25
		System.out.println("\n---- BM25 ----\n");
		System.out.println("\n-- TF --\n");
		b.processCorpusV2(c);	*/
	}
}