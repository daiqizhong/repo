package com.weishang.comm.mail.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.alibaba.fastjson.JSON;
import com.weishang.comm.mail.AlarmEvent;
import com.weishang.comm.mail.IEventExecutor;
import com.weishang.comm.model.MailFrom;
import com.weishang.comm.util.IpUtils;

import freemarker.template.Template;

/**
 * 邮件报警
 */
@Component
public class AlarmEventExector implements IEventExecutor<AlarmEvent> {

	@Resource
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Value("${mail.from}")
	private String from;

	@Value("${mail.to}")
	private String to;

	@Value("${mail.subject}")
	private String subject;

	@Value("${mail.template}")
	private String template;

	@Value("${project_name}")
	private String project;

	@Override
	public Object execute(AlarmEvent event) {
		try {
			Object source = event.getSource();
			String fullStackTrace = ExceptionUtils.getFullStackTrace(((Exception) source)).replaceAll("at ", "<br>&nbsp;&nbsp;at ");

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("host", IpUtils.getIp());
			map.put("exception", fullStackTrace);
			map.put("project", project);

			this.sendHtml(getMailText(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendHtml(String html) throws Exception {
		// 用于发送多元化邮件，包括附件，图片，html 等
		JavaMailSenderImpl mimeMailSender = new JavaMailSenderImpl();

		List<MailFrom> fromList = JSON.parseArray(from, MailFrom.class);
		MailFrom mailFrom = fromList.get(RandomUtils.nextInt(fromList.size()));

		// 设置邮件服务主机
		mimeMailSender.setHost(mailFrom.getHost());

		// 发送者邮箱的用户名
		mimeMailSender.setUsername(mailFrom.getUsername());

		// 发送者邮箱的密码
		mimeMailSender.setPassword(mailFrom.getPassword());

		// 创建多元化邮件
		MimeMessage mimeMessage = mimeMailSender.createMimeMessage();

		// 创建 mimeMessage 帮助类，用于封装信息至 mimeMessage
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "GBK");

		// 发送者地址，必须填写正确的邮件格式，否者会发送失败
		helper.setFrom(mailFrom.getFrom());

		// 邮件主题
		helper.setSubject(String.format(subject, project));

		// 邮件接收者的邮箱地址
		helper.setTo(to.split(","));

		helper.setText(html, true);

		mimeMailSender.send(mimeMessage);
	}

	private String getMailText(Map<String, Object> map) throws Exception {
		// FreeMarker通过Map传递动态数据
		Template alarmTemplate = freeMarkerConfigurer.getConfiguration().getTemplate(template);

		// 解析模板并替换动态数据，最终content将替换模板文件中的${content}标签。
		return FreeMarkerTemplateUtils.processTemplateIntoString(alarmTemplate, map);
	}

}
