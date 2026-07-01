package net.mako.tdim.rendering.msh;

import net.mako.tdim.rendering.components.Mesh;
import net.mako.tdim.rendering.components.Model;
import net.mako.tdim.rendering.components.Triangle;
import net.mako.tdim.rendering.components.Vertex;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ModelConverter {

    public File file;
    public FormatType formatType;

    public ModelConverter(File file, FormatType formatType) {
        this.file = file;
        this.formatType = formatType;
    }

    public Model getModel() {

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            List<String> lines = br.readAllLines();

            if (formatType == FormatType.BASIC_T) {
                return parseBasic(lines);
            }

            if (formatType == FormatType.INDEXED_T) {
                return parseIndexed(lines);
            }

            if(formatType == FormatType.MULTIMESH) {
                return parseMultimesh(lines);
            }

            if(formatType == FormatType.INDEXED_MULTIMESH) {
                return parseIndexedMultimesh(lines);
            }

            return new Model(new ArrayList<>());

        } catch (IOException e) {
            throw new RuntimeException("Failed to load msh file.", e);
        }
    }

    private Model parseBasic(List<String> lines) {

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

        return new Mesh(triangles).getModel();
    }

    private Model parseIndexedMultimesh(List<String> lines) {

        List<Vertex> vertices = new ArrayList<>();
        List<Mesh> meshes = new ArrayList<>();

        List<Triangle> currentTriangles = new ArrayList<>();
        List<Integer> currentIndices = new ArrayList<>();


        boolean insideVertices = false;
        boolean insideMesh = false;
        boolean insideTriangle = false;

        boolean filled = false;
        Color color = Color.WHITE;
        String tag = "Triangle";

        for (String line : lines) {

            line = line.trim();

            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            // =========================
            // OPEN VERTICES
            // =========================
            if (line.equals("vertices:[")) {

                if (insideVertices) {
                    throw new RuntimeException("Nested vertices block.");
                }

                insideVertices = true;
                continue;
            }

            // =========================
            // OPEN MESH
            // =========================

            if (line.equals("mesh:[")) {

                if (insideMesh) {
                    throw new RuntimeException("Nested mesh block.");
                }

                insideMesh = true;
                currentTriangles.clear();

                continue;
            }

            // =========================
            // OPEN TRIANGLE
            // =========================

            if (line.equals("t:[")) {

                if (!insideMesh) {
                    throw new RuntimeException("Triangle declared outside mesh.");
                }

                if (insideTriangle) {
                    throw new RuntimeException("Nested triangle block.");
                }

                insideTriangle = true;

                currentIndices.clear();
                filled = false;
                color = Color.WHITE;
                tag = "Triangle";

                continue;
            }

            // =========================
            // CLOSE BLOCKS
            // =========================

            if (line.equals("]")) {

                // fermeture triangle
                if (insideTriangle) {

                    if (currentIndices.size() != 3) {
                        throw new RuntimeException(
                                "Triangle must contain exactly 3 indices."
                        );
                    }

                    currentTriangles.add(
                            new Triangle(
                                    vertices.get(currentIndices.get(0)),
                                    vertices.get(currentIndices.get(1)),
                                    vertices.get(currentIndices.get(2)),
                                    filled,
                                    color,
                                    tag
                            )
                    );

                    insideTriangle = false;
                    continue;
                }

                // fermeture vertices
                if (insideVertices) {
                    insideVertices = false;
                    continue;
                }

                // fermeture mesh
                if (insideMesh) {

                    meshes.add(
                            new Mesh(
                                    new ArrayList<>(currentTriangles)
                            )
                    );

                    currentTriangles.clear();
                    insideMesh = false;

                    continue;
                }

                throw new RuntimeException("Unexpected closing ']'.");
            }

            // =========================
            // PARSE GLOBAL VERTICES
            // =========================

            if (insideVertices) {

                String[] split = line.split(":", 2);

                if (split.length != 2) {
                    continue;
                }

                if (!split[0].trim().equals("i")) {
                    continue;
                }

                String[] coords = split[1].trim().split(",");

                if (coords.length != 2) {
                    throw new RuntimeException("Invalid vertex: " + line);
                }

                vertices.add(
                        new Vertex(
                                Double.parseDouble(coords[0].trim()),
                                Double.parseDouble(coords[1].trim())
                        )
                );

                continue;
            }

            // =========================
            // TRIANGLE DATA
            // =========================

            if (!insideTriangle) {
                continue;
            }

            String[] split = line.split(":", 2);

            if (split.length != 2) {
                continue;
            }

            String key = split[0].trim();
            String value = split[1].trim();

            if (value.endsWith(",")) {
                value = value.substring(0, value.length() - 1).trim();
            }

            switch (key) {

                case "v":

                    currentIndices.add(
                            Integer.parseInt(value)
                    );

                    break;

                case "fill":

                    filled = Boolean.parseBoolean(value);

                    break;

                case "color": {

                    String cleaned = value
                            .replace("{", "")
                            .replace("}", "");

                    String[] rgb = cleaned.split(",");

                    if (rgb.length != 3) {
                        throw new RuntimeException("Invalid color: " + value);
                    }

                    color = new Color(
                            Integer.parseInt(rgb[0].trim()),
                            Integer.parseInt(rgb[1].trim()),
                            Integer.parseInt(rgb[2].trim())
                    );

                    break;
                }

                case "tag":

                    tag = value;

                    break;
            }
        }

        // =========================
        // VALIDATION
        // =========================

        if (insideVertices) {
            throw new RuntimeException("Vertices block not closed.");
        }

        if (insideTriangle) {
            throw new RuntimeException("Triangle block not closed.");
        }

        if (insideMesh) {
            throw new RuntimeException("Mesh block not closed.");
        }

        return new Model(meshes);
    }

    private Model parseMultimesh(List<String> lines) {

        List<Mesh> meshes = new ArrayList<>();
        List<Triangle> triangles = new ArrayList<>();
        List<Vertex> currentVertices = new ArrayList<>();

        boolean insideMesh = false;
        boolean insideTriangle = false;

        boolean filled = false;
        Color color = Color.WHITE;
        String tag = "Triangle";

        for (String line : lines) {

            line = line.trim();

            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }

            // Ouvrir un mesh
            if (line.equals("mesh:[")) {

                if (insideMesh) {
                    throw new RuntimeException("Nested mesh declaration.");
                }

                insideMesh = true;
                triangles.clear();

                continue;
            }

            // Ouvrir un triangle
            if (line.equals("t:[")) {

                if (!insideMesh) {
                    throw new RuntimeException("Triangle outside mesh.");
                }

                if (insideTriangle) {
                    throw new RuntimeException("Nested triangle declaration.");
                }

                insideTriangle = true;

                currentVertices.clear();
                filled = false;
                color = Color.WHITE;
                tag = "Triangle";

                continue;
            }

            // Fermeture ] (triangle ou mesh)
            if (line.equals("]")) {

                // Fermer triangle
                if (insideTriangle) {

                    if (currentVertices.size() != 3) {
                        throw new RuntimeException(
                                "Triangle must contain exactly 3 vertices, got "
                                        + currentVertices.size()
                        );
                    }

                    triangles.add(
                            new Triangle(
                                    currentVertices.get(0),
                                    currentVertices.get(1),
                                    currentVertices.get(2),
                                    filled,
                                    color,
                                    tag
                            )
                    );

                    insideTriangle = false;
                    continue;
                }

                // Fermer mesh
                if (insideMesh) {

                    meshes.add(
                            new Mesh(
                                    new ArrayList<>(triangles)
                            )
                    );

                    triangles.clear();
                    insideMesh = false;
                    continue;
                }

                throw new RuntimeException("Unexpected closing ']'.");
            }

            // Les propriétés ne sont valides que dans un triangle
            if (!insideTriangle) {
                continue;
            }

            String[] split = line.split(":", 2);

            if (split.length != 2) {
                continue;
            }

            String key = split[0].trim();
            String value = split[1].trim();

            if (value.endsWith(",")) {
                value = value.substring(0, value.length() - 1).trim();
            }

            switch (key) {

                case "v": {

                    String cleaned = value
                            .replace("{", "")
                            .replace("}", "");

                    String[] coords = cleaned.split(",");

                    if (coords.length != 2) {
                        throw new RuntimeException("Invalid vertex: " + value);
                    }

                    currentVertices.add(
                            new Vertex(
                                    Double.parseDouble(coords[0].trim()),
                                    Double.parseDouble(coords[1].trim())
                            )
                    );

                    break;
                }

                case "fill": {
                    filled = Boolean.parseBoolean(value);
                    break;
                }

                case "color": {

                    String cleaned = value
                            .replace("{", "")
                            .replace("}", "");

                    String[] rgb = cleaned.split(",");

                    if (rgb.length != 3) {
                        throw new RuntimeException("Invalid color: " + value);
                    }

                    color = new Color(
                            Integer.parseInt(rgb[0].trim()),
                            Integer.parseInt(rgb[1].trim()),
                            Integer.parseInt(rgb[2].trim())
                    );

                    break;
                }

                case "tag": {
                    tag = value;
                    break;
                }
            }
        }

        if (insideTriangle) {
            throw new RuntimeException("Unclosed triangle.");
        }

        if (insideMesh) {
            throw new RuntimeException("Unclosed mesh.");
        }

        return new Model(meshes);
    }

    private Model parseIndexed(List<String> lines) {

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



        return new Mesh(triangles).getModel();
    }
}
