CREATE TABLE users (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(120) UNIQUE,
    password VARCHAR(120) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE events (
    event_id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    pretty_name VARCHAR(255) NOT NULL UNIQUE,
    location VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL
);

CREATE TABLE subscription (
    subscription_number INT AUTO_INCREMENT PRIMARY KEY,
    event_id VARCHAR(36) NOT NULL,
    subscribed_user_id VARCHAR(36) NOT NULL,
    indication_user_id VARCHAR(36),

    CONSTRAINT fk_subscription_event FOREIGN KEY (event_id) REFERENCES events(event_id),
    CONSTRAINT fk_subscription_user FOREIGN KEY (subscribed_user_id) REFERENCES users(id),
    CONSTRAINT fk_subscription_indication FOREIGN KEY (indication_user_id) REFERENCES users(id)
);