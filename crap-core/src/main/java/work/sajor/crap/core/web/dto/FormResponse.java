package work.sajor.crap.core.web.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import work.sajor.crap.core.web.WebDict;

@Data
@Accessors(chain = true)
public class FormResponse {

    WebDict dict = new WebDict();

    Object data;

    Object extra;

    public FormResponse() {
    }

    public FormResponse(Object data) {
        this.data = data;
    }

    public FormResponse(Object data, WebDict dict) {
        this.data = data;
        this.dict = dict;
    }
}
