package recherche;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import exceptions.Bm25Exception;
import utile.TailleMot;

import java.util.Map.Entry;

import classesPrinpales.Corpus;
import classesPrinpales.Document;
import classesPrinpales.Mot;

public class Bm25 
{
	HashMap<Document, double[]> tf;
	double[] idf;
	private double k1;
	private double b;
	private double avgDl;
	
	public Bm25()
	{
		tf = new HashMap<Document,double[]>(); 
		idf = new double[0];
		k1 = 1.2;
		b = 0.75;
		avgDl = 0;
	}
	
	public void vocabulaire(Corpus c)
	{
		Vocabulary v = Vocabulary.getInstance();
		
		v.ajoutCorpus(c);
	}
	
	public Bm25 processCorpus(Corpus c)
	{
		vocabulaire(c);
		Vocabulary v = Vocabulary.getInstance();	
		
		Iterator<Document> it = c.iterator();
		
		int[] countMotDoc = new int[v.countHm()];
		
		//Calcul de la longueur moyenne des documents (avgDl)
		double longDoc = 0;
		
		while (it.hasNext())
		{
			Document d = it.next();
			longDoc += d.size();
			double[] tfTab = new double[v.countHm()];
			
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
						countMotDoc[idM] ++;
						motVuDoc.add(idM);
					}
				}
			}
			
			System.out.println("\n" + d.titre + "\n");
			//Calcul de la fréquence d'un mot par document 
			for (int i = 0; i < tfTab.length; i++)
			{
				//Affichage résultat tf brut 
				System.out.println(v.getMotID(i) + " : " + tfTab[i]);
			}
			
			tf.put(d, tfTab);
		}
		
		//Calcul + ajout du Idf de chaque mots
		idf = new double[v.countHm()];
		avgDl = longDoc / c.size();
		
		System.out.println("\n-- IDF --\n");
		
		for (int i = 0; i < idf.length; i++)
		{
			//idf = log[(Nombre de documents dans le corpus - Nombre de document ou le mot apparait + 0.5) / (Nombre de document ou le mot apparait + 0.5) + 1]
			if (countMotDoc[i] > 0) {idf[i] = Math.log10((c.size() - countMotDoc[i] + 0.5) / (countMotDoc[i] + 0.5) + 1);}
			else {idf[i] = 0;}
			
			//Affichage resultat idf de chaque mot 
			System.out.println(v.getMotID(i) + " : " + idf[i]);
		}
		
		return this;
	}
	
	//Même méthode que la précédente avec l'ajout du traitement de vocabulaire réduit 
	public Bm25 processCorpusV2(Corpus c)
	{
		Vocabulary v = Vocabulary.getInstance();
		v.ajoutStopwords("stopWords.txt");
		HashSet<Mot> sw = v.getSW();
		int nbMT = 0;
		Iterator<Document> it = c.iterator();
		
		int[] countMotDoc = new int[v.countHm()+1];
		
		double longDoc = 0;
		
		while (it.hasNext())
		{
			Document d = it.next();
			longDoc += d.size();
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
			
			tf.put(d, tfTab);
		}
		
		idf = new double[v.countHm()];
		avgDl = longDoc / c.size();
		
		for (int i = 0; i < idf.length; i++)
		{
			//(Nombre de doc dans le corus) - (Nb de document ou le mot apparait) / (Nb de document ou le mot apparait)
			if (countMotDoc[i] > 0) {idf[i] = Math.log10((c.size() - countMotDoc[i] + 0.5) / (countMotDoc[i] + 0.5) + 1);}
			else {idf[i] = 0;}
			
			System.out.println(v.getMotID(i) + " : " + idf[i]);
			
		}
		System.out.println("\nNombre de mots présents dans le corpus : " + new TailleMot().calculer(c) + "\n");
		System.out.println("\nNombre de mots traités (sans Stop Word) : " + nbMT);
		return this;
	}
	
	public void processQuery(String requete, int max) throws Bm25Exception
	{
		if (requete == null || requete.isEmpty()) {throw new Bm25Exception();}
		
		Vector<Double> vReq = features(requete);
		
		HashMap<Document, Double> evReq = evaluate(vReq);
		
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
			
			if (idM != null)
			{
				tfRequete.set(idM, tfRequete.get(idM)+1);
			}
		}
		
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
			 
			//score = somme [(idf(i) * tf(i) * (k1+1)) / (idf(i) + k1 * (1-b+b* (longuer du document / moyenne longueur documents))]  
			double score = 0;
			
			for (int i = 0; i < requete.size(); i++)
			{
				//On calcule le score si le mot est present dans la requete et dans le doc 
				if (requete.get(i) > 0 && dTf[i] > 0)
				{
					score += idf[i] * ((dTf[i] * (k1 + 1)) / (dTf[i] + k1 * (1 - b + b * (d.size() / avgDl))));
				}
			}
			
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