package frc.robot.Autonomous.BlueAutos;

import com.pathplanner.lib.commands.PathPlannerAuto;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.RobotContainer;
import frc.robot.commands.ElevateL3Command;

public final class BlueMidScoreOneCommand{

    public static Command getPathPlannerCommand_Score1() {
        return new PathPlannerAuto("AB_MidScore1");
    }

    public static Command runDefaultedAutoCommand(){
        return new SequentialCommandGroup(
            getPathPlannerCommand_Score1()
        );
    }

}