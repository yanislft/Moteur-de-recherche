package recherche;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import classesPrinpales.*;

import java.util.Set;
import java.util.Vector;
import exceptions.TfIdfException;
import utile.TailleMot;

public class TfIdf 
{
	HashMap<Document, double[]> tf; //Fréquence d'un mot dans un document 
	double[] idf; //Fréquence de document contenant un mot 
	
	public TfIdf()
	{
		tf = new HashMap<Document,double[]>(); 
		idf = new double[0]; 
	}
	
	public void vocabulaire(Corpus c)
	{
		Vocabulary v = Vocabulary.getInstance();
		
		v.ajoutCorpus(c);
	}
	
	public TfIdf processCorpus(Corpus c) throws TfIdfException
	{
		if(c == null || c.size() == 0) {throw new TfIdfException();}
		
		vocabulaire(c);
		Vocabulary v = Vocabulary.getInstance();
		
		//Permet de compter dans combien de documents chaque mots du vocabulaire apparaît (Idf)
		int[] countMotDoc = new int[v.countHm()+1];
		
		Iterator<Document> it = c.iterator();
		
		while (it.hasNext())
		{
			Document d = it.next();
			
			//Permet de compter le nombre de fois qu'est present un mot/document (Tf)
			double[] tfTab = new double[v.countHm()];
			
			//Permet de stocker les mots qu'on vu dans un document pour ne pas les compter +1 fois/document
			HashSet<Integer> motVuDoc = new HashSet<Integer>();
			
			Iterator<Mot> itM = d.iterator();
			
			while (itM.hasNext())
			{
				Mot m = itM.next();
				Integer idM = v.getIdMot(m);
				
				if (idM != null)
				{					
					tfTab[idM]++;
						
					if (! motVuDoc.contains(idM))
					{				
						countMotDoc[idM]++;
						motVuDoc.add(idM);
					}
				}
			}
			
			System.out.println("\n" + d.titre + "\n");
			//Calcul de la fréquence d'un mot par document 
			for (int i = 0; i < tfTab.length; i++)
			{
				
				tfTab[i] = tfTab[i] / d.size();

				//Affichage résultat tf
				System.out.println(v.getMotID(i) + " : " + tfTab[i]);
			}
			
			//Ajout de résultat dans la HashMap
			tf.put(d, tfTab);
		}
		
		//Calcul et ajout du Idf de chaque mot 
		idf = new double[v.countHm()];
		
		System.out.println("\n-- IDF --\n");
		
		for (int i = 0; i < idf.length; i++)
		{
			//log[(Nombre de document dans le corus) / (Nombre de document ou le mot apparait)]
			if (countMotDoc[i] > 0) {idf[i] = Math.log10((c.size() / countMotDoc[i]));}
			else {idf[i] = 0;}
			
			//Affichage resultat idf de chaque mot 
			System.out.println(v.getMotID(i) + " : " + idf[i]);
		}
		
		System.out.println("\n-- TF-IDF --\n");
		
		//Calcul + affichage TFIDF
		HashMap<Document, double[]> tfidf = new HashMap<Document, double[]>();
		List<Entry<Document, double[]>> tf2 = new ArrayList<>(tf.entrySet());
		
		for (int i = 0; i < tf2.size(); i++)
		{
			Document d = tf2.get(i).getKey();
			double[] tfTab = tf2.get(i).getValue();
			double[] tfidfTab = new double[tfTab.length];
			
			System.out.println("\n" + d.titre + "\n");
			
			for (int j = 0; j < tfTab.length; j++)
			{
				tfidfTab[j] = tfTab[j] * idf[j];
				
				System.out.println(v.getMotID(j).toString() + " : " + tfidfTab[j]);
			}
			
			tfidf.put(d, tfidfTab);
		}
			
		return this;
	}
	
