# event-sourcing-cqrs-postgresql-sample
event-sourcing-cqrs-postgresql-sample

This is an implementation based on [Event Sourcing with PostgreSQL](https://github.com/eugene-khyst/postgresql-event-sourcing)
but using spring data jpa with native queries and also separating the command handler, event handler and query handler in three isolated 
microservice modules. Also, the event store is in a separated schema from the projection database.

---

Event Sourcing, CQRS (Command Query Responsibility Segregation), and event store are closely related software patterns 
that together form a powerful way to build complex, high-performance, and resilient applications. Let's break down each 
concept and provide a detailed look into the related topics.

---

## **Event Sourcing**

Event Sourcing is a software pattern where state changes in a system are captured as a series of events, rather than being 
stored in a database as the current state. This allows the application to "replay" events to reconstruct any prior state. 
Each event describes a change in the state (e.g., "OrderPlaced" for an order), which means the entire history is preserved.

Event sourcing is strongly linked to domain-driven design (DDD) and uses similar terminology.

In event sourcing, an entity is referred to as an **aggregate**.

A series of events tied to the same aggregate is known as a **stream**.

The current state of an entity can be reconstructed by replaying all the events associated with it.

Event sourcing is most effective for entities with a short lifecycle and a limited number of events (such as orders).

Replaying all events to restore the state of a short-lived entity has minimal impact on performance, so optimization techniques aren’t necessary for these cases.

For entities with a long lifespan (such as user profiles or bank accounts) that accumulate thousands of events, replaying all events to restore state becomes inefficient, making **snapshotting** a valuable optimization.

**Advantages of Event Sourcing**:
- **Auditability**: Every state change is recorded, making it easy to track the history.
- **Time-travel**: The system state at any point in time can be reconstructed by replaying events up to that point.
- **Flexibility**: New projections of the data can be created without needing to modify or access the original event data.

**Challenges**:
- **Complexity**: Requires more careful design, especially in managing event versioning.
- **Storage**: Storing all events can take up considerable space, especially if not pruned or archived.

---

## **CQRS (Command Query Responsibility Segregation)**

CQRS is a pattern that separates the commands that alter data (writes) from the queries that retrieve data (reads). This 
can provide performance, scalability, and flexibility advantages by allowing each side (command and query) to be optimized 
independently.

**Key Advantages of CQRS**:
- **Scalability**: Commands and queries can be scaled separately.
- **Optimized data access**: Each side can be optimized specifically for its purpose, e.g., complex read models can be precomputed and stored separately.
- **Supports Event Sourcing**: CQRS is often paired with Event Sourcing to create highly decoupled and scalable systems.

**Challenges**:
- **Complexity**: CQRS adds complexity to the architecture, with separate models and handlers.
- **Consistency**: Achieving consistency between the read and write sides can require eventual consistency patterns.

---

## **Event Store**

The event store is a specialized storage system that keeps track of all events in an Event Sourcing system. This is where 
all the events are stored, acting as the source of truth for system changes. Event stores are designed to append events 
rather than update or delete them, which aligns with the principles of Event Sourcing.

---

### **Detailed Breakdown of Related Topics**

1. **Command**
    - A command represents a specific action or operation requested by a user or system, such as "CreateOrder" or "UpdateCustomerInfo."
    - Commands are **intent-driven** (they represent a user or system's intent to perform an action) and are generally issued by the client.
    - In a CQRS architecture, commands are handled by the **Command Handler**.

2. **Aggregate**
    - An aggregate is a domain-driven design (DDD) concept where related entities are grouped together into a single unit, managed by an aggregate root.
    - Aggregates enforce business rules and invariants within themselves, ensuring that state changes are valid.
    - For example, an "Order" aggregate may include line items, addresses, and totals, all grouped under the "Order" entity.
    - In Event Sourcing, aggregates are constructed by replaying the events relevant to that aggregate.

3. **Event**
    - An event is a record of something that has happened in the past. It describes a state change, e.g., "OrderPlaced" or "ProductAddedToCart."
    - Events are **immutable** and cannot be changed once created.
    - Events typically represent a fact and are stored in the event store. The system's current state can be recreated by replaying these events.

4. **Aggregate Snapshot**
    - An aggregate snapshot is a precomputed state of an aggregate at a certain point in time. Instead of replaying every event from the beginning, snapshots allow the system to start from a more recent state, improving performance.
    - Snapshotting is particularly useful for aggregates with a large number of events.
    - Snapshots must include sufficient data to rebuild the aggregate and can be stored separately in a snapshot storage.

5. **Event Subscriptions**
    - Event subscriptions allow other parts of the system to listen to specific events and react to them, often used in CQRS and Event Sourcing systems for notifying other services or updating projections.
    - Subscribers can either pull events or be notified (pushed) via an event bus.
    - Event subscriptions enable asynchronous communication and decouple event producers from consumers.

6. **Projections**
    - Projections are read-only views or representations of events tailored to support specific query patterns.
    - They are often created by applying events to a read model designed specifically for querying.
    - Projections enable CQRS by providing optimized, precomputed read models, which can be independently scaled.

7. **Command Handler**
    - A Command Handler is responsible for processing a command and determining how the system state should change in response.
    - It often invokes an aggregate to perform operations, ensures validation, and may publish resulting events.
    - Command Handlers are critical in CQRS architectures for separating the responsibilities of commands from queries.

8. **Event Handler**
    - An Event Handler processes events as they occur, typically to update projections, notify external services, or trigger other actions.
    - It might handle tasks like updating a "LatestOrders" projection when an "OrderPlaced" event is received.
    - Event Handlers work asynchronously, allowing the system to be decoupled and to follow the principle of eventual consistency.

9. **Query Handler**
    - A Query Handler deals with queries, retrieving data based on specific filters or parameters provided in the request.
    - It reads from projections or dedicated query models, without altering the state of the system.
    - Query Handlers enable the read side of CQRS, where the query model is often optimized for fast data access.

10. **Integration Channels**
    - Integration channels are mechanisms through which different parts of the system or even external systems communicate.
    - Examples include **message queues** (e.g., RabbitMQ, Kafka), **REST APIs**, or **event buses**.
    - In CQRS and Event Sourcing, integration channels allow decoupling of the command and query sides, and they support asynchronous communication for scalable, fault-tolerant architectures.

---

### **How These Patterns Work Together**

1. **Command-Event Flow**:
    - When a command is issued, it’s handled by a Command Handler, which uses aggregates to validate and apply business rules.
    - Once validated, the aggregate publishes an event representing the change.
    - The event is stored in the Event Store, ensuring the change is durable and trackable.

2. **Updating Projections with Event Handlers**:
    - Event Handlers listen for events and use them to update projections, which are separate read models optimized for querying.
    - These projections provide real-time or near-real-time views of the data, accessible via Query Handlers.

3. **Ensuring Read-Write Separation (CQRS)**:
    - The command side (commands and Command Handlers) is entirely separate from the query side (queries and Query Handlers).
    - Command Handlers work with aggregates to enforce business rules, while Query Handlers work with projections to quickly retrieve data.

4. **Integration Channels for Communication**:
    - Integration channels, like message brokers or event buses, facilitate communication between the different components and allow scalable processing of commands, events, and queries across distributed systems.

In summary, these patterns enable systems to handle complex, high-volume transactions with scalability, resilience, and clarity 
in business rules. The Command, Event, and Query models interact through well-defined handlers, while projections and snapshots 
allow for efficient data access and performance. Event Subscriptions and Integration Channels support real-time updates and 
loosely coupled communication, enhancing the flexibility and responsiveness of the entire system.

### References
[Event Sourcing with PostgreSQL](https://github.com/eugene-khyst/postgresql-event-sourcing)
