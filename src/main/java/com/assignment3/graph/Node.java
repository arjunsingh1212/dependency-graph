package com.assignment3.graph;

import java.util.Map;

@SuppressWarnings("PMD.CommentRequired")
public class Node implements Comparable<Node> {
  private Integer nodeID;
  private String nodeName;
  private Map<String, String> additionalInfo;

  public Node(final Integer nodeID, final String nodeName,
              final Map<String, String> additionalInfo) {
    this.nodeID = nodeID;
    this.nodeName = nodeName;
    this.additionalInfo = additionalInfo;
  }

  public Integer getNodeID() {
    return nodeID;
  }

  public void setNodeID(final Integer nodeID) {
    this.nodeID = nodeID;
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNodeName(final String nodeName) {
    this.nodeName = nodeName;
  }

  public Map<String, String> getAdditionalInfo() {
    return additionalInfo;
  }

  public void setAdditionalInfo(final Map<String, String> additionalInfo) {
    this.additionalInfo = additionalInfo;
  }

  @Override
  public String toString() {
    return "Node{"
            + "nodeID=" + nodeID
            + ", nodeName='" + nodeName + '\''
            + ", additionalInfo=" + additionalInfo
            + '}';
  }

  @Override
  public int compareTo(final Node obj) {
    int res;
    if (nodeID > obj.nodeID) {
      res = 1;
    } else if (nodeID < obj.nodeID) {
      res = -1;
    } else {
      res = 0;
    }
    return res;
  }
}
