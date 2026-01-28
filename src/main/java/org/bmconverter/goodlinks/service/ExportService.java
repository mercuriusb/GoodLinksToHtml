package org.bmconverter.goodlinks.service;

import org.bmconverter.goodlinks.model.Bookmark;
import org.bmconverter.goodlinks.model.TagNode;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;

import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportService{
  private final PebbleEngine engine = new PebbleEngine.Builder().build();

  public void export(List<Bookmark> bookmarks, TagNode root, File outputDir) throws IOException{
    if(!outputDir.exists()){
      outputDir.mkdirs();
    }

    // Copy search.js to output directory
    copyResource("/templates/search.js", new File(outputDir, "search.js"));

    PebbleTemplate template = engine.getTemplate("templates/base.html");

    // Generate index.html (Root) - empty of bookmarks
    generatePage(template, root, "Bookmarks", List.of(), null, new File(outputDir, "index.html"));

    // Generate pages for each tag
    generateTagPages(template, root, root, outputDir);
  }

  private void copyResource(String resourcePath, File destination) throws IOException{
    try(InputStream is = getClass().getResourceAsStream(resourcePath)){
      if(is == null){
        throw new FileNotFoundException("Resource not found: " + resourcePath);
      }
      Files.copy(is, destination.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }
  }

  private void generatePage(PebbleTemplate template, TagNode root, String title, List<Bookmark> bookmarks, TagNode current, File outputFile) throws IOException{
    Map<String, Object> context = new HashMap<>();
    context.put("title", title);
    context.put("currentTagName", current != null ? current.getFullName() : "");
    context.put("bookmarks", bookmarks);
    context.put("rootTag", root);
    context.put("navigationHtml", renderNavigation(root, current != null ? current.getFullName() : ""));

    try(OutputStream os = new FileOutputStream(outputFile);
        Writer writer = new OutputStreamWriter(os, "UTF-8")){
      template.evaluate(writer, context);
    }
  }

  private void generateTagPages(PebbleTemplate template, TagNode root, TagNode current, File outputDir) throws IOException{
    for(TagNode child : current.getChildren().values()){
      String fileName = child.getFullName().replace("/", "_") + ".html";
      generatePage(template, root, child.getFullName(), child.getBookmarks(), child, new File(outputDir, fileName));
      generateTagPages(template, root, child, outputDir);
    }
  }

  private String renderNavigation(TagNode node, String currentTagName){
    StringBuilder sb = new StringBuilder();
    for(TagNode child : node.getChildren().values()){
      sb.append("<li>");
      String activeClass = child.getFullName().equals(currentTagName) ? "active-tag" : "";
      if(child.getChildren().isEmpty()){
        sb.append(String.format("<a class=\"%s\" href=\"%s.html\">%s</a>", activeClass, child.getUrlName(), child.getName()));
      }else{
        String openAttr = child.isParentOf(currentTagName) ? "open" : "";
        sb.append(String.format("<details %s>", openAttr));
        sb.append(String.format("<summary><a class=\"%s\" href=\"%s.html\">%s</a></summary>", activeClass, child.getUrlName(), child.getName()));
        sb.append("<ul>");
        sb.append(renderNavigation(child, currentTagName));
        sb.append("</ul>");
        sb.append("</details>");
      }
      sb.append("</li>");
    }
    return sb.toString();
  }
}
