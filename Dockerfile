# Stage 1: Build the application
FROM ghcr.io/graalvm/native-image-community:17 AS builder

# Set working directory
WORKDIR /app

# Install Maven
RUN microdnf install maven

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the native image
# Note: --enable-preview is required for Java 25 features if used
RUN mvn -Pnative native:compile -DskipTests

# Stage 2: Create the runtime image
FROM debian:bookworm-slim

WORKDIR /app

# Copy the native executable from the builder stage
COPY --from=builder /app/target/bmconverter /app/bmconverter

# Default input and output locations
RUN mkdir -p /app/input /app/output

# Set the entrypoint to the native executable
ENTRYPOINT ["/app/bmconverter"]

# Default arguments (assuming the user mounts the input file and output directory)
CMD ["--input", "/app/input/GoodLinks-Export.json", "--output", "/app/output"]
