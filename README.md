# Bookmarks Export Application

A Java-based utility to export bookmarks from a JSON file (GoodLinks format) into a beautiful, hierarchical, and searchable HTML website.

## Features

- **Hierarchical Navigation**: Automatically builds a tree structure from tags using the `/` separator.
- **Fast Search**: Client-side search using a pre-built inverted index (no backend required).
- **Modern UI**: Styled with Tailwind CSS and DaisyUI for a clean, responsive experience.
- **Flexible Formats**: Supports running via Maven (JAR), Docker, or as a GraalVM Native Image.
- **Logging**: Robust logging with Log4j 2.

## Prerequisites

- **Java 25+** (for building and running via Maven)
- **Maven 3.9+**
- **Docker** (optional, for containerized execution)
- **GraalVM** (optional, for building native executables)

## Installation

### 1. Clone the repository

```bash
git clone https://github.com/<YOUR_GITHUB_USERNAME>/GoodLinksToHtml.git
cd GoodLinksToHtml
```

### 2. Build the project

```bash
mvn clean install
```

## Usage

### Option 1: Run with Maven

To convert your bookmarks, specify the input JSON file and the desired output directory:

```bash
mvn exec:java -Dexec.args="--input src/main/resources/GoodLinks-Export.json --output output"
```

### Option 2: Run with Docker

Build and run without needing a local Java installation:

**Build the image:**

```bash
docker build -t bmconverter .
```

**Run the conversion:**

```bash
docker run --rm \
  -v $(pwd)/src/main/resources:/app/input \
  -v $(pwd)/output:/app/output \
  bmconverter
```

### Option 3: GraalVM Native Image

Build a standalone executable (Apple Silicon / Linux / Windows).

**Note**: To build this locally, you **must** have a GraalVM distribution of the JDK installed and set as your `JAVA_HOME`. If you are using SDKMAN, you can install it with:

```bash
sdk install java 22.3.r17-grl # or a newer GraalVM version
sdk use java 22.3.r17-grl
```

**Build command:**

```bash
mvn clean package -Pnative
```

Run the generated binary:

```bash
./target/bmconverter --input src/main/resources/GoodLinks-Export.json --output output
```

**Tip**: If you don't want to install GraalVM locally, use the **Docker** option (Option 2), which handles the GraalVM environment automatically inside the container.

## Configuration

- **Input File**: A JSON file containing an array of bookmarks with `title`, `summary`, `url`, and `tags`.
- **Output**: The application generates a set of HTML files and a `search.js` / `search-index.json` in the specified directory.

## Project Structure

- `src/main/java`: Java source code (Picocli CLI, Pebble Templates).
- `src/main/resources/templates`: HTML templates (Pebble) and search logic.
- `spec.md`: Detailed technical specification of the application.

## License

This project is open-source and available under the MIT License.
