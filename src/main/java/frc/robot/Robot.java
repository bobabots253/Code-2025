// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.littletonrobotics.urcl.URCL;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import edu.wpi.first.apriltag.AprilTagDetection;
import edu.wpi.first.apriltag.AprilTagDetector;
import edu.wpi.first.apriltag.AprilTagPoseEstimator;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.HttpCamera;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.HttpCamera.HttpCameraKind;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.estimator.PoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Constants.ElevatorConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.Constants.VisionConstants;
import frc.robot.Autonomous.AutoModeManager;
import frc.robot.Autonomous.AutoModeManager.DesiredMode;
import frc.robot.Bobaboard.BotControls;
import frc.robot.Bobaboard.ControlHub;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;

/*Important Notes for 2025:
22 April Tags - Diff Angles (not all @ 90*). - center tags @ 30* down
solution: run 2 limelights? - tags clustered together = easier tf
current auto-align works, remember to flip by 180 when red
AUTO:
Start in the center, alliance dependant
- certain areas are a no go zone, likely need to exempt opposing barge zone
- intake available during auto. multi-CORAL auto? 
  - 4 L1 dumps - min
  - 3 L3 CORAL + ALGAE - ideal
COOPERTITION:
- basically necessary if goal is to get CORAL RP
- unlikely L4 scores
ENDGAME:
- 2 shallow + park = free RP
- Shallow Cage Climb? - 1678 2023 climb?
CHANGES/DIFFS:
- no feeding alliance partners
- coop basically neccesary
- ALGAE COOP is kinda bad value when <2 bc net gain is only +4
Initial Design Brainstorm:
- Variable Slapdown Ground Intake (1678, 2024)
- Indexer (6328, 2024), (1678,2024)
- Dual Stage Elavator? - hard but gurantee upgradbality and L1
- Cascading Elavator (X?) - very diffircult, never done b4. chance to do everything
- Spin down Gamepiece (4414, 2023). Will not bounce off.
PHIL:
Possible RP:
AUTO RP: Free RP
Coral RP: Coop and L1 -> L3 = Work
Barge RP: [2 Shallow + 1 Park = Free RP], 1 deep + 2 park = RP
Win = 3 RP, relevant to above but not casuality

Design:
Robust, Fast, Easy to Rebuild
Intakes: Touch it, Own It.
Auto Domination = Easy Head Start --> Snowball/Hold Lead

Discuss: TMRW
Goals.

Aspects with Sig Influence:
AUTO: goal is multi-piece autos = RP & Head Start
If we feed L1, fast cycles
If we score L2+, gurantee 5 at min
MUST Clear ALGAE B4 Scoring, 2 in 1 mechanism?
Driveteam wanted a hold rotation button (Easy)
Driveteam wanted a direct drive to place (Waiting for April Tag Map to make TreeMap)
*/

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public RobotContainer m_robotContainer;
  private final ControlHub mControlBoard = ControlHub.getInstance();
  private final BotControls mDriveControls = new BotControls();
  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_robotContainer = RobotContainer.getInstance();

    //Advantage Scope Reference:
    DataLogManager.start();
    URCL.start();
    DriverStation.startDataLog(DataLogManager.getLog());
    mDriveControls.PutControllerOption();
    // m_robotContainer.m_robotDrive.zeroHeading();
    // LimelightHelpers.SetIMUMode(VisionConstants.FRONT_LEFT_APRIL_TAG_LL, 1);
    // LimelightHelpers.SetIMUMode(VisionConstants.FRONT_RIGHT_APRIL_TAG_LL, 1);
    // LimelightHelpers.SetFiducialIDFiltersOverride(VisionConstants.FRONT_LEFT_APRIL_TAG_LL, VisionConstants.TRUSTWORTHY_TAGS);
    // LimelightHelpers.SetFiducialIDFiltersOverride(VisionConstants.FRONT_RIGHT_APRIL_TAG_LL, VisionConstants.TRUSTWORTHY_TAGS);
    SmartDashboard.putNumber("Set P Value",ElevatorConstants.kIncrementalPostionP);
    SmartDashboard.putNumber("Set I Value",ElevatorConstants.kIncrementalPostionI);
    SmartDashboard.putNumber("Set D Value",ElevatorConstants.kIncrementalPositionD);
  }
  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   */
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
    mDriveControls.o_reportBotControlData();
  }

  /** This function is called once each time the robot enters Disabled mode. */
  @Override
  public void disabledInit() {
    //RobotContainer.getInstance().m_Climb.followerClimbSparkMax.configure(Configs.ClimbSubsystem.climbFollowerBrakeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
  }

  @Override
  public void disabledPeriodic() {
    LimelightHelpers.SetIMUMode(VisionConstants.FRONT_LEFT_APRIL_TAG_LL, 0);
    LimelightHelpers.SetIMUMode(VisionConstants.FRONT_RIGHT_APRIL_TAG_LL, 0);
  }

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {

    Pose2d BlueEFAlgaePose2d = new Pose2d((new Translation2d(5.686646, 4.0200)),
                                              new Rotation2d(180 * (Math.PI/180)));
    Pose2d RedEFAlgaePose2d = new Pose2d((new Translation2d(11.850906, 4.0200)), new Rotation2d(0 * (Math.PI/180)));//testing pose
    ///Pose2d blueECoralPose2d = new Pose2d((new Translation2d(5.686646, 3.8549)), //good 10/22
    // new Rotation2d(180 * (Math.PI/180)));

    //C side Poses
    Pose2d blueECoralPose2d = new Pose2d((new Translation2d(4.94939989, 2.89768804)), //good 10/22
    new Rotation2d(120 * (Math.PI/180)));
    Pose2d redECoralPose2d = new Pose2d ((new Translation2d(12.59816289, 5.15411196)), //good 10/22
    new Rotation2d(-60 * (Math.PI/180)));
    Pose2d blueFCoralPose2d = new Pose2d((new Translation2d(5.23550811, 3.06278804)), //good 10/22
    new Rotation2d(120 * (Math.PI/180)));
    Pose2d redFCoralPose2d = new Pose2d ((new Translation2d(12.31215311, 4.98901196)), //good 10/22
    new Rotation2d(-60 * (Math.PI/180)));
    Pose2d redCoralSecondOutPose2d = new Pose2d ((new Translation2d(12.899, 1.538)), //good 10/22
    new Rotation2d(120 * (Math.PI/180)));

    //E side Poses
    Pose2d blueICoralPose2d = new Pose2d((new Translation2d(5.23550811, 4.98901196)), //good 10/22
    new Rotation2d(-120 * (Math.PI/180)));
    Pose2d redICoralPose2d = new Pose2d ((new Translation2d(12.31215311, 3.06278804)), //good 10/22
    new Rotation2d(60 * (Math.PI/180)));
    Pose2d blueJCoralPose2d = new Pose2d((new Translation2d(4.94939989, 5.15411196)), //fixed rot 10/22
    new Rotation2d(-120 * (Math.PI/180)));
    Pose2d redJCoralPose2d = new Pose2d((new Translation2d(12.59816289, 2.89768804)), //fixed rot 10/22
    new Rotation2d(60 * (Math.PI/180)));

    //Advance Auto Pose
    //2.41
    Pose2d redLeftAIntialPose2d = new Pose2d((new Translation2d(14.349, 5.625)), //fixed rot 10/22
    new Rotation2d(-120 * (Math.PI/180)));
    //1.46
    Pose2d redLeftASecondPose2d = new Pose2d((new Translation2d(14.805, 4.463)), //fixed rot 10/22
    new Rotation2d(180 * (Math.PI/180)));
    //.9
    Pose2d redBCoralPose2d = new Pose2d ((new Translation2d(14.256818, 4.1851)), //good 10/22
    new Rotation2d(180 * (Math.PI/180)));
    //2.21
    Pose2d redLeftPlayerStationPose2d = new Pose2d ((new Translation2d(15.872, 7.507)), //good 10/22 
    new Rotation2d(-125 * (Math.PI/180)));
    //2.21 then goes toward redLeftASecondPose2d then another 1.05
    Pose2d redACoralPose2d = new Pose2d ((new Translation2d(14.256818, 3.8549)), //good 10/22 
    new Rotation2d(180 * (Math.PI/180)));

    //2.34s
    Pose2d redRightAInitialPose2d = new Pose2d ((new Translation2d(14.134, 2.309)), //good 10/22 
    new Rotation2d(120 * (Math.PI/180)));
    //1.7s
    Pose2d redRightASecondPose2d = new Pose2d ((new Translation2d(14.805, 3.554)), //good 10/22 
    new Rotation2d(180 * (Math.PI/180)));

    Pose2d redRightPlayerStationPose2d = new Pose2d ((new Translation2d(15.944, 0.567)), //good 10/22 
    new Rotation2d(125 * (Math.PI/180)));

    
    //BlueSide Poses
    //2.51
    Pose2d blueLeftAIntialPose2d = new Pose2d((new Translation2d(3.560, 5.625)), //fixed rot 10/22
    new Rotation2d(-60 * (Math.PI/180)));
    //1.84
    Pose2d blueLeftASecondPose2d = new Pose2d((new Translation2d(2.697, 4.463)), //fixed rot 10/22
    new Rotation2d(0));
    //1.00
    Pose2d blueACoralPose2d = new Pose2d((new Translation2d(3.2812, 4.190238)), // good 10/22
    new Rotation2d(0));
    //2.33
    Pose2d blueLeftHumanPlayerPose2d = new Pose2d((new Translation2d(1.678, 7.495)), // good 10/22
    new Rotation2d(-55 * (Math.PI/180)));

    Pose2d blueLeftKickPose2d = new Pose2d((new Translation2d(4.867, 5.721 )), new Rotation2d( -120 * (Math.PI/180)));

    //Blue Right
    //2.5
    Pose2d blueRightAIntialPose2d = new Pose2d((new Translation2d(3.560, 2.509)), //fixed rot 10/22
    new Rotation2d(60 * (Math.PI/180)));
    //1.8
    Pose2d blueRightASecondPose2d = new Pose2d((new Translation2d(2.697, 3.554)), //fixed rot 10/22
    new Rotation2d(0));
    //1.04s
    Pose2d blueBCoralPose2d = new Pose2d((new Translation2d(3.2812, 3.861562)), //good 10/22
    new Rotation2d(0));

    Pose2d blueRightHumanPlayerPose2d = new Pose2d((new Translation2d(1.630, 0.579)), // good 10/22
    new Rotation2d(55 * (Math.PI/180)));

    //BlueLeftAA
    Pose2d blueLeftInitialOffPose2d = new Pose2d((new Translation2d(2.429, 5.305)), // good 10/22
    new Rotation2d(0 * (Math.PI/180))); // -> to blueACoralA2
    Pose2d blueLeftHumanPose2d = new Pose2d((new Translation2d(2.429, 5.305)), // good 10/22
    new Rotation2d(-55 * (Math.PI/180)));
    // Pose2d blueLeftInitialOffPose2d = new Pose2d((new Translation2d(2.429, 5.305)), // good 10/22
    // new Rotation2d(0 * (Math.PI/180)));

    //BlueRightAA
    Pose2d blueRightInitialOffPose2d = new Pose2d((new Translation2d(2.429, 2.745)), // good 10/22
    new Rotation2d(0 * (Math.PI/180)));
    Pose2d blueRightHumanPose2d = new Pose2d((new Translation2d(1.256, 0.969)), // good 10/22
    new Rotation2d(55 * (Math.PI/180)));



    AutoModeManager.updateAutoMode();
    
    if (AutoModeManager.m_autonomousCommand == null){
      if(AutoModeManager.desiredMode == AutoModeManager.DesiredMode.L1_MIDDLE_START){
        if(DriverStation.getAlliance().get() == Alliance.Blue){
          m_robotContainer.ReturnAutoCommand(BlueEFAlgaePose2d).schedule();
        }else{
          m_robotContainer.ReturnAutoCommand(RedEFAlgaePose2d).schedule();//testing
        }
      }else if(AutoModeManager.desiredMode == AutoModeManager.DesiredMode.C_SIDE_LEFT_L2){
        if(DriverStation.getAlliance().get() == Alliance.Blue){
          m_robotContainer.ReturnL2AutoCommand(blueECoralPose2d).schedule();//testing
        }else{
          m_robotContainer.ReturnL2AutoCommand(redECoralPose2d).schedule();//testing
        }
      }else if(AutoModeManager.desiredMode == AutoModeManager.DesiredMode.C_SIDE_RIGHT_L2){
          if(DriverStation.getAlliance().get() == Alliance.Blue){
            m_robotContainer.ReturnL2AutoCommand(blueFCoralPose2d).schedule();//testing
          }else{
            m_robotContainer.ReturnL2AutoCommand(redFCoralPose2d).schedule();//testing
          }
      }else if(AutoModeManager.desiredMode == AutoModeManager.DesiredMode.E_SIDE_LEFT_L2){
        if(DriverStation.getAlliance().get() == Alliance.Blue){
          m_robotContainer.ReturnL2AutoCommand(blueICoralPose2d).schedule();//testing
        }else{
          m_robotContainer.ReturnL2AutoCommand(redICoralPose2d).schedule();//testing
        }
      }else if(AutoModeManager.desiredMode == AutoModeManager.DesiredMode.E_SIDE_RIGHT_L2){
        if(DriverStation.getAlliance().get() == Alliance.Blue){
          //m_robotContainer.ReturnL2AutoCommand(blueJCoralPose2d).schedule();
          m_robotContainer.ReturnL2SimpleHumanCommand(blueJCoralPose2d, blueLeftKickPose2d, blueLeftHumanPlayerPose2d, blueLeftKickPose2d, blueJCoralPose2d, 3.5, 0.1, 2.5, 2.5).schedule();//testing
        }else{
          //m_robotContainer.ReturnL2AutoCommand(redJCoralPose2d).schedule();
          m_robotContainer.ReturnL2HumanCommand(redJCoralPose2d, redCoralSecondOutPose2d, redRightPlayerStationPose2d, redRightASecondPose2d, redACoralPose2d, 3.5, 0.1, 2.5, 2.5).schedule();
          //m_robotContainer.PIDAtonomousMoveTwiceToPose(redCoralSecondOutPose2d, redLeftPlayerStationPose2d).schedule();
          //testing
        }
      }else if(AutoModeManager.desiredMode == AutoModeManager.DesiredMode.LEFT_A2){
        if(DriverStation.getAlliance().get() == Alliance.Blue){
          m_robotContainer.aSideL2AutoCommand(blueLeftAIntialPose2d, blueLeftASecondPose2d, blueACoralPose2d, blueLeftHumanPlayerPose2d).schedule();//testing
        }else{
          m_robotContainer.aSideL2AutoCommand(redRightAInitialPose2d, redRightASecondPose2d, redACoralPose2d, redRightPlayerStationPose2d).schedule();//testing
      }
      }else if(AutoModeManager.desiredMode == AutoModeManager.DesiredMode.RIGHT_A2){
        if(DriverStation.getAlliance().get() == Alliance.Blue){
          m_robotContainer.aSideL2AutoCommand(blueRightAIntialPose2d, blueRightASecondPose2d, blueBCoralPose2d, blueRightHumanPlayerPose2d).schedule();//testing
        }else{
          m_robotContainer.aSideL2AutoCommand(redLeftAIntialPose2d, redLeftASecondPose2d, redBCoralPose2d, redLeftPlayerStationPose2d).schedule();//testing
        }
      }else if(AutoModeManager.desiredMode == AutoModeManager.DesiredMode.RIGHT_A2){
        if(DriverStation.getAlliance().get() == Alliance.Blue){
          m_robotContainer.aSideL2AutoCommand(blueRightAIntialPose2d, blueRightASecondPose2d, blueBCoralPose2d, blueRightHumanPlayerPose2d).schedule();//testing
        }else{
          m_robotContainer.aSideL2AutoCommand(redLeftAIntialPose2d, redLeftASecondPose2d, redBCoralPose2d, redLeftPlayerStationPose2d).schedule();//testing
        }
      }else if(AutoModeManager.desiredMode == AutoModeManager.DesiredMode.BLUE_LEFT_AA){
        if(DriverStation.getAlliance().get() == Alliance.Blue){
          m_robotContainer.returnBlueLeftAACommand(blueRightInitialOffPose2d, 2.5, blueBCoralPose2d, 
          1.5, blueRightHumanPose2d, 2.3, 1.0, blueACoralPose2d, 
          kDefaultPeriod, kDefaultPeriod);
          // m_robotContainer.aSideL2AutoCommand(blueRightAIntialPose2d, blueRightASecondPose2d, blueBCoralPose2d, blueRightHumanPlayerPose2d).schedule();//testing
        }else{
          // m_robotContainer.aSideL2AutoCommand(redLeftAIntialPose2d, redLeftASecondPose2d, redBCoralPose2d, redLeftPlayerStationPose2d).schedule();//testing
        }
      }else if(AutoModeManager.desiredMode == AutoModeManager.DesiredMode.BLUE_RIGHT_AA){
          if(DriverStation.getAlliance().get() == Alliance.Blue){
            m_robotContainer.returnBlueLeftAACommand(blueLeftInitialOffPose2d, 2.5, blueACoralPose2d, 
          1.5, blueLeftHumanPose2d, 2.3, 1.0, blueACoralPose2d, 
          2.5, 1.8);
          }else{

          }
        }
      
      //m_robotContainer.ReturnAutoCommand(BlueEFAlgaePose2d).schedule();
      //m_robotContainer.ReturnL3AutoCommand(blueECoralPose2d).schedule();
    } else {
      AutoModeManager.m_autonomousCommand.schedule();
    }
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    LimelightHelpers.SetIMUMode(VisionConstants.FRONT_LEFT_APRIL_TAG_LL, 3);
    LimelightHelpers.SetIMUMode(VisionConstants.FRONT_RIGHT_APRIL_TAG_LL, 3);
  }

  @Override
  public void teleopInit() {
    mControlBoard.verifyPossibleControllerInit();
    mDriveControls.selectControllerOption();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    LimelightHelpers.SetIMUMode(VisionConstants.FRONT_LEFT_APRIL_TAG_LL, 3);
    LimelightHelpers.SetIMUMode(VisionConstants.FRONT_RIGHT_APRIL_TAG_LL, 3);
    SmartDashboard.putString("ALLIANCE", RobotContainer.isRedAlliance().get().toString());
    SmartDashboard.putNumber("MATCH TIME", DriverStation.getMatchTime());
    mControlBoard.verifyControllerIntegrity();
    mControlBoard.update();
    mDriveControls.RunRobot();
  }
        

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}
}
