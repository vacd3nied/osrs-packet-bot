/*
 * Copyright (c) 2017, Devin French <https://github.com/devinfrench>
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
package net.runelite.api.queries;

import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.lang.Math.abs;

public abstract class TileObjectQuery<EntityType extends TileObject, QueryType> extends Query<EntityType, QueryType>
{
	private static final int REGION_SIZE = 104;

	protected List<Tile> getTiles(Client client)
	{
		List<Tile> tilesList = new ArrayList<>();
		Region scene = client.getRegion();
		Tile[][][] tiles = scene.getTiles();
		int z = client.getPlane();
		for (int x = 0; x < REGION_SIZE; ++x)
		{
			for (int y = 0; y < REGION_SIZE; ++y)
			{
				Tile tile = tiles[z][x][y];
				if (tile == null)
				{
					continue;
				}
				tilesList.add(tile);
			}
		}
		return tilesList;
	}

	@SuppressWarnings("unchecked")
	public QueryType idEquals(int... ids)
	{
		predicate = and(object ->
		{
			for (int id : ids)
			{
				if (object.getId() == id)
				{
					return true;
				}
			}
			return false;
		});
		return (QueryType) this;
	}

	@SuppressWarnings("unchecked")
	public QueryType atWorldLocation(WorldPoint location)
	{
		predicate = and(object -> object.getWorldLocation().equals(location));
		return (QueryType) this;
	}

	@SuppressWarnings("unchecked")
	public QueryType atLocalLocation(LocalPoint location)
	{
		predicate = and(object -> object.getLocalLocation().equals(location));
		return (QueryType) this;
	}

	@SuppressWarnings("unchecked")
	public QueryType isWithinDistance(LocalPoint to, int distance)
	{
		predicate = and(a -> a.getLocalLocation().distanceTo(to) <= distance);
		return (QueryType) this;
	}

	@SuppressWarnings("unchecked")
	public QueryType isWithinArea(LocalPoint from, int area)
	{
		predicate = and(a ->
		{
			LocalPoint localLocation = a.getLocalLocation();
			return abs(localLocation.getX() - from.getX()) < area
				&& abs(localLocation.getY() - from.getY()) < area;
		});
		return (QueryType) this;
	}

	@SuppressWarnings("unchecked")
	public QueryType isWithinWorldArea(WorldArea area) {
		predicate = and(a ->
		{
			return area.contains(a.getWorldLocation());
		});
		return (QueryType) this;
	}

	public Optional<EntityType> closest(final Client client) {
		final QueryResult<EntityType> queryResult = eval(client);
		final WorldPoint localPoint = client.getLocalPlayer().getWorldLocation();

		return queryResult.stream().min(Comparator.comparingInt(a -> (a).getWorldLocation().distanceTo(localPoint)));
	}

	public Optional<EntityType> closest(final Client client, final LocalPoint localPoint) {
		final QueryResult<EntityType> queryResult = eval(client);
		return queryResult.stream().min(Comparator.comparingInt(a -> (a).getLocalLocation().distanceTo(localPoint)));
	}
}
