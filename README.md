# Moteur de Recherche – Projet Java (L2 Informatique)

## Description

Ce projet Java a pour but de simuler un moteur de recherche sur un corpus de documents. Il permet d'extraire des caractéristiques textuelles à l’aide des méthodes **Tf-Idf** et **BM25**, d’analyser des documents, de traiter des requêtes textuelles et d’effectuer une recherche en classant les résultats par pertinence.

Deux jeux de données sont supportés :
- `WIKIPEDIA` : corpus contenant des extraits d’articles Wikipedia (séparés par `|||`).
- `OUVRAGE` : corpus d’ouvrages littéraires (fichiers avec tabulations `\t`).

## Organisation du code

Le projet est structuré en plusieurs packages :

### 1. `classesPrinpales`
- `Mot` : encapsule un mot unique.
- `Document` : représente un document (titre + liste de mots).
- `Corpus` : contient un ensemble de `Document` et gère l'extraction des caractéristiques.
- `DataSets` : énumération pour sélectionner le corpus (`WIKIPEDIA`, `OUVRAGE`).

### 2. `recherche`
- `TfIdf` : calcule les scores TF-IDF des mots dans chaque document.
- `Bm25` : implémente l’algorithme de score BM25.
- `Vocabulary` : gère le vocabulaire global (mots, identifiants, stopwords).

### 3. `utile`
- `TailleDocument` : calcule le nombre de documents dans un corpus.
- `TailleMot` : calcule le nombre total de mots.

### 4. `exceptions`
- `CorpusException` : corpus non valide ou fichier introuvable.
- `TfIdfException` : corpus vide ou nul.
- `Bm25Exception` : requête invalide.
- `MoteurRechercheException` : classe mère des exceptions du moteur.

### 5. `main`
- `Test` : point d'entrée principal avec des tests des fonctionnalités principales du moteur.

## Fonctionnalités principales

- Chargement et parsing de corpus.
- Calcul de la **taille en documents** et en **mots**.
- Calcul des scores TF, IDF, et TF-IDF.
- Suppression des **stop words** avec un fichier `stopWords.txt`.
- Traitement de requêtes (scores, classement) selon :
  - Méthode **Tf-Idf**.
  - Méthode **BM25**.

## Utilisation

1. Placer le corpus (ex. `stemmed.txt`) et les fichiers nécessaires (`stopWords.txt`) dans le répertoire du projet.
2. Compiler les fichiers `.java` :
   ```bash
   javac main/Test.java
   ```
3. Exécuter le programme :
   ```bash
   java main.Test
   ```

## Exemple de sortie

```
---------- Partie A ----------
WIKIPEDIA [Document1, Document2, ...]

---------- Partie B ----------
Nombre de documents présents dans le corpus : 10
Nombre de mots présents dans le corpus : 2047

---------- Partie C ----------
-- TFIDF --
-- TF --
mot1 : 0.12
mot2 : 0.08
...
-- Process Query --
Document1 : 0.6743
Document2 : 0.5982
...
```

## Auteur

Yanis Laftimi  
L2 Informatique – Groupe 4  
Université d'Avignon
