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

@Getter
@ToString
@Slf4j
public abstract class Aggregate {
  protected final UUID aggregateId;
  protected List<Event> changes;
  // The current version of the aggregate after the latest event has been applied.
  protected long aggregateVersion;
  // The version of the aggregate before any changes were applied—essentially the version when the aggregate was originally loaded.
  protected long reconstitutedAggregateVersion;

  protected Aggregate(@NonNull UUID aggregateId, long version) {
    this.aggregateId = aggregateId;
    this.aggregateVersion = version;
    this.changes = Collections.emptyList();
  }

  // Reconstitution method to rebuild state from events
  public void reconstituteFromEvents(List<Event> events) {
    // Guard clause to check for unsaved changes
    if (!changes.isEmpty()) {
      throw new IllegalStateException("Cannot reconstitute from history. The aggregate has uncommitted changes.");
    }

    // Validate and apply events using Stream API
    events.stream()
      .peek(event -> {
        // Validate the event aggregate version before applying
        if (event.getAggregateVersion() <= aggregateVersion) {
          throw new IllegalArgumentException(
            "Event aggregate version (%s) must be greater than the current aggregate version (%s).".formatted(event.getAggregateVersion(), aggregateVersion)
          );
        }
      })
      .forEach(this::apply);  // Apply each valid event to the aggregate's state

    // Update currentAggregateVersion and reconstitutedAggregateVersion if events are applied successfully
    events.stream().reduce((first, second) -> second).ifPresent(lastEvent -> reconstitutedAggregateVersion = aggregateVersion = lastEvent.getAggregateVersion());
  }

  // Apply change and register event for saving
  public void applyChange(Event event) {
    validateEventVersion(event);
    apply(event);
    changes.add(event);
    aggregateVersion = event.getAggregateVersion();
  }

  // Method to apply an event and modify state
  private void apply(Event event) {
    log.info("Applying event {}", event);
    invoke(event, "apply");
  }

  protected long getNextAggregateVersion() {
    return this.aggregateVersion + 1;
  }

  protected void validateEventVersion(Event event) {
    if (event.getAggregateVersion() != getNextAggregateVersion()) {
      throw new IllegalStateException(
        String.format("Event version %s doesn't match expected version %s. " + "Current state may be inconsistent.", event.getAggregateVersion(), getNextAggregateVersion()));
    }
  }

  // Get the uncommitted changes (events)
  public List<Event> getUncommittedChanges() {
    return changes;
  }

  // Clear uncommitted changes after they have been saved
  public void markChangesAsCommitted() {
    this.changes.clear();
  }

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

  @Nonnull
  public abstract String getAggregateType();

}
