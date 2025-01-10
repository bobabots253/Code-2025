// package frc.robot.Autonomous;

// import frc.robot.subsystems.*;
// import frc.robot.*;

// import com.pathplanner.lib.commands.PathPlannerAuto;

// import edu.wpi.first.wpilibj.sysid.SysIdRoutineLog.State;
// import edu.wpi.first.wpilibj2.command.Command;
// import edu.wpi.first.wpilibj2.command.Commands;
// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
// import edu.wpi.first.wpilibj2.command.RunCommand;
// import frc.robot.Constants.AutoConstants;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import edu.wpi.first.wpilibj2.command.WaitCommand;
// import java.util.ArrayList;
// import java.util.List;

// import javax.swing.Action;


// import edu.wpi.first.wpilibj.Timer;

// /**
//  * An abstract class that is the basis of the robot's autonomous routines. This
//  * is implemented in auto modes (which are
//  * routines that do actions).
//  */
// public abstract class AutoModeBase {
// 	protected double m_update_rate = 1.0 / 50.0;
// 	protected boolean m_active = false;
// 	protected double startTime = 0.0;

// 	protected double currentTime() {
// 		return Timer.getFPGATimestamp() - startTime;
// 	}

// 	public void run() {
// 		m_active = true;
// 		try {
// 			routine();
// 		} catch (AutoModeEndedException e) {
// 			System.out.println("Auto mode done, ended early");
// 			return;
// 		}

// 		done();
// 		System.out.println("Auto mode done");
// 	}

// 	public void done() {}

// 	public void stop() {
// 		m_active = false;
// 	}

// 	public boolean isActive() {
// 		return m_active;
// 	}

// 	public boolean isActiveWithThrow() throws AutoModeEndedException {
// 		if (!isActive()) {
// 			throw new AutoModeEndedException();
// 		}

// 		return isActive();
// 	}

// 	public void runAction(Action action) throws AutoModeEndedException {
// 		isActiveWithThrow();
// 		action.start();

// 		while (isActiveWithThrow() && !action.isFinished()) {
// 			action.update();
// 			long waitTime = (long) (m_update_rate * 1000.0);

// 			try {
// 				Thread.sleep(waitTime);
// 			} catch (InterruptedException e) {
// 				e.printStackTrace();
// 			}
// 		}

// 		action.done();
// 	}

// 	protected Trajectory<TimedState<Pose2dWithMotion>> logTrajectory(
// 			Trajectory<TimedState<Pose2dWithMotion>> trajectory) {
// 		trajectories.add(trajectory);
// 		return trajectory;
// 	}

// 	public List<Trajectory<TimedState<Pose2dWithMotion>>> getPaths() {
// 		return trajectories;
// 	}
// }