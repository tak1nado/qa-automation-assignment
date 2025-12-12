package com.project.qa.steps;

import com.project.qa.helpers.ThreadVarsHashMap;
import com.project.qa.helpers.user.engine.UserSessions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@ContextConfiguration(locations = {"classpath:spring-application-context.xml"})
public abstract class AbstractStepDefs {
    @Autowired protected ThreadVarsHashMap<TestKeyword> threadVarsHashMap;
    @Autowired protected UserSessions userSessions;

}