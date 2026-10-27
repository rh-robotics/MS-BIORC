package org.firstinspires.ftc.teamcode.teleop.testing;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp(name="Servo Test", group="testing")
public class ServoTest extends OpMode {
    Servo testServo;
    public static double position = 0;

    @Override
    public void init() {
        testServo = hardwareMap.get(Servo.class, "testServo");
    }

    @Override
    public void loop() {
        testServo.setPosition(position);
    }
}