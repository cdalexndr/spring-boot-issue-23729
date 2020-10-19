package example;

import java.util.Objects;

public class URLImpl implements URL {
    private String url;

    public URLImpl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this==o) return true;
        if (o==null || getClass()!=o.getClass()) return false;
        URLImpl url1 = (URLImpl) o;
        return url.equals(url1.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String getUrl() {
        return url;
    }
}
