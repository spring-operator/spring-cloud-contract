/*
 * Copyright 2016-2017 the original author or authors.
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

package org.springframework.cloud.contract.wiremock;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import javax.servlet.http.HttpServletResponse;

import com.github.tomakehurst.wiremock.common.Exceptions;
import com.github.tomakehurst.wiremock.core.FaultInjector;
import org.apache.coyote.Response;
import org.apache.tomcat.util.net.SocketWrapperBase;
import org.springframework.util.ReflectionUtils;

import static com.github.tomakehurst.wiremock.common.Exceptions.throwUnchecked;

/**
 * @author Dave Syer
 *
 */
public class TomcatFaultInjector implements FaultInjector {

	private static final byte[] GARBAGE = "lskdu018973t09sylgasjkfg1][]'./.sdlv"
			.getBytes(Charset.forName("UTF-8"));
	private final Response response;
	private SocketWrapperBase<?> socket;

	public TomcatFaultInjector(HttpServletResponse response) {
		this.response = ((org.apache.catalina.connector.Response) getField(response,
				"response")).getCoyoteResponse();
		this.socket = (SocketWrapperBase<?>) getField(
				getField(this.response, "outputBuffer"), "socketWrapper");
	}

	private Object getField(Object target, String string) {
		Field field = ReflectionUtils.findField(target.getClass(), string);
		ReflectionUtils.makeAccessible(field);
		return ReflectionUtils.getField(field, target);
	}

	@Override
	public void emptyResponseAndCloseConnection() {
		try {
			this.socket.close();
		}
		catch (IOException e) {
			Exceptions.throwUnchecked(e);
		}
	}

	@Override
	public void malformedResponseChunk() {
		try {
			this.response.sendHeaders();
			this.response.doWrite(ByteBuffer.wrap(GARBAGE));
			this.socket.flush(true);
			this.socket.close();
		}
		catch (IOException e) {
			throwUnchecked(e);
		}
	}

	@Override
	public void randomDataAndCloseConnection() {
		try {
			this.socket.write(true, GARBAGE, 0, GARBAGE.length);
			this.socket.flush(true);
			this.socket.close();
		}
		catch (IOException e) {
			throwUnchecked(e);
		}
	}

}
