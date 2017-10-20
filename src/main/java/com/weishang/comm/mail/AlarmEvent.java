package com.weishang.comm.mail;

import org.springframework.context.ApplicationEvent;

/**
 * 
 */
public class AlarmEvent extends ApplicationEvent {

	private static final long serialVersionUID = -5684023431509700202L;

	private IEventExecutor<ApplicationEvent> eventExecutor;

	public AlarmEvent(Object source) {
		super(source);
	}

	public AlarmEvent(Object source, IEventExecutor<ApplicationEvent> eventExecutor) {
		super(source);
		this.eventExecutor = eventExecutor;
	}

	public IEventExecutor<ApplicationEvent> getEventExecutor() {
		return eventExecutor;
	}

	public void setEventExecutor(IEventExecutor<ApplicationEvent> eventExecutor) {
		this.eventExecutor = eventExecutor;
	}

}
