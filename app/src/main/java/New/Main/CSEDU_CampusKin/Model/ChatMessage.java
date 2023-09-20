package New.Main.CSEDU_CampusKin.Model;

public class ChatMessage {
    private String message;
    private boolean isMine;
    private String fileUrl;

    public ChatMessage(String message, boolean isMine) {
        this.message = message;
        this.isMine = isMine;
        this.fileUrl = null;
    }

    public ChatMessage(String fileUrl, boolean isMine, boolean isFile) {
        this.fileUrl = fileUrl;
        this.isMine = isMine;
        this.message = null;
    }

    public String getMessage() {
        return message;
    }

    public boolean isMine() {
        return isMine;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}