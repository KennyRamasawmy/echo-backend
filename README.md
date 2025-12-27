# Leitner System

Application de révision espacée basée sur le système de Leitner.

## Prérequis

- Java 21
- Maven 3.8+

## Architecture

Ce projet utilise l'**architecture hexagonale** (Ports & Adapters) :

```
src/main/java/com/leitner/
├── application/           # Point d'entrée Spring Boot
├── domain/
│   ├── model/            # Entités métier (Card, Category)
│   ├── port/
│   │   ├── in/           # Ports entrants (use cases)
│   │   └── out/          # Ports sortants (repositories)
│   └── service/          # Implémentation des use cases
└── infrastructure/
    ├── adapter/
    │   ├── in/web/       # Contrôleurs REST (adaptateur entrant)
    │   └── out/persistence/  # JPA (adaptateur sortant)
    └── config/           # Configuration Spring
```

## Installation

```bash
mvn clean install
```

## Démarrage de l'application

```bash
mvn spring-boot:run
```

L'application sera disponible sur `http://localhost:8080`

## Exécution des tests

```bash
mvn test
```

## Couverture de tests

```bash
mvn test jacoco:report
```

Le rapport de couverture sera disponible dans `target/site/jacoco/index.html`

## API

L'API respecte le contrat Swagger fourni :

### Endpoints

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/cards` | Récupère toutes les cartes (filtre optionnel par tags) |
| POST | `/cards` | Crée une nouvelle carte |
| GET | `/cards/quizz` | Récupère les cartes du quizz du jour |
| PATCH | `/cards/{cardId}/answer` | Répond à une carte |

### Exemples

**Créer une carte :**
```bash
curl -X POST http://localhost:8080/cards \
  -H "Content-Type: application/json" \
  -d '{"question": "What is TDD?", "answer": "Test Driven Development", "tag": "Development"}'
```

**Récupérer toutes les cartes :**
```bash
curl http://localhost:8080/cards
```

**Récupérer les cartes par tag :**
```bash
curl "http://localhost:8080/cards?tags=Development"
```

**Récupérer le quizz du jour :**
```bash
curl http://localhost:8080/cards/quizz
```

**Répondre à une carte :**
```bash
curl -X PATCH http://localhost:8080/cards/{cardId}/answer \
  -H "Content-Type: application/json" \
  -d '{"isValid": true}'
```

## Base de données

Utilise H2 en mémoire. Console accessible sur `http://localhost:8080/h2-console` :
- JDBC URL: `jdbc:h2:mem:leitnerdb`
- User: `sa`
- Password: (vide)

## Système de Leitner

| Catégorie | Fréquence |
|-----------|-----------|
| FIRST | 1 jour |
| SECOND | 2 jours |
| THIRD | 4 jours |
| FOURTH | 8 jours |
| FIFTH | 16 jours |
| SIXTH | 32 jours |
| SEVENTH | 64 jours |

- Bonne réponse → catégorie suivante
- Mauvaise réponse → retour en catégorie FIRST
- Catégorie SEVENTH réussie → carte considérée comme apprise
