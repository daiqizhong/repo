package com.weishang.comm.mail;

public interface IEventExecutor<ApplicationEvent> {

	Object execute(ApplicationEvent event);
}
