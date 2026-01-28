package org.bmconverter.goodlinks.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TagNode{
  private final List<Bookmark> bookmarks = new ArrayList<>();
  private final Map<String, TagNode> children = new TreeMap<>();
  private final String fullName;
  private final String name;

  public TagNode(String name, String fullName){
    this.name = name;
    this.fullName = fullName;
  }

  public void addBookmark(Bookmark bookmark){
    bookmarks.add(bookmark);
  }

  public List<Bookmark> getBookmarks(){
    return bookmarks;
  }

  public Map<String, TagNode> getChildren(){
    return children;
  }

  public String getFullName(){
    return fullName;
  }

  public String getName(){
    return name;
  }

  public TagNode getOrCreateChild(String name){
    String childFullName = fullName.isEmpty() ? name : fullName + "/" + name;
    return children.computeIfAbsent(name, k -> new TagNode(k, childFullName));
  }

  public String getUrlName(){
    return fullName.isEmpty() ? "index" : fullName.replace("/", "_");
  }

  public boolean isParentOf(String otherFullName){
    if(otherFullName == null){
      return false;
    }
    if(this.fullName.isEmpty()){
      return true;
    }
    return otherFullName.startsWith(this.fullName + "/") || otherFullName.equals(this.fullName);
  }
}
