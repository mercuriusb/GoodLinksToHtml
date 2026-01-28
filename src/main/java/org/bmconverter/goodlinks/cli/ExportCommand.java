package org.bmconverter.goodlinks.cli;

import org.bmconverter.goodlinks.model.Bookmark;
import org.bmconverter.goodlinks.model.TagNode;
import org.bmconverter.goodlinks.service.BookmarkService;
import org.bmconverter.goodlinks.service.ExportService;
import org.bmconverter.goodlinks.service.SearchIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "bookmarks-export", mixinStandardHelpOptions = true, version = "1.0",
    description = "Export bookmarks JSON to hierarchical HTML pages.")
public class ExportCommand implements Callable<Integer>{
  private static final Logger logger = LoggerFactory.getLogger(ExportCommand.class);

  @Option(names = {"-i", "--input"}, description = "Input JSON file", defaultValue = "src/main/resources/GoodLinks-Export.json")
  private File inputFile;

  @Option(names = {"-o", "--output"}, description = "Output directory", defaultValue = "output")
  private File outputDir;

  @Override
  public Integer call() throws Exception{
    if(!inputFile.exists()){
      logger.error("Input file not found: {}", inputFile.getAbsolutePath());
      return 1;
    }

    if(!outputDir.exists()){
      outputDir.mkdirs();
    }

    BookmarkService bookmarkService = new BookmarkService();
    SearchIndexService searchIndexService = new SearchIndexService();
    ExportService exportService = new ExportService();

    logger.info("Loading bookmarks...");
    List<Bookmark> bookmarks = bookmarkService.loadBookmarks(inputFile);

    logger.info("Building tag hierarchy...");
    TagNode root = bookmarkService.buildTagHierarchy(bookmarks);

    logger.info("Generating search index...");
    searchIndexService.generateIndex(bookmarks, new File(outputDir, "search-index.json"));

    logger.info("Exporting HTML pages...");
    exportService.export(bookmarks, root, outputDir);

    logger.info("Export completed successfully to: {}", outputDir.getAbsolutePath());
    return 0;
  }

  public static void main(String[] args){
    int exitCode = new CommandLine(new ExportCommand()).execute(args);
    System.exit(exitCode);
  }
}
