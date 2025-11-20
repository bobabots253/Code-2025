// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants.OIConstants;
import frc.robot.Autonomous.AutoModeManager;
import frc.robot.Bobaboard.ControlHub;
import frc.robot.commands.autoAlign;
import frc.robot.commands.autoAlignWithTimeout;
import frc.robot.commands.autonomousMovementAlign;
import frc.robot.commands.autonomousMovementAlignWithTimeOut;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.EndEffectorSubsystem;

/*
 * This class is where the bulk of the robot (including the subsystems) should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // 
  /*READ ME:
  The robot's subsystems
  */
  //public final TestSubsystem m_TestSubsystem;
  private static RobotContainer instance = null;
  public final AutoModeManager m_AutoModeManager;
  public final ControlHub m_ControlHub;
  public static DriveSubsystem m_robotDrive;
  public final ElevatorSubsystem m_Elevator;
  public final EndEffectorSubsystem m_Effector;
  public Command chosenSpinMove;

  /*READ ME:
  A static instance of the Robot Container with all its contents
  */
  public static RobotContainer getInstance() {
      if(instance == null) instance = new RobotContainer();
      return instance;
  }
  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    m_robotDrive = DriveSubsystem.getInstance();

    m_ControlHub = ControlHub.getInstance();
    m_Elevator = ElevatorSubsystem.getInstance();
    m_Effector = EndEffectorSubsystem.getInstance();

    m_AutoModeManager = new AutoModeManager();
    SmartDashboard.putData("autoModeSelection", AutoModeManager.mModeChooser);
    m_robotDrive.setDefaultCommand(new RunCommand(
      () -> m_robotDrive.drive(
          -MathUtil.applyDeadband(m_ControlHub.driverController.getLeftY(), OIConstants.kDriveDeadband),
          -MathUtil.applyDeadband(m_ControlHub.driverController.getLeftX(), OIConstants.kDriveDeadband),
          -MathUtil.applyDeadband(m_ControlHub.driverController.getRightX(), OIConstants.kDriveDeadband),
          true, true),
      m_robotDrive));

  }


  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link edu.wpi.first.wpilibj.GenericHID} or one of its
   * subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then calling
   * passing it to a
   * {@link JoystickButton}.
      * @return 
      */

   public Command stowElevatorCommand(){
    return new ParallelCommandGroup(
          new RunCommand(() -> {
            m_Elevator.setLazyElevatorState(States.ElevatorPos.STOW);
            }, m_Elevator)
        );
    }

   public Command tierOneElevatorCommand(){
    return new ParallelCommandGroup(
          new RunCommand(() -> {
            m_Elevator.setLazyElevatorState(States.ElevatorPos.L1Score);
            }, m_Elevator)
        );
    }

   public Command tierTwoElevatorCommand(){
    return new ParallelCommandGroup(
          new RunCommand(() -> {
            m_Elevator.setLazyElevatorState(States.ElevatorPos.L2Score);
            }, m_Elevator));  
    }

    public Command tierThreeElevatorCommand(){
      return new ParallelCommandGroup(
            new RunCommand(() -> {
              m_Elevator.setLazyElevatorState(States.ElevatorPos.L3SCORE);
              }, m_Elevator)
          );
      }

    public void cancelAutoAlignFunction(){
      Command currentCommand = m_robotDrive.getCurrentCommand();
      if(currentCommand != null && currentCommand != m_robotDrive.getDefaultCommand()){
        currentCommand.cancel();
      }
    }


    public Command tierTwoScoreCommand(){
      return new ParallelCommandGroup(
          new RunCommand(() -> {
            m_Elevator.setLazyElevatorState(States.ElevatorPos.L2Score);
          }, m_Elevator),
          new SequentialCommandGroup(
            new WaitCommand(1.5),
            new InstantCommand(() -> {
              m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
            }, m_Effector),
            new WaitCommand(1.2)
            
          )
      );
    }

    public Command tierOneHandoffCommand(){
      return new RunCommand(() -> {
              m_Elevator.setLazyElevatorState(States.ElevatorPos.L1HANDOFF);
              }, m_Elevator);
    }

    public Command doubleRollerCommand(){
      return new SequentialCommandGroup(
        new InstantCommand(() -> {
          m_Effector.setIntakeLazyPercentageOpenLoop(.7);
        }, m_Effector),
        new WaitCommand(.075),
        new InstantCommand(() -> {
          m_Effector.setIntakeLazyPercentageOpenLoop(0);
        }, m_Effector),
        new WaitCommand(.1),
        new InstantCommand(() -> {
          m_Effector.setIntakeLazyPercentageOpenLoop(.7);
        }, m_Effector),
        new WaitCommand(2),
        new InstantCommand(() -> {
          m_Effector.setIntakeLazyPercentageOpenLoop(0);
        }, m_Effector)
      );
      
    }

    public Command tripleRollerCommand(){
      return new SequentialCommandGroup(
        new InstantCommand(() -> {
          m_Effector.setIntakeLazyPercentageOpenLoop(.7);
        }, m_Effector),
        new WaitCommand(.003),
        new InstantCommand(() -> {
          m_Effector.setIntakeLazyPercentageOpenLoop(0);
        }, m_Effector),
        new WaitCommand(.003),
        new InstantCommand(() -> {
          m_Effector.setIntakeLazyPercentageOpenLoop(.7);
        }, m_Effector),
        new WaitCommand(0.01),
        new InstantCommand(() -> {
          m_Effector.setIntakeLazyPercentageOpenLoop(0);
        }, m_Effector),
        new WaitCommand(0.2),
        new InstantCommand(() -> {
          m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
        }, m_Effector)
      );
      
    }

    public Command tierOneExecuteCommand(){
      return new ParallelCommandGroup(
        new RunCommand(() -> { //.withTimeout()?
          m_Elevator.setLazyElevatorState(States.ElevatorPos.L1HANDOFF);
          }, m_Elevator),
          new SequentialCommandGroup(
            new WaitCommand(0.2),
            new ParallelCommandGroup(
              new RunCommand(() -> {
              m_Elevator.setLazyElevatorState(States.ElevatorPos.L1FLICK);
                }, m_Elevator),
                new SequentialCommandGroup(
                  new WaitCommand(0.3),
                  new InstantCommand(() -> {
                  m_Effector.setIntakeLazyPercentageOpenLoop(.5);
                    }, m_Effector).withTimeout(2.0)
              )
            )
          )
        );
    }

    

    public Command autoAlignCommand(Boolean isRight){
      Pose2d currentPos = m_robotDrive.getPose();
      Pose2d desiredPos;
      Field2d targetField = new Field2d();
      List<Pose2d> rightReefTags = new ArrayList<Pose2d>();
      List<Pose2d> leftReefTags = new ArrayList<Pose2d>();
      rightReefTags.add(FieldSetup.allianceReefBSupplier.get());
      rightReefTags.add(FieldSetup.allianceReefDSupplier.get());
      rightReefTags.add(FieldSetup.allianceReefFSupplier.get());
      rightReefTags.add(FieldSetup.allianceReefHSupplier.get());
      rightReefTags.add(FieldSetup.allianceReefJSupplier.get());
      rightReefTags.add(FieldSetup.allianceReefLSupplier.get());

      leftReefTags.add(FieldSetup.allianceReefASupplier.get());
      leftReefTags.add(FieldSetup.allianceReefCSupplier.get());
      leftReefTags.add(FieldSetup.allianceReefESupplier.get());
      leftReefTags.add(FieldSetup.allianceReefGSupplier.get());
      leftReefTags.add(FieldSetup.allianceReefISupplier.get());
      leftReefTags.add(FieldSetup.allianceReefKSupplier.get());
      if(isRight){
        desiredPos = currentPos.nearest(rightReefTags);
      } else {
        desiredPos = currentPos.nearest(leftReefTags);
      }
      targetField.setRobotPose(desiredPos);
      SmartDashboard.putData("TargetPose", targetField);
      return new autoAlign(m_robotDrive, desiredPos);
    }

    public Command autoAlignAlgaeCommad(){
      Pose2d currentPos = m_robotDrive.getPose();
      Pose2d desiredPos;
      Field2d targetField = new Field2d();
      List<Pose2d> algaeTags = new ArrayList<Pose2d>();
      algaeTags.add(FieldSetup.allianceAlgaeABSupplier.get());
      algaeTags.add(FieldSetup.allianceAlgaeCDSupplier.get());
      algaeTags.add(FieldSetup.allianceAlgaeEFSupplier.get());
      algaeTags.add(FieldSetup.allianceAlgaeGHSupplier.get());
      algaeTags.add(FieldSetup.allianceAlgaeIJSupplier.get());
      algaeTags.add(FieldSetup.allianceAlgaeKLSupplier.get());

      desiredPos = currentPos.nearest(algaeTags);
      targetField.setRobotPose(desiredPos);
      SmartDashboard.putData("TargetPose", targetField);
      return new autoAlign(m_robotDrive, desiredPos);
    }

      public Command coralRollersAuto(double requestedSpeed){
                   return Commands.sequence(
                       new InstantCommand(() -> {
                        m_Effector.setIntakeLazyPercentageOpenLoop(requestedSpeed);
                         }, m_Effector)
           );
       }
       
       public Command setElevatorStowAuto(){
           return Commands.sequence(
               new RunCommand(() -> {
                m_Elevator.setLazyElevatorState(States.ElevatorPos.STOW);
               }, m_Elevator)
            );
        }
       
        public Command setElevatorL1Auto(){
            return Commands.sequence(
                new RunCommand(() -> {
                  m_Elevator.setLazyElevatorState(States.ElevatorPos.L1Score);
                }, m_Elevator)
            );
        }
       
        public Command setElevatorL2Auto(){
            return Commands.sequence(
                new RunCommand(() -> {
                  m_Elevator.setLazyElevatorState(States.ElevatorPos.L2Score);
                }, m_Elevator)
            );
        }
       
        public Command setElevatorL3Auto(){
            return Commands.sequence(
                new RunCommand(() -> {
                  m_Elevator.setLazyElevatorState(States.ElevatorPos.L3SCORE);
                }, m_Elevator)
            );
        }
       
        public Command flickL1CoralAuto(){
            return Commands.sequence(
                coralRollersAuto(0.7).withTimeout(0.075),
                new WaitCommand(0.1),
                coralRollersAuto(0.7).withTimeout(2.0)
            );
        }
       
        public Command rollerDefaultScore(){
            return Commands.sequence(
                coralRollersAuto(1.0)
            );
        }
    
        public Command rollerDefaultStop(){
            return Commands.sequence(
                coralRollersAuto(0.0)
            );
        }
    
        public static Command spinMove(){
            return m_robotDrive.spinMoveCommand(.75);

        }

        public static Command inverseSpinMove(){
          return m_robotDrive.inverseSpinMoveCommand(0.75);
        }

    public static Command PIDPathfindToPose(Pose2d targetPose){
        return new autoAlign(m_robotDrive, targetPose);
    }

    public static Command pIDPathfindToPoseWithTimeout(Pose2d targetPose, double timeoutSeconds){
      return new autoAlignWithTimeout(m_robotDrive, targetPose, timeoutSeconds);
    }

    public static Command PIDAutonomousMoveToPose(Pose2d targetPose){
        return new autonomousMovementAlign(m_robotDrive, targetPose);
    }

    public static Command PIDAutonomousMoveToPoseWithTimeout(Pose2d targetPose, double timeoutSeconds){
      return new autonomousMovementAlignWithTimeOut(m_robotDrive, targetPose, timeoutSeconds);
  }

    public Command ReturnAutoCommand(Pose2d targetPose){
      return Commands.sequence(
          spinMove(),
          PIDPathfindToPose(targetPose).withTimeout(5)
      );
}
    public Command aSideL2AutoCommand(Pose2d initialLinearPose, Pose2d beforeAlign, Pose2d targetPose, Pose2d humanStationPose){
      return Commands.sequence(
        spinMove(),
        PIDAutonomousMoveToPose(initialLinearPose).withTimeout(3.25),
        PIDAutonomousMoveToPose(beforeAlign).withTimeout(2.2),
        PIDPathfindToPose(targetPose).withTimeout(1.4),
        Commands.parallel(
          setElevatorL2Auto().withTimeout(2).andThen(setElevatorStowAuto()),
          Commands.sequence(
            new WaitCommand(.9),
            new InstantCommand(() -> {
              m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
            }, m_Effector),
            new WaitCommand(1),
            new InstantCommand(() -> {
              m_Effector.setIntakeLazyPercentageOpenLoop(0);
            }, m_Effector)
            )
          ),
        PIDPathfindToPose(humanStationPose)
        
        );
    }

    public Command ReturnL3AutoCommand(Pose2d targetPose){
      return Commands.sequence(
          spinMove(),
          PIDPathfindToPose(targetPose).withTimeout(3),
      Commands.parallel(
              setElevatorL3Auto().withTimeout(3.3).andThen(setElevatorStowAuto()),
          Commands.sequence(
              new WaitCommand(1.0), 
              new InstantCommand(() -> {
                m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
              }, m_Effector),
              new WaitCommand(1.0),
              new InstantCommand(() -> {
                m_Effector.setIntakeLazyPercentageOpenLoop(0);
              }, m_Effector) 
          )
      )

    );
    }

    public Command ReturnL2AutoCommand(Pose2d targetPose){
      return Commands.sequence(
          spinMove(),
          PIDPathfindToPose(targetPose).withTimeout(3.5),
      Commands.parallel(
              setElevatorL2Auto().withTimeout(3.3).andThen(setElevatorStowAuto()),
          Commands.sequence(
              new WaitCommand(1),
              new InstantCommand(() -> {
                m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
              }, m_Effector),
              new WaitCommand(1.0),
              new InstantCommand(() -> {
                m_Effector.setIntakeLazyPercentageOpenLoop(0);
              }, m_Effector)  
          )
      )

    );
    }

    public Command ReturnL2HumanCommand(Pose2d targetPose, Pose2d outReef, Pose2d humanPlayer, Pose2d secondOutReef, Pose2d secondTarget, double firstTimeout, double secondTimeout, double thirdTimeout, double quadTimeout){
      return Commands.sequence(
          spinMove(),
          pIDPathfindToPoseWithTimeout(targetPose, firstTimeout).withTimeout(3.5),
      Commands.parallel(
              setElevatorL2Auto().withTimeout(1.8).andThen(setElevatorStowAuto()),
          Commands.sequence(
              new WaitCommand(1), 
              new InstantCommand(() -> {
                m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
              }, m_Effector),
              new WaitCommand(0.5),
              new InstantCommand(() -> {
                m_Effector.setIntakeLazyPercentageOpenLoop(0);
              }, m_Effector)  
          )
      ).withTimeout(2.7),
      PIDAutonomousMoveToPoseWithTimeout(outReef, secondTimeout).withTimeout(0.1),
      pIDPathfindToPoseWithTimeout(humanPlayer, thirdTimeout).withTimeout(3),
      new WaitCommand(1.25),
      Commands.parallel(
        PIDPathfindToPose(secondTarget),
        Commands.sequence(
          new InstantCommand(() -> {
            m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
          }, m_Effector),
          new WaitCommand(.5),
          new InstantCommand(() -> {
            m_Effector.setIntakeLazyPercentageOpenLoop(0);
          }, m_Effector)
        )
        ),
        Commands.parallel(
          setElevatorL2Auto().withTimeout(3.3).andThen(setElevatorStowAuto()),
          Commands.sequence(
              new WaitCommand(1), //tune
              new InstantCommand(() -> {
                m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
              }, m_Effector),
              new WaitCommand(1.0),
              new InstantCommand(() -> {
                m_Effector.setIntakeLazyPercentageOpenLoop(0);
              }, m_Effector) 
      )
      ).withTimeout(5)
    );
    }

    public Command ReturnL2SimpleHumanCommand(Pose2d targetPose, Pose2d outReef, Pose2d humanPlayer, Pose2d secondOutReef, Pose2d secondTarget, double firstTimeout, double secondTimeout, double thirdTimeout, double quadTimeout){
      return Commands.sequence(
          spinMove(),
          pIDPathfindToPoseWithTimeout(targetPose, firstTimeout).withTimeout(3.5),
      Commands.parallel(
              setElevatorL2Auto().withTimeout(1.8).andThen(setElevatorStowAuto()),
          Commands.sequence(
              new WaitCommand(1), 
              new InstantCommand(() -> {
                m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
              }, m_Effector),
              new WaitCommand(0.5),
              new InstantCommand(() -> {
                m_Effector.setIntakeLazyPercentageOpenLoop(0);
              }, m_Effector)
          )
      ).withTimeout(2.7),
      PIDAutonomousMoveToPoseWithTimeout(outReef, secondTimeout).withTimeout(0.1),
      pIDPathfindToPoseWithTimeout(humanPlayer, thirdTimeout).withTimeout(3),
      new WaitCommand(2.5),
      Commands.parallel(
        PIDPathfindToPose(secondTarget),
        Commands.sequence(
          new InstantCommand(() -> {
            m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
          }, m_Effector),
          new WaitCommand(.5),
          new InstantCommand(() -> {
            m_Effector.setIntakeLazyPercentageOpenLoop(0);
          }, m_Effector)
        )
        )
      );
    }
    
    public Command returnChosenSpinMove(){
      return DriverStation.getAlliance().get() == Alliance.Red ?  inverseSpinMove() : spinMove();
    }

    public Command returnBlueLeftAACommand(Pose2d offReef, double offReefTimeout, Pose2d firstScorePose2d, 
      double firstScoreTimeout, Pose2d humanPlayerPose2d, double humanPlayerTimeout, double stationPeriod,
      Pose2d secondScorePose2d, double secondScoreTimeout, double elevtorTimeout){
      return Commands.sequence(
        returnChosenSpinMove(),
        PIDAutonomousMoveToPoseWithTimeout(offReef, offReefTimeout).withTimeout(offReefTimeout),
        Commands.parallel(
          pIDPathfindToPoseWithTimeout(firstScorePose2d, firstScoreTimeout).withTimeout(firstScoreTimeout),
          Commands.parallel(
            setElevatorL2Auto().withTimeout(elevtorTimeout).andThen(setElevatorStowAuto()),
          Commands.sequence(
            new WaitCommand(2.2),
            new InstantCommand(() -> {
              m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
            }, m_Effector),
            new WaitCommand(.5),
            new InstantCommand(() -> {
              m_Effector.setIntakeLazyPercentageOpenLoop(0);
            }, m_Effector)
          ).withTimeout(elevtorTimeout+.5)
          ).withTimeout(elevtorTimeout+.9)
        ),
        pIDPathfindToPoseWithTimeout(humanPlayerPose2d, humanPlayerTimeout).withTimeout(humanPlayerTimeout),
        new WaitCommand(stationPeriod),
        Commands.parallel(
        pIDPathfindToPoseWithTimeout(secondScorePose2d, secondScoreTimeout).withTimeout(secondScoreTimeout),
        Commands.sequence(
          new InstantCommand(() -> {
            m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
          }, m_Effector),
          new WaitCommand(.5),
          new InstantCommand(() -> {
            m_Effector.setIntakeLazyPercentageOpenLoop(0);
          }, m_Effector)
        ).withTimeout(0.6)
        ).withTimeout(3.3),
        Commands.parallel(
          setElevatorL2Auto().withTimeout(elevtorTimeout).andThen(setElevatorStowAuto()),
          Commands.sequence(
            new WaitCommand(1.5),
            new InstantCommand(() -> {
              m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
            }, m_Effector),
            new WaitCommand(.5),
            new InstantCommand(() -> {
              m_Effector.setIntakeLazyPercentageOpenLoop(0);
            }, m_Effector)
          ).withTimeout(elevtorTimeout+ .4 ) //0.4 seconds to allow stow
        )

        );
    }

  /**
   * Returns the current alliance, with false indicating blue and true indicating red.
   * If there is no alliance, blue alliance is assumed. ie: not in match
   *
   * @return The current alliance of the robot.
   */
  public boolean getAlliance() {
     var alliance = DriverStation.getAlliance();
    if (alliance.isPresent()) {
      return alliance.get() == DriverStation.Alliance.Red;
    }
    return false;
  }

  //Returns isRed or isBlue from FMS @ Start of the Match
  public static Optional<Alliance> isRedAlliance(){
    return DriverStation.getAlliance();
  }

}


/* FRC 253 2025 Code by:
Kaden Chow - Programming Lead - MHS 2026 - https://github.com/Kachow2323
Joshua Seo - Programming Lead - MHS 2027 - https://github.com/Shaguins
Finn Nolan - Asst. Programming Lead - MHS 2027 - https://github.com/Finn-253
Theo Nolan - Asst. Programming Lead - MHS 2027 - https://github.com/Theo-253

With Invaluable Help from:
  Mentor John :D
  Mentor Lauren :D
  Mentor Katie :D
  Everyone else from inside and outside of 253
*/
