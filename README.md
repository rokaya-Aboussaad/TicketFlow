# TicketFlow — microservices Spring Boot

Java 21 · Spring Boot 3.5.7 · Spring Cloud 2025.0.1 · Eureka · Config Server · Gateway · OpenFeign · Kafka · MySQL 8 (une base par service).

## Démarrage local
Prérequis : JDK 21, Docker Desktop (démarré), connexion internet (premier build).

```powershell
# 1) Compiler et lancer les tests (depuis TicketFlow/)
.\mvnw.cmd clean verify

# 2) Lancer toute la stack (MySQL, Kafka, Eureka, Config Server, 6 services, Gateway)
docker compose -f infra/docker-compose.yml up --build -d

# 3) Suivre l'état / les logs
docker compose -f infra/docker-compose.yml ps
docker compose -f infra/docker-compose.yml logs -f booking-service
```

Le premier build est long (téléchargement des dépendances Maven). Compter 1 à 2 minutes après `up` pour que tous les services s'enregistrent dans Eureka.

| Composant | Adresse |
|---|---|
| API Gateway | http://localhost:8080 |
| Eureka | http://localhost:8761 |
| Config Server | http://localhost:8888/customer-service/default |
| Adminer (interface MySQL) | http://localhost:8090 (serveur `mysql`, utilisateur `ticketflow`, mot de passe `ticketflow`) |
| MySQL (depuis la machine) | `localhost:3310` (utilisateur `ticketflow` / `ticketflow`, root / `root`) |

Arrêter : `docker compose -f infra/docker-compose.yml down` (ajouter `-v` pour aussi supprimer les données MySQL).

## Base de données
MySQL 8 avec une base par microservice (`customer_db`, `event_db`, `booking_db`, `payment_db`, `ticket_db`), créées par `infra/mysql/init.sql`. Les tables sont générées par Hibernate (`ddl-auto: update`). Chaque service lit `DB_HOST`, `DB_PORT`, `DB_USER`, `DB_PASSWORD` (valeurs par défaut : `localhost`, `3310`, `ticketflow`, `ticketflow`). Les tests de `customer-service` utilisent H2 en mémoire et n'ont pas besoin de MySQL.

## Parcours API de démonstration
1. Créer un événement : `POST http://localhost:8080/api/events`
```json
{"title":"Concert TicketFlow","description":"Démo","venue":"Casablanca","startsAt":"2027-05-01T19:00:00","endsAt":"2027-05-01T22:00:00","capacity":100,"price":150.00}
```
2. Créer une réservation : `POST /api/bookings` avec header `Idempotency-Key: demo-001` et body `{"customerId":1,"eventId":1,"quantity":2}`.
3. Simuler le paiement : `POST /api/payments` body `{"bookingId":1,"simulateSuccess":true}`.
4. Booking consomme `payment-events`, confirme la réservation et publie `booking-confirmed`.
5. Ticket Service émet un billet unique par place; Notification Service écrit une notification de démonstration dans ses logs.

## Endpoints
- Customer : `/api/customers`
- Event : `/api/events`
- Booking : `/api/bookings`
- Payment : `/api/payments`
- Ticket : `/api/tickets`
- Notification health : `/api/notifications/health`

## Limites connues avant production
- Paiement simulé uniquement; aucun prestataire ni webhook signé.
- Notifications en adaptateur console; pas encore de SMTP/SMS.
- Identifiants MySQL de démonstration en clair dans `docker-compose.yml`; utiliser des secrets en production.
- Authentification OIDC/Keycloak, autorisations par rôle, outbox transactionnelle, DLQ Kafka, observabilité et tests d'intégration restent à réaliser.
- La réservation appelle Event Service pour bloquer la capacité; une compensation est faite en cas d'échec d'enregistrement local. Pour une garantie forte multi-service, compléter avec saga/outbox et tâches de réconciliation.

## Travail en équipe (3 personnes)
- Développeuse A : Event Service et capacité / tests.
- Développeuse B : Booking Service et intégration Feign (responsable réservation).
- Développeuse C : Payment, Ticket, Notification et Kafka.
- Intégration commune : une Pull Request par fonctionnalité vers `main`; ne jamais pousser directement sur `main`.
- `ticketflow-config` est un dépôt séparé : chaque changement de configuration doit être commit/poussé dans ce dépôt aussi.

Commandes recommandées : `git switch -c feature/booking-reservation`, `git add ...`, `git commit -m "feat(booking): implement reservation flow"`, `git push -u origin feature/booking-reservation`. Avant une nouvelle tâche : `git switch main && git pull --ff-only`.
