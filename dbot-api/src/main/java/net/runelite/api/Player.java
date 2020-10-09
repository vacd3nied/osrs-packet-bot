/*
 * Copyright (c) 2016-2017, Adam <Adam@sigterm.info>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.api;

import net.runelite.api.packet.client.*;
import net.runelite.api.packet.client.ClientPacket;

import java.awt.Polygon;

public interface Player extends Actor
{
	@Override
	int getCombatLevel();

	PlayerComposition getPlayerComposition();

	Polygon[] getPolygons();

	int getTeam();

	boolean isClanMember();

	boolean isFriend();

	int getIndex();

	HeadIcon getOverheadIcon();

	default ClientPlayerAction1 action1() {
		return new ClientPlayerAction1(getIndex(), 0);
	}

	default ClientPlayerAction2 action2() {
		return new ClientPlayerAction2(getIndex(), 0);
	}

	default ClientPlayerAction3 action3() {
		return new ClientPlayerAction3(getIndex(), 0);
	}

	default ClientPlayerAction4 action4() {
		return new ClientPlayerAction4(getIndex(), 0);
	}

	default ClientPlayerAction5 action5() {
		return new ClientPlayerAction5(getIndex(), 0);
	}
}
