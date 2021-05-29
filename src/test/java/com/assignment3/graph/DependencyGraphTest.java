package com.assignment3.graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.assignment3.exceptions.GraphException;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class DependencyGraphTest {

  private DependencyGraph graph;
  private final Map<String, String> additionalDetails = new HashMap<>();

  @BeforeEach
  void init() throws GraphException {
    graph = new DependencyGraph();
    graph.addNode(1, "one", additionalDetails);
    graph.addNode(2, "two", additionalDetails);
    graph.addNode(3, "three", additionalDetails);
    graph.addNode(4, "four", additionalDetails);
    graph.addNode(5, "five", additionalDetails);
    graph.addNode(6, "six", additionalDetails);
    graph.addDependency(1, 2);
    graph.addDependency(2, 3);
    graph.addDependency(1, 3);
    graph.addDependency(3, 4);
    graph.addDependency(4, 5);
  }

  @Nested
  class ImmediateParents {
    @Nested
    class CorrectTests {
      @Test
      @DisplayName("Getting Immediate Parents")
      void immediateParentsTest1() throws GraphException {
        List<Node> expected = new ArrayList<>();
        Node node1 = new Node(1, "one", additionalDetails);
        Node node2 = new Node(2, "two", additionalDetails);
        expected.add(node1);
        expected.add(node2);
        List<Node> actual = graph.immediateParents(3);
        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected.toString(), actual.toString());
      }

      @Test
      @DisplayName("Getting Immediate Parents, but no dependencies present")
      void immediateParentsTest2() throws GraphException {
        ArrayList<Node> expected = new ArrayList<>();
        List<Node> actual = graph.immediateParents(6);
        assertEquals(expected.toString(), actual.toString());
      }
    }

    @Nested
    class ExceptionTests {
      @Test
      @DisplayName("Getting Immediate Parents, node id not present")
      void immediateParentsTest1() {
        assertThrows(GraphException.class, () -> graph.immediateParents(7));
      }
    }
  }

  @Nested
  class ImmediateChildren {
    @Nested
    class CorrectTests {
      @Test
      @DisplayName("Getting Immediate Children")
      void immediateChildrenTest1() throws GraphException {
        List<Node> expected = new ArrayList<>();
        Node node1 = new Node(2, "two", additionalDetails);
        Node node2 = new Node(3, "three", additionalDetails);
        expected.add(node1);
        expected.add(node2);
        List<Node> actual = graph.immediateChildren(1);
        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected.toString(), actual.toString());
      }

      @Test
      @DisplayName("Getting Immediate Children, but no dependencies present")
      void immediateChildrenTest2() throws GraphException {
        List<Node> expected = new ArrayList<>();
        List<Node> actual = graph.immediateParents(6);
        assertEquals(expected.toString(), actual.toString());
      }
    }

    @Nested
    class ExceptionTests {
      @Test
      @DisplayName("Getting Immediate Parents, node id not present")
      void immediateChildrenTest1() {
        assertThrows(GraphException.class, () -> graph.immediateParents(7));
      }
    }
  }

  @Nested
  class Ancestors {
    @Nested
    class CorrectTests {
      @Test
      @DisplayName("Getting All Ancestors")
      void ancestorsTest1() throws GraphException {
        List<Node> expected = new ArrayList<>();
        Node node1 = new Node(2, "two", additionalDetails);
        Node node2 = new Node(3, "three", additionalDetails);
        Node node3 = new Node(1, "one", additionalDetails);
        expected.add(node1);
        expected.add(node2);
        expected.add(node3);
        List<Node> actual = graph.ancestors(4);
        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected.toString(), actual.toString());
      }

      @Test
      @DisplayName("Getting ancestors, but no dependencies present")
      void ancestorsTest2() throws GraphException {
        List<Node> expected = new ArrayList<>();
        List<Node> actual = graph.ancestors(6);
        assertEquals(expected.toString(), actual.toString());
      }
    }

    @Nested
    class ExceptionTests {
      @Test
      @DisplayName("Getting Ancestors, node id not present")
      void ancestorsTest1() {
        assertThrows(GraphException.class, () -> graph.ancestors(7));
      }
    }
  }

  @Nested
  class Descendants {
    @Nested
    class CorrectTests {
      @Test
      @DisplayName("Getting All Descendants")
      void descendantsTest1() throws GraphException {
        List<Node> expected = new ArrayList<>();
        Node node1 = new Node(4, "four", additionalDetails);
        Node node2 = new Node(5, "five", additionalDetails);
        expected.add(node1);
        expected.add(node2);
        List<Node> actual = graph.descendants(3);
        Collections.sort(expected);
        Collections.sort(actual);
        assertEquals(expected.toString(), actual.toString());
      }

      @Test
      @DisplayName("Getting Descendants, but no dependencies present")
      void descendantsTest2() throws GraphException {
        List<Node> expected = new ArrayList<>();
        List<Node> actual = graph.descendants(6);
        assertEquals(expected.toString(), actual.toString());
      }
    }

    @Nested
    class ExceptionTests {
      @Test
      @DisplayName("Getting Ancestors, node id not present")
      void descendantsTest1() {
        assertThrows(GraphException.class, () -> graph.descendants(7));
      }
    }
  }

  @Nested
  class DeleteDependency {
    @Nested
    class CorrectTests {
      @Test
      @DisplayName("Deleting dependency")
      void deleteDependencyTest() throws GraphException {
        graph.deleteDependency(3, 4);
        assertFalse(graph.getGraph().get(3).contains(4));
        assertFalse(graph.getRevGraph().get(4).contains(3));
      }
    }

    @Nested
    class ExceptionTests {
      @Test
      @DisplayName("Deleting dependency, node id not present")
      void deleteDependencyTest1() {
        assertThrows(GraphException.class, () -> graph.deleteDependency(3, 7));
      }

      @Test
      @DisplayName("Deleting dependency, dependency not present")
      void deleteDependencyTest2() {
        assertThrows(GraphException.class, () -> graph.deleteDependency(3, 5));
      }
    }
  }

  @Nested
  class DeleteNode {
    @Nested
    class CorrectTests {
      @Test
      @DisplayName("Deleting Node")
      void deleteNodeTest() throws GraphException {
        graph.deleteNode(3);
        assertFalse(graph.getGraph().get(1).contains(3));
        assertFalse(graph.getGraph().get(2).contains(3));
        assertFalse(graph.getRevGraph().get(4).contains(3));
      }
    }

    @Nested
    class ExceptionTests {
      @Test
      @DisplayName("Deleting node, node id not present")
      void deleteNodeTest1() {
        assertThrows(GraphException.class, () -> graph.deleteNode(7));
      }
    }
  }

  @Nested
  class AddDependency {
    @Nested
    class CorrectTests {
      @Test
      @DisplayName("Simple dependency addition")
      void addDependencyTest() throws GraphException {
        graph.addDependency(1, 4);
        assertTrue(graph.getGraph().get(1).contains(4));
        assertTrue(graph.getRevGraph().get(4).contains(1));
      }
    }

    @Nested
    class ExceptionTests {
      @Test
      @DisplayName("Dependency Addition, Node ID not present")
      void addDependencyTest1() {
        assertThrows(GraphException.class, () -> graph.addDependency(4, 7));
      }

      @Test
      @DisplayName("Dependency Addition, Forming multi-edge (Dependency already present)")
      void addDependencyTest2() {
        assertThrows(GraphException.class, () -> graph.addDependency(1, 2));
      }

      @Test
      @DisplayName("Dependency Addition, Forming loop-edge (Same parent child)")
      void addDependencyTest3() {
        assertThrows(GraphException.class, () -> graph.addDependency(1, 1));
      }

      @Test
      @DisplayName("Dependency Addition, Forming cycle 1")
      void addDependencyTest4() {
        assertThrows(GraphException.class, () -> graph.addDependency(5, 1));
      }

      @Test
      @DisplayName("Dependency Addition, Forming cycle 2")
      void addDependencyTest5() {
        assertThrows(GraphException.class, () -> graph.addDependency(4, 2));
      }

      @Test
      @DisplayName("Dependency Addition, Forming cycle 3")
      void addDependencyTest6() {
        assertThrows(GraphException.class, () -> graph.addDependency(3, 1));
      }
    }
  }

  @Nested
  class AddNode {
    @Nested
    class CorrectTests {
      @Test
      @DisplayName("Simple Node addition")
      void addNodeTest() throws GraphException {
        graph.addNode(7, "seven", additionalDetails);
        assertTrue(graph.getNodeDetails().containsKey(7));
        assertTrue(graph.getGraph().containsKey(7));
        assertTrue(graph.getGraph().get(7).isEmpty());
        assertTrue(graph.getRevGraph().containsKey(7));
        assertTrue(graph.getRevGraph().get(7).isEmpty());
      }
    }

    @Nested
    class ExceptionTests {
      @Test
      @DisplayName("Node Addition, Node ID already present")
      void addNodeTest1() {
        assertThrows(GraphException.class, () -> graph.addNode(4, "Hello 4", additionalDetails));
      }
    }
  }
}