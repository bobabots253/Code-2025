// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import java.lang.reflect.Array;

import com.studica.frc.AHRS;
import com.studica.frc.AHRS.NavXComType;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.util.PathPlannerLogging;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;
import com.pathplanner.lib.util.PathPlannerLogging;

import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.kinematics.struct.SwerveModuleStateStruct;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.DoublePublisher;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.StructArrayPublisher;
import edu.wpi.first.networktables.StructPublisher;
import edu.wpi.first.util.WPIUtilJNI;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DriveConstants;
import frc.robot.limelights.VisionSubsystem;
import frc.robot.LimelightHelpers;
import frc.robot.RobotContainer;
import frc.utils.SwerveUtils;
import edu.wpi.first.wpilibj.SPI;

public class DriveSubsystem extends SubsystemBase {
  private static DriveSubsystem instance;

  // Create MAXSwerveModules
  private final MAXSwerveModule m_frontLeft = new MAXSwerveModule(
      DriveConstants.kFrontLeftDrivingCanId,
      DriveConstants.kFrontLeftTurningCanId,
      DriveConstants.kFrontLeftChassisAngularOffset);

  private final MAXSwerveModule m_frontRight = new MAXSwerveModule(
      DriveConstants.kFrontRightDrivingCanId,
      DriveConstants.kFrontRightTurningCanId,
      DriveConstants.kFrontRightChassisAngularOffset);

  private final MAXSwerveModule m_rearLeft = new MAXSwerveModule(
      DriveConstants.kRearLeftDrivingCanId,
      DriveConstants.kRearLeftTurningCanId,
      DriveConstants.kBackLeftChassisAngularOffset);

  private final MAXSwerveModule m_rearRight = new MAXSwerveModule(
      DriveConstants.kRearRightDrivingCanId,
      DriveConstants.kRearRightTurningCanId,
      DriveConstants.kBackRightChassisAngularOffset);

  // The gyro sensor
  private final static AHRS Nav_x = new AHRS(NavXComType.kMXP_SPI);
  public final boolean fieldFlipped = DriverStation.getAlliance().isPresent() && DriverStation.getAlliance().get() == Alliance.Red;
  // Locations for the swerve drive modules relative to the robot center.
  // Distance in meters
  Translation2d m_frontLeftLocation = new Translation2d(0.4086, 0.4086);
  Translation2d m_frontRightLocation = new Translation2d(0.4086, -0.4086);
  Translation2d m_backLeftLocation = new Translation2d(-0.4086, 0.4086);
  Translation2d m_backRightLocation = new Translation2d(-0.4086, -0.4086);

  //Swerve Kinematics used for the AUTO
  SwerveDriveKinematics m_kinematics = new SwerveDriveKinematics(
    m_frontLeftLocation, m_frontRightLocation, m_backLeftLocation, m_backRightLocation
  );

  // Slew rate filter variables for controlling lateral acceleration
  private double m_currentRotation = 0.0;
  private double m_currentTranslationDir = 0.0;
  private double m_currentTranslationMag = 0.0;

  private SlewRateLimiter m_magLimiter = new SlewRateLimiter(DriveConstants.kMagnitudeSlewRate);
  private SlewRateLimiter m_rotLimiter = new SlewRateLimiter(DriveConstants.kRotationalSlewRate);
  private double m_prevTime = WPIUtilJNI.now() * 1e-6;
  final Field2d m_fieldGyro = new Field2d();
  final Field2d m_fieldVision = new Field2d();
  public static Pose2d refinedVisionPose;

  // Odometry class for tracking robot pose
  SwerveDriveOdometry m_odometry = new SwerveDriveOdometry(
      DriveConstants.kDriveKinematics,
      Rotation2d.fromDegrees(-Nav_x.getAngle()),
      new SwerveModulePosition[] {
          m_frontLeft.getPosition(),
          m_frontRight.getPosition(),
          m_rearLeft.getPosition(),
          m_rearRight.getPosition()
      });
      
      SwerveDrivePoseEstimator odometryVision = new SwerveDrivePoseEstimator(DriveConstants.kDriveKinematics,
      (fieldFlipped ? getInitialFlippeRotation2d(): getRotation2DHeading()), new SwerveModulePosition[] {
              m_frontLeft.getPosition(),
              m_frontRight.getPosition(),
              m_rearLeft.getPosition(),
              m_rearRight.getPosition()
      }, new Pose2d(),
      VecBuilder.fill(0.01, 0.01, Units.degreesToRadians(5)),
      VecBuilder.fill(0.75, 0.75, 99999999));

