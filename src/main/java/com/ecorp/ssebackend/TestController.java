package com.ecorp.ssebackend;

import com.ecorp.ssebackend.api.PersonsApi;
import com.ecorp.ssebackend.model.ProgressUpdatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin
public class TestController implements PersonsApi, ApplicationListener<PersonUpdatedEvent> {

  @Autowired private ApplicationEventPublisher applicationEventPublisher;

  private final SubscribableChannel subscribableChannel = MessageChannels.publishSubscribe().getObject();

    //List<Person> persons = new ArrayList<>();
    //private Sinks.Many<ServerSentEvent<Person>> likesSink = Sinks.many().multicast().onBackpressureBuffer();
    /*@GetMapping(value = "/persons", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> getPersons() {
        return Flux.create(sink -> {
          MessageHandler handler = message -> sink.next("sd");
          sink.onCancel(() -> subscribableChannel.unsubscribe(handler));
          subscribableChannel.subscribe(handler);
        }, FluxSink.OverflowStrategy.LATEST);
    }*/

  /*@GetMapping(value = "/persons", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public Mono<ResponseEntity<Flux<Person>>> getPersons() {
    Flux f = Flux.create(sink -> {
      MessageHandler handler = message -> sink.next(message.getPayload());
      sink.onCancel(() -> subscribableChannel.unsubscribe(handler));
      subscribableChannel.subscribe(handler);
    }, FluxSink.OverflowStrategy.LATEST);
    ResponseEntity responseEntity = ResponseEntity.ok(f);
    return Mono.just(responseEntity);
  }*/

    @GetMapping(value = "/add/{name}")
    public void addPerson(@PathVariable("name") String name) {
        Person p = new Person();
        p.setName(name);
      PersonUpdatedEvent personUpdatedEvent = new PersonUpdatedEvent(this, p);
      applicationEventPublisher.publishEvent(personUpdatedEvent);
    }

  @Override
  public void onApplicationEvent(PersonUpdatedEvent event) {
    subscribableChannel.send(new GenericMessage<>(ServerSentEvent.builder().id("1").data(event.getP()).build()));
  }

  @Override
  public Mono<ResponseEntity<Flux<ProgressUpdatedEvent>>> updatePet(ServerWebExchange exchange) {
    Flux f = Flux.create(sink -> {
      MessageHandler handler = message -> sink.next(message.getPayload());
      sink.onCancel(() -> subscribableChannel.unsubscribe(handler));
      subscribableChannel.subscribe(handler);
    }, FluxSink.OverflowStrategy.LATEST);
    ResponseEntity responseEntity = ResponseEntity.ok(f);
    return Mono.just(responseEntity);
  }
/*
  @Override
  public ResponseEntity<List<ProgressUpdatedEvent>> updatePet(UpdatePetRequest updatePetRequest) {
    Flux f = Flux.create(sink -> {
      MessageHandler handler = message -> sink.next(message.getPayload());
      sink.onCancel(() -> subscribableChannel.unsubscribe(handler));
      subscribableChannel.subscribe(handler);
    }, FluxSink.OverflowStrategy.LATEST);
    ResponseEntity responseEntity = ResponseEntity.ok(f);
    return Mono.just(responseEntity);
  }*/

}
