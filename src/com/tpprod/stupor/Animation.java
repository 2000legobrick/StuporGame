package com.tpprod.stupor;

import java.util.ArrayList;

public class Animation {
    public void Animate(ArrayList<Mob> mobs) {
        for (Mob entity : mobs) {
            if (entity.getVelocityX() > entity.getSpeed() - 5 || entity.getVelocityX() < -entity.getSpeed() + 5) {
                entity.NextFrame(1);
            } else if (entity.getVelocityX() != 0) {
                entity.NextFrame(2);
            } else {
                entity.NextFrame(3);
            }
        }
    }
}
