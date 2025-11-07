package frc.robot.subsystems.swerve;

import com.ctre.phoenix6.hardware.Pigeon2;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.config.PIDConstants;
import com.pathplanner.lib.config.RobotConfig;
import com.pathplanner.lib.controllers.PPHolonomicDriveController;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.util.WPIUtilJNI;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.swerve.SwerveConstants.DriveConstants;
import frc.robot.subsystems.swerve.SwerveConstants.ModuleConstants;
import frc.utils.SwerveUtils;

public class SwerveSubsystem extends SubsystemBase{
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

    private final CommandXboxController driverController = new CommandXboxController(0);

  private Pigeon2 m_gyroPigeon = new Pigeon2(25);
  public final boolean isBlueAlliance = DriverStation.getAlliance().get() == Alliance.Blue;

  Translation2d m_frontLeftLocation = new Translation2d(0.4086, 0.4086);
  Translation2d m_frontRightLocation = new Translation2d(0.4086, -0.4086);
  Translation2d m_backLeftLocation = new Translation2d(-0.4086, 0.4086);
  Translation2d m_backRightLocation = new Translation2d(-0.4086, -0.4086);

  //Swerve Kinematics used for transposing robotRelative and fieldRelative Chassiss Speeds
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

  private PIDController m_rotationLockPIDController = new PIDController(
        ModuleConstants.kRotationLockTurningP, ModuleConstants.kRotationLockTurningI , ModuleConstants.kRotationLockTurningD );

  public static Pose2d refinedVisionPose;

  // Odometry class for tracking robot pose (Gyro-Only)
  SwerveDriveOdometry m_odometry = new SwerveDriveOdometry(
      DriveConstants.kDriveKinematics,
      getInitialRotation2dBasedOnAlliance(),
      new SwerveModulePosition[] {
          m_frontLeft.getPosition(),
          m_frontRight.getPosition(),
          m_rearLeft.getPosition(),
          m_rearRight.getPosition()
      });

     public enum WantedState {
        TELEOP_DRIVE,
        PATHPLANNER_AUTO_PATH,
        ROTATION_LOCK,
        HOLONOMIC_DRIVE_TO_POINT,
        IDLE
    }

    public enum SystemState {
        TELEOP_DRIVE,
        PATHPLANNER_AUTO_PATH,
        ROTATION_LOCK,
        HOLONOMIC_DRIVE_TO_POINT,
        IDLE
    }

    private SystemState systemState = SystemState.IDLE;
    private WantedState wantedState = WantedState.IDLE;
    private WantedState previousWantedState = WantedState.IDLE;

    private Rotation2d desiredRotationForRotationLockState;
    private Pose2d desiredPoseForDriveToPoint = new Pose2d();
    private CommandXboxController controller;


