package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.limelightvision.LimeLight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

public class AprilTagsLimeLightTest extends OpMode {
  private LimeLight3A limelight;
  private IMU imu;
  
  @Override
public void init() {
  limelight = hardwareMap.get(LimeLight3A.class, deviceName: "LimeLight");
  limelight.pipelineSwitch(index:8); //april tag #11 pipeline
  imu = hardwareMap.get(IMU.class, deviceName: "imu");
  RevHubOrientationOnRobot revHubOrientationOnRobot = new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.FORWARD);
  imu.initialize(new IMU.Parameters(revHubOrientationOnRobot));
}

  @Override
public void start() {
  limelight.start();

}

  @Override
public void loop() {
  YawPitchRollAngles orientation = imv.getRobotYawPitchRollAngles();
  limelight.updateRobotOrientation(orientation, getYaw());
  LLResult LLResult = limelight.getLatestResult();
  if (llResult != null && llResult.isValid()) {
    Pose3D botPose = llResult.getBotpose_MT2();
    telemetry.addData(caption: "Tx", llResult.getTx());
    telemetry.addData(caption: "Ty", llResult.getTy());
    telemetry.addData(caption: "Ta", llResult.getTa());
}

}
