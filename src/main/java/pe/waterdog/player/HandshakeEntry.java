/*
 * Copyright 2021 WaterdogTEAM
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package pe.waterdog.player;

import com.google.gson.JsonObject;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.util.EncryptionUtils;
import pe.waterdog.ProxyServer;
import pe.waterdog.event.defaults.PreClientDataSetEvent;
import pe.waterdog.network.protocol.ProtocolVersion;
import pe.waterdog.network.session.LoginData;

import java.security.interfaces.ECPublicKey;
import java.util.UUID;

public class HandshakeEntry {

    private final ECPublicKey identityPublicKey;
    private final JsonObject clientData;
    private final JsonObject extraData;
    private final boolean xboxAuthed;
    private final ProtocolVersion protocol;

    public HandshakeEntry(ECPublicKey identityPublicKey, JsonObject clientData, JsonObject extraData, boolean xboxAuthed, ProtocolVersion protocol) {
        this.identityPublicKey = identityPublicKey;
        this.clientData = clientData;
        this.extraData = extraData;
        this.xboxAuthed = xboxAuthed;
        this.protocol = protocol;
    }

    public LoginData buildData(BedrockServerSession session, ProxyServer proxy) throws Exception {
        // This is first event which exposes new player connecting to proxy.
        // The purpose is to change player's client data or set encryption keypair before joining first downstream.
        PreClientDataSetEvent event = new PreClientDataSetEvent(clientData, extraData, EncryptionUtils.createKeyPair(), session);
        proxy.getEventManager().callEvent(event);

        LoginData.LoginDataBuilder builder = LoginData.builder();
        builder.displayName(extraData.get("displayName").getAsString());
        builder.uuid(UUID.fromString(extraData.get("identity").getAsString()));
        builder.xuid(extraData.get("XUID").getAsString());
        builder.xboxAuthed(this.xboxAuthed);
        builder.protocol(this.protocol);
        builder.joinHostname(clientData.get("ServerAddress").getAsString().split(":")[0]);
        builder.address(session.getAddress());
        builder.keyPair(event.getKeyPair());
        builder.clientData(clientData);
        builder.extraData(extraData);

        if (proxy.getConfiguration().isUpstreamEncryption()) {
            HandshakeUtils.processEncryption(session, this.identityPublicKey);
        }
        return builder.build();
    }

    public ECPublicKey getIdentityPublicKey() {
        return this.identityPublicKey;
    }

    public boolean isXboxAuthed() {
        return this.xboxAuthed;
    }

    public String getDisplayName() {
        return this.extraData.get("displayName").getAsString();
    }

    public ProtocolVersion getProtocol() {
        return this.protocol;
    }

    public JsonObject getClientData() {
        return this.clientData;
    }

    public JsonObject getExtraData() {
        return this.extraData;
    }
}