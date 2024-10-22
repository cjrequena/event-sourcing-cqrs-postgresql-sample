package com.cjrequena.sample.domain.event;

import com.cjrequena.sample.entity.EventEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Method;
import java.time.OffsetDateTime;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
@Log4j2
public abstract class Event {

  // Unique id for the specific message. This id is globally unique
  protected UUID id;

  // The event offset_txid
  protected long offsetTxId;

  // Unique aggregateId for the specific message. This id is globally unique
  protected final UUID aggregateId;

  // The event aggregateVersion.
  protected final long aggregateVersion;

  // Type of message
  protected String eventType;

  // The content type of the event data. Must adhere to RFC 2046 format.
  public String dataContentType;

  // Base64 encoded event payload. Must adhere to RFC4648.
  protected String dataBase64;

  // A URI describing the schema for the event data
  //protected String dataSchema;

  // The time the event occurred
  protected OffsetDateTime time;

  public EventEntity mapToEventEntity() {
    log.info("Mapping to event entity {}", this);
    return invoke();
  }

  @SneakyThrows
  private EventEntity invoke() {
    Class<?> parameterType = this.getClass();
    Method method = this.getClass().getMethod("mapToEventEntity");
    return (EventEntity) method.invoke(this);
  }

}
