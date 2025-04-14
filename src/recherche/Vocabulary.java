package recherche;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import classesPrinpales.*;

public class Vocabulary 
{
	private HashMap<Mot, Integer> hm;
	private HashMap<Integer, Mot> hm2;
	private HashSet<Mot> hs;
	
	//Seule instance de la classe
	private static Vocabulary vocab = null;
	
	private Vocabulary()
	{
		hm = new HashMap<Mot, Integer>();
		hm2 = new HashMap<Integer, Mot>();
		hs = new HashSet<Mot>();
	}
	
	//Ajoute tous les mots (de manière unique) contenus dans le corpus donné en paramètre 
	public void ajoutCorpus(Corpus c)
	{		
		//Permet de parcourir les documents du corpus 
		Iterator<Document> itDoc = c.iterator();
		Integer id = 0;
		
		while (itDoc.hasNext())
		{
			Document d = itDoc.next();
			
			//Permet de parcourir les mots dess documents
			Iterator<Mot> itMot = d.iterator();
			
			while (itMot.hasNext())
			{
				Mot m = itMot.next();
			
				//Si le mot n'est pas déjà présent dans le vocabulaire on l'ajoute, sinon on ne fait rien
				if (! hm.containsKey(m))
				{
					vocab.hm.put(m,id);
					vocab.hm2.put(id, m);
					id++;	
				}
			}
		}
	}
	
	//Ajout des StopWords dans la HashSet 
	public void ajoutStopwords(String nomFichier)
	{
		Path file = Paths.get(nomFichier);
		
		try 
		{
			BufferedReader br = Files.newBufferedReader(file);
			
			String line;
			
			while ((line = br.readLine()) != null)
			{
				Mot m = new Mot(line);
				vocab.hs.add(m);
			}
			
			br.close();
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Methode qui retourne la seule instance de Vocabulary 
	public static Vocabulary getInstance()
	{
		if (vocab == null)
		{
			vocab = new Vocabulary();
		}
		return vocab;
	}	
	
	//Méthode qui retourne le nombre de mot que contient le vocabulaire 
	public int countHm()
	{
		return vocab.hm.size();
	}
	
	//Méthode qui retourne le nombre de mot "Stop Words" que contient la classe 
	public int countHs()
	{
		return vocab.hs.size();
	}
	
	//Méthode qui retourne l'id qu'a un mot dans la HashMap
	public Integer getIdMot(Mot m)
	{		
		return hm.get(m);
	}
	
	//Méthode qui retourne un mot de la hashMap selon son id 
	public String getMotID(int id)
	{
		return hm2.get(id).toString();
	}
	
	//Méthode qui retourne la HashSet contenant les StopWords
	public HashSet<Mot> getSW()
	{
		return hs;
	}
}