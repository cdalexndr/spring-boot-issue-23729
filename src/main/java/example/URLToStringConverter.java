package example;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class URLToStringConverter implements AttributeConverter<URL, String> {
    @Override
    public String convertToDatabaseColumn(URL uri) {
        if (uri==null)
            return null;
        return uri.getUrl();
    }

    @Override
    public URL convertToEntityAttribute(String s) {
        if (s==null || s.isBlank())
            return null;
        return new URLImpl(s);
    }
}
