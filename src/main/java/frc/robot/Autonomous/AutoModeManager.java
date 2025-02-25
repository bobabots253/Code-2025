package frc.robot.Autonomous;
import java.util.Optional;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Autonomous.BlueAutos.BlueBottomCommand;
import frc.robot.Autonomous.BlueAutos.BlueMidSafetyCommand;
//import frc.robot.subsystems.TestSubsystem;
import frc.robot.Autonomous.DefaultCommands.DoNothingCommand;
import frc.robot.Autonomous.DefaultCommands.GoAutoCommand;
import frc.robot.Autonomous.DefaultCommands.ReturnAutoCommand;
import frc.robot.Autonomous.DefaultCommands.StandStillCommand;

public final class AutoModeManager{
    public enum DesiredMode {
		DO_NOTHING,
        GO_AUTO,
        RETURN_AUTO,
        AB_BOTAUTO,
        NEW_AUTO,
        AB_MIDLEAVE,
        AB_MIDSCOREONE,
        STAND_STILL
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

    public AutoModeManager() {
    mModeChooser.setDefaultOption("Default Auto", DesiredMode.DO_NOTHING);
    mModeChooser.addOption("Return Auto", DesiredMode.RETURN_AUTO);
    mModeChooser.addOption("Go Auto", DesiredMode.GO_AUTO);
    mModeChooser.addOption("Bottom Feeder", DesiredMode.AB_BOTAUTO);
    mModeChooser.addOption("Safety Auto", DesiredMode.AB_MIDLEAVE);
    mModeChooser.addOption("Mid 1 Coral", DesiredMode.AB_MIDSCOREONE);
    mModeChooser.addOption("Stand Still Boi", DesiredMode.STAND_STILL);
    }

    public static void updateAutoMode(){
        DesiredMode desiredMode = mModeChooser.getSelected();
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
            case AB_BOTAUTO:
                m_autonomousCommand = BlueBottomCommand.runFullAutoCommand();
                break;
            case AB_MIDLEAVE:
                m_autonomousCommand = BlueMidSafetyCommand.runDefaultedAutoCommand();
                break;
            case AB_MIDSCOREONE:
                m_autonomousCommand = BlueMidSafetyCommand.runScoreOneAutoCommand();
                break;
            case STAND_STILL:
                m_autonomousCommand = StandStillCommand.runDefaultedAutoCommand();   
            default:
			    System.out.println("ERROR: unexpected auto mode!");
				break;
        }
        return m_autonomousCommand;
    }

    
}