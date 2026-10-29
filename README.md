# SkyShop

API REST d'une application e-commerce, développée avec **Spring Boot**, **Spring Security (JWT)** et **PostgreSQL**. Le projet sert de terrain d'apprentissage pour construire un back-end complet : authentification, gestion du catalogue, migrations de base de données et documentation d'API.

> **État du projet :** le back-end (authentification, catégories, produits) est fonctionnel. Le panier, les commandes et les paiements sont en cours de développement. Le front-end Angular est initialisé mais pas encore développé.

## Sommaire

- [Technologies](#technologies)
- [Fonctionnalités](#fonctionnalités)
- [Architecture](#architecture)
- [Prérequis](#prérequis)
- [Installation et lancement](#installation-et-lancement)
- [Documentation de l'API (Swagger)](#documentation-de-lapi-swagger)
- [Endpoints](#endpoints)
- [Exemple d'utilisation](#exemple-dutilisation)
- [Modèle de données](#modèle-de-données)
- [Configuration](#configuration)
- [Feuille de route](#feuille-de-route)
- [Auteur](#auteur)

## Technologies

| Domaine | Technologies |
| --- | --- |
| Langage | Java 17 |
| Framework | Spring Boot 4.0.6 (Web MVC, Data JPA, Validation) |
| Sécurité | Spring Security, JWT (jjwt 0.12.7), mots de passe chiffrés |
| Base de données | PostgreSQL 17, migrations Flyway |
| Documentation | springdoc-openapi (Swagger UI) |
| Outils | Maven, Lombok, Docker Compose |
| Front-end (initialisé) | Angular 22, Tailwind CSS |

## Fonctionnalités

**Disponibles**

- Inscription et connexion d'un utilisateur, avec génération d'un token JWT (valable 30 minutes).
- Deux rôles définis : `ROLE_CLIENT` et `ROLE_ADMIN`.
- Gestion des catégories : création, lecture, modification, suppression.
- Gestion des produits : création (envoi `multipart/form-data`), lecture, modification, suppression.
- Service de lecture des fichiers du dossier d'upload (`/uploads/{filename}`), en préparation de la gestion des images de produits.
- Liste paginée des produits avec filtre par nom et par catégorie, tri et direction configurables.
- Accès public en lecture au catalogue (`GET /products`, `GET /categories`), authentification requise pour le reste.
- Réponses JSON homogènes (enveloppe `ApiResponse` avec message, données et pagination).
- Gestion centralisée des erreurs (exceptions métier : ressource introuvable, conflit, accès refusé, identifiants invalides, etc.).
- Schéma de base versionné avec Flyway, validé par Hibernate (`ddl-auto: validate`).
- Documentation interactive de l'API avec Swagger UI.

**En cours**

- Panier et lignes de panier (entités et services créés, endpoints à exposer).
- Commandes et paiements (tables créées, logique à implémenter).
- Interface Angular.

## Architecture

```
skyshop/
├── backend/                     # API Spring Boot
│   ├── src/main/java/com/jordan/skyshoplink/
│   │   ├── controller/          # Contrôleurs REST (produits, catégories)
│   │   ├── security/            # JWT, filtre, configuration Spring Security, authentification
│   │   ├── service/             # Logique métier
│   │   ├── repository/          # Accès aux données (Spring Data JPA)
│   │   ├── entity/              # Entités JPA
│   │   ├── dto/                 # Objets d'entrée et de sortie
│   │   ├── mapper/              # Conversion entités <-> DTO
│   │   ├── specification/       # Filtres dynamiques (recherche de produits)
│   │   ├── exception/           # Exceptions métier et gestion globale des erreurs
│   │   ├── common/              # Réponses API, pagination, stockage des images
│   │   └── config/              # Configuration OpenAPI et CORS
│   └── src/main/resources/
│       ├── application.yaml
│       └── db/migration/        # Migrations Flyway (V1 à V5)
├── frontend/                    # Application Angular (initialisée)
└── docker-compose.yaml          # PostgreSQL (et services applicatifs)
```

L'API suit une architecture en couches : **controller → service → repository**, avec des DTO pour ne jamais exposer directement les entités.

## Prérequis

- Java 17
- Docker et Docker Compose (pour PostgreSQL)
- Node.js et npm (uniquement pour le front-end)

Maven n'est pas à installer : le projet embarque le wrapper `mvnw`.

## Installation et lancement

**1. Cloner le dépôt**

```bash
git clone https://github.com/jordanky-tech/skyshop.git
cd skyshop
```

**2. Démarrer la base de données PostgreSQL**

```bash
docker compose up -d postgres
```

La base `skyshop` est alors disponible sur `localhost:5438` (utilisateur `skyshop`, mot de passe `skyshop`).

**3. Lancer le back-end**

```bash
cd backend
./mvnw spring-boot:run
```

Sous Windows : `mvnw.cmd spring-boot:run`.

Au démarrage, Flyway applique automatiquement les migrations et crée les tables. L'API est accessible sur `http://localhost:8080`.

**4. (Optionnel) Lancer le front-end**

```bash
cd frontend
npm install
npm start
```

Le front-end est disponible sur `http://localhost:4200`. Il n'est pas encore connecté à l'API.

## Documentation de l'API (Swagger)

Une fois le back-end lancé, la documentation interactive est disponible ici :

- Swagger UI : `http://localhost:8080/swagger-ui.html`
- Spécification OpenAPI (JSON) : `http://localhost:8080/v3/api-docs`

Pour tester les routes protégées dans Swagger, connectez-vous d'abord avec `POST /auth/login`, puis cliquez sur **Authorize** et collez le token reçu.

## Endpoints

| Méthode | Route | Description | Accès |
| --- | --- | --- | --- |
| POST | `/auth/register` | Créer un compte | Public |
| POST | `/auth/login` | Se connecter et obtenir un token JWT | Public |
| GET | `/categories` | Lister les catégories | Public |
| GET | `/categories/{id}` | Détail d'une catégorie | Public |
| POST | `/categories` | Créer une catégorie | JWT |
| PUT | `/categories/{id}` | Modifier une catégorie | JWT |
| DELETE | `/categories/{id}` | Supprimer une catégorie | JWT |
| GET | `/products` | Lister les produits | Public |
| GET | `/products/{id}` | Détail d'un produit | Public |
| GET | `/products/paginated` | Produits paginés, filtrés et triés | Public |
| POST | `/products` | Créer un produit (`multipart/form-data`) | JWT |
| PUT | `/products/{id}` | Modifier un produit | JWT |
| DELETE | `/products/{id}` | Supprimer un produit | JWT |
| GET | `/uploads/{filename}` | Récupérer un fichier du dossier d'upload | JWT |

Paramètres de `GET /products/paginated` :

| Paramètre | Défaut | Description |
| --- | --- | --- |
| `name` | aucun | Filtre sur le nom du produit |
| `catgoryId` | aucun | Filtre sur l'identifiant de catégorie |
| `page` | `0` | Numéro de page |
| `size` | `10` | Nombre d'éléments par page |
| `sortby` | `name` | Champ de tri |
| `direction` | `desc` | `asc` ou `desc` |

## Exemple d'utilisation

**Inscription**

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"firstname":"Jordan","lastname":"Kenne","email":"jordan@example.com","password":"MotDePasse123"}'
```

**Connexion**

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"jordan@example.com","password":"MotDePasse123"}'
```

Le token est renvoyé dans le champ `bearer` de la réponse.

**Créer une catégorie (route protégée)**

```bash
curl -X POST http://localhost:8080/categories \
  -H "Authorization: Bearer <votre_token>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Électronique","descritpion":"Téléphones et accessoires"}'
```

**Lister les produits d'une catégorie, page 0**

```bash
curl "http://localhost:8080/products/paginated?catgoryId=<id_categorie>&page=0&size=10&sortby=name&direction=asc"
```

## Modèle de données

Le schéma est créé par les migrations Flyway du dossier `backend/src/main/resources/db/migration` :

| Migration | Contenu |
| --- | --- |
| `V1` | Table `users` |
| `V2` | Tables `categories` et `products` |
| `V3` | Tables `carts` et `cart_items` |
| `V4` | Tables `orders` et `order_items` |
| `V5` | Table `payments` |

Relations principales : un utilisateur possède un panier et des commandes ; une commande contient des lignes de commande liées à des produits et peut avoir des paiements ; un produit appartient à une catégorie.

## Configuration

La configuration se trouve dans `backend/src/main/resources/application.yaml`.

| Paramètre | Valeur par défaut | Description |
| --- | --- | --- |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5438/skyshop` | Connexion à PostgreSQL |
| `app.jwt.access.secret` | variable d'environnement `JWT_SECRET` | Clé de signature des tokens |
| `app.upload.dir` | `./upload` | Dossier de stockage des fichiers téléversés |

> **Sécurité :** définissez toujours votre propre `JWT_SECRET` (longue chaîne aléatoire) et changez les identifiants PostgreSQL avant tout déploiement. Les valeurs par défaut sont réservées au développement local.

```bash
export JWT_SECRET="une_longue_chaine_aleatoire_et_secrete"
```

## Tests

```bash
cd backend
./mvnw test
```

## Feuille de route

- [ ] Endpoints du panier (ajout, modification, suppression d'articles)
- [ ] Création et suivi des commandes
- [ ] Paiements
- [ ] Restriction des routes d'administration au rôle `ROLE_ADMIN`
- [ ] Envoi et affichage des images de produits
- [ ] Interface Angular (catalogue, panier, connexion)
- [ ] Dockerfiles du back-end et du front-end pour un lancement complet avec `docker compose up`
- [ ] Tests unitaires et d'intégration des services

## Auteur

**Jordan Kenne Yemeli** — développeur Java / Spring Boot en formation, Douala, Cameroun.

- GitHub : [jordanky-tech](https://github.com/jordanky-tech)
- LinkedIn : [jordan-kenne](https://www.linkedin.com/in/jordan-kenne-1507883b3)
