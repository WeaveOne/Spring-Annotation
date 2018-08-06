package cn.willvi.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import cn.willvi.bean.Person;
import cn.willvi.config.MainConfig;

public class Main {

	public static void main(String[] args) {
		ApplicationContext ioc = new AnnotationConfigApplicationContext(MainConfig.class);
		Person person = ioc.getBean(Person.class);
		System.out.println(person.toString());
	}
}
