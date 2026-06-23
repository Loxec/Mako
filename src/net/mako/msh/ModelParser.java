package net.mako.msh;

import net.mako.rendering.Mesh;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ModelParser {

    private String path;
    private URL url;

    public ModelParser(String path) {
        if(path.startsWith("/")) {
            this.path = path;
        }else {
            this.path = "/" + path;
        }

        URL url = getClass().getResource(path);
        this.url = url;

        if (this.url == null) {
            throw new RuntimeException("Model not found: " + path);
        }
    }


    public FormatType getFormatType() {

        FormatType formatType = null;

        if(this.url == null) {
            formatType = FormatType.NULL;
        }else{
            try {
                BufferedReader br = new BufferedReader(new FileReader(this.url.getFile()));

                List<String> lines = br.readAllLines();

                for(String line : lines) {
                    line = line.trim();
                    if(line.length() == 0 || line.startsWith("#")) {
                        continue;
                    } else if(line.startsWith("msh_format:")) {
                        line = line.substring("msh_format:".length()).trim();

                        switch(line) {
                            case "basic_t":
                                return FormatType.BASIC_T;

                            case "indexed_t":
                                return FormatType.INDEXED_T;

                            default:
                                return FormatType.NULL;
                        }
                    }else{
                        formatType = FormatType.NULL;
                    }
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return formatType;
    }

    public Mesh getMesh(FormatType formatType) {

        Mesh mesh = null;

        if(formatType == FormatType.NULL) {
            mesh = null;
        }else if(formatType == FormatType.BASIC_T) {
            ModelConverter converter = new ModelConverter(new File(this.url.getFile()),FormatType.BASIC_T);
            mesh = converter.getMesh();
        }else if(formatType == FormatType.INDEXED_T) {
            ModelConverter converter = new ModelConverter(new File(this.url.getFile()),FormatType.INDEXED_T);
            mesh = converter.getMesh();
        }

        return mesh;
    }

}
