import javax.swing.*;
import java.awt.*;

public class RandomTimer extends Thread {
    private GUI buttongame;

    @Override
    public synchronized void run() {
        buttongame.actualButtonList.clear();
        int rand = (3 + (int) (Math.random() * ((6 - 3) + 1))) * 1000;
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        rand = (int) (Math.random() * 4) + 1;
        while (buttongame.actualButtonList.size() < rand) {
            int randButton = (int) (Math.random() * 16) + 1;
            if (!buttongame.actualButtonList.contains(randButton)) buttongame.actualButtonList.add(randButton);
        }
        if(buttongame.buttonClicked) {
            for (Integer i : buttongame.actualButtonList) {
                JButton selectedButton = buttongame.buttonList.get(i - 1);
                selectedButton.setEnabled(true);
                selectedButton.setBackground(Color.GREEN);
            }
        }
    }

    public void setCallBack(GUI game){
        this.buttongame = game;
    }
}