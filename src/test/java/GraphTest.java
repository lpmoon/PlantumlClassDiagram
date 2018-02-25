import com.lpmoon.plantuml.classdiagram.graph.Graph;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class GraphTest {

    @Test
    public void testSingle() {
        Graph<String> graph = new Graph<>();
        graph.add("a");
        graph.add("b");
        graph.add("c");

        Assert.assertEquals(1, graph.findConnectedNodes("a", true, true).size());
    }

    @Test
    public void testConnected() {
        Graph<String> graph = new Graph<>();
        graph.add("a");
        graph.add("b");
        graph.add("c");
        graph.add("d");

        graph.connect("a", "b");
        graph.connect("a", "c");

        List<String> nodes = graph.findConnectedNodes("a", true, true);
        Collections.sort(nodes);

        Assert.assertEquals(nodes.toArray(), new String[]{"a", "b", "c"});
        Assert.assertEquals(3, graph.findConnectedNodes("a", true, true).size());
        Assert.assertEquals(1, graph.findConnectedNodes("d", true, true).size());
    }

    @Test
    public void testConnected2() {
        Graph<String> graph = new Graph<>();
        graph.add("a");

        for (int i = 0; i < 10; i++) {
            graph.connect("a", "a" + i);
            for (int j = 0; j < 10; j++) {
                graph.connect("a" + i, ("a" + i) + j);
            }
        }


        List<String> nodes = graph.findConnectedNodes("a", true, true);

        Assert.assertEquals(111, graph.findConnectedNodes("a",true, true).size());
    }
}
