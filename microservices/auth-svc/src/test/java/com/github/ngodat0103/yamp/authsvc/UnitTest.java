package com.github.ngodat0103.yamp.authsvc;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({
  "com.github.ngodat0103.yamp.authsvc.repository",
  "com.github.ngodat0103.yamp.authsvc.service"
})
public class UnitTest {}
