# Query-evaluation


### Architecture

```
p1716451_p1712760_query-evaluation
├── doc
│    ├── rapport.pdf
│    └── diagramme_classe.png
├── query-evaluation
│    ├── pom.xml
│    ├── src
│        ├── main
│        └── test
│    ├── lib
│        └── ojdbc6.jar
├── README.md

```


### Exécution

- Exécution du programme : `cd query-evaluation && mvn compile && mvn exec:java`
- Exécution de la série de Test JUnit 4 : `cd query-evaluation && mvn test`
- Par défaut, l'exemple donné dans le sujet est exécuté.
- Pour exécuter une autre requête, il suffit de modifier le chemin vers l'exemple à exécuter dans `query-evaluation/src/main/java/fr/univlyon1/mif37/dex/App.java`. Pour exécuter l'exemple 1 : `"/qsqrEngine/exemple1.txt"`.
