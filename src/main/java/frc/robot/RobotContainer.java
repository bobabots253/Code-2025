// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

// import frc.robot.subsystems.ClimbSubsystem;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.subsystems.Superstructure;
import frc.robot.subsystems.endeffector.EndEffectorSubsystem;
import frc.robot.subsystems.swerve.SwerveSubsystem;


/*
 * This class is where the bulk of the robot (including the subsystems) should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystem Declaration
  private final SwerveSubsystem swerveSubsystem;
  private final EndEffectorSubsystem endEffectorSubsystem;

  /*
   * The Superstructure is the central coordinator of all commands. Commands from the operator 
   *  will typically invoke the Superstructure's logic, which then orchestrates the other subsystem(s).
   */
  private final Superstructure superstructure;

  // Add the controllers connected to the driver station. 
  //  Remember that numbers start at [0]
  private final CommandXboxController driverController = new CommandXboxController(0);
  private final CommandXboxController operatorController = new CommandXboxController(1);

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    swerveSubsystem = new SwerveSubsystem();
    endEffectorSubsystem = new EndEffectorSubsystem();

    // Initialize the Superstructure by passing in the subsystems it needs to coordinate.
    superstructure = new Superstructure(
      swerveSubsystem,
      endEffectorSubsystem
      );

    // Configure all the commands to button bindings.
    configureBindings();
  }

  private void configureBindings() {

  /*
   * This command runs continuously on the SwerveSubsystem when no other command is scheduled. 
   * Why? Ensures the robot is always ready for manual driver input (TELEOP_DRIVE state).
   *  Note: For this exercise, we only have one drivesubsystem state atm
   */
  swerveSubsystem.setDefaultCommand(new InstantCommand(
      () -> swerveSubsystem.setWantedState(SwerveSubsystem.WantedState.TELEOP_DRIVE),
            swerveSubsystem));
  
  operatorController
                .rightBumper()
                //When the button is PRESSED, request the MANUAL_L1 state (shoot out game piece).
                .onTrue(superstructure.setStateCommand(Superstructure.WantedSuperState.MANUAL_L1))
                // When the button is RELEASED, request the INTAKE state, which is idling the rollers.
                .onFalse(superstructure.setStateCommand(Superstructure.WantedSuperState.INTAKE_CORAL_FROM_STATION));
  }

  // These methods allow access to other subsystems which are primarily used when creating Autonomous commands.
    //Not implemented at the moment.
  public SwerveSubsystem getDriveSubsystem(){
    return swerveSubsystem;
  }

  public EndEffectorSubsystem getTestSubsystem(){
    return endEffectorSubsystem;
  }

  public Superstructure getSuperStructure(){
    return superstructure;
  }

  // Allow Taring via Instant Commands during Disabled
  private static InstantCommand instantCommand(Runnable runnable) {
    return new InstantCommand(runnable) {
        @Override
        public boolean runsWhenDisabled() {
            return true;
        }
    };
}

}