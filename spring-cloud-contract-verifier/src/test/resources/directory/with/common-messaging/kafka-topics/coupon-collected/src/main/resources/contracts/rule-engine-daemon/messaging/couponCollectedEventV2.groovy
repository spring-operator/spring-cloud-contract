/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.springframework.cloud.contract.spec.Contract

Contract.make {

	description("""Should send a message in topic coupon_collected""")

	label 'couponCollectedSm'

	input {
		triggeredBy('couponCollectedSm()')
	}

	outputMessage {
		sentTo('coupon_collected')

		body([
				receiverSnId:
						value(consumer("receiver-sn-id"), producer(regex('([^\\W]|-)+'))),
				sessionId   : value(consumer(7928568413097907541), producer(regex('\\d+'))),
				createdTs   : value(consumer(1504688949158), producer(regex('\\d+'))),
				couponToken : value(
						consumer("440006-6-1504688949139-xyuzzrx5"),
						producer(regex('([^\\W]|-)+')))
		])

		headers {
			messagingContentType(applicationJsonUtf8())
		}
	}
}
