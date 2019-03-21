/*
 *  Copyright 2013-2016 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.springframework.cloud.contract.verifier.messaging.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Message;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.contract.verifier.messaging.MessageVerifier;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessage;
import org.springframework.cloud.contract.verifier.messaging.internal.ContractVerifierMessaging;
import org.springframework.cloud.contract.verifier.messaging.noop.NoOpContractVerifierAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Marcin Grzejszczak
 */
@Configuration
@ConditionalOnClass(Message.class)
@AutoConfigureBefore(NoOpContractVerifierAutoConfiguration.class)
public class ContractVerifierCamelConfiguration {

	@Bean
	@ConditionalOnMissingBean
	MessageVerifier<Message> contractVerifierMessageExchange(
			CamelContext context) {
		return new CamelStubMessages(context);
	}

	@Bean
	@ConditionalOnMissingBean
	public ContractVerifierMessaging<Message> contractVerifierMessaging(
			MessageVerifier<Message> exchange) {
		return new ContractVerifierCamelHelper(exchange);
	}
}

class ContractVerifierCamelHelper extends ContractVerifierMessaging<Message> {

	public ContractVerifierCamelHelper(
			MessageVerifier<Message> exchange) {
		super(exchange);
	}

	@Override
	protected ContractVerifierMessage convert(Message receive) {
		return new ContractVerifierMessage(receive.getBody(), receive.getHeaders());
	}
}
