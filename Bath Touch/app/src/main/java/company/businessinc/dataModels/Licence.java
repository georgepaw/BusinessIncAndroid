package company.businessinc.dataModels;

/**
 * Created by Grzegorz on 28/04/2015.
 */
public class Licence {
    private String libName;
    private String author;
    private String description;
    private String url;

    public Licence(String libName, String author, String description, String url) {
        this.libName = libName;
        this.author = author;
        this.description = description;
        this.url = url;
    }

    public String getLibName() {
        return libName;
    }

    public void setLibName(String libName) {
        this.libName = libName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
