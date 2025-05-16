Scenario: User successfully subscribes to an event
Given a user with username "usertest" and password "abc123"
And an existing event with id "evt-123" and prettyName "event-name"
And this user is not yet subscribed to the event
When the user tries to subscribe to the event "evt-123"
Then the subscription should be created with a link containing "/event/"