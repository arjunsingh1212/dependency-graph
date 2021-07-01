package com.assignment3;

import com.assignment3.exceptions.GraphException;
import com.assignment3.graph.DependencyGraph;
import com.assignment3.graph.Node;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@SuppressWarnings({"PMD.CommentRequired", "PMD.SystemPrintln"})
public final class Runner {

  private static final BufferedReader BUFFERED_READER = new BufferedReader(
          new InputStreamReader(System.in));

  private static final String MENU = "Choose an Option\n"
          + "1. Get Immediate Parents\n"
          + "2. Get Immediate Children\n"
          + "3. Get Ancestors\n"
          + "4. Get Descendants\n"
          + "5. Delete Dependency\n"
          + "6. Delete Node\n"
          + "7. Add Dependency\n"
          + "8. Add Node\n"
          + "9. Exit\n"
          + "Enter your choice : ";

  private Runner() {
  }

  private static void immediateParentsUtil(
          final DependencyGraph graph) throws IOException {
    System.out.print("Enter Node ID :");
    final int nodeID = Integer.parseInt(BUFFERED_READER.readLine());
    try {
      final List<Node> parents = graph.immediateParents(nodeID);
      System.out.println(parents.toString());
    } catch (GraphException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void immediateChildrenUtil(
          final DependencyGraph graph) throws IOException {
    System.out.print("Enter Node ID :");
    final int nodeID = Integer.parseInt(BUFFERED_READER.readLine());
    try {
      final List<Node> children = graph.immediateChildren(nodeID);
      System.out.println(children.toString());
    } catch (GraphException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void ancestorsUtil(
          final DependencyGraph graph) throws IOException {
    System.out.print("Enter Node ID :");
    final int nodeID = Integer.parseInt(BUFFERED_READER.readLine());
    try {
      final List<Node> children = graph.ancestors(nodeID);
      System.out.println(children.toString());
    } catch (GraphException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void descendantsUtil(
          final DependencyGraph graph) throws IOException {
    System.out.print("Enter Node ID :");
    final int nodeID = Integer.parseInt(BUFFERED_READER.readLine());
    try {
      final List<Node> children = graph.descendants(nodeID);
      System.out.println(children.toString());
    } catch (GraphException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void deleteDependencyUtil(
          final DependencyGraph graph) throws IOException {
    System.out.print("Enter Parent ID :");
    final int parentID = Integer.parseInt(BUFFERED_READER.readLine());
    System.out.print("Enter Child ID :");
    final int childID = Integer.parseInt(BUFFERED_READER.readLine());
    try {
      graph.deleteDependency(parentID, childID);
      System.out.println("Deleted Successfully");
    } catch (GraphException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void deleteNodeUtil(
          final DependencyGraph graph) throws IOException {
    System.out.print("Enter Node ID :");
    final int nodeID = Integer.parseInt(BUFFERED_READER.readLine());
    try {
      graph.deleteNode(nodeID);
      System.out.println("Deleted Successfully");
    } catch (GraphException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void addDependencyUtil(
          final DependencyGraph graph) throws IOException {
    System.out.print("Enter Parent ID :");
    final int parentID = Integer.parseInt(BUFFERED_READER.readLine());
    System.out.print("Enter Child ID :");
    final int childID = Integer.parseInt(BUFFERED_READER.readLine());
    try {
      graph.addDependency(parentID, childID);
      System.out.println("Added Successfully");
    } catch (GraphException e) {
      System.out.println(e.getMessage());
    }
  }

  private static void addNodeUtil(
          final DependencyGraph graph) throws IOException {
    try {
      graph.addNode();
      System.out.println("Added Successfully");
    } catch (GraphException e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(final String[] args) throws IOException {
    final DependencyGraph graph = new DependencyGraph();
    boolean loop = true;
    int option = 0;
    while (loop) {
      System.out.print(MENU);
      try {
        option = Integer.parseInt(BUFFERED_READER.readLine());
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      switch (option) {
        case 1:
          immediateParentsUtil(graph);
          break;
        case 2:
          immediateChildrenUtil(graph);
          break;
        case 3:
          ancestorsUtil(graph);
          break;
        case 4:
          descendantsUtil(graph);
          break;
        case 5:
          deleteDependencyUtil(graph);
          break;
        case 6:
          deleteNodeUtil(graph);
          break;
        case 7:
          addDependencyUtil(graph);
          break;
        case 8:
          addNodeUtil(graph);
          break;
        case 9:
          loop = false;
          break;
        default:
          System.out.println("Please enter a valid option!");
          break;
      }
    }
  }
}
