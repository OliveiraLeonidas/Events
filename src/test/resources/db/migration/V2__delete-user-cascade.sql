# USE db_events;

ALTER TABLE subscription
DROP FOREIGN KEY fk_subscription_user;

ALTER TABLE subscription
ADD CONSTRAINT fk_subscription_user
FOREIGN KEY (subscribed_user_id)
REFERENCES users(id)
ON DELETE CASCADE;