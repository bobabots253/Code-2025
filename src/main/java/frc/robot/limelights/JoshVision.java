package frc.robot.limelights;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
import frc.robot.LimelightHelpers.PoseEstimate;

public class JoshVision extends SubsystemBase{
    // Pose2d position = getData();
    public static PoseEstimator location;
    AHRS navx = new AHRS(NavXComType.kMXP_SPI);
    public Field2d field;
    private static JoshVision instance;
    public static JoshVision getInstance(){
        if(instance == null) instance = new JoshVision();
        return instance;
    }
    
    private JoshVision(){
        
    }
    

    public double currentYaw(){
        double yaw = -navx.getRotation2d().getDegrees();
        if(DriverStation.getAlliance().orElse(DriverStation.Alliance.Red) == DriverStation.Alliance.Red){
            yaw+=180;
        }
        return yaw;
    }
    //this is to test not megatag 2
    public static LimelightHelpers.PoseEstimate pose = LimelightHelpers.getBotPoseEstimate_wpiBlue(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL);
    public static LimelightHelpers.PoseEstimate megaPose = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL);
    public static Pose2d currentLocation;
    //this is my old function
    public PoseEstimate getData(){
        
        PoseEstimate translation = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL);
        //Pose2d estimation = new Pose2d(translation.pose.getTranslation(), Rotation2d.fromDegrees(currentYaw()));
        return translation;
    }
    public Pose2d getPose2d(Translation2d translation, Rotation2d rotation){
        return new Pose2d(translation, rotation);

    }
    //have a variable for the reeftage so I can either put int a number or idk make a enum to put in for int 
    //have the boolean be left if its false and right if its true you should make a enum
    // public Command generateCommand(int tagID, boolean side){
    //     // return Commands.defer(() -> {
    //     //     //var branch = getBranchFromTag(tagID.getPosition, null);
            
    //     // })
    //     return new Command;
    // }

    private static Pose2d getBranchFromTag(Pose2d tag, Boolean side) {
        var translation = tag.getTranslation().plus(
            new Translation2d(
                //side.tagOffset.getY(),
                //side.tagOffset.getX()
            ).rotateBy(tag.getRotation())
        );

        return new Pose2d(
            translation.getX(),
            translation.getY(),
            tag.getRotation()
        );
    }


    //@SuppressWarnings("unchecked")
    @Override
    public void periodic(){
        LimelightHelpers.SetRobotOrientation(Constants.VisionConstants.FRONT_LEFT_APRIL_TAG_LL, currentYaw()-23, 0, 22, 0, 0, 0);
        //location.setVisionMeasurementStdDevs(VecBuilder.fill(.7,.7,9999999));
        currentLocation = getPose2d(getData().pose.getTranslation(), Rotation2d.fromDegrees(currentYaw()));
        //location.addVisionMeasurement(currentLocation, getData().timestampSeconds);
        //field.setRobotPose(location.getEstimatedPosition());
        // SmartDashboard.putData("robot", field);
    }


}





























// public class JoshVision{
//     Pose2d currentPose = getData();
//     Field2d joshVisionMeasurment = new Field2d();
//     AHRS navx = new AHRS(NavXComType.kMXP_SPI);


    



// }





