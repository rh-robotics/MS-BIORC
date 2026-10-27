package org.firstinspires.ftc.teamcode.teleop.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp(name="DcMotor Test", group="testing")
public class DcMotorTest extends OpMode {
    DcMotorEx testMotor;
    public static double power = 0;

    @Override
    public void init() {
        testMotor = hardwareMap.get(DcMotorEx.class, "testMotor");
    }

    @Override
    public void loop() {
        testMotor.setPower(power);
    }
}