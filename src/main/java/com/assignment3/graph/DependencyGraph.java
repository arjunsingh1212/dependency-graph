package com.assignment3.graph;

import com.assignment3.exceptions.GraphException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"PMD.CommentRequired","PMD.SystemPrintln"})
public class DependencyGraph {

  // These collections is used later in finding the ancestors,
  // descendants and checking cycle.
  private List<Integer> tempIDsArray;
  private Map<Integer, Boolean> visited = new ConcurrentHashMap<>();

  // nodeDetails will keep the information of all the nodes.
  // This helps in using only the nodeID to form the graph.
  private final Map<Integer, Node> nodeDetails = new ConcurrentHashMap<>();

  private final Map<Integer, List<Integer>> graph = new ConcurrentHashMap<>();

  // revGraph keeps the child to parent relation information
  private final Map<Integer, List<Integer>> revGraph = new ConcurrentHashMap<>();

  private static final BufferedReader BUFFERED_READER =
          new BufferedReader(new InputStreamReader(System.in));

  public Map<Integer, List<Integer>> getGraph() {
    return graph;
  }

  public Map<Integer, List<Integer>> getRevGraph() {
    return revGraph;
  }

  public Map<Integer, Node> getNodeDetails() {
    return nodeDetails;
  }

  // This method checks if the node ID is already assigned or not
  private boolean isIDPresent(final Integer nodeID) {
    return nodeDetails.get(nodeID) != null;
  }

  // ----------------------------------------------------------------------------------
  // This is API 1 - Finds the immediate parents of the node with passed Node ID

  public List<Node> immediateParents(final Integer nodeID) throws GraphException {
    final List<Node> result = new ArrayList<>();
    if (isIDPresent(nodeID)) {
      // We get the parent's node ID using the revGraph
      for (final Integer parent : revGraph.get(nodeID)) {
        // Using the node ID, we extract the node details from nodeDetails Array
        result.add(nodeDetails.get(parent));
      }
    } else {
      throw new GraphException("Node with node id " + nodeID
              + " is not present.");
    }
    return result;
  }


  // ----------------------------------------------------------------------------------
  // This is API 2 - Finds the immediate children of the node with passed Node ID

  public List<Node> immediateChildren(final Integer nodeID) throws GraphException {
    final List<Node> result = new ArrayList<>();
    if (isIDPresent(nodeID)) {
      // We get the children's node ID using the graph
      for (final Integer child : graph.get(nodeID)) {
        // Using the node ID, we extract the node details from nodeDetails Array
        result.add(nodeDetails.get(child));
      }
    } else {
      throw new GraphException("Node with node id " + nodeID
              + " is not present.");
    }
    return result;
  }

  // ----------------------------------------------------------------------------------
  // This is Utility function
  // It fills the tempIDsArray with all ancestors or descendants ID

  private void utilForAncestorDescendant(
          final Integer nodeID, final Map<Integer, List<Integer>> genericGraph) {
    // Using the DFS approach
    for (final Integer node : genericGraph.get(nodeID)) {
      if (visited.get(node) == null) {
        visited.put(node, true);
        tempIDsArray.add(node);
        utilForAncestorDescendant(node, genericGraph);
      }
    }
  }

  // ----------------------------------------------------------------------------------
  // This is API 3 - Finds the ancestors of the node with passed Node ID

  public List<Node> ancestors(final Integer nodeID) throws GraphException {
    final List<Node> result = new ArrayList<>();
    if (isIDPresent(nodeID)) {
      tempIDsArray = new ArrayList<>();
      visited = new HashMap<>();
      // Populating the tempIDsArray with the ancestors IDs
      utilForAncestorDescendant(nodeID, revGraph);
      for (final Integer ancestorId : tempIDsArray) {
        result.add(nodeDetails.get(ancestorId));
      }
    } else {
      throw new GraphException("Node with node ID " + nodeID + " is not present.");
    }
    return result;
  }

  // ----------------------------------------------------------------------------------
  // This is API 4 - Finds the descendants of the node with passed Node ID

  public List<Node> descendants(final Integer nodeID) throws GraphException {
    final List<Node> result = new ArrayList<>();
    if (isIDPresent(nodeID)) {
      tempIDsArray = new ArrayList<>();
      visited = new HashMap<>();
      // Populating the tempIDsArray with the ancestors IDs
      utilForAncestorDescendant(nodeID, graph);
      for (final Integer descendantId : tempIDsArray) {
        result.add(nodeDetails.get(descendantId));
      }
    } else {
      throw new GraphException("Node with node ID " + nodeID + " is not present.");
    }
    return result;
  }

  // ----------------------------------------------------------------------------------
  // This is API 5 - Deletes the dependency of graph with passed parentID and childID