      SwerveDrivePoseEstimator refinedodometryVision = new SwerveDrivePoseEstimator(DriveConstants.kDriveKinematics,
      (fieldFlipped ? getInitialFlippeRotation2d(): getRotation2DHeading()), new SwerveModulePosition[] {
              m_frontLeft.getPosition(),
              m_frontRight.getPosition(),
              m_rearLeft.getPosition(),
              m_rearRight.getPosition()
      }, new Pose2d(),
      VecBuilder.fill(0.01, 0.01, Units.degreesToRadians(5)),
      VecBuilder.fill(0.75, 0.75, 99999999));

  /** Creates a new DriveSubsystem. */
  public DriveSubsystem() {
    // Configure Autobuilder (Auto manager/handler) for the routine
    try{
      RobotConfig config = RobotConfig.fromGUISettings();
      //getPoseVision
    AutoBuilder.configure(
      this::getPose, // Robot pose supplier
      this::resetOdometry, // Method to reset odometry (will be called if your auto has a starting pose)
      this::getRobotRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE
      (speeds, feedforwards) -> driveRobotRelative(speeds), // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
      new PPHolonomicDriveController( // HolonomicPathFollowerConfig, this should likely live in your Constants class
              new PIDConstants(Constants.ModuleConstants.kDrivingP, Constants.ModuleConstants.kDrivingI, Constants.ModuleConstants.kDrivingD), // Translation PID constants
              new PIDConstants(Constants.ModuleConstants.kTurningP, Constants.ModuleConstants.kTurningI, Constants.ModuleConstants.kTurningD) // Rotation PID constants
      ),
      config,
      () -> {
        var alliance = DriverStation.getAlliance();
        if (alliance.isPresent()) {
          return alliance.get() == DriverStation.Alliance.Red;
        }
        return false;
      },
      this // Reference to this subsystem to set requirements
    );

        } catch (Exception e) {
      // Handle exception as needed
      e.printStackTrace();
    }
    
  }

  public static synchronized DriveSubsystem getInstance() {
    if (instance == null) {
        instance = new DriveSubsystem();
    }
    return instance;
}
  @Override
  public void periodic() {
    SmartDashboard.putBoolean("NavX Exists", Nav_x.isConnected());
    // Update the odometry in the periodic block
    //Main Odometry Update
    m_odometry.update(
        Rotation2d.fromDegrees(-Nav_x.getAngle()),
        new SwerveModulePosition[] {
            m_frontLeft.getPosition(),
            m_frontRight.getPosition(),
            m_rearLeft.getPosition(),
            m_rearRight.getPosition()
        });

    odometryVision.update(
      Rotation2d.fromDegrees(-Nav_x.getAngle()),
      new SwerveModulePosition[] {
            m_frontLeft.getPosition(),
            m_frontRight.getPosition(),
            m_rearLeft.getPosition(),
            m_rearRight.getPosition()
      });
    
      refinedodometryVision.update(
        Rotation2d.fromDegrees(-Nav_x.getAngle()),
        new SwerveModulePosition[] {
              m_frontLeft.getPosition(),
              m_frontRight.getPosition(),
              m_rearLeft.getPosition(),
              m_rearRight.getPosition()
        });
    
    refinedVisionPose = VisionSubsystem.getInstance().getEstimatedPose();

    try{
      refinedodometryVision.addVisionMeasurement(refinedVisionPose, Timer.getFPGATimestamp() - NTlatency);
    } catch ( Exception err){
      System.out.println("Couldn't Return Refined Vision");
    }

    SmartDashboard.putData("Field Gyro", m_fieldGyro);
    SmartDashboard.putData("Field Vision", m_fieldVision);
    m_fieldGyro.setRobotPose(m_odometry.getPoseMeters());
    // m_fieldVision.setRobotPose(
    //   odometryVision.getEstimatedPosition().getX(),
    //   odometryVision.getEstimatedPosition().getY(),
    //   (Nav_x.getRotation2d())
    // );
    m_fieldVision.setRobotPose(odometryVision.getEstimatedPosition());

    try {
      addVisionMeasurement("limelight");
    }
    catch(Exception erException) {
      System.out.println("No Valid Limelight Targets");
    }


    double[] driveMotorCurrent = {
      m_frontLeft.getDriveCurrent(), m_frontRight.getDriveCurrent(),
      m_rearLeft.getDriveCurrent(), m_rearRight.getDriveCurrent()
    };

    double[] turnMotorCurrent = {
      m_frontLeft.getTurnCurrent(), m_frontRight.getTurnCurrent(),
      m_rearLeft.getTurnCurrent(), m_rearRight.getTurnCurrent()
    };

    SmartDashboard.putNumberArray("Turn motor currents", turnMotorCurrent);
    SmartDashboard.putNumberArray("Drive motor currents", driveMotorCurrent);

    double[] pose = {getPose().getX(), getPose().getY(), getPose().getRotation().getDegrees()};
    SmartDashboard.putNumberArray("POSE", pose);

  }
  
