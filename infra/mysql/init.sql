-- Une base de données par microservice (exécuté au premier démarrage du conteneur MySQL)
CREATE DATABASE IF NOT EXISTS customer_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS event_db    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS booking_db  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS payment_db  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS ticket_db   CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON `customer_db`.* TO 'ticketflow'@'%';
GRANT ALL PRIVILEGES ON `event_db`.*    TO 'ticketflow'@'%';
GRANT ALL PRIVILEGES ON `booking_db`.*  TO 'ticketflow'@'%';
GRANT ALL PRIVILEGES ON `payment_db`.*  TO 'ticketflow'@'%';
GRANT ALL PRIVILEGES ON `ticket_db`.*   TO 'ticketflow'@'%';
FLUSH PRIVILEGES;