  public void deleteDependency(final Integer parentID, final Integer childID) throws GraphException {
    if (!isIDPresent(parentID) || !isIDPresent(childID)) {
      throw new GraphException("Either parent id or child id is not present.");
    }
    if (!graph.get(parentID).contains(childID)) {
      throw new GraphException("This dependency is not present.");
    }
    graph.get(parentID).remove(childID);
    revGraph.get(childID).remove(parentID);
  }

  // ----------------------------------------------------------------------------------
  // This is API 6 - Deletes the node of the graph with passed Node ID

  public void deleteNode(final Integer nodeID) throws GraphException {
    if (isIDPresent(nodeID)) {
      nodeDetails.remove(nodeID);
      graph.remove(nodeID);
      // Removing all the outgoing edges
      for (final Map.Entry<Integer, List<Integer>> entry : graph.entrySet()) {
        if (entry.getValue().contains(nodeID)) {
          graph.get(entry.getKey()).remove(nodeID);
        }
      }
      // Removing all the incoming edges
      for (final Map.Entry<Integer, List<Integer>> entry : revGraph.entrySet()) {
        if (entry.getValue().contains(nodeID)) {
          revGraph.get(entry.getKey()).remove(nodeID);
        }
      }
    } else {
      throw new GraphException("Node with node ID " + nodeID + " is not present.");
    }
  }

  // ----------------------------------------------------------------------------------
  // Utility function that finds out whether the parentID node and childID node will
  // form cycle in the graph or not. (Using DFS approach)

  /**
  private boolean isCyclic(final Integer parentID, final Integer childID) {
    boolean isCurrentNodeParent = false;
    boolean isNextNodeParent = false;
    for (final Integer node : graph.get(childID)) {
      if (node.equals(parentID)) {
        isCurrentNodeParent = true;
      } else if (visited.get(node) == null) {
        visited.put(node, true);
        isNextNodeParent = isCyclic(parentID, node);
      }
    }
    return isCurrentNodeParent || isNextNodeParent;
  }
  */


  private boolean isCyclic(final Integer parentID, final Integer childID) {
    boolean isCyclePresent = false;
    for (final Integer node : graph.get(childID)) {
      if (node.equals(parentID)) {
        isCyclePresent = true;
      } else if (visited.get(node) == null) {
        visited.put(node, true);
        isCyclePresent = isCyclic(parentID, node);
      }
    }
    return isCyclePresent;
  }

  // ----------------------------------------------------------------------------------
  // This is API 7 - Adds a dependency with passed parentID and childID avoiding cycles

  public void addDependency(final Integer parentID, final Integer childID)
          throws GraphException {
    if (!isIDPresent(parentID) || !isIDPresent(childID)) {
      throw new GraphException("Either parent id or child id is not present.");
    }
    if (graph.get(parentID).contains(childID)) {
      throw new GraphException("Dependency already present.");
    }
    if (parentID.equals(childID)) {
      throw new GraphException("parent and child id same. Can't form the loop edge.");
    }
    visited = new HashMap<>();
    if (isCyclic(parentID, childID)) {
      throw new GraphException("It will lead to a cyclic dependency.");
    }
    graph.get(parentID).add(childID);
    revGraph.get(childID).add(parentID);
  }

  // ----------------------------------------------------------------------------------
  // This is API 8 - Adds the node with passed Node ID, Node Name and additional info

  public void addNode(final Integer nodeID, final String nodeName,
                      final Map<String, String> additionalInfo)
          throws GraphException {

    if (isIDPresent(nodeID)) {
      throw new GraphException("Node is Already Present.");
    }
    final Node node = new Node(nodeID, nodeName, additionalInfo);
    nodeDetails.put(nodeID, node);
    graph.put(nodeID, new ArrayList<>());
    revGraph.put(nodeID, new ArrayList<>());
  }

  // ----------------------------------------------------------------------------------
  // This is back-up method to add node with out passing details in the arguments
  public void addNode() throws IOException, GraphException {
    System.out.print("Enter the node ID of the node (unique) :");
    final int nodeID = Integer.parseInt(BUFFERED_READER.readLine());

    if (isIDPresent(nodeID)) {
      throw new GraphException("Node is Already Present.");
    }

    System.out.print("Enter the Name of the node :");
    final String nodeName = BUFFERED_READER.readLine();
    final Map<String, String> additionalInfo = new ConcurrentHashMap<>();
    while (true) {
      System.out.print(
              "Do you want to enter any additional information in form "
                      + "of Key-Value pair (y/n)? : ");
      final String response = BUFFERED_READER.readLine();
      if ("y".equals(response)) {
        System.out.print("Enter the key : ");
        final String key = BUFFERED_READER.readLine();
        System.out.print("Enter the value : ");
        final String value = BUFFERED_READER.readLine();
        additionalInfo.put(key, value);
      } else {
        break;
      }
    }
    final Node node = new Node(nodeID, nodeName, additionalInfo);
    nodeDetails.put(nodeID, node);
    graph.put(nodeID, new ArrayList<>());
    revGraph.put(nodeID, new ArrayList<>());
  }
}
