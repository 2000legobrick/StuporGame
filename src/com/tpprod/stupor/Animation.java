package com.tpprod.stupor;

import java.util.ArrayList;

public class Animation {
    private ArrayList<Mob> mobs = new ArrayList<Mob>();

    public void Animate() {
        mobs = StateMachine.getPhysics().getMobs();
        for (Mob entity : mobs) {
            if (entity.getVelocityX() > 12 || entity.getVelocityX() < -12) {
                entity.NextFrame(1);
            } else if (entity.getVelocityX() != 0 && entity.getVelocityX() > -12 && entity.getVelocityX() < 12) {
                entity.NextFrame(2);
            } else if (entity.getVelocityX() == 0) {
                entity.NextFrame(3);
            } else {
                entity.NextFrame(0);
            }
        }
    }
}
