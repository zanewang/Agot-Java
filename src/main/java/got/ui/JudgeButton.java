package got.ui;

import javax.swing.JButton;

public class JudgeButton extends JButton {

    private String preFamily;
    private String nextFamily;
    private int preRank;
    private int nextRank;

    public JudgeButton(String preFamily, String nextFamily, int preRank, int nextRank) {
        this.setPreFamily(preFamily);
        this.setNextFamily(nextFamily);
        this.preRank = preRank;
        this.nextRank = nextRank;
    }

    public void setPreFamily(String preFamily) {
        this.preFamily = preFamily;
    }

    public String getPreFamily() {
        return preFamily;
    }

    public void setNextFamily(String nextFamily) {
        this.nextFamily = nextFamily;
    }

    public String getNextFamily() {
        return nextFamily;
    }

    public void setPreRank(int preRank) {
        this.preRank = preRank;
    }

    public int getPreRank() {
        return preRank;
    }

    public void setNextRank(int nextRank) {
        this.nextRank = nextRank;
    }

    public int getNextRank() {
        return nextRank;
    }

    public void change() {
        // TODO Auto-generated method stub
        String tmp = preFamily;
        preFamily = nextFamily;
        nextFamily = tmp;
    }
}
