package elliot.hoi.tech.Controllers;

public class UploadFileResponse {
    public UploadFileResponse(String status, String details) {
        this.status = status;
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }

    private String status;
    private String details;
}
