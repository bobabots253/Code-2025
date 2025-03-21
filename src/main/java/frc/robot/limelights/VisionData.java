package frc.robot.limelights;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.util.Units;
import frc.robot.LimelightHelpers;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.Constants;

public class VisionData {
    public final String name;
    public final LimelightHelpers.PoseEstimate MegaTag;
    public final LimelightHelpers.PoseEstimate MegaTag2;
    public boolean canTrustRotation;
    public boolean canTrustPosition;
    private final DriveSubsystem driveRequire = RobotContainer.getInstance().m_robotDrive;

    public boolean optimized;

    public VisionData(String name, LimelightHelpers.PoseEstimate MegaTag, LimelightHelpers.PoseEstimate MegaTag2) {
        this.name = name;
        this.MegaTag = MegaTag;
        this.MegaTag2 = MegaTag2;
        this.optimized = false;

        // These are calculated when the measurement is created
        this.canTrustRotation = canTrustRotation();
        this.canTrustPosition = canTrustPosition();
    }

    //Makes sure: Distance <= 3 meters ; Angular <= 180 deg/s ; Translational <= 2 m/s
    private boolean canTrustRotation() {
        return
            this.canTrustRotation = 
            this.MegaTag != null
            && this.MegaTag2 != null;   
    }

    /**
     * Checks if the MegaTag2 Pose2d is within distance tolerance relative to the bot's position.
     * @return Whether position data can be trusted.
     */
    private boolean canTrustPosition() {
        return
            this.canTrustRotation = 
            this.MegaTag != null
            && this.MegaTag2 != null;
    }
}
