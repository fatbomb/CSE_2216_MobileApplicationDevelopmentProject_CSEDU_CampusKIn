package New.Main.CSEDU_CampusKin.Model;

public class registrationModel {
    boolean reg;
    String regno;

    public registrationModel() {
    }

    public registrationModel(boolean reg, String regno) {
        this.reg = reg;
        this.regno = regno;
    }

    public boolean isReg() {
        return reg;
    }

    public void setReg(boolean reg) {
        this.reg = reg;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }
}
