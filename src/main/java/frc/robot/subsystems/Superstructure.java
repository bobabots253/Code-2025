package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants;

public class Superstructure extends SubsystemBase {
    private final DriveSubsystem m_driveSubsystem;
    private final ElevatorSubsystem m;

    private final CommandXboxController driverController = new CommandXboxController(0);
    private final CommandXboxController operatorController = new CommandXboxController(0);

    private Constants.SuperstructureConstants.AutomationLevel automationLevel =
            Constants.SuperstructureConstants.AutomationLevel.AUTO_DRIVE_AND_MANUAL_RELEASE;

    private WantedSuperState wantedSuperState = WantedSuperState.STOPPED;
    private CurrentSuperState currentSuperState = CurrentSuperState.STOPPED;
    private CurrentSuperState previousSuperState;

    public enum WantedSuperState {
        HOME,
        STOPPED,
        DEFAULT_STATE,
        INTAKE_CORAL_FROM_STATION,
        SCORE_L1_MANUAL_ALIGN,
        SCORE_L1_LEFT_BASE,
        SCORE_L1_RIGHT_BASE,
        SCORE_L1_LEFT_TOP,
        SCORE_L1_RIGHT_TOP,
        SCORE_LEFT_L2,
        SCORE_LEFT_L3,
        SCORE_RIGHT_L2,
        SCORE_RIGHT_L3,
        MANUAL_L3,
        MANUAL_L2,
        MANUAL_L1,
    }

    public enum CurrentSuperState {
        HOME,
        STOPPED,
        NO_PIECE_TELEOP,
        HOLDING_CORAL_TELEOP,
        NO_PIECE_AUTO,
        HOLDING_CORAL_AUTO,
        INTAKE_CORAL_FROM_STATION,
        SCORE_TELEOP_L1_MANUAL_ALIGNMENT,
        SCORE_L1_LEFT_BASE,
        SCORE_L1_RIGHT_BASE,
        SCORE_L1_LEFT_TOP,
        SCORE_L1_RIGHT_TOP,
        SCORE_LEFT_TELEOP_L2,
        SCORE_LEFT_TELEOP_L3,
        SCORE_RIGHT_TELEOP_L2,
        SCORE_RIGHT_TELEOP_L3,
        SCORE_AUTO_L1,
        SCORE_LEFT_AUTO_L2,
        SCORE_LEFT_AUTO_L3,
        SCORE_LEFT_AUTO_L4,
        SCORE_RIGHT_AUTO_L2,
        SCORE_RIGHT_AUTO_L3,
        MANUAL_L3,
        MANUAL_L2,
        MANUAL_L1,
    }

    public Superstructure(
        DriveSubsystem m_DriveSubsystem,
        ElevatorSubsystem m_TestSubsystem){
            this.m_driveSubsystem = m_DriveSubsystem;
            this.m_elevatorSubsystem = m_TestSubsystem;
    }


    private CurrentSuperState handStateTransitions() {
        previousSuperState = currentSuperState;
        switch (wantedSuperState) {
            default:
                currentSuperState = CurrentSuperState.STOPPED;
                break;
        case INTAKE_CORAL_FROM_STATION:
                currentSuperState = CurrentSuperState.INTAKE_CORAL_FROM_STATION;
                break;
        case DEFAULT_STATE:
                if (intakeSubsystem.hasStraightCoral()) {
                    if (DriverStation.isAutonomous()) {
                        currentSuperState = CurrentSuperState.HOLDING_CORAL_AUTO;
                    } else {
                        currentSuperState = CurrentSuperState.HOLDING_CORAL_TELEOP;
                    }
                } else {
                    if (DriverStation.isAutonomous()) {
                        currentSuperState = CurrentSuperState.NO_PIECE_AUTO;
                    } else {
                        currentSuperState = CurrentSuperState.NO_PIECE_TELEOP;
                    }
                } 
    }
}    
    


