Scenario: Create event with valid data
Given that I have an event with the name "summit", prettyName "summit-2024", and location as "online"
And with a future start date and a future end date
When I try to create this event
Then the event should be created successfully