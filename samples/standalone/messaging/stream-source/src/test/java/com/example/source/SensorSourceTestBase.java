/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.source;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.context.WebApplicationContext;

/**
 * Base class for sensor autogenerated tests (used by Spring Cloud Contract).
 *
 * This bootstraps the Spring Boot application code.
 *
 * @author Marius Bogoevici
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ContractVerifierSampleStreamSourceApplication.class, properties="spring.cloud.stream.bindings.output.destination=sensor-data")
@AutoConfigureMessageVerifier
public abstract class SensorSourceTestBase {

	@Autowired
	ContractVerifierSampleStreamSourceApplication application;

	@Autowired
	WebApplicationContext context;

	@Before
	public void setup() {
		RestAssuredMockMvc.webAppContextSetup(this.context);
	}

	public void createSensorData() {
		application.poll();
	}
}
