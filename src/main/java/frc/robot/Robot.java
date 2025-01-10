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
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Constants.OIConstants;
import frc.robot.Autonomous.AutoModeManager;
import frc.robot.Autonomous.AutoModeManager.DesiredMode;
import frc.robot.Bobaboard.BotControls;
import frc.robot.Bobaboard.ControlHub;
import frc.robot.subsystems.DriveSubsystem;
import frc.utils.CoordinateSpace;

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
  public void disabledInit() {}

  @Override
  public void disabledPeriodic() {}

  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    AutoModeManager.updateAutoMode();
    AutoModeManager.m_autonomousCommand.schedule();
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    mControlBoard.verifyPossibleControllerInit();
    mDriveControls.selectControllerOption();
  }

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    SmartDashboard.putString("ALLIANCE", RobotContainer.isRedAlliance().get().toString());
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
