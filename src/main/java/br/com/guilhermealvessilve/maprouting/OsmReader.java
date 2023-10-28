package br.com.guilhermealvessilve.maprouting;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.container.v0_6.NodeContainer;
import org.openstreetmap.osmosis.core.container.v0_6.RelationContainer;
import org.openstreetmap.osmosis.core.container.v0_6.WayContainer;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.TagCollectionImpl;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;

import crosby.binary.osmosis.OsmosisReader;

/**
 * Receives data from the Osmosis pipeline and prints ways which have the
 * 'highway key.
 *
 * Reference:
 *  <a href="https://neis-one.org/2017/10/processing-osm-data-java/">processing-osm-data-java/</a>
 *
 * @author pa5cal
 */
public class OsmReader implements Sink {

    @Override
    public void initialize(Map<String, Object> map) {

    }

    @Override
    public void process(EntityContainer entityContainer) {
        if (entityContainer instanceof NodeContainer nodeContainer) {
            /*
            var tags = nodeContainer.getEntity().getTags();
            if (tags.size() > 0) {
                System.out.println("nodeContainer: " + toString(tags));
            }
             */
        } else if (entityContainer instanceof WayContainer wayContainer) {
            // Vertex
            var way = wayContainer.getEntity();
            processHighway(way.getTags());
        } else if (entityContainer instanceof RelationContainer relationContainer) {
            // Edges
            System.out.println("relationContainer: " + toString(relationContainer.getEntity().getTags()));
        } else {
            System.out.println("Unknown Entity!");
        }
    }

    private void processHighway(Collection<Tag> tags) {
        for (var tag : tags) {
            if ("highway".equalsIgnoreCase(tag.getKey())) {
                System.out.println("wayContainer: " + tag);
            }
        }
    }

    @Override
    public void complete() {

    }

    @Override
    public void close() {

    }

    public static void main(String[] args) throws IOException {
        try (var inputStream = new BufferedInputStream(new FileInputStream("_/graph-databases-in-action/south-america-latest.osm.pbf"))) {
            OsmosisReader reader = new OsmosisReader(inputStream);
            reader.setSink(new OsmReader());
            reader.run();
        }
    }

    private String toString(Collection<Tag> tagCollection) {
        return tagCollection.stream().toList().toString();
    }

    /**
     * 8 = {Tag@1174} "Tag('surface'='asphalt')"
     * 5 = {Tag@1171} "Tag('oneway'='yes')"
     * 1 = {Tag@1167} "Tag('lanes'='2')"
     * 0 = {Tag@1166} "Tag('highway'='motorway')"
     * 3 = {Tag@1169} "Tag('maxspeed'='60')"
     * 6 = {Tag@1172} "Tag('ref'='D025')"
     * 2 = {Tag@1168} "Tag('lit'='yes')"
     * 7 = {Tag@1173} "Tag('source'='Reconocimiento cartográfico de campo 2016 por KG')"
     * 4 = {Tag@1170} "Tag('name'='Avenida Aviador Silvio Pettirossi')"
     */

    public enum Direction {
        FORWARD, BACKWARD, LEFT, RIGHT
    }
}
