/*
 * Copyright 1999-2011 Alibaba Group.
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
package com.alibaba.dubbo.remoting.transport;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.Channel;
import junit.framework.TestCase;

import java.io.IOException;

import static org.easymock.EasyMock.*;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.containsString;

/**
 * @author ding.lid
 */
public class AbstractCodecTest extends TestCase {

    public void test_checkPayload_default8M() throws Exception {
        Channel channel = createMock(Channel.class);
        expect(channel.getUrl()).andReturn(URL.valueOf("dubbo://1.1.1.1")).anyTimes();
        replay(channel);

        AbstractCodec.checkPayload(channel, 1 * 1024 * 1024);

        try {
            AbstractCodec.checkPayload(channel, 15 * 1024 * 1024);
        }
        catch (IOException expected) {
            assertThat(expected.getMessage(), allOf(
                    containsString("Data length too large: "),
                    containsString("max payload: " + 8 * 1024 * 1024)
            ));
        }

        verify(channel);
    }

    public void test_checkPayload_minusPayloadNoLimit() throws Exception {
        Channel channel = createMock(Channel.class);
        expect(channel.getUrl()).andReturn(URL.valueOf("dubbo://1.1.1.1?payload=-1")).anyTimes();
        replay(channel);

        AbstractCodec.checkPayload(channel, 15 * 1024 * 1024);

        verify(channel);
    }
}
