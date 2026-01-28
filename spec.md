# Specifications for Bookmarks Export Application

## Overview

Create a Java application that exports bookmarks from a JSON file into a hierarchical HTML website.

## Core Requirements

1. **Platform**: Developed using Java 17+.
2. **Interface**: Provide a CLI for interacting with the application.
3. **Data Source**: Parse `GoodLinks-Export.json` from `src/main/resources`.
4. **Data Content**: The JSON contains bookmarks with a title, summary (description), and tags.
5. **Filtering**:
    - Bookmarks without any tags must be ignored.
    - The tag "index" must be ignored and not displayed in navigation.
    - Bookmarks that only have the "index" tag must be ignored.
6. **Output**: Generate HTML pages listing bookmarks. The application title should be "Bookmarks".
7. **Start Page**: The `index.html` page must not contain any bookmark list or tags. It should only show the navigation bar and search field.

## User Interface (HTML/CSS)

1. **Framework**: Use Tailwind CSS and DaisyUI for styling.
2. **Layout**:
    - A persistent navigation bar on the left.
    - A list of bookmarks for the currently selected tag on the right.
3. **Bookmark Listing**:
    - Displayed as a simple list (not cards).
    - Sorted by title (case-insensitive).
    - Format: **Title** followed by the description on the same line.
4. **Search**:
    - A search box at the top of every page.
    - Searches across titles and descriptions.
    - Pure client-side JavaScript execution using a pre-generated inverted index (created during the export process).

## Tag Hierarchy & Navigation

1. **Structure**: Tags use `/` as a separator to define a parent-child hierarchy.
2. **Navigation Tree**: The left sidebar must display this hierarchy as a tree.
3. **Folder Behavior**:
    - Folder tags (parent tags) are clickable.
    - Clicking a folder tag displays only the bookmarks explicitly tagged with that folder (excluding bookmarks tagged with sub-tags).
    - Clicking a folder should also expand/collapse its children in the navigation bar.
4. **Active State & Persistence**:
    - When a tag is selected, its parent folders in the sidebar must remain open.
    - The currently selected tag must be visualized with **bold text in a primary color**, without any background highlight or rectangular border.

## Technical Details

1. **Package Structure**: The project uses the base package `org.bmconverter.goodlinks`.
2. **Logging**: Uses **SLF4J** with a simple backend for application logging (optimized for GraalVM).
3. **Template Engine**: Use a template engine (e.g., Pebble) that allows users to modify the HTML structure.
4. **Index Generation**: The search index must be generated during the export process.
5. **Docker Support**:
    - Provide a `Dockerfile` for containerized execution.
    - The image should use a multi-stage build to produce a minimal runtime environment.
6. **Native Executable**:
    - Support building a native executable using GraalVM (Native Image).
    - Ensure compatibility with Apple Silicon (AArch64) and Linux (x86_64).
7. **Project Metadata**:
    - Group ID: `org.bmconverter`
    - Artifact ID: `GoodLinksToHtml`
8. **Development Support**: Includes a comprehensive `.gitignore` supporting IntelliJ IDEA, Eclipse, and VS Code.

