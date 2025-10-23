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
//import frc.robot.subsystems.TestSubsystem;
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
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
// import frc.robot.subsystems.ClimbSubsystem;
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
  // public final ClimbSubsystem m_Climb;
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
    //m_TestSubsystem =TestSubsystem.getInstance();
    m_robotDrive = DriveSubsystem.getInstance();
    m_AutoModeManager = new AutoModeManager();
    m_ControlHub = ControlHub.getInstance();
    //VisionSubsystem.getInstance(m_robotDrive);
    m_Elevator = ElevatorSubsystem.getInstance();
    m_Effector = EndEffectorSubsystem.getInstance();
    // m_Climb = ClimbSubsystem.getInstance();
    // Configure default commands
    SmartDashboard.putData("Auto Mode", AutoModeManager.mModeChooser);
    SmartDashboard.putBoolean("AutoAlign Status", false);
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
            // new InstantCommand(() -> {
            //   m_Effector.setIntakeLazyPercentageOpenLoop(0);
            //   }, m_Effector)

            // new ParallelCommandGroup(
            //   new SequentialCommandGroup(
            //     new WaitCommand(1),
            //     new InstantCommand(() -> {
            //       m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
            //     }, m_Effector)
            //   )
              // new InstantCommand(() -> {
              //   m_Effector.setIntakeLazyPercentageOpenLoop(.8);
              // }, m_Effector)
            
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
            return m_robotDrive.spinMoveCommand(1.5);
        // Command.sequence(
        //     new RunCommand(() -> {
        //         mDriveSubsystem.spinMoveCommand(2);
        //     }, mDriveSubsystem)
        // );
    }

    public static Command PIDPathfindToPose(Pose2d targetPose){
        return new autoAlign(m_robotDrive, targetPose);
    }

    public Command ReturnAutoCommand(Pose2d targetPose){
      return Commands.sequence(
          spinMove(),
          PIDPathfindToPose(targetPose).withTimeout(5),
      Commands.parallel(
              setElevatorL1Auto().withTimeout(3.3).andThen(setElevatorStowAuto()),
          Commands.sequence(
              new WaitCommand(0.8), //tune
              doubleRollerCommand().withTimeout(2.5) //tune
                  .andThen(rollerDefaultStop()) 
          )
      )

  );
}

    public Command ReturnL3AutoCommand(Pose2d targetPose){
      return Commands.sequence(
          spinMove(),
          PIDPathfindToPose(targetPose).withTimeout(3),
      Commands.parallel(
              setElevatorL3Auto().withTimeout(3.3).andThen(setElevatorStowAuto()),
          Commands.sequence(
              new WaitCommand(1.0), //tune
              new InstantCommand(() -> {
                m_Effector.setIntakeLazyPercentageOpenLoop(1.0);
              }, m_Effector),
              new WaitCommand(1.0),
              new InstantCommand(() -> {
                m_Effector.setIntakeLazyPercentageOpenLoop(0);
              }, m_Effector) //tune 
          )
      )

    );
    }

    // public Command intakeCoralCommand(){
    //   return new SequentialCommandGroup(
    //         new InstantCommand(() -> {
    //           m_Effector.setLazyEndEffectorState(States.EndEffectorPos.INTAKE);},
    //               m_Effector)
    //           // new RunCommand(() -> {
    //           //   m_Effector.setLazyEndEffectorState(States.EndEffectorPos.STOW.intake);},
    //           //     m_Effector),
             
    //         );
    //     }

        // public Command hardExtakeCoralCommand(){
        //   return new SequentialCommandGroup(
        //         new InstantCommand(() -> 
        //           m_Effector.setLazyEndEffectorState(States.EndEffectorPos.HARD_REMOVE))
        //           // new RunCommand(() -> {
        //           //   m_Effector.setLazyEndEffectorState(States.EndEffectorPos.STOW.intake);},
        //           //     m_Effector),
                 
        //         );
        //     }

          // public Command softExtakeCoralCommand(){
          // return new SequentialCommandGroup(
          //       new InstantCommand(() -> 
          //         m_Effector.setLazyEndEffectorState(States.EndEffectorPos.SOFT_REMOVE))
          //         // new RunCommand(() -> {
          //         //   m_Effector.setLazyEndEffectorState(States.EndEffectorPos.STOW.intake);},
          //         //     m_Effector),
                 
          //       );
          //   }

          // public Command smartIntakeCoralCommand(){
          // return new SequentialCommandGroup(
          //       new InstantCommand(() -> 
          //         m_Effector.setLazyEndEffectorState(States.EndEffectorPos.SMART_INTAKE))
          //         // new RunCommand(() -> {
          //         //   m_Effector.setLazyEndEffectorState(States.EndEffectorPos.STOW.intake);},
          //         //     m_Effector),
                 
          //       );
          //   }
            // public Command algaeExtendCommand(){
            //   return new SequentialCommandGroup(
            //         new InstantCommand(() -> 
            //           m_Effector.setLazyEndEffectorState(States.EndEffectorPos.EXTENDED_PIVOT))
            //           // new RunCommand(() -> {
            //           //   m_Effector.setLazyEndEffectorState(States.EndEffectorPos.STOW.intake);},
            //           //     m_Effector),
                     
            //         );
            // }

            // public Command stowAlgaeCommand(){
            //   return new SequentialCommandGroup(
            //         new InstantCommand(() -> 
            //           m_Effector.setLazyEndEffectorState(States.EndEffectorPos.STOW))
            //           // new RunCommand(() -> {
            //           //   m_Effector.setLazyEndEffectorState(States.EndEffectorPos.STOW.intake);},
            //           //     m_Effector),
                     
            //         );
            // }



            // public Command deployAlgaeRollers(){
            //   return new SequentialCommandGroup(
            //     new RunCommand(() -> {
            //       m_Effector.setLazyEndEffectorState(States.EndEffectorPos.DEPLOY);
            //     }, m_Effector),
            //     new WaitCommand(.2),
            //     new RunCommand(() -> {
            //             m_Effector.setLazyEndEffectorState(States.EndEffectorPos.L1Score);
            //           }, m_Effector)
            //     );
            //   }

  // public void permissibleForward(BooleanSupplier permission){
  //   new ConditionalCommand(RunElevatorPositive(), StopElevator(), permission);
  // }
  // public void permissibleBackward(BooleanSupplier permission){
  //   new ConditionalCommand(RunElevatorNegative(), StopElevator(), permission);
  // }

  // public void RunNegative(){
  //   new RunCommand(() -> m_TestSubsystem.setOpenLoop(-0.2), m_TestSubsystem);
  // }

  // public Command IntakePrep(){
  //   return new RunCommand(() -> m_TestSubsystem.setState(States.TestPos.POS1), m_TestSubsystem);
  // }

  // public Command IntakeStow(){
  //   return new RunCommand(() -> m_TestSubsystem.setState(States.TestPos.STOW), m_TestSubsystem);
  // }

  // public Command Score(){
  //   return new ParallelCommandGroup(
  //         // new RunCommand(() -> {
  //         //   arm.setArmState(States.ArmPos.SCORE);
  //         //   }, arm),
  //         // new SequentialCommandGroup(
  //         //   new WaitCommand(HookConstants.delay),
  //         //   new RunCommand(() -> {
  //         //     hook.setHookState(States.HookPos.SCORE);
  //         //   }, hook
  //         //   )
  //         // )
  //       );
  // }

  // public Command StowArm(){
  // return new RunCommand(() -> {
  //       arm.setArmState(States.ArmPos.STOW); 
  //       hook.setHookState(States.HookPos.STOW);
  //      }, arm, hook);
  // }

  /* READ ME:
  //    * This command runs the SCORE command for the AMP shot in AUTO
  //    * By condensing the entire score command into one method we no longer have to keep defining it everywhere and we set the standard for each attempt
  //    * Utilizes Constants.java for realtive and absoulte scoring encoder values.
  //    * Parrallel Command Group - The command runs at the same time but we put a time delay to calculate the exact timing
  //    * We needed the wait command bc we need the momentum from the swinign arm to score into the AMP
  //    */

  // public Command scoreHookDelay() {
  //   return new ParallelCommandGroup(
  //         new RunCommand(() -> {
  //           arm.setArmState(States.ArmPos.SCORE);
  //           }, arm),
  //         new SequentialCommandGroup(
  //           new WaitCommand(HookConstants.delay),
  //           new RunCommand(() -> {
  //             hook.setHookState(States.HookPos.SCORE);
  //           }, hook
  //           )
  //         )
  //       );
  // }

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

  // public static Command ampAutoDrive() {
  //   return new DriveToPose(FieldSetup.allianceAmpEntryPoseSupplier, FieldSetup.ampEntryTolerance);
  // }

  
  // double redAMP_x = 14.7;
  //       double redAMP_Y = 7.8;
  //       Translation2d redAMPTranslation2d = new Translation2d(redAMP_x,redAMP_Y);
  //       Pose2d redAMPPose2d = new Pose2d((redAMPTranslation2d), Rotation2d.fromDegrees(90));
  
  // public static Command PathFindReef21(boolean permission){
  //   return new PathfindToPose(FieldSetup.allianceReefFarSupplier, FieldSetup.kReefFarEntranceTolerance, permission);
  // }

  // public static Command PathfindClosest(boolean permission, Boolean isRight) {
  //   return new PathfindClosest(permission,isRight);
  // }

}


/* 2023-2024 For-TEA-Simo Java Code by:

Kaden J. Chow - Programming Lead - MHS 2026 - https://github.com/Kachow2323
Ronit Barman - Tech Captain - MHS 2024
Joshua Seo - Programming - MHS 2027
Adam Situ - Asst. Programming Lead - MHS 2027
Michelle Y - Asst. Programming Lead - MHS 2025

With Invaluable Help from:
  Mentor John :D
  Mentor Lauren :D
  Mentor Katie :D
  Everyone else from inside and outside of 253
*/
