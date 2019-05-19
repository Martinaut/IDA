# IDA

IDA ist eine dialogbasierte Benutzungsschnittstelle für interaktive Datenanalyse in natürlicher Sprache.

## Installation

### Systemvoraussetzungen

* Java Runtime Environment ab Version 11
* GraphDB

### GraphDB

1. Neues GraphDB-Repository erstellen (z. B. mit Repository ID `ida`)
2. Schema-Daten (Dateien in Ordner `graphdb/data`) in GraphDB importieren
3. WordNet herunterladen von http://wordnet-rdf.princeton.edu/static/wordnet.nt.gz
4. WordNet in Named Graph `http://dke.jku.at/ida/similarity#wordnet` importieren
5. Text Similarity Index mit dem Namen `wordnet` in GraphDB erstellen. Als Abfrage-Query die Abfrage aus Datei `graphdb/similarity-index.sparql` verwenden.
6. Materialisierungs-Abfragen aus Ordner `graphdb/materialization/wordnet-en` ausführen

### Konfiguration

Anwendung kann durch das Festlegen von Einstellungen in der Datei `application.properties` konfiguriert werden. Folgende
Einstellungen können festgelegt werden:

* __graphdb.remote.server-url__: URL zum GraphDB-Server (Bsp.: `http://localhost:7200/`)
* __graphdb.remote.repository-id__: ID des GraphDB-Repositories (Bsp.: `ida`)

Folgende Einstellungen sind optional:

* __scxml.query-endpoint__: Endpoint des Abfrage-Servers (Standard: `http://ida-mock-executor` (Mock-Server))
* __csp.use-levels__: Levels im Constraint-Satisfaction Prozess zur Befüllung der Analysesituation miteinbeziehen (Standard: `true`)
* __csp.use-level-predicates__: Level Predicates im Constraint-Satisfaction Prozess zur Befüllung der Analysesituation miteinbeziehen (Standard: `true`)
* __csp.use-base-measure-predicates__: Base Measure Predicates im Constraint-Satisfaction Prozess zur Befüllung der Analysesituation miteinbeziehen (Standard: `false`)
* __csp.use-aggregate-measure-predicates__: Levels im Constraint-Satisfaction Prozess zur Befüllung der Analysesituation miteinbeziehen (Standard: `false`)

## Ordnerstruktur

* `app`: Main-Klasse und Regeln, sowie NLP
* `engine`: Core-Funktionalitäten
* `frontend`: Angular-App
* `graphdb`: Scripts zum Erstellen des Similarity Index