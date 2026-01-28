package org.bmconverter.goodlinks.service;

import org.bmconverter.goodlinks.model.Bookmark;
import org.bmconverter.goodlinks.model.TagNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class BookmarkService{
  private final ObjectMapper objectMapper = new ObjectMapper();

  public TagNode buildTagHierarchy(List<Bookmark> bookmarks){
    TagNode root = new TagNode("Root", "");
    for(Bookmark bookmark : bookmarks){
      if(bookmark.tags() == null || bookmark.tags().isEmpty()){
        continue;
      }
      List<String> filteredTags = bookmark.tags().stream()
                                          .filter(tag -> !tag.equalsIgnoreCase("index"))
                                          .distinct()
                                          .toList();

      if(filteredTags.isEmpty()){
        continue;
      }

      for(String tag : filteredTags){
        String[] parts = tag.split("/");
        TagNode current = root;
        for(String part : parts){
          current = current.getOrCreateChild(part);
        }
        // Only add to the leaf node of this specific tag string
        if(!current.getBookmarks().contains(bookmark)){
          current.addBookmark(bookmark);
        }
      }
    }
    sortTagNodeBookmarks(root);
    return root;
  }

  public List<Bookmark> loadBookmarks(File jsonFile) throws IOException{
    List<Bookmark> bookmarks = objectMapper.readValue(jsonFile, new TypeReference<List<Bookmark>>(){
    });
    return bookmarks.stream()
                    .filter(b -> b.tags() != null && !b.tags().isEmpty())
                    .filter(b -> b.tags().stream().anyMatch(t -> !t.equalsIgnoreCase("index")))
                    .sorted((b1, b2) -> b1.title().compareToIgnoreCase(b2.title()))
                    .toList();
  }

  private void sortTagNodeBookmarks(TagNode node){
    node.getBookmarks().sort((b1, b2) -> b1.title().compareToIgnoreCase(b2.title()));
    for(TagNode child : node.getChildren().values()){
      sortTagNodeBookmarks(child);
    }
  }
}
