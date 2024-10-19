package com.cjrequena.sample.domain.aggregate;

import com.cjrequena.sample.domain.event.Event;
import jakarta.annotation.Nonnull;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Abstract base class for aggregates in the domain model.
 * An aggregate is a cluster of domain objects that can be treated as a single unit.
 * This class manages the state and behavior of the aggregate by handling events
 * and ensuring consistency of the aggregate's version.
 */
@Getter
@ToString
@Slf4j
public abstract class Aggregate {
  protected final UUID aggregateId;
  protected List<Event> unconfirmedEventsPool;
  protected long aggregateVersion; // The current version of the aggregate after the latest event has been applied.
  protected long reconstitutedAggregateVersion; // The version of the aggregate before any changes were applied.

  /**
   * Constructs an instance of the Aggregate class.
   *
   * @param aggregateId The unique identifier for this aggregate.
   * @param version The initial version of the aggregate.
   */
  protected Aggregate(@NonNull UUID aggregateId, long version) {
    this.aggregateId = aggregateId;
    this.aggregateVersion = version;
    this.unconfirmedEventsPool = Collections.emptyList();
  }

  /**
   * Reconstitutes the aggregate's state from a list of confirmed events.
   * This method applies the confirmed events to the aggregate and updates the
   * aggregate's version accordingly. If there are unconfirmed events,
   * an exception will be thrown to prevent inconsistencies.
   *
   * @param events The list of events used to reconstitute the aggregate.
   * @throws IllegalStateException if there are uncommitted changes.
   * @throws IllegalArgumentException if any event's aggregate version is not greater than the current version.
   */
  public void reconstituteFromConfirmedEvents(List<Event> events) {
    // Guard clause to check for unsaved changes
    if (!unconfirmedEventsPool.isEmpty()) {
      throw new IllegalStateException("Cannot reconstitute from history. The aggregate has unconfirmed events.");
    }

    // Validate and apply events using Stream API
    events.stream()
      .peek(event -> {
        // Validate the event aggregate version before applying
        if (event.getAggregateVersion() <= aggregateVersion) {
          throw new IllegalArgumentException(
            "Event aggregate version (%s) must be greater than the current aggregate version (%s)."
              .formatted(event.getAggregateVersion(), aggregateVersion));
        }
      })
      .forEach(this::applyEvent);  // Apply each valid event to the aggregate's state

    // Update currentAggregateVersion and reconstitutedAggregateVersion if events are applied successfully
    events.stream().reduce((first, second) -> second)
      .ifPresent(lastEvent -> reconstitutedAggregateVersion = aggregateVersion = lastEvent.getAggregateVersion());
  }

  /**
   * Applies an unconfirmed event represented by the given event and registers it for saving.
   *
   * @param event The event representing the change to apply.
   * @throws IllegalStateException if the event's version does not match the expected version.
   */
  public void applyUnconfirmedEvent(Event event) {
    validateEventVersion(event);
    applyEvent(event);
    unconfirmedEventsPool.add(event);
    aggregateVersion = event.getAggregateVersion();
  }

  /**
   * Applies the specified event to the aggregate's state.
   *
   * @param event The event to apply.
   */
  private void applyEvent(Event event) {
    log.info("Applying event {}", event);
    invoke(event, "apply");
  }

  /**
   * Gets the next expected aggregate version.
   *
   * @return The next aggregate version.
   */
  protected long getNextAggregateVersion() {
    return this.aggregateVersion + 1;
  }

  /**
   * Validates the version of the specified event against the expected version.
   *
   * @param event The event to validate.
   * @throws IllegalStateException if the event's version does not match the expected version.
   */
  protected void validateEventVersion(Event event) {
    if (event.getAggregateVersion() != getNextAggregateVersion()) {
      throw new IllegalStateException(
        String.format("Event version %s doesn't match expected version %s. " +
          "Current state may be inconsistent.", event.getAggregateVersion(), getNextAggregateVersion()));
    }
  }

  /**
   * Marks the unconfirmed events as confirmed, clearing the unconfirmed events pool.
   */
  public void markUnconfirmedEventsAsConfirmed() {
    this.unconfirmedEventsPool.clear();
  }

  /**
   * Invokes a method on the aggregate with the specified parameter.
   *
   * @param parameter The parameter to pass to the method.
   * @param methodName The name of the method to invoke.
   * @throws UnsupportedOperationException if the method is not supported or accessible.
   * @throws RuntimeException if invocation fails due to an exception thrown by the invoked method.
   */
  @SneakyThrows
  private void invoke(Object parameter, String methodName) {
    Class<?> parameterType = parameter.getClass();
    try {
      Method method = this.getClass().getMethod(methodName, parameterType);
      method.invoke(this, parameter);
    } catch (NoSuchMethodException e) {
      throw new UnsupportedOperationException(
        String.format("Aggregate %s doesn't support method %s(%s).", this.getClass().getSimpleName(), methodName, parameterType.getSimpleName()), e);
    } catch (IllegalAccessException e) {
      throw new UnsupportedOperationException(
        String.format("Method %s in aggregate %s is not accessible.", methodName, this.getClass().getSimpleName()), e);
    } catch (InvocationTargetException e) {
      throw new RuntimeException(
        String.format("Invocation of method %s failed due to: %s", methodName, e.getCause().getMessage()), e.getCause());
    }
  }

  /**
   * Returns the type of the aggregate as a string.
   *
   * @return The aggregate type.
   */
  @Nonnull
  public abstract String getAggregateType();
}
