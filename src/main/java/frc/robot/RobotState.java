package frc.robot;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class RobotState {
    private static RobotState instance;

    public static RobotState getInstance() {
        if (instance == null) {
            instance = new RobotState();
        }
        return instance;
    }

    private Pose2d robotToFieldFromSwerveDriveOdometry = new Pose2d();
    private ChassisSpeeds robotChassisSpeeds = new ChassisSpeeds();
}
