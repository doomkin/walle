// Robot WallE for Robocode https://robocode.sourceforge.io/

package walle;

import robocode.*;
import robocode.util.*;

public class WallE extends AdvancedRobot
{
    double moveDirection = 1;

    public void run()
    {
        while (true) {
            turnRadarLeftRadians(1);
            scan();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e)
    {
        double power = getEnergy() > Rules.MAX_BULLET_POWER ? Rules.MAX_BULLET_POWER : Rules.MIN_BULLET_POWER;
        double enemyMoveAngle = Utils.normalRelativeAngle(Math.PI - getHeadingRadians() - e.getBearingRadians() + e.getHeadingRadians());
        double correctionGun = Math.asin(e.getVelocity() * Math.sin(enemyMoveAngle) / Rules.getBulletSpeed(power));

        setTurnRightRadians(Math.sin(e.getBearingRadians() - correctionGun));
        setAhead(e.getDistance() * moveDirection);

        double fireAngle = Utils.normalRelativeAngle(getHeadingRadians() + e.getBearingRadians() - getGunHeadingRadians() - correctionGun);
        double beta = Math.PI - enemyMoveAngle - correctionGun, t = 0;
        if (beta != 0) {
            t = e.getDistance() * Math.sin(enemyMoveAngle) / Rules.getBulletSpeed(power) / Math.sin(beta);
        }
        double impactX = getX() + Rules.getBulletSpeed(power) * t * Math.sin(fireAngle + getGunHeadingRadians());
        double impactY = getY() + Rules.getBulletSpeed(power) * t * Math.cos(fireAngle + getGunHeadingRadians());

        setTurnGunRightRadians(fireAngle);

        if (impactX >= 0 && impactX <= getBattleFieldWidth() && impactY >= 0 && impactY <= getBattleFieldHeight()) {
            setFire(power);
        }
            
        setTurnRadarRightRadians(2 * Utils.normalRelativeAngle(getHeadingRadians() - getRadarHeadingRadians() + e.getBearingRadians()));
    }

    public void onHitWall(HitWallEvent event)
    {
        moveDirection *= -1;
    }
}
