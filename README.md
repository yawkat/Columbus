Columbus
========

Minecraft world generation & NBT API.

NBT
---

```Java
NamedTag tag = NBT.deserializeStream(stream);
TagCompound compound = (TagCompound) tag.getValue();
byte test = compound.getByte("test");
compound.addTag("test", new TagByte(test + 1));
NBT.serializeStream(stream);
```

Worlds
------

```Java
// create level.dat handle (metadata)
Level level = new Level();
level.setName("Test");

// create overworld
World overworld = new World(new WorldProperties(128));

// create save folder handle
LevelFolder folder = new LevelFolder(level, overwold);

// create chunk at (0|0)
Chunk zero = overworld.getOrCreateChunk(0, 0);
// set (0|0|0) to block ID 5 (wood)
zero.setBlock(0, 0, 0, 5, 0);

// create a default lighter
Lighter lighter = new Lighter();
lighter.putDefaultBlockLightData();
// update heightmap
overworld.refreshHeightMap(lighter);
// increase light value of all blocks to 100%
overworld.fullbright();

// save
folder.print(Paths.get("~/.minecraft/saves/test/"));
```

Reading world folders is not yet supported.
