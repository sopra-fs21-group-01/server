package ch.uzh.ifi.hase.soprafs21.rest.dto;

import java.util.Map;

public class FunPostDTO {

    public Map<String, String> getContents() {
        return contents;
    }

    public void setContents(Map<String, String> contents) {
        this.contents = contents;
    }

    private Map<String, String> contents;

    public Map<String, String> getSuccess() {
        return success;
    }

    public void setSuccess(Map<String, String> success) {
        this.success = success;
    }

    private Map<String, String> success;
}
