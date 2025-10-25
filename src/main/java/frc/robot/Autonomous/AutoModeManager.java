package frc.robot.Autonomous;
import java.util.Optional;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Autonomous.BlueAutos.CrossLineCommand;
import frc.robot.Autonomous.BlueAutos.MidSingleScore;
//import frc.robot.subsystems.TestSubsystem;
import frc.robot.Autonomous.DefaultCommands.DoNothingCommand;
import frc.robot.Autonomous.DefaultCommands.GoAutoCommand;
import frc.robot.Autonomous.DefaultCommands.ReturnAutoCommand;
import frc.robot.Autonomous.DefaultCommands.StandStillCommand;

public class AutoModeManager{
    public enum DesiredMode {
		DO_NOTHING,
        GO_AUTO,
        RETURN_AUTO,
        STAND_STILL,
        CROSS_LINE,
        L1_MIDDLE_START,
        MIDSINGLESCORE,
        L3,
        C_SIDE_LEFT_L2,
        C_SIDE_RIGHT_L2,
        E_SIDE_LEFT_L2,
        E_SIDE_RIGHT_L2,
        C_SIDE_L1,
        E_SIDE_L1,
        BLUE_LEFT_A2,
        BLUE_RIGHT_A2,
        RED_LEFT_A2,
        RED_RIGHT_A2

	}

    public static AutoModeManager AutoQueue;
    public static AutoModeManager getInstance() {
        if(AutoQueue == null) AutoQueue = new AutoModeManager();
        return AutoQueue;
    }

    private DesiredMode defaultMode = DesiredMode.DO_NOTHING;
    //private Optional<AutoModeBase> mAutoMode = Optional.empty();
    public static SendableChooser<DesiredMode> mModeChooser = new SendableChooser<>();
    public static Command m_autonomousCommand;
    public static DesiredMode desiredMode;

    public AutoModeManager() {
    mModeChooser.setDefaultOption("Default Auto", DesiredMode.DO_NOTHING);
    // mModeChooser.addOption("Return Auto", DesiredMode.RETURN_AUTO);
    // mModeChooser.addOption("Go Auto", DesiredMode.GO_AUTO);
    // mModeChooser.addOption("Bottom Feeder", DesiredMode.AB_BOTAUTO);
    // mModeChooser.addOption("Safety Auto", DesiredMode.AB_MIDLEAVE);
    // mModeChooser.addOption("Mid 1 Coral", DesiredMode.AB_MIDSCOREONE);
    // mModeChooser.addOption("Stand Still Boi", DesiredMode.STAND_STILL);
    mModeChooser.addOption("Simple Cross Line", DesiredMode.CROSS_LINE);
    mModeChooser.addOption("L1 from middle ", DesiredMode.L1_MIDDLE_START);
    mModeChooser.addOption("Blue Mid L1-1P", DesiredMode.MIDSINGLESCORE);
    mModeChooser.addOption("C Side Left Branch l2", DesiredMode.C_SIDE_LEFT_L2);
    mModeChooser.addOption("C Side Right Branch l2", DesiredMode.C_SIDE_RIGHT_L2);
    mModeChooser.addOption("E Side Left Branch l2", DesiredMode.E_SIDE_LEFT_L2);
    mModeChooser.addOption("E Side Right Branch l2", DesiredMode.E_SIDE_RIGHT_L2);
    mModeChooser.addOption("Blue Left A2", DesiredMode.BLUE_LEFT_A2);
    mModeChooser.addOption("Blue Right A2", DesiredMode.BLUE_LEFT_A2);
    mModeChooser.addOption("Red Left A2", DesiredMode.RED_LEFT_A2);
    mModeChooser.addOption("Red RIght A2", DesiredMode.RED_LEFT_A2);

    }

    public static void updateAutoMode(){
        desiredMode = mModeChooser.getSelected();
        if (desiredMode == null) {
			    desiredMode = DesiredMode.DO_NOTHING;
        }else{
        System.out.println("Auto Chosen");
        }   
        m_autonomousCommand = grabAutoMode(desiredMode);
    }

    public static Command grabAutoMode(DesiredMode data){
        switch(data){
            case DO_NOTHING:
				m_autonomousCommand = DoNothingCommand.NoAuto();
                break;
            case GO_AUTO:
                m_autonomousCommand = GoAutoCommand.runDefaultedAutoCommand();
                break;
            case RETURN_AUTO:
                m_autonomousCommand = ReturnAutoCommand.runDefaultedAutoCommand();
                break;
            case CROSS_LINE:
                m_autonomousCommand = CrossLineCommand.runDefaultedAutoCommand(); 
                break;
            case MIDSINGLESCORE:
                m_autonomousCommand = null;
                break;
            case L1_MIDDLE_START:
                m_autonomousCommand = null;
                break;
            case C_SIDE_LEFT_L2:
                m_autonomousCommand = null;
                break;
            case C_SIDE_RIGHT_L2:
                m_autonomousCommand = null;
                break;
            case E_SIDE_LEFT_L2:
                m_autonomousCommand = null;
                break;
            case E_SIDE_RIGHT_L2:
                m_autonomousCommand = null;
                break;
            case BLUE_LEFT_A2:
                m_autonomousCommand = null;
                break;
            case BLUE_RIGHT_A2:
                m_autonomousCommand = null;
                break;
            case RED_LEFT_A2:
                m_autonomousCommand = null;
                break;
            case RED_RIGHT_A2:
                m_autonomousCommand = null;
                break;
            default:
			    System.out.println("ERROR: unexpected auto mode!");
				break;
        }
        return m_autonomousCommand;
    }

    
}