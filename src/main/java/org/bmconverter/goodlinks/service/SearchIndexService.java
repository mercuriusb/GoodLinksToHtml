package org.bmconverter.goodlinks.service;

import org.bmconverter.goodlinks.model.Bookmark;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class SearchIndexService{
  private final ObjectMapper objectMapper = new ObjectMapper();

  public void generateIndex(List<Bookmark> bookmarks, File outputFile) throws IOException{
    Map<String, Set<Integer>> index = new HashMap<>();

    for(int i = 0; i < bookmarks.size(); i++){
      Bookmark b = bookmarks.get(i);
      Set<String> tokens = tokenize(b.title() + " " + (b.summary() != null ? b.summary() : ""));
      for(String token : tokens){
        index.computeIfAbsent(token, k -> new HashSet<>()).add(i);
      }
    }

    Map<String, Object> result = new HashMap<>();
    result.put("bookmarks", bookmarks);
    result.put("index", index);

    objectMapper.writeValue(outputFile, result);
  }

  private Set<String> tokenize(String text){
    if(text == null){
      return Collections.emptySet();
    }
    String[] parts = text.toLowerCase().split("\\W+");
    Set<String> tokens = new HashSet<>();
    for(String part : parts){
      if(part.length() > 2){
        tokens.add(part);
      }
    }
    return tokens;
  }
}
