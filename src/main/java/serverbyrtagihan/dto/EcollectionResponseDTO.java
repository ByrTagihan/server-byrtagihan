package serverbyrtagihan.dto;

import com.fasterxml.jackson.databind.JsonNode;

public class EcollectionResponseDTO {
    private String status;
    private String data;

    private JsonNode dataJson;

    private String message;

    // getters and setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonNode getDataJson() {
        return dataJson;
    }

    public void setDataJson(JsonNode dataJson) {
        this.dataJson = dataJson;
    }
}

