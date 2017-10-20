package com.weishang.comm.mail;

import javax.annotation.Resource;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
public class AlarmEventPublisher implements ApplicationEventPublisherAware {

	private ApplicationEventPublisher eventPublisher;
	@Resource
	private IEventExecutor<ApplicationEvent> alarmEventExector;

	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
		this.eventPublisher = applicationEventPublisher;
	}

	public void execute(Exception exception) {
		AlarmEvent alarmEvent = new AlarmEvent(exception, alarmEventExector);
		this.eventPublisher.publishEvent(alarmEvent);
	}
}