	//Même méthode que la précédente avec l'ajout du traitement de vocabulaire réduit 
	public TfIdf processCorpusV2(Corpus c)
	{	
		vocabulaire(c);
		Vocabulary v = Vocabulary.getInstance();
		v.ajoutStopwords("stopWords.txt");
		HashSet<Mot> sw = v.getSW();
		int nbMT = 0;
		
		Iterator<Document> it = c.iterator();
		
		int[] countMotDoc = new int[v.countHm()+1];
		
		while (it.hasNext())
		{
			Document d = it.next();
			double[] tfTab = new double[v.countHm()];
			
			HashSet<Integer> motVuDoc = new HashSet<Integer>();
			
			Iterator<Mot> itM = d.iterator();
			
			while (itM.hasNext())
			{
				Mot m = itM.next();
				Integer idM = v.getIdMot(m);
				
				//Traitement du mot uniquement s'il ne fait pas parti des Stop Words
				if (!sw.contains(m))
				{
					nbMT++;
					
					if (idM != null)
					{ 
						tfTab[idM]++;
							
						if (! motVuDoc.contains(idM))
						{				
							countMotDoc[idM] ++;
							motVuDoc.add(idM);
						}
					}
				}	
			}
			
			System.out.println("\n" + d.titre + "\n");
			
			for (int i = 0; i < tfTab.length; i++)
			{
					tfTab[i] = tfTab[i] / d.size();
					System.out.println(v.getMotID(i) + " : " + tfTab[i]);
			}
			
			tf.put(d, tfTab);
		}
		
		idf = new double[v.countHm()];
		
		for (int i = 0; i < idf.length; i++)
		{
			if (countMotDoc[i] > 0) {idf[i] = Math.log10((c.size() / countMotDoc[i]));}
			else {idf[i] = 0;}
			System.out.println(v.getMotID(i) + " : " + idf[i]);
		}
		
		System.out.println("\nNombre de mots présents dans le corpus : " + new TailleMot().calculer(c) + "\n");
		System.out.println("\nNombre de mots traités (sans Stop Word) : " + nbMT);
		
		return this;
	}
	
	
	public void processQuery(String requete, int max)
	{
		//Extrait les mots de la requête et calcule leurs Tf
		Vector<Double> vReq = features(requete);
		
		//Évaluation des scores
		HashMap<Document, Double> evReq = evaluate(vReq);
		
		//Tri + affichage des résulatats 
		List<Map.Entry<Document, Double>> listReq = new ArrayList<>(evReq.entrySet());
		
		triRapide(listReq, 0, listReq.size()-1);
		
		for (int i = 0; i < max; i++)
		{
			System.out.println(listReq.get(i).getKey().titre + " : " + listReq.get(i).getValue());
		}
	}
	
	private Vector<Double> features(String requete)
	{
		Vocabulary v = Vocabulary.getInstance();
		
		Vector<Double> tfRequete = new Vector<>(v.countHm());
		
		for (int i = 0; i < v.countHm(); i++)
		{
			tfRequete.add(0.0);
		}
		
		String[] motsRequete = requete.split(" ");
		
		for (int i = 0; i < motsRequete.length; i++)
		{
			Mot m = new Mot(motsRequete[i]);
			Integer idM = v.getIdMot(m);
			
			//Si le mot de la requête est présent dans le vocabulaire on l'incrémente
			if (idM != null)
			{
				tfRequete.set(idM, tfRequete.get(idM)+1);
			}
		}
		
		//Calcul de la fréquence des mots de la requête
		for (int i = 0; i < tfRequete.size(); i++)
		{
			tfRequete.set(i, tfRequete.get(i) / motsRequete.length);
		}
		
		return tfRequete;
	}
	
	private HashMap<Document, Double> evaluate(Vector<Double> requete)
	{
		
		HashMap<Document, Double> scoresDocuments = new HashMap<>();
		
		Iterator<Map.Entry<Document, double[]>> it = tf.entrySet().iterator();
		
		while (it.hasNext())
		{
			Entry<Document, double[]> it2 = it.next();
			Document d = it2.getKey();
			double[] dTf = it2.getValue();
			
			double prodScal = 0, normeReq = 0, normeDoc = 0;
			
			for (int i = 0; i < requete.size(); i++)
			{
				prodScal += requete.get(i) * dTf[i];
				normeReq += requete.get(i) * requete.get(i);
				normeDoc += dTf[i] * dTf[i];				
			}
			
			//Calcul de la longueur des tf requête et document 
			normeReq = Math.sqrt(normeReq);
			normeDoc = Math.sqrt(normeDoc);
			
			double score = prodScal / (normeReq * normeDoc);
			
			scoresDocuments.put(d, score);
		}
		
		return scoresDocuments;
	}
	
	private void triRapide(List<Map.Entry<Document, Double>> l, int a, int b)
	{
		if (a < b)
		{
			int m = 0;
			
			m =partitionnement(l, a, b, m);
			triRapide(l, a, m-1);
			triRapide(l, m+1, b);
		}
	}
	
	private int partitionnement(List<Map.Entry<Document, Double>> l,int a, int b, int m)
	{
		int i = a + 1, j = b;
		
		while (i <= j)
		{
			while (i <= b && l.get(i).getValue() > l.get(a).getValue())
			{
				i++;
			}
			
			while (j > a && l.get(j).getValue() <= l.get(a).getValue())
			{
				j--;
			}
			
			if (i < j)
			{
				Entry<Document, Double> tmp = l.get(i);
				l.set(i, l.get(j));
				l.set(j, tmp);
				i++;
				j--;
			}
		}
		
		Entry<Document, Double> tmp = l.get(a);
		l.set(a, l.get(j));
		l.set(j, tmp);
		
		return j;
	}
}