  double yaw;
  double NTlatency = 0.003;
  public void addVisionMeasurement(String limelight) {
      LimelightHelpers.SetRobotOrientation(limelight, getHeading(), 0,
              0, 0, 0, 0);
      if (LimelightHelpers.getTV(limelight)) {
          LimelightHelpers.PoseEstimate mt2 = LimelightHelpers.getBotPoseEstimate_wpiBlue_MegaTag2(limelight);
          if (!(Math.abs(-Nav_x.getRate()) > 540) && !(mt2.tagCount == 0)) {
              odometryVision.setVisionMeasurementStdDevs(VecBuilder.fill(.7, .7, 9999999));
              odometryVision.addVisionMeasurement(mt2.pose, Timer.getFPGATimestamp() - NTlatency); //Timer.getFPGATimestamp()mt2.timestampSeconds
          }
      }
  }

  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

    public Pose2d getPoseVision() {
    return odometryVision.getEstimatedPosition();
  }

  // Return Chassis Speed
  public ChassisSpeeds getRobotRelativeSpeeds() {
    return m_kinematics.toChassisSpeeds(m_frontLeft.getState(), m_frontRight.getState(), m_rearLeft.getState(), m_rearRight.getState());
  }

  public Pose2d getRefinedPoseVision(){
    return refinedVisionPose;
  }

