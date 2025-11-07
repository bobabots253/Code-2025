package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants;
import frc.robot.subsystems.endeffector.EndEffectorSubsystem;
import frc.robot.subsystems.endeffector.EndEffectorSubsystem.WantedState;
import frc.robot.subsystems.swerve.SwerveSubsystem;

/**
 * The Superstructure acts as a central coordinator that manages the desired states of multiple child subsystems (Swerve, EndEffector, etc.)
 *  to execute complex robot actions (like "Intake").
 * By breaking  down the robot's actions into these Superstates and forcing the Superstructure to coordinate actions
 *  we can finitely control the options the robot can take.
 */
public class Superstructure extends SubsystemBase {
    // References to the subsystems this class coordinates
    private final SwerveSubsystem m_driveSubsystem;
    private final EndEffectorSubsystem m_endEffectorSubsystem;

    //This driver controller is defined here but is not being used in the current Superstructure logic, maybe when we 
    //  finish writing more complex logic.
    private final CommandXboxController driverController = new CommandXboxController(0);

    //Likewise, automation level constants are defined here, but the logic to use them is not implemented yet
    private Constants.SuperstructureConstants.AutomationLevel automationLevel =
            Constants.SuperstructureConstants.AutomationLevel.MANUAL;
    private Constants.SuperstructureConstants.ReefSelectionMethod reefSelectionMethod =
            Constants.SuperstructureConstants.ReefSelectionMethod.POSE;

    /**
     * Enumerate the desired Superstates requested by the operator/commands.
     * Commands set this state via the public setter method at the bottom of this file.
     */
    public enum WantedSuperState {
        STOPPED,
        INTAKE_CORAL_FROM_STATION,
        MANUAL_L1,
    }

    /**
     * "Private" enumeration of the internal SuperStates. Ie: CurrentSuperState mirrors WantedSuperState.
     */
    public enum CurrentSuperState {
        STOPPED,
        INTAKE_CORAL_FROM_STATION,
        MANUAL_L1,
    }

    /**
     * Constructor like every subsystem: takes the subsystems it needs to coordinate.
     */

    public Superstructure(
        SwerveSubsystem driveSubsystem,
        EndEffectorSubsystem endEffectorSubsystem){
            this.m_driveSubsystem = driveSubsystem;
            this.m_endEffectorSubsystem = endEffectorSubsystem;
    }

    // SuperState Variables
    private WantedSuperState wantedSuperState = WantedSuperState.STOPPED;
    private CurrentSuperState currentSuperState = CurrentSuperState.STOPPED;
    private CurrentSuperState previousSuperState = CurrentSuperState.STOPPED;


    /*
     * Actions to Take:
     *  1. Determine the new system state we want to run (handle transition logic)
     *  2. Execute the actions corresponding to the new system state
     */
    @Override
    public void periodic(){
        currentSuperState = handleStateTransitions();
        applyStates();
    }

    /**
     * Method used in determining which CurrentSuperState to transition to based on the WantedSuperState.
     * Note: Normally complex logic checks (ie: checking if the arm is safe before transitioning) would go here.
     *  But since we don't have any complex mechanisms, we can directly transition into the next state.
     */
    private CurrentSuperState handleStateTransitions() {
        previousSuperState = currentSuperState;
        switch (wantedSuperState) {
            default:
                // Default case ensures the robot always has a known state.
                currentSuperState = CurrentSuperState.STOPPED;
                break;
        case INTAKE_CORAL_FROM_STATION:
                currentSuperState = CurrentSuperState.INTAKE_CORAL_FROM_STATION;
                break;
        case MANUAL_L1:
            currentSuperState = CurrentSuperState.MANUAL_L1;
            break;
        }
        return currentSuperState;
    }

    /**
     * Executes the actual logic for the current state by setting the WantedStates of the child subsystems.
     */
    private void applyStates() {
        switch (currentSuperState) {
            case INTAKE_CORAL_FROM_STATION:
                intakeCoralFromStation();
                break;
            case MANUAL_L1:
                ejectL1();
                break;
            case STOPPED:
                stopped();
                break;
        }
    }

    private void intakeCoralFromStation() {   
        m_endEffectorSubsystem.setWantedState(WantedState.IDLE);
        m_driveSubsystem.setWantedState(SwerveSubsystem.WantedState.TELEOP_DRIVE);
    }

    private void ejectL1() {
        m_endEffectorSubsystem.setWantedState(WantedState.SHOOTING);
        m_driveSubsystem.setWantedState(SwerveSubsystem.WantedState.IDLE);
    }

    private void stopped(){
        m_endEffectorSubsystem.setWantedState(WantedState.IDLE);
        m_driveSubsystem.setWantedState(SwerveSubsystem.WantedState.IDLE);
    }

    /**
     * Public setter method allowing Commands to change the desired SuperState.
     */
    public void setWantedSuperState(WantedSuperState requestedState) {
        this.wantedSuperState = requestedState;
    }

    /**
     * A helper method to create a simple InstantCommand that calls the setter.
     * Why? Makes [configureBindings()] in RobotContainer more readable.
     */
    public Command setStateCommand(WantedSuperState superState) {
        return new InstantCommand(() -> setWantedSuperState(superState));
    }
}    
