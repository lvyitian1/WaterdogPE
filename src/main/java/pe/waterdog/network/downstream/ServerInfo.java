/**
 * Copyright 2020 WaterdogTEAM
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pe.waterdog.network.downstream;

import com.nukkitx.protocol.bedrock.BedrockClientSession;

import java.net.InetSocketAddress;

public class ServerInfo {

    private final String serverName;
    private final InetSocketAddress address;

    private BedrockClientSession downstreamClient;

    public ServerInfo(String serverName, InetSocketAddress address){
        this.serverName = serverName;
        this.address = address;
    }

    public BedrockClientSession getDownstreamClient() {
        return downstreamClient;
    }

    public void setDownstreamClient(BedrockClientSession downstreamClient) {
        this.downstreamClient = downstreamClient;
    }

    public String getServerName() {
        return serverName;
    }

    public InetSocketAddress getAddress() {
        return address;
    }
}