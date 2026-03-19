// PedroPathing for robot in the RED STARTING CORNER

package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;

@Autonomous(name = "Pedro Pathing Autonomous", group = "Autonomous")
@Configurable // Panels
public class PedroAutonomous extends OpMode {
  private TelemetryManager panelsTelemetry; // Panels Telemetry instance
  public Follower follower; // Pedro Pathing follower instance
  private int pathState; // Current autonomous path state (state machine)
  private Paths paths; // Paths defined in the Paths class

  @Override
  public void init() {
    panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

    follower = Constants.createFollower(hardwareMap);
    follower.setStartingPose(new Pose(72, 8, Math.toRadians(90)));

    paths = new Paths(follower); // Build paths

    panelsTelemetry.debug("Status", "Initialized");
    panelsTelemetry.update(telemetry);
  }

  @Override
  public void loop() {
    follower.update(); // Update Pedro Pathing
    pathState = autonomousPathUpdate(); // Update autonomous state machine

    // Log values to Panels and Driver Station
    panelsTelemetry.debug("Path State", pathState);
    panelsTelemetry.debug("X", follower.getPose().getX());
    panelsTelemetry.debug("Y", follower.getPose().getY());
    panelsTelemetry.debug("Heading", follower.getPose().getHeading());
    panelsTelemetry.update(telemetry);
  }

  public static class Paths {
    public PathChain Path1;
    public PathChain Path2;
    public PathChain Path3;
    public PathChain Path4;
    public PathChain Path5;
    public PathChain Path6;
    public PathChain Path7;
    public PathChain Path8;
    public PathChain Path9;
    public PathChain Path10;
    public PathChain Path11;
    public PathChain Path12;
    public PathChain Path13;

    public Paths(Follower follower) {
      Path1 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(123.631, 131.613),
            new Pose(73.474, 71.596)
            )
          )
          .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(180))
          .build();

      Path2 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(73.474, 71.596),
            new Pose(105.683, 34.892)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path3 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(105.683, 34.892),
            new Pose(133.436, 34.380)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path4 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(133.436, 34.380),
            new Pose(73.045, 71.607)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path5 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(73.045, 71.607),
            new Pose(122.735, 131.034)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path6 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(122.735, 131.034),
            new Pose(79.252, 62.569)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path7 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(79.252, 62.569),
            new Pose(130.112, 58.443)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path8 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(130.112, 58.443),
            new Pose(72.744, 72.240)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path9 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(72.744, 72.240),
            new Pose(123.964, 131.634)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path10 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(123.964, 131.634),
            new Pose(97.355, 82.969)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path11 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(97.355, 82.969),
            new Pose(131.422, 83.613)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path12 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(131.422, 83.613),
            new Pose(123.299, 131.470)
            )
          )
          .setTangentHeadingInterpolation()
          .build();

      Path13 = follower.pathBuilder()
          .addPath(
            new BezierLine(
              new Pose(123.299, 131.470),
            new Pose(129.337, 133.719)
            )
          )
          .setTangentHeadingInterpolation()
          .build();
    }
  }

  public int autonomousPathUpdate() {
    // Add your state machine Here
    // Access paths with paths.pathName
    // Refer to the Pedro Pathing Docs (Auto Example) for an example state machine
    return 0;
  }
}
