package net.runelite.api;

public interface Buffer {

    int getOffset();

    void setOffset(int offset);

    void putByte(int var1);

    void putShort(int var1);

    void putInt(int var1);

    void putLong(long var1);

    void putString(String var1);

    void putBytes(byte[] var1, int var2, int var3);

    void encryptXtea2(int[] bytes);

    int putCrc(int var1);

    long readLong();

    String readString();

    String getJagString();

    void readBytes(byte[] buffer, int offset, int length);

    int readShort();

    int readShortSmart();

    int getUSmart();

    int getLargeSmart();

    int readVarInt();

    int readInt();

}
