package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants;
import frc.robot.subsystems.elevator.ElevatorSubsystem;

public class Superstructure extends SubsystemBase {
    private final DriveSubsystem m_driveSubsystem;
    private final ElevatorSubsystem m_ElevatorSubsystem;

    private final CommandXboxController driverController = new CommandXboxController(0);
    private final CommandXboxController operatorController = new CommandXboxController(0);

    private Constants.SuperstructureConstants.AutomationLevel automationLevel =
            Constants.SuperstructureConstants.AutomationLevel.AUTO_DRIVE_AND_MANUAL_RELEASE;
    private Constants.SuperstructureConstants.ReefSelectionMethod reefSelectionMethod =
            Constants.SuperstructureConstants.ReefSelectionMethod.POSE;
    
    public enum WantedSuperState {
        HOME,
        STOPPED,
        DEFAULT_STATE,
        INTAKE_CORAL_FROM_STATION,
        SCORE_L1_MANUAL_ALIGN,
        SCORE_L1_MID_BASE,
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
        SCORE_LEFT_TELEOP_L2,
        SCORE_LEFT_TELEOP_L3,
        SCORE_RIGHT_TELEOP_L2,
        SCORE_RIGHT_TELEOP_L3,
        SCORE_AUTO_L1,
        SCORE_LEFT_AUTO_L2,
        SCORE_LEFT_AUTO_L3,
        SCORE_RIGHT_AUTO_L2,
        SCORE_RIGHT_AUTO_L3,
        MANUAL_L3,
        MANUAL_L2,
        MANUAL_L1,
    }

    public Superstructure(
        DriveSubsystem m_DriveSubsystem,
        ElevatorSubsystem m_ElevatorSubsystem){
            this.m_driveSubsystem = m_DriveSubsystem;
            this.m_ElevatorSubsystem = m_ElevatorSubsystem;
    }

    private WantedSuperState wantedSuperState = WantedSuperState.STOPPED;
    private CurrentSuperState currentSuperState = CurrentSuperState.STOPPED;
    private CurrentSuperState previousSuperState;


    @Override
    public void periodic(){
        currentSuperState = handStateTransitions();
        applyStates();
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
                break;
        case SCORE_L1_MANUAL_ALIGN:
                currentSuperState = DriverStation.isAutonomous()
                        ? CurrentSuperState.SCORE_AUTO_L1
                        : CurrentSuperState.SCORE_TELEOP_L1_MANUAL_ALIGNMENT;
                break;
        case SCORE_LEFT_L2:
                currentSuperState = DriverStation.isAutonomous()
                    ? CurrentSuperState.SCORE_LEFT_AUTO_L2
                    : CurrentSuperState.SCORE_LEFT_TELEOP_L2;
                break;
        case SCORE_LEFT_L3:    
                currentSuperState = DriverStation.isAutonomous()
                        ? CurrentSuperState.SCORE_LEFT_AUTO_L3
                        : CurrentSuperState.SCORE_LEFT_TELEOP_L3;
                break;
        case SCORE_RIGHT_L2:
                currentSuperState = DriverStation.isAutonomous()
                    ? CurrentSuperState.SCORE_RIGHT_AUTO_L2
                    : CurrentSuperState.SCORE_RIGHT_TELEOP_L2;
            break;
        case SCORE_RIGHT_L3:
                currentSuperState = DriverStation.isAutonomous()
                    ? CurrentSuperState.SCORE_RIGHT_AUTO_L3
                    : CurrentSuperState.SCORE_RIGHT_TELEOP_L3;
            break;
        case MANUAL_L1:
            currentSuperState = CurrentSuperState.MANUAL_L1;
            break;
        case MANUAL_L2:
            currentSuperState = CurrentSuperState.MANUAL_L2;
            break;
        case MANUAL_L3:
            currentSuperState = CurrentSuperState.MANUAL_L3;
            break;
        }
        return currentSuperState;
    }

    private void applyStates() {
        //force interop
        switch (currentSuperState) {
            case HOME:
                home();
                break;
            case INTAKE_CORAL_FROM_STATION:
                intakeCoralFromStation();
                break;
            case NO_PIECE_TELEOP:
                noPiece();
                break;
            case NO_PIECE_AUTO:
                noPieceAuto();
                break;
            case HOLDING_CORAL_AUTO:
                holdingCoralAuto();
                break;
            case HOLDING_CORAL_TELEOP:
                holdingCoral();
                break;
            case SCORE_TELEOP_L1_MANUAL_ALIGNMENT:
                scoreL1Teleop();
                break;
            case SCORE_LEFT_TELEOP_L2:
                scoreL2Teleop(Constants.SuperstructureConstants.ScoringSide.LEFT);
                break;
            case SCORE_LEFT_TELEOP_L3:
                scoreL3Teleop(Constants.SuperstructureConstants.ScoringSide.LEFT);
                break;
            case SCORE_RIGHT_TELEOP_L2:
                scoreL2Teleop(Constants.SuperstructureConstants.ScoringSide.RIGHT);
                break;
            case SCORE_RIGHT_TELEOP_L3:
                scoreL3Teleop(Constants.SuperstructureConstants.ScoringSide.RIGHT);
                break;
            case SCORE_AUTO_L1:
                scoreL1Auto();
                break;
            case SCORE_LEFT_AUTO_L2:
                scoreL2Auto(Constants.SuperstructureConstants.ScoringSide.LEFT);
                break;
            case SCORE_LEFT_AUTO_L3:
                scoreL3Auto(Constants.SuperstructureConstants.ScoringSide.LEFT);
                break;
            case SCORE_RIGHT_AUTO_L2:
                scoreL2Auto(Constants.SuperstructureConstants.ScoringSide.RIGHT);
                break;
            case SCORE_RIGHT_AUTO_L3:
                scoreL3Auto(Constants.SuperstructureConstants.ScoringSide.RIGHT);
                break;
            case MANUAL_L3:
                ejectL3();
                break;
            case MANUAL_L2:
                ejectL2();
                break;
            case MANUAL_L1:
                ejectL1();
                break;
            case STOPPED:
                stopped();
                break;
        }
    }

    private void home() {

    }
}    
    


