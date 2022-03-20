package com.bl.hs.keyword.test;

import com.bl.hs.keyword.engine.KeywordEngine;
import org.testng.annotations.Test;

public class TestNG {

    public KeywordEngine keywordEngine;

    @Test
    public void loginTest() {
        keywordEngine = new KeywordEngine();
        keywordEngine.startExecution("login");

    }
}
