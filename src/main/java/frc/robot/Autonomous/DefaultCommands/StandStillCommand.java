package frc.robot.Autonomous.DefaultCommands;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;

public final class StandStillCommand{

    public static Command getPathPlannerCommand() {
        return new PathPlannerAuto("T_StandStill");
    }

    public static Command runDefaultedAutoCommand(){
        return new SequentialCommandGroup(
        StandStillCommand.getPathPlannerCommand()
        );
    }

    public static void scheduleDefaultCommand(){
        StandStillCommand.getPathPlannerCommand().schedule();
    }
}