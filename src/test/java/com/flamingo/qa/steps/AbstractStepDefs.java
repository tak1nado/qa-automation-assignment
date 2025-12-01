package com.flamingo.qa.steps;

import com.flamingo.qa.helpers.ThreadVarsHashMap;
import com.flamingo.qa.helpers.user.engine.UserSessions;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractStepDefs {
    @Autowired protected ThreadVarsHashMap<TestKeyword> threadVarsHashMap;
    @Autowired protected UserSessions userSessions;

}