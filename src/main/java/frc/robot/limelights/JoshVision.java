package frc.robot.limelights;

import static edu.wpi.first.units.Units.Rotation;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;

public class JoshVision {
    AHRS gyro = new AHRS(NavXComType.kMXP_SPI);
    Field2d field = new Field2d();

    public void periodic () {
        
        SmartDashboard.putData( "JoshTesting", field);
        System.out.println("wow");
        field.setRobotPose(getEstimatePos());
    }
    public Pose2d getEstimatePos(){
        //Pose2d estimate = new Pose2d();
        LimelightHelpers.SetRobotOrientation(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL, -gyro.getAngle(), 0, 0, 0, 0, 0);
        PoseEstimate testing = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL);
        Pose2d position = new Pose2d(testing.pose.getTranslation(), Rotation2d.fromDegrees(-gyro.getAngle()));
        return position;
    }
}
