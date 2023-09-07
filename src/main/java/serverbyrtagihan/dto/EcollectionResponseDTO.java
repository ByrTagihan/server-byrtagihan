package serverbyrtagihan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EcollectionResponseDTO {
    private String status;
    private String data;
    private EcollectionDataDTO  datas;

    private String message;

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

    public EcollectionDataDTO getDatas() {
        return datas;
    }

    public void setDatas(EcollectionDataDTO datas) {
        this.datas = datas;
    }
}

