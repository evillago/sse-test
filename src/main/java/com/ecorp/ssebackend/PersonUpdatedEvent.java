package com.ecorp.ssebackend;

import org.springframework.context.ApplicationEvent;

import java.util.List;

public class PersonUpdatedEvent extends ApplicationEvent {

  private Person p;

  public PersonUpdatedEvent(Object source, Person p) {
    super(source);
    this.p = p;
  }

  public Person getP() {
    return p;
  }

  public void setP(Person p) {
    this.p = p;
  }
}
