package net.runelite.api.packet.client;

import lombok.SneakyThrows;
import net.runelite.api.NetWriter;
import net.runelite.api.PacketBuffer;
import net.runelite.api.PacketNode;
import net.runelite.api.packet.GamePacketInfo;
import net.runelite.api.packet.Packet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class ClientPacket extends Packet {

    private static List<PacketInfo> packetInfoList = null;

    @SneakyThrows
    public void writePacket(final NetWriter netWriter) {
        try {
            if (packetInfoList == null)
                loadPacketInfo(netWriter.getClass().getClassLoader());

            String packetName = getClass().getAnnotation(net.runelite.api.packet.PacketInfo.class).value();

            for (PacketInfo packetInfo : packetInfoList) {
                if (packetInfo.name.equals(packetName)) {
                    net.runelite.api.ClientPacket clientPacket = (net.runelite.api.ClientPacket) packetInfo.clientPacketField.get(null);
                    final PacketNode node = netWriter.getPacketNode(clientPacket);

                    System.out.println("Dispatch: " + clientPacket.getPacketId() + ", " + packetName + " packet=" + toString());
                    for (int i = 0; i < packetInfo.packetFields.size(); i++) {
                        final Method methodCall = packetInfo.obfuscatedWriteCalls.get(i);
                        final Class<?> garbageType = methodCall.getParameterTypes()[1];
                        final Object garbageValue = convertType(packetInfo.garbageValues.get(i), garbageType);
                        final Object param = getField(packetInfo.packetFields.get(i));

                        try {
                            methodCall.invoke(node.getPacketBuffer(), param, garbageValue);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    netWriter.dispatchPacket(node);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPacketName(net.runelite.api.ClientPacket cp) {
        if (packetInfoList == null)
            loadPacketInfo(cp.getClass().getClassLoader());

        for (final PacketInfo packetInfo : packetInfoList) {
            Field clientPacketField = packetInfo.clientPacketField;
            clientPacketField.setAccessible(true);
            try {
                if (cp.equals(packetInfo.clientPacketField.get(null))) {
                    return packetInfo.name;
                }
            } catch (IllegalAccessException e) {
            }
        }

        return null;
    }

    private Object convertType(Integer obj, Class<?> clazz) {
        if (clazz == int.class) {
            return obj;
        } else if (clazz == long.class) {
            return obj.longValue();
        } else if (clazz == short.class) {
            return obj.shortValue();
        } else if (clazz == byte.class) {
            return obj.byteValue();
        } else {
            return null;
        }
    }

    @SneakyThrows
    private Object getField(String name) {
        final Field field = this.getClass().getDeclaredField(name);
        field.setAccessible(true);

        return field.get(this);
    }

    public static synchronized void loadPacketInfo(final ClassLoader classLoader) {
        if (packetInfoList != null)
            return;

        try {
            final ObjectInputStream ois = new ObjectInputStream(classLoader.getResourceAsStream("packet_mappings.dat"));
            final String clientPacketClassName = (String) ois.readObject();
            final String bufferClassName = (String) ois.readObject();
            final Class<?> clientPacketClass = classLoader.loadClass(clientPacketClassName);
            final Class<?> bufferClass = classLoader.loadClass(bufferClassName);

            final Map<String, List<Object>> obj = (Map<String, List<Object>>) ois.readObject();
            ois.close();

            packetInfoList = new LinkedList<>();

            for (Map.Entry<String, List<Object>> entry : obj.entrySet()) {
                List<Object> value = entry.getValue();
                String obfuscatedPacketName = (String) value.get(0);
                List<String> paramNames = (List<String>) value.get(1);
                List<String> obfuscatedNames = (List<String>) value.get(2);
                List<Integer> garbageValues = (List<Integer>) value.get(3);

                List<Method> obfuscatedMethodCalls = obfuscatedNames.stream().map(name -> {
                    for (Method declaredMethod : bufferClass.getDeclaredMethods()) {
                        if (declaredMethod.getName().equals(name)) {
                            declaredMethod.setAccessible(true);
                            return declaredMethod;
                        }
                    }

                    return null;
                }).collect(Collectors.toList());

                final Field clientPacketField = clientPacketClass.getDeclaredField(obfuscatedPacketName);
                clientPacketField.setAccessible(true);

                PacketInfo packetInfo = new PacketInfo(entry.getKey(), clientPacketField, paramNames,
                        obfuscatedMethodCalls, garbageValues);

                packetInfoList.add(packetInfo);
            }

        } catch (IOException | ClassNotFoundException | NoSuchFieldException e) {
            e.printStackTrace();
            System.exit(0);
        }

    }

    public static ClientPacket fromPacketNode(final NetWriter netWriter, final PacketNode node) {
        final net.runelite.api.ClientPacket clientPacket = node.getClientPacket();
        final GamePacketInfo gamePacketInfo = GamePacketInfo.fromGamePacket(netWriter, clientPacket);

        if (gamePacketInfo == null)
            return null;

        final PacketBuffer buffer = node.getPacketBuffer();
        final int initialOffset = buffer.getOffset();
        buffer.setOffset(1);

        final Packet result = gamePacketInfo.getReader().apply(buffer);
        buffer.setOffset(initialOffset);

        return (ClientPacket) result;
    }

    private static class PacketInfo {
        String name;
        Field clientPacketField;
        List<String> packetFields;
        List<Method> obfuscatedWriteCalls;
        List<Integer> garbageValues;

        public PacketInfo(String name, Field clientPacketField, List<String> packetFields, List<Method> obfuscatedWriteCalls, List<Integer> garbageValues) {
            this.name = name;
            this.clientPacketField = clientPacketField;
            this.packetFields = packetFields;
            this.obfuscatedWriteCalls = obfuscatedWriteCalls;
            this.garbageValues = garbageValues;
        }
    }
}
