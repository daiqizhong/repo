package com.weishang.comm.mail;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 事件监听器
 */
@Component
public class AlarmEventListener implements ApplicationListener<ApplicationEvent> {

	@Override
	public void onApplicationEvent(ApplicationEvent applicationEvent) {

		if (applicationEvent instanceof AlarmEvent) {
			AlarmEvent event = (AlarmEvent) applicationEvent;
			event.getEventExecutor().execute(event);
		}
	}

}