    public SwerveSubsystem() {
        try{
        RobotConfig config = RobotConfig.fromGUISettings();
        AutoBuilder.configure(
        this::getGyroPose, // Robot pose supplier
        this::resetOdometry, // Method to reset odometry (will be called if your auto has a starting pose)
        this::getRobotRelativeFromFieldRelativeSpeeds, // ChassisSpeeds supplier. MUST BE ROBOT RELATIVE //getRobotRelativeSpeeds
        (speeds, feedforwards) -> driveRobotRelative(speeds), // Method that will drive the robot given ROBOT RELATIVE ChassisSpeeds
        new PPHolonomicDriveController( // HolonomicPathFollowerConfig, this should likely live in your Constants class
                new PIDConstants(SwerveConstants.ModuleConstants.kDrivingP, SwerveConstants.ModuleConstants.kDrivingI, SwerveConstants.ModuleConstants.kDrivingD), // Translation PID constants
                new PIDConstants(SwerveConstants.ModuleConstants.kTurningP, SwerveConstants.ModuleConstants.kTurningI, SwerveConstants.ModuleConstants.kTurningD) // Rotation PID constants
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

    @Override
    public void periodic() {
        systemState = handleStateTransition();

        m_odometry.update(
        getInitialRotation2dBasedOnAlliance(),
        new SwerveModulePosition[] {
            m_frontLeft.getPosition(),
            m_frontRight.getPosition(),
            m_rearLeft.getPosition(),
            m_rearRight.getPosition()
        });

        applyStates();
    }

    private SystemState handleStateTransition() {
        if (wantedState != previousWantedState) {
            previousWantedState = wantedState;
        }       
        switch (wantedState) {
        case TELEOP_DRIVE:
             return SystemState.TELEOP_DRIVE;
        case PATHPLANNER_AUTO_PATH:
             return SystemState.PATHPLANNER_AUTO_PATH;
        case ROTATION_LOCK:
             return SystemState.ROTATION_LOCK;
        case HOLONOMIC_DRIVE_TO_POINT:
             return SystemState.HOLONOMIC_DRIVE_TO_POINT;
        default:
             return SystemState.IDLE;
        }
        
    }

    private void applyStates() {
        switch (systemState) {
            default:
            case TELEOP_DRIVE:
                getFCDriveCommand().schedule();
                break;
            case PATHPLANNER_AUTO_PATH:
                break;
            case ROTATION_LOCK:
                getFCDriveCommandWithRotationLock(desiredRotationForRotationLockState);
                break;
            case HOLONOMIC_DRIVE_TO_POINT:
                break;
        }
    }

    public void resetOdometry(Pose2d pose) {
        m_odometry.resetPosition(
            getInitialRotation2dBasedOnAlliance(),
            new SwerveModulePosition[] {
                m_frontLeft.getPosition(),
                m_frontRight.getPosition(),
                m_rearLeft.getPosition(),
                m_rearRight.getPosition()
            },
            pose);
    }

    /**
     * Sets the swerve ModuleStates.
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
                ? ChassisSpeeds.fromFieldRelativeSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered, getInitialRotation2dBasedOnAlliance())
                : new ChassisSpeeds(xSpeedDelivered, ySpeedDelivered, rotDelivered));
        SwerveDriveKinematics.desaturateWheelSpeeds(
            swerveModuleStates, DriveConstants.kMaxSpeedMetersPerSecond);
        m_frontLeft.setDesiredState(swerveModuleStates[0]);
        m_frontRight.setDesiredState(swerveModuleStates[1]);
        m_rearLeft.setDesiredState(swerveModuleStates[2]);
        m_rearRight.setDesiredState(swerveModuleStates[3]);
    }

    /**
     * Method to drive the robot with a Rotation Lock
     * 
     * @param xSpeed             Speed of the robot in the x direction (forward) [-1, 1].
     * @param ySpeed             Speed of the robot in the y direction (sideways) [-1, 1].
     * @param fieldRelativeAngle The desired field-relative angle to lock the robot's heading to.
     * @param rateLimit          Whether to enable rate limiting for smoother translation control.
     */
    public void driveWithAngleLock(double xSpeed, double ySpeed, Rotation2d fieldRelativeAngle, boolean rateLimit) {
        
        double xSpeedCommanded;
        double ySpeedCommanded;

        if (rateLimit) {
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

        } else {
            // No rate limiting, just use the raw inputs
            xSpeedCommanded = xSpeed;
            ySpeedCommanded = ySpeed;
        }

        
        // Set the setpoint for the PID controller to the desired angle
        m_rotationLockPIDController.setSetpoint(fieldRelativeAngle.getRadians());
        
        double rotOutput = m_rotationLockPIDController.calculate(
            getInitialRotation2dBasedOnAlliance().getRadians()
        );

        m_currentRotation = rotOutput;

        double xSpeedDelivered = xSpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond;
        double ySpeedDelivered = ySpeedCommanded * DriveConstants.kMaxSpeedMetersPerSecond;
        double rotDelivered = m_currentRotation * DriveConstants.kMaxAngularSpeed; // This now uses the PID output

        SwerveModuleState[] swerveModuleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(
            ChassisSpeeds.fromFieldRelativeSpeeds(
                xSpeedDelivered, ySpeedDelivered, rotDelivered, getInitialRotation2dBasedOnAlliance()
            )
        );

        SwerveDriveKinematics.desaturateWheelSpeeds(
            swerveModuleStates, DriveConstants.kMaxSpeedMetersPerSecond);
        m_frontLeft.setDesiredState(swerveModuleStates[0]);
        m_frontRight.setDesiredState(swerveModuleStates[1]);
        m_rearLeft.setDesiredState(swerveModuleStates[2]);
        m_rearRight.setDesiredState(swerveModuleStates[3]);
    }

      public void driveRobotRelative(ChassisSpeeds speeds) {
        var swerveModuleStates = DriveConstants.kDriveKinematics.toSwerveModuleStates(speeds);
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, DriveConstants.kMaxSpeedMetersPerSecond);
        m_frontLeft.setDesiredState(swerveModuleStates[0]);
        m_frontRight.setDesiredState(swerveModuleStates[1]);
        m_rearLeft.setDesiredState(swerveModuleStates[2]);
        m_rearRight.setDesiredState(swerveModuleStates[3]);
    }

    public Command getFCDriveCommand(){
    return new RunCommand(
      () -> {drive(
          -MathUtil.applyDeadband(driverController.getLeftY(), SwerveConstants.OIConstants.kDriveDeadband),
          -MathUtil.applyDeadband(driverController.getLeftX(), SwerveConstants.OIConstants.kDriveDeadband),
          -MathUtil.applyDeadband(driverController.getRightX(), SwerveConstants.OIConstants.kDriveDeadband),
          true, true);},
      this);
    }

    public Command getFCDriveCommandWithRotationLock(Rotation2d desiredRotationLockAngle){
    return new RunCommand(
      () -> {driveWithAngleLock(
          -MathUtil.applyDeadband(driverController.getLeftY(), SwerveConstants.OIConstants.kDriveDeadband),
          -MathUtil.applyDeadband(driverController.getLeftX(), SwerveConstants.OIConstants.kDriveDeadband),
          desiredRotationForRotationLockState,
          true);},
      this);
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

    public Pose2d getGyroPose(){
        return m_odometry.getPoseMeters();
    }

    public ChassisSpeeds getRobotRelativeSpeeds() {
        return m_kinematics.toChassisSpeeds(m_frontLeft.getState(), m_frontRight.getState(), m_rearLeft.getState(), m_rearRight.getState());
    }

    public ChassisSpeeds getRobotRelativeFromFieldRelativeSpeeds(){
        return ChassisSpeeds.fromFieldRelativeSpeeds(m_kinematics.toChassisSpeeds(m_frontLeft.getState(), m_frontRight.getState(), m_rearLeft.getState(), m_rearRight.getState()), getInitialRotation2dBasedOnAlliance());
    }

    public Rotation2d getGyroRotation2DBlue(){
        return m_gyroPigeon.getRotation2d();
    }

    public Rotation2d getGyroRotation2DRed(){
        return m_gyroPigeon.getRotation2d().plus(Rotation2d.fromRadians(Math.PI));
    }

    public double getGyroYawDegreesBlue(){
        return getGyroRotation2DBlue().getDegrees();
    }

    public double getGyroYawDegreesRed(){
        return getGyroRotation2DRed().getDegrees();
    }


    public Rotation2d getInitialRotation2dBasedOnAlliance(){
        return isBlueAlliance ? getGyroRotation2DBlue() :  getGyroRotation2DRed();
    }

    /** Zeroes the heading of the robot. */
    public void zeroHeading() {
        m_gyroPigeon.reset();
    }

    /** Resets the drive encoders to currently read a position of 0. */
    public void resetEncoders() {
        m_frontLeft.resetEncoders();
        m_rearLeft.resetEncoders();
        m_frontRight.resetEncoders();
        m_rearRight.resetEncoders();
    }

    public void setWantedState(WantedState state) {
        this.wantedState = state;
    }

}
