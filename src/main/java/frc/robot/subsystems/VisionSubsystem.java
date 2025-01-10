package frc.robot.subsystems;

import java.util.Optional;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.LimelightHelpers;

public class VisionSubsystem extends SubsystemBase {
    private static VisionSubsystem instance;
    private double lastTX;
    private double lastDistance;
    private double lastMeasurementTime;
    private boolean isOld;
    private double kCacheTimeout = 10;

    public VisionSubsystem() {
        this.lastTX = 0;
        this.lastDistance = 0;
        this.lastMeasurementTime = 0;
        this.isOld = true;
    }

    public static synchronized VisionSubsystem getInstance() {
        if (instance == null) {
            instance = new VisionSubsystem();
        }
        return instance;
    }

    public Optional<Double> getTX() {
        return isOld ? Optional.empty() : Optional.of(lastTX);
    }

    public void periodic() {
        SmartDashboard.putNumber("Limelight Distance", lastDistance);
        SmartDashboard.putNumber("Limelight Angular Error", lastTX % 360);

        if (LimelightHelpers.getTV("limelight")) {
            lastTX = DriveSubsystem.getInstance().getRotation2DHeading().getDegrees()
                    - (LimelightHelpers.getTX("limelight"));
            Pose3d tagPose = LimelightHelpers.getTargetPose3d_CameraSpace("limelight");
            lastDistance = tagPose.getTranslation().getNorm();
            isOld = false;

            lastMeasurementTime = Timer.getFPGATimestamp();
        }

        if (Timer.getFPGATimestamp() - lastMeasurementTime > kCacheTimeout) {
            isOld = true;
        }
    }

}
