package net.mako.tdim.rendering.msh;

import net.mako.tdim.rendering.components.Model;

import java.io.*;
import java.net.URL;
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

                            case "multimesh":
                                return FormatType.MULTIMESH;

                            case "indexed_multimesh":
                                return FormatType.INDEXED_MULTIMESH;

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

    public Model getModel(FormatType formatType) {

        Model mesh = null;

        if(formatType == FormatType.NULL) {
            mesh = null;
        }else if(formatType == FormatType.BASIC_T) {
            ModelConverter converter = new ModelConverter(new File(this.url.getFile()),FormatType.BASIC_T);
            mesh = converter.getModel();
        }else if(formatType == FormatType.INDEXED_T) {
            ModelConverter converter = new ModelConverter(new File(this.url.getFile()),FormatType.INDEXED_T);
            mesh = converter.getModel();
        }else if(formatType == FormatType.MULTIMESH) {
            ModelConverter converter = new ModelConverter(new File(this.url.getFile()),FormatType.MULTIMESH);
            mesh = converter.getModel();
        }else if(formatType == FormatType.INDEXED_MULTIMESH) {
            ModelConverter converter = new ModelConverter(new File(this.url.getFile()),FormatType.INDEXED_MULTIMESH);
            mesh = converter.getModel();
        }

        return mesh;
    }

}