  //Drive !ROBOT! Centric for Auto
  public void driveRobotRelative(ChassisSpeeds speeds) {
    var swerveModuleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(speeds);
    SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, DriveConstants.kMaxSpeedMetersPerSecond);
    m_frontLeft.setDesiredState(swerveModuleStates[0]);
    m_frontRight.setDesiredState(swerveModuleStates[1]);
    m_rearLeft.setDesiredState(swerveModuleStates[2]);
    m_rearRight.setDesiredState(swerveModuleStates[3]);
  }
    //Works for translation and rotations (Translation 2D and Rotation 2D?==[pnk ]  } 
  /**\
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    m_odometry.resetPosition(
        Rotation2d.fromDegrees(-Nav_x.getAngle()),
        new SwerveModulePosition[] {
            m_frontLeft.getPosition(),
            m_frontRight.getPosition(),
            m_rearLeft.getPosition(),
            m_rearRight.getPosition()
        },
        pose);
  }

  /**
   * Method to drive the robot using joystick info.
   *
   * @param xSpeed        Speed of the robot in the x direction (forward).
   * @param ySpeed        Speed of the robot in the y direction (sideways).
   * @param rot           Angular rate of the robot.
   * @param fieldRelative Whether the provided x and y speeds are relative to the
   *                      field.
   * @param rateLimit     Whether to enable rate limiting for smoother control.
   */
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative, boolean rateLimit) {
    
    double xSpeedCommanded;
    double ySpeedCommanded;

    if (rateLimit) {
      // Convert XY to polar for rate limiting
      double inputTranslationDir = Math.atan2(ySpeed, xSpeed);
      double inputTranslationMag = Math.sqrt(Math.pow(xSpeed, 2) + Math.pow(ySpeed, 2));

      // Calculate the direction slew rate based on an estimate of the lateral acceleration
      double directionSlewRate;
      if (m_currentTranslationMag != 0.0) {
        directionSlewRate = Math.abs(DriveConstants.kDirectionSlewRate / m_currentTranslationMag);
      } else {
        directionSlewRate = 500.0; //some high number that means the slew rate is effectively instantaneous
      }
      

      double currentTime = WPIUtilJNI.now() * 1e-6;
      double elapsedTime = currentTime - m_prevTime;
      double angleDif = SwerveUtils.AngleDifference(inputTranslationDir, m_currentTranslationDir);
      if (angleDif < 0.45*Math.PI) {
        m_currentTranslationDir = SwerveUtils.StepTowardsCircular(m_currentTranslationDir, inputTranslationDir, directionSlewRate * elapsedTime);
        m_currentTranslationMag = m_magLimiter.calculate(inputTranslationMag);
      }
      else if (angleDif > 0.85*Math.PI) {
        if (m_currentTranslationMag > 1e-4) { //some small number to avoid floating-point errors with equality checking
          // keep currentTranslationDir unchanged
          m_currentTranslationMag = m_magLimiter.calculate(0.0);
        }
        else {
          m_currentTranslationDir = SwerveUtils.WrapAngle(m_currentTranslationDir + Math.PI);
          m_currentTranslationMag = m_magLimiter.calculate(inputTranslationMag);
        }
      }
      else {
        m_currentTranslationDir = SwerveUtils.StepTowardsCircular(m_currentTranslationDir, inputTranslationDir, directionSlewRate * elapsedTime);
        m_currentTranslationMag = m_magLimiter.calculate(0.0);
      }
      m_prevTime = currentTime;
      
      xSpeedCommanded = m_currentTranslationMag * Math.cos(m_currentTranslationDir);
      ySpeedCommanded = m_currentTranslationMag * Math.sin(m_currentTranslationDir);
      m_currentRotation = m_rotLimiter.calculate(rot);


    } else {
      xSpeedCommanded = xSpeed;
      ySpeedCommanded = ySpeed;
      m_currentRotation = rot;
    }

    // Convert the commanded speeds into the correct units for the drivetrain
    double xSpeedDelivered = xSpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond;
    double ySpeedDelivered = ySpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond;
    double rotDelivered = m_currentRotation * DriveConstants.kMaxAngularSpeed;

    SwerveModuleState[] swerveModuleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(
        fieldRelative
            ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered, Rotation2d.fromDegrees(-Nav_x.getAngle()))
            : new ChassisSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered));
    SwerveDriveKinematics.desaturateWheelSpeeds(
        swerveModuleStates, DriveConstants.kMaxSpeedMetersPerSecond);
    m_frontLeft.setDesiredState(swerveModuleStates[0]);
    m_frontRight.setDesiredState(swerveModuleStates[1]);
    m_rearLeft.setDesiredState(swerveModuleStates[2]);
    m_rearRight.setDesiredState(swerveModuleStates[3]);
  }

  /**
   * Sets the wheels into an X formation to prevent movement.
   */
  public void setX() {
    m_frontLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
    m_frontRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
    m_rearLeft.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(-45)));
    m_rearRight.setDesiredState(new SwerveModuleState(0, Rotation2d.fromDegrees(45)));
  }

  /**
   * Sets the swerve ModuleStates.
   *
   * @param desiredStates The desired SwerveModule states.
   */
  public void setModuleStates(SwerveModuleState[] desiredStates) {
    SwerveDriveKinematics.desaturateWheelSpeeds(
        desiredStates, DriveConstants.kMaxSpeedMetersPerSecond);
    m_frontLeft.setDesiredState(desiredStates[0]);
    m_frontRight.setDesiredState(desiredStates[1]);
    m_rearLeft.setDesiredState(desiredStates[2]);
    m_rearRight.setDesiredState(desiredStates[3]);
  }

  /** Resets the drive encoders to currently read a position of 0. */
  public void resetEncoders() {
    m_frontLeft.resetEncoders();
    m_rearLeft.resetEncoders();
    m_frontRight.resetEncoders();
    m_rearRight.resetEncoders();
  }

  /** Zeroes the heading of the robot. */
  public void zeroHeading() {
    Nav_x.reset();
  }

  // public static void zeroGyro(){
  //   Nav_x.zeroYaw();
  // }

  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from -180 to 180
   */
  public double getHeading() {
    return Rotation2d.fromDegrees(-Nav_x.getAngle()).getDegrees();
  }

  public Rotation2d getRotation2DHeading(){
    return Rotation2d.fromDegrees(-Nav_x.getAngle());
  }

  public Rotation2d getInitialFlippeRotation2d(){
    return Rotation2d.fromDegrees(-Nav_x.getAngle()).plus(Rotation2d.fromRadians(Math.PI));
  }

  public Pose2d getCurrentPose() {
    return odometryVision.getEstimatedPosition();
  }

  public void Xmode(){
      SwerveModuleState[] lockStates = {
        new SwerveModuleState(0, Rotation2d.fromDegrees(45)),
        new SwerveModuleState(0, Rotation2d.fromDegrees(-45)),
        new SwerveModuleState(0, Rotation2d.fromDegrees(-45)),
        new SwerveModuleState(0, Rotation2d.fromDegrees(45))
      };
  setModuleStates(lockStates);
}

//   public SwerveModulePosition[] returnSwerverModulePositions(){
//     return new SwerveModulePosition[] {
//       m_frontLeft.getPosition(),
//       m_frontRight.getPosition(),
//       m_rearLeft.getPosition(),
//       m_rearRight.getPosition()
// };
//   }
  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return Nav_x.getRate() * (DriveConstants.kGyroReversed ? -1.0 : 1.0);
  }

}
