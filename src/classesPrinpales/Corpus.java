package classesPrinpales;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

import exceptions.CorpusException;
import exceptions.TfIdfException;
import recherche.*;
import utile.*;

public class Corpus extends Vector<Document>
{
	String titre;
	private Object typeRech;
	
	public Corpus()
	{
		titre = null;
		typeRech = null;
	}
	
	//Constructeur permettant de charger tous les documents d'un fichier (Wikipedia ou Ouvrages) dans le corpus
	public Corpus(String cheminFichier, DataSets ds) throws CorpusException, IOException
	{
		titre = ds.name();
		
		//Récupère le chemin du fichier 
		Path file = Paths.get(cheminFichier);
		

		if (!Files.exists(file) ||Files.size(file) == 0) {throw new CorpusException(cheminFichier);}
		
		
		if(ds == DataSets.WIKIPEDIA)
		{
			try 
			{
				//Ouverture du fichier 
				BufferedReader br = Files.newBufferedReader(file);
				
				String line;
				
				//stemmed.txt max 4158 lignes 
				int z = 1, n = 10;
				
				while ((line = br.readLine()) != null && z <= n)
				{ 
					String col[] = line.split("\\|\\|\\|");
					 
					Document d = new Document(col[0]);
					String[] mot = col[1].split(" ");
					 
					//Ajout de tous les mots dans le document 
					for (int i = 1; i < mot.length; i++)
					{
						d.putMot(mot[i]);
					}
					
					//Ajoute le document au vector 
					add(d);
					z++;
				} 
				br.close();
			}
			
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Même procédé que le précédent le seul changement est la récupération des informations 
		else if (ds == DataSets.OUVRAGE)
		{
			Path fileOuvr = Paths.get(cheminFichier);
			
			BufferedReader bfOuvr;
			
			try
			{
				bfOuvr = Files.newBufferedReader(fileOuvr);
				
				String line;
			
				while ((line = bfOuvr.readLine()) != null)
				{
					String col[] = line.split("\t");					 
					 
					Document d = new Document(col[2]);
					
					String[] mot = col[6].split(" ");
					
					for (int i = 1; i < mot.length; i++)
					{
						d.putMot(mot[i]);
					}
					
					add(d);
				}
				bfOuvr.close();
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	//Ajout d'un document dans le vector
	public void addDocument(Document d)
	{
		add(d);
	}
	
	//Affichage du titre du corpus le titre de chaque document et tout les mots qu'ils contiennent 
	public String toString()
	{
		return titre + " " + super.toString();
	}
	
	//Si le paramètre est vrai renvoie la en nombre de document que contient le corpus, sinon renvoie le nombre de mot qu'il contient
	public int taille(boolean b)
	{
		if (b) 
		{
			TailleDocument td = new TailleDocument();
			return td.calculer(this);
		}
		else 
		{
			TailleMot tm = new TailleMot();
			return tm.calculer(this);
		}
	}
	
	//Renvoie la methode processCorpus selon l'objet entré en paramètre
	public Object getFeatures(Object o) throws TfIdfException
	{
		TfIdf a = new TfIdf();
		Bm25 b = new Bm25();
		
		//Vérification de la classe à traiter 
		if (o.getClass() == a.getClass() || o.getClass() == b.getClass())
		{
			if (o.getClass() == a.getClass())
			{
				a = (TfIdf) o;
				a.processCorpus(this);
				typeRech = a;
			}
			
			else 
			{
				b = (Bm25) o;
				b.processCorpus(this);
				typeRech = b;
			}
		}
		
		else {typeRech = null;}
		
		return typeRech;
	}
	
	//Renvoie la methode processCorpus selon l'objet entré en paramètre en traitant un vocabulaire réduit (SW)
	public Object getFeaturesV2(Object o)
	{
		TfIdf a = new TfIdf();
		Bm25 b = new Bm25();
		
		if (o.getClass() == a.getClass() || o.getClass() == b.getClass())
		{
			if (o.getClass() == a.getClass())
			{
				a = (TfIdf) o;
				a.processCorpusV2(this);
				typeRech = a;
			}
			
			else 
			{
				b = (Bm25) o;
				b.processCorpusV2(this);
				typeRech = b;
			}
		}
		
		else {typeRech = b;}
		
		return typeRech;
	}
}