package net.runelite.api;

public interface MachineInfo {

    int getOsType();

    boolean getOs64Bit();

    int getOsVersionType();

    int getJavaVendorType();

    int getJavaVersionMajor();

    int getJavaVersionMinor();

    int getJavaVersionPatch();

    int getMaxMemoryMB();

    void setOsType(int type);

    void setOs64Bit(boolean os64Bit);

    void setOsVersionType(int type);

    void setJavaVendorType(int type);

    void setJavaVersionMajor(int javaVersionMajor);

    void setJavaVersionMinor(int javaVersionMinor);

    void setJavaVersionPatch(int javaVersionPatch);

    void setMaxMemoryMB(int maxMemoryMB);

}
