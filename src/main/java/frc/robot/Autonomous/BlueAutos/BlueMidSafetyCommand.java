package frc.robot.Autonomous.BlueAutos;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;

public final class BlueMidSafetyCommand{

    public static Command getPathPlannerCommand_MidLeave() {
        return new PathPlannerAuto("AB_MidLeave");
    }
    public static Command getPathPlannerCommand_MidScore1() {
        return new PathPlannerAuto("AB_MidScore1");
    }

    public static Command runDefaultedAutoCommand(){
        return new SequentialCommandGroup(
        getPathPlannerCommand_MidLeave()
        );
    }
    public static Command runScoreOneAutoCommand(){
        return new SequentialCommandGroup(
        getPathPlannerCommand_MidScore1()
        );
    }
}