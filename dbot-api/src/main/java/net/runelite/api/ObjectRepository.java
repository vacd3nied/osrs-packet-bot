package net.runelite.api;

import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import org.apache.commons.math3.ml.clustering.Cluster;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.ml.clustering.DBSCANClusterer;
import org.apache.commons.math3.ml.clustering.DoublePoint;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.*;

public class ObjectRepository {

    private static final String OBJECT_FILE = "objdata.dat";
    private static Map<Integer, Set<WorldPoint>> objectMap;

    private static void load() {
        if (objectMap == null) {
            try {
                final ObjectInputStream ofOis = new ObjectInputStream(new FileInputStream(OBJECT_FILE));
                objectMap = (Map<Integer, Set<WorldPoint>>) ofOis.readObject();
                ofOis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static List<WorldPoint> nearest(final WorldPoint start, final int... ids) {
        final List<WorldPoint> points = new ArrayList<>();

        load();

        for (final int id : ids) {
            final Set<WorldPoint> worldPoints = objectMap.get(id);

            if (worldPoints != null)
                points.addAll(worldPoints);
        }

        points.sort(Comparator.comparingDouble(p -> p.distanceTo2D(start)));

        return points;
    }

    public static List<WorldArea> findObjectClusters(final int expandBy, final int radius,
                                                     final int minObjects, final int... ids) {
        final List<WorldArea> clusters = new LinkedList<>();

        for (int plane = 0; plane < 4; plane++) {
            clusters.addAll(findClustersByPlane(plane, expandBy, radius, minObjects, ids));
        }

        return clusters;
    }

    public static List<WorldArea> findClustersByPlane(final int plane, final int expandBy, final int radius,
                                                      final int minObjects, final int... ids) {
        final List<WorldArea> areas = new LinkedList<>();
        final List<DoublePoint> objects = new ArrayList<>();
        load();

        for (int id : ids) {
            final Set<WorldPoint> worldPoints = objectMap.get(id);
            if (worldPoints != null)
                worldPoints.stream()
                        .filter(wp -> wp.getPlane() == plane)
                        .map(wp -> new DoublePoint(new double[]{wp.getX(), wp.getY()}))
                        .forEach(objects::add);
        }

        final DBSCANClusterer<DoublePoint> clusterer = new DBSCANClusterer<>(radius, minObjects);
        final List<Cluster<DoublePoint>> clusters = clusterer.cluster(objects);

        clusters.stream().map(c -> getArea(c, expandBy, plane)).forEach(areas::add);

        return areas;
    }

    private static WorldArea getArea(final Cluster<DoublePoint> cluster, final int expandBy, final int plane) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (final DoublePoint obj : cluster.getPoints()) {
            double[] points = obj.getPoint();
            if (points[0] < minX)
                minX = (int) points[0];
            if (points[0] > maxX)
                maxX = (int) points[0];
            if (points[1] < minY)
                minY = (int) points[1];
            if (points[1] > maxY)
                maxY = (int) points[1];
        }

        return new WorldArea(minX - expandBy, minY - expandBy, maxX - minX + expandBy, maxY - minY + expandBy, plane);
    }

    public static void main(String[] args) {
        List<WorldArea> clustersByPlane = findClustersByPlane(0, 4, 6, 3,
                ObjectID.YEW
        );

        for (WorldArea worldArea : clustersByPlane) {
            System.out.println("Area: x=" + worldArea.getX() + " y=" + worldArea.getY() + " width=" + worldArea.getWidth() + " height=" + worldArea.getHeight() + " plane=" + worldArea.getPlane());
            System.out.println();
        }

//        Field[] declaredFields = ObjectID.class.getDeclaredFields();
//
//        for (Field declaredField : declaredFields) {
//            if (declaredField.getName().startsWith("BANK_BOOTH") || declaredField.getName().startsWith("BANK_CHEST")) {
//                System.out.println("ObjectID." + declaredField.getName() + ",");
//            }
//        }
    }
}
