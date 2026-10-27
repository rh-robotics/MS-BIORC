package org.firstinspires.ftc.teamcode.teleop.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name="CRServo Test", group="testing")
public class CRServoTest extends OpMode {
    CRServo testCRServo;
    public static double power = 1;

    @Override
    public void init() {
        testCRServo = hardwareMap.get(CRServo.class, "testServo");
    }

    @Override
    public void loop() {
        testCRServo.setPower(power);
    }
}