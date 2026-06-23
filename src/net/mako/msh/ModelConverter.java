package net.mako.msh;

import net.mako.rendering.Mesh;
import net.mako.rendering.Triangle;
import net.mako.rendering.Vertex;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ModelConverter {

    public File file;
    public FormatType formatType;

    public ModelConverter(File file, FormatType formatType) {
        this.file = file;
        this.formatType = formatType;
    }

    public Mesh getMesh() {

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            List<String> lines = br.readAllLines();

            if (formatType == FormatType.BASIC_T) {
                return parseBasic(lines);
            }

            if (formatType == FormatType.INDEXED_T) {
                return parseIndexed(lines);
            }

            return new Mesh(new ArrayList<>());

        } catch (IOException e) {
            throw new RuntimeException("Failed to load msh file.", e);
        }
    }

    private Mesh parseBasic(List<String> lines) {

        List<Triangle> triangles = new ArrayList<>();
        List<Vertex> currentVertices = new ArrayList<>();

        boolean inside = false;
        boolean filled = false;
        Color color = Color.WHITE;
        String tag = "Triangle";

        for (String line : lines) {

            line = line.trim();

            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            // =========================
            // OPEN TRIANGLE
            // =========================
            if (line.equals("t:[")) {

                if (inside) {
                    throw new RuntimeException("Nested triangle declaration.");
                }

                inside = true;
                currentVertices.clear();
                filled = false;
                color = Color.WHITE;
                tag = "Triangle";

                continue;
            }

            // =========================
            // CLOSE TRIANGLE
            // =========================
            if (line.equals("]")) {

                if (!inside) {
                    continue;
                }

                if (currentVertices.size() != 3) {
                    throw new RuntimeException(
                            "Triangle must have exactly 3 vertices, got " + currentVertices.size()
                    );
                }

                triangles.add(new Triangle(
                        currentVertices.get(0),
                        currentVertices.get(1),
                        currentVertices.get(2),
                        filled,
                        color,
                        tag
                ));

                inside = false;
                continue;
            }

            // ignore outside triangle
            if (!inside) {
                continue;
            }

            // =========================
            // KEY : VALUE
            // =========================
            String[] split = line.split(":", 2);
            if (split.length < 2) {
                continue;
            }

            String key = split[0].trim();
            String value = split[1].trim();

            // clean trailing commas safely
            if (value.endsWith(",")) {
                value = value.substring(0, value.length() - 1).trim();
            }

            switch (key) {

                // =========================
                // VERTEX
                // =========================
                case "v": {

                    String v = value.replace("{", "").replace("}", "");
                    String[] c = v.split(",");

                    if (c.length < 2) {
                        throw new RuntimeException("Invalid vertex: " + value);
                    }

                    currentVertices.add(new Vertex(
                            Double.parseDouble(c[0].trim()),
                            Double.parseDouble(c[1].trim())
                    ));

                    break;
                }

                // =========================
                // FILL
                // =========================
                case "fill":
                    filled = Boolean.parseBoolean(value);
                    break;

                // =========================
                // COLOR
                // =========================
                case "color": {

                    String[] c = value
                            .replace("{", "")
                            .replace("}", "")
                            .split(",");

                    if (c.length < 3) {
                        throw new RuntimeException("Invalid color: " + value);
                    }

                    color = new Color(
                            Integer.parseInt(c[0].trim()),
                            Integer.parseInt(c[1].trim()),
                            Integer.parseInt(c[2].trim())
                    );

                    break;
                }

                // =========================
                // TAG
                // =========================
                case "tag":
                    tag = value;
                    break;
            }
        }

        if (inside) {
            throw new RuntimeException("Missing closing ']'");
        }

        return new Mesh(triangles);
    }

    private Mesh parseIndexed(List<String> lines) {

        List<Vertex> vertices = new ArrayList<>();
        List<Triangle> triangles = new ArrayList<>();

        List<Vertex> current = new ArrayList<>();

        boolean inVertices = false;
        boolean inTriangles = false;

        boolean filled = false;
        Color color = Color.WHITE;
        String tag = "Triangle";

        for (String line : lines) {

            line = line.trim();

            if (line.isEmpty() || line.startsWith("#")) continue;

            // =========================
            // OPEN BLOCKS
            // =========================
            if (line.startsWith("vertices:[")) {
                inVertices = true;
                continue;
            }

            if (line.startsWith("t:[")) {
                inTriangles = true;

                current.clear();
                filled = false;
                color = Color.WHITE;
                tag = "Triangle";

                continue;
            }

            // =========================
            // CLOSE BLOCK
            // =========================
            if (line.equals("]")) {

                // close vertices
                if (inVertices) {
                    inVertices = false;
                    continue;
                }

                // close triangle => BUILD HERE (IMPORTANT FIX)
                if (inTriangles) {

                    if (current.size() != 3) {
                        throw new RuntimeException("Triangle must have 3 vertices");
                    }

                    triangles.add(new Triangle(
                            current.get(0),
                            current.get(1),
                            current.get(2),
                            filled,
                            color,
                            tag
                    ));

                    current.clear();
                    inTriangles = false;
                    continue;
                }
            }

            // =========================
            // SPLIT
            // =========================
            String[] split = line.split(":", 2);
            if (split.length < 2) continue;

            String key = split[0].trim();
            String value = split[1].trim();

            if (value.endsWith(",")) {
                value = value.substring(0, value.length() - 1).trim();
            }

            // =========================
            // VERTICES
            // =========================
            if (inVertices) {

                if (key.equals("i")) {

                    String[] p = value.split(",");

                    vertices.add(new Vertex(
                            Double.parseDouble(p[0].trim()),
                            Double.parseDouble(p[1].trim())
                    ));
                }

                continue;
            }

            // =========================
            // TRIANGLES
            // =========================
            if (inTriangles) {

                switch (key) {

                    case "v": {
                        int index = Integer.parseInt(value);
                        current.add(vertices.get(index));
                        break;
                    }

                    case "fill":
                        filled = Boolean.parseBoolean(value);
                        break;

                    case "color": {
                        String[] c = value.replace("{", "").replace("}", "").split(",");

                        color = new Color(
                                Integer.parseInt(c[0].trim()),
                                Integer.parseInt(c[1].trim()),
                                Integer.parseInt(c[2].trim())
                        );
                        break;
                    }

                    case "tag":
                        tag = value;
                        break;
                }
            }
        }

        return new Mesh(triangles);
    }
}
