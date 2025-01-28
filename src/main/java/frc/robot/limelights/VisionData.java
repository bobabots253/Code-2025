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
    public final boolean canTrustRotation;
    public final boolean canTrustPosition;
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
        ChassisSpeeds robotChassisSpeeds = driveRequire.getRobotRelativeSpeeds();
        double currentVelocity = Math.sqrt(Math.pow(robotChassisSpeeds.vxMetersPerSecond, 2) + Math.pow(robotChassisSpeeds.vyMetersPerSecond, 2));
        return this.MegaTag2 != null
            && this.MegaTag2.avgTagDist <= Constants.VisionConstants.AVG_MT2_TAG_DIST
            && this.MegaTag != null
            && this.MegaTag.tagCount >= Constants.VisionConstants.MIN_MT_TAG_COUNT
            && Units.radiansToDegrees(robotChassisSpeeds.omegaRadiansPerSecond) <= Constants.VisionConstants.MAX_ANGULAR
            && currentVelocity <= 2;
    }

    /**
     * Checks if the MegaTag2 Pose2d is within distance tolerance relative to the bot's position.
     * @return Whether position data can be trusted.
     */
    private boolean canTrustPosition() {
        ChassisSpeeds robotChassisSpeeds = driveRequire.getRobotRelativeSpeeds();
        double currentVelocity = Math.sqrt(Math.pow(robotChassisSpeeds.vxMetersPerSecond, 2) + Math.pow(robotChassisSpeeds.vyMetersPerSecond, 2));
        return this.MegaTag2 != null
            && this.MegaTag2.tagCount > 0
            && this.MegaTag2.avgTagDist < Constants.VisionConstants.TRUSTWORTHY_DISTANCE
            && Units.radiansToDegrees(robotChassisSpeeds.omegaRadiansPerSecond) <= Constants.VisionConstants.MAX_ANGULAR
            && currentVelocity <= 2;
    }
}